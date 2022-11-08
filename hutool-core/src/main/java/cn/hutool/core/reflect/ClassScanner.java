package cn.hutool.core.reflect;

import cn.hutool.core.classloader.ClassLoaderUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.iter.EnumerationIter;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.net.url.URLDecoder;
import cn.hutool.core.net.url.URLUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.SystemUtil;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
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
	 * 包名，最后跟一个点，表示包名，避免在检查前缀时的歧义<br>
	 * 如果包名指定为空，不跟点
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
	private final Predicate<Class<?>> classPredicate;
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
	 * 扫描指定包路径下所有包含指定注解的类，包括其他加载的jar或者类
	 *
	 * @param packageName     包路径
	 * @param annotationClass 注解类
	 * @return 类集合
	 */
	public static Set<Class<?>> scanAllPackageByAnnotation(final String packageName, final Class<? extends Annotation> annotationClass) {
		return scanAllPackage(packageName, clazz -> clazz.isAnnotationPresent(annotationClass));
	}

	/**
	 * 扫描指定包路径下所有包含指定注解的类<br>
	 * 如果classpath下已经有类，不再扫描其他加载的jar或者类
	 *
	 * @param packageName     包路径
	 * @param annotationClass 注解类
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackageByAnnotation(final String packageName, final Class<? extends Annotation> annotationClass) {
		return scanPackage(packageName, clazz -> clazz.isAnnotationPresent(annotationClass));
	}

	/**
	 * 扫描指定包路径下所有指定类或接口的子类或实现类，不包括指定父类本身，包括其他加载的jar或者类
	 *
	 * @param packageName 包路径
	 * @param superClass  父类或接口（不包括）
	 * @return 类集合
	 */
	public static Set<Class<?>> scanAllPackageBySuper(final String packageName, final Class<?> superClass) {
		return scanAllPackage(packageName, clazz -> superClass.isAssignableFrom(clazz) && !superClass.equals(clazz));
	}

	/**
	 * 扫描指定包路径下所有指定类或接口的子类或实现类，不包括指定父类本身<br>
	 * 如果classpath下已经有类，不再扫描其他加载的jar或者类
	 *
	 * @param packageName 包路径
	 * @param superClass  父类或接口（不包括）
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackageBySuper(final String packageName, final Class<?> superClass) {
		return scanPackage(packageName, clazz -> superClass.isAssignableFrom(clazz) && !superClass.equals(clazz));
	}

	/**
	 * 扫描该包路径下所有class文件，包括其他加载的jar或者类
	 *
	 * @return 类集合
	 * @since 5.7.5
	 */
	public static Set<Class<?>> scanAllPackage() {
		return scanAllPackage(StrUtil.EMPTY, null);
	}

	/**
	 * 扫描classpath下所有class文件，如果classpath下已经有类，不再扫描其他加载的jar或者类
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
	public static Set<Class<?>> scanPackage(final String packageName) {
		return scanPackage(packageName, null);
	}

	/**
	 * 扫描包路径下和所有在classpath中加载的类，满足class过滤器条件的所有class文件，<br>
	 * 如果包路径为 com.abs + A.class 但是输入 abs会产生classNotFoundException<br>
	 * 因为className 应该为 com.abs.A 现在却成为abs.A,此工具类对该异常进行忽略处理<br>
	 *
	 * @param packageName 包路径 com | com. | com.abs | com.abs.
	 * @param classFilter class过滤器，过滤掉不需要的class
	 * @return 类集合
	 * @since 5.7.5
	 */
	public static Set<Class<?>> scanAllPackage(final String packageName, final Predicate<Class<?>> classFilter) {
		return new ClassScanner(packageName, classFilter).scan(true);
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
	public static Set<Class<?>> scanPackage(final String packageName, final Predicate<Class<?>> classFilter) {
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
	public ClassScanner(final String packageName) {
		this(packageName, null);
	}

	/**
	 * 构造，默认UTF-8编码
	 *
	 * @param packageName 包名，所有包传入""或者null
	 * @param classPredicate 过滤器，无需传入null
	 */
	public ClassScanner(final String packageName, final Predicate<Class<?>> classPredicate) {
		this(packageName, classPredicate, CharsetUtil.UTF_8);
	}

	/**
	 * 构造
	 *
	 * @param packageName 包名，所有包传入""或者null
	 * @param classPredicate 过滤器，无需传入null
	 * @param charset     编码
	 */
	public ClassScanner(String packageName, final Predicate<Class<?>> classPredicate, final Charset charset) {
		packageName = StrUtil.emptyIfNull(packageName);
		this.packageName = packageName;
		this.packageNameWithDot = StrUtil.addSuffixIfNot(packageName, StrUtil.DOT);
		this.packageDirName = packageName.replace(CharUtil.DOT, File.separatorChar);
		this.packagePath = packageName.replace(CharUtil.DOT, CharUtil.SLASH);
		this.classPredicate = classPredicate;
		this.charset = charset;
	}

	/**
	 * 扫描包路径下满足class过滤器条件的所有class文件<br>
	 * 此方法首先扫描指定包名下的资源目录，如果未扫描到，则扫描整个classpath中所有加载的类
	 *
	 * @return 类集合
	 */
	public Set<Class<?>> scan() {
		return scan(false);
	}

	/**
	 * 扫描包路径下满足class过滤器条件的所有class文件
	 *
	 * @param forceScanJavaClassPaths 是否强制扫描其他位于classpath关联jar中的类
	 * @return 类集合
	 * @since 5.7.5
	 */
	public Set<Class<?>> scan(final boolean forceScanJavaClassPaths) {
		for (final URL url : ResourceUtil.getResourceUrlIter(this.packagePath)) {
			switch (url.getProtocol()) {
				case "file":
					scanFile(new File(URLDecoder.decode(url.getFile(), this.charset)), null);
					break;
				case "jar":
					scanJar(URLUtil.getJarFile(url));
					break;
			}
		}

		// classpath下未找到，则扫描其他jar包下的类
		if (forceScanJavaClassPaths || CollUtil.isEmpty(this.classes)) {
			scanJavaClassPaths();
		}

		return Collections.unmodifiableSet(this.classes);
	}

	/**
	 * 设置是否在扫描到类时初始化类
	 *
	 * @param initialize 是否初始化类
	 */
	public void setInitialize(final boolean initialize) {
		this.initialize = initialize;
	}

	/**
	 * 设置自定义的类加载器
	 *
	 * @param classLoader 类加载器
	 * @since 4.6.9
	 */
	public void setClassLoader(final ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	// --------------------------------------------------------------------------------------------------- Private method start

	/**
	 * 扫描Java指定的ClassPath路径
	 */
	private void scanJavaClassPaths() {
		final String[] javaClassPaths = SystemUtil.getJavaClassPaths();
		for (String classPath : javaClassPaths) {
			// bug修复，由于路径中空格和中文导致的Jar找不到
			classPath = URLDecoder.decode(classPath, CharsetUtil.defaultCharset());

			scanFile(new File(classPath), null);
		}
	}

	/**
	 * 扫描文件或目录中的类
	 *
	 * @param file    文件或目录
	 * @param rootDir 包名对应classpath绝对路径
	 */
	private void scanFile(final File file, final String rootDir) {
		if (file.isFile()) {
			final String fileName = file.getAbsolutePath();
			if (fileName.endsWith(FileNameUtil.EXT_CLASS)) {
				final String className = fileName//
						// 8为classes长度，fileName.length() - 6为".class"的长度
						.substring(rootDir.length(), fileName.length() - 6)//
						.replace(File.separatorChar, CharUtil.DOT);//
				//加入满足条件的类
				addIfAccept(className);
			} else if (fileName.endsWith(FileNameUtil.EXT_JAR)) {
				try {
					scanJar(new JarFile(file));
				} catch (final IOException e) {
					throw new IORuntimeException(e);
				}
			}
		} else if (file.isDirectory()) {
			final File[] files = file.listFiles();
			if (null != files) {
				for (final File subFile : files) {
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
	private void scanJar(final JarFile jar) {
		String name;
		for (final JarEntry entry : new EnumerationIter<>(jar.entries())) {
			name = StrUtil.removePrefix(entry.getName(), StrUtil.SLASH);
			if (StrUtil.isEmpty(packagePath) || name.startsWith(this.packagePath)) {
				if (name.endsWith(FileNameUtil.EXT_CLASS) && false == entry.isDirectory()) {
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
	protected Class<?> loadClass(final String className) {
		ClassLoader loader = this.classLoader;
		if (null == loader) {
			loader = ClassLoaderUtil.getClassLoader();
			this.classLoader = loader;
		}

		Class<?> clazz = null;
		try {
			clazz = Class.forName(className, this.initialize, loader);
		} catch (final NoClassDefFoundError | ClassNotFoundException e) {
			// 由于依赖库导致的类无法加载，直接跳过此类
		} catch (final UnsupportedClassVersionError e) {
			// 版本导致的不兼容的类，跳过
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
		return clazz;
	}

	/**
	 * 通过过滤器，是否满足接受此类的条件
	 *
	 * @param className 类名
	 */
	private void addIfAccept(final String className) {
		if (StrUtil.isBlank(className)) {
			return;
		}
		final int classLen = className.length();
		final int packageLen = this.packageName.length();
		if (classLen == packageLen) {
			//类名和包名长度一致，用户可能传入的包名是类名
			if (className.equals(this.packageName)) {
				addIfAccept(loadClass(className));
			}
		} else if (classLen > packageLen) {
			//检查类名是否以指定包名为前缀，包名后加.（避免类似于cn.hutool.A和cn.hutool.ATest这类类名引起的歧义）
			if (".".equals(this.packageNameWithDot) || className.startsWith(this.packageNameWithDot)) {
				addIfAccept(loadClass(className));
			}
		}
	}

	/**
	 * 通过过滤器，是否满足接受此类的条件
	 *
	 * @param clazz 类
	 */
	private void addIfAccept(final Class<?> clazz) {
		if (null != clazz) {
			final Predicate<Class<?>> classFilter = this.classPredicate;
			if (classFilter == null || classFilter.test(clazz)) {
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
	private String subPathBeforePackage(final File file) {
		String filePath = file.getAbsolutePath();
		if (StrUtil.isNotEmpty(this.packageDirName)) {
			filePath = StrUtil.subBefore(filePath, this.packageDirName, true);
		}
		return StrUtil.addSuffixIfNot(filePath, File.separator);
	}
	// --------------------------------------------------------------------------------------------------- Private method end
}
