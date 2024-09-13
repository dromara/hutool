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
import org.dromara.hutool.core.convert.Converter;
import org.dromara.hutool.core.reflect.TypeUtil;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReference;

/**
 * {@link AtomicReference}转换器
 *
 * @author Looly
 * @since 3.0.8
 */
public class AtomicReferenceConverter extends AbstractConverter {
	private static final long serialVersionUID = 1L;

	private final Converter converter;

	/**
	 * 构造
	 *
	 * @param converter 用于转换AtomicReference包装的对象类型
	 */
	public AtomicReferenceConverter(final Converter converter) {
		this.converter = converter;
	}

	@Override
	protected AtomicReference<?> convertInternal(final Class<?> targetClass, final Object value) {

		//尝试将值转换为Reference泛型的类型
		Object targetValue = null;
		final Type paramType = TypeUtil.getTypeArgument(AtomicReference.class);
		if (!TypeUtil.isUnknown(paramType)) {
			targetValue = converter.convert(paramType, value);
		}
		if (null == targetValue) {
			targetValue = value;
		}

		return new AtomicReference<>(targetValue);
	}

}
