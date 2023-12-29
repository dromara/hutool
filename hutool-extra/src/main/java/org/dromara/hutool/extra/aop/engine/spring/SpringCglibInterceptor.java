/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.aop.engine.spring;

import org.dromara.hutool.extra.aop.Aspect;
import org.dromara.hutool.extra.aop.SimpleInterceptor;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Spring-cglib实现的动态代理切面<br>
 * 只用于JDK8
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
