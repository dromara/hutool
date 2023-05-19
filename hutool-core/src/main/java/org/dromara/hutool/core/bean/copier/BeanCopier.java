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

package org.dromara.hutool.core.bean.copier;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.copier.Copier;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Bean拷贝，提供：
 *
 * <pre>
 *     1. Bean 转 Bean
 *     2. Bean 转 Map
 *     3. Map  转 Bean
 *     4. Map  转 Map
 * </pre>
 *
 * @param <T> 目标对象类型
 * @author looly
 * @since 3.2.3
 */
public class BeanCopier<T> implements Copier<T>, Serializable {
	private static final long serialVersionUID = 1L;

	private final Copier<T> copier;

	/**
	 * 创建BeanCopier
	 *
	 * @param <T>         目标Bean类型
	 * @param source      来源对象，可以是Bean或者Map
	 * @param target      目标Bean对象
	 * @param copyOptions 拷贝属性选项
	 * @return BeanCopier
	 */
	public static <T> BeanCopier<T> of(final Object source, final T target, final CopyOptions copyOptions) {
		return of(source, target, target.getClass(), copyOptions);
	}

	/**
	 * 创建BeanCopier
	 *
	 * @param <T>         目标Bean类型
	 * @param source      来源对象，可以是Bean或者Map
	 * @param target      目标Bean对象
	 * @param destType    目标的泛型类型，用于标注有泛型参数的Bean对象
	 * @param copyOptions 拷贝属性选项
	 * @return BeanCopier
	 */
	public static <T> BeanCopier<T> of(final Object source, final T target, final Type destType, final CopyOptions copyOptions) {
		return new BeanCopier<>(source, target, destType, copyOptions);
	}

	/**
	 * 构造
	 *
	 * @param source      来源对象，可以是Bean或者Map，不能为{@code null}
	 * @param target      目标Bean对象，不能为{@code null}
	 * @param targetType  目标的泛型类型，用于标注有泛型参数的Bean对象
	 * @param copyOptions 拷贝属性选项
	 */
	@SuppressWarnings("unchecked")
	public BeanCopier(final Object source, final T target, final Type targetType, final CopyOptions copyOptions) {
		Assert.notNull(source, "Source bean must be not null!");
		Assert.notNull(target, "Target bean must be not null!");

		final Copier<T> copier;
		if (source instanceof Map) {
			if (target instanceof Map) {
				copier = (Copier<T>) new MapToMapCopier((Map<?, ?>) source, (Map<?, ?>) target, targetType, copyOptions);
			} else {
				copier = new MapToBeanCopier<>((Map<?, ?>) source, target, targetType, copyOptions);
			}
		} else if (source instanceof ValueProvider) {
			copier = new ValueProviderToBeanCopier<>((ValueProvider<String>) source, target, targetType, copyOptions);
		} else {
			if (target instanceof Map) {
				copier = (Copier<T>) new BeanToMapCopier(source, (Map<?, ?>) target, targetType, copyOptions);
			} else {
				copier = new BeanToBeanCopier<>(source, target, targetType, copyOptions);
			}
		}
		this.copier = copier;
	}

	@Override
	public T copy() {
		return copier.copy();
	}
}
