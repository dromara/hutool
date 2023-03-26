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

package cn.hutool.core.comparator;

import cn.hutool.core.bean.BeanUtil;

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
	 * @param property 属性名
	 * @param isNullGreater {@code null}值是否排在后（从小到大排序）
	 */
	public PropertyComparator(final String property, final boolean isNullGreater) {
		super(isNullGreater, (bean)-> BeanUtil.getProperty(bean, property));
	}
}
