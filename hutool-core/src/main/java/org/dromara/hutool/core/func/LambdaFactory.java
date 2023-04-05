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

package org.dromara.hutool.core.func;

import org.dromara.hutool.core.exceptions.UtilException;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.core.map.WeakConcurrentMap;
import org.dromara.hutool.core.reflect.MethodUtil;
import org.dromara.hutool.core.reflect.ModifierUtil;
import org.dromara.hutool.core.reflect.ReflectUtil;
import org.dromara.hutool.core.reflect.lookup.LookupUtil;

import java.io.Serializable;
import java.lang.invoke.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.invoke.LambdaMetafactory.FLAG_SERIALIZABLE;
import static java.lang.invoke.MethodType.methodType;

/**
 * 以类似反射的方式动态创建Lambda，在性能上有一定优势，同时避免每次调用Lambda时创建匿名内部类
 * TODO JDK9+存在兼容问题
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
	 * @param methodClass           声明方法的类的类型
	 * @param methodName            方法名称
	 * @param paramTypes            方法参数数组
	 * @return 接受Lambda的函数式接口对象
	 * @param <F> Function类型
	 */
	public static <F> F build(final Class<F> functionInterfaceType, final Class<?> methodClass, final String methodName, final Class<?>... paramTypes) {
		return build(functionInterfaceType, MethodUtil.getMethod(methodClass, methodName, paramTypes));
	}

	/**
	 * 根据提供的方法或构造对象，构建对应的Lambda函数<br>
	 * 调用函数相当于执行对应的方法或构造
	 *
	 * @param functionInterfaceType 接受Lambda的函数式接口类型
	 * @param executable                方法对象，支持构造器
	 * @param <F>                   Function类型
	 * @return 接受Lambda的函数式接口对象
	 */
	@SuppressWarnings("unchecked")
	public static <F> F build(final Class<F> functionInterfaceType, final Executable executable) {
		Assert.notNull(functionInterfaceType);
		Assert.notNull(executable);
		final MutableEntry<Class<?>, Executable> cacheKey = new MutableEntry<>(functionInterfaceType, executable);
		return (F) CACHE.computeIfAbsent(cacheKey, key -> {
			final List<Method> abstractMethods = Arrays.stream(functionInterfaceType.getMethods())
					.filter(ModifierUtil::isAbstract)
					.collect(Collectors.toList());
			Assert.equals(abstractMethods.size(), 1, "不支持非函数式接口");
			ReflectUtil.setAccessible(executable);

			final MethodHandle methodHandle = LookupUtil.unreflect(executable);
			final MethodType instantiatedMethodType;
			if (executable instanceof Method) {
				final Method method = (Method) executable;
				instantiatedMethodType = MethodType.methodType(method.getReturnType(), method.getDeclaringClass(), method.getParameterTypes());
			} else {
				final Constructor<?> constructor = (Constructor<?>) executable;
				instantiatedMethodType = MethodType.methodType(constructor.getDeclaringClass(), constructor.getParameterTypes());
			}
			final boolean isSerializable = Serializable.class.isAssignableFrom(functionInterfaceType);

			final Method invokeMethod = abstractMethods.get(0);
			final MethodHandles.Lookup caller = LookupUtil.lookup(executable.getDeclaringClass());
			final String invokeName = invokeMethod.getName();
			final MethodType invokedType = methodType(functionInterfaceType);
			final MethodType samMethodType = methodType(invokeMethod.getReturnType(), invokeMethod.getParameterTypes());
			try {
				final CallSite callSite = isSerializable ?
						LambdaMetafactory.altMetafactory(
								caller,
								invokeName,
								invokedType,
								samMethodType,
								methodHandle,
								instantiatedMethodType,
								FLAG_SERIALIZABLE
						) :
						LambdaMetafactory.metafactory(
								caller,
								invokeName,
								invokedType,
								samMethodType,
								methodHandle,
								instantiatedMethodType
						);
				//noinspection unchecked
				return (F) callSite.getTarget().invoke();
			} catch (final Throwable e) {
				throw new UtilException(e);
			}
		});

	}
}
