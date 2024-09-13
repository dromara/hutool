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
import org.dromara.hutool.core.convert.ConvertException;
import org.dromara.hutool.core.convert.Converter;
import org.dromara.hutool.core.convert.ConverterWithRoot;
import org.dromara.hutool.core.lang.tuple.Triple;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.core.reflect.TypeUtil;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * {@link Triple} 转换器，支持以下类型转为Triple：
 * <ul>
 *     <li>Bean，包含{@code getLeft}、{@code getMiddle}和{@code getRight}方法</li>
 * </ul>
 *
 * @author looly
 * @since 6.0.0
 */
public class TripleConverter extends ConverterWithRoot implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 *
	 * @param rootConverter 根转换器，用于转换无法被识别的对象
	 */
	public TripleConverter(final Converter rootConverter) {
		super(rootConverter);
	}

	@Override
	public Object convert(Type targetType, final Object value) throws ConvertException {
		if (targetType instanceof TypeReference) {
			targetType = ((TypeReference<?>) targetType).getType();
		}
		final Type leftType = TypeUtil.getTypeArgument(targetType, 0);
		final Type middileType = TypeUtil.getTypeArgument(targetType, 1);
		final Type rightType = TypeUtil.getTypeArgument(targetType, 2);

		return convert(leftType, middileType, rightType, value);
	}

	/**
	 * 转换对象为指定键值类型的指定类型Map
	 *
	 * @param leftType   键类型
	 * @param middleType 中值类型
	 * @param rightType  值类型
	 * @param value      被转换的值
	 * @return 转换后的Map
	 * @throws ConvertException 转换异常或不支持的类型
	 */
	@SuppressWarnings("rawtypes")
	public Triple<?, ?, ?> convert(final Type leftType, final Type middleType, final Type rightType, final Object value)
		throws ConvertException {
		Map map = null;
		if (value instanceof Map) {
			map = (Map) value;
		} else if (BeanUtil.isReadableBean(value.getClass())) {
			// 一次性只读场景，包装为Map效率更高
			map = BeanUtil.toBeanMap(value);
		}

		if (null != map) {
			return mapToTriple(leftType, middleType, rightType, map);
		}

		throw new ConvertException("Unsupported to map from [{}] of type: {}", value, value.getClass().getName());
	}

	/**
	 * Map转Entry
	 *
	 * @param leftType  键类型
	 * @param rightType 值类型
	 * @param map       被转换的map
	 * @return Entry
	 */
	@SuppressWarnings("rawtypes")
	private Triple<?, ?, ?> mapToTriple(final Type leftType, final Type middleType, final Type rightType, final Map map) {

		final Object left = map.get("left");
		final Object middle = map.get("middle");
		final Object right = map.get("right");

		return Triple.of(
			TypeUtil.isUnknown(leftType) ? left : rootConverter.convert(leftType, left),
			TypeUtil.isUnknown(middleType) ? middle : rootConverter.convert(middleType, middle),
			TypeUtil.isUnknown(rightType) ? right : rootConverter.convert(rightType, right)
		);
	}
}
