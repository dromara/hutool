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

import org.dromara.hutool.lang.Assert;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 * 包装 {@link Iterator}并根据{@link Predicate}定义，过滤元素输出<br>
 * 类实现来自Apache Commons Collection
 *
 * @author apache commons, looly
 * @since 5.8.0
 */
public class FilterIter<E> implements Iterator<E> {

	private final Iterator<? extends E> iterator;
	private final Predicate<? super E> filter;

	/**
	 * 下一个元素
	 */
	private E nextObject;
	/**
	 * 标记下一个元素是否被计算
	 */
	private boolean nextObjectSet = false;

	/**
	 * 构造
	 *
	 * @param iterator 被包装的{@link Iterator}
	 * @param filter   过滤函数，{@code null}表示不过滤
	 * @throws NullPointerException {@code iterator}为{@code null}时抛出
	 */
	public FilterIter(final Iterator<? extends E> iterator, final Predicate<? super E> filter) {
		this.iterator = Assert.notNull(iterator);
		this.filter = filter;
	}

	@Override
	public boolean hasNext() {
		return nextObjectSet || setNextObject();
	}

	@Override
	public E next() {
		if (false == nextObjectSet && false == setNextObject()) {
			throw new NoSuchElementException();
		}
		nextObjectSet = false;
		return nextObject;
	}

	@Override
	public void remove() {
		if (nextObjectSet) {
			throw new IllegalStateException("remove() cannot be called");
		}
		iterator.remove();
	}

	/**
	 * 获取被包装的{@link Iterator}
	 *
	 * @return {@link Iterator}
	 */
	public Iterator<? extends E> getIterator() {
		return iterator;
	}

	/**
	 * 获取过滤函数
	 *
	 * @return 过滤函数，可能为{@code null}
	 */
	public Predicate<? super E> getFilter() {
		return filter;
	}

	/**
	 * 设置下一个元素，如果存在返回{@code true}，否则{@code false}
	 */
	private boolean setNextObject() {
		while (iterator.hasNext()) {
			final E object = iterator.next();
			if (null == filter || filter.test(object)) {
				nextObject = object;
				nextObjectSet = true;
				return true;
			}
		}
		return false;
	}

}
