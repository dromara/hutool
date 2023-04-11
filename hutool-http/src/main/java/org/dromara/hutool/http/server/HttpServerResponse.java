/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.http.server;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.net.url.URLEncoder;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.http.meta.ContentType;
import org.dromara.hutool.http.meta.Header;
import org.dromara.hutool.http.meta.HttpStatus;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Http响应对象，用于写出数据到客户端
 */
@SuppressWarnings("resource")
public class HttpServerResponse extends HttpServerBase {

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
	public HttpServerResponse(final HttpExchange httpExchange) {
		super(httpExchange);
	}

	/**
	 * 发送HTTP状态码，Content-Length为0不定长度，会输出Transfer-encoding: chunked
	 *
	 * @param httpStatusCode HTTP状态码，见HttpStatus
	 * @return this
	 */
	public HttpServerResponse send(final int httpStatusCode) {
		return send(httpStatusCode, 0);
	}

	/**
	 * 发送成功状态码
	 *
	 * @return this
	 */
	public HttpServerResponse sendOk() {
		return send(HttpStatus.HTTP_OK);
	}

	/**
	 * 发送成功状态码
	 *
	 * @param bodyLength 响应体长度，默认0表示不定长度，会输出Transfer-encoding: chunked
	 * @return this
	 * @since 5.5.7
	 */
	public HttpServerResponse sendOk(final int bodyLength) {
		return send(HttpStatus.HTTP_OK, bodyLength);
	}

	/**
	 * 发送404错误页
	 *
	 * @param content 错误页页面内容，默认text/html类型
	 * @return this
	 */
	public HttpServerResponse send404(final String content) {
		return sendError(HttpStatus.HTTP_NOT_FOUND, content);
	}

	/**
	 * 发送错误页
	 *
	 * @param errorCode HTTP错误状态码，见HttpStatus
	 * @param content   错误页页面内容，默认text/html类型
	 * @return this
	 */
	public HttpServerResponse sendError(final int errorCode, final String content) {
		send(errorCode);
		setContentType(ContentType.TEXT_HTML.toString());
		return write(content);
	}

