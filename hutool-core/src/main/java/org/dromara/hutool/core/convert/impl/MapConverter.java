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
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.core.reflect.TypeUtil;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * {@link Map} 转换器，通过预定义key和value的类型，实现：
 * <ul>
 *     <li>Map 转 Map，key和value类型自动转换</li>
 *     <li>Bean 转 Map，字段和字段值类型自动转换</li>
 * </ul>
 *
 * @author Looly
 * @since 3.0.8
 */
public class MapConverter implements Converter, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例
	 */
	public static MapConverter INSTANCE = new MapConverter();

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
	public Map<?, ?> convert(final Type targetType, final Type keyType, final Type valueType, final Object value)
		throws ConvertException{
		Map map;
		if (value instanceof Map) {
			final Class<?> valueClass = value.getClass();
			if (valueClass.equals(targetType)) {
				final Type[] typeArguments = TypeUtil.getTypeArguments(valueClass);
				if (null != typeArguments //
						&& 2 == typeArguments.length//
						&& Objects.equals(keyType, typeArguments[0]) //
						&& Objects.equals(valueType, typeArguments[1])) {
					//对于键值对类型一致的Map对象，不再做转换，直接返回原对象
					return (Map) value;
				}
			}

			map = MapUtil.createMap(TypeUtil.getClass(targetType), LinkedHashMap::new);
			convertMapToMap(keyType, valueType, (Map) value, map);
		} else if (BeanUtil.isBean(value.getClass())) {
			map = BeanUtil.beanToMap(value);
			// 二次转换，转换键值类型
			map = convert(targetType, keyType, valueType, map);
		} else {
			throw new ConvertException("Unsupported to map from [{}] of type: {}", value, value.getClass().getName());
		}
		return map;
	}

	/**
	 * Map转Map
	 *
	 * @param srcMap    源Map
	 * @param targetMap 目标Map
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private void convertMapToMap(final Type keyType, final Type valueType, final Map<?, ?> srcMap, final Map targetMap) {
		final CompositeConverter convert = CompositeConverter.getInstance();
		srcMap.forEach((key, value) -> targetMap.put(
			TypeUtil.isUnknown(keyType) ? key : convert.convert(keyType, key),
			TypeUtil.isUnknown(valueType) ? value : convert.convert(valueType, value)
		));
	}
}
