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

package org.dromara.hutool.core.convert.impl;

import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.convert.CompositeConverter;
import org.dromara.hutool.core.convert.ConvertException;
import org.dromara.hutool.core.convert.Converter;
import org.dromara.hutool.core.lang.tuple.Triple;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.core.reflect.TypeUtil;

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
public class TripleConverter implements Converter {

	/**
	 * 单例
	 */
	public static final TripleConverter INSTANCE = new TripleConverter();

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
		if (BeanUtil.isReadableBean(value.getClass())) {
			map = BeanUtil.beanToMap(value);
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
	private static Triple<?, ?, ?> mapToTriple(final Type leftType, final Type middleType, final Type rightType, final Map map) {

		final Object left = map.get("left");
		final Object middle = map.get("middle");
		final Object right = map.get("right");

		final CompositeConverter convert = CompositeConverter.getInstance();
		return Triple.of(
			TypeUtil.isUnknown(leftType) ? left : convert.convert(leftType, left),
			TypeUtil.isUnknown(middleType) ? middle : convert.convert(middleType, middle),
			TypeUtil.isUnknown(rightType) ? right : convert.convert(rightType, right)
		);
	}
}
