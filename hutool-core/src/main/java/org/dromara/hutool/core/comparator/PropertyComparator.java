/*
 * Copyright (c) 2024 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.comparator;

import org.dromara.hutool.core.bean.BeanUtil;

/**
 * Bean属性排序器<br>
 * 支持读取Bean多层次下的属性
 *
 * @author Looly
 *
 * @param <T> 被比较的Bean
 */
public class PropertyComparator<T> extends FuncComparator<T> {
	private static final long serialVersionUID = 9157326766723846313L;

	/**
	 * 构造，默认{@code null}排在后（从小到大排序）
	 *
	 * @param property 属性名
	 */
	public PropertyComparator(final String property) {
		this(property, true);
	}

	/**
	 * 构造
	 *
	 * @param property      属性名
	 * @param isNullGreater null值是否排在后（从小到大排序）
	 */
	public PropertyComparator(final String property, final boolean isNullGreater) {
		this(property, true, isNullGreater);
	}

	/**
	 * 构造
	 *
	 * @param property      属性名
	 * @param compareSelf   在字段值相同情况下，是否比较对象本身。
	 *                      如果此项为{@code false}，字段值比较后为0会导致对象被认为相同，可能导致被去重。
	 * @param isNullGreater null值是否排在后（从小到大排序）
	 * @since 5.8.28
	 */
	public PropertyComparator(final String property, final boolean compareSelf, final boolean isNullGreater) {
		super(isNullGreater, compareSelf, (bean) -> BeanUtil.getProperty(bean, property));
	}
}
