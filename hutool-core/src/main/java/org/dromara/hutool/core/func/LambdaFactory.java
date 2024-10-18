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

package org.dromara.hutool.core.func;

import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.core.map.reference.WeakConcurrentMap;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.core.reflect.ReflectUtil;
import org.dromara.hutool.core.reflect.lookup.LookupUtil;
import org.dromara.hutool.core.reflect.method.MethodTypeUtil;
import org.dromara.hutool.core.reflect.method.MethodUtil;

import java.lang.invoke.*;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 以类似反射的方式动态创建Lambda，在性能上有一定优势，同时避免每次调用Lambda时创建匿名内部类
 *
 * @author nasodaengineer
 */
public class LambdaFactory {

	private LambdaFactory() throws IllegalAccessException {
		throw new IllegalAccessException();
	}

	private static final Map<MutableEntry<Class<?>, Executable>, Object> CACHE = new WeakConcurrentMap<>();

	/**
	 * 构建Lambda
	 * <pre>{@code
	 * class Something {
	 *     private Long id;
	 *     private String name;
	 *     // ... 省略GetterSetter方法
	 * }
	 * Function<Something, Long> getIdFunction = LambdaFactory.buildLambda(Function.class, Something.class, "getId");
	 * BiConsumer<Something, String> setNameConsumer = LambdaFactory.buildLambda(BiConsumer.class, Something.class, "setName", String.class);
	 * }
	 * </pre>
	 *
	 * @param functionInterfaceType 接受Lambda的函数式接口类型
	 * @param declaringClass           声明方法的类的类型
	 * @param methodName            方法名称
	 * @param paramTypes            方法参数数组
	 * @param <F>                   Function类型
	 * @return 接受Lambda的函数式接口对象
	 */
	public static <F> F build(final Class<F> functionInterfaceType, final Class<?> declaringClass, final String methodName, final Class<?>... paramTypes) {
		return build(functionInterfaceType, MethodUtil.getMethod(declaringClass, methodName, paramTypes), declaringClass);
	}

	/**
	 * 根据提供的方法或构造对象，构建对应的Lambda函数<br>
	 * 调用函数相当于执行对应的方法或构造
	 *
	 * @param functionInterfaceType 接受Lambda的函数式接口类型
	 * @param executable            方法对象，支持构造器
	 * @param <F>                   Function类型
	 * @return 接受Lambda的函数式接口对象
	 */
	public static <F> F build(final Class<F> functionInterfaceType, final Executable executable) {
		return build(functionInterfaceType, executable, null);
	}

	/**
	 * 根据提供的方法或构造对象，构建对应的Lambda函数<br>
	 * 调用函数相当于执行对应的方法或构造
	 *
	 * @param <F>                   Function类型
	 * @param functionInterfaceType 接受Lambda的函数式接口类型
	 * @param executable            方法对象，支持构造器
	 * @param declaringClass        {@link Executable}声明的类，如果方法或构造定义在父类中，此处用于指定子类
	 * @return 接受Lambda的函数式接口对象
	 */
	@SuppressWarnings("unchecked")
	public static <F> F build(final Class<F> functionInterfaceType,
							  final Executable executable, final Class<?> declaringClass) {
		Assert.notNull(functionInterfaceType);
		Assert.notNull(executable);

		final MutableEntry<Class<?>, Executable> cacheKey = new MutableEntry<>(functionInterfaceType, executable);
		return (F) CACHE.computeIfAbsent(cacheKey,
			key -> doBuildWithoutCache(functionInterfaceType, executable, declaringClass));
	}

	/**
	 * 根据提供的方法或构造对象，构建对应的Lambda函数，即通过Lambda函数代理方法或构造<br>
	 * 调用函数相当于执行对应的方法或构造
	 *
	 * @param <F>            Function类型
	 * @param funcType       接受Lambda的函数式接口类型
	 * @param executable     方法对象，支持构造器
	 * @param declaringClass {@link Executable}声明的类，如果方法或构造定义在父类中，此处用于指定子类
	 * @return 接受Lambda的函数式接口对象
	 */
	@SuppressWarnings("unchecked")
	private static <F> F doBuildWithoutCache(final Class<F> funcType,
											 final Executable executable, final Class<?> declaringClass) {
		ReflectUtil.setAccessible(executable);

		// 获取Lambda函数
		final Method invokeMethod = LambdaUtil.getInvokeMethod(funcType);
		try {
			return (F) metaFactory(funcType, invokeMethod, executable, declaringClass)
				.getTarget().invoke();
		} catch (final Throwable e) {
			throw new HutoolException(e);
		}
	}

	/**
	 * 通过Lambda函数代理方法或构造
	 * <p>
	 *     TODO 在多模块项目中，使用module-info.java声明的模块项目，使用此方法获取的Lookup对象存在权限不足问题<br>
	 *     见：https://gitee.com/dromara/hutool/issues/I96JIP
	 * </p>
	 *
	 * @param funcType       函数类型
	 * @param funcMethod     函数执行的方法
	 * @param executable     被代理的方法或构造
	 * @param declaringClass {@link Executable}声明的类，如果方法或构造定义在父类中，此处用于指定子类
	 * @return {@link CallSite}
	 * @throws LambdaConversionException 权限等异常
	 */
	private static CallSite metaFactory(final Class<?> funcType, final Method funcMethod,
										final Executable executable, final Class<?> declaringClass) throws LambdaConversionException {
		// 查找上下文与调用者的访问权限
		final MethodHandles.Lookup caller = LookupUtil.lookup(executable.getDeclaringClass());
		// 要实现的方法的名字
		final String invokeName = funcMethod.getName();
		// 调用点期望的方法参数的类型和返回值的类型(方法signature)
		final MethodType invokedType = MethodType.methodType(funcType);

		final Class<?>[] paramTypes = funcMethod.getParameterTypes();
		// 函数对象将要实现的接口方法类型
		final MethodType samMethodType = MethodType.methodType(funcMethod.getReturnType(), paramTypes);
		// 一个直接方法句柄(DirectMethodHandle), 描述调用时将被执行的具体实现方法
		final MethodHandle implMethodHandle = LookupUtil.unreflect(executable);

		if (ClassUtil.isSerializable(funcType)) {
			return LambdaMetafactory.altMetafactory(
				caller,
				invokeName,
				invokedType,
				samMethodType,
				implMethodHandle,
				MethodTypeUtil.methodType(executable, declaringClass),
				LambdaMetafactory.FLAG_SERIALIZABLE
			);
		}

		return LambdaMetafactory.metafactory(
			caller,
			invokeName,
			invokedType,
			samMethodType,
			implMethodHandle,
			MethodTypeUtil.methodType(executable, declaringClass)
		);
	}
}
