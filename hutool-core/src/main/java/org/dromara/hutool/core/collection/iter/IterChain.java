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

package org.dromara.hutool.core.collection.iter;

import org.dromara.hutool.core.lang.Chain;
import org.dromara.hutool.core.array.ArrayUtil;

import java.util.*;

/**
 * 组合{@link Iterator}，将多个{@link Iterator}组合在一起，便于集中遍历。<br>
 * 来自Jodd
 *
 * @param <T> 元素类型
 * @author looly, jodd
 */
public class IterChain<T> implements Iterator<T>, Chain<Iterator<T>, IterChain<T>> {

	/**
	 * 所有的Iterator
	 */
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

	/**
	 * 当前位置
	 */
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
		if (!hasNext()) {
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
