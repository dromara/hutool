package cn.hutool.http.client.engine.httpclient4;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.GlobalHeaders;
import cn.hutool.http.HttpException;
import cn.hutool.http.client.ClientEngine;
import cn.hutool.http.client.Request;
import cn.hutool.http.client.Response;
import cn.hutool.http.client.body.RequestBody;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Apache HttpClient5的HTTP请求引擎
 *
 * @author looly
 * @since 6.0.0
 */
public class HttpClient4Engine implements ClientEngine {

	private final CloseableHttpClient engine;

	/**
	 * 构造
	 */
	public HttpClient4Engine() {
		this.engine = HttpClients.custom()
				// 设置默认头信息
				.setDefaultHeaders(toHeaderList(GlobalHeaders.INSTANCE.headers()))
				.build();
	}

	@Override
	public Response send(final Request message) {
		final HttpEntityEnclosingRequestBase request = buildRequest(message);
		final CloseableHttpResponse response;
		try {
			response = this.engine.execute(request);
		} catch (final IOException e) {
			throw new HttpException(e);
		}

		return new HttpClient4Response(response, message.charset());
	}

	@Override
	public Object getRawEngine() {
		return this.engine;
	}

	@Override
	public void close() throws IOException {
		this.engine.close();
	}

	/**
	 * 构建请求体
	 *
	 * @param message {@link Request}
	 * @return {@link HttpEntityEnclosingRequestBase}
	 */
	private static HttpEntityEnclosingRequestBase buildRequest(final Request message) {
		final UrlBuilder url = message.url();
		Assert.notNull(url, "Request URL must be not null!");
		final URI uri = url.toURI();

		final HttpEntityEnclosingRequestBase request = new HttpEntityEnclosingRequestBase() {
			@Override
			public String getMethod() {
				return message.method().name();
			}
		};
		request.setURI(uri);

		// 填充自定义头
		request.setHeaders(toHeaderList(message.headers()).toArray(new Header[0]));

		// 填充自定义消息体
		final RequestBody body = message.body();
		request.setEntity(new HttpClient4BodyEntity(
				// 用户自定义的内容类型
				message.header(cn.hutool.http.meta.Header.CONTENT_TYPE),
				// 用户自定义编码
				message.charset(),
				message.isChunked(),
				body));

		return request;
	}

	/**
	 * 获取默认头列表
	 *
	 * @return 默认头列表
	 */
	private static List<Header> toHeaderList(final Map<String, List<String>> headersMap) {
		final List<Header> result = new ArrayList<>();
		headersMap.forEach((k, v1) -> v1.forEach((v2) -> result.add(new BasicHeader(k, v2))));
		return result;
	}
}
