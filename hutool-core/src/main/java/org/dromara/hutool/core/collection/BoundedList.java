/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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
