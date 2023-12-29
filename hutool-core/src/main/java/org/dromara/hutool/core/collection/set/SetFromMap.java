/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.collection.set;

import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 基于Map的Set实现
 *
 * @param <E> 元素类型
 * @author guava
 */
public class SetFromMap<E> extends AbstractSet<E> implements Serializable {
	private static final long serialVersionUID = 1L;

	private final Map<E, Boolean> m;  // The backing map
	private transient Set<E> s;       // Its keySet

	/**
	 * 构造
	 *
	 * @param map Map
	 */
	public SetFromMap(final Map<E, Boolean> map) {
		m = map;
		s = map.keySet();
	}

	@Override
	public void clear() {
		m.clear();
	}

	@Override
	public int size() {
		return m.size();
	}

	@Override
	public boolean isEmpty() {
		return m.isEmpty();
	}

	@SuppressWarnings("SuspiciousMethodCalls")
	@Override
	public boolean contains(final Object o) {
		return m.containsKey(o);
	}

	@Override
	public boolean remove(final Object o) {
		return m.remove(o) != null;
	}

	@Override
	public boolean add(final E e) {
		return m.put(e, Boolean.TRUE) == null;
	}

	@Override
	public Iterator<E> iterator() {
		return s.iterator();
	}

	@Override
	public Object[] toArray() {
		return s.toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return super.toArray(a);
	}

	@Override
	public String toString() {
		return s.toString();
	}

	@Override
	public int hashCode() {
		return s.hashCode();
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(final Object o) {
		return o == this || s.equals(o);
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		return s.containsAll(c);
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		return s.removeAll(c);
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		return s.retainAll(c);
	}

	@Override
	public void forEach(final Consumer<? super E> action) {
		s.forEach(action);
	}

	@Override
	public boolean removeIf(final Predicate<? super E> filter) {
		return s.removeIf(filter);
	}

	@Override
	public Spliterator<E> spliterator() {
		return s.spliterator();
	}

	@Override
	public Stream<E> stream() {
		return s.stream();
	}

	@Override
	public Stream<E> parallelStream() {
		return s.parallelStream();
	}

	private void readObject(final java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		s = m.keySet();
	}
}
