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

package org.dromara.hutool.core.collection.set;

import org.dromara.hutool.core.lang.wrapper.SimpleWrapper;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Set包装类<br>
 * 提供列表包装，用于在执行列表方法前后自定义处理逻辑
 *
 * @param <E> 元素类型
 * @author Looly
 * @since 6.0.0
 */
public class SetWrapper<E> extends SimpleWrapper<Set<E>> implements Set<E> {

	/**
	 * 构造
	 *
	 * @param raw 原始对象
	 */
	public SetWrapper(final Set<E> raw) {
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
		return new Object[0];
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

	@Override
	public boolean containsAll(final Collection<?> c) {
		return raw.containsAll(c);
	}

	@Override
	public boolean addAll(final Collection<? extends E> c) {
		return raw.addAll(c);
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		return raw.retainAll(c);
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
	public void clear() {
		raw.clear();
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
