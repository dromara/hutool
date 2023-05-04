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

package org.dromara.hutool.core.reflect.kotlin;

import org.dromara.hutool.core.bean.copier.ValueProvider;
import org.dromara.hutool.core.bean.copier.provider.MapValueProvider;
import org.dromara.hutool.core.lang.Opt;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

/**
 * Kotlin反射包装相关工具类
 *
 * @author VampireAchao, Looly
 */
public class KClassUtil {

	@SuppressWarnings("unchecked")
	private static final Class<? extends Annotation> META_DATA_CLASS =
		(Class<? extends Annotation>) Opt.ofTry(() -> Class.forName("kotlin.Metadata")).get();

	/**
	 * 是否提供或处于Kotlin环境中
	 */
	public static final boolean IS_KOTLIN_ENABLE = null != META_DATA_CLASS;

	/**
	 * 检查给定的类是否为Kotlin类<br>
	 * Kotlin类带有@kotlin.Metadata注解
	 *
	 * @param clazz 类
	 * @return 是否Kotlin类
	 */
	public static boolean isKotlinClass(final Class<?> clazz) {
		return IS_KOTLIN_ENABLE && clazz.isAnnotationPresent(META_DATA_CLASS);
	}

	/**
	 * 获取Kotlin类的所有构造方法
	 *
	 * @param targetType kotlin类
	 * @return 构造列表
	 */
	public static List<?> getConstructors(final Class<?> targetType) {
		return KClassImpl.getConstructors(targetType);
	}

	/**
	 * 获取参数列表
	 *
	 * @param kCallable kotlin的类、方法或构造
	 * @return 参数列表
	 */
	public static List<KParameter> getParameters(final Object kCallable) {
		return KCallable.getParameters(kCallable);
	}

	/**
	 * 从{@link ValueProvider}中提取对应name的参数列表
	 *
	 * @param kCallable     kotlin的类、方法或构造
	 * @param valueProvider {@link ValueProvider}
	 * @return 参数数组
	 */
	public static Object[] getParameterValues(final Object kCallable, final ValueProvider<String> valueProvider) {
		final List<KParameter> parameters = getParameters(kCallable);
		final Object[] args = new Object[parameters.size()];
		KParameter kParameter;
		for (int i = 0; i < parameters.size(); i++) {
			kParameter = parameters.get(i);
			args[i] = valueProvider.value(kParameter.getName(), kParameter.getType());
		}
		return args;
	}

	/**
	 * 实例化Kotlin对象
	 *
	 * @param <T>        对象类型
	 * @param targetType 对象类型
	 * @param map        参数名和参数值的Map
	 * @return 对象
	 */
	public static <T> T newInstance(final Class<T> targetType, final Map<String, ?> map) {
		return newInstance(targetType, new MapValueProvider(map));
	}

	/**
	 * 实例化Kotlin对象
	 *
	 * @param <T>           对象类型
	 * @param targetType    对象类型
	 * @param valueProvider 值提供器，用于提供构造所需参数值
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(final Class<T> targetType, final ValueProvider<String> valueProvider) {
		final List<?> constructors = getConstructors(targetType);
		for (final Object constructor : constructors) {
			final Object[] parameterValues = getParameterValues(constructor, valueProvider);
			return (T) KCallable.call(constructor, parameterValues);
		}
		return null;
	}
}
