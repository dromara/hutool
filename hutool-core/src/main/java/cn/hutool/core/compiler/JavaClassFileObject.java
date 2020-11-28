package cn.hutool.core.compiler;


import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * Java 字节码文件对象
 *
 * @author lzpeng
 * @see JavaClassFileManager#getClassLoader(javax.tools.JavaFileManager.Location
 * @see JavaClassFileManager#getJavaFileForOutput(javax.tools.JavaFileManager.Location, java.lang.String, javax.tools.JavaFileObject.Kind, javax.tools.FileObject)
 * @since 5.5.2
 */
final class JavaClassFileObject extends SimpleJavaFileObject {

	/**
	 * 字节码输出流
	 */
	private final ByteArrayOutputStream byteArrayOutputStream;

	/**
	 * 构造
	 *
	 * @param className 需要编译的类名
	 * @param kind      需要编译的文件类型
	 * @see JavaClassFileManager#getJavaFileForOutput(javax.tools.JavaFileManager.Location, java.lang.String, javax.tools.JavaFileObject.Kind, javax.tools.FileObject)
	 */
	protected JavaClassFileObject(final String className, final Kind kind) {
		super(URI.create("string:///" + className.replaceAll("\\.", "/") + kind.extension), kind);
		this.byteArrayOutputStream = new ByteArrayOutputStream();
	}

	/**
	 * 获得字节码输入流
	 * 编译器编辑源码后，我们将通过此输出流获得编译后的字节码，以便运行时加载类
	 *
	 * @return 字节码输入流
	 * @see JavaClassFileManager#getClassLoader(javax.tools.JavaFileManager.Location)
	 */
	@Override
	public InputStream openInputStream() {
		return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
	}

	/**
	 * 获得字节码输出流
	 * 编译器编辑源码时，会将编译结果输出到本输出流中
	 *
	 * @return 字节码输出流
	 */
	@Override
	public OutputStream openOutputStream() {
		return this.byteArrayOutputStream;
	}

}