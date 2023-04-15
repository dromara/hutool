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

import org.dromara.hutool.core.classloader.ClassLoaderUtil;
import org.dromara.hutool.core.exceptions.UtilException;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.map.WeakConcurrentMap;
import org.dromara.hutool.core.array.ArrayUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 反射中{@link Constructor}构造工具类，包括获取构造类和通过构造实例化对象相关工具
 *
 * @author looly
 */
public class ConstructorUtil {
	/**
	 * 构造对象缓存
	 */
	private static final WeakConcurrentMap<Class<?>, Constructor<?>[]> CONSTRUCTORS_CACHE = new WeakConcurrentMap<>();
	// --------------------------------------------------------------------------------------------------------- Constructor

	/**
	 * 查找类中的指定参数的构造方法，如果找到构造方法，会自动设置可访问为true
	 *
	 * @param <T>            对象类型
	 * @param clazz          类
	 * @param parameterTypes 参数类型，只要任何一个参数是指定参数的父类或接口或相等即可，此参数可以不传
	 * @return 构造方法，如果未找到返回null
	 */
	@SuppressWarnings("unchecked")
	public static <T> Constructor<T> getConstructor(final Class<T> clazz, final Class<?>... parameterTypes) {
		if (null == clazz) {
			return null;
		}

		final Constructor<?>[] constructors = getConstructors(clazz);
		Class<?>[] pts;
		for (final Constructor<?> constructor : constructors) {
			pts = constructor.getParameterTypes();
			if (ClassUtil.isAllAssignableFrom(pts, parameterTypes)) {
				// 构造可访问
				ReflectUtil.setAccessible(constructor);
				return (Constructor<T>) constructor;
			}
		}
		return null;
	}

	/**
	 * 获得一个类中所有构造列表
	 *
	 * @param <T>       构造的对象类型
	 * @param beanClass 类，非{@code null}
	 * @return 字段列表
	 * @throws SecurityException 安全检查异常
	 */
	@SuppressWarnings("unchecked")
	public static <T> Constructor<T>[] getConstructors(final Class<T> beanClass) throws SecurityException {
		Assert.notNull(beanClass);
		return (Constructor<T>[]) CONSTRUCTORS_CACHE.computeIfAbsent(beanClass, (key) -> getConstructorsDirectly(beanClass));
	}

	/**
	 * 获得一个类中所有构造列表，直接反射获取，无缓存
	 *
	 * @param beanClass 类
	 * @return 字段列表
	 * @throws SecurityException 安全检查异常
	 */
	public static Constructor<?>[] getConstructorsDirectly(final Class<?> beanClass) throws SecurityException {
		return beanClass.getDeclaredConstructors();
	}

	// --------------------------------------------------------------------------------------------------------- newInstance

	/**
	 * 实例化对象
	 *
	 * @param <T>   对象类型
	 * @param clazz 类名
	 * @return 对象
	 * @throws UtilException 包装各类异常
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(final String clazz) throws UtilException {
		try {
			return (T) ClassLoaderUtil.loadClass(clazz).newInstance();
		} catch (final Exception e) {
			throw new UtilException(e, "Instance class [{}] error!", clazz);
		}
	}

	/**
	 * 实例化对象
	 *
	 * @param <T>    对象类型
	 * @param clazz  类
	 * @param params 构造函数参数
	 * @return 对象
	 * @throws UtilException 包装各类异常
	 */
	public static <T> T newInstance(final Class<T> clazz, final Object... params) throws UtilException {
		if (ArrayUtil.isEmpty(params)) {
			final Constructor<T> constructor = getConstructor(clazz);
			if(null == constructor){
				throw new UtilException("No constructor for [{}]", clazz);
			}
			try {
				return constructor.newInstance();
			} catch (final Exception e) {
				throw new UtilException(e, "Instance class [{}] error!", clazz);
			}
		}

		final Class<?>[] paramTypes = ClassUtil.getClasses(params);
		final Constructor<T> constructor = getConstructor(clazz, paramTypes);
		if (null == constructor) {
			throw new UtilException("No Constructor matched for parameter types: [{}]", new Object[]{paramTypes});
		}
		try {
			return constructor.newInstance(params);
		} catch (final Exception e) {
			throw new UtilException(e, "Instance class [{}] error!", clazz);
		}
	}

	/**
	 * 尝试遍历并调用此类的所有构造方法，直到构造成功并返回
	 * <p>
	 * 对于某些特殊的接口，按照其默认实现实例化，例如：
	 * <pre>
	 *     Map       -》 HashMap
	 *     Collction -》 ArrayList
	 *     List      -》 ArrayList
	 *     Set       -》 HashSet
	 * </pre>
	 *
	 * @param <T>  对象类型
	 * @param type 被构造的类
	 * @return 构造后的对象，构造失败返回{@code null}
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstanceIfPossible(Class<T> type) {
		Assert.notNull(type);

		// 原始类型
		if (type.isPrimitive()) {
			return (T) ClassUtil.getPrimitiveDefaultValue(type);
		}

		// 某些特殊接口的实例化按照默认实现进行
		if (type.isAssignableFrom(AbstractMap.class)) {
			type = (Class<T>) HashMap.class;
		} else if (type.isAssignableFrom(List.class)) {
			type = (Class<T>) ArrayList.class;
		} else if (type.isAssignableFrom(Set.class)) {
			type = (Class<T>) HashSet.class;
		}

		try {
			return newInstance(type);
		} catch (final Exception e) {
			// ignore
			// 默认构造不存在的情况下查找其它构造
		}

		// 枚举
		if (type.isEnum()) {
			return type.getEnumConstants()[0];
		}

		// 数组
		if (type.isArray()) {
			return (T) Array.newInstance(type.getComponentType(), 0);
		}

		final Constructor<T>[] constructors = getConstructors(type);
		Class<?>[] parameterTypes;
		for (final Constructor<T> constructor : constructors) {
			parameterTypes = constructor.getParameterTypes();
			if (0 == parameterTypes.length) {
				continue;
			}
			ReflectUtil.setAccessible(constructor);
			try {
				return constructor.newInstance(ClassUtil.getDefaultValues(parameterTypes));
			} catch (final Exception ignore) {
				// 构造出错时继续尝试下一种构造方式
			}
		}
		return null;
	}
}
