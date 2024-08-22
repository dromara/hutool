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

import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.core.reflect.Invoker;
import org.dromara.hutool.core.reflect.ModifierUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 方法调用器，通过反射调用方法。
 *
 * @author Looly
 */
public class MethodInvoker implements Invoker {

	/**
	 * 创建方法调用器
	 *
	 * @param method 方法
	 * @return 方法调用器
	 */
	public static MethodInvoker of(final Method method) {
		return new MethodInvoker(method);
	}

	private final Method method;
	private final Class<?>[] paramTypes;
	private final Class<?> type;
	private boolean checkArgs;

	/**
	 * 构造
	 *
	 * @param method 方法
	 */
	public MethodInvoker(final Method method) {
		this.method = method;

		this.paramTypes = method.getParameterTypes();
		if (paramTypes.length == 1) {
			// setter方法读取参数类型
			type = paramTypes[0];
		} else {
			type = method.getReturnType();
		}
	}

	/**
	 * 设置是否检查参数<br>
	 * <pre>
	 * 1. 参数个数是否与方法参数个数一致
	 * 2. 如果某个参数为null但是方法这个位置的参数为原始类型，则赋予原始类型默认值
	 * </pre>
	 *
	 * @param checkArgs 是否检查参数
	 * @return this
	 */
	public MethodInvoker setCheckArgs(final boolean checkArgs) {
		this.checkArgs = checkArgs;
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T invoke(Object target, final Object... args) throws HutoolException{
		if(this.checkArgs){
			checkArgs(args);
		}

		final Method method = this.method;
		// static方法调用则target为null
		if(ModifierUtil.isStatic(method)){
			target = null;
		}
		// 根据方法定义的参数类型，将用户传入的参数规整和转换
		final Object[] actualArgs = MethodUtil.actualArgs(method, args);
		try {
			return MethodHandleUtil.invokeExact(target, method, actualArgs);
		} catch (final Exception e) {
			// 传统反射方式执行方法
			try {
				return (T) method.invoke(target, actualArgs);
			} catch (final IllegalAccessException | InvocationTargetException ex) {
				throw new HutoolException(ex);
			}
		}
	}

	/**
	 * 执行静态方法
	 *
	 * @param <T>    对象类型
	 * @param args   参数对象
	 * @return 结果
	 * @throws HutoolException 多种异常包装
	 */
	public <T> T invokeStatic(final Object... args) throws HutoolException {
		return invoke(null, args);
	}

	@Override
	public Class<?> getType() {
		return this.type;
	}

	/**
	 * 检查传入参数的有效性。
	 *
	 * @param args 传入的参数数组，不能为空。
	 * @throws IllegalArgumentException 如果参数数组为空或长度为0，则抛出此异常。
	 */
	private void checkArgs(final Object[] args) {
		final Class<?>[] paramTypes = this.paramTypes;
		if (null != args) {
			Assert.isTrue(args.length == paramTypes.length, "Params length [{}] is not fit for param length [{}] of method !", args.length, paramTypes.length);
			Class<?> type;
			for (int i = 0; i < args.length; i++) {
				type = paramTypes[i];
				if (type.isPrimitive() && null == args[i]) {
					// 参数是原始类型，而传入参数为null时赋予默认值
					args[i] = ClassUtil.getDefaultValue(type);
				}
			}
		}
	}
}
