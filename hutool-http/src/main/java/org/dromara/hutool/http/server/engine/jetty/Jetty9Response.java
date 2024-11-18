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

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.http.server.ServerResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Jetty响应对象包装
 *
 * @author Looly
 */
public class Jetty9Response implements ServerResponse {

	private final HttpServletResponse response;
	private Charset charset;

	/**
	 * 构造
	 *
	 * @param response Jetty响应对象
	 */
	public Jetty9Response(final HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public Jetty9Response setStatus(final int statusCode) {
		this.response.setStatus(statusCode);
		return this;
	}

	@Override
	public Jetty9Response setCharset(final Charset charset) {
		this.charset = charset;
		return this;
	}

	@Override
	public Charset getCharset() {
		return this.charset;
	}

	@Override
	public Jetty9Response addHeader(final String header, final String value) {
		this.response.addHeader(header, value);
		return this;
	}

	@Override
	public Jetty9Response setHeader(final String header, final String value) {
		this.response.setHeader(header, value);
		return this;
	}

	@Override
	public OutputStream getOutputStream() {
		try {
			return this.response.getOutputStream();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
