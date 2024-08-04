/*
 * Copyright (c) 2024 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.http.client;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.io.resource.Resource;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.map.multi.ListValueMap;
import org.dromara.hutool.core.net.url.UrlBuilder;
import org.dromara.hutool.core.net.url.UrlQuery;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.http.GlobalHeaders;
import org.dromara.hutool.http.HttpGlobalConfig;
import org.dromara.hutool.http.HttpUtil;
import org.dromara.hutool.http.client.body.*;
import org.dromara.hutool.http.client.engine.ClientEngine;
import org.dromara.hutool.http.client.engine.ClientEngineFactory;
import org.dromara.hutool.http.meta.HeaderName;
import org.dromara.hutool.http.meta.Method;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 请求消息体，包括请求的URI、请求头、请求体等
 *
 * @author looly
 * @since 6.0.0
 */
public class Request implements HeaderOperation<Request> {

	/**
	 * 构建一个HTTP请求，不解码和编码，此时要求用户传入的URL必须是已经编码过的。
	 *
	 * @param url URL链接
	 * @return HttpRequest
	 */
	public static Request ofWithoutEncode(final String url) {
		return of(url, null);
	}

	/**
	 * 构建一个HTTP请求，默认编解码规则，规则为：
	 * <ul>
	 *     <li>如果传入的URL已编码，则先解码，再按照RFC3986规范重新编码。</li>
	 *     <li>如果传入的URL未编码，则先解码（解码后不变），再按照RFC3986规范重新编码。</li>
	 *     <li>如果传入的URL部分编码，则先解码编码过的部分，再按照RFC3986规范重新编码。</li>
	 * </ul>
	 * 如果服务端要求的URL编码规则不符合RFC3986，则需要先自行编码，再调用{@link #of(String, Charset)}指定decodeAndEncodeCharset为{@code null}
	 *
	 * @param url URL链接，默认自动解码并重新编码URL中的参数等信息
	 * @return HttpRequest
	 */
	public static Request of(final String url) {
		return of(url, DEFAULT_CHARSET);
	}

	/**
	 * 构建一个HTTP请求<br>
	 * 对于传入的URL，可以自定义是否解码已经编码的内容，规则如下：<br>
	 * <ul>
	 *     <li>如果url已编码，则decodeAndEncodeCharset设置为{@code null}，此时URL不会解码，发送也不编码。</li>
	 *     <li>如果url未编码或部分编码，则需要设置decodeAndEncodeCharset，此时URL会解码编码后的参数，发送时按照RFC3986规范重新编码。</li>
	 *     <li>如果url未编码，且存在歧义字符串，则需要设置decodeAndEncodeCharset为{@code null}，并调用{@link Request#setEncodeUrl(boolean)}为true编码URL。</li>
	 * </ul>
	 *
	 * @param url                    URL链接
	 * @param decodeAndEncodeCharset 编码，如果为{@code null}不自动解码编码URL
	 * @return HttpRequest
	 */
	public static Request of(final String url, final Charset decodeAndEncodeCharset) {
		return of(UrlBuilder.ofHttp(url, decodeAndEncodeCharset));
	}

	/**
	 * 构建一个HTTP请求
	 *
	 * @param url {@link UrlBuilder}
	 * @return HttpRequest
	 */
	public static Request of(final UrlBuilder url) {
		return new Request().url(url);
	}

	/**
	 * 默认的请求编码、URL的encode、decode编码
	 */
	private static final Charset DEFAULT_CHARSET = CharsetUtil.UTF_8;

	/**
	 * 请求方法
	 */
	private Method method;
	/**
	 * 请求的URL
	 */
	private UrlBuilder url;
	/**
	 * 请求编码
	 */
	private Charset charset = DEFAULT_CHARSET;
	/**
	 * 存储头信息
	 */
	private final ListValueMap<String, String> headers;
	/**
	 * 请求体
	 */
	private HttpBody body;
	/**
	 * 最大重定向次数
	 */
	private int maxRedirectCount;
	/**
	 * 是否是REST请求模式，REST模式运行GET请求附带body
	 */
	private boolean isRest;

