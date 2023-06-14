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

package org.dromara.hutool.core.convert.impl;

import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.bean.copier.ValueProvider;
import org.dromara.hutool.core.bean.copier.provider.BeanValueProvider;
import org.dromara.hutool.core.bean.copier.provider.MapValueProvider;
import org.dromara.hutool.core.convert.ConvertException;
import org.dromara.hutool.core.convert.Converter;
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
public class KBeanConverter implements Converter, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例对象
	 */
	public static KBeanConverter INSTANCE = new KBeanConverter();

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
