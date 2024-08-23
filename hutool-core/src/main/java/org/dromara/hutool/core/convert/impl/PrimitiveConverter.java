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

import org.dromara.hutool.core.convert.AbstractConverter;
import org.dromara.hutool.core.convert.ConvertException;
import org.dromara.hutool.core.convert.MatcherConverter;

import java.io.Serializable;
import java.lang.reflect.Type;

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
public class PrimitiveConverter extends AbstractConverter implements MatcherConverter, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例对象
	 */
	public static final PrimitiveConverter INSTANCE = new PrimitiveConverter();

	@Override
	public boolean match(final Type targetType, final Class<?> rawType, final Object value) {
		return rawType.isPrimitive();
	}

	@Override
	protected Object convertInternal(final Class<?> primitiveClass, final Object value) {
		final Object result;
		if (byte.class == primitiveClass) {
			result = NumberConverter.INSTANCE.convert(Byte.class, value);
		} else if (short.class == primitiveClass) {
			result = NumberConverter.INSTANCE.convert(Short.class, value);
		} else if (int.class == primitiveClass) {
			result = NumberConverter.INSTANCE.convert(Integer.class, value);
		} else if (long.class == primitiveClass) {
			result = NumberConverter.INSTANCE.convert(Long.class, value);
		} else if (float.class == primitiveClass) {
			result = NumberConverter.INSTANCE.convert(Float.class, value);
		} else if (double.class == primitiveClass) {
			result = NumberConverter.INSTANCE.convert(Double.class, value);
		} else if (char.class == primitiveClass) {
			result = CharacterConverter.INSTANCE.convert(Character.class, value);
		} else if (boolean.class == primitiveClass) {
			result = BooleanConverter.INSTANCE.convert(Boolean.class, value);
		} else{
			throw new ConvertException("Unsupported target type: {}", primitiveClass);
		}

		if(null == result){
			throw new ConvertException("Can not convert {} to {}", value, primitiveClass);
		}

		return result;
	}
}
