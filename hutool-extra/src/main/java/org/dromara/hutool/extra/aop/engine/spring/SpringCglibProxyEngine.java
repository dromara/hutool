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

package org.dromara.hutool.extra.aop.engine.spring;

import org.dromara.hutool.core.lang.Assert;
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

	/**
	 * 构造
	 */
	public SpringCglibProxyEngine(){
		// SPI方式加载时检查BC库是否引入
		Assert.notNull(Enhancer.class);
	}

	@Override
	public <T> T proxy(final T target, final Aspect aspect) {
		final Class<?> targetClass = target.getClass();

		final Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(targetClass);
		enhancer.setCallback(new SpringCglibInterceptor(target, aspect));

		return create(enhancer, targetClass);
	}

	/**
	 * 创建代理对象<br>
	 * https://gitee.com/dromara/hutool/issues/I74EX7<br>
	 * 某些对象存在非空参数构造，则需遍历查找需要的构造完成代理对象构建。
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
