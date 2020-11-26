package cn.hutool.core.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;

/**
 * 描述: 包扫描工具类
 *
 * @author kfyty
 */
public abstract class PackageScanUtil {
    /**
     * 以基础类的包名为基础包名进行扫描
     * @param mainClass 基础类
     * @return 扫描到的类的全限定名
     * @throws IOException IO 异常
     */
    public static Set<String> scanClassName(Class<?> mainClass) throws IOException {
        return scanClassName(mainClass.getPackage().getName());
    }

    /**
     * 以基础类的包名为基础包名进行扫描
     * @param mainClass 基础类
     * @return 扫描到的类的 class 对象
     * @throws IOException IO 异常
     * @throws ClassNotFoundException 可以扫描到但加载失败，通常发生在依赖作用域为 provided 时
     */
    public static Set<Class<?>> scanClass(Class<?> mainClass) throws IOException, ClassNotFoundException {
        return scanClass(mainClass.getPackage().getName());
    }

    /**
     * 以基础包名进行扫描
     * @param basePackage 基础包名
     * @return 扫描到的类的全限定名
     * @throws IOException IO 异常
     */
    public static Set<String> scanClassName(String basePackage) throws IOException {
        Set<String> classes = new HashSet<>();
        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(basePackage.replace(CharUtil.DOT, CharUtil.SLASH));
        while(urls.hasMoreElements()) {
            URL url = urls.nextElement();
            if(URLUtil.URL_PROTOCOL_JAR.equals(url.getProtocol())) {
                classes.addAll(scanClassNameByJar(url));
                continue;
            }
            if(URLUtil.URL_PROTOCOL_FILE.equals(url.getProtocol())) {
                classes.addAll(scanClassNameByFile(url));
                continue;
            }
            throw new IllegalArgumentException("Protocol:[" + url.getProtocol() + "] not supported yet !");
        }
        return classes;
    }

    /**
     * 以基础包名进行扫描
     * @param basePackage 基础类
     * @return 扫描到的类的 class 对象
     * @throws IOException IO 异常
     * @throws ClassNotFoundException 可以扫描到但加载失败，通常发生在依赖作用域为 provided 时
     */
    public static Set<Class<?>> scanClass(String basePackage) throws IOException, ClassNotFoundException {
        Set<String> classes = scanClassName(basePackage);
        if(CollectionUtil.isEmpty(classes)) {
            return Collections.emptySet();
        }
        Set<Class<?>> result = new HashSet<>();
        for (String clazz : classes) {
            result.add(Class.forName(clazz));
        }
        return result;
    }

    /**
     * 从 jar 包中扫描
     * @param url jar 包的 url
     * @return 扫描到的类的全限定名
     * @throws IOException IO 异常
     */
    private static Set<String> scanClassNameByJar(URL url) throws IOException {
        Set<String> classes = new HashSet<>();
        String path = !url.getPath().contains(URLUtil.JAR_URL_SEPARATOR) ? url.getPath() : url.getPath().split(URLUtil.JAR_URL_SEPARATOR)[1];
        Enumeration<JarEntry> entries = ((JarURLConnection) url.openConnection()).getJarFile().entries();
        while(entries.hasMoreElements()) {
            String classPath = entries.nextElement().getName();
            if(!classPath.startsWith(path) || !classPath.endsWith(FileUtil.CLASS_EXT)) {
                continue;
            }
            classes.add(classPath.replace(CharUtil.SLASH, CharUtil.DOT).replace(FileUtil.CLASS_EXT, StrUtil.EMPTY));
        }
        return classes;
    }

    /**
     * 从项目文件中扫描
     * @param url 项目文件的 url
     * @return 扫描到的类的全限定名
     * @throws IOException IO 异常
     */
    private static Set<String> scanClassNameByFile(URL url) throws IOException {
        File[] files = new File(url.getPath()).listFiles();
        if(ArrayUtil.isEmpty(files)) {
            return Collections.emptySet();
        }
        Set<String> classes = new HashSet<>();
        for(File file : files) {
            if(file.isDirectory()) {
                classes.addAll(scanClassNameByFile(file.toURI().toURL()));
                continue;
            }
            if(!file.getPath().endsWith(FileUtil.CLASS_EXT)) {
                continue;
            }
            String classPath = file.getPath();
            classPath = classPath.substring(classPath.indexOf(File.separator + "classes") + 9, classPath.lastIndexOf(StrUtil.DOT));
            classes.add(classPath.replace(File.separator, StrUtil.DOT));
        }
        return classes;
    }
}
