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

package org.dromara.hutool.json;

import org.dromara.hutool.core.bean.copier.CopyOptions;
import org.dromara.hutool.core.codec.binary.HexUtil;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.map.CaseInsensitiveLinkedMap;
import org.dromara.hutool.core.map.CaseInsensitiveTreeMap;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.serializer.JSONMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 内部JSON工具类，仅用于JSON内部使用
 *
 * @author Looly
 */
public final class InternalJSONUtil {

	/**
	 * 尝试转换字符串为number, boolean, or null，无法转换返回String<br>
	 * 此方法用于解析JSON字符串时，将字符串中的值转换为JSON值对象
	 *
	 * @param string A String.
	 * @return A simple JSON value.
	 */
	public static Object parseValueFromString(final String string) {
		// null处理
		if (StrUtil.isEmpty(string) || StrUtil.NULL.equalsIgnoreCase(string)) {
			return null;
		}

		// boolean处理
		if ("true".equalsIgnoreCase(string)) {
			return Boolean.TRUE;
		}
		if ("false".equalsIgnoreCase(string)) {
			return Boolean.FALSE;
		}

		return parseNumberOrString(string);
	}

	/**
	 * 尝试转换字符串为number or null，无法转换返回String<br>
	 * 此方法用于解析JSON字符串时，将字符串中的值转换为JSON值对象
	 *
	 * @param string A String.
	 * @return A simple JSON value.
	 */
	public static Object parseNumberOrString(final String string) {
		// null处理
		if (StrUtil.isEmpty(string)) {
			return null;
		}

		// Number处理
		final char b = string.charAt(0);
		if ((b >= '0' && b <= '9') || b == '-') {
			try {
				if (StrUtil.containsAnyIgnoreCase(string, ".", "e")) {
					// pr#192@Gitee，Double会出现小数精度丢失问题，此处使用BigDecimal
					return new BigDecimal(string);
				} else {
					final long myLong = Long.parseLong(string);
					if (string.equals(Long.toString(myLong))) {
						if (myLong == (int) myLong) {
							return (int) myLong;
						} else {
							return myLong;
						}
					}
				}
			} catch (final Exception ignore) {
			}
		}

		// 无法解析的超大数字，直接返回字符串
		// "xxx"这种直接字符串形式的值，直接返回
		return string;
	}

	/**
	 * 将{@link JSONConfig}参数转换为Bean拷贝所用的{@link CopyOptions}
	 *
	 * @param config {@link JSONConfig}
	 * @return {@link CopyOptions}
	 * @since 5.8.0
	 */
	public static CopyOptions toCopyOptions(final JSONConfig config) {
		final JSONMapper mapper = JSONFactory.of(config, null).getMapper();
		return CopyOptions.of()
			.setIgnoreCase(config.isIgnoreCase())
			.setIgnoreError(config.isIgnoreError())
			.setIgnoreNullValue(config.isIgnoreNullValue())
			.setTransientSupport(config.isTransientSupport())
			.setConverter((targetType, value) -> mapper.toJSON(value, false));
	}

	/**
	 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
	 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
	 * JSON字符串中不能包含控制字符和未经转义的引号和反斜杠
	 *
	 * @param string 字符串
	 * @return 适合在JSON中显示的字符串
	 */
	public static String quote(final CharSequence string) {
		return quote(string, true);
	}

	/**
	 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
	 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
	 * JSON字符串中不能包含控制字符和未经转义的引号和反斜杠
	 *
	 * @param string 字符串
	 * @param isWrap 是否使用双引号包装字符串
	 * @return 适合在JSON中显示的字符串
	 * @since 3.3.1
	 */
	public static String quote(final CharSequence string, final boolean isWrap) {
		return quote(string, new StringBuilder(), isWrap).toString();
	}

	/**
	 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
	 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
	 * JSON字符串中不能包含控制字符和未经转义的引号和反斜杠
	 *
	 * @param str        字符串
	 * @param appendable {@link Appendable}
	 * @throws IORuntimeException IO异常
	 */
	public static void quote(final CharSequence str, final Appendable appendable) throws IORuntimeException {
		quote(str, appendable, true);
	}

