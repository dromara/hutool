package cn.hutool.core.compiler;

import cn.hutool.core.io.IoUtil;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import java.io.InputStream;
import java.security.SecureClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * Java 字节码文件对象
 * 正常我们使用javac命令编译源码时会将class文件写入到磁盘中，但在运行时动态编译类不适合保存在磁盘中
 * 我们采取此对象来管理运行时动态编译类生成的字节码
 *
 * @author lzpeng
 * @see JavaSourceCompilerBak#compile()
 * @see com.sun.tools.javac.api.ClientCodeWrapper.WrappedJavaFileManager#getJavaFileForOutput(javax.tools.JavaFileManager.Location, java.lang.String, javax.tools.JavaFileObject.Kind, javax.tools.FileObject)
 */
final class JavaClassFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    /**
     * 存储java字节码文件对象映射
     */
    private final Map<String, JavaFileObject> javaFileObjectMap = new HashMap<>();

    /**
     * 加载动态编译生成类的父类加载器
     */
    private final ClassLoader parent;

    /**
     * 构造
     *
     * @param parent      父类加载器
     * @param fileManager 字节码文件管理器
     * @see JavaSourceCompilerBak#compile()
     */
    protected JavaClassFileManager(final ClassLoader parent, final JavaFileManager fileManager) {
        super(fileManager);
        if (parent == null) {
            this.parent = Thread.currentThread().getContextClassLoader();
        } else {
            this.parent = parent;
        }
    }

    /**
     * 获得动态编译生成的类的类加载器
     *
     * @param location 源码位置
     * @return 动态编译生成的类的类加载器
     * @see JavaSourceCompilerBak#compile()
     */
    @Override
    public ClassLoader getClassLoader(final Location location) {
        return new SecureClassLoader(parent) {

            /**
             * 查找类
             * @param name 类名
             * @return 类的class对象
             * @throws ClassNotFoundException 未找到类异常
             */
            @Override
            protected Class<?> findClass(final String name) throws ClassNotFoundException {
                final JavaFileObject javaFileObject = javaFileObjectMap.get(name);
                if (javaFileObject != null) {
                    try {
                        final InputStream inputStream = javaFileObject.openInputStream();
                        final byte[] bytes = IoUtil.readBytes(inputStream);
                        final Class<?> c = defineClass(name, bytes, 0, bytes.length);
                        return c;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                throw new ClassNotFoundException(name);
            }
        };
    }

    /**
     * 获得Java字节码文件对象
     * 编译器编译源码时会将Java源码对象编译转为Java字节码对象
     *
     * @param location  源码位置
     * @param className 类名
     * @param kind      文件类型
     * @param sibling   将Java源码对象
     * @return Java字节码文件对象
     * @see com.sun.tools.javac.api.lientCodeWrapper.WrappedJavaFileManager#getJavaFileForOutput(javax.tools.JavaFileManager.Location, java.lang.String, javax.tools.JavaFileObject.Kind, javax.tools.FileObject)
     */
    @Override
    public JavaFileObject getJavaFileForOutput(final Location location, final String className, final Kind kind, final FileObject sibling) {
        final JavaFileObject javaFileObject = new JavaClassFileObject(className, kind);
        javaFileObjectMap.put(className, javaFileObject);
        return javaFileObject;
    }

}
