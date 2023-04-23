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

package org.dromara.hutool.core.reflect.lookup;

import org.dromara.hutool.core.exception.HutoolException;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * jdk11中直接调用MethodHandles.lookup()获取到的MethodHandles.Lookup只能对接口类型才会权限获取方法的方法句柄MethodHandle。
 * 如果是普通类型Class,需要使用jdk9开始提供的 MethodHandles#privateLookupIn(java.lang.Class, java.lang.invoke.MethodHandles.Lookup)方法.
 * <p>
 * 参考：https://blog.csdn.net/u013202238/article/details/108687086
 *
 * @author looly
 * @since 6.0.0
 */
public class MethodLookupFactory implements LookupFactory {

	private final Method privateLookupInMethod;

	/**
	 * 构造
	 */
	public MethodLookupFactory() {
		this.privateLookupInMethod = createJdk9PrivateLookupInMethod();
	}

	@Override
	public MethodHandles.Lookup lookup(final Class<?> callerClass) {
		try {
			return (MethodHandles.Lookup) privateLookupInMethod.invoke(MethodHandles.class, callerClass, MethodHandles.lookup());
		} catch (final IllegalAccessException e) {
			throw new HutoolException(e);
		} catch (final InvocationTargetException e) {
			throw new HutoolException(e.getTargetException());
		}
	}

	private static Method createJdk9PrivateLookupInMethod() {
		try {
			return MethodHandles.class.getMethod("privateLookupIn", Class.class, MethodHandles.Lookup.class);
		} catch (final NoSuchMethodException e) {
			//可能是jdk9 以下版本
			throw new IllegalStateException(
				"There is no 'privateLookupIn(Class, Lookup)' method in java.lang.invoke.MethodHandles.", e);
		}
	}
}
