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

import io.undertow.io.Sender;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import org.dromara.hutool.http.server.handler.ServerResponse;

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Undertow响应对象
 *
 * @author looly
 * @since 6.0.0
 */
public class UndertowResponse extends UndertowExchangeBase implements ServerResponse {

	private Charset charset;

	/**
	 * 构造
	 *
	 * @param exchange Undertow exchange
	 */
	public UndertowResponse(final HttpServerExchange exchange) {
		super(exchange);
		this.charset = DEFAULT_CHARSET;
	}

	/**
	 * 获取原始的HttpServerExchange对象
	 *
	 * @return HttpServerExchange对象
	 */
	public HttpServerExchange getExchange() {
		return exchange;
	}

	@Override
	public ServerResponse setStatus(final int statusCode) {
		this.exchange.setStatusCode(statusCode);
		return this;
	}

	@Override
	public ServerResponse setCharset(final Charset charset) {
		this.charset = charset;
		return this;
	}

	@Override
	public Charset getCharset() {
		return this.charset;
	}

	@Override
	public ServerResponse addHeader(final String header, final String value) {
		this.exchange.getResponseHeaders().add(new HttpString(header), value);
		return this;
	}

	@Override
	public ServerResponse setHeader(final String header, final String value) {
		this.exchange.getResponseHeaders().put(new HttpString(header), value);
		return this;
	}

	@Override
	public ServerResponse setHeader(final String header, final List<String> value) {
		this.exchange.getResponseHeaders().putAll(new HttpString(header), value);
		return this;
	}

	/**
	 * 获取Sender对象，用于发送数据
	 *
	 * @return Sender对象
	 */
	public Sender getSender() {
		return this.exchange.getResponseSender();
	}

	@Override
	public OutputStream getOutputStream() {
		return this.exchange.getOutputStream();
	}

	@Override
	public ServerResponse write(final byte[] data) {
		getSender().send(ByteBuffer.wrap(data));
		return this;
	}
}
