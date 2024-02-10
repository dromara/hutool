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

package org.dromara.hutool.json;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.bean.copier.CopyOptions;
import org.dromara.hutool.core.codec.binary.HexUtil;
import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.core.map.CaseInsensitiveLinkedMap;
import org.dromara.hutool.core.map.CaseInsensitiveTreeMap;
import org.dromara.hutool.core.math.NumberUtil;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.reflect.TypeUtil;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.split.SplitUtil;
import org.dromara.hutool.json.mapper.JSONValueMapper;
import org.dromara.hutool.json.serialize.GlobalSerializeMapping;
import org.dromara.hutool.json.serialize.JSONDeserializer;
import org.dromara.hutool.json.serialize.JSONStringer;
import org.dromara.hutool.json.writer.GlobalValueWriterMapping;
import org.dromara.hutool.json.writer.JSONValueWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Predicate;

/**
 * 内部JSON工具类，仅用于JSON内部使用
 *
 * @author Looly
 */
public final class InternalJSONUtil {

	private InternalJSONUtil() {
	}

	/**
	 * 在需要的时候包装对象<br>
	 * 包装包括：
	 * <ul>
	 * <li>array or collection =》 JSONArray</li>
	 * <li>map =》 JSONObject</li>
	 * <li>standard property (Double, String, et al) =》 原对象</li>
	 * <li>来自于java包 =》 字符串</li>
	 * <li>其它 =》 尝试包装为JSONObject，否则返回{@code null}</li>
	 * </ul>
	 *
	 * @param object     被包装的对象
	 * @param jsonConfig JSON选项
	 * @return 包装后的值，null表示此值需被忽略
	 */
	static Object wrap(final Object object, final JSONConfig jsonConfig) {
		return JSONValueMapper.of(jsonConfig).map(object);
	}

	/**
	 * 值转为String，用于JSON中。规则为：
	 * <ul>
	 *     <li>对象如果实现了{@link JSONStringer}接口，调用{@link JSONStringer#toJSONString()}方法</li>
	 *     <li>对象如果实现了{@link JSONStringer}接口，调用{@link JSONStringer#toJSONString()}方法</li>
	 *     <li>对象如果是数组或Collection，包装为{@link JSONArray}</li>
	 *     <li>对象如果是Map，包装为{@link JSONObject}</li>
	 *     <li>对象如果是数字，使用{@link NumberUtil#toStr(Number)}转换为字符串</li>
	 *     <li>其他情况调用toString并使用双引号包装</li>
	 * </ul>
	 *
	 * @param value 需要转为字符串的对象
	 * @return 字符串
	 * @throws JSONException If the value is or contains an invalid number.
	 */
	static String valueToString(final Object value) throws JSONException {
		if (value == null) {
			return StrUtil.NULL;
		}
		if (value instanceof JSONStringer) {
			try {
				return ((JSONStringer) value).toJSONString();
			} catch (final Exception e) {
				throw new JSONException(e);
			}
		} else if (value instanceof Number) {
			return NumberUtil.toStr((Number) value);
		} else if (value instanceof Boolean || value instanceof JSONObject || value instanceof JSONArray) {
			return value.toString();
		} else if (value instanceof Map) {
			final Map<?, ?> map = (Map<?, ?>) value;
			return new JSONObject(map).toString();
		} else if (value instanceof Collection) {
			final Collection<?> coll = (Collection<?>) value;
			return new JSONArray(coll).toString();
		} else if (ArrayUtil.isArray(value)) {
			return new JSONArray(value).toString();
		} else {
			return quote(value.toString());
		}
	}

	/**
	 * 将Property的键转化为JSON形式<br>
	 * 用于识别类似于：org.dromara.hutool.json这类用点隔开的键<br>
	 * 注意：不允许重复键
	 *
	 * @param jsonObject JSONObject
	 * @param key        键
	 * @param value      值
	 * @param predicate  属性过滤器，{@link Predicate#test(Object)}为{@code true}保留
	 */
	public static void propertyPut(final JSONObject jsonObject, final Object key, final Object value, final Predicate<MutableEntry<String, Object>> predicate) {
		final String[] path = SplitUtil.splitToArray(Convert.toStr(key), StrUtil.DOT);
		final int last = path.length - 1;
		JSONObject target = jsonObject;
		for (int i = 0; i < last; i += 1) {
			final String segment = path[i];
			JSONObject nextTarget = target.getJSONObject(segment);
			if (nextTarget == null) {
				nextTarget = new JSONObject(target.config());
				target.set(segment, nextTarget, predicate);
			}
			target = nextTarget;
		}
		target.set(path[last], value, predicate);
	}

	/**
	 * 默认情况下是否忽略null值的策略选择，以下对象不忽略null值，其它对象忽略：
	 *
	 * <pre>
	 *     1. CharSequence
	 *     2. JSONTokener
	 *     3. Map
	 * </pre>
	 *
	 * @param obj 需要检查的对象
	 * @return 是否忽略null值
	 * @since 4.3.1
	 */
	static boolean defaultIgnoreNullValue(final Object obj) {
		return (!(obj instanceof CharSequence))//
				&& (!(obj instanceof JSONTokener))//
				&& (!(obj instanceof Map));
	}

