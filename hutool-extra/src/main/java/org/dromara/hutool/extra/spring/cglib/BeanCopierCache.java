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

package org.dromara.hutool.extra.spring.cglib;

import org.dromara.hutool.core.map.WeakConcurrentMap;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

/**
 * BeanCopier属性缓存<br>
 * 缓存用于防止多次反射造成的性能问题
 *
 * @author Looly
 * @since 5.4.1
 */
public enum BeanCopierCache {
	/**
	 * BeanCopier属性缓存单例
	 */
	INSTANCE;

	private final WeakConcurrentMap<String, BeanCopier> cache = new WeakConcurrentMap<>();

	/**
	 * 获得类与转换器生成的key在{@link BeanCopier}的Map中对应的元素
	 *
	 * @param srcClass    源Bean的类
	 * @param targetClass 目标Bean的类
	 * @param converter   转换器
	 * @return Map中对应的BeanCopier
	 */
	public BeanCopier get(final Class<?> srcClass, final Class<?> targetClass, final Converter converter) {
		return get(srcClass, targetClass, null != converter);
	}

	/**
	 * 获得类与转换器生成的key在{@link BeanCopier}的Map中对应的元素
	 *
	 * @param srcClass     源Bean的类
	 * @param targetClass  目标Bean的类
	 * @param useConverter 是否使用转换器
	 * @return Map中对应的BeanCopier
	 * @since 5.8.0
	 */
	public BeanCopier get(final Class<?> srcClass, final Class<?> targetClass, final boolean useConverter) {
		final String key = genKey(srcClass, targetClass, useConverter);
		return cache.computeIfAbsent(key, (k) -> BeanCopier.create(srcClass, targetClass, useConverter));
	}

	/**
	 * 获得类与转换器生成的key<br>
	 * 结构类似于：srcClassName#targetClassName#1 或者 srcClassName#targetClassName#0
	 *
	 * @param srcClass     源Bean的类
	 * @param targetClass  目标Bean的类
	 * @param useConverter 是否使用转换器
	 * @return 属性名和Map映射的key
	 */
	private String genKey(final Class<?> srcClass, final Class<?> targetClass, final boolean useConverter) {
		final StringBuilder key = StrUtil.builder()
				.append(srcClass.getName())
				.append('#').append(targetClass.getName())
				.append('#').append(useConverter ? 1 : 0);
		return key.toString();
	}
}
