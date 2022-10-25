package cn.hutool.http.client;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.StrUtil;
import cn.hutool.http.meta.Header;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;

import java.io.Closeable;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 响应内容接口，包括响应状态码、HTTP消息头、响应体等信息
 *
 * @author looly
 * @since 6.0.0
 */
public interface Response extends Closeable {

	/**
	 * 获取状态码
	 *
	 * @return 状态码
	 */
	int getStatus();

	/**
	 * 根据name获取头信息<br>
	 * 根据RFC2616规范，header的name不区分大小写
	 *
	 * @param name Header名
	 * @return Header值
	 */
	String header(final String name);

	/**
	 * 获取字符集编码
	 *
	 * @return 字符集
	 */
	Charset charset();

	/**
	 * 获得服务区响应流<br>
	 * 流获取后处理完毕需关闭此类
	 *
	 * @return 响应流
	 */
	InputStream bodyStream();

	/**
	 * 获取响应主体
	 *
	 * @return String
	 * @throws HttpException 包装IO异常
	 */
	default String body() throws HttpException {
		return HttpUtil.getString(bodyStream(), charset(), true);
	}

	/**
	 * 请求是否成功，判断依据为：状态码范围在200~299内。
	 *
	 * @return 是否成功请求
	 */
	default boolean isOk() {
		final int status = getStatus();
		return status >= 200 && status < 300;
	}

	/**
	 * 根据name获取头信息
	 *
	 * @param name Header名
	 * @return Header值
	 */
	default String header(final Header name) {
		if (null == name) {
			return null;
		}
		return header(name.toString());
	}

	/**
	 * 获取内容编码
	 *
	 * @return String
	 */
	default String contentEncoding() {
		return header(Header.CONTENT_ENCODING);
	}

	/**
	 * 获取内容长度，以下情况长度无效：
	 * <ul>
	 *     <li>Transfer-Encoding: Chunked</li>
	 *     <li>Content-Encoding: XXX</li>
	 * </ul>
	 * 参考：<a href="https://blog.csdn.net/jiang7701037/article/details/86304302">https://blog.csdn.net/jiang7701037/article/details/86304302</a>
	 *
	 * @return 长度，-1表示服务端未返回或长度无效
	 * @since 5.7.9
	 */
	default long contentLength() {
		long contentLength = Convert.toLong(header(Header.CONTENT_LENGTH), -1L);
		if (contentLength > 0 && (isChunked() || StrUtil.isNotBlank(contentEncoding()))) {
			//按照HTTP协议规范，在 Transfer-Encoding和Content-Encoding设置后 Content-Length 无效。
			contentLength = -1;
		}
		return contentLength;
	}

	/**
	 * 是否为Transfer-Encoding:Chunked的内容
	 *
	 * @return 是否为Transfer-Encoding:Chunked的内容
	 * @since 4.6.2
	 */
	default boolean isChunked() {
		final String transferEncoding = header(Header.TRANSFER_ENCODING);
		return "Chunked".equalsIgnoreCase(transferEncoding);
	}

	/**
	 * 获取本次请求服务器返回的Cookie信息
	 *
	 * @return Cookie字符串
	 * @since 3.1.1
	 */
	default String getCookieStr() {
		return header(Header.SET_COOKIE);
	}
}
