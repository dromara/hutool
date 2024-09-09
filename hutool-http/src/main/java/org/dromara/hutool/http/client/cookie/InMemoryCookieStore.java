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

package org.dromara.hutool.http.client.cookie;

import org.dromara.hutool.http.HttpException;
import org.dromara.hutool.log.Log;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于内存的Cookie存储实现，线程安全
 *
 * @author looly
 * @since 6.0.0
 */
public class InMemoryCookieStore implements CookieStoreSpi {
	private static final Log LOG = Log.get();

	private final Map<String, ConcurrentHashMap<String, CookieSpi>> cookies;

	/**
	 * 构造
	 */
	public InMemoryCookieStore() {
		this.cookies = new ConcurrentHashMap<>();
	}

	@Override
	public List<URI> getURIs() {
		final Set<String> keySet = cookies.keySet();
		final List<URI> uris = new ArrayList<>(keySet.size());

		try {
			for (final String hostKey : keySet) {
				uris.add(new URI("http", hostKey, null, null));
			}
		} catch (final URISyntaxException e) {
			throw new HttpException(e);
		}
		return uris;
	}

	@Override
	public void add(final URI httpUrl, final CookieSpi cookie) {
		if (null == cookie || !cookie.isPersistent() || cookie.isExpired()) {
			return;
		}

		final String hostKey = httpUrl.getHost();
		this.cookies.computeIfAbsent(hostKey, k -> new ConcurrentHashMap<>());

		final String cookieKey = this.cookieKey(cookie);
		LOG.debug("Add cookie {}: {}", cookieKey, cookie);
		cookies.get(hostKey).put(cookieKey, cookie);
	}

	@Override
	public List<CookieSpi> get(final URI httpUrl) {
		final String hostKey = httpUrl.getHost();
		final List<CookieSpi> result = this.get(httpUrl.getHost());
		LOG.debug("Get cookies {}: {}", hostKey, result);
		return result;
	}

	@Override
	public List<CookieSpi> getCookies() {
		final List<CookieSpi> result = new ArrayList<>();
		for (final String hostKey : this.cookies.keySet()) {
			result.addAll(this.get(hostKey));
		}
		return result;
	}

	@Override
	public boolean remove(final URI httpUrl, final CookieSpi cookie) {
		return this.remove(httpUrl.getHost(), cookie);
	}

	@Override
	public boolean clear() {
		this.cookies.clear();
		return true;
	}

	/**
	 * 获取cookie集合
	 */
	private List<CookieSpi> get(final String hostKey) {
		final List<CookieSpi> result = new ArrayList<>();

		if (this.cookies.containsKey(hostKey)) {
			final Collection<CookieSpi> cookies = this.cookies.get(hostKey).values();
			for (final CookieSpi cookie : cookies) {
				if (cookie.isExpired()) {
					this.remove(hostKey, cookie);
				} else {
					result.add(cookie);
				}
			}
		}
		return result;
	}

	/**
	 * 从缓存中移除cookie
	 *
	 * @param hostKey hostKey
	 * @param cookie  cookie
	 */
	private boolean remove(final String hostKey, final CookieSpi cookie) {
		final String cookieKey = this.cookieKey(cookie);
		if (this.cookies.containsKey(hostKey) && this.cookies.get(hostKey).containsKey(cookieKey)) {
			// 从内存中移除httpUrl对应的cookie
			this.cookies.get(hostKey).remove(cookieKey);
			return true;
		}
		return false;
	}

	/**
	 * 获取cookie的key<br>
	 * 格式：name + domain
	 *
	 * @param cookie cookie
	 * @return cookie的key
	 */
	private String cookieKey(final CookieSpi cookie) {
		return cookie == null ? null : cookie.getName() + cookie.getDomain();
	}
}
