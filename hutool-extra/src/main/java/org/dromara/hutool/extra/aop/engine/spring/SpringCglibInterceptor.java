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

import org.dromara.hutool.extra.aop.Aspect;
import org.dromara.hutool.extra.aop.SimpleInterceptor;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Spring-cglib实现的动态代理切面
 *
 * @author looly
 */
public class SpringCglibInterceptor extends SimpleInterceptor implements MethodInterceptor {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 *
	 * @param target 被代理对象
	 * @param aspect 切面实现
	 */
	public SpringCglibInterceptor(final Object target, final Aspect aspect) {
		super(target, aspect);
	}

	@Override
	public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy) throws Throwable {
		final Object target = this.target;
		Object result = null;
		// 开始前回调
		if (aspect.before(target, method, args)) {
			try {
				result = proxy.invoke(target, args);
			} catch (final Throwable e) {
				Throwable throwable = e;
				if(throwable instanceof  InvocationTargetException){
					throwable = ((InvocationTargetException) throwable).getTargetException();
				}

				// 异常回调（只捕获业务代码导致的异常，而非反射导致的异常）
				if (aspect.afterException(target, method, args, throwable)) {
					throw throwable;
				}
			}
		}

		// 结束执行回调
		if (aspect.after(target, method, args, result)) {
			return result;
		}
		return null;
	}
}
