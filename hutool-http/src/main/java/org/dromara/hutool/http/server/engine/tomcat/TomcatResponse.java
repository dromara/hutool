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

package org.dromara.hutool.http.server.engine.tomcat;

import org.apache.catalina.connector.Response;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.http.server.handler.ServerResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Tomcat响应对象包装
 *
 * @author Looly
 * @since 6.0.0
 */
public class TomcatResponse implements ServerResponse {

	private final Response response;
	private Charset charset;

	/**
	 * 构造
	 *
	 * @param response Tomcat响应对象
	 */
	public TomcatResponse(final Response response) {
		this.response = response;
	}

	@Override
	public TomcatResponse setStatus(final int statusCode) {
		response.setStatus(statusCode);
		return this;
	}

	@Override
	public TomcatResponse setCharset(final Charset charset) {
		this.charset = charset;
		return this;
	}

	@Override
	public Charset getCharset() {
		return this.charset;
	}

	@Override
	public TomcatResponse addHeader(final String header, final String value) {
		this.response.addHeader(header, value);
		return this;
	}

	@Override
	public TomcatResponse setHeader(final String header, final String value) {
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
