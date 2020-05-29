package cn.hutool.core.lang;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.EnumerationIter;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类扫描器
 *
 * @author looly
 * @since 4.6.9
 */
public class ClassScanner implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 包名
	 */
	private final String packageName;
	/**
	 * 包名，最后跟一个点，表示包名，避免在检查前缀时的歧义
	 */
	private final String packageNameWithDot;
	/**
	 * 包路径，用于文件中对路径操作
	 */
	private final String packageDirName;
	/**
	 * 包路径，用于jar中对路径操作，在Linux下与packageDirName一致
	 */
	private final String packagePath;
	/**
	 * 过滤器
	 */
	private final Filter<Class<?>> classFilter;
	/**
	 * 编码
	 */
	private final Charset charset;
	/**
	 * 类加载器
	 */
	private ClassLoader classLoader;
	/**
	 * 是否初始化类
	 */
	private boolean initialize;
	/**
	 * 扫描结果集
	 */
	private final Set<Class<?>> classes = new HashSet<>();

	/**
	 * 扫描指定包路径下所有包含指定注解的类
	 *
	 * @param packageName     包路径
	 * @param annotationClass 注解类
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackageByAnnotation(String packageName, final Class<? extends Annotation> annotationClass) {
		return scanPackage(packageName, clazz -> clazz.isAnnotationPresent(annotationClass));
	}

	/**
	 * 扫描指定包路径下所有指定类或接口的子类或实现类，不包括指定父类本身
	 *
	 * @param packageName 包路径
	 * @param superClass  父类或接口（不包括）
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackageBySuper(String packageName, final Class<?> superClass) {
		return scanPackage(packageName, clazz -> superClass.isAssignableFrom(clazz) && !superClass.equals(clazz));
	}

	/**
	 * 扫描该包路径下所有class文件
	 *
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackage() {
		return scanPackage(StrUtil.EMPTY, null);
	}

	/**
	 * 扫描该包路径下所有class文件
	 *
	 * @param packageName 包路径 com | com. | com.abs | com.abs.
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackage(String packageName) {
		return scanPackage(packageName, null);
	}

	/**
	 * 扫描包路径下满足class过滤器条件的所有class文件，<br>
	 * 如果包路径为 com.abs + A.class 但是输入 abs会产生classNotFoundException<br>
	 * 因为className 应该为 com.abs.A 现在却成为abs.A,此工具类对该异常进行忽略处理<br>
	 *
	 * @param packageName 包路径 com | com. | com.abs | com.abs.
	 * @param classFilter class过滤器，过滤掉不需要的class
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackage(String packageName, Filter<Class<?>> classFilter) {
		return new ClassScanner(packageName, classFilter).scan();
	}

	/**
	 * 构造，默认UTF-8编码
	 */
	public ClassScanner() {
		this(null);
	}

	/**
	 * 构造，默认UTF-8编码
	 *
	 * @param packageName 包名，所有包传入""或者null
	 */
	public ClassScanner(String packageName) {
		this(packageName, null);
	}

	/**
	 * 构造，默认UTF-8编码
	 *
	 * @param packageName 包名，所有包传入""或者null
	 * @param classFilter 过滤器，无需传入null
	 */
	public ClassScanner(String packageName, Filter<Class<?>> classFilter) {
		this(packageName, classFilter, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 构造
	 *
	 * @param packageName 包名，所有包传入""或者null
	 * @param classFilter 过滤器，无需传入null
	 * @param charset     编码
	 */
	public ClassScanner(String packageName, Filter<Class<?>> classFilter, Charset charset) {
		packageName = StrUtil.nullToEmpty(packageName);
		this.packageName = packageName;
		this.packageNameWithDot = StrUtil.addSuffixIfNot(packageName, StrUtil.DOT);
		this.packageDirName = packageName.replace(CharUtil.DOT, File.separatorChar);
		this.packagePath = packageName.replace(CharUtil.DOT, CharUtil.SLASH);
		this.classFilter = classFilter;
		this.charset = charset;
	}

	/**
	 * 扫描包路径下满足class过滤器条件的所有class文件
	 *
	 * @return 类集合
	 */
	public Set<Class<?>> scan() {
		for (URL url : ResourceUtil.getResourceIter(this.packagePath)) {
			switch (url.getProtocol()) {
				case "file":
					scanFile(new File(URLUtil.decode(url.getFile(), this.charset.name())), null);
					break;
				case "jar":
					scanJar(URLUtil.getJarFile(url));
					break;
			}
		}

		if (CollUtil.isEmpty(this.classes)) {
			scanJavaClassPaths();
		}

		return Collections.unmodifiableSet(this.classes);
	}

	/**
	 * 设置是否在扫描到类时初始化类
	 *
	 * @param initialize 是否初始化类
	 */
	public void setInitialize(boolean initialize) {
		this.initialize = initialize;
	}

	/**
	 * 设置自定义的类加载器
	 *
	 * @param classLoader 类加载器
	 * @since 4.6.9
	 */
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	// --------------------------------------------------------------------------------------------------- Private method start

	/**
	 * 扫描Java指定的ClassPath路径
	 */
	private void scanJavaClassPaths() {
		final String[] javaClassPaths = ClassUtil.getJavaClassPaths();
		for (String classPath : javaClassPaths) {
			// bug修复，由于路径中空格和中文导致的Jar找不到
			classPath = URLUtil.decode(classPath, CharsetUtil.systemCharsetName());

			scanFile(new File(classPath), null);
		}
	}

	/**
	 * 扫描文件或目录中的类
	 *
	 * @param file    文件或目录
	 * @param rootDir 包名对应classpath绝对路径
	 */
	private void scanFile(File file, String rootDir) {
		if (file.isFile()) {
			final String fileName = file.getAbsolutePath();
			if (fileName.endsWith(FileUtil.CLASS_EXT)) {
				final String className = fileName//
						// 8为classes长度，fileName.length() - 6为".class"的长度
						.substring(rootDir.length(), fileName.length() - 6)//
						.replace(File.separatorChar, CharUtil.DOT);//
				//加入满足条件的类
				addIfAccept(className);
			} else if (fileName.endsWith(FileUtil.JAR_FILE_EXT)) {
				try {
					scanJar(new JarFile(file));
				} catch (IOException e) {
					throw new IORuntimeException(e);
				}
			}
		} else if (file.isDirectory()) {
			final File[] files = file.listFiles();
			if(null != files){
				for (File subFile : files) {
					scanFile(subFile, (null == rootDir) ? subPathBeforePackage(file) : rootDir);
				}
			}
		}
	}

	/**
	 * 扫描jar包
	 *
	 * @param jar jar包
	 */
	private void scanJar(JarFile jar) {
		String name;
		for (JarEntry entry : new EnumerationIter<>(jar.entries())) {
			name = StrUtil.removePrefix(entry.getName(), StrUtil.SLASH);
			if (name.startsWith(this.packagePath)) {
				if (name.endsWith(FileUtil.CLASS_EXT) && false == entry.isDirectory()) {
					final String className = name//
							.substring(0, name.length() - 6)//
							.replace(CharUtil.SLASH, CharUtil.DOT);//
					addIfAccept(loadClass(className));
				}
			}
		}
	}

	/**
	 * 加载类
	 *
	 * @param className 类名
	 * @return 加载的类
	 */
	private Class<?> loadClass(String className) {
		ClassLoader loader = this.classLoader;
		if (null == loader) {
			loader = ClassLoaderUtil.getClassLoader();
			this.classLoader = loader;
		}

		Class<?> clazz = null;
		try {
			clazz = Class.forName(className, this.initialize, loader);
		} catch (NoClassDefFoundError e) {
			// 由于依赖库导致的类无法加载，直接跳过此类
		} catch (UnsupportedClassVersionError e) {
			// 版本导致的不兼容的类，跳过
		} catch (Exception e) {
			throw new RuntimeException(e);
			// Console.error(e);
		}
		return clazz;
	}

	/**
	 * 通过过滤器，是否满足接受此类的条件
	 *
	 * @param className 类名
	 */
	private void addIfAccept(String className) {
		if (StrUtil.isBlank(className)) {
			return;
		}
		int classLen = className.length();
		int packageLen = this.packageName.length();
		if (classLen == packageLen) {
			//类名和包名长度一致，用户可能传入的包名是类名
			if (className.equals(this.packageName)) {
				addIfAccept(loadClass(className));
			}
		} else if (classLen > packageLen) {
			//检查类名是否以指定包名为前缀，包名后加.（避免类似于cn.hutool.A和cn.hutool.ATest这类类名引起的歧义）
			if (className.startsWith(this.packageNameWithDot)) {
				addIfAccept(loadClass(className));
			}
		}
	}

	/**
	 * 通过过滤器，是否满足接受此类的条件
	 *
	 * @param clazz 类
	 */
	private void addIfAccept(Class<?> clazz) {
		if (null != clazz) {
			Filter<Class<?>> classFilter = this.classFilter;
			if (classFilter == null || classFilter.accept(clazz)) {
				this.classes.add(clazz);
			}
		}
	}

	/**
	 * 截取文件绝对路径中包名之前的部分
	 *
	 * @param file 文件
	 * @return 包名之前的部分
	 */
	private String subPathBeforePackage(File file) {
		String filePath = file.getAbsolutePath();
		if (StrUtil.isNotEmpty(this.packageDirName)) {
			filePath = StrUtil.subBefore(filePath, this.packageDirName, true);
		}
		return StrUtil.addSuffixIfNot(filePath, File.separator);
	}
	// --------------------------------------------------------------------------------------------------- Private method end
}