	/**
	 * 默认构造
	 */
	public Request() {
		method = Method.GET;
		headers = new ListValueMap<>(new LinkedHashMap<>());
		maxRedirectCount = HttpGlobalConfig.getMaxRedirectCount();

		// 全局默认请求头
		header(GlobalHeaders.INSTANCE.headers(), false);
	}

	/**
	 * 获取Http请求方法
	 *
	 * @return {@link Method}
	 * @since 4.1.8
	 */
	public Method method() {
		return this.method;
	}

	/**
	 * 设置请求方法
	 *
	 * @param method HTTP方法
	 * @return HttpRequest
	 */
	public Request method(final Method method) {
		this.method = method;
		return this;
	}

	/**
	 * 获取请求的URL
	 *
	 * @return URL
	 */
	public UrlBuilder url() {
		return url;
	}

	/**
	 * 获取处理后的请求URL，即如果为非REST的GET请求，将form类型的body拼接为URL的一部分
	 *
	 * @return URL
	 */
	public UrlBuilder handledUrl() {
		return urlWithParamIfGet();
	}

	/**
	 * 设置URL
	 *
	 * @param url URL
	 * @return this
	 */
	public Request url(final UrlBuilder url) {
		this.url = url;
		return this;
	}

	/**
	 * 设置自定义编码，一般用于：
	 * <ul>
	 *     <li>编码请求体</li>
	 *     <li>服务端未返回编码时，使用此编码解码响应体</li>
	 * </ul>
	 *
	 * @param charset 编码
	 * @return this
	 */
	public Request charset(final Charset charset) {
		this.charset = charset;
		return this;
	}

	/**
	 * 获取请求编码，默认{@link #DEFAULT_CHARSET}，一般用于：
	 * <ul>
	 *     <li>编码请求体</li>
	 *     <li>服务端未返回编码时，使用此编码解码响应体</li>
	 * </ul>
	 *
	 * @return 编码
	 */
	public Charset charset() {
		return this.charset;
	}

	/**
	 * 设置是否编码URL
	 *
	 * @param isEncodeUrl 如果为{@code true}，则使用请求编码编码URL，{@code false}则不编码URL
	 * @return this
	 */
	public Request setEncodeUrl(final boolean isEncodeUrl) {
		Assert.notNull(this.url, "Request URL must be not null!");
		this.url.setCharset(isEncodeUrl ? this.charset : null);
		return this;
	}

	// region header

	@Override
	public Map<String, ? extends Collection<String>> headers() {
		return MapUtil.view(this.headers);
	}

	/**
	 * 设置一个header<br>
	 * 如果覆盖模式，则替换之前的值，否则加入到值列表中<br>
	 * 如果给定值为{@code null}，则删除这个头信息
	 *
	 * @param name       Header名，{@code null}跳过
	 * @param value      Header值，{@code null}表示删除name对应的头
	 * @param isOverride 是否覆盖已有值
	 * @return this
	 */
	@Override
	public Request header(final String name, final String value, final boolean isOverride) {
		if (null == name) {
			return this;
		}
		if (null == value) {
			headers.remove(name);
			return this;
		}

		if (isOverride) {
			this.headers.put(name.trim(), ListUtil.of(value));
		} else {
			this.headers.putValue(name.trim(), value);
		}
		return this;
	}

	/**
	 * 简单验证，生成的头信息类似于：
	 * <pre>
	 * Authorization: Basic YWxhZGRpbjpvcGVuc2VzYW1l
	 * </pre>
	 *
	 * @param username 用户名
	 * @param password 密码
	 * @return this
	 */
	public Request basicAuth(final String username, final String password) {
		return basicAuth(username, password, charset);
	}

	// endregion

	// region body get
	/**
	 * 获取请求体
	 *
	 * @return 请求体
	 */
	public HttpBody body() {
		return this.body;
	}

	/**
	 * 获取请求体字符串
	 *
	 * @return 请求体字符串
	 */
	public String bodyStr() {
		return StrUtil.str(bodyBytes(), this.charset);
	}

	/**
	 * 获取请求体字节码
	 *
	 * @return 请求体字节码
	 */
	public byte[] bodyBytes() {
		return this.body == null ? null : this.body.getBytes();
	}

	/**
	 * 获取请求体资源流
	 *
	 * @return 请求体资源流
	 */
	public InputStream bodyStream() {
		return this.body == null ? null : this.body.getStream();
	}

