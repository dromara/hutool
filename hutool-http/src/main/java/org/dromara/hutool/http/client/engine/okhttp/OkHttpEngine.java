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

package org.dromara.hutool.http.client.engine.okhttp;

import okhttp3.OkHttpClient;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.http.HttpException;
import org.dromara.hutool.http.client.ClientConfig;
import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.http.client.RequestContext;
import org.dromara.hutool.http.client.Response;
import org.dromara.hutool.http.client.cookie.InMemoryCookieStore;
import org.dromara.hutool.http.client.engine.AbstractClientEngine;
import org.dromara.hutool.http.meta.HeaderName;
import org.dromara.hutool.http.meta.HttpStatus;
import org.dromara.hutool.http.meta.Method;
import org.dromara.hutool.http.proxy.ProxyInfo;
import org.dromara.hutool.http.ssl.SSLInfo;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.PasswordAuthentication;
import java.net.ProxySelector;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp3客户端引擎封装
 *
 * @author looly
 * @since 6.0.0
 */
public class OkHttpEngine extends AbstractClientEngine {

	private OkHttpClient client;

	/**
	 * 构造
	 */
	public OkHttpEngine() {
		// issue#IABWBL JDK8下，在IDEA旗舰版加载Spring boot插件时，启动应用不会检查字段类是否存在
		// 此处构造时调用下这个类，以便触发类是否存在的检查
		Assert.notNull(OkHttpClient.class);
	}

	@Override
	public OkHttpEngine init(final ClientConfig config) {
		this.config = config;
		return this;
	}

	@Override
	public Response send(final Request message) {
		initEngine();
		return doSend(new RequestContext(message));
	}

	@Override
	public Object getRawEngine() {
		return this.client;
	}

	@Override
	public void close() {
		// do nothing
	}

	@Override
	protected void reset() {
		// 重置客户端
		this.client = null;
	}

	@Override
	protected void initEngine() {
		if (null != this.client) {
			return;
		}

		final OkHttpClient.Builder builder = new OkHttpClient.Builder();
		final ClientConfig config = ObjUtil.defaultIfNull(this.config, ClientConfig::of);

		// SSL
		final SSLInfo sslInfo = config.getSslInfo();
		if (null != sslInfo) {
			final SSLSocketFactory socketFactory = sslInfo.getSocketFactory();
			final X509TrustManager trustManager = sslInfo.getTrustManager();
			if (null != socketFactory && null != trustManager) {
				builder.sslSocketFactory(socketFactory, trustManager);
			}
		}

		// 连接超时
		final int connectionTimeout = config.getConnectionTimeout();
		if (connectionTimeout > 0) {
			builder.connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS);
		}

		// 读写超时
		final int readTimeout = config.getReadTimeout();
		if (readTimeout > 0) {
			// 读写共用读取超时
			builder.readTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS)
				.writeTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS);
		}

		// 连接池
		if (config instanceof OkHttpClientConfig) {
			builder.connectionPool(((OkHttpClientConfig) config).getConnectionPool());
		}

		// 关闭自动重定向，手动实现
		builder.followRedirects(false);

		// 设置代理
		setProxy(builder, config);

		// Cookie管理
		if (null != this.config && this.config.isUseCookieManager()) {
			this.cookieStore = new InMemoryCookieStore();
			builder.cookieJar(new CookieJarImpl(this.cookieStore));
		}

		this.client = builder.build();
	}

	/**
	 * 发送请求
	 *
	 * @param context 请求上下文
	 * @return {@link Response}
	 */
	private Response doSend(final RequestContext context) {
		final Request message = context.getRequest();

		final okhttp3.Request request = OkHttpRequestBuilder.INSTANCE.build(message);
		final okhttp3.Response response;
		try {
			response = client.newCall(request).execute();
		} catch (final IOException e) {
			throw new HttpException(e);
		}

		// 自定义重定向
		final int maxRedirects = message.maxRedirects();
		if(maxRedirects > 0 && context.getRedirectCount() < maxRedirects){
			final int code = response.code();
			if (HttpStatus.isRedirected(code)) {
				message.locationTo(response.header(HeaderName.LOCATION.getValue()));
			}
			// https://www.rfc-editor.org/rfc/rfc7231#section-6.4.7
			// https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Redirections
			// 307方法和消息主体都不发生变化。
			if (HttpStatus.HTTP_TEMP_REDIRECT != code) {
				// 重定向默认使用GET
				message.method(Method.GET);
			}
			// 自增计数器
			context.incrementRedirectCount();
			return doSend(context);
		}

		return new OkHttpResponse(response, message);
	}

	/**
	 * 设置代理信息
	 *
	 * @param builder 客户端构建器
	 * @param config  配置
	 */
	private static void setProxy(final OkHttpClient.Builder builder, final ClientConfig config) {
		final ProxyInfo proxyInfo = config.getProxy();
		if (null != proxyInfo) {
			final ProxySelector proxySelector = proxyInfo.getProxySelector();
			if(null != proxySelector){
				builder.proxySelector(proxySelector);
			}
			final PasswordAuthentication auth = proxyInfo.getAuth();
			if (null != auth) {
				builder.proxyAuthenticator(new BasicProxyAuthenticator(auth));
			}
		}
	}
}
