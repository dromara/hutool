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

package org.dromara.hutool.core.net.url;

import org.dromara.hutool.core.codec.PercentCodec;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.iter.IterUtil;
import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.map.TableMap;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

/**
 * URL中查询字符串部分的封装，类似于：
 * <pre>
 *   key1=v1&amp;key2=&amp;key3=v3
 * </pre>
 * 查询封装分为解析查询字符串和构建查询字符串，解析可通过charset为null来自定义是否decode编码后的内容，<br>
 * 构建则通过charset是否为null是否encode参数键值对
 *
 * @author looly
 * @since 5.3.1
 */
public class UrlQuery {

	private final TableMap<CharSequence, CharSequence> query;
	/**
	 * 编码模式
	 */
	private EncodeMode encodeMode;

	// region ----- of

	/**
	 * 构建UrlQuery
	 *
	 * @param queryStr 初始化的查询字符串
	 * @param charset  decode用的编码，null表示不做decode
	 * @return UrlQuery
	 */
	public static UrlQuery of(final String queryStr, final Charset charset) {
		return of(queryStr, charset, true);
	}

	/**
	 * 构建UrlQuery
	 *
	 * @param queryStr       初始化的查询字符串
	 * @param charset        decode用的编码，null表示不做decode
	 * @param autoRemovePath 是否自动去除path部分，{@code true}则自动去除第一个?前的内容
	 * @return UrlQuery
	 * @since 5.5.8
	 */
	public static UrlQuery of(final String queryStr, final Charset charset, final boolean autoRemovePath) {
		return of(queryStr, charset, autoRemovePath, null);
	}

	/**
	 * 构建UrlQuery
	 *
	 * @param queryStr       初始化的查询字符串
	 * @param charset        decode用的编码，null表示不做decode
	 * @param autoRemovePath 是否自动去除path部分，{@code true}则自动去除第一个?前的内容
	 * @param encodeMode     编码模式。
	 * @return UrlQuery
	 */
	public static UrlQuery of(final String queryStr, final Charset charset, final boolean autoRemovePath, final EncodeMode encodeMode) {
		return of(encodeMode).parse(queryStr, charset, autoRemovePath);
	}

	/**
	 * 构建UrlQuery
	 *
	 * @return UrlQuery
	 */
	public static UrlQuery of() {
		return of(EncodeMode.NORMAL);
	}

	/**
	 * 构建UrlQuery
	 *
	 * @param encodeMode 编码模式
	 * @return UrlQuery
	 */
	public static UrlQuery of(final EncodeMode encodeMode) {
		return new UrlQuery(null, encodeMode);
	}

	/**
	 * 构建UrlQuery
	 *
	 * @param queryMap 初始化的查询键值对
	 * @return UrlQuery
	 */
	public static UrlQuery of(final Map<? extends CharSequence, ?> queryMap) {
		return of(queryMap, null);
	}

	/**
	 * 构建UrlQuery
	 *
	 * @param queryMap   初始化的查询键值对
	 * @param encodeMode 编码模式
	 * @return UrlQuery
	 */
	public static UrlQuery of(final Map<? extends CharSequence, ?> queryMap, final EncodeMode encodeMode) {
		return new UrlQuery(queryMap, encodeMode);
	}
	// endregion

	/**
	 * 构造
	 *
	 * @param queryMap   初始化的查询键值对
	 * @param encodeMode 编码模式
	 */
	public UrlQuery(final Map<? extends CharSequence, ?> queryMap, final EncodeMode encodeMode) {
		if (MapUtil.isNotEmpty(queryMap)) {
			query = new TableMap<>(queryMap.size());
			addAll(queryMap);
		} else {
			query = new TableMap<>(MapUtil.DEFAULT_INITIAL_CAPACITY);
		}
		this.encodeMode = ObjUtil.defaultIfNull(encodeMode, EncodeMode.NORMAL);
	}

	/**
	 * 设置编码模式<br>
	 * 根据不同场景以及不同环境，对Query中的name和value采用不同的编码策略
	 *
	 * @param encodeMode 编码模式
	 * @return this
	 * @since 6.0.0
	 */
	public UrlQuery setEncodeMode(final EncodeMode encodeMode) {
		this.encodeMode = encodeMode;
		return this;
	}

	/**
	 * 增加键值对
	 *
	 * @param key   键
	 * @param value 值，集合和数组转换为逗号分隔形式
	 * @return this
	 */
	public UrlQuery add(final CharSequence key, final Object value) {
		this.query.put(key, toStr(value));
		return this;
	}

	/**
	 * 批量增加键值对
	 *
	 * @param queryMap query中的键值对
	 * @return this
	 */
	public UrlQuery addAll(final Map<? extends CharSequence, ?> queryMap) {
		if (MapUtil.isNotEmpty(queryMap)) {
			queryMap.forEach(this::add);
		}
		return this;
	}

