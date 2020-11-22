package cn.hutool.core.compiler;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;

import javax.tools.SimpleJavaFileObject;

import cn.hutool.core.io.IoUtil;

/**
 * Java 源码文件对象
 *
 * @author lzpeng
 * @see JavaSourceCompilerBak#getJavaFileObjectByJavaFile(java.io.File)
 * @see JavaSourceCompilerBak#getJavaFileObjectByZipOrJarFile(java.io.File)
 * @see JavaSourceCompilerBak#getJavaFileObject(java.util.Map)
 * @see com.sun.tools.javac.api.ClientCodeWrapper.WrappedFileObject#getCharContent(boolean)
 */
final class JavaSourceFileObject extends SimpleJavaFileObject {

    /**
     * 输入流
     */
    private InputStream inputStream;

    /**
     * 构造
     *
     * @param uri  需要编译的文件uri
     * @param kind 需要编译的文件类型
     * @see JavaSourceCompilerBak#getJavaFileObjectByJavaFile(java.io.File)
     */
    protected JavaSourceFileObject(URI uri, Kind kind) {
        super(uri, kind);
    }

    /**
     * 构造
     *
     * @param name        需要编译的文件名
     * @param inputStream 输入流
     * @param kind        需要编译的文件类型
     * @see JavaSourceCompilerBak#getJavaFileObjectByZipOrJarFile(java.io.File)
     */
    protected JavaSourceFileObject(final String name, final InputStream inputStream, final Kind kind) {
        super(URI.create("string:///" + name), kind);
        this.inputStream = inputStream;
    }

    /**
     * 构造
     *
     * @param className 需要编译的类名
     * @param code      需要编译的类源码
     * @param kind      需要编译的文件类型
     * @see JavaSourceCompilerBak#getJavaFileObject(java.util.Map)
     */
    protected JavaSourceFileObject(final String className, final String code, final Kind kind) {
        super(URI.create("string:///" + className.replaceAll("\\.", "/") + kind.extension), kind);
        this.inputStream = new ByteArrayInputStream(code.getBytes());
    }

    /**
     * 获得类源码的输入流
     *
     * @return 类源码的输入流
     * @throws IOException IO 异常
     */
    @Override
    public InputStream openInputStream() throws IOException {
        if (inputStream == null) {
            inputStream = toUri().toURL().openStream();
        }
        return new BufferedInputStream(inputStream);
    }

    /**
     * 获得类源码
     * 编译器编辑源码前，会通过此方法获取类的源码
     *
     * @param ignoreEncodingErrors 是否忽略编码错误
     * @return 需要编译的类的源码
     * @throws IOException IO异常
     * @see com.sun.tools.javac.api.ClientCodeWrapper.WrappedFileObject#getCharContent(boolean)
     */
    @Override
    public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws IOException {
        final InputStream in = openInputStream();
        final String code = IoUtil.read(in, Charset.defaultCharset());
        IoUtil.close(in);
        return code;
    }

}