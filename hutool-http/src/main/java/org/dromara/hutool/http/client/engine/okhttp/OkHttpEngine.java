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

package org.dromara.hutool.http.client.engine.okhttp;

import okhttp3.OkHttpClient;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.http.client.ClientConfig;
import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.http.client.Response;
import org.dromara.hutool.http.client.body.HttpBody;
import org.dromara.hutool.http.client.engine.ClientEngine;
import org.dromara.hutool.http.proxy.HttpProxy;
import org.dromara.hutool.http.ssl.SSLInfo;

import java.io.IOException;
import java.net.PasswordAuthentication;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp3客户端引擎封装
 *
 * @author looly
 * @since 6.0.0
 */
public class OkHttpEngine implements ClientEngine {

	private ClientConfig config;
	private OkHttpClient client;

	/**
	 * 构造
	 */
	public OkHttpEngine() {}

	@Override
	public OkHttpEngine init(final ClientConfig config) {
		this.config = config;
		// 重置客户端
		this.client = null;
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

	/**
	 * 初始化引擎
	 */
	private void initEngine() {
		if (null != this.client) {
			return;
		}

		final OkHttpClient.Builder builder = new OkHttpClient.Builder();

		final ClientConfig config = ObjUtil.defaultIfNull(this.config, ClientConfig::of);
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

		// SSL
		final SSLInfo sslInfo = config.getSslInfo();
		if (null != sslInfo) {
			builder.sslSocketFactory(sslInfo.getSocketFactory(), sslInfo.getTrustManager());
		}

		// 设置代理
		setProxy(builder, config);

		// 默认关闭自动跳转
		builder.setFollowRedirects$okhttp(false);

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
		// if (HttpMethod.permitsRequestBody(method)) {
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
			builder.setProxy$okhttp(proxy);
			final PasswordAuthentication auth = proxy.getAuth();
			if (null != auth) {
				builder.setProxyAuthenticator$okhttp(new BasicProxyAuthenticator(auth));
			}
		}
	}
}
