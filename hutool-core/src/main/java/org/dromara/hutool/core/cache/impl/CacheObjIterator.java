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
