/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.http.client.engine.httpclient4;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.message.BasicHeader;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.http.GlobalHeaders;
import org.dromara.hutool.http.HttpException;
import org.dromara.hutool.http.client.ApacheHttpClientConfig;
import org.dromara.hutool.http.client.ClientConfig;
import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.http.client.Response;
import org.dromara.hutool.http.client.cookie.InMemoryCookieStore;
import org.dromara.hutool.http.client.engine.AbstractClientEngine;
import org.dromara.hutool.http.proxy.ProxyInfo;
import org.dromara.hutool.http.ssl.SSLInfo;

import java.io.IOException;
import java.net.PasswordAuthentication;
import java.net.ProxySelector;
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
public class HttpClient4Engine extends AbstractClientEngine {

	private CloseableHttpClient engine;

	/**
	 * 构造
	 */
	public HttpClient4Engine() {
		// issue#IABWBL JDK8下，在IDEA旗舰版加载Spring boot插件时，启动应用不会检查字段类是否存在
		// 此处构造时调用下这个类，以便触发类是否存在的检查
		Assert.notNull(CloseableHttpClient.class);
	}

	@Override
	public Response send(final Request message) {
		initEngine();

		final HttpUriRequest request = HttpUriRequestBuilder.INSTANCE.build(message);
		final CloseableHttpResponse response;
		try {
			response = this.engine.execute(request);
		} catch (final IOException e) {
			throw new HttpException(e);
		}
		return new HttpClient4Response(response, message);
	}

	@Override
	public Object getRawEngine() {
		return this.engine;
	}

	@Override
	public void close() throws IOException {
		IoUtil.nullSafeClose(this.engine);
	}

	@Override
	protected void reset() {
		// 重置客户端
		IoUtil.closeQuietly(this.engine);
		this.engine = null;
	}

	@Override
	protected void initEngine() {
		if (null != this.engine) {
			return;
		}

		final HttpClientBuilder clientBuilder = HttpClients.custom();
		final ClientConfig config = ObjUtil.defaultIfNull(this.config, ApacheHttpClientConfig::of);

		// SSL配置
		final SSLInfo sslInfo = config.getSslInfo();
		if (null != sslInfo) {
			clientBuilder.setSSLSocketFactory(buildSocketFactory(sslInfo));
		}

		// 连接配置，包括连接池
		clientBuilder.setConnectionManager(buildConnectionManager(config));

		// 实例级别默认请求配置
		clientBuilder.setDefaultRequestConfig(buildDefaultRequestConfig(config));

		// 缓存
		if (config.isDisableCache()) {
			clientBuilder.disableAuthCaching();
		}

		// 设置默认头信息
		clientBuilder.setDefaultHeaders(toHeaderList(GlobalHeaders.INSTANCE.headers()));

		// 设置代理
		setProxy(clientBuilder, config);

		// Cookie管理
		if(config.isUseCookieManager()){
			this.cookieStore = new InMemoryCookieStore();
			clientBuilder.setDefaultCookieStore(new HttpClient4CookieStore(this.cookieStore));
		}

		this.engine = clientBuilder.build();
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
	 * 构建连接池管理器
	 *
	 * @param config 配置
	 * @return PoolingHttpClientConnectionManager
	 */
	private PoolingHttpClientConnectionManager buildConnectionManager(final ClientConfig config) {
		final PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();

		// 连接池配置
		if (config instanceof ApacheHttpClientConfig) {
			final ApacheHttpClientConfig apacheHttpClientConfig = (ApacheHttpClientConfig) config;
			final int maxTotal = apacheHttpClientConfig.getMaxTotal();
			if(maxTotal > 0){
				manager.setMaxTotal(maxTotal);
			}
			final int maxPerRoute = apacheHttpClientConfig.getMaxPerRoute();
			if(maxPerRoute > 0){
				manager.setDefaultMaxPerRoute(maxPerRoute);
			}
		}

		return manager;
	}

	/**
	 * 构建请求配置，包括连接请求超时和响应（读取）超时
	 *
	 * @param config {@link ClientConfig}
	 * @return {@link RequestConfig}
	 */
	private static RequestConfig buildDefaultRequestConfig(final ClientConfig config) {
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
		if (config instanceof ApacheHttpClientConfig) {
			requestConfigBuilder.setMaxRedirects(((ApacheHttpClientConfig) config).getMaxRedirects());
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

		final ProxyInfo proxy = config.getProxy();
		if (null != proxy) {
			final ProxySelector proxySelector = proxy.getProxySelector();
			if(null != proxySelector){
				clientBuilder.setRoutePlanner(new SystemDefaultRoutePlanner(proxySelector));
			}
			final PasswordAuthentication auth = proxy.getAuth();
			if (null != auth) {
				// 代理验证
				final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
				credentialsProvider.setCredentials(
					new AuthScope(new HttpHost(proxy.getAuthHost(), proxy.getAuthPort())),
					new UsernamePasswordCredentials(auth.getUserName(), String.valueOf(auth.getPassword())));
				clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
			}
		}
	}
}
