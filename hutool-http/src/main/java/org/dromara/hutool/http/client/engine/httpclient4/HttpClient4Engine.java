/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.http.client.engine.httpclient4;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.net.url.UrlBuilder;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.http.GlobalHeaders;
import org.dromara.hutool.http.HttpException;
import org.dromara.hutool.http.client.ClientConfig;
import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.http.client.Response;
import org.dromara.hutool.http.client.body.HttpBody;
import org.dromara.hutool.http.client.engine.ClientEngine;
import org.dromara.hutool.http.meta.HeaderName;
import org.dromara.hutool.http.proxy.HttpProxy;
import org.dromara.hutool.http.ssl.SSLInfo;

import java.io.IOException;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
	public HttpClient4Engine init(final ClientConfig config) {
		this.config = config;
		// 重置客户端
		IoUtil.closeQuietly(this.engine);
		this.engine = null;
		return this;
	}

	@Override
	public Response send(final Request message) {
		initEngine();

		final HttpUriRequest request = buildRequest(message);
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
		final ClientConfig config = ObjUtil.defaultIfNull(this.config, ClientConfig::of);
		// SSL配置
		final SSLInfo sslInfo = config.getSslInfo();
		if (null != sslInfo) {
			clientBuilder.setSSLSocketFactory(buildSocketFactory(sslInfo));
		}
		if(config.isDisableCache()){
			clientBuilder.disableAuthCaching();
		}

		clientBuilder.setDefaultRequestConfig(buildRequestConfig(config));

		// 设置默认头信息
		clientBuilder.setDefaultHeaders(toHeaderList(GlobalHeaders.INSTANCE.headers()));

		// 默认关闭自动重定向
		clientBuilder.disableRedirectHandling();

		// 设置代理
		setProxy(clientBuilder, config);

		this.engine = clientBuilder.build();
	}

	/**
	 * 构建请求体
	 *
	 * @param message {@link Request}
	 * @return {@link HttpUriRequest}
	 */
	private static HttpUriRequest buildRequest(final Request message) {
		final UrlBuilder url = message.handledUrl();
		Assert.notNull(url, "Request URL must be not null!");
		final URI uri = url.toURI();

		final RequestBuilder requestBuilder = RequestBuilder
			.create(message.method().name())
			.setUri(uri);

		// 填充自定义头
		message.headers().forEach((k, v1) -> v1.forEach((v2) -> requestBuilder.addHeader(k, v2)));

		// 填充自定义消息体
		final HttpBody body = message.handledBody();
		if(null != body){
			requestBuilder.setEntity(new HttpClient4BodyEntity(
				// 用户自定义的内容类型
				message.header(HeaderName.CONTENT_TYPE),
				// 用户自定义编码
				message.charset(),
				message.isChunked(),
				body));
		}

		return requestBuilder.build();
	}

	/**
	 * 获取默认头列表
	 *
	 * @return 默认头列表
	 */
	private static List<Header> toHeaderList(final Map<String, ? extends Collection<String>> headersMap) {
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

	/**
	 * 设置代理信息
	 *
	 * @param clientBuilder {@link org.apache.hc.client5.http.impl.classic.HttpClientBuilder}
	 * @param config        配置
	 */
	private static void setProxy(final HttpClientBuilder clientBuilder, final ClientConfig config) {
		if (null == config) {
			return;
		}

		final HttpProxy proxy = config.getProxy();
		if (null != proxy) {
			final HttpHost httpHost = new HttpHost(proxy.getHost(), proxy.getPort());
			clientBuilder.setProxy(httpHost);
			final PasswordAuthentication auth = proxy.getAuth();
			if (null != auth) {
				// 代理验证
				final BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
				credsProvider.setCredentials(
					new AuthScope(httpHost),
					new UsernamePasswordCredentials(auth.getUserName(), String.valueOf(auth.getPassword())));
				clientBuilder.setDefaultCredentialsProvider(credsProvider);
			}
		}
	}
}
