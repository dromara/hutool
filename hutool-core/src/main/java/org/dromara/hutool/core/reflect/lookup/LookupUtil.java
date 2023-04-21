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

import org.dromara.hutool.core.exceptions.HutoolException;
import org.dromara.hutool.core.lang.caller.CallerUtil;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.reflect.ModifierUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.JdkUtil;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

/**
 * {@link MethodHandles.Lookup}工具<br>
 * {@link MethodHandles.Lookup}是一个方法句柄查找对象，用于在指定类中查找符合给定方法名称、方法类型的方法句柄。
 *
 * <p>
 * jdk8中如果直接调用{@link MethodHandles#lookup()}获取到的{@link MethodHandles.Lookup}在调用findSpecial和unreflectSpecial
 * 时会出现权限不够问题，抛出"no private access for invokespecial"异常，因此针对JDK8及JDK9+分别封装lookup方法。
 * </p>
 *
 * <p>
 * 参考：https://blog.csdn.net/u013202238/article/details/108687086
 *
 * @author looly
 * @since 6.0.0
 */
public class LookupUtil {

	private static final LookupFactory factory;

	static {
		if (JdkUtil.IS_JDK8) {
			factory = new ConstructorLookupFactory();
		} else {
			factory = new MethodLookupFactory();
		}
	}

	// region ----- lookup

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
		return factory.lookup(callerClass);
	}
	// endregion

	/**
	 * 将{@link Method}或者{@link Constructor} 包装为方法句柄{@link MethodHandle}
	 *
	 * @param methodOrConstructor {@link Method}或者{@link Constructor}
	 * @return 方法句柄{@link MethodHandle}
	 * @throws HutoolException {@link IllegalAccessException} 包装
	 */
	public static MethodHandle unreflect(final Member methodOrConstructor) throws HutoolException {
		try {
			if (methodOrConstructor instanceof Method) {
				return unreflectMethod((Method) methodOrConstructor);
			} else {
				return lookup().unreflectConstructor((Constructor<?>) methodOrConstructor);
			}
		} catch (final IllegalAccessException e) {
			throw new HutoolException(e);
		}
	}

	/**
	 * 将{@link Method} 转换为方法句柄{@link MethodHandle}
	 *
	 * @param method {@link Method}
	 * @return {@link MethodHandles}
	 * @throws IllegalAccessException 无权访问
	 */
	public static MethodHandle unreflectMethod(final Method method) throws IllegalAccessException {
		final Class<?> caller = method.getDeclaringClass();
		final MethodHandles.Lookup lookup = lookup(caller);
		if (ModifierUtil.isDefault(method)) {
			// 当方法是default方法时，尤其对象是代理对象，需使用句柄方式执行
			// 代理对象情况下调用method.invoke会导致循环引用执行，最终栈溢出
			return lookup.unreflectSpecial(method, caller);
		}

		try {
			return lookup.unreflect(method);
		} catch (final Exception ignore) {
			// 某些情况下，无权限执行方法则尝试执行特殊方法
			return lookup.unreflectSpecial(method, caller);
		}
	}

	// region ----- findMethod

	/**
	 * 查找指定方法的方法句柄<br>
	 * 此方法只会查找：
	 * <ul>
	 *     <li>当前类的方法（包括构造方法和private方法）</li>
	 *     <li>父类的方法（包括构造方法和private方法）</li>
	 *     <li>当前类的static方法</li>
	 * </ul>
	 *
	 * @param callerClass 方法所在类或接口
	 * @param name        方法名称，{@code null}或者空则查找构造方法
	 * @param returnType  返回值类型
	 * @param argTypes    返回类型和参数类型列表
	 * @return 方法句柄 {@link MethodHandle}，{@code null}表示未找到方法
	 */
	public static MethodHandle findMethod(final Class<?> callerClass, final String name,
										  final Class<?> returnType, final Class<?>... argTypes) {
		return findMethod(callerClass, name, MethodType.methodType(returnType, argTypes));
	}

	/**
	 * 查找指定方法的方法句柄<br>
	 * 此方法只会查找：
	 * <ul>
	 *     <li>当前类的方法（包括构造方法和private方法）</li>
	 *     <li>父类的方法（包括构造方法和private方法）</li>
	 *     <li>当前类的static方法</li>
	 * </ul>
	 *
	 * @param callerClass 方法所在类或接口
	 * @param name        方法名称，{@code null}或者空则查找构造方法
	 * @param type        返回类型和参数类型，可以使用{@code MethodType#methodType}构建
	 * @return 方法句柄 {@link MethodHandle}，{@code null}表示未找到方法
	 */
	public static MethodHandle findMethod(final Class<?> callerClass, final String name, final MethodType type) {
		if (StrUtil.isBlank(name)) {
			return findConstructor(callerClass, type);
		}

		MethodHandle handle = null;
		final MethodHandles.Lookup lookup = LookupUtil.lookup(callerClass);
		try {
			handle = lookup.findVirtual(callerClass, name, type);
		} catch (final IllegalAccessException | NoSuchMethodException ignore) {
			//ignore
		}

		// static方法
		if (null == handle) {
			try {
				handle = lookup.findStatic(callerClass, name, type);
			} catch (final IllegalAccessException | NoSuchMethodException ignore) {
				//ignore
			}
		}

		// 特殊方法，包括构造方法、私有方法等
		if (null == handle) {
			try {
				handle = lookup.findSpecial(callerClass, name, type, callerClass);
			} catch (final NoSuchMethodException ignore) {
				//ignore
			} catch (final IllegalAccessException e) {
				throw new HutoolException(e);
			}
		}

		return handle;
	}
	// endregion

	// region ----- findConstructor

	/**
	 * 查找指定的构造方法
	 *
	 * @param callerClass 类
	 * @param argTypes    参数类型列表
	 * @return 构造方法句柄
	 */
	public static MethodHandle findConstructor(final Class<?> callerClass, final Class<?>... argTypes) {
		final Constructor<?> constructor = ConstructorUtil.getConstructor(callerClass, argTypes);
		if(null != constructor){
			return LookupUtil.unreflect(constructor);
		}
		return null;
	}

	/**
	 * 查找指定的构造方法，给定的参数类型必须完全匹配，不能有拆装箱或继承关系等/
	 *
	 * @param callerClass 类
	 * @param argTypes    参数类型列表，完全匹配
	 * @return 构造方法句柄
	 */
	public static MethodHandle findConstructorExact(final Class<?> callerClass, final Class<?>... argTypes) {
		return findConstructor(callerClass, MethodType.methodType(void.class, argTypes));
	}

	/**
	 * 查找指定的构造方法
	 *
	 * @param callerClass 类
	 * @param type        参数类型，此处返回类型应为void.class
	 * @return 构造方法句柄
	 */
	public static MethodHandle findConstructor(final Class<?> callerClass, final MethodType type) {
		final MethodHandles.Lookup lookup = lookup(callerClass);
		try {
			return lookup.findConstructor(callerClass, type);
		} catch (final NoSuchMethodException e) {
			return null;
		} catch (final IllegalAccessException e) {
			throw new HutoolException(e);
		}
	}
	// endregion
}
