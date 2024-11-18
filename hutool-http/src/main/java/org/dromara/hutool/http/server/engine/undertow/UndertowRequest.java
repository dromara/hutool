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

package org.dromara.hutool.http.server.engine.undertow;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.http.server.handler.ServerRequest;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Undertow请求对象
 *
 * @author looly
 * @since 6.0.0
 */
public class UndertowRequest extends UndertowExchangeBase implements ServerRequest {

	/**
	 * 构造
	 *
	 * @param exchange Undertow请求对象
	 */
	public UndertowRequest(final HttpServerExchange exchange) {
		super(exchange);
	}

	@Override
	public String getMethod() {
		return this.exchange.getRequestMethod().toString();
	}

	@Override
	public String getPath() {
		return this.exchange.getRelativePath();
	}

	@Override
	public String getQuery() {
		return this.exchange.getQueryString();
	}

	@Override
	public String getHeader(final String name) {
		return this.exchange.getRequestHeaders().getFirst(name);
	}

	/**
	 * 获取所有请求头
	 *
	 * @return 请求头
	 */
	public HeaderMap getHeaders() {
		return this.exchange.getRequestHeaders();
	}

	@Override
	public Charset getCharset() {
		return CharsetUtil.charset(this.exchange.getRequestCharset());
	}

	@Override
	public InputStream getBodyStream() {
		return this.exchange.getInputStream();
	}
}
