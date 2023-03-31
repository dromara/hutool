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

package cn.hutool.core.net.url;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UrlQueryUtil {
	/**
	 * 将Map形式的Form表单数据转换为Url参数形式，会自动url编码键和值
	 *
	 * @param paramMap 表单数据
	 * @return url参数
	 */
	public static String toQuery(final Map<String, ?> paramMap) {
		return toQuery(paramMap, CharsetUtil.UTF_8);
	}

	/**
	 * 将Map形式的Form表单数据转换为Url参数形式<br>
	 * paramMap中如果key为空（null和""）会被忽略，如果value为null，会被做为空白符（""）<br>
	 * 会自动url编码键和值<br>
	 * 此方法用于拼接URL中的Query部分，并不适用于POST请求中的表单
	 *
	 * <pre>
	 * key1=v1&amp;key2=&amp;key3=v3
	 * </pre>
	 *
	 * @param paramMap 表单数据
	 * @param charset  编码，{@code null} 表示不encode键值对
	 * @return url参数
	 * @see #toQuery(Map, Charset, boolean)
	 */
	public static String toQuery(final Map<String, ?> paramMap, final Charset charset) {
		return toQuery(paramMap, charset, false);
	}

	/**
	 * 将Map形式的Form表单数据转换为Url参数形式<br>
	 * paramMap中如果key为空（null和""）会被忽略，如果value为null，会被做为空白符（""）<br>
	 * 会自动url编码键和值
	 *
	 * <pre>
	 * key1=v1&amp;key2=&amp;key3=v3
	 * </pre>
	 *
	 * @param paramMap         表单数据
	 * @param charset          编码，null表示不encode键值对
	 * @param isFormUrlEncoded 是否为x-www-form-urlencoded模式，此模式下空格会编码为'+'
	 * @return url参数
	 * @since 5.7.16
	 */
	public static String toQuery(final Map<String, ?> paramMap, final Charset charset, final boolean isFormUrlEncoded) {
		return UrlQuery.of(paramMap, isFormUrlEncoded).build(charset);
	}

	/**
	 * 对URL参数做编码，只编码键和值<br>
	 * 提供的值可以是url附带参数，但是不能只是url
	 *
	 * <p>注意，此方法只能标准化整个URL，并不适合于单独编码参数值</p>
	 *
	 * @param urlWithParams url和参数，可以包含url本身，也可以单独参数
	 * @param charset       编码
	 * @return 编码后的url和参数
	 * @since 4.0.1
	 */
	public static String encodeQuery(final String urlWithParams, final Charset charset) {
		if (StrUtil.isBlank(urlWithParams)) {
			return StrUtil.EMPTY;
		}

		String urlPart = null; // url部分，不包括问号
		String paramPart; // 参数部分
		final int pathEndPos = urlWithParams.indexOf('?');
		if (pathEndPos > -1) {
			// url + 参数
			urlPart = StrUtil.subPre(urlWithParams, pathEndPos);
			paramPart = StrUtil.subSuf(urlWithParams, pathEndPos + 1);
			if (StrUtil.isBlank(paramPart)) {
				// 无参数，返回url
				return urlPart;
			}
		} else if (false == StrUtil.contains(urlWithParams, '=')) {
			// 无参数的URL
			return urlWithParams;
		} else {
			// 无URL的参数
			paramPart = urlWithParams;
		}

		paramPart = normalizeQuery(paramPart, charset);

		return StrUtil.isBlank(urlPart) ? paramPart : urlPart + "?" + paramPart;
	}

	/**
	 * 标准化参数字符串，即URL中？后的部分
	 *
	 * <p>注意，此方法只能标准化整个URL，并不适合于单独编码参数值</p>
	 *
	 * @param queryPart 参数字符串
	 * @param charset   编码
	 * @return 标准化的参数字符串
	 * @since 4.5.2
	 */
	public static String normalizeQuery(final String queryPart, final Charset charset) {
		if (StrUtil.isEmpty(queryPart)) {
			return queryPart;
		}
		final StringBuilder builder = new StringBuilder(queryPart.length() + 16);
		final int len = queryPart.length();
		String name = null;
		int pos = 0; // 未处理字符开始位置
		char c; // 当前字符
		int i; // 当前字符位置
		for (i = 0; i < len; i++) {
			c = queryPart.charAt(i);
			if (c == '=') { // 键值对的分界点
				if (null == name) {
					// 只有=前未定义name时被当作键值分界符，否则做为普通字符
					name = (pos == i) ? StrUtil.EMPTY : queryPart.substring(pos, i);
					pos = i + 1;
				}
			} else if (c == '&') { // 参数对的分界点
				if (pos != i) {
					if (null == name) {
						// 对于像&a&这类无参数值的字符串，我们将name为a的值设为""
						name = queryPart.substring(pos, i);
						builder.append(RFC3986.QUERY_PARAM_NAME.encode(name, charset)).append('=');
					} else {
						builder.append(RFC3986.QUERY_PARAM_NAME.encode(name, charset)).append('=')
								.append(RFC3986.QUERY_PARAM_VALUE.encode(queryPart.substring(pos, i), charset)).append('&');
					}
					name = null;
				}
				pos = i + 1;
			}
		}

		// 结尾处理
		if (null != name) {
			builder.append(URLEncoder.encodeQuery(name, charset)).append('=');
		}
		if (pos != i) {
			if (null == name && pos > 0) {
				builder.append('=');
			}
			builder.append(URLEncoder.encodeQuery(queryPart.substring(pos, i), charset));
		}

		// 以&结尾则去除之
		final int lastIndex = builder.length() - 1;
		if ('&' == builder.charAt(lastIndex)) {
			builder.delete(lastIndex, builder.length());
		}
		return builder.toString();
	}

	/**
	 * 将URL参数解析为Map（也可以解析Post中的键值对参数）
	 *
	 * @param paramsStr 参数字符串（或者带参数的Path）
	 * @param charset   字符集
	 * @return 参数Map
	 * @since 5.2.6
	 */
	public static Map<String, String> decodeQuery(final String paramsStr, final Charset charset) {
		final Map<CharSequence, CharSequence> queryMap = UrlQuery.of(paramsStr, charset).getQueryMap();
		if (MapUtil.isEmpty(queryMap)) {
			return MapUtil.empty();
		}
		return Convert.toMap(String.class, String.class, queryMap);
	}

	/**
	 * 将URL参数解析为Map（也可以解析Post中的键值对参数）
	 *
	 * @param paramsStr 参数字符串（或者带参数的Path）
	 * @param charset   字符集
	 * @return 参数Map
	 * @since 5.2.6
	 */
	public static Map<String, List<String>> decodeQueryList(final String paramsStr, final Charset charset) {
		final Map<CharSequence, CharSequence> queryMap = UrlQuery.of(paramsStr, charset).getQueryMap();
		if (MapUtil.isEmpty(queryMap)) {
			return MapUtil.empty();
		}

		final Map<String, List<String>> params = new LinkedHashMap<>();
		queryMap.forEach((key, value) -> {
			final List<String> values = params.computeIfAbsent(StrUtil.str(key), k -> new ArrayList<>(1));
			// 一般是一个参数
			values.add(StrUtil.str(value));
		});
		return params;
	}
}
