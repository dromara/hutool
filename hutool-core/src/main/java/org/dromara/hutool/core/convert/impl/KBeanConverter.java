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
import org.dromara.hutool.core.bean.copier.ValueProvider;
import org.dromara.hutool.core.bean.copier.provider.BeanValueProvider;
import org.dromara.hutool.core.bean.copier.provider.MapValueProvider;
import org.dromara.hutool.core.convert.ConvertException;
import org.dromara.hutool.core.convert.Converter;
import org.dromara.hutool.core.convert.MatcherConverter;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.reflect.TypeUtil;
import org.dromara.hutool.core.reflect.kotlin.KClassUtil;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Kotlin Bean转换器，支持：
 * <pre>
 * Map =》 Bean
 * Bean =》 Bean
 * ValueProvider =》 Bean
 * </pre>
 *
 * @author Looly
 */
public class KBeanConverter implements MatcherConverter, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例对象
	 */
	public static KBeanConverter INSTANCE = new KBeanConverter();

	@Override
	public boolean match(final Type targetType, final Class<?> rawType, final Object value) {
		return KClassUtil.isKotlinClass(rawType);
	}

	@Override
	public Object convert(final Type targetType, final Object value) throws ConvertException {
		Assert.notNull(targetType);
		if (null == value) {
			return null;
		}

		// value本身实现了Converter接口，直接调用
		if(value instanceof Converter){
			return ((Converter) value).convert(targetType, value);
		}

		final Class<?> targetClass = TypeUtil.getClass(targetType);
		Assert.notNull(targetClass, "Target type is not a class!");

		return convertInternal(targetType, targetClass, value);
	}

	@SuppressWarnings("unchecked")
	private Object convertInternal(final Type targetType, final Class<?> targetClass, final Object value) {
		ValueProvider<String> valueProvider = null;
		if(value instanceof ValueProvider){
			valueProvider = (ValueProvider<String>) value;
		} else if(value instanceof Map){
			valueProvider = new MapValueProvider((Map<String, ?>) value);
		} else if(BeanUtil.isWritableBean(value.getClass())){
			valueProvider = new BeanValueProvider(value);
		}

		if(null != valueProvider){
			return KClassUtil.newInstance(targetClass, valueProvider);
		}

		throw new ConvertException("Unsupported source type: [{}] to [{}]", value.getClass(), targetType);
	}
}
