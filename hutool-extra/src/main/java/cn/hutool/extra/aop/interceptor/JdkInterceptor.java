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

package cn.hutool.extra.aop.interceptor;

import cn.hutool.core.reflect.ModifierUtil;
import cn.hutool.core.reflect.ReflectUtil;
import cn.hutool.extra.aop.aspects.Aspect;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * JDK实现的动态代理切面
 *
 * @author Looly
 * @author ted.L
 */
public class JdkInterceptor implements InvocationHandler, Serializable {
	private static final long serialVersionUID = 1L;

	private final Object target;
	private final Aspect aspect;

	/**
	 * 构造
	 *
	 * @param target 被代理对象
	 * @param aspect 切面实现
	 */
	public JdkInterceptor(final Object target, final Aspect aspect) {
		this.target = target;
		this.aspect = aspect;
	}

	/**
	 * 获取目标对象
	 *
	 * @return 目标对象
	 */
	public Object getTarget() {
		return this.target;
	}

	@SuppressWarnings("SuspiciousInvocationHandlerImplementation")
	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		final Object target = this.target;
		final Aspect aspect = this.aspect;
		Object result = null;

		// 开始前回调
		if (aspect.before(target, method, args)) {
			ReflectUtil.setAccessible(method);

			try {
				result = method.invoke(ModifierUtil.isStatic(method) ? null : target, args);
			} catch (final InvocationTargetException e) {
				// 异常回调（只捕获业务代码导致的异常，而非反射导致的异常）
				if (aspect.afterException(target, method, args, e.getTargetException())) {
					throw e;
				}
			}

			// 结束执行回调
			if (aspect.after(target, method, args, result)) {
				return result;
			}
		}

		return null;
	}

}
