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

package org.dromara.hutool.http.client.cookie;

import java.net.URI;
import java.util.List;

/**
 * 线程隔离的Cookie存储。多线程环境下Cookie隔离使用，防止Cookie覆盖<br>
 *
 * 见：https://stackoverflow.com/questions/16305486/cookiemanager-for-multiple-threads
 *
 * @author looly
 * @since 6.0.0
 */
public class ThreadLocalCookieStore implements CookieStoreSpi {

	private final static ThreadLocal<CookieStoreSpi> STORES = new ThreadLocal<CookieStoreSpi>() {
		@Override
		protected synchronized CookieStoreSpi initialValue() {
			/* InMemoryCookieStore */
			return new InMemoryCookieStore();
		}
	};

	/**
	 * 获取本线程下的CookieStore
	 *
	 * @return CookieStore
	 */
	public CookieStoreSpi getCookieStore() {
		return STORES.get();
	}

	/**
	 * 移除当前线程的Cookie
	 *
	 * @return this
	 */
	public ThreadLocalCookieStore removeCurrent() {
		STORES.remove();
		return this;
	}

	@Override
	public List<URI> getURIs() {
		return getCookieStore().getURIs();
	}

	@Override
	public void add(final URI httpUrl, final CookieSpi cookie) {
		getCookieStore().add(httpUrl, cookie);
	}

	@Override
	public List<CookieSpi> get(final URI httpUrl) {
		return getCookieStore().get(httpUrl);
	}

	@Override
	public List<CookieSpi> getCookies() {
		return getCookieStore().getCookies();
	}

	@Override
	public boolean remove(final URI httpUrl, final CookieSpi cookie) {
		return getCookieStore().remove(httpUrl, cookie);
	}

	@Override
	public boolean clear() {
		return getCookieStore().clear();
	}
}
