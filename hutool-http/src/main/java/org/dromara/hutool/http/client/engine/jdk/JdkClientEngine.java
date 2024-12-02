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

package org.dromara.hutool.http.client.engine.jdk;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.net.url.UrlUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.http.HttpException;
import org.dromara.hutool.http.client.ClientConfig;
import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.http.client.RequestContext;
import org.dromara.hutool.http.client.body.HttpBody;
import org.dromara.hutool.http.client.engine.AbstractClientEngine;
import org.dromara.hutool.http.meta.HeaderName;
import org.dromara.hutool.http.meta.HttpStatus;
import org.dromara.hutool.http.meta.Method;
import org.dromara.hutool.http.proxy.ProxyInfo;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;

/**
 * 基于JDK的UrlConnection的Http客户端引擎实现
 *
 * @author looly
 */
public class JdkClientEngine extends AbstractClientEngine {

	/**
	 * Cookie管理
	 */
	private JdkCookieManager cookieManager;

	/**
	 * 构造
	 */
	public JdkClientEngine() {
	}

	/**
	 * 获取Cookie管理器
	 *
	 * @return Cookie管理器
	 */
	public JdkCookieManager getCookieManager() {
		return this.cookieManager;
	}

	@Override
	public JdkHttpResponse send(final Request message) {
		return doSend(new RequestContext(message));
	}

	@Override
	public Object getRawEngine() {
		return this;
	}

	@Override
	public void close() {
		// 关闭Cookie管理器
		this.cookieManager = null;
	}

	@Override
	protected void reset() {
		// do nothing
	}

	@Override
	protected void initEngine() {
		this.cookieManager = (null != this.config && this.config.isUseCookieManager()) ? new JdkCookieManager() : new JdkCookieManager(null);
	}

	private JdkHttpResponse doSend(final RequestContext context) {
		final Request message = context.getRequest();
		final JdkHttpConnection conn = buildConn(message);
		try {
			doSend(conn, message);
		} catch (final IOException e) {
			// 出错后关闭连接
			IoUtil.closeQuietly(conn);
			throw new IORuntimeException(e);
		}

		return sendRedirectIfPossible(conn, context);
	}

	/**
	 * 执行发送
	 *
	 * @param message 请求消息
	 * @throws IOException IO异常
	 */
	private void doSend(final JdkHttpConnection conn, final Request message) throws IOException {
		final HttpBody body = message.handledBody();
		if (null != body) {
			// 带有消息体，一律按照Rest方式发送
			body.writeClose(conn.getOutputStream());
			return;
		}

		// 非Rest简单GET请求
		conn.connect();
	}

	/**
	 * 构建{@link JdkHttpConnection}
	 *
	 * @param message {@link Request}消息
	 * @return {@link JdkHttpConnection}
	 */
	private JdkHttpConnection buildConn(final Request message) {
		final ClientConfig config = ObjUtil.defaultIfNull(this.config, ClientConfig::of);

		final URL url = message.handledUrl().toURL();
		Proxy proxy = null;
		final ProxyInfo proxyInfo = config.getProxy();
		if (null != proxyInfo) {
			proxy = proxyInfo.selectFirst(UrlUtil.toURI(url));
		}
		final JdkHttpConnection conn = JdkHttpConnection
			.of(url, proxy)
			.setConnectTimeout(config.getConnectionTimeout())
			.setReadTimeout(config.getReadTimeout())
			.setMethod(message.method())//
			.setSSLInfo(config.getSslInfo())
			// 关闭自动重定向，手动处理重定向
			.setInstanceFollowRedirects(false)
			.setDisableCache(config.isDisableCache())
			// 覆盖默认Header
			.header(message.headers(), true);

		if (!message.method().isIgnoreBody()) {
			// 在允许发送body的情况下，如果用户自定义了Content-Length，则使用用户定义的值
			final long contentLength = message.contentLength();
			if (contentLength > 0) {
				// 固定请求长度
				conn.setFixedLengthStreamingMode(contentLength);
			} else if (message.isChunked()) {
				conn.setChunkedStreamingMode(4096);
			}
		}

		// Cookie管理
		if (null == message.header(HeaderName.COOKIE) && null != this.cookieManager) {
			// 用户没有自定义Cookie，则读取Cookie管理器中的信息并附带到请求中
			// 不覆盖模式回填Cookie头，这样用户定义的Cookie将优先
			conn.header(this.cookieManager.loadForRequest(conn), false);
		}

		return conn;
	}

	/**
	 * 调用转发，如果需要转发返回转发结果，否则返回{@code null}
	 *
	 * @param conn    {@link JdkHttpConnection}}
	 * @param context 请求上下文
	 * @return {@link JdkHttpResponse}，无转发返回 {@code null}
	 */
	private JdkHttpResponse sendRedirectIfPossible(final JdkHttpConnection conn, final RequestContext context) {
		final Request message = context.getRequest();
		// 手动实现重定向
		if (message.maxRedirects() > 0) {
			final int code;
			try {
				code = conn.getCode();
			} catch (final IOException e) {
				// 错误时静默关闭连接
				conn.closeQuietly();
				throw new HttpException(e);
			}

			if (HttpStatus.isRedirected(code)) {
				message.locationTo(conn.header(HeaderName.LOCATION));

				// https://www.rfc-editor.org/rfc/rfc7231#section-6.4.7
				// https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Redirections
				// 307方法和消息主体都不发生变化。
				if (HttpStatus.HTTP_TEMP_REDIRECT != code) {
					// 重定向默认使用GET
					message.method(Method.GET);
				}

				if (context.canRedirect()) {
					return doSend(context);
				}
			}
		}

		// 最终页面
		return new JdkHttpResponse(conn, this.cookieManager, context.getRequest());
	}
}
