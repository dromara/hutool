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

package org.dromara.hutool.core.cache.impl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * {@link AbstractCache} 的CacheObj迭代器.
 *
 * @author looly
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @since 4.0.10
 */
public class CacheObjIterator<K, V> implements Iterator<CacheObj<K, V>>, Serializable {
	private static final long serialVersionUID = 1L;

	private final Iterator<CacheObj<K, V>> iterator;
	private CacheObj<K, V> nextValue;

	/**
	 * 构造
	 *
	 * @param iterator 原{@link Iterator}
	 */
	CacheObjIterator(final Iterator<CacheObj<K, V>> iterator) {
		this.iterator = iterator;
		nextValue();
	}

	/**
	 * @return 是否有下一个值
	 */
	@Override
	public boolean hasNext() {
		return nextValue != null;
	}

	/**
	 * @return 下一个值
	 */
	@Override
	public CacheObj<K, V> next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		final CacheObj<K, V> cachedObject = nextValue;
		nextValue();
		return cachedObject;
	}

	/**
	 * 从缓存中移除没有过期的当前值，此方法不支持
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException("Cache values Iterator is not support to modify.");
	}

	/**
	 * 下一个值，当不存在则下一个值为null
	 */
	private void nextValue() {
		while (iterator.hasNext()) {
			nextValue = iterator.next();
			if (nextValue.isExpired() == false) {
				return;
			}
		}
		nextValue = null;
	}
}
