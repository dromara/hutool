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

package org.dromara.hutool.cglib;

import org.dromara.hutool.lang.Assert;
import org.dromara.hutool.reflect.ConstructorUtil;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanMap;
import net.sf.cglib.core.Converter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Cglib工具类
 *
 * @author looly
 * @since 5.4.1
 */
public class CglibUtil {

	/**
	 * 拷贝Bean对象属性到目标类型<br>
	 * 此方法通过指定目标类型自动创建之，然后拷贝属性
	 *
	 * @param <T>         目标对象类型
	 * @param source      源bean对象
	 * @param targetClass 目标bean类，自动实例化此对象
	 * @return 目标对象
	 */
	public static <T> T copy(final Object source, final Class<T> targetClass) {
		return copy(source, targetClass, null);
	}

	/**
	 * 拷贝Bean对象属性<br>
	 * 此方法通过指定目标类型自动创建之，然后拷贝属性
	 *
	 * @param <T>         目标对象类型
	 * @param source      源bean对象
	 * @param targetClass 目标bean类，自动实例化此对象
	 * @param converter   转换器，无需可传{@code null}
	 * @return 目标对象
	 */
	public static <T> T copy(final Object source, final Class<T> targetClass, final Converter converter) {
		final T target = ConstructorUtil.newInstanceIfPossible(targetClass);
		copy(source, target, converter);
		return target;
	}

	/**
	 * 拷贝Bean对象属性
	 *
	 * @param source 源bean对象
	 * @param target 目标bean对象
	 */
	public static void copy(final Object source, final Object target) {
		copy(source, target, null);
	}

	/**
	 * 拷贝Bean对象属性
	 *
	 * @param source    源bean对象
	 * @param target    目标bean对象
	 * @param converter 转换器，无需可传{@code null}
	 */
	public static void copy(final Object source, final Object target, final Converter converter) {
		Assert.notNull(source, "Source bean must be not null.");
		Assert.notNull(target, "Target bean must be not null.");

		final Class<?> sourceClass = source.getClass();
		final Class<?> targetClass = target.getClass();
		final BeanCopier beanCopier = BeanCopierCache.INSTANCE.get(sourceClass, targetClass, converter);

		beanCopier.copy(source, target, converter);
	}

	/**
	 * 拷贝List Bean对象属性
	 *
	 * @param <S>    源bean类型
	 * @param <T>    目标bean类型
	 * @param source 源bean对象list
	 * @param target 目标bean对象
	 * @return 目标bean对象list
	 */
	public static <S, T> List<T> copyList(final Collection<S> source, final Supplier<T> target) {
		return copyList(source, target, null, null);
	}

	/**
	 * 拷贝List Bean对象属性
	 *
	 * @param source    源bean对象list
	 * @param target    目标bean对象
	 * @param converter 转换器，无需可传{@code null}
	 * @param <S>       源bean类型
	 * @param <T>       目标bean类型
	 * @return 目标bean对象list
	 * @since 5.4.1
	 */
	public static <S, T> List<T> copyList(final Collection<S> source, final Supplier<T> target, final Converter converter) {
		return copyList(source, target, converter, null);
	}

	/**
	 * 拷贝List Bean对象属性
	 *
	 * @param source   源bean对象list
	 * @param target   目标bean对象
	 * @param callback 回调对象
	 * @param <S>      源bean类型
	 * @param <T>      目标bean类型
	 * @return 目标bean对象list
	 * @since 5.4.1
	 */
	public static <S, T> List<T> copyList(final Collection<S> source, final Supplier<T> target, final BiConsumer<S, T> callback) {
		return copyList(source, target, null, callback);
	}

	/**
	 * 拷贝List Bean对象属性
	 *
	 * @param source    源bean对象list
	 * @param target    目标bean对象
	 * @param converter 转换器，无需可传{@code null}
	 * @param callback  回调对象
	 * @param <S>       源bean类型
	 * @param <T>       目标bean类型
	 * @return 目标bean对象list
	 */
	public static <S, T> List<T> copyList(final Collection<S> source, final Supplier<T> target, final Converter converter, final BiConsumer<S, T> callback) {
		return source.stream().map(s -> {
			final T t = target.get();
			copy(s, t, converter);
			if (callback != null) {
				callback.accept(s, t);
			}
			return t;
		}).collect(Collectors.toList());
	}

	/**
	 * 将Bean转换为Map
	 *
	 * @param bean Bean对象
	 * @return {@link BeanMap}
	 * @since 5.4.1
	 */
	public static BeanMap toMap(final Object bean) {
		return BeanMap.create(bean);
	}

	/**
	 * 将Map中的内容填充至Bean中
	 * @param map Map
	 * @param bean Bean
	 * @param <T> Bean类型
	 * @return bean
	 * @since 5.6.3
	 */
	@SuppressWarnings("rawtypes")
	public static <T> T fillBean(final Map map, final T bean){
		BeanMap.create(bean).putAll(map);
		return bean;
	}

	/**
	 * 将Map转换为Bean
	 * @param map Map
	 * @param beanClass Bean类
	 * @param <T> Bean类型
	 * @return bean
	 * @since 5.6.3
	 */
	@SuppressWarnings("rawtypes")
	public static <T> T toBean(final Map map, final Class<T> beanClass){
		return fillBean(map, ConstructorUtil.newInstanceIfPossible(beanClass));
	}
}
