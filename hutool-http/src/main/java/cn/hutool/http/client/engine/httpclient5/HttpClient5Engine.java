package cn.hutool.http.client.engine.httpclient5;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.net.ssl.SSLUtil;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.GlobalHeaders;
import cn.hutool.http.HttpException;
import cn.hutool.http.client.ClientConfig;
import cn.hutool.http.client.ClientEngine;
import cn.hutool.http.client.Request;
import cn.hutool.http.client.Response;
import cn.hutool.http.client.body.HttpBody;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.message.BasicHeader;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Apache HttpClient5的HTTP请求引擎
 *
 * @author looly
 * @since 6.0.0
 */
public class HttpClient5Engine implements ClientEngine {

	private ClientConfig config;
	private CloseableHttpClient engine;

	/**
	 * 构造
	 */
	public HttpClient5Engine() {}

	@Override
	public HttpClient5Engine setConfig(final ClientConfig config) {
		this.config = config;
		// 重置客户端
		IoUtil.close(this.engine);
		this.engine = null;
		return this;
	}

	@Override
	public Response send(final Request message) {
		initEngine();

		final ClassicHttpRequest request = buildRequest(message);
		final ClassicHttpResponse response;
		try {
			response = this.engine.executeOpen(null, request, null);
		} catch (final IOException e) {
			throw new HttpException(e);
		}

		return new HttpClient5Response(response, message.charset());
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
	 * 初始化引擎
	 */
	private void initEngine(){
		if(null != this.engine){
			return;
		}

		// 连接配置
		final PoolingHttpClientConnectionManagerBuilder connectionManagerBuilder = PoolingHttpClientConnectionManagerBuilder.create()
				.setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()
						.setSslContext(SSLUtil.createTrustAnySSLContext()).build());

		// 请求配置
		RequestConfig requestConfig = null;

		if(null != this.config){
			final int connectionTimeout = this.config.getConnectionTimeout();
			if(connectionTimeout > 0){
				connectionManagerBuilder.setDefaultConnectionConfig(ConnectionConfig.custom()
						.setConnectTimeout(connectionTimeout, TimeUnit.MILLISECONDS).build());
			}
			final RequestConfig.Builder builder = RequestConfig.custom();

			if(connectionTimeout > 0){
				builder.setConnectionRequestTimeout(connectionTimeout, TimeUnit.MILLISECONDS);
			}
			final int readTimeout = this.config.getReadTimeout();
			if(readTimeout > 0){
				builder.setResponseTimeout(readTimeout, TimeUnit.MILLISECONDS);
			}

			requestConfig = builder.build();
		}

		final HttpClientBuilder builder = HttpClients.custom()
				.setConnectionManager(connectionManagerBuilder.build())
				.setDefaultRequestConfig(requestConfig)
				// 设置默认头信息
				.setDefaultHeaders(toHeaderList(GlobalHeaders.INSTANCE.headers()));

		// TODO 设置代理

		this.engine = builder.build();
	}

	/**
	 * 构建请求体
	 *
	 * @param message {@link Request}
	 * @return {@link ClassicHttpRequest}
	 */
	@SuppressWarnings("ConstantConditions")
	private static ClassicHttpRequest buildRequest(final Request message) {
		final UrlBuilder url = message.url();
		Assert.notNull(url, "Request URL must be not null!");
		final URI uri = url.toURI();

		final ClassicHttpRequest request = new HttpUriRequestBase(message.method().name(), uri);

		// 填充自定义头
		request.setHeaders(toHeaderList(message.headers()).toArray(new Header[0]));

		// 填充自定义消息体
		final HttpBody body = message.body();
		request.setEntity(new HttpClient5BodyEntity(
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
