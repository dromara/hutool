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
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.reflect.lookup.LookupUtil;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

/**
 * 方法句柄{@link MethodHandle}封装工具类<br>
 * 方法句柄是一个有类型的，可以直接执行的指向底层方法、构造器、field等的引用，可以简单理解为函数指针，它是一种更加底层的查找、调整和调用方法的机制。
 *
 * <p>
 * {@link MethodHandle}类似于C/C++的函数指针，用于模拟在字节码层次的方法调用，且可以采用字节码优化，优于反射。
 * </p>
 * <p>
 * 参考：
 * <ul>
 *     <li><a href="https://stackoverflow.com/questions/22614746/how-do-i-invoke-java-8-default-methods-reflectively">
 *         https://stackoverflow.com/questions/22614746/how-do-i-invoke-java-8-default-methods-reflectively</a></li>
 * </ul>
 *
 * @author looly
 * @since 5.7.7
 */
public class MethodHandleUtil {

	/**
	 * 执行方法句柄，{@link MethodHandle#invoke(Object...)}包装<br>
	 *
	 * @param methodHandle {@link MethodHandle}
	 * @param args         方法参数值，支持子类转换和自动拆装箱
	 * @param <T>          返回值类型
	 * @return 方法返回值
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invokeHandle(final MethodHandle methodHandle, final Object... args) {
		try {
			return (T) methodHandle.invoke(args);
		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 执行接口或对象中的方法
	 *
	 * @param <T>    返回结果类型
	 * @param obj    接口的子对象或代理对象
	 * @param method 方法
	 * @param args   参数
	 * @return 结果
	 */
	public static <T> T invoke(final Object obj, final Method method, final Object... args) {
		return invoke(false, obj, method, args);
	}

	/**
	 * 执行接口或对象中的特殊方法（private、static等）<br>
	 *
	 * <pre class="code">
	 *     interface Duck {
	 *         default String quack() {
	 *             return "Quack";
	 *         }
	 *     }
	 *     Duck duck = (Duck) Proxy.newProxyInstance(
	 *         ClassLoaderUtil.getClassLoader(),
	 *         new Class[] { Duck.class },
	 *         MethodHandleUtil::invokeDefault);
	 * </pre>
	 *
	 * @param <T>        返回结果类型
	 * @param obj        接口的子对象或代理对象
	 * @param methodName 方法名称
	 * @param args       参数
	 * @return 结果
	 */
	public static <T> T invokeSpecial(final Object obj, final String methodName, final Object... args) {
		Assert.notNull(obj, "Object to get method must be not null!");
		Assert.notBlank(methodName, "Method name must be not blank!");

		final Method method = MethodUtil.getMethodOfObj(obj, methodName, args);
		if (null == method) {
			throw new UtilException("No such method: [{}] from [{}]", methodName, obj.getClass());
		}
		return invokeSpecial(obj, method, args);
	}

	/**
	 * 执行接口或对象中的特殊方法（private、static等）<br>
	 *
	 * <pre class="code">
	 *     interface Duck {
	 *         default String quack() {
	 *             return "Quack";
	 *         }
	 *     }
	 *     Duck duck = (Duck) Proxy.newProxyInstance(
	 *         ClassLoaderUtil.getClassLoader(),
	 *         new Class[] { Duck.class },
	 *         MethodHandleUtil::invoke);
	 * </pre>
	 *
	 * @param <T>    返回结果类型
	 * @param obj    接口的子对象或代理对象
	 * @param method 方法
	 * @param args   参数
	 * @return 结果
	 */
	public static <T> T invokeSpecial(final Object obj, final Method method, final Object... args) {
		return invoke(true, obj, method, args);
	}

	/**
	 * 执行接口或对象中的方法<br>
	 *
	 * <pre class="code">
	 *     interface Duck {
	 *         default String quack() {
	 *             return "Quack";
	 *         }
	 *     }
	 *     Duck duck = (Duck) Proxy.newProxyInstance(
	 *         ClassLoaderUtil.getClassLoader(),
	 *         new Class[] { Duck.class },
	 *         MethodHandleUtil::invoke);
	 * </pre>
	 *
	 * @param <T>       返回结果类型
	 * @param isSpecial 是否为特殊方法（private、static等）
	 * @param obj       接口的子对象或代理对象
	 * @param method    方法
	 * @param args      参数
	 * @return 结果
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invoke(final boolean isSpecial, final Object obj, final Method method, final Object... args) {
		Assert.notNull(method, "Method must be not null!");
		final Class<?> declaringClass = method.getDeclaringClass();
		final MethodHandles.Lookup lookup = LookupUtil.lookup(declaringClass);
		try {
			MethodHandle handle = isSpecial ? lookup.unreflectSpecial(method, declaringClass)
					: lookup.unreflect(method);
			if (null != obj) {
				handle = handle.bindTo(obj);
			}
			return (T) handle.invokeWithArguments(args);
		} catch (final Throwable e) {
			throw new UtilException(e);
		}
	}
}