	/**
	 * 解析URL中的查询字符串
	 *
	 * @param queryStr 查询字符串，类似于key1=v1&amp;key2=&amp;key3=v3
	 * @param charset  decode编码，null表示不做decode
	 * @return this
	 */
	public UrlQuery parse(final String queryStr, final Charset charset) {
		return parse(queryStr, charset, true);
	}

	/**
	 * 解析URL中的查询字符串
	 *
	 * @param queryStr       查询字符串，类似于key1=v1&amp;key2=&amp;key3=v3
	 * @param charset        decode编码，null表示不做decode
	 * @param autoRemovePath 是否自动去除path部分，{@code true}则自动去除第一个?前的内容
	 * @return this
	 * @since 5.5.8
	 */
	public UrlQuery parse(String queryStr, final Charset charset, final boolean autoRemovePath) {
		if (StrUtil.isBlank(queryStr)) {
			return this;
		}

		if (autoRemovePath) {
			// 去掉Path部分
			final int pathEndPos = queryStr.indexOf('?');
			if (pathEndPos > -1) {
				queryStr = StrUtil.subSuf(queryStr, pathEndPos + 1);
				if (StrUtil.isBlank(queryStr)) {
					return this;
				}
			}
		}

		return doParse(queryStr, charset);
	}

	/**
	 * 获得查询的Map
	 *
	 * @return 查询的Map，只读
	 */
	public Map<CharSequence, CharSequence> getQueryMap() {
		return MapUtil.view(this.query);
	}

	/**
	 * 获取查询值
	 *
	 * @param key 键
	 * @return 值
	 */
	public CharSequence get(final CharSequence key) {
		if (MapUtil.isEmpty(this.query)) {
			return null;
		}
		return this.query.get(key);
	}

	/**
	 * 构建URL查询字符串，即将key-value键值对转换为{@code key1=v1&key2=v2&key3=v3}形式。<br>
	 * 对于{@code null}处理规则如下：
	 * <ul>
	 *     <li>如果key为{@code null}，则这个键值对忽略</li>
	 *     <li>如果value为{@code null}，只保留key，如key1对应value为{@code null}生成类似于{@code key1&key2=v2}形式</li>
	 * </ul>
	 *
	 * @param charset encode编码，null表示不做encode编码
	 * @return URL查询字符串
	 */
	public String build(final Charset charset) {
		return build(charset, null != charset);
	}

	/**
	 * 构建URL查询字符串，即将key-value键值对转换为{@code key1=v1&key2=v2&key3=v3}形式。<br>
	 * 对于{@code null}处理规则如下：
	 * <ul>
	 *     <li>如果key为{@code null}，则这个键值对忽略</li>
	 *     <li>如果value为{@code null}，只保留key，如key1对应value为{@code null}生成类似于{@code key1&key2=v2}形式</li>
	 * </ul>
	 *
	 * @param charset       encode编码，null表示不做encode编码
	 * @param encodePercent 是否编码`%`
	 * @return URL查询字符串
	 */
	public String build(final Charset charset, final boolean encodePercent) {
		switch (this.encodeMode) {
			case FORM_URL_ENCODED:
				return build(FormUrlencoded.ALL, FormUrlencoded.ALL, charset, encodePercent);
			case STRICT:
				return build(RFC3986.QUERY_PARAM_NAME_STRICT, RFC3986.QUERY_PARAM_VALUE_STRICT, charset, encodePercent);
			default:
				return build(RFC3986.QUERY_PARAM_NAME, RFC3986.QUERY_PARAM_VALUE, charset, encodePercent);
		}
	}

	/**
	 * 构建URL查询字符串，即将key-value键值对转换为{@code key1=v1&key2=v2&key3=v3}形式。<br>
	 * 对于{@code null}处理规则如下：
	 * <ul>
	 *     <li>如果key为{@code null}，则这个键值对忽略</li>
	 *     <li>如果value为{@code null}，只保留key，如key1对应value为{@code null}生成类似于{@code key1&key2=v2}形式</li>
	 * </ul>
	 *
	 * @param keyCoder   键值对中键的编码器
	 * @param valueCoder 键值对中值的编码器
	 * @param charset    encode编码，null表示不做encode编码
	 * @return URL查询字符串
	 * @since 5.7.16
	 */
	public String build(final PercentCodec keyCoder, final PercentCodec valueCoder, final Charset charset) {
		return build(keyCoder, valueCoder, charset, true);
	}

