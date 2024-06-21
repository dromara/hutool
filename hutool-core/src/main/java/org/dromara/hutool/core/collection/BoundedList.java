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

package org.dromara.hutool.core.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 指定边界大小的List<br>
 * 通过指定边界，可以限制List的最大容量
 *
 * @param <E> 元素类型
 * @author Looly
 * @since 6.0.0
 */
public class BoundedList<E> extends ListWrapper<E> implements BoundedCollection<E> {

	private final int maxSize;

	/**
	 * 构造
	 *
	 * @param maxSize 最大容量
	 */
	public BoundedList(final int maxSize) {
		this(new ArrayList<>(maxSize), maxSize);
	}

	/**
	 * 构造，限制集合的最大容量为提供的List
	 *
	 * @param raw     原始对象
	 * @param maxSize 最大容量
	 */
	public BoundedList(final List<E> raw, final int maxSize) {
		super(raw);
		this.maxSize = maxSize;
	}

	@Override
	public boolean isFull() {
		return size() == this.maxSize;
	}

	@Override
	public int maxSize() {
		return this.maxSize;
	}

	@Override
	public boolean add(final E e) {
		checkFull(1);
		return super.add(e);
	}

	@Override
	public void add(final int index, final E element) {
		checkFull(1);
		super.add(index, element);
	}

	@Override
	public boolean addAll(final Collection<? extends E> c) {
		checkFull(c.size());
		return super.addAll(c);
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends E> c) {
		checkFull(c.size());
		return super.addAll(index, c);
	}

	private void checkFull(final int addSize) {
		if (size() + addSize > this.maxSize) {
			throw new IndexOutOfBoundsException("List is no space to add " + addSize + " elements!");
		}
	}
}
