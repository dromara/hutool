package cn.hutool.core.compiler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.URLUtil;

import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject.Kind;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Java 源码编译器
 *
 * @author lzpeng
 */
public final class JavaSourceCompiler {

    /**
     * java 编译器
     */
    private static final JavaCompiler JAVA_COMPILER = ToolProvider.getSystemJavaCompiler();

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
     * @param parent 父类加载器
     */
    private JavaSourceCompiler(ClassLoader parent) {
        this.parentClassLoader = parent;
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
        final ClassLoader parent;
        if (this.parentClassLoader == null) {
            parent = Thread.currentThread().getContextClassLoader();
        } else {
            parent = this.parentClassLoader;
        }
        // 获得classPath
        final List<File> classPath = getClassPath();
        final URL[] urLs = URLUtil.getURLs(classPath.toArray(new File[0]));
        final URLClassLoader ucl = URLClassLoader.newInstance(urLs, parent);
        if (sourceCodeMap.isEmpty() && sourceFileList.isEmpty()) {
            // 没有需要编译的源码
            return ucl;
        }
        // 没有需要编译的源码文件返回加载zip或jar包的类加载器
        final Iterable<JavaFileObject> javaFileObjectList = getJavaFileObject();
        // 创建编译器
        final JavaFileManager standardJavaFileManager = JAVA_COMPILER.getStandardFileManager(null, null, null);
        final JavaFileManager javaFileManager = new JavaClassFileManager(ucl, standardJavaFileManager);
        final DiagnosticCollector<? super JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();
        final List<String> options = new ArrayList<>();
        if (!classPath.isEmpty()) {
            final List<String> cp = classPath.stream().map(File::getAbsolutePath).collect(Collectors.toList());
            options.add("-cp");
            options.addAll(cp);
        }
        // 编译文件
        final CompilationTask task = JAVA_COMPILER.getTask(null, javaFileManager, diagnosticCollector,
                options, null, javaFileObjectList);
        final Boolean result = task.call();
        if (Boolean.TRUE.equals(result)) {
            return javaFileManager.getClassLoader(StandardLocation.CLASS_OUTPUT);
        } else {
            // 编译失败,收集错误信息
            final List<?> diagnostics = diagnosticCollector.getDiagnostics();
            final String errorMsg = diagnostics.stream().map(String::valueOf)
                    .collect(Collectors.joining(System.lineSeparator()));
            // CompileException
            throw new RuntimeException(errorMsg);
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
            List<File> jarOrZipFile = FileUtil.loopFiles(file, this::isJarOrZipFile);
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
    private Iterable<JavaFileObject> getJavaFileObject() {
        final Collection<JavaFileObject> collection = new ArrayList<>();
        for (File file : sourceFileList) {
            // .java 文件
            final List<File> javaFileList = FileUtil.loopFiles(file, this::isJavaFile);
            for (File javaFile : javaFileList) {
                collection.add(getJavaFileObjectByJavaFile(javaFile));
            }
            // 压缩包
            final List<File> jarOrZipFileList = FileUtil.loopFiles(file, this::isJarOrZipFile);
            for (File jarOrZipFile : jarOrZipFileList) {
                collection.addAll(getJavaFileObjectByZipOrJarFile(jarOrZipFile));
            }
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
                    .map(entry -> new JavaSourceFileObject(entry.getKey(), entry.getValue(), Kind.SOURCE))
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
        return new JavaSourceFileObject(file.toURI(), Kind.SOURCE);
    }

    /**
     * 通过zip包或jar包创建Java文件对象
     *
     * @param file 压缩文件
     * @return Java文件对象
     */
    private Collection<JavaFileObject> getJavaFileObjectByZipOrJarFile(final File file) {
        final Collection<JavaFileObject> collection = new ArrayList<>();
        try {
            final ZipFile zipFile = new ZipFile(file);
            final Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                final ZipEntry zipEntry = entries.nextElement();
                final String name = zipEntry.getName();
                if (name.endsWith(".java")) {
                    final InputStream inputStream = zipFile.getInputStream(zipEntry);
                    final JavaSourceFileObject fileObject = new JavaSourceFileObject(name, inputStream, Kind.SOURCE);
                    collection.add(fileObject);
                }
            }
            return collection;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }


    /**
     * 是否是jar 或 zip 文件
     *
     * @param file 文件
     * @return 是否是jar 或 zip 文件
     */
    private boolean isJarOrZipFile(final File file) {
        final String fileName = file.getName();
        return fileName.endsWith(".jar") || fileName.endsWith(".zip");
    }

    /**
     * 是否是.java文件
     *
     * @param file 文件
     * @return 是否是.java文件
     */
    private boolean isJavaFile(final File file) {
        final String fileName = file.getName();
        return fileName.endsWith(".java");
    }

}
