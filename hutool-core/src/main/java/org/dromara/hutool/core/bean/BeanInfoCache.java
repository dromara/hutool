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

package org.dromara.hutool.core.bean;

import org.dromara.hutool.core.func.SerSupplier;
import org.dromara.hutool.core.map.ReferenceConcurrentMap;
import org.dromara.hutool.core.map.WeakConcurrentMap;

import java.beans.PropertyDescriptor;
import java.util.Map;

/**
 * Bean属性缓存<br>
 * 缓存用于防止多次反射造成的性能问题
 *
 * @author Looly
 */
public enum BeanInfoCache {
	/**
	 * 单例
	 */
	INSTANCE;

	private final WeakConcurrentMap<Class<?>, Map<String, PropertyDescriptor>> pdCache = new WeakConcurrentMap<>();
	private final WeakConcurrentMap<Class<?>, Map<String, PropertyDescriptor>> ignoreCasePdCache = new WeakConcurrentMap<>();

	/**
	 * 获得属性名和{@link PropertyDescriptor}Map映射
	 *
	 * @param beanClass  Bean的类
	 * @param ignoreCase 是否忽略大小写
	 * @return 属性名和{@link PropertyDescriptor}Map映射
	 */
	public Map<String, PropertyDescriptor> getPropertyDescriptorMap(final Class<?> beanClass, final boolean ignoreCase) {
		return getCache(ignoreCase).get(beanClass);
	}

	/**
	 * 获得属性名和{@link PropertyDescriptor}Map映射
	 *
	 * @param beanClass  Bean的类
	 * @param ignoreCase 是否忽略大小写
	 * @param supplier   缓存对象产生函数
	 * @return 属性名和{@link PropertyDescriptor}Map映射
	 * @since 5.4.1
	 */
	public Map<String, PropertyDescriptor> getPropertyDescriptorMap(
			final Class<?> beanClass,
			final boolean ignoreCase,
			final SerSupplier<Map<String, PropertyDescriptor>> supplier) {
		return getCache(ignoreCase).computeIfAbsent(beanClass, (key) -> supplier.get());
	}

	/**
	 * 加入缓存
	 *
	 * @param beanClass                      Bean的类
	 * @param fieldNamePropertyDescriptorMap 属性名和{@link PropertyDescriptor}Map映射
	 * @param ignoreCase                     是否忽略大小写
	 */
	public void putPropertyDescriptorMap(final Class<?> beanClass, final Map<String, PropertyDescriptor> fieldNamePropertyDescriptorMap, final boolean ignoreCase) {
		getCache(ignoreCase).put(beanClass, fieldNamePropertyDescriptorMap);
	}

	/**
	 * 清空缓存
	 *
	 * @since 5.7.21
	 */
	public void clear() {
		this.pdCache.clear();
		this.ignoreCasePdCache.clear();
	}

	/**
	 * 根据是否忽略字段名的大小写，返回不用Cache对象
	 *
	 * @param ignoreCase 是否忽略大小写
	 * @return {@link ReferenceConcurrentMap}
	 * @since 5.4.1
	 */
	private ReferenceConcurrentMap<Class<?>, Map<String, PropertyDescriptor>> getCache(final boolean ignoreCase) {
		return ignoreCase ? ignoreCasePdCache : pdCache;
	}
}
