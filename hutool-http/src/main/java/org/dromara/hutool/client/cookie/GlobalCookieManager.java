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

package org.dromara.hutool.client.cookie;

import org.dromara.hutool.io.IORuntimeException;
import org.dromara.hutool.map.MapUtil;
import org.dromara.hutool.net.url.URLUtil;
import org.dromara.hutool.client.engine.jdk.JdkHttpConnection;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局Cookie管理器，只针对Hutool请求有效
 *
 * @author Looly
 * @since 4.5.15
 */
public class GlobalCookieManager {

	/** Cookie管理 */
	private static CookieManager cookieManager;
	static {
		cookieManager = new CookieManager(new ThreadLocalCookieStore(), CookiePolicy.ACCEPT_ALL);
	}

	/**
	 * 关闭Cookie
	 *
	 * @see GlobalCookieManager#setCookieManager(CookieManager)
	 * @since 5.6.5
	 */
	public static void closeCookie() {
		setCookieManager(null);
	}

	/**
	 * 自定义{@link CookieManager}
	 *
	 * @param customCookieManager 自定义的{@link CookieManager}
	 */
	public static void setCookieManager(final CookieManager customCookieManager) {
		cookieManager = customCookieManager;
	}

	/**
	 * 获取全局{@link CookieManager}
	 *
	 * @return {@link CookieManager}
	 */
	public static CookieManager getCookieManager() {
		return cookieManager;
	}

	/**
	 * 获取指定域名下所有Cookie信息
	 *
	 * @param conn HTTP连接
	 * @return Cookie信息列表
	 * @since 4.6.9
	 */
	public static List<HttpCookie> getCookies(final JdkHttpConnection conn){
		return cookieManager.getCookieStore().get(getURI(conn));
	}

	/**
	 * 将本地存储的Cookie信息附带到Http请求中，不覆盖用户定义好的Cookie
	 *
	 * @param conn {@link JdkHttpConnection}
	 */
	public static void add(final JdkHttpConnection conn) {
		if(null == cookieManager) {
			// 全局Cookie管理器关闭
			return;
		}

		final Map<String, List<String>> cookieHeader;
		try {
			cookieHeader = cookieManager.get(getURI(conn), new HashMap<>(0));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}


		// 不覆盖模式回填Cookie头，这样用户定义的Cookie将优先
		conn.header(cookieHeader, false);
	}

	/**
	 * 存储响应的Cookie信息到本地
	 *
	 * @param conn {@link JdkHttpConnection}
	 */
	public static void store(final JdkHttpConnection conn) {
		store(conn, conn.headers());
	}

	/**
	 * 存储响应的Cookie信息到本地<br>
	 * 通过读取
	 *
	 * @param conn {@link JdkHttpConnection}
	 * @param responseHeaders 头信息Map
	 */
	public static void store(final JdkHttpConnection conn, final Map<String, List<String>> responseHeaders) {
		if(null == cookieManager || MapUtil.isEmpty(responseHeaders)) {
			// 全局Cookie管理器关闭或头信息为空
			return;
		}

		try {
			cookieManager.put(getURI(conn), responseHeaders);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获取连接的URL中URI信息
	 * @param conn HttpConnection
	 * @return URI
	 */
	private static URI getURI(final JdkHttpConnection conn){
		return URLUtil.toURI(conn.getUrl());
	}
}
