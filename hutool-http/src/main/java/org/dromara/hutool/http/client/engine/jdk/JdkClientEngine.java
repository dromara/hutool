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

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.http.HttpException;
import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.http.client.RequestContext;
import org.dromara.hutool.http.client.body.HttpBody;
import org.dromara.hutool.http.client.cookie.InMemoryCookieStore;
import org.dromara.hutool.http.client.engine.AbstractClientEngine;
import org.dromara.hutool.http.meta.HeaderName;
import org.dromara.hutool.http.meta.HttpStatus;
import org.dromara.hutool.http.meta.Method;

import java.io.IOException;

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
		initEngine();
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
		if(null != this.cookieManager){
			return;
		}
		if(null != this.config && this.config.isUseCookieManager()){
			this.cookieStore = new InMemoryCookieStore();
			this.cookieManager = new JdkCookieManager(this.cookieStore);
		}
	}

	/**
	 * 发送请求
	 *
	 * @param context 请求上下文
	 * @return 响应对象
	 */
	private JdkHttpResponse doSend(final RequestContext context) {
		final Request message = context.getRequest();
		final JdkHttpConnection conn = new JdkRequestBuilder(this.config, this.cookieManager).build(message);
		try {
			doSend(conn, message);
		} catch (final IOException e) {
			// 出错后关闭连接
			IoUtil.closeQuietly(conn);
			throw new HttpException(e);
		}

		// 自定义重定向
		final int maxRedirects = message.maxRedirects();
		if (maxRedirects > 0 && context.getRedirectCount() < maxRedirects) {
			final int code;
			try {
				code = conn.getCode();
			} catch (final IOException e) {
				// 错误时静默关闭连接
				conn.closeQuietly();
				throw new HttpException(e);
			}

			if (HttpStatus.isRedirected(code)) {
				// https://www.rfc-editor.org/rfc/rfc7231#section-6.4.7
				// https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Redirections
				// 307方法和消息主体都不发生变化。
				if (HttpStatus.HTTP_TEMP_REDIRECT != code) {
					// 重定向默认使用GET
					message.method(Method.GET);
				}
				message.locationTo(conn.header(HeaderName.LOCATION));
				// 自增计数器
				context.incrementRedirectCount();
				return doSend(context);
			}
		}

		// 最终页面
		return new JdkHttpResponse(conn, this.cookieManager, context.getRequest());
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
}
