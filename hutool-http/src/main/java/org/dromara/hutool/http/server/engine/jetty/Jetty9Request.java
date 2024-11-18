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

package org.dromara.hutool.http.server.engine.jetty;

import org.dromara.hutool.http.server.handler.ServerRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * Jetty请求对象包装
 *
 * @author Looly
 */
public class Jetty9Request implements ServerRequest {

	private final HttpServletRequest request;

	/**
	 * 构造
	 *
	 * @param request Jetty请求对象
	 */
	public Jetty9Request(final HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public String getMethod() {
		return this.request.getMethod();
	}

	@Override
	public String getPath() {
		return this.request.getContextPath();
	}

	@Override
	public String getQuery() {
		return this.request.getQueryString();
	}

	@Override
	public String getHeader(final String name) {
		return this.request.getHeader(name);
	}

	@Override
	public InputStream getBodyStream() {
		try {
			return this.request.getInputStream();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
