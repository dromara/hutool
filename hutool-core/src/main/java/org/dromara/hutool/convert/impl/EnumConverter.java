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

package org.dromara.hutool.convert.impl;

import org.dromara.hutool.convert.AbstractConverter;
import org.dromara.hutool.convert.ConvertException;
import org.dromara.hutool.lang.EnumItem;
import org.dromara.hutool.map.MapUtil;
import org.dromara.hutool.map.WeakConcurrentMap;
import org.dromara.hutool.reflect.ClassUtil;
import org.dromara.hutool.reflect.MethodUtil;
import org.dromara.hutool.reflect.ModifierUtil;
import org.dromara.hutool.util.EnumUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 无泛型检查的枚举转换器
 *
 * @author Looly
 * @since 4.0.2
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class EnumConverter extends AbstractConverter {
	private static final long serialVersionUID = 1L;

	public static final EnumConverter INSTANCE = new EnumConverter();

	private static final WeakConcurrentMap<Class<?>, Map<Class<?>, Method>> VALUE_OF_METHOD_CACHE = new WeakConcurrentMap<>();

	@Override
	protected Object convertInternal(final Class<?> targetClass, final Object value) {
		Enum enumValue = tryConvertEnum(value, targetClass);
		if (null == enumValue && false == value instanceof String) {
			// 最后尝试先将value转String，再valueOf转换
			enumValue = Enum.valueOf((Class) targetClass, convertToStr(value));
		}

		if (null != enumValue) {
			return enumValue;
		}

		throw new ConvertException("Can not convert {} to {}", value, targetClass);
	}

	/**
	 * 尝试转换，转换规则为：
	 * <ul>
	 *     <li>如果实现{@link EnumItem}接口，则调用fromInt或fromStr转换</li>
	 *     <li>找到类似转换的静态方法调用实现转换且优先使用</li>
	 *     <li>约定枚举类应该提供 valueOf(String) 和 valueOf(Integer)用于转换</li>
	 *     <li>oriInt /name 转换托底</li>
	 * </ul>
	 *
	 * @param value     被转换的值
	 * @param enumClass enum类
	 * @return 对应的枚举值
	 */
	protected static Enum tryConvertEnum(final Object value, final Class enumClass) {
		if (value == null) {
			return null;
		}

		// EnumItem实现转换
		if (EnumItem.class.isAssignableFrom(enumClass)) {
			final EnumItem first = (EnumItem) EnumUtil.getEnumAt(enumClass, 0);
			if (null != first) {
				if (value instanceof Integer) {
					return (Enum) first.fromInt((Integer) value);
				} else if (value instanceof String) {
					return (Enum) first.fromStr(value.toString());
				}
			}
		}

		// 用户自定义方法
		// 查找枚举中所有返回值为目标枚举对象的方法，如果发现方法参数匹配，就执行之
		try {
			final Map<Class<?>, Method> methodMap = getMethodMap(enumClass);
			if (MapUtil.isNotEmpty(methodMap)) {
				final Class<?> valueClass = value.getClass();
				for (final Map.Entry<Class<?>, Method> entry : methodMap.entrySet()) {
					if (ClassUtil.isAssignable(entry.getKey(), valueClass)) {
						return MethodUtil.invokeStatic(entry.getValue(), value);
					}
				}
			}
		} catch (final Exception ignore) {
			//ignore
		}

		//oriInt 应该滞后使用 以 GB/T 2261.1-2003 性别编码为例，对应整数并非连续数字会导致数字转枚举时失败
		//0 - 未知的性别
		//1 - 男性
		//2 - 女性
		//5 - 女性改(变)为男性
		//6 - 男性改(变)为女性
		//9 - 未说明的性别
		Enum enumResult = null;
		if (value instanceof Integer) {
			enumResult = EnumUtil.getEnumAt(enumClass, (Integer) value);
		} else if (value instanceof String) {
			try {
				enumResult = Enum.valueOf(enumClass, (String) value);
			} catch (final IllegalArgumentException e) {
				//ignore
			}
		}

		return enumResult;
	}

	/**
	 * 获取用于转换为enum的所有static方法
	 *
	 * @param enumClass 枚举类
	 * @return 转换方法map，key为方法参数类型，value为方法
	 */
	private static Map<Class<?>, Method> getMethodMap(final Class<?> enumClass) {
		return VALUE_OF_METHOD_CACHE.computeIfAbsent(enumClass, (key) -> Arrays.stream(enumClass.getMethods())
				.filter(ModifierUtil::isStatic)
				.filter(m -> m.getReturnType() == enumClass)
				.filter(m -> m.getParameterCount() == 1)
				.filter(m -> false == "valueOf".equals(m.getName()))
				.collect(Collectors.toMap(m -> m.getParameterTypes()[0], m -> m, (k1, k2) -> k1)));
	}
}
