package cn.hutool.core.compiler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.URLUtil;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Java 源码编译器
 *
 * @author lzpeng
 */
public class JavaSourceCompiler {

	/**
	 * 待编译的文件 可以是 .java文件 压缩文件 文件夹 递归搜索文件夹内的zip包和jar包
	 */
	private final List<File> sourceFileList = new ArrayList<>();

	/**
	 * 编译时需要加入classpath中的文件 可以是 压缩文件 文件夹递归搜索文件夹内的zip包和jar包
	 */
	private final List<File> libraryFileList = new ArrayList<>();

	/**
	 * 源码映射 key: 类名 value: 类源码
	 */
	private final Map<String, String> sourceCodeMap = new LinkedHashMap<>();

	/**
	 * 编译类时使用的父类加载器
	 */
	private final ClassLoader parentClassLoader;


	/**
	 * 构造
	 *
	 * @param parent 父类加载器，null则使用默认类加载器
	 */
	private JavaSourceCompiler(ClassLoader parent) {
		this.parentClassLoader = ObjectUtil.defaultIfNull(parent, ClassLoaderUtil.getClassLoader());
	}


	/**
	 * 创建Java源码编译器
	 *
	 * @param parent 父类加载器
	 * @return Java源码编译器
	 */
	public static JavaSourceCompiler create(ClassLoader parent) {
		return new JavaSourceCompiler(parent);
	}


	/**
	 * 向编译器中加入待编译的文件 支持 .java, 文件夹, 压缩文件 递归搜索文件夹内的压缩文件和jar包
	 *
	 * @param files 待编译的文件 支持 .java, 文件夹, 压缩文件 递归搜索文件夹内的压缩文件和jar包
	 * @return Java源码编译器
	 */
	public JavaSourceCompiler addSource(final File... files) {
		if (ArrayUtil.isNotEmpty(files)) {
			this.sourceFileList.addAll(Arrays.asList(files));
		}
		return this;
	}

	/**
	 * 向编译器中加入待编译的源码Map
	 *
	 * @param sourceCodeMap 源码Map key: 类名 value 源码
	 * @return Java源码编译器
	 */
	public JavaSourceCompiler addSource(final Map<String, String> sourceCodeMap) {
		if (MapUtil.isNotEmpty(sourceCodeMap)) {
			this.sourceCodeMap.putAll(sourceCodeMap);
		}
		return this;
	}

	/**
	 * 加入编译Java源码时所需要的jar包
	 *
	 * @param files 编译Java源码时所需要的jar包
	 * @return Java源码编译器
	 */
	public JavaSourceCompiler addLibrary(final File... files) {
		if (ArrayUtil.isNotEmpty(files)) {
			this.libraryFileList.addAll(Arrays.asList(files));
		}
		return this;
	}

	/**
	 * 向编译器中加入待编译的源码Map
	 *
	 * @param className  类名
	 * @param sourceCode 源码
	 * @return Java文件编译器
	 */
	public JavaSourceCompiler addSource(final String className, final String sourceCode) {
		if (className != null && sourceCode != null) {
			this.sourceCodeMap.put(className, sourceCode);
		}
		return this;
	}

	/**
	 * 编译所有文件并返回类加载器
	 *
	 * @return 类加载器
	 */
	public ClassLoader compile() {
		// 获得classPath
		final List<File> classPath = getClassPath();
		final URL[] urLs = URLUtil.getURLs(classPath.toArray(new File[0]));
		final URLClassLoader ucl = URLClassLoader.newInstance(urLs, this.parentClassLoader);
		if (sourceCodeMap.isEmpty() && sourceFileList.isEmpty()) {
			// 没有需要编译的源码
			return ucl;
		}

		// 没有需要编译的源码文件返回加载zip或jar包的类加载器

		// 创建编译器
		final JavaFileManager javaFileManager = new JavaClassFileManager(ucl, CompilerUtil.getFileManager());

		// classpath
		final List<String> options = new ArrayList<>();
		if (false == classPath.isEmpty()) {
			final List<String> cp = classPath.stream().map(File::getAbsolutePath).collect(Collectors.toList());
			options.add("-cp");
			options.addAll(cp);
		}

		// 编译文件
		final DiagnosticCollector<? super JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();
		final List<JavaFileObject> javaFileObjectList = getJavaFileObject();
		final CompilationTask task = CompilerUtil.getTask(javaFileManager, diagnosticCollector, options, javaFileObjectList);
		if (task.call()) {
			// 加载编译后的类
			return javaFileManager.getClassLoader(StandardLocation.CLASS_OUTPUT);
		} else {
			// 编译失败,收集错误信息
			throw new CompilerException(DiagnosticUtil.getMessages(diagnosticCollector));
		}
	}

	/**
	 * 获得编译源码时需要的classpath
	 *
	 * @return 编译源码时需要的classpath
	 */
	private List<File> getClassPath() {
		List<File> classPathFileList = new ArrayList<>();
		for (File file : libraryFileList) {
			List<File> jarOrZipFile = FileUtil.loopFiles(file, (subFile)-> JavaFileObjectUtil.isJarOrZipFile(subFile.getName()));
			classPathFileList.addAll(jarOrZipFile);
			if (file.isDirectory()) {
				classPathFileList.add(file);
			}
		}
		return classPathFileList;
	}

	/**
	 * 获得待编译的Java文件对象
	 *
	 * @return 待编译的Java文件对象
	 */
	private List<JavaFileObject> getJavaFileObject() {
		final List<JavaFileObject> collection = new ArrayList<>();

		// 源码文件
		for (File file : sourceFileList) {
			FileUtil.walkFiles(file, (subFile)-> collection.addAll(JavaFileObjectUtil.getJavaFileObjects(file)));
		}

		// 源码Map
		collection.addAll(getJavaFileObjectByMap(this.sourceCodeMap));
		return collection;
	}

	/**
	 * 通过源码Map获得Java文件对象
	 *
	 * @param sourceCodeMap 源码Map
	 * @return Java文件对象集合
	 */
	private Collection<JavaFileObject> getJavaFileObjectByMap(final Map<String, String> sourceCodeMap) {
		if (MapUtil.isNotEmpty(sourceCodeMap)) {
			return sourceCodeMap.entrySet().stream()
					.map(entry -> new JavaSourceFileObject(entry.getKey(), entry.getValue(), CharsetUtil.CHARSET_UTF_8))
					.collect(Collectors.toList());
		}
		return Collections.emptySet();
	}

	/**
	 * 通过.java文件创建Java文件对象
	 *
	 * @param file .java文件
	 * @return Java文件对象
	 */
	private JavaFileObject getJavaFileObjectByJavaFile(final File file) {
		return new JavaSourceFileObject(file.toURI());
	}

}
