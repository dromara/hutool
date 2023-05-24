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

package org.dromara.hutool.extra.aop.engine.spring;

import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.extra.aop.Aspect;
import org.dromara.hutool.extra.aop.engine.ProxyEngine;
import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.Constructor;

/**
 * 基于Spring-cglib的切面代理工厂
 *
 * @author looly
 */
public class SpringCglibProxyEngine implements ProxyEngine {

	@Override
	public <T> T proxy(final T target, final Aspect aspect) {
		final Class<?> targetClass = target.getClass();

		final Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(targetClass);
		enhancer.setCallback(new SpringCglibInterceptor(target, aspect));

		return create(enhancer, targetClass);
	}

	/**
	 * 创建代理对象
	 *
	 * @param <T>         代理对象类型
	 * @param enhancer    {@link Enhancer}
	 * @param targetClass 目标类型
	 * @return 代理对象
	 */
	@SuppressWarnings("unchecked")
	private static <T> T create(final Enhancer enhancer, final Class<?> targetClass) {
		final Constructor<?>[] constructors = ConstructorUtil.getConstructors(targetClass);
		Class<?>[] parameterTypes;
		Object[] values;
		IllegalArgumentException finalException = null;
		for (final Constructor<?> constructor : constructors) {
			parameterTypes = constructor.getParameterTypes();
			values = ClassUtil.getDefaultValues(parameterTypes);

			try {
				return (T) enhancer.create(parameterTypes, values);
			} catch (final IllegalArgumentException e) {
				//ignore
				finalException = e;
			}
		}
		if (null != finalException) {
			throw finalException;
		}

		throw new IllegalArgumentException("No constructor provided");
	}
}
