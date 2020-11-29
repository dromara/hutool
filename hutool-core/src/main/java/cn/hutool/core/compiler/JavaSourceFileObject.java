package cn.hutool.core.compiler;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.URLUtil;

import javax.tools.SimpleJavaFileObject;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;

/**
 * Java 源码文件对象，支持：<br>
 * <ol>
 *     <li>源文件，通过文件的uri传入</li>
 *     <li>代码内容，通过流传入</li>
 * </ol>
 *
 * @author lzpeng
 * @since 5.5.2
 */
class JavaSourceFileObject extends SimpleJavaFileObject {

	/**
	 * 输入流
	 */
	private InputStream inputStream;

	/**
	 * 构造，支持File等路径类型的源码
	 *
	 * @param uri  需要编译的文件uri
	 */
	protected JavaSourceFileObject(URI uri) {
		super(uri, Kind.SOURCE);
	}

	/**
	 * 构造，支持String类型的源码
	 *
	 * @param className 需要编译的类名
	 * @param code      需要编译的类源码
	 */
	protected JavaSourceFileObject(String className, String code, Charset charset) {
		this(className, IoUtil.toStream(code, charset));
	}

	/**
	 * 构造，支持流中读取源码（例如zip或网络等）
	 *
	 * @param name        需要编译的文件名
	 * @param inputStream 输入流
	 */
	protected JavaSourceFileObject(String name, InputStream inputStream) {
		this(URLUtil.getStringURI(name.replace(CharUtil.DOT, CharUtil.SLASH) + Kind.SOURCE.extension));
		this.inputStream = inputStream;
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
	 */
	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
		try(final InputStream in = openInputStream()){
			return IoUtil.readUtf8(in);
		}
	}

}