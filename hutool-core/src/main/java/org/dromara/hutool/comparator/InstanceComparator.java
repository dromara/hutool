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

package org.dromara.hutool.comparator;

import org.dromara.hutool.lang.Assert;

import java.util.Comparator;

/**
 * 按照指定类型顺序排序，对象顺序取决于对象对应的类在数组中的位置。
 *
 * <p>如果对比的两个对象类型相同，返回{@code 0}，默认如果对象类型不在列表中，则排序在前</p>
 * <p>此类来自Spring，有所改造</p>
 *
 * @param <T> 用于比较的对象类型
 * @author Phillip Webb
 * @since 5.4.1
 */
public class InstanceComparator<T> implements Comparator<T> {

	private final boolean atEndIfMiss;
	private final Class<?>[] instanceOrder;

	/**
	 * 构造
	 *
	 * @param instanceOrder 用于比较排序的对象类型数组，排序按照数组位置排序
	 */
	public InstanceComparator(final Class<?>... instanceOrder) {
		this(false, instanceOrder);
	}

	/**
	 * 构造
	 *
	 * @param atEndIfMiss   如果不在列表中是否排在后边
	 * @param instanceOrder 用于比较排序的对象类型数组，排序按照数组位置排序
	 */
	public InstanceComparator(final boolean atEndIfMiss, final Class<?>... instanceOrder) {
		Assert.notNull(instanceOrder, "'instanceOrder' array must not be null");
		this.atEndIfMiss = atEndIfMiss;
		this.instanceOrder = instanceOrder;
	}


	@Override
	public int compare(final T o1, final T o2) {
		final int i1 = getOrder(o1);
		final int i2 = getOrder(o2);
		return Integer.compare(i1, i2);
	}

	/**
	 * 查找对象类型所在列表的位置
	 *
	 * @param object 对象
	 * @return 位置，未找到位置根据{@link #atEndIfMiss}取不同值，false返回-1，否则返回列表长度
	 */
	private int getOrder(final T object) {
		if (object != null) {
			for (int i = 0; i < this.instanceOrder.length; i++) {
				if (this.instanceOrder[i].isInstance(object)) {
					return i;
				}
			}
		}
		return this.atEndIfMiss ? this.instanceOrder.length : -1;
	}
}
