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

package org.dromara.hutool.http.client.engine.okhttp;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存CookieStore实现，用于OkHttp3
 *
 * @author Looly
 * @since 6.0.0
 */
public class InMemoryOkCookieStore implements OkCookieStore {

	private final HashMap<String, ConcurrentHashMap<String, Cookie>> cookies;

	/**
	 * 构造
	 */
	public InMemoryOkCookieStore() {
		this.cookies = new HashMap<>();
	}

	@Override
	public void add(final HttpUrl httpUrl, final Cookie cookie) {
		if (!cookie.persistent()) {
			return;
		}

		final String name = this.cookieName(cookie);
		final String hostKey = httpUrl.host();

		if (!this.cookies.containsKey(hostKey)) {
			this.cookies.put(hostKey, new ConcurrentHashMap<>());
		}
		cookies.get(hostKey).put(name, cookie);
	}

	@Override
	public void add(final HttpUrl httpUrl, final List<Cookie> cookies) {
		for (final Cookie cookie : cookies) {
			if (isCookieExpired(cookie)) {
				continue;
			}
			this.add(httpUrl, cookie);
		}
	}

	@Override
	public List<Cookie> get(final HttpUrl httpUrl) {
		return this.get(httpUrl.host());
	}

	@Override
	public List<Cookie> getCookies() {
		final ArrayList<Cookie> result = new ArrayList<>();

		for (final String hostKey : this.cookies.keySet()) {
			result.addAll(this.get(hostKey));
		}

		return result;
	}

	/**
	 * 获取cookie集合
	 */
	private List<Cookie> get(final String hostKey) {
		final ArrayList<Cookie> result = new ArrayList<>();

		if (this.cookies.containsKey(hostKey)) {
			final Collection<Cookie> cookies = this.cookies.get(hostKey).values();
			for (final Cookie cookie : cookies) {
				if (isCookieExpired(cookie)) {
					this.remove(hostKey, cookie);
				} else {
					result.add(cookie);
				}
			}
		}
		return result;
	}

	@Override
	public boolean remove(final HttpUrl httpUrl, final Cookie cookie) {
		return this.remove(httpUrl.host(), cookie);
	}

	/**
	 * 从缓存中移除cookie
	 *
	 * @param hostKey hostKey
	 * @param cookie  cookie
	 */
	private boolean remove(final String hostKey, final Cookie cookie) {
		final String name = this.cookieName(cookie);
		if (this.cookies.containsKey(hostKey) && this.cookies.get(hostKey).containsKey(name)) {
			// 从内存中移除httpUrl对应的cookie
			this.cookies.get(hostKey).remove(name);
			return true;
		}
		return false;
	}

	@Override
	public boolean removeAll() {
		this.cookies.clear();
		return true;
	}

	/**
	 * 判断cookie是否失效
	 */
	private boolean isCookieExpired(final Cookie cookie) {
		return cookie.expiresAt() < System.currentTimeMillis();
	}

	/**
	 * 获取cookie的key<br>
	 * 格式：name + domain
	 *
	 * @param cookie cookie
	 * @return cookie的key
	 */
	private String cookieName(final Cookie cookie) {
		return cookie == null ? null : cookie.name() + cookie.domain();
	}
}
