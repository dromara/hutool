/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.reflect.method;

import org.dromara.hutool.core.exception.ExceptionUtil;
import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.reflect.lookup.LookupUtil;

import java.lang.invoke.MethodHandle;
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
	 * 执行方法句柄，{@link MethodHandle#invokeWithArguments(Object...)}包装<br>
	 * 非static方法需先调用{@link MethodHandle#bindTo(Object)}绑定执行对象。
	 *
	 * <p>
	 * 需要注意的是，此处没有使用{@link MethodHandle#invoke(Object...)}，因为其参数第一个必须为对象或类。<br>
	 * {@link MethodHandle#invokeWithArguments(Object...)}只需传参数即可。
	 * </p>
	 *
	 * @param methodHandle {@link MethodHandle}
	 * @param args         方法参数值，支持子类转换和自动拆装箱
	 * @param <T>          返回值类型
	 * @return 方法返回值
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invokeHandle(final MethodHandle methodHandle, final Object... args) {
		try {
			return (T) methodHandle.invokeWithArguments(args);
		} catch (final Throwable e) {
			throw ExceptionUtil.wrapRuntime(e);
		}
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
	 * @param <T>    返回结果类型
	 * @param obj    接口的子对象或代理对象
	 * @param method 方法
	 * @param args   参数，自动根据{@link Method}定义类型转换
	 * @return 结果
	 * @throws HutoolException 执行异常包装
	 */
	public static <T> T invoke(final Object obj, final Method method, final Object... args) throws HutoolException {
		Assert.notNull(method, "Method must be not null!");
		return invokeExact(obj, method, MethodUtil.actualArgs(method, args));
	}

	/**
	 * 执行接口或对象中的方法，参数类型不做转换，必须与方法参数类型完全匹配<br>
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
	 * @throws HutoolException 执行异常包装
	 */
	public static <T> T invokeExact(final Object obj, final Method method, final Object... args) throws HutoolException {
		Assert.notNull(method, "Method must be not null!");
		MethodHandle handle;
		try {
			handle = LookupUtil.unreflectMethod(method);
		} catch (final Throwable e) {
			throw ExceptionUtil.wrapRuntime(e);
		}

		if (null != obj) {
			handle = handle.bindTo(obj);
		}
		return invokeHandle(handle, args);
	}
}
