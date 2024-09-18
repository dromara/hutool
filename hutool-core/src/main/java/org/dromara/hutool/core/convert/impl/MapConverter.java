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

package org.dromara.hutool.core.convert.impl;

import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.convert.*;
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
public class MapConverter extends ConverterWithRoot implements MatcherConverter, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例
	 */
	public static final MapConverter INSTANCE = new MapConverter(CompositeConverter.getInstance());

	/**
	 * 构造
	 *
	 * @param rootConverter 根转换器，用于转换Map中键值对中的值，非{@code null}
	 */
	public MapConverter(final Converter rootConverter) {
		super(rootConverter);
	}

	@Override
	public boolean match(final Type targetType, final Class<?> rawType, final Object value) {
		return Map.class.isAssignableFrom(rawType);
	}

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
	public Map<?, ?> convert(final Type targetType, final Type keyType, final Type valueType, final Object value) throws ConvertException{
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
		} else if (BeanUtil.isWritableBean(value.getClass())) {
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
		srcMap.forEach((key, value) -> targetMap.put(
			TypeUtil.isUnknown(keyType) ? key : rootConverter.convert(keyType, key),
			TypeUtil.isUnknown(valueType) ? value : rootConverter.convert(valueType, value)
		));
	}
}
