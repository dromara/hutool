/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.http.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import org.dromara.hutool.core.lang.wrapper.Wrapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

/**
 * {@link HttpExchange}包装类，提供增强方法和缓存
 *
 * @author looly
 */
public class HttpExchangeWrapper extends HttpExchange implements Wrapper<HttpExchange> {

	private final HttpExchange raw;
	private final HttpServerRequest request;
	private final HttpServerResponse response;

	/**
	 * 构造
	 *
	 * @param raw {@link HttpExchange}
	 */
	public HttpExchangeWrapper(final HttpExchange raw) {
		this.raw = raw;
		this.request = new HttpServerRequest(this);
		this.response = new HttpServerResponse(this);
	}

	@Override
	public HttpExchange getRaw() {
		return this.raw;
	}

	/**
	 * 获取请求
	 *
	 * @return 请求
	 */
	public HttpServerRequest getRequest() {
		return request;
	}

	/**
	 * 获取响应
	 *
	 * @return 响应
	 */
	public HttpServerResponse getResponse() {
		return response;
	}

	// region ----- HttpExchange methods
	@Override
	public Headers getRequestHeaders() {
		return this.raw.getRequestHeaders();
	}

	@Override
	public Headers getResponseHeaders() {
		return this.raw.getResponseHeaders();
	}

	@Override
	public URI getRequestURI() {
		return this.raw.getRequestURI();
	}

	@Override
	public String getRequestMethod() {
		return this.raw.getRequestMethod();
	}

	@Override
	public HttpContext getHttpContext() {
		return this.raw.getHttpContext();
	}

	@Override
	public void close() {
		this.raw.close();
	}

	@Override
	public InputStream getRequestBody() {
		return this.raw.getRequestBody();
	}

	@Override
	public OutputStream getResponseBody() {
		return this.raw.getResponseBody();
	}

	@Override
	public void sendResponseHeaders(final int rCode, final long responseLength) throws IOException {
		this.raw.sendResponseHeaders(rCode, responseLength);
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		return this.raw.getRemoteAddress();
	}

	@Override
	public int getResponseCode() {
		return this.raw.getResponseCode();
	}

	@Override
	public InetSocketAddress getLocalAddress() {
		return this.raw.getLocalAddress();
	}

	@Override
	public String getProtocol() {
		return this.raw.getProtocol();
	}

	@Override
	public Object getAttribute(final String name) {
		return this.raw.getAttribute(name);
	}

	@Override
	public void setAttribute(final String name, final Object value) {
		this.raw.setAttribute(name, value);
	}

	@Override
	public void setStreams(final InputStream i, final OutputStream o) {
		this.raw.setStreams(i, o);
	}

	@Override
	public HttpPrincipal getPrincipal() {
		return this.raw.getPrincipal();
	}
	// endregion
}
