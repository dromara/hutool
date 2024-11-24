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

package org.dromara.hutool.http.server.servlet;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.http.server.handler.ServerResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Jakarta Servlet响应对象包装
 *
 * @author Looly
 */
public class ServletResponse implements ServerResponse {

	private final HttpServletResponse response;
	private Charset charset;

	/**
	 * 构造
	 *
	 * @param response Jetty响应对象
	 */
	public ServletResponse(final HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public ServletResponse setStatus(final int statusCode) {
		this.response.setStatus(statusCode);
		return this;
	}

	@Override
	public ServletResponse setCharset(final Charset charset) {
		this.charset = charset;
		return this;
	}

	@Override
	public Charset getCharset() {
		return this.charset;
	}

	@Override
	public ServletResponse addHeader(final String header, final String value) {
		this.response.addHeader(header, value);
		return this;
	}

	@Override
	public ServletResponse setHeader(final String header, final String value) {
		this.response.setHeader(header, value);
		return this;
	}

	/**
	 * 增加Cookie
	 *
	 * @param cookie Cookie对象
	 * @return this
	 */
	public ServletResponse addCookie(final Cookie cookie){
		ServletUtil.addCookie(this.response, cookie);
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
