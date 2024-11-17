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

package org.dromara.hutool.http.server.engine.sun;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.http.meta.ContentType;
import org.dromara.hutool.http.meta.HttpStatus;
import org.dromara.hutool.http.server.ServerResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * SunHttp服务器响应对象
 *
 * @author looly
 * @since 6.0.0
 */
public class SunServerResponse extends SunServerBase implements ServerResponse {

	private Charset charset;
	/**
	 * 是否已经发送了Http状态码，如果没有，提前写出状态码
	 */
	private boolean isSendCode;

	/**
	 * 构造
	 *
	 * @param httpExchange {@link HttpExchange}
	 */
	public SunServerResponse(final HttpExchange httpExchange) {
		super(httpExchange);
	}

	@Override
	public SunServerResponse setStatus(final int statusCode) {
		return send(statusCode, 0);
	}

	/**
	 * 发送成功状态码
	 *
	 * @return this
	 */
	public SunServerResponse sendOk() {
		return setStatus(HttpStatus.HTTP_OK);
	}

	/**
	 * 发送成功状态码
	 *
	 * @param bodyLength 响应体长度，默认0表示不定长度，会输出Transfer-encoding: chunked
	 * @return this
	 * @since 5.5.7
	 */
	public SunServerResponse sendOk(final int bodyLength) {
		return send(HttpStatus.HTTP_OK, bodyLength);
	}

	/**
	 * 发送404错误页
	 *
	 * @param content 错误页页面内容，默认text/html类型
	 * @return this
	 */
	public SunServerResponse send404(final String content) {
		return sendError(HttpStatus.HTTP_NOT_FOUND, content);
	}

	/**
	 * 发送错误页
	 *
	 * @param errorCode HTTP错误状态码，见HttpStatus
	 * @param content   错误页页面内容，默认text/html类型
	 * @return this
	 */
	@SuppressWarnings("resource")
	public SunServerResponse sendError(final int errorCode, final String content) {
		setStatus(errorCode);
		setContentType(ContentType.TEXT_HTML.toString());
		write(content);
		return this;
	}

	/**
	 * 发送HTTP状态码
	 *
	 * @param httpStatusCode HTTP状态码，见HttpStatus
	 * @param bodyLength     响应体长度，默认0表示不定长度，会输出Transfer-encoding: chunked
	 * @return this
	 */
	public SunServerResponse send(final int httpStatusCode, final long bodyLength) {
		if (this.isSendCode) {
			throw new IORuntimeException("Http status code has been send!");
		}

		try {
			this.httpExchange.sendResponseHeaders(httpStatusCode, bodyLength);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		this.isSendCode = true;
		return this;
	}

	@Override
	public SunServerResponse setCharset(final Charset charset) {
		this.charset = charset;
		return this;
	}

	@Override
	public Charset getCharset() {
		return this.charset;
	}

	/**
	 * 获得所有响应头，获取后可以添加新的响应头
	 *
	 * @return 响应头
	 */
	public Headers getHeaders() {
		return this.httpExchange.getResponseHeaders();
	}

	@Override
	public SunServerResponse addHeader(final String header, final String value) {
		getHeaders().add(header, value);
		return this;
	}

	@Override
	public SunServerResponse setHeader(final String header, final String value) {
		getHeaders().set(header, value);
		return this;
	}

	/**
	 * 设置响应头，如果已经存在，则覆盖
	 *
	 * @param header 头key
	 * @param value  值列表
	 * @return this
	 */
	public SunServerResponse setHeader(final String header, final List<String> value) {
		getHeaders().put(header, value);
		return this;
	}

	/**
	 * 设置所有响应头，如果已经存在，则覆盖
	 *
	 * @param headers 响应头map
	 * @return this
	 */
	public SunServerResponse setHeaders(final Map<String, List<String>> headers) {
		getHeaders().putAll(headers);
		return this;
	}

	/**
	 * 设置属性
	 *
	 * @param name  属性名
	 * @param value 属性值
	 * @return this
	 */
	public SunServerResponse setAttr(final String name, final Object value) {
		this.httpExchange.setAttribute(name, value);
		return this;
	}

	@SuppressWarnings("resource")
	@Override
	public OutputStream getOutputStream() {
		if (!this.isSendCode) {
			sendOk();
		}
		return this.httpExchange.getResponseBody();
	}

	/**
	 * 写出数据到客户端
	 *
	 * @param in     数据流
	 * @param length 指定响应内容长度，默认0表示不定长度，会输出Transfer-encoding: chunked
	 * @return this
	 */
	@SuppressWarnings("resource")
	public SunServerResponse write(final InputStream in, final int length) {
		if (!isSendCode) {
			sendOk(Math.max(0, length));
		}
		OutputStream out = null;
		try {
			out = this.httpExchange.getResponseBody();
			IoUtil.copy(in, out);
		} finally {
			IoUtil.closeQuietly(out);
			IoUtil.closeQuietly(in);
		}
		return this;
	}
}
