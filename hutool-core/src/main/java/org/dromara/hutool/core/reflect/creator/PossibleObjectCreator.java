/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.reflect.creator;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.reflect.ReflectUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * 尝试方式对象实例化器<br>
 * 通过判断类型或调用可能的构造，构建对象，支持：
 * <ul>
 *     <li>原始类型</li>
 *     <li>接口或抽象类型</li>
 *     <li>枚举</li>
 *     <li>数组</li>
 *     <li>使用默认参数的构造方法</li>
 * </ul>
 * <p>
 * 对于接口或抽象类型，构造其默认实现：
 * <pre>
 *     Map       -》 HashMap
 *     Collction -》 ArrayList
 *     List      -》 ArrayList
 *     Set       -》 HashSet
 * </pre>
 *
 * @param <T> 对象类型
 */
public class PossibleObjectCreator<T> implements ObjectCreator<T> {

	/**
	 * 创建默认的对象实例化器
	 *
	 * @param clazz 实例化的类
	 * @param <T>   对象类型
	 * @return DefaultObjectCreator
	 */
	public static <T> PossibleObjectCreator<T> of(final Class<T> clazz) {
		return new PossibleObjectCreator<>(clazz);
	}

	final Class<T> clazz;

	/**
	 * 构造
	 *
	 * @param clazz 实例化的类
	 */
	public PossibleObjectCreator(final Class<T> clazz) {
		this.clazz = Assert.notNull(clazz);
	}

	@Override
	@SuppressWarnings("unchecked")
	public T create() {
		Class<T> type = this.clazz;

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
			return DefaultObjectCreator.of(type).create();
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

		final Constructor<T>[] constructors = ConstructorUtil.getConstructors(type);
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
