/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.comparator;

import org.dromara.hutool.core.lang.Assert;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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
	/**
	 * map存储对象类型所在列表的位置,k为对象，v为位置
	 */
	private final Map<? super T, Integer> map;

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
	 * @param map         参与排序的map，map中的value值大小决定了对象的排序先后
	 */
	public IndexedComparator(final boolean atEndIfMiss, final Map<? super T, Integer> map) {
		this.atEndIfMiss = atEndIfMiss;
		this.map = map;
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
		map = new HashMap<>(objs.length, 1);
		for (int i = 0; i < objs.length; i++) {
			map.put(objs[i], i);
		}
	}

	@Override
	public int compare(final T o1, final T o2) {
		final int index1 = getOrder(o1);
		final int index2 = getOrder(o2);

		if (index1 == index2) {
			if (index1 < 0 || index1 == this.map.size()) {
				// 任意一个元素不在map中, 返回原顺序
				return 1;
			}

			// 位置一样，认为是同一个元素
			return 0;
		}

		return Integer.compare(index1, index2);
	}

	/**
	 * 查找对象类型所对应的顺序值,即在原列表中的顺序
	 *
	 * @param object 对象
	 * @return 位置，未找到位置根据{@link #atEndIfMiss}取不同值，false返回-1，否则返回map长度
	 */
	private int getOrder(final T object) {
		Integer order = map.get(object);
		if (order == null) {
			order = this.atEndIfMiss ? this.map.size() : -1;
		}
		return order;
	}
}
