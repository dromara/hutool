/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.client.engine.httpclient5;

import org.dromara.hutool.GlobalHeaders;
import org.dromara.hutool.HttpException;
import org.dromara.hutool.client.ClientConfig;
import org.dromara.hutool.client.ClientEngine;
import org.dromara.hutool.client.Request;
import org.dromara.hutool.client.Response;
import org.dromara.hutool.client.body.HttpBody;
import org.dromara.hutool.io.IoUtil;
import org.dromara.hutool.lang.Assert;
import org.dromara.hutool.net.url.UrlBuilder;
import org.dromara.hutool.ssl.SSLInfo;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
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
	public HttpClient5Engine() {
	}

	@Override
	public HttpClient5Engine setConfig(final ClientConfig config) {
		this.config = config;
		// 重置客户端
		IoUtil.closeQuietly(this.engine);
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
	private void initEngine() {
		if (null != this.engine) {
			return;
		}

		final HttpClientBuilder clientBuilder = HttpClients.custom();

		final ClientConfig config = this.config;
		if (null != config) {
			clientBuilder.setConnectionManager(buildConnectionManager(config));
			clientBuilder.setDefaultRequestConfig(buildRequestConfig(config));
		}

		// 设置默认头信息
		clientBuilder.setDefaultHeaders(toHeaderList(GlobalHeaders.INSTANCE.headers()));

		// TODO 设置代理

		this.engine = clientBuilder.build();
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
				message.header(org.dromara.hutool.meta.Header.CONTENT_TYPE),
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
	 * 构建连接管理器，包括SSL配置和连接超时配置
	 *
	 * @param config {@link ClientConfig}
	 * @return {@link PoolingHttpClientConnectionManager}
	 */
	private static PoolingHttpClientConnectionManager buildConnectionManager(final ClientConfig config) {
		final PoolingHttpClientConnectionManagerBuilder connectionManagerBuilder = PoolingHttpClientConnectionManagerBuilder.create();
		// SSL配置
		final SSLInfo sslInfo = config.getSslInfo();
		if (null != sslInfo) {
			connectionManagerBuilder.setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()
					.setTlsVersions(sslInfo.getProtocols())
					.setSslContext(sslInfo.getSslContext())
					.setHostnameVerifier(sslInfo.getHostnameVerifier())
					.build());
		}
		// 连接超时配置
		final int connectionTimeout = config.getConnectionTimeout();
		if (connectionTimeout > 0) {
			connectionManagerBuilder.setDefaultConnectionConfig(ConnectionConfig.custom()
					.setSocketTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
					.setConnectTimeout(connectionTimeout, TimeUnit.MILLISECONDS).build());
		}

		return connectionManagerBuilder.build();
	}

	/**
	 * 构建请求配置，包括连接请求超时和响应（读取）超时
	 *
	 * @param config {@link ClientConfig}
	 * @return {@link RequestConfig}
	 */
	private static RequestConfig buildRequestConfig(final ClientConfig config) {
		final int connectionTimeout = config.getConnectionTimeout();

		// 请求配置
		final RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
		if (connectionTimeout > 0) {
			requestConfigBuilder.setConnectionRequestTimeout(connectionTimeout, TimeUnit.MILLISECONDS);
		}
		final int readTimeout = config.getReadTimeout();
		if (readTimeout > 0) {
			requestConfigBuilder.setResponseTimeout(readTimeout, TimeUnit.MILLISECONDS);
		}

		return requestConfigBuilder.build();
	}
}
