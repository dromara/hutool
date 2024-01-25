/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.http.client.engine.jdk;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.stream.EmptyInputStream;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.http.HttpException;
import org.dromara.hutool.http.HttpUtil;
import org.dromara.hutool.http.client.Response;
import org.dromara.hutool.http.client.body.ResponseBody;
import org.dromara.hutool.http.client.cookie.GlobalCookieManager;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpCookie;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Http响应类<br>
 * 非线程安全对象
 *
 * @author Looly
 */
public class JdkHttpResponse implements Response, Closeable {

	/**
	 * 请求时的默认编码
	 */
	private final Charset requestCharset;
	/**
	 * 响应内容体，{@code null} 表示无内容
	 */
	private ResponseBody body;
	private Map<String, List<String>> headers;

	/**
	 * 是否忽略响应读取时可能的EOF异常。<br>
	 * 在Http协议中，对于Transfer-Encoding: Chunked在正常情况下末尾会写入一个Length为0的的chunk标识完整结束。<br>
	 * 如果服务端未遵循这个规范或响应没有正常结束，会报EOF异常，此选项用于是否忽略这个异常。
	 */
	private final boolean ignoreEOFError;

	/**
	 * 持有连接对象
	 */
	protected JdkHttpConnection httpConnection;
	/**
	 * 响应状态码
	 */
	protected int status;

	/**
	 * 构造
	 *
	 * @param httpConnection {@link JdkHttpConnection}
	 * @param ignoreEOFError 是否忽略响应读取时可能的EOF异常
	 * @param requestCharset 编码，从请求编码中获取默认编码
	 * @param isAsync        是否异步
	 * @param isIgnoreBody   是否忽略读取响应体
	 */
	protected JdkHttpResponse(final JdkHttpConnection httpConnection, final boolean ignoreEOFError,
							  final Charset requestCharset, final boolean isAsync, final boolean isIgnoreBody) {
		this.httpConnection = httpConnection;
		this.ignoreEOFError = ignoreEOFError;
		this.requestCharset = requestCharset;
		init(isAsync, isIgnoreBody);
	}

	/**
	 * 获取状态码
	 *
	 * @return 状态码
	 */
	@Override
	public int getStatus() {
		return this.status;
	}

	@Override
	public String header(final String name) {
		final List<String> headerValues = this.headers.get(name);
		if (ArrayUtil.isNotEmpty(headerValues)) {
			return headerValues.get(0);
		}
		return null;
	}

	/**
	 * 获取headers
	 *
	 * @return Headers Map
	 */
	@Override
	public Map<String, List<String>> headers() {
		return this.headers;
	}

	@Override
	public Charset charset() {
		return ObjUtil.defaultIfNull(Response.super.charset(), requestCharset);
	}

	/**
	 * 同步<br>
	 * 如果为异步状态，则暂时不读取服务器中响应的内容，而是持有Http链接的{@link InputStream}。<br>
	 * 当调用此方法时，异步状态转为同步状态，此时从Http链接流中读取body内容并暂存在内容中。如果已经是同步状态，则不进行任何操作。
	 *
	 * @return this
	 */
	public JdkHttpResponse sync() {
		if (null != this.body) {
			this.body.sync();
		}
		close();
		return this;
	}

	// ---------------------------------------------------------------- Http Response Header start

	/**
	 * 获取Cookie
	 *
	 * @return Cookie列表
	 * @since 3.1.1
	 */
	public List<HttpCookie> getCookies() {
		return GlobalCookieManager.getCookies(this.httpConnection);
	}

	/**
	 * 获取Cookie
	 *
	 * @param name Cookie名
	 * @return {@link HttpCookie}
	 * @since 4.1.4
	 */
	public HttpCookie getCookie(final String name) {
		final List<HttpCookie> cookie = getCookies();
		if (null != cookie) {
			for (final HttpCookie httpCookie : cookie) {
				if (httpCookie.getName().equals(name)) {
					return httpCookie;
				}
			}
		}
		return null;
	}

	/**
	 * 获取Cookie值
	 *
	 * @param name Cookie名
	 * @return Cookie值
	 * @since 4.1.4
	 */
	public String getCookieValue(final String name) {
		final HttpCookie cookie = getCookie(name);
		return (null == cookie) ? null : cookie.getValue();
	}
	// ---------------------------------------------------------------- Http Response Header end

	// ---------------------------------------------------------------- Body start

	/**
	 * 获得服务区响应流<br>
	 * 异步模式下获取Http原生流，同步模式下获取获取到的在内存中的副本<br>
	 * 如果想在同步模式下获取流，请先调用{@link #sync()}方法强制同步<br>
	 * 流获取后处理完毕需关闭此类
	 *
	 * @return 响应流
	 */
	@Override
	public InputStream bodyStream() {
		// 使用ResponseBody中的stream有利于控制响应数据的同步与否
		return null == this.body ? EmptyInputStream.INSTANCE : this.body.getStream();
	}

	@Override
	public ResponseBody body() {
		return this.body;
	}

	/**
	 * 获取响应流字节码<br>
	 * 此方法会转为同步模式
	 *
	 * @return byte[]
	 */
	@SuppressWarnings("resource")
	@Override
	public byte[] bodyBytes() {
		sync();
		return body().getBytes();
	}
	// ---------------------------------------------------------------- Body end

	@Override
	public void close() {
		// 关闭流
		IoUtil.closeQuietly(this.body);
		// 关闭连接
		this.httpConnection.closeQuietly();
	}

	@Override
	public String toString() {
		return HttpUtil.toString(this);
	}

	// ---------------------------------------------------------------- Private method start

	/**
	 * 初始化Http响应<br>
	 * 初始化包括：
	 *
	 * <pre>
	 * 1、读取Http状态
	 * 2、读取头信息
	 * 3、持有Http流，并不关闭流
	 * </pre>
	 *
	 * @throws HttpException IO异常
	 */
	private void init(final boolean isAsync, final boolean isIgnoreBody) throws HttpException {
		// 获取响应状态码
		try {
			this.status = httpConnection.getCode();
		} catch (final IOException e) {
			if (!(e instanceof FileNotFoundException)) {
				throw new HttpException(e);
			}
			// 服务器无返回内容，忽略之
		}

		// 读取响应头信息
		try {
			this.headers = httpConnection.headers();
		} catch (final IllegalArgumentException e) {
			// ignore
			// StaticLog.warn(e, e.getMessage());
		}

		// 存储服务端设置的Cookie信息
		GlobalCookieManager.store(httpConnection, this.headers);

		// 获取响应内容流
		if (!isIgnoreBody) {
			this.body = new ResponseBody(this, new JdkHttpInputStream(this), isAsync, this.ignoreEOFError);
		}
	}
	// ---------------------------------------------------------------- Private method end
}
