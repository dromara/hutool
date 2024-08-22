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
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.http.client.ClientConfig;
import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.http.client.Response;
import org.dromara.hutool.http.client.body.HttpBody;
import org.dromara.hutool.http.client.engine.AbstractClientEngine;
import org.dromara.hutool.http.proxy.HttpProxy;
import org.dromara.hutool.http.ssl.SSLInfo;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.PasswordAuthentication;
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

		final okhttp3.Response response;
		try {
			response = client.newCall(buildRequest(message)).execute();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		return new OkHttpResponse(response, message.charset());
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

		final ClientConfig conf = ObjUtil.defaultIfNull(this.config, ClientConfig::of);
		// 连接超时
		final int connectionTimeout = conf.getConnectionTimeout();
		if (connectionTimeout > 0) {
			builder.connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS);
		}
		// 读写超时
		final int readTimeout = conf.getReadTimeout();
		if (readTimeout > 0) {
			// 读写共用读取超时
			builder.readTimeout(conf.getReadTimeout(), TimeUnit.MILLISECONDS)
				.writeTimeout(conf.getReadTimeout(), TimeUnit.MILLISECONDS);
		}

		// SSL
		final SSLInfo sslInfo = conf.getSslInfo();
		if (null != sslInfo){
			final SSLSocketFactory socketFactory = sslInfo.getSocketFactory();
			final X509TrustManager trustManager = sslInfo.getTrustManager();
			if(null != socketFactory && null != trustManager){
				builder.sslSocketFactory(socketFactory, trustManager);
			}
		}

		// 设置代理
		setProxy(builder, conf);

		// 默认关闭自动跳转
		builder.followRedirects(false);

		this.client = builder.build();
	}

	/**
	 * 构建请求体
	 *
	 * @param message {@link Request}
	 * @return {@link okhttp3.Request}
	 */
	private static okhttp3.Request buildRequest(final Request message) {
		final okhttp3.Request.Builder builder = new okhttp3.Request.Builder()
			.url(message.handledUrl().toURL());

		// 填充方法
		final String method = message.method().name();
		final HttpBody body = message.handledBody();
		if (null != body) {
			// 为了兼容支持rest请求，在此不区分是否为GET等方法，一律按照body是否有值填充，兼容
			builder.method(method, new OkHttpRequestBody(body));
		} else {
			builder.method(method, null);
		}

		// 填充头信息
		message.headers().forEach((key, values)-> values.forEach(value-> builder.addHeader(key, value)));

		return builder.build();
	}

	/**
	 * 设置代理信息
	 *
	 * @param builder 客户端构建器
	 * @param config  配置
	 */
	private static void setProxy(final OkHttpClient.Builder builder, final ClientConfig config) {
		final HttpProxy proxy = config.getProxy();
		if (null != proxy) {
			builder.proxy(proxy);
			final PasswordAuthentication auth = proxy.getAuth();
			if (null != auth) {
				builder.proxyAuthenticator(new BasicProxyAuthenticator(auth));
			}
		}
	}
}
