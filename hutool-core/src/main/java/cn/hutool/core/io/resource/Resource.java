package cn.hutool.core.io.resource;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * 资源接口定义<br>
 * <p>资源是数据表示的统称，我们可以将任意的数据封装为一个资源，然后读取其内容。</p>
 * <p>资源可以是文件、URL、ClassPath中的文件亦或者jar(zip)包中的文件。</p>
 * <p>
 *     提供资源接口的意义在于，我们可以使用一个方法接收任意类型的数据，从而处理数据，
 *     无需专门针对File、InputStream等写多个重载方法，同时也为更好的扩展提供了可能。
 * </p>
 * <p>使用非常简单，假设我们需要从classpath中读取一个xml，我们不用关心这个文件在目录中还是在jar中：</p>
 * <pre>
 *     Resource resource = new ClassPathResource("test.xml");
 *     String xmlStr = resource.readUtf8Str();
 * </pre>
 * <p>同样，我们可以自己实现Resource接口，按照业务需要从任意位置读取数据，比如从数据库中。</p>
 *
 * @author looly
 * @since 3.2.1
 */
public interface Resource {

	/**
	 * 获取资源名，例如文件资源的资源名为文件名
	 *
	 * @return 资源名
	 * @since 4.0.13
	 */
	String getName();

	/**
	 * 获得解析后的{@link URL}，无对应URL的返回{@code null}
	 *
	 * @return 解析后的{@link URL}
	 */
	URL getUrl();

	/**
	 * 获得 {@link InputStream}
	 *
	 * @return {@link InputStream}
	 */
	InputStream getStream();

	/**
	 * 检查资源是否变更<br>
	 * 一般用于文件类资源，检查文件是否被修改过。
	 *
	 * @return 是否变更
	 * @since 5.7.21
	 */
	default boolean isModified(){
		return false;
	}

	/**
	 * 将资源内容写出到流，不关闭输出流，但是关闭资源流
	 *
	 * @param out 输出流
	 * @throws IORuntimeException IO异常
	 * @since 5.3.5
	 */
	default void writeTo(OutputStream out) throws IORuntimeException {
		try (InputStream in = getStream()) {
			IoUtil.copy(in, out);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获得Reader
	 *
	 * @param charset 编码
	 * @return {@link BufferedReader}
	 */
	default BufferedReader getReader(Charset charset) {
		return IoUtil.getReader(getStream(), charset);
	}

	/**
	 * 读取资源内容，读取完毕后会关闭流<br>
	 * 关闭流并不影响下一次读取
	 *
	 * @param charset 编码
	 * @return 读取资源内容
	 * @throws IORuntimeException 包装{@link IOException}
	 */
	default String readStr(Charset charset) throws IORuntimeException {
		return IoUtil.read(getReader(charset));
	}

	/**
	 * 读取资源内容，读取完毕后会关闭流<br>
	 * 关闭流并不影响下一次读取
	 *
	 * @return 读取资源内容
	 * @throws IORuntimeException 包装IOException
	 */
	default String readUtf8Str() throws IORuntimeException {
		return readStr(CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 读取资源内容，读取完毕后会关闭流<br>
	 * 关闭流并不影响下一次读取
	 *
	 * @return 读取资源内容
	 * @throws IORuntimeException 包装IOException
	 */
	default byte[] readBytes() throws IORuntimeException {
		return IoUtil.readBytes(getStream());
	}
}
