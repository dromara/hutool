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
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.List;

/**
 * CookieJar实现，用于OkHttp3
 *
 * @author Looly
 * @since 6.0.0
 */
public class CookieJarImpl implements CookieJar {

	private final OkCookieStore cookieStore;

	/**
	 * 构造
	 */
	public CookieJarImpl() {
		this(new InMemoryOkCookieStore());
	}

	/**
	 * 获取Cookie存储器
	 *
	 * @return Cookie存储器
	 */
	public OkCookieStore getCookieStore() {
		return this.cookieStore;
	}

	/**
	 * 构造
	 *
	 * @param cookieStore Cookie存储器，用于自定义Cookie存储实现
	 */
	public CookieJarImpl(final OkCookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}

	@Override
	public List<Cookie> loadForRequest(final HttpUrl httpUrl) {
		return this.cookieStore.get(httpUrl);
	}

	@Override
	public void saveFromResponse(final HttpUrl httpUrl, final List<Cookie> list) {
		this.cookieStore.add(httpUrl, list);
	}
}
