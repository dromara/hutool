/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

import org.dromara.hutool.core.net.url.UrlUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.http.client.ClientConfig;
import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.http.client.engine.EngineRequestBuilder;
import org.dromara.hutool.http.meta.HeaderName;
import org.dromara.hutool.http.proxy.ProxyInfo;

import java.net.Proxy;
import java.net.URL;

/**
 * JDK Http请求构建器
 *
 * @author Looly
 * @since 6.0.0
 */
public class JdkRequestBuilder implements EngineRequestBuilder<JdkHttpConnection> {

	private final ClientConfig config;
	private final JdkCookieManager cookieManager;

	/**
	 * 构造
	 *
	 * @param config        客户端配置，为null则使用默认配置
	 * @param cookieManager Cookie管理器
	 */
	public JdkRequestBuilder(final ClientConfig config, final JdkCookieManager cookieManager) {
		this.config = ObjUtil.defaultIfNull(config, ClientConfig::of);
		this.cookieManager = cookieManager;
	}

	@Override
	public JdkHttpConnection build(final Request message) {
		final ClientConfig config = this.config;

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
}
