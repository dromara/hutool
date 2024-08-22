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

package org.dromara.hutool.core.collection.iter;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.util.ObjUtil;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 复制 {@link Iterator}<br>
 * 为了解决并发情况下{@link Iterator}遍历导致的问题（当Iterator被修改会抛出ConcurrentModificationException）
 * ，故使用复制原Iterator的方式解决此问题。
 *
 * <p>
 * 解决方法为：在构造方法中遍历Iterator中的元素，装入新的List中然后遍历之。
 * 当然，修改这个复制后的Iterator是没有意义的，因此remove方法将会抛出异常。
 *
 * <p>
 * 需要注意的是，在构造此对象时需要保证原子性（原对象不被修改），最好加锁构造此对象，构造完毕后解锁。
 *
 * @param <E> 元素类型
 * @author Looly
 * @since 3.0.7
 */
public class CopiedIter<E> implements IterableIter<E>, Serializable {
	private static final long serialVersionUID = 1L;

	private final Iterator<E> listIterator;

	/**
	 * 根据已有{@link Iterator}，返回新的{@code CopiedIter}
	 *
	 * @param iterator {@link Iterator}
	 * @param <E>      元素类型
	 * @return {@code CopiedIter}
	 */
	public static <E> CopiedIter<E> copyOf(final Iterator<E> iterator) {
		return new CopiedIter<>(iterator);
	}

	/**
	 * 构造，当{@code iterator}为空时，默认复制一个空迭代器
	 *
	 * @param iterator 被复制的Iterator
	 */
	public CopiedIter(final Iterator<E> iterator) {
		final List<E> eleList = ListUtil.of(ObjUtil.defaultIfNull(iterator, Collections.emptyIterator()));
		this.listIterator = eleList.iterator();
	}

	@Override
	public boolean hasNext() {
		return this.listIterator.hasNext();
	}

	@Override
	public E next() {
		return this.listIterator.next();
	}

	/**
	 * 此对象不支持移除元素
	 *
	 * @throws UnsupportedOperationException 当调用此方法时始终抛出此异常
	 */
	@Override
	public void remove() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This is a read-only iterator.");
	}
}
