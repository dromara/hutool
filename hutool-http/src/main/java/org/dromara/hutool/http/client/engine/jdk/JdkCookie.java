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
import org.dromara.hutool.core.reflect.method.MethodUtil;
import org.dromara.hutool.http.client.cookie.CookieSpi;

import java.net.HttpCookie;
import java.time.Instant;

/**
 * JDK Cookie实现
 *
 * @author looly
 * @since 6.0.0
 */
public class JdkCookie extends SimpleWrapper<HttpCookie> implements CookieSpi {

	/**
	 * 构造
	 *
	 * @param httpCookie 原始对象
	 */
	public JdkCookie(final HttpCookie httpCookie) {
		super(httpCookie);
	}

	@Override
	public String getName() {
		return raw.getName();
	}

	@Override
	public String getValue() {
		return raw.getValue();
	}

	@Override
	public boolean isSecure() {
		return raw.getSecure();
	}

	@Override
	public boolean isHttpOnly() {
		return raw.isHttpOnly();
	}

	@Override
	public boolean isHostOnly() {
		return false;
	}

	@Override
	public String getDomain() {
		return raw.getDomain();
	}

	@Override
	public String getPath() {
		return raw.getPath();
	}

	@Override
	public boolean isExpired() {
		return raw.hasExpired();
	}

	@Override
	public boolean isExpired(final Instant now) {
		final long maxAge = raw.getMaxAge();
		if (maxAge == 0) {
			return true;
		}

		if (maxAge < 0) {
			return false;
		}

		final long creationTime = MethodUtil.invoke(raw, "getCreationTime");
		final long deltaSecond = (now.toEpochMilli() - creationTime) / 1000;
		return deltaSecond > maxAge;
	}

	@Override
	public String getAttribute(final String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isPersistent() {
		return true;
	}
}
