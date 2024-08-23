/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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
import org.dromara.hutool.core.convert.MatcherConverter;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.util.ObjUtil;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * 空值或空对象转换器，转换结果为目标类型对象的实例化对象
 *
 * @author Looly
 * @since 6.0.0
 */
public class EmptyBeanConverter extends AbstractConverter implements MatcherConverter, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例
	 */
	public static final EmptyBeanConverter INSTANCE = new EmptyBeanConverter();

	@Override
	public boolean match(final Type targetType, final Class<?> rawType, final Object value) {
		return ObjUtil.isEmpty(value);
	}

	@Override
	protected Object convertInternal(final Class<?> targetClass, final Object value) {
		// issue#3649 空值转空对象，则直接实例化
		return ConstructorUtil.newInstanceIfPossible(targetClass);
	}
}
