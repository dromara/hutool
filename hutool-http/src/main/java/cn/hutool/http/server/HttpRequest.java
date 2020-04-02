package cn.hutool.http.server;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;

/**
 * Http请求对象，对{@link HttpExchange}封装
 *
 * @author looly
 * @since 5.2.6
 */
public class HttpRequest {

	private final HttpExchange httpExchange;

	/**
	 * 构造
	 *
	 * @param httpExchange {@link HttpExchange}
	 */
	public HttpRequest(HttpExchange httpExchange) {
		this.httpExchange = httpExchange;
	}

	/**
	 * 获得Http Method
	 *
	 * @return Http Method
	 */
	public String getMethod() {
		return this.httpExchange.getRequestMethod();
	}

	/**
	 * 是否为GET请求
	 *
	 * @return 是否为GET请求
	 */
	public boolean isGetMethod() {
		return Method.GET.name().equalsIgnoreCase(getMethod());
	}

	/**
	 * 是否为POST请求
	 *
	 * @return 是否为POST请求
	 */
	public boolean isPostMethod() {
		return Method.POST.name().equalsIgnoreCase(getMethod());
	}

	/**
	 * 获得请求URI
	 *
	 * @return 请求URI
	 */
	public URI getURI() {
		return this.httpExchange.getRequestURI();
	}

	/**
	 * 获得请求路径Path
	 *
	 * @return 请求路径
	 */
	public String getPath() {
		return getURI().getPath();
	}

	/**
	 * 获取请求参数
	 *
	 * @return 参数字符串
	 */
	public String getQuery() {
		return getURI().getQuery();
	}

	/**
	 * 获得请求header中的信息
	 *
	 * @return header值
	 */
	public Headers getHeaders() {
		return this.httpExchange.getRequestHeaders();
	}

	/**
	 * 获得请求header中的信息
	 *
	 * @param headerKey 头信息的KEY
	 * @return header值
	 */
	public String getHeader(String headerKey) {
		return getHeaders().getFirst(headerKey);
	}

	/**
	 * 获得请求header中的信息
	 *
	 * @param headerKey 头信息的KEY
	 * @param charset   字符集
	 * @return header值
	 */
	public String getHeader(String headerKey, Charset charset) {
		final String header = getHeader(headerKey);
		if (null != header) {
			return CharsetUtil.convert(header, CharsetUtil.CHARSET_ISO_8859_1, charset);
		}
		return null;
	}

	/**
	 * 获得User-Agent
	 *
	 * @return User-Agent字符串
	 */
	public String getUserAgentStr() {
		return getHeader("User-Agent");
	}

	/**
	 * 获得User-Agent，未识别返回null
	 *
	 * @return User-Agent字符串，未识别返回null
	 */
	public UserAgent getUserAgent() {
		return UserAgentUtil.parse(getUserAgentStr());
	}

	/**
	 * 获取请求体的流，流中可以读取请求内容，包括请求表单数据或文件上传数据
	 *
	 * @return 流
	 */
	public InputStream getBodyStream() {
		return this.httpExchange.getRequestBody();
	}

	/**
	 * 获取请求体文本，可以是form表单、json、xml等任意内容<br>
	 * 根据请求的Content-Type判断编码，判断失败使用UTF-8编码
	 *
	 * @return 请求
	 */
	public String getBody() {
		final String contentType = getHeader(Header.CONTENT_TYPE.toString());
		final String charsetStr = HttpUtil.getCharset(contentType);
		final Charset charset = CharsetUtil.parse(charsetStr, CharsetUtil.CHARSET_UTF_8);

		return getBody(charset);
	}

	/**
	 * 获取请求体文本，可以是form表单、json、xml等任意内容
	 *
	 * @param charset 编码
	 * @return 请求
	 */
	public String getBody(Charset charset) {
		InputStream in = null;
		try {
			in = getBodyStream();
			return IoUtil.read(in, charset);
		} finally {
			IoUtil.close(in);
		}
	}

	/**
	 * 是否为Multipart类型表单，此类型表单用于文件上传
	 *
	 * @return 是否为Multipart类型表单，此类型表单用于文件上传
	 */
	public boolean isMultipart() {
		if (false == isPostMethod()) {
			return false;
		}

		final String contentType = getHeader(Header.CONTENT_TYPE.toString());
		if (StrUtil.isBlank(contentType)) {
			return false;
		}

		return contentType.toLowerCase().startsWith("multipart/");
	}
}
