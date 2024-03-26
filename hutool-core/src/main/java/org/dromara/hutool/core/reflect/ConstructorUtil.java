/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.reflect;

import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.map.reference.WeakConcurrentMap;
import org.dromara.hutool.core.reflect.creator.DefaultObjectCreator;
import org.dromara.hutool.core.reflect.creator.PossibleObjectCreator;

import java.lang.reflect.Constructor;

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
	 * 实例化对象<br>
	 * 类必须有空构造函数
	 *
	 * @param <T>   对象类型
	 * @param clazz 类名
	 * @return 对象
	 * @throws HutoolException 包装各类异常
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(final String clazz) throws HutoolException {
		return (T) DefaultObjectCreator.of(clazz).create();
	}

	/**
	 * 实例化对象
	 *
	 * @param <T>    对象类型
	 * @param clazz  类
	 * @param params 构造函数参数
	 * @return 对象
	 * @throws HutoolException 包装各类异常
	 */
	public static <T> T newInstance(final Class<T> clazz, final Object... params) throws HutoolException {
		return DefaultObjectCreator.of(clazz, params).create();
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
	public static <T> T newInstanceIfPossible(final Class<T> type) {
		return PossibleObjectCreator.of(type).create();
	}
}