	/**
	 * 获取处理过的请求体，即如果是非REST的GET请求，始终返回{@code null}
	 *
	 * @return 请求体
	 */
	public HttpBody handledBody() {
		if (Method.GET.equals(method) && !this.isRest) {
			return null;
		}
		return body();
	}
	// endregion

	// region body set
	/**
	 * 添加请求表单内容
	 *
	 * @param formMap 表单内容
	 * @return this
	 */
	public Request form(final Map<String, Object> formMap) {
		final AtomicBoolean isMultiPart = new AtomicBoolean(false);
		formMap.forEach((key, value) -> {
			if (hasMultipartValue(value)) {
				isMultiPart.set(true);
			}
		});

		if (isMultiPart.get()) {
			return body(MultipartBody.of(formMap, charset()));
		}
		return body(new UrlEncodedFormBody(formMap, charset()));
	}

	/**
	 * 添加字符串请求体
	 *
	 * @param body 请求体
	 * @return this
	 */
	public Request body(final String body) {
		return body(new StringBody(body, charset()));
	}

	/**
	 * 添加请求体
	 *
	 * @param body 请求体，可以是文本、表单、流、byte[] 或 Multipart
	 * @return this
	 */
	public Request body(final HttpBody body) {
		this.body = body;

		// 根据内容赋值默认Content-Type
		if (StrUtil.isBlank(header(HeaderName.CONTENT_TYPE))) {
			header(HeaderName.CONTENT_TYPE, body.getContentType(charset()), true);
		}

		return this;
	}
	// endregion

	/**
	 * 获取最大重定向请求次数
	 *
	 * @return 最大重定向请求次数
	 */
	public int maxRedirectCount() {
		return maxRedirectCount;
	}

	/**
	 * 设置最大重定向次数<br>
	 * 如果次数小于1则表示不重定向，大于等于1表示打开重定向
	 *
	 * @param maxRedirectCount 最大重定向次数
	 * @return this
	 */
	public Request setMaxRedirectCount(final int maxRedirectCount) {
		this.maxRedirectCount = Math.max(maxRedirectCount, 0);
		return this;
	}

	/**
	 * 设置是否rest模式<br>
	 * rest模式下get请求不会把参数附加到URL之后，而是作为body发送
	 *
	 * @param isRest 是否rest模式
	 * @return this
	 */
	public Request setRest(final boolean isRest) {
		this.isRest = isRest;
		return this;
	}

	/**
	 * 发送请求
	 *
	 * @return 响应内容
	 */
	public Response send() {
		return send(ClientEngineFactory.getEngine());
	}

	/**
	 * 发送请求
	 *
	 * @param engine 自自定义引擎
	 * @return 响应内容
	 */
	public Response send(final ClientEngine engine) {
		return engine.send(this);
	}

	/**
	 * 对于GET请求将参数加到URL中<br>
	 * 此处不对URL中的特殊字符做单独编码<br>
	 * 对于非rest的GET请求，且处于重定向时，参数丢弃
	 */
	private UrlBuilder urlWithParamIfGet() {
		if (Method.GET.equals(method) && !this.isRest) {
			final HttpBody body = this.body;
			if (body instanceof FormBody) {
				final UrlBuilder urlBuilder = UrlBuilder.of(this.url);
				UrlQuery query = urlBuilder.getQuery();
				if (null == query) {
					query = UrlQuery.of();
					urlBuilder.setQuery(query);
				}
				query.addAll(((FormBody<?>) body).form());
				return urlBuilder;
			}
		}

		return this.url();
	}

	@Override
	public String toString() {
		return HttpUtil.toString(this);
	}

	/**
	 * 检查form表单中的对象是否为Multipart对象
	 * @param value 对象
	 * @return 是否为Multipart对象
	 */
	private static boolean hasMultipartValue(final Object value){
		if(value instanceof Iterable){
			for (final Object subValue : (Iterable<?>) value) {
				if(hasMultipartValue(subValue)){
					return true;
				}
			}
		}

		return value instanceof File ||
			value instanceof Path ||
			value instanceof Resource ||
			value instanceof InputStream ||
			value instanceof Reader;
	}
}
