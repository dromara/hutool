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
import org.dromara.hutool.core.convert.MatcherConverter;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * 强转转换器
 *
 * @author Looly
 * @since 4.0.2
 */
public class CastConverter implements MatcherConverter, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例
	 */
	public static final CastConverter INSTANCE = new CastConverter();

	@Override
	public boolean match(final Type targetType, final Class<?> rawType, final Object value) {
		return rawType.isInstance(value);
	}

	@Override
	public Object convert(final Type targetType, final Object value) throws ConvertException{
		// 此处无需逻辑，目标对象类型是value的父类或接口，直接返回匹配即可
		return value;
	}
}
