package cn.hutool.http.client;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.http.HttpGlobalConfig;
import cn.hutool.http.client.body.RequestBody;
import cn.hutool.http.meta.Header;
import cn.hutool.http.meta.Method;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求消息体，包括请求的URI、请求头、请求体等
 *
 * @author looly
 * @since 6.0.0
 */
public class Request implements Headers<Request> {

	/**
	 * 构建一个HTTP请求<br>
	 * 对于传入的URL，可以自定义是否解码已经编码的内容，设置见{@link HttpGlobalConfig#setDecodeUrl(boolean)}<br>
	 * 在构建Http请求时，用户传入的URL可能有编码后和未编码的内容混合在一起，如果{@link HttpGlobalConfig#isDecodeUrl()}为{@code true}，则会统一解码编码后的参数，<br>
	 * 按照RFC3986规范，在发送请求时，全部编码之。如果为{@code false}，则不会解码已经编码的内容，在请求时只编码需要编码的部分。
	 *
	 * @param url URL链接，默认自动编码URL中的参数等信息
	 * @return HttpRequest
	 */
	public static Request of(final String url) {
		return of(url, HttpGlobalConfig.isDecodeUrl() ? DEFAULT_CHARSET : null);
	}

	/**
	 * 构建一个HTTP请求<br>
	 * 对于传入的URL，可以自定义是否解码已经编码的内容。<br>
	 * 在构建Http请求时，用户传入的URL可能有编码后和未编码的内容混合在一起，如果charset参数不为{@code null}，则会统一解码编码后的参数，<br>
	 * 按照RFC3986规范，在发送请求时，全部编码之。如果为{@code false}，则不会解码已经编码的内容，在请求时只编码需要编码的部分。
	 *
	 * @param url     URL链接
	 * @param charset 编码，如果为{@code null}不自动解码编码URL
	 * @return HttpRequest
	 */
	public static Request of(final String url, final Charset charset) {
		return of(UrlBuilder.ofHttp(url, charset));
	}

	/**
	 * 构建一个HTTP请求<br>
	 *
	 * @param url {@link UrlBuilder}
	 * @return HttpRequest
	 */
	public static Request of(final UrlBuilder url) {
		return new Request().url(url);
	}

	/**
	 * 默认的请求编码、URL的encode、decode编码
	 */
	private static final Charset DEFAULT_CHARSET = CharsetUtil.UTF_8;

	/**
	 * 请求方法
	 */
	private Method method = Method.GET;
	/**
	 * 请求的URL
	 */
	private UrlBuilder url;
	/**
	 * 存储头信息
	 */
	private final Map<String, List<String>> headers = new HashMap<>();
	private RequestBody body;

	/**
	 * 获取Http请求方法
	 *
	 * @return {@link Method}
	 * @since 4.1.8
	 */
	public Method method() {
		return this.method;
	}

	/**
	 * 设置请求方法
	 *
	 * @param method HTTP方法
	 * @return HttpRequest
	 */
	public Request method(final Method method) {
		this.method = method;
		return this;
	}

	/**
	 * 获取请求的URL
	 *
	 * @return URL
	 */
	public UrlBuilder url() {
		return url;
	}

	/**
	 * 设置URL
	 *
	 * @param url URL
	 * @return this
	 */
	public Request url(final UrlBuilder url) {
		this.url = url;
		return this;
	}

	/**
	 * 设置编码
	 *
	 * @param charset 编码
	 * @return this
	 */
	public Request charset(final Charset charset) {
		Assert.notNull(this.url, "You must be set request url first.");
		this.url.setCharset(charset);
		return this;
	}

	/**
	 * 获取请求编码，如果用户未设置，返回{@link #DEFAULT_CHARSET}
	 *
	 * @return 编码
	 */
	public Charset charset() {
		Assert.notNull(this.url, "You must be set request url first.");
		return ObjUtil.defaultIfNull(this.url.getCharset(), DEFAULT_CHARSET);
	}

	@Override
	public Map<String, List<String>> headers() {
		return MapUtil.view(this.headers);
	}

	/**
	 * 是否为Transfer-Encoding:Chunked的内容
	 *
	 * @return 是否为Transfer-Encoding:Chunked的内容
	 */
	public boolean isChunked() {
		final String transferEncoding = header(Header.TRANSFER_ENCODING);
		return "Chunked".equalsIgnoreCase(transferEncoding);
	}

	/**
	 * 设置一个header<br>
	 * 如果覆盖模式，则替换之前的值，否则加入到值列表中<br>
	 * 如果给定值为{@code null}，则删除这个头信息
	 *
	 * @param name       Header名，{@code null}跳过
	 * @param value      Header值，{@code null}表示删除name对应的头
	 * @param isOverride 是否覆盖已有值
	 * @return this
	 */
	@Override
	public Request header(final String name, final String value, final boolean isOverride) {
		if (null == name) {
			return this;
		}
		if (null == value) {
			headers.remove(name);
			return this;
		}

		final List<String> values = headers.get(name.trim());
		if (isOverride || CollUtil.isEmpty(values)) {
			final ArrayList<String> valueList = new ArrayList<>();
			valueList.add(value);
			headers.put(name.trim(), valueList);
		} else {
			values.add(value.trim());
		}
		return this;
	}

	/**
	 * 获取请求体
	 *
	 * @return this
	 */
	public RequestBody body() {
		return this.body;
	}

	/**
	 * 添加请求体
	 *
	 * @param body 请求体，可以是文本、表单、流、byte[] 或 Multipart
	 * @return this
	 */
	public Request body(final RequestBody body) {
		this.body = body;
		return this;
	}
}
