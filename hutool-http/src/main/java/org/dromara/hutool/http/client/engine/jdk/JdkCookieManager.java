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

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.net.url.UrlUtil;
import org.dromara.hutool.http.client.cookie.ThreadLocalCookieStore;

import java.io.Closeable;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDK Cookie管理器，用于管理Cookie信息，包括：
 *
 * <pre>
 * 1. 存储Cookie信息到本地
 * 2. 从本地获取Cookie信息，添加到请求头中
 * </pre>
 *
 * @author Looly
 * @since 6.0.0
 */
public class JdkCookieManager implements Closeable {

	private CookieManager cookieManager;

	/**
	 * 构造
	 */
	public JdkCookieManager() {
		this(new CookieManager(new ThreadLocalCookieStore(), CookiePolicy.ACCEPT_ALL));
	}

	/**
	 * 构造
	 *
	 * @param raw 原始对象
	 */
	public JdkCookieManager(final CookieManager raw) {
		this.cookieManager = raw;
	}

	/**
	 * 是否启用Cookie管理
	 *
	 * @return 是否启用Cookie管理
	 */
	public boolean isEnable() {
		return null != this.cookieManager;
	}

	@Override
	public void close() {
		this.cookieManager = null;
	}

	/**
	 * 获取{@link CookieManager}
	 *
	 * @return {@link CookieManager}
	 */
	public CookieManager getCookieManager() {
		return this.cookieManager;
	}

	/**
	 * 自定义{@link CookieManager}
	 *
	 * @param cookieManager 自定义的{@link CookieManager}
	 * @return this
	 */
	public JdkCookieManager setCookieManager(final CookieManager cookieManager) {
		this.cookieManager = cookieManager;
		return this;
	}

	/**
	 * 获取指定域名下所有Cookie信息
	 *
	 * @param conn HTTP连接
	 * @return Cookie信息列表
	 * @since 4.6.9
	 */
	public List<HttpCookie> getCookies(final JdkHttpConnection conn) {
		if (null == this.cookieManager) {
			return null;
		}
		return this.cookieManager.getCookieStore().get(getURI(conn));
	}

	/**
	 * 将本地存储的Cookie信息附带到Http请求中，不覆盖用户定义好的Cookie
	 *
	 * @param conn {@link JdkHttpConnection}
	 * @return this
	 */
	public Map<String, List<String>> loadForRequest(final JdkHttpConnection conn) {
		if (null == cookieManager) {
			// Cookie管理器关闭
			return null;
		}

		try {
			return this.cookieManager.get(getURI(conn), new HashMap<>(0));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 存储响应的Cookie信息到本地
	 *
	 * @param conn {@link JdkHttpConnection}
	 * @return this
	 */
	public JdkCookieManager saveFromResponse(final JdkHttpConnection conn) {
		return saveFromResponse(conn, conn.headers());
	}

	/**
	 * 存储响应的Cookie信息到本地<br>
	 * 通过读取
	 *
	 * @param conn            {@link JdkHttpConnection}
	 * @param responseHeaders 头信息Map
	 * @return this
	 */
	public JdkCookieManager saveFromResponse(final JdkHttpConnection conn, final Map<String, List<String>> responseHeaders) {
		if (null == this.cookieManager || MapUtil.isEmpty(responseHeaders)) {
			// Cookie管理器关闭或头信息为空
			return this;
		}

		try {
			cookieManager.put(getURI(conn), responseHeaders);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	/**
	 * 获取连接的URL中URI信息
	 *
	 * @param conn HttpConnection
	 * @return URI
	 */
	private static URI getURI(final JdkHttpConnection conn) {
		return UrlUtil.toURI(conn.getUrl());
	}
}
