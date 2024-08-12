/*
 * Copyright (c) 2013-2024 Hutool Team.
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

import org.dromara.hutool.core.convert.AbstractConverter;
import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.convert.ConvertException;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;

import java.util.function.Function;

/**
 * 原始类型转换器<br>
 * 支持类型为：<br>
 * <ul>
 * 		<li>{@code byte}</li>
 * 		<li>{@code short}</li>
 * 		 <li>{@code int}</li>
 * 		 <li>{@code long}</li>
 * 		<li>{@code float}</li>
 * 		<li>{@code double}</li>
 * 		<li>{@code char}</li>
 * 		<li>{@code boolean}</li>
 * </ul>
 *
 * @author Looly
 */
public class PrimitiveConverter extends AbstractConverter {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例对象
	 */
	public static final PrimitiveConverter INSTANCE = new PrimitiveConverter();

	/**
	 * 构造<br>
	 *
	 * @throws IllegalArgumentException 传入的转换类型非原始类型时抛出
	 */
	public PrimitiveConverter() {
	}

	@Override
	protected Object convertInternal(final Class<?> targetClass, final Object value) {
		return PrimitiveConverter.convert(value, targetClass, this::convertToStr);
	}

	@Override
	protected String convertToStr(final Object value) {
		return StrUtil.trim(super.convertToStr(value));
	}

	/**
	 * 将指定值转换为原始类型的值
	 * @param value 值
	 * @param primitiveClass 原始类型
	 * @param toStringFunc 当无法直接转换时，转为字符串后再转换的函数
	 * @return 转换结果
	 * @since 5.5.0
	 */
	protected static Object convert(final Object value, final Class<?> primitiveClass, final Function<Object, String> toStringFunc) {
		if (byte.class == primitiveClass) {
			return ObjUtil.defaultIfNull(NumberConverter.convert(value, Byte.class, toStringFunc), 0);
		} else if (short.class == primitiveClass) {
			return ObjUtil.defaultIfNull(NumberConverter.convert(value, Short.class, toStringFunc), 0);
		} else if (int.class == primitiveClass) {
			return ObjUtil.defaultIfNull(NumberConverter.convert(value, Integer.class, toStringFunc), 0);
		} else if (long.class == primitiveClass) {
			return ObjUtil.defaultIfNull(NumberConverter.convert(value, Long.class, toStringFunc), 0);
		} else if (float.class == primitiveClass) {
			return ObjUtil.defaultIfNull(NumberConverter.convert(value, Float.class, toStringFunc), 0);
		} else if (double.class == primitiveClass) {
			return ObjUtil.defaultIfNull(NumberConverter.convert(value, Double.class, toStringFunc), 0);
		} else if (char.class == primitiveClass) {
			return Convert.convert(Character.class, value);
		} else if (boolean.class == primitiveClass) {
			return Convert.convert(Boolean.class, value);
		}

		throw new ConvertException("Unsupported target type: {}", primitiveClass);
	}
}
