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

package cn.hutool.core.convert.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.convert.CompositeConverter;
import cn.hutool.core.convert.Converter;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.reflect.TypeReference;
import cn.hutool.core.reflect.TypeUtil;
import cn.hutool.core.text.StrUtil;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

/**
 * {@link Map} 转换器
 *
 * @author Looly
 * @since 3.0.8
 */
public class MapConverter implements Converter, Serializable {
	private static final long serialVersionUID = 1L;

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
	 */
	@SuppressWarnings("rawtypes")
	public Map<?, ?> convert(final Type targetType, final Type keyType, final Type valueType, final Object value) {
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
			map = MapUtil.createMap(TypeUtil.getClass(targetType));
			convertMapToMap(keyType, valueType, (Map) value, map);
		} else if (BeanUtil.isBean(value.getClass())) {
			map = BeanUtil.beanToMap(value);
			// 二次转换，转换键值类型
			map = convert(targetType, keyType, valueType, map);
		} else {
			throw new UnsupportedOperationException(StrUtil.format("Unsupported toMap value type: {}", value.getClass().getName()));
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
		srcMap.forEach((key, value) -> {
			key = TypeUtil.isUnknown(keyType) ? key : convert.convert(keyType, key, null);
			value = TypeUtil.isUnknown(valueType) ? value : convert.convert(valueType, value, null);
			targetMap.put(key, value);
		});
	}
}
