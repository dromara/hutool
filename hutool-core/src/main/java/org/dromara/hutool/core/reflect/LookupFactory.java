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

package org.dromara.hutool.core.reflect;

import org.dromara.hutool.core.exceptions.UtilException;
import org.dromara.hutool.core.lang.caller.CallerUtil;
import org.dromara.hutool.core.util.JdkUtil;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * {@link MethodHandles.Lookup}工厂，用于创建{@link MethodHandles.Lookup}对象<br>
 * {@link MethodHandles.Lookup}是一个方法句柄查找对象，用于在指定类中查找符合给定方法名称、方法类型的方法句柄。
 *
 * <p>
 * jdk8中如果直接调用{@link MethodHandles#lookup()}获取到的{@link MethodHandles.Lookup}在调用findSpecial和unreflectSpecial
 * 时会出现权限不够问题，抛出"no private access for invokespecial"异常，因此针对JDK8及JDK9+分别封装lookup方法。
 * </p>
 *
 * <p>
 * 参考：
 * <p><a href="https://blog.csdn.net/u013202238/article/details/108687086">https://blog.csdn.net/u013202238/article/details/108687086</a></p>
 *
 * @author looly
 * @since 5.7.7
 */
public class LookupFactory {

	private static final int ALLOWED_MODES = MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED
			| MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC;

	private static Method privateLookupInMethod;
	private static Constructor<MethodHandles.Lookup> jdk8LookupConstructor;

	static {
		if(JdkUtil.IS_JDK8){
			// jdk8 这种方式其实也适用于jdk9及以上的版本,但是上面优先,可以避免 jdk9 反射警告
			jdk8LookupConstructor = createJdk8LookupConstructor();
		} else {
			// jdk9+ 开始提供的java.lang.invoke.MethodHandles.privateLookupIn方法
			privateLookupInMethod = createJdk9PrivateLookupInMethod();
		}
	}

	/**
	 * jdk8中如果直接调用{@link MethodHandles#lookup()}获取到的{@link MethodHandles.Lookup}在调用findSpecial和unreflectSpecial
	 * 时会出现权限不够问题，抛出"no private access for invokespecial"异常，因此针对JDK8及JDK9+分别封装lookup方法。
	 *
	 * @return {@link MethodHandles.Lookup}
	 */
	public static MethodHandles.Lookup lookup() {
		return lookup(CallerUtil.getCaller());
	}

	/**
	 * jdk8中如果直接调用{@link MethodHandles#lookup()}获取到的{@link MethodHandles.Lookup}在调用findSpecial和unreflectSpecial
	 * 时会出现权限不够问题，抛出"no private access for invokespecial"异常，因此针对JDK8及JDK9+分别封装lookup方法。
	 *
	 * @param callerClass 被调用的类或接口
	 * @return {@link MethodHandles.Lookup}
	 */
	public static MethodHandles.Lookup lookup(final Class<?> callerClass) {
		//使用反射,因为当前jdk可能不是java9或以上版本
		if (privateLookupInMethod != null) {
			try {
				return (MethodHandles.Lookup) privateLookupInMethod.invoke(MethodHandles.class, callerClass, MethodHandles.lookup());
			} catch (final IllegalAccessException | InvocationTargetException e) {
				throw new UtilException(e);
			}
		}
		//jdk 8
		try {
			return jdk8LookupConstructor.newInstance(callerClass, ALLOWED_MODES);
		} catch (final Exception e) {
			throw new IllegalStateException("no 'Lookup(Class, int)' method in java.lang.invoke.MethodHandles.", e);
		}
	}

	@SuppressWarnings("JavaReflectionMemberAccess")
	private static Method createJdk9PrivateLookupInMethod(){
		try {
			return MethodHandles.class.getMethod("privateLookupIn", Class.class, MethodHandles.Lookup.class);
		} catch (final NoSuchMethodException e) {
			//可能是jdk9 以下版本
			throw new IllegalStateException(
					"There is no 'privateLookupIn(Class, Lookup)' method in java.lang.invoke.MethodHandles.", e);
		}
	}

	private static Constructor<MethodHandles.Lookup> createJdk8LookupConstructor(){
		final Constructor<MethodHandles.Lookup> constructor;
		try {
			constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
		} catch (final NoSuchMethodException e) {
			//可能是jdk8 以下版本
			throw new IllegalStateException(
					"There is no 'Lookup(Class, int)' constructor in java.lang.invoke.MethodHandles.", e);
		}
		constructor.setAccessible(true);
		return constructor;
	}
}
