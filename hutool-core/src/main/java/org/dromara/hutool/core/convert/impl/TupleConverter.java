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

import org.dromara.hutool.core.convert.ConvertException;
import org.dromara.hutool.core.convert.Converter;
import org.dromara.hutool.core.lang.tuple.Tuple;

import java.lang.reflect.Type;

/**
 * {@link Tuple}转换器
 *
 * @author looly
 * @since 6.0.0
 */
public class TupleConverter implements Converter {

	/**
	 * 单例
	 */
	public static final TupleConverter INSTANCE = new TupleConverter();

	@Override
	public Object convert(final Type targetType, final Object value) throws ConvertException {
		final Object[] convert = ArrayConverter.INSTANCE.convert(Object[].class, value);
		return Tuple.of(convert);
	}
}