	/**
	 * 构建URL查询字符串，即将key-value键值对转换为{@code key1=v1&key2=v2&key3=v3}形式。<br>
	 * 对于{@code null}处理规则如下：
	 * <ul>
	 *     <li>如果key为{@code null}，则这个键值对忽略</li>
	 *     <li>如果value为{@code null}，只保留key，如key1对应value为{@code null}生成类似于{@code key1&key2=v2}形式</li>
	 * </ul>
	 *
	 * @param keyCoder      键值对中键的编码器
	 * @param valueCoder    键值对中值的编码器
	 * @param charset       encode编码，null表示不做encode编码
	 * @param encodePercent 是否编码`%`
	 * @return URL查询字符串
	 * @since 5.8.0
	 */
	public String build(final PercentCodec keyCoder, final PercentCodec valueCoder,
						final Charset charset, final boolean encodePercent) {
		if (MapUtil.isEmpty(this.query)) {
			return StrUtil.EMPTY;
		}

		final char[] safeChars = encodePercent ? null : new char[]{'%'};
		final StringBuilder sb = new StringBuilder();
		CharSequence name;
		CharSequence value;
		for (final Map.Entry<CharSequence, CharSequence> entry : this.query) {
			name = entry.getKey();
			if (null != name) {
				if (sb.length() > 0) {
					sb.append("&");
				}
				sb.append(keyCoder.encode(name, charset, safeChars));
				value = entry.getValue();
				if (null != value) {
					sb.append("=").append(valueCoder.encode(value, charset, safeChars));
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 生成查询字符串，类似于aaa=111&amp;bbb=222<br>
	 * 此方法不对任何特殊字符编码，仅用于输出显示
	 *
	 * @return 查询字符串
	 */
	@Override
	public String toString() {
		return build(null);
	}

	/**
	 * 解析URL中的查询字符串<br>
	 * 规则见：https://url.spec.whatwg.org/#urlencoded-parsing
	 *
	 * @param queryStr 查询字符串，类似于key1=v1&amp;key2=&amp;key3=v3
	 * @param charset  decode编码，null表示不做decode
	 * @return this
	 * @since 5.5.8
	 */
	private UrlQuery doParse(final String queryStr, final Charset charset) {
		final int len = queryStr.length();
		String name = null;
		int pos = 0; // 未处理字符开始位置
		int i; // 未处理字符结束位置
		char c; // 当前字符
		for (i = 0; i < len; i++) {
			c = queryStr.charAt(i);
			switch (c) {
				case '='://键和值的分界符
					if (null == name) {
						// name可以是""
						name = queryStr.substring(pos, i);
						// 开始位置从分节符后开始
						pos = i + 1;
					}
					// 当=不作为分界符时，按照普通字符对待
					break;
				case '&'://键值对之间的分界符
					addParam(name, queryStr.substring(pos, i), charset);
					name = null;
					if (i + 4 < len && "amp;".equals(queryStr.substring(i + 1, i + 5))) {
						// issue#850@Github，"&amp;"转义为"&"
						i += 4;
					}
					// 开始位置从分节符后开始
					pos = i + 1;
					break;
			}
		}

		// 处理结尾
		addParam(name, queryStr.substring(pos, i), charset);

		return this;
	}

	/**
	 * 对象转换为字符串，用于URL的Query中
	 *
	 * @param value 值
	 * @return 字符串
	 */
	private static String toStr(final Object value) {
		final String result;
		if (value instanceof Iterable) {
			result = CollUtil.join((Iterable<?>) value, ",");
		} else if (value instanceof Iterator) {
			result = IterUtil.join((Iterator<?>) value, ",");
		} else {
			result = Convert.toStr(value);
		}
		return result;
	}

	/**
	 * 将键值对加入到值为List类型的Map中,，情况如下：
	 * <pre>
	 *     1、key和value都不为null，类似于 "a=1"或者"=1"，直接put
	 *     2、key不为null，value为null，类似于 "a="，值传""
	 *     3、key为null，value不为null，类似于 "1"
	 *     4、key和value都为null，忽略之，比如&&
	 * </pre>
	 *
	 * @param key     key，为null则value作为key
	 * @param value   value，为null且key不为null时传入""
	 * @param charset 编码
	 */
	private void addParam(final String key, final String value, final Charset charset) {
		final boolean isFormUrlEncoded = EncodeMode.FORM_URL_ENCODED == this.encodeMode;
		if (null != key) {
			final String actualKey = UrlDecoder.decode(key, charset, isFormUrlEncoded);
			this.query.put(actualKey, StrUtil.emptyIfNull(UrlDecoder.decode(value, charset, isFormUrlEncoded)));
		} else if (null != value) {
			// name为空，value作为name，value赋值null
			this.query.put(UrlDecoder.decode(value, charset, isFormUrlEncoded), null);
		}
	}

	/**
	 * 编码模式<br>
	 * 根据不同场景以及不同环境，对Query中的name和value采用不同的编码策略
	 *
	 * @author looly
	 * @since 6.0.0
	 */
	public enum EncodeMode {
		/**
		 * 正常模式（宽松模式），这种模式下，部分分隔符无需转义
		 */
		NORMAL,
		/**
		 * x-www-form-urlencoded模式，此模式下空格会编码为'+'，"~"和"*"会被转义
		 */
		FORM_URL_ENCODED,
		/**
		 * 严格模式，此模式下，非UNRESERVED的字符都会被转义
		 */
		STRICT
	}
}