	/**
	 * 发送HTTP状态码
	 *
	 * @param httpStatusCode HTTP状态码，见HttpStatus
	 * @param bodyLength     响应体长度，默认0表示不定长度，会输出Transfer-encoding: chunked
	 * @return this
	 */
	public HttpServerResponse send(final int httpStatusCode, final long bodyLength) {
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

	/**
	 * 获得所有响应头，获取后可以添加新的响应头
	 *
	 * @return 响应头
	 */
	public Headers getHeaders() {
		return this.httpExchange.getResponseHeaders();
	}

	/**
	 * 添加响应头，如果已经存在，则追加
	 *
	 * @param header 头key
	 * @param value  值
	 * @return this
	 */
	public HttpServerResponse addHeader(final String header, final String value) {
		getHeaders().add(header, value);
		return this;
	}

	/**
	 * 设置响应头，如果已经存在，则覆盖
	 *
	 * @param header 头key
	 * @param value  值
	 * @return this
	 */
	public HttpServerResponse setHeader(final Header header, final String value) {
		return setHeader(header.getValue(), value);
	}

	/**
	 * 设置响应头，如果已经存在，则覆盖
	 *
	 * @param header 头key
	 * @param value  值
	 * @return this
	 */
	public HttpServerResponse setHeader(final String header, final String value) {
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
	public HttpServerResponse setHeader(final String header, final List<String> value) {
		getHeaders().put(header, value);
		return this;
	}

	/**
	 * 设置所有响应头，如果已经存在，则覆盖
	 *
	 * @param headers 响应头map
	 * @return this
	 */
	public HttpServerResponse setHeaders(final Map<String, List<String>> headers) {
		getHeaders().putAll(headers);
		return this;
	}

	/**
	 * 设置Content-Type头，类似于:text/html;charset=utf-8<br>
	 * 如果用户传入的信息无charset信息，自动根据charset补充，charset设置见{@link #setCharset(Charset)}
	 *
	 * @param contentType Content-Type头内容
	 * @return this
	 */
	public HttpServerResponse setContentType(String contentType) {
		if (null != contentType && null != this.charset) {
			if (! contentType.contains(";charset=")) {
				contentType = ContentType.build(contentType, this.charset);
			}
		}

		return setHeader(Header.CONTENT_TYPE, contentType);
	}

	/**
	 * 设置Content-Length头
	 *
	 * @param contentLength Content-Length头内容
	 * @return this
	 */
	public HttpServerResponse setContentLength(final long contentLength) {
		return setHeader(Header.CONTENT_LENGTH, String.valueOf(contentLength));
	}

	/**
	 * 设置响应的编码
	 *
	 * @param charset 编码
	 * @return this
	 */
	public HttpServerResponse setCharset(final Charset charset) {
		this.charset = charset;
		return this;
	}

	/**
	 * 设置属性
	 *
	 * @param name  属性名
	 * @param value 属性值
	 * @return this
	 */
	public HttpServerResponse setAttr(final String name, final Object value) {
		this.httpExchange.setAttribute(name, value);
		return this;
	}

	/**
	 * 获取响应数据流
	 *
	 * @return 响应数据流
	 */
	public OutputStream getOut() {
		if (! this.isSendCode) {
			sendOk();
		}
		return this.httpExchange.getResponseBody();
	}

	/**
	 * 获取响应数据流
	 *
	 * @return 响应数据流
	 */
	public PrintWriter getWriter() {
		final Charset charset = ObjUtil.defaultIfNull(this.charset, DEFAULT_CHARSET);
		return new PrintWriter(new OutputStreamWriter(getOut(), charset));
	}

	/**
	 * 写出数据到客户端
	 *
	 * @param data        数据
	 * @param contentType Content-Type类型
	 * @return this
	 */
	public HttpServerResponse write(final String data, final String contentType) {
		setContentType(contentType);
		return write(data);
	}

	/**
	 * 写出数据到客户端
	 *
	 * @param data 数据
	 * @return this
	 */
	public HttpServerResponse write(final String data) {
		final Charset charset = ObjUtil.defaultIfNull(this.charset, DEFAULT_CHARSET);
		return write(ByteUtil.toBytes(data, charset));
	}

	/**
	 * 写出数据到客户端
	 *
	 * @param data        数据
	 * @param contentType 返回的类型
	 * @return this
	 */
	public HttpServerResponse write(final byte[] data, final String contentType) {
		setContentType(contentType);
		return write(data);
	}

	/**
	 * 写出数据到客户端
	 *
	 * @param data 数据
	 * @return this
	 */
	public HttpServerResponse write(final byte[] data) {
		final ByteArrayInputStream in = new ByteArrayInputStream(data);
		return write(in, in.available());
	}

	/**
	 * 返回数据给客户端
	 *
	 * @param in          需要返回客户端的内容
	 * @param contentType 返回的类型
	 * @return this
	 * @since 5.2.6
	 */
	public HttpServerResponse write(final InputStream in, final String contentType) {
		return write(in, 0, contentType);
	}

	/**
	 * 返回数据给客户端
	 *
	 * @param in          需要返回客户端的内容
	 * @param length 内容长度，默认0表示不定长度，会输出Transfer-encoding: chunked
	 * @param contentType 返回的类型
	 * @return this
	 * @since 5.2.7
	 */
	public HttpServerResponse write(final InputStream in, final int length, final String contentType) {
		setContentType(contentType);
		return write(in, length);
	}

	/**
	 * 写出数据到客户端
	 *
	 * @param in 数据流
	 * @return this
	 */
	public HttpServerResponse write(final InputStream in) {
		return write(in, 0);
	}

	/**
	 * 写出数据到客户端
	 *
	 * @param in     数据流
	 * @param length 指定响应内容长度，默认0表示不定长度，会输出Transfer-encoding: chunked
	 * @return this
	 */
	public HttpServerResponse write(final InputStream in, final int length) {
		if (! isSendCode) {
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

	/**
	 * 返回文件给客户端（文件下载）
	 *
	 * @param file 写出的文件对象
	 * @return this
	 * @since 5.2.6
	 */
	public HttpServerResponse write(final File file) {
		return write(file, null);
	}

	/**
	 * 返回文件给客户端（文件下载）
	 *
	 * @param file 写出的文件对象
	 * @param fileName 文件名
	 * @return this
	 * @since 5.5.8
	 */
	public HttpServerResponse write(final File file, String fileName) {
		final long fileSize = file.length();
		if(fileSize > Integer.MAX_VALUE){
			throw new IllegalArgumentException("File size is too bigger than " + Integer.MAX_VALUE);
		}

		if(StrUtil.isBlank(fileName)){
			fileName = file.getName();
		}
		final String contentType = FileUtil.getMimeType(fileName, ContentType.OCTET_STREAM.getValue());
		BufferedInputStream in = null;
		try {
			in = FileUtil.getInputStream(file);
			write(in, (int)fileSize, contentType, fileName);
		} finally {
			IoUtil.closeQuietly(in);
		}
		return this;
	}

	/**
	 * 返回文件数据给客户端（文件下载）
	 *
	 * @param in          需要返回客户端的内容
	 * @param contentType 返回的类型
	 * @param fileName    文件名
	 * @since 5.2.6
	 */
	public void write(final InputStream in, final String contentType, final String fileName) {
		write(in, 0, contentType, fileName);
	}

	/**
	 * 返回文件数据给客户端（文件下载）
	 *
	 * @param in          需要返回客户端的内容
	 * @param length 长度
	 * @param contentType 返回的类型
	 * @param fileName    文件名
	 * @return this
	 * @since 5.2.7
	 */
	public HttpServerResponse write(final InputStream in, final int length, final String contentType, final String fileName) {
		final Charset charset = ObjUtil.defaultIfNull(this.charset, DEFAULT_CHARSET);

		if (! contentType.startsWith("text/")) {
			// 非文本类型数据直接走下载
			setHeader(Header.CONTENT_DISPOSITION, StrUtil.format("attachment;filename={}", URLEncoder.encodeAll(fileName, charset)));
		}
		return write(in, length, contentType);
	}
}
