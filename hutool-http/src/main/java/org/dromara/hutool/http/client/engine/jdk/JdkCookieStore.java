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

import org.dromara.hutool.core.lang.wrapper.SimpleWrapper;
import org.dromara.hutool.http.client.cookie.CookieSpi;
import org.dromara.hutool.http.client.cookie.CookieStoreSpi;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * JDK CookieStore实现
 *
 * @author looly
 * @since 6.0.0
 */
public class JdkCookieStore extends SimpleWrapper<CookieStoreSpi> implements CookieStore {
	/**
	 * 构造
	 *
	 * @param raw 原始对象
	 */
	public JdkCookieStore(final CookieStoreSpi raw) {
		super(raw);
	}

	@Override
	public void add(final URI uri, final HttpCookie cookie) {
		this.raw.add(uri, new JdkCookie(cookie));
	}

	@Override
	public List<HttpCookie> get(final URI uri) {
		final List<CookieSpi> cookieSpis = this.raw.get(uri);
		if (null == cookieSpis) {
			return null;
		}

		final List<HttpCookie> result = new ArrayList<>(cookieSpis.size());
		for (final CookieSpi cookieSpi : cookieSpis) {
			result.add(((JdkCookie) cookieSpi).getRaw());
		}
		return result;
	}

	@Override
	public List<HttpCookie> getCookies() {
		final List<CookieSpi> cookieSpis = this.raw.getCookies();
		if (null == cookieSpis) {
			return null;
		}

		final List<HttpCookie> result = new ArrayList<>(cookieSpis.size());
		for (final CookieSpi cookieSpi : cookieSpis) {
			result.add(((JdkCookie) cookieSpi).getRaw());
		}
		return result;
	}

	@Override
	public List<URI> getURIs() {
		return this.raw.getURIs();
	}

	@Override
	public boolean remove(final URI uri, final HttpCookie cookie) {
		return false;
	}

	@Override
	public boolean removeAll() {
		return false;
	}
}