	/**
	 * 将{@link JSONConfig}参数转换为Bean拷贝所用的{@link CopyOptions}
	 *
	 * @param config {@link JSONConfig}
	 * @return {@link CopyOptions}
	 * @since 5.8.0
	 */
	public static CopyOptions toCopyOptions(final JSONConfig config) {
		return CopyOptions.of()
				.setIgnoreCase(config.isIgnoreCase())
				.setIgnoreError(config.isIgnoreError())
				.setIgnoreNullValue(config.isIgnoreNullValue())
				.setTransientSupport(config.isTransientSupport())
				// 使用JSON转换器
				.setConverter(config.getConverter());
	}

	/**
	 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
	 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
	 * JSON字符串中不能包含控制字符和未经转义的引号和反斜杠
	 *
	 * @param string 字符串
	 * @return 适合在JSON中显示的字符串
	 */
	public static String quote(final String string) {
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
	public static String quote(final String string, final boolean isWrap) {
		return quote(string, new StringWriter(), isWrap).toString();
	}

	/**
	 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
	 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
	 * JSON字符串中不能包含控制字符和未经转义的引号和反斜杠
	 *
	 * @param str    字符串
	 * @param writer Writer
	 * @throws IORuntimeException IO异常
	 */
	public static void quote(final String str, final Writer writer) throws IORuntimeException {
		quote(str, writer, true);
	}

	/**
	 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
	 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
	 * JSON字符串中不能包含控制字符和未经转义的引号和反斜杠
	 *
	 * @param str    字符串
	 * @param writer Writer
	 * @param isWrap 是否使用双引号包装字符串
	 * @return Writer
	 * @throws IORuntimeException IO异常
	 * @since 3.3.1
	 */
	public static Writer quote(final String str, final Writer writer, final boolean isWrap) throws IORuntimeException {
		try {
			return _quote(str, writer, isWrap);
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
	 * @param config   JSON配置项，{@code null}则使用默认配置
	 * @return Map
	 */
	static Map<String, Object> createRawMap(final int capacity, JSONConfig config) {
		final Map<String, Object> rawHashMap;
		if (null == config) {
			config = JSONConfig.of();
		}
		final Comparator<String> keyComparator = config.getKeyComparator();
		if (config.isIgnoreCase()) {
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

	/**
	 * 根据值类型获取{@link JSONValueWriter}，首先判断对象是否实现了{@link JSONValueWriter}接口<br>
	 * 如果未实现从{@link GlobalValueWriterMapping}中查找全局的writer，否则返回null。
	 *
	 * @param value 值
	 * @param <T>   值类型
	 * @return {@link JSONValueWriter}
	 */
	@SuppressWarnings("unchecked")
	public static <T> JSONValueWriter<T> getValueWriter(final T value) {
		if (value instanceof JSONValueWriter) {
			return (JSONValueWriter<T>) value;
		}
		// 全局自定义序列化，支持null的自定义写出
		return (JSONValueWriter<T>) GlobalValueWriterMapping.get(null == value ? null : value.getClass());
	}

	/**
	 * 根据目标类型，获取对应的{@link JSONDeserializer}，首先判断是否实现了{@link JSONDeserializer}接口<br>
	 * 如果未实现从{@link GlobalSerializeMapping}中查找全局的{@link JSONDeserializer}，否则返回null
	 *
	 * @param targetType 目标类型
	 * @param <T> 目标类型
	 * @return {@link JSONDeserializer}
	 */
	@SuppressWarnings("unchecked")
	public static <T> JSONDeserializer<T> getDeserializer(final Type targetType) {
		final Class<T> rawType = (Class<T>) TypeUtil.getClass(targetType);
		if (null != rawType && JSONDeserializer.class.isAssignableFrom(rawType)) {
			return (JSONDeserializer<T>) ConstructorUtil.newInstanceIfPossible(rawType);
		}

		// 全局自定义反序列化（优先级低于实现JSONDeserializer接口）
		return (JSONDeserializer<T>) GlobalSerializeMapping.getDeserializer(targetType);
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
	private static Writer _quote(final String str, final Writer writer, final boolean isWrap) throws IOException {
		if (StrUtil.isEmpty(str)) {
			if (isWrap) {
				writer.write("\"\"");
			}
			return writer;
		}

		char c; // 当前字符
		final int len = str.length();
		if (isWrap) {
			writer.write('"');
		}
		for (int i = 0; i < len; i++) {
			c = str.charAt(i);
			switch (c) {
				case '\\':
				case '"':
					writer.write("\\");
					writer.write(c);
					break;
				default:
					writer.write(escape(c));
			}
		}
		if (isWrap) {
			writer.write('"');
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
