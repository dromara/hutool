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

import org.dromara.hutool.core.lang.wrapper.SimpleWrapper;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * 列表包装类<br>
 * 提供列表包装，用于在执行列表方法前后自定义处理逻辑
 *
 * @param <E> 元素类型
 */
public class ListWrapper<E> extends SimpleWrapper<List<E>> implements List<E> {
	/**
	 * 构造
	 *
	 * @param raw 原始对象
	 */
	public ListWrapper(final List<E> raw) {
		super(raw);
	}


	@Override
	public int size() {
		return raw.size();
	}

	@Override
	public boolean isEmpty() {
		return raw.isEmpty();
	}

	@Override
	public boolean contains(final Object o) {
		return raw.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return raw.iterator();
	}

	@Override
	public void forEach(final Consumer<? super E> action) {
		raw.forEach(action);
	}

	@Override
	public Object[] toArray() {
		return raw.toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return raw.toArray(a);
	}

	@Override
	public boolean add(final E e) {
		return raw.add(e);
	}

	@Override
	public boolean remove(final Object o) {
		return raw.remove(o);
	}

	@SuppressWarnings("SlowListContainsAll")
	@Override
	public boolean containsAll(final Collection<?> c) {
		return raw.containsAll(c);
	}

	@Override
	public boolean addAll(final Collection<? extends E> c) {
		return raw.addAll(c);
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends E> c) {
		return raw.addAll(index, c);
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		return raw.removeAll(c);
	}

	@Override
	public boolean removeIf(final Predicate<? super E> filter) {
		return raw.removeIf(filter);
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		return raw.retainAll(c);
	}

	@Override
	public void replaceAll(final UnaryOperator<E> operator) {
		raw.replaceAll(operator);
	}

	@Override
	public void sort(final Comparator<? super E> c) {
		raw.sort(c);
	}

	@Override
	public void clear() {
		raw.clear();
	}

	@Override
	public E get(final int index) {
		return raw.get(index);
	}

	@Override
	public E set(final int index, final E element) {
		return raw.set(index, element);
	}

	@Override
	public void add(final int index, final E element) {
		raw.add(index, element);
	}

	@Override
	public E remove(final int index) {
		return raw.remove(index);
	}

	@Override
	public int indexOf(final Object o) {
		return raw.indexOf(o);
	}

	@Override
	public int lastIndexOf(final Object o) {
		return raw.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return raw.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(final int index) {
		return raw.listIterator(index);
	}

	@Override
	public List<E> subList(final int fromIndex, final int toIndex) {
		return raw.subList(fromIndex, toIndex);
	}

	@Override
	public Spliterator<E> spliterator() {
		return raw.spliterator();
	}

	@Override
	public Stream<E> stream() {
		return raw.stream();
	}

	@Override
	public Stream<E> parallelStream() {
		return raw.parallelStream();
	}
}
