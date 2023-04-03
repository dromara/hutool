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

package org.dromara.hutool.core.comparator;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.array.ArrayUtil;

import java.util.Comparator;

/**
 * 按照数组的顺序正序排列，数组的元素位置决定了对象的排序先后<br>
 * 默认的，如果参与排序的元素并不在数组中，则排序在前（可以通过atEndIfMiss设置)
 *
 * @param <T> 被排序元素类型
 * @author looly
 * @since 4.1.5
 */
public class IndexedComparator<T> implements Comparator<T> {

	private final boolean atEndIfMiss;
	private final T[] array;

	/**
	 * 构造
	 *
	 * @param objs 参与排序的数组，数组的元素位置决定了对象的排序先后
	 */
	@SuppressWarnings("unchecked")
	public IndexedComparator(final T... objs) {
		this(false, objs);
	}

	/**
	 * 构造
	 *
	 * @param atEndIfMiss 如果不在列表中是否排在后边
	 * @param objs        参与排序的数组，数组的元素位置决定了对象的排序先后
	 */
	@SuppressWarnings("unchecked")
	public IndexedComparator(final boolean atEndIfMiss, final T... objs) {
		Assert.notNull(objs, "'objs' array must not be null");
		this.atEndIfMiss = atEndIfMiss;
		this.array = objs;
	}

	@Override
	public int compare(final T o1, final T o2) {
		final int index1 = getOrder(o1);
		final int index2 = getOrder(o2);

		// 任意一个元素不在列表中
		if(index1 == index2){
			if(index1 < 0 || index1 == this.array.length){
				// 任意一个元素不在列表中, 返回原顺序
				return 1;
			}
		}

		return Integer.compare(index1, index2);
	}

	/**
	 * 查找对象类型所在列表的位置
	 *
	 * @param object 对象
	 * @return 位置，未找到位置根据{@link #atEndIfMiss}取不同值，false返回-1，否则返回列表长度
	 */
	private int getOrder(final T object) {
		int order = ArrayUtil.indexOf(array, object);
		if (order < 0) {
			order = this.atEndIfMiss ? this.array.length : -1;
		}
		return order;
	}
}
