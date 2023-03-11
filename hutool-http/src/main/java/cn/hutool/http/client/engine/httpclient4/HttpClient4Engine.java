package cn.hutool.http.client.engine.httpclient4;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.GlobalHeaders;
import cn.hutool.http.HttpException;
import cn.hutool.http.client.ClientConfig;
import cn.hutool.http.client.ClientEngine;
import cn.hutool.http.client.Request;
import cn.hutool.http.client.Response;
import cn.hutool.http.client.body.HttpBody;
import cn.hutool.http.ssl.SSLInfo;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
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
public class HttpClient4Engine implements ClientEngine {

	private ClientConfig config;
	private CloseableHttpClient engine;

	/**
	 * 构造
	 */
	public HttpClient4Engine() {
	}

	@Override
	public HttpClient4Engine setConfig(final ClientConfig config) {
		this.config = config;
		// 重置客户端
		IoUtil.close(this.engine);
		this.engine = null;
		return this;
	}

	@Override
	public Response send(final Request message) {
		initEngine();

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
	 * 初始化引擎
	 */
	private void initEngine() {
		if (null != this.engine) {
			return;
		}

		final HttpClientBuilder clientBuilder = HttpClients.custom();
		final ClientConfig config = this.config;
		if (null != config) {
			// SSL配置
			final SSLInfo sslInfo = config.getSslInfo();
			if (null != sslInfo) {
				clientBuilder.setSSLSocketFactory(buildSocketFactory(sslInfo));
			}

			clientBuilder.setDefaultRequestConfig(buildRequestConfig(config));
		}

		// 设置默认头信息
		clientBuilder.setDefaultHeaders(toHeaderList(GlobalHeaders.INSTANCE.headers()));

		this.engine = clientBuilder.build();
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
		final HttpBody body = message.body();
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

	/**
	 * 支持SSL
	 *
	 * @return SSLConnectionSocketFactory
	 */
	private static SSLConnectionSocketFactory buildSocketFactory(final SSLInfo sslInfo) {
		return new SSLConnectionSocketFactory(
				sslInfo.getSslContext(),
				sslInfo.getProtocols(),
				null,
				sslInfo.getHostnameVerifier());
	}

	/**
	 * 构建请求配置，包括连接请求超时和响应（读取）超时
	 *
	 * @param config {@link ClientConfig}
	 * @return {@link RequestConfig}
	 */
	private static RequestConfig buildRequestConfig(final ClientConfig config) {
		// 请求配置
		final RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();

		// 连接超时
		final int connectionTimeout = config.getConnectionTimeout();
		if (connectionTimeout > 0) {
			requestConfigBuilder.setConnectTimeout(connectionTimeout);
			requestConfigBuilder.setConnectionRequestTimeout(connectionTimeout);
		}
		final int readTimeout = config.getReadTimeout();
		if (readTimeout > 0) {
			requestConfigBuilder.setSocketTimeout(readTimeout);
		}

		return requestConfigBuilder.build();
	}
}
