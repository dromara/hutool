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

package org.dromara.hutool.core.convert.impl;

import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.convert.CompositeConverter;
import org.dromara.hutool.core.convert.ConvertException;
import org.dromara.hutool.core.convert.Converter;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.core.reflect.TypeUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.CharUtil;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * {@link Map.Entry} 转换器，支持以下类型转为Entry
 * <ul>
 *     <li>{@link Map}</li>
 *     <li>{@link Map.Entry}</li>
 *     <li>带分隔符的字符串，支持分隔符{@code :}、{@code =}、{@code ,}</li>
 *     <li>Bean，包含{@code getKey}和{@code getValue}方法</li>
 * </ul>
 *
 * @author looly
 */
public class EntryConverter implements Converter {

	/**
	 * 单例
	 */
	public static final EntryConverter INSTANCE = new EntryConverter();

	@Override
	public Object convert(Type targetType, final Object value) throws ConvertException {
		if (targetType instanceof TypeReference) {
			targetType = ((TypeReference<?>) targetType).getType();
		}
		final Type keyType = TypeUtil.getTypeArgument(targetType, 0);
		final Type valueType = TypeUtil.getTypeArgument(targetType, 1);

		return convert(targetType, keyType, valueType, value);
	}

	/**
	 * 转换对象为指定键值类型的指定类型Map
	 *
	 * @param targetType 目标的Map类型
	 * @param keyType    键类型
	 * @param valueType  值类型
	 * @param value      被转换的值
	 * @return 转换后的Map
	 * @throws ConvertException 转换异常或不支持的类型
	 */
	@SuppressWarnings("rawtypes")
	public Map.Entry<?, ?> convert(final Type targetType, final Type keyType, final Type valueType, final Object value)
		throws ConvertException {
		Map map = null;
		if (value instanceof Map.Entry) {
			final Map.Entry entry = (Map.Entry) value;
			map = MapUtil.of(entry.getKey(), entry.getValue());
		}else if (value instanceof Map) {
			map = (Map) value;
		} else if (value instanceof CharSequence) {
			final CharSequence str = (CharSequence) value;
			map = strToMap(str);
		} else if (BeanUtil.isBean(value.getClass())) {
			map = BeanUtil.beanToMap(value);
		}

		if (null != map) {
			return mapToEntry(targetType, keyType, valueType, map);
		}

		throw new ConvertException("Unsupported to map from [{}] of type: {}", value, value.getClass().getName());
	}

	/**
	 * 字符串转单个键值对的Map，支持分隔符{@code :}、{@code =}、{@code ,}
	 *
	 * @param str 字符串
	 * @return map or null
	 */
	private static Map<CharSequence, CharSequence> strToMap(final CharSequence str) {
		// key:value  key=value  key,value
		final int index = StrUtil.indexOf(str,
			c -> c == CharUtil.COLON || c == CharUtil.EQUAL || c == CharUtil.COMMA,
			0, str.length());

		if (index > -1) {
			return MapUtil.of(str.subSequence(0, index), str.subSequence(index + 1, str.length()));
		}
		return null;
	}

	/**
	 * Map转Entry
	 *
	 * @param targetType 目标的Map类型
	 * @param keyType    键类型
	 * @param valueType  值类型
	 * @param map        被转换的map
	 * @return Entry
	 */
	@SuppressWarnings("rawtypes")
	private static Map.Entry<?, ?> mapToEntry(final Type targetType, final Type keyType, final Type valueType, final Map map) {

		Object key = null;
		Object value = null;
		if (1 == map.size()) {
			final Map.Entry entry = (Map.Entry) map.entrySet().iterator().next();
			key = entry.getKey();
			value = entry.getValue();
		} else if (2 == map.size()) {
			key = map.get("key");
			value = map.get("value");
		}

		final CompositeConverter convert = CompositeConverter.getInstance();
		return (Map.Entry<?, ?>) ConstructorUtil.newInstance(TypeUtil.getClass(targetType),
			TypeUtil.isUnknown(keyType) ? key : convert.convert(keyType, key),
			TypeUtil.isUnknown(valueType) ? value : convert.convert(valueType, value)
		);
	}
}
