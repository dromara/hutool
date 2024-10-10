/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.http.client.engine.jdk;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.stream.EmptyInputStream;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.http.HttpException;
import org.dromara.hutool.http.HttpUtil;
import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.http.client.Response;
import org.dromara.hutool.http.client.body.ResponseBody;
import org.dromara.hutool.http.meta.HeaderName;

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
	 * 持有连接对象
	 */
	protected JdkHttpConnection httpConnection;
	/**
	 * 请求时的默认编码
	 */
	private final Charset requestCharset;
	/**
	 * Cookie管理器
	 */
	private final JdkCookieManager cookieManager;
	/**
	 * 响应头
	 */
	private Map<String, List<String>> headers;
	/**
	 * 响应内容体，{@code null} 表示无内容
	 */
	private ResponseBody body;
	/**
	 * 响应状态码
	 */
	protected int status;

	/**
	 * 构造
	 *
	 * @param httpConnection {@link JdkHttpConnection}
	 * @param cookieManager  Cookie管理器
	 * @param message        请求消息
	 */
	protected JdkHttpResponse(final JdkHttpConnection httpConnection,
							  final JdkCookieManager cookieManager,
							  final Request message) {
		this.httpConnection = httpConnection;
		this.cookieManager = cookieManager;
		this.requestCharset = message.charset();
		init(message.method().isIgnoreBody());
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
		return HttpUtil.header(this.headers, name);
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

	@Override
	public JdkHttpResponse sync() {
		if (null != this.body) {
			this.body.sync();
		}
		// 关闭连接
		this.httpConnection.closeQuietly();
		return this;
	}

	// ---------------------------------------------------------------- Http Response Header start

	/**
	 * 获取Cookie<br>
	 * 如果启用Cookie管理器，则返回这个站点相关的所有Cookie信息，否则返回当前的响应的Cookie信息
	 *
	 * @return Cookie列表
	 */
	public List<HttpCookie> getCookies() {
		if (this.cookieManager.isEnable()) {
			return this.cookieManager.getCookies(this.httpConnection);
		}
		return HttpCookie.parse(this.header(HeaderName.SET_COOKIE));
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
	 * @param isIgnoreBody 是否忽略消息体
	 * @throws HttpException IO异常
	 */
	private void init(final boolean isIgnoreBody) throws HttpException {
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
		if (null != this.cookieManager) {
			this.cookieManager.saveFromResponse(this.httpConnection, this.headers);
		}

		// 获取响应内容流
		if (!isIgnoreBody) {
			this.body = new ResponseBody(this, new JdkHttpInputStream(this));
		}
	}
	// ---------------------------------------------------------------- Private method end
}