	/**
	 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
	 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
	 * JSON字符串中不能包含控制字符和未经转义的引号和反斜杠
	 *
	 * @param str        字符串
	 * @param appendable {@link Appendable}
	 * @param isWrap     是否使用双引号包装字符串
	 * @return Writer
	 * @throws IORuntimeException IO异常
	 * @since 3.3.1
	 */
	public static Appendable quote(final CharSequence str, final Appendable appendable, final boolean isWrap) throws IORuntimeException {
		try {
			return _quote(str, appendable, isWrap);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 转义显示不可见字符
	 *
	 * @param str 字符串
	 * @return 转义后的字符串
	 */
	public static String escape(final String str) {
		if (StrUtil.isEmpty(str)) {
			return str;
		}

		final int len = str.length();
		final StringBuilder builder = new StringBuilder(len);
		char c;
		for (int i = 0; i < len; i++) {
			c = str.charAt(i);
			builder.append(escape(c));
		}
		return builder.toString();
	}

	/**
	 * 根据配置创建对应的原始Map
	 *
	 * @param capacity 初始大小
	 * @param factory  JSON工厂，{@code null}则使用默认配置
	 * @return Map
	 */
	static Map<String, JSON> createRawMap(final int capacity, final JSONFactory factory) {
		final JSONConfig config = ObjUtil.apply(factory, JSONFactory::getConfig);
		final boolean ignoreCase = ObjUtil.defaultIfNull(config, JSONConfig::isIgnoreCase, false);
		final Comparator<String> keyComparator = ObjUtil.apply(config, JSONConfig::getKeyComparator);

		final Map<String, JSON> rawHashMap;
		if (ignoreCase) {
			if (null != keyComparator) {
				rawHashMap = new CaseInsensitiveTreeMap<>(keyComparator);
			} else {
				rawHashMap = new CaseInsensitiveLinkedMap<>(capacity);
			}
		} else {
			if (null != keyComparator) {
				rawHashMap = new TreeMap<>(keyComparator);
			} else {
				rawHashMap = new LinkedHashMap<>(capacity);
			}
		}
		return rawHashMap;
	}

	// --------------------------------------------------------------------------------------------- Private method start

	/**
	 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
	 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
	 * JSON字符串中不能包含控制字符和未经转义的引号和反斜杠
	 *
	 * @param str    字符串
	 * @param writer Writer
	 * @param isWrap 是否使用双引号包装字符串
	 * @return Writer
	 * @throws IOException IO异常
	 * @since 3.3.1
	 */
	private static Appendable _quote(final CharSequence str, final Appendable writer, final boolean isWrap) throws IOException {
		if (StrUtil.isEmpty(str)) {
			if (isWrap) {
				writer.append("\"\"");
			}
			return writer;
		}

		char c; // 当前字符
		final int len = str.length();
		if (isWrap) {
			writer.append('"');
		}
		for (int i = 0; i < len; i++) {
			c = str.charAt(i);
			switch (c) {
				case '\\':
				case '"':
					writer.append("\\");
					writer.append(c);
					break;
				default:
					writer.append(escape(c));
			}
		}
		if (isWrap) {
			writer.append('"');
		}
		return writer;
	}

	/**
	 * 转义不可见字符<br>
	 * 见：<a href="https://en.wikibooks.org/wiki/Unicode/Character_reference/0000-0FFF">https://en.wikibooks.org/wiki/Unicode/Character_reference/0000-0FFF</a>
	 *
	 * @param c 字符
	 * @return 转义后的字符串
	 */
	@SuppressWarnings("UnnecessaryUnicodeEscape")
	private static String escape(final char c) {
		switch (c) {
			case '\b':
				return "\\b";
			case '\t':
				return "\\t";
			case '\n':
				return "\\n";
			case '\f':
				return "\\f";
			case '\r':
				return "\\r";
			default:
				if (c < CharUtil.SPACE || //
					(c >= '\u0080' && c <= '\u00a0') || //
					(c >= '\u2000' && c <= '\u2010') || //
					(c >= '\u2028' && c <= '\u202F') || //
					(c >= '\u2066' && c <= '\u206F')//
				) {
					return HexUtil.toUnicodeHex(c);
				} else {
					return Character.toString(c);
				}
		}
	}
	// --------------------------------------------------------------------------------------------- Private method end
}
