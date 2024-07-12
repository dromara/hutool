/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
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

import org.dromara.hutool.core.map.reference.WeakConcurrentMap;

/**
 * Bean描述信息工厂类<br>
 * 通过不同的类和策略，生成对应的{@link BeanDesc}，策略包括：
 * <ul>
 *     <li>当类为Record时，生成{@link RecordBeanDesc}</li>
 *     <li>当类为普通Bean时，生成{@link StrictBeanDesc}</li>
 * </ul>
 *
 * @author Looly
 * @since 6.0.0
 */
public class BeanDescFactory {

	private static final WeakConcurrentMap<Class<?>, BeanDesc> bdCache = new WeakConcurrentMap<>();

	/**
	 * 获取{@link BeanDesc} Bean描述信息，使用Weak缓存
	 *
	 * @param clazz Bean类
	 * @return {@link BeanDesc}
	 */
	public static BeanDesc getBeanDesc(final Class<?> clazz) {
		return bdCache.computeIfAbsent(clazz, (key) -> getBeanDescWithoutCache(clazz));
	}

	/**
	 * 获取{@link BeanDesc} Bean描述信息，不使用缓存
	 *
	 * @param clazz Bean类
	 * @return {@link BeanDesc}
	 */
	public static BeanDesc getBeanDescWithoutCache(final Class<?> clazz) {
		if (RecordUtil.isRecord(clazz)) {
			return new RecordBeanDesc(clazz);
		} else {
			return new StrictBeanDesc(clazz);
		}
	}

	/**
	 * 清空全局的Bean属性缓存
	 *
	 * @since 5.7.21
	 */
	public static void clearCache() {
		bdCache.clear();
	}
}
