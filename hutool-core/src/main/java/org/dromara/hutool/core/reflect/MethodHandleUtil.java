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

import org.dromara.hutool.core.bean.NullWrapperBean;
import org.dromara.hutool.core.convert.Convert;
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
			return (T) methodHandle.invokeWithArguments(args);
		} catch (final Throwable e) {
			throw new RuntimeException(e);
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
	 * @param <T>       返回结果类型
	 * @param obj       接口的子对象或代理对象
	 * @param method    方法
	 * @param args      参数，自动根据{@link Method}定义类型转换
	 * @return 结果
	 * @throws HutoolException 执行异常包装
	 */
	public static <T> T invoke(final Object obj, final Method method, final Object... args) throws HutoolException{
		Assert.notNull(method, "Method must be not null!");
		return invokeExact(obj, method, actualArgs(method, args));
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
	 * @param <T>       返回结果类型
	 * @param obj       接口的子对象或代理对象
	 * @param method    方法
	 * @param args      参数
	 * @return 结果
	 * @throws HutoolException 执行异常包装
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invokeExact(final Object obj, final Method method, final Object... args) throws HutoolException{
		Assert.notNull(method, "Method must be not null!");
		try {
			MethodHandle handle = LookupUtil.unreflectMethod(method);
			if (null != obj) {
				handle = handle.bindTo(obj);
			}
			return (T) handle.invokeWithArguments(args);
		} catch (final Throwable e) {
			if(e instanceof RuntimeException){
				throw (RuntimeException)e;
			}
			throw new HutoolException(e);
		}
	}

	/**
	 * 检查用户传入参数：
	 * <ul>
	 *     <li>1、忽略多余的参数</li>
	 *     <li>2、参数不够补齐默认值</li>
	 *     <li>3、通过NullWrapperBean传递的参数,会直接赋值null</li>
	 *     <li>4、传入参数为null，但是目标参数类型为原始类型，做转换</li>
	 *     <li>5、传入参数类型不对应，尝试转换类型</li>
	 * </ul>
	 *
	 * @param method 方法
	 * @param args   参数
	 * @return 实际的参数数组
	 */
	private static Object[] actualArgs(final Method method, final Object[] args) {
		final Class<?>[] parameterTypes = method.getParameterTypes();
		final Object[] actualArgs = new Object[parameterTypes.length];
		if (null != args) {
			for (int i = 0; i < actualArgs.length; i++) {
				if (i >= args.length || null == args[i]) {
					// 越界或者空值
					actualArgs[i] = ClassUtil.getDefaultValue(parameterTypes[i]);
				} else if (args[i] instanceof NullWrapperBean) {
					//如果是通过NullWrapperBean传递的null参数,直接赋值null
					actualArgs[i] = null;
				} else if (!parameterTypes[i].isAssignableFrom(args[i].getClass())) {
					//对于类型不同的字段，尝试转换，转换失败则使用原对象类型
					final Object targetValue = Convert.convert(parameterTypes[i], args[i], args[i]);
					if (null != targetValue) {
						actualArgs[i] = targetValue;
					}
				} else {
					actualArgs[i] = args[i];
				}
			}
		}

		return actualArgs;
	}
}
