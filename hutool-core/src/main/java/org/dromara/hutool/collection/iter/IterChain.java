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

package org.dromara.hutool.collection.iter;

import org.dromara.hutool.lang.Chain;
import org.dromara.hutool.array.ArrayUtil;

import java.util.*;

/**
 * 组合{@link Iterator}，将多个{@link Iterator}组合在一起，便于集中遍历。<br>
 * 来自Jodd
 *
 * @param <T> 元素类型
 * @author looly, jodd
 */
public class IterChain<T> implements Iterator<T>, Chain<Iterator<T>, IterChain<T>> {

	protected final List<Iterator<T>> allIterators = new ArrayList<>();

	/**
	 * 构造
	 * 可以使用 {@link #addChain(Iterator)} 方法加入更多的集合。
	 */
	public IterChain() {
	}

	/**
	 * 构造
	 * @param iterators 多个{@link Iterator}
	 * @throws IllegalArgumentException 当存在重复的迭代器，或添加的迭代器中存在{@code null}时抛出
	 */
	@SafeVarargs
	public IterChain(final Iterator<T>... iterators) {
		if (ArrayUtil.isNotEmpty(iterators)) {
			for (final Iterator<T> iterator : iterators) {
				addChain(iterator);
			}
		}
	}

	/**
	 * 添加迭代器
	 *
	 * @param iterator 迭代器
	 * @return 当前实例
	 * @throws IllegalArgumentException 当迭代器被重复添加，或待添加的迭代器为{@code null}时抛出
	 */
	@Override
	public IterChain<T> addChain(final Iterator<T> iterator) {
		Objects.requireNonNull(iterator);
		if (allIterators.contains(iterator)) {
			throw new IllegalArgumentException("Duplicate iterator");
		}
		allIterators.add(iterator);
		return this;
	}

	// ---------------------------------------------------------------- interface

	protected int currentIter = -1;

	@Override
	public boolean hasNext() {
		if (currentIter == -1) {
			currentIter = 0;
		}

		final int size = allIterators.size();
		for (int i = currentIter; i < size; i++) {
			final Iterator<T> iterator = allIterators.get(i);
			if (iterator.hasNext()) {
				currentIter = i;
				return true;
			}
		}
		return false;
	}

	@Override
	public T next() {
		if (false == hasNext()) {
			throw new NoSuchElementException();
		}

		return allIterators.get(currentIter).next();
	}

	@Override
	public void remove() {
		if (-1 == currentIter) {
			throw new IllegalStateException("next() has not yet been called");
		}

		allIterators.get(currentIter).remove();
	}

	@Override
	public Iterator<Iterator<T>> iterator() {
		return this.allIterators.iterator();
	}
}
