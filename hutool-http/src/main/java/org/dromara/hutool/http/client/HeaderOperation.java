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

package org.dromara.hutool.http.client;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.http.meta.HeaderName;

import java.net.HttpCookie;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP请求头的存储和读取相关方法
 *
 * @param <T> 返回对象类型，方便链式编程
 */
@SuppressWarnings("unchecked")
public interface HeaderOperation<T extends HeaderOperation<T>> {

	// region -----------------------------------------------------------  headers

	/**
	 * 获取headers
	 *
	 * @return Headers Map
	 */
	Map<String, ? extends Collection<String>> headers();

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
	T header(final String name, final String value, final boolean isOverride);

	/**
	 * 获取指定的Header值，如果不存在返回{@code null}
	 *
	 * @param headerName header名
	 * @return header值
	 */
	default String header(final HeaderName headerName) {
		return header(headerName.getValue());
	}

	/**
	 * 获取指定的Header值，如果不存在返回{@code null}
	 *
	 * @param name header名
	 * @return header值
	 */
	default String header(final String name) {
		final Collection<String> values = headers().get(name);
		if (ArrayUtil.isNotEmpty(values)) {
			return CollUtil.getFirst(values);
		}

		return null;
	}

	/**
	 * 设置一个header<br>
	 * 如果覆盖模式，则替换之前的值，否则加入到值列表中
	 *
	 * @param name       Header名
	 * @param value      Header值
	 * @param isOverride 是否覆盖已有值
	 * @return T 本身
	 */
	default T header(final HeaderName name, final String value, final boolean isOverride) {
		return header(name.toString(), value, isOverride);
	}

	/**
	 * 设置一个header<br>
	 * 覆盖模式，则替换之前的值
	 *
	 * @param name  Header名
	 * @param value Header值
	 * @return T 本身
	 */
	default T header(final HeaderName name, final String value) {
		return header(name.toString(), value, true);
	}

	/**
	 * 添加请求头，默认覆盖原有头参数
	 *
	 * @param name  请求头参数名称
	 * @param value 参数值
	 * @return this
	 */
	default T header(final String name, final String value) {
		return header(name, value, true);
	}

	/**
	 * 设置请求头<br>
	 * 不覆盖原有请求头
	 *
	 * @param headerMap  请求头
	 * @param isOverride 是否覆盖
	 * @return this
	 */
	default T header(final Map<String, ? extends Collection<String>> headerMap, final boolean isOverride) {
		if (MapUtil.isNotEmpty(headerMap)) {
			String name;
			for (final Map.Entry<String, ? extends Collection<String>> entry : headerMap.entrySet()) {
				name = entry.getKey();
				for (final String value : entry.getValue()) {
					this.header(name, StrUtil.emptyIfNull(value), isOverride);
				}
			}
		}
		return (T) this;
	}

	/**
	 * 设置请求头<br>
	 * 覆盖原有请求头，请求参数为普通Map,简化使用
	 *
	 * @param headerMap  请求头
	 * @return this
	 * @author dazer
	 */
	default T header(final Map<String, String> headerMap) {
		if (MapUtil.isEmpty(headerMap)) {
			return (T) this;
		}
		final Map<String, List<String>> headerMaps = new LinkedHashMap<>(headers().size());
		headerMap.forEach((key, value) -> {
			headerMaps.put(key, ListUtil.of(value));
		});
		return header(headerMaps, true);
	}

	/**
	 * 设置contentType
	 *
	 * @param contentType contentType
	 * @return T
	 */
	default T contentType(final String contentType) {
		header(HeaderName.CONTENT_TYPE, contentType);
		return (T) this;
	}

	/**
	 * 设置是否为长连接
	 *
	 * @param isKeepAlive 是否长连接
	 * @return T
	 */
	default T keepAlive(final boolean isKeepAlive) {
		header(HeaderName.CONNECTION, isKeepAlive ? "Keep-Alive" : "Close");
		return (T) this;
	}

	// endregion -----------------------------------------------------------  headers

	// region -----------------------------------------------------------  auth

	/**
	 * 令牌验证，生成的头类似于："Authorization: Bearer XXXXX"，一般用于JWT
	 *
	 * @param token 令牌内容
	 * @return T this
	 */
	default T bearerAuth(final String token) {
		return auth("Bearer " + token);
	}

	/**
	 * 验证，简单插入Authorization头
	 *
	 * @param content 验证内容
	 * @return T this
	 */
	default T auth(final String content) {
		header(HeaderName.AUTHORIZATION, content, true);
		return (T) this;
	}

	/**
	 * 验证，简单插入Authorization头
	 *
	 * @param content 验证内容
	 * @return T this
	 */
	default T proxyAuth(final String content) {
		header(HeaderName.PROXY_AUTHORIZATION, content, true);
		return (T) this;
	}

	// endregion -----------------------------------------------------------  auth
	// region -----------------------------------------------------------  Cookies

	/**
	 * 设置Cookie<br>
	 * 自定义Cookie后会覆盖Hutool的默认Cookie行为
	 *
	 * @param cookies Cookie值数组，如果为{@code null}则设置无效，使用默认Cookie行为
	 * @return this
	 * @since 5.4.1
	 */
	default T cookie(final Collection<HttpCookie> cookies) {
		return cookie(CollUtil.isEmpty(cookies) ? null : cookies.toArray(new HttpCookie[0]));
	}

	/**
	 * 设置Cookie<br>
	 * 自定义Cookie后会覆盖Hutool的默认Cookie行为
	 *
	 * @param cookies Cookie值数组，如果为{@code null}则设置无效，使用默认Cookie行为
	 * @return this
	 * @since 3.1.1
	 */
	default T cookie(final HttpCookie... cookies) {
		if (ArrayUtil.isEmpty(cookies)) {
			return disableCookie();
		}
		// 名称/值对之间用分号和空格 ('; ')
		// https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Cookie
		return cookie(ArrayUtil.join(cookies, "; "));
	}

	/**
	 * 设置Cookie<br>
	 * 自定义Cookie后会覆盖Hutool的默认Cookie行为
	 *
	 * @param cookie Cookie值，如果为{@code null}则设置无效，使用默认Cookie行为
	 * @return this
	 * @since 3.0.7
	 */
	default T cookie(final String cookie) {
		return header(HeaderName.COOKIE, cookie, true);
	}

	/**
	 * 禁用默认Cookie行为，此方法调用后会将Cookie置为空。<br>
	 * 如果想重新启用Cookie，请调用：{@link #cookie(String)}方法自定义Cookie。<br>
	 * 如果想启动默认的Cookie行为（自动回填服务器传回的Cookie），则调用{@link #enableDefaultCookie()}
	 *
	 * @return this
	 * @since 3.0.7
	 */
	default T disableCookie() {
		return cookie(StrUtil.EMPTY);
	}

	/**
	 * 打开默认的Cookie行为（自动回填服务器传回的Cookie）
	 *
	 * @return this
	 */
	default T enableDefaultCookie() {
		return cookie((String) null);
	}

	// endregion -----------------------------------------------------------  Cookies
}
