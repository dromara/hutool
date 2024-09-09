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

package org.dromara.hutool.http.client.engine.httpclient5;

import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.dromara.hutool.core.lang.wrapper.SimpleWrapper;
import org.dromara.hutool.http.HttpException;
import org.dromara.hutool.http.client.cookie.CookieSpi;
import org.dromara.hutool.http.client.cookie.CookieStoreSpi;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * HttpClient5 Cookie存储器实现
 *
 * @author looly
 * @since 6.0.0
 */
public class HttpClient5CookieStore extends SimpleWrapper<CookieStoreSpi> implements CookieStore {

	/**
	 * 构造
	 *
	 * @param cookieStore Cookie存储器
	 */
	public HttpClient5CookieStore(final CookieStoreSpi cookieStore) {
		super(cookieStore);
	}

	@Override
	public void addCookie(final Cookie cookie) {
		final URI uri;
		try {
			uri = new URI("http", cookie.getDomain(), cookie.getPath(), null);
		} catch (final URISyntaxException e) {
			throw new HttpException(e);
		}

		this.raw.add(uri, new HttpClient5Cookie(cookie));
	}

	@Override
	public List<Cookie> getCookies() {
		final List<CookieSpi> cookies = this.raw.getCookies();
		final List<Cookie> result = new ArrayList<>(cookies.size());
		for (final CookieSpi cookie : cookies) {
			result.add(((HttpClient5Cookie) cookie).getRaw());
		}
		return result;
	}

	@Override
	public boolean clearExpired(final Date date) {
		// get时检查过期
		this.raw.getCookies();
		return true;
	}

	@Override
	public void clear() {
		this.raw.clear();
	}
}
