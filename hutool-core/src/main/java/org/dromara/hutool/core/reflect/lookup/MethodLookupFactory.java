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
