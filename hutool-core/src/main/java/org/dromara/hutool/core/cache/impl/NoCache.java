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

package org.dromara.hutool.core.cache.impl;

import org.dromara.hutool.core.cache.Cache;
import org.dromara.hutool.core.func.SerSupplier;

import java.util.Iterator;

/**
 * 无缓存实现，用于快速关闭缓存
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author Looly, jodd, VampireAchao
 */
public class NoCache<K, V> implements Cache<K, V> {
	private static final long serialVersionUID = 1L;

	@Override
	public int capacity() {
		return 0;
	}

	@Override
	public long timeout() {
		return 0;
	}

	@Override
	public void put(final K key, final V object) {
		// 跳过
	}

	@Override
	public void put(final K key, final V object, final long timeout) {
		// 跳过
	}

	@Override
	public boolean containsKey(final K key) {
		return false;
	}

	@Override
	public V get(final K key) {
		return null;
	}

	@Override
	public V get(final K key, final boolean isUpdateLastAccess) {
		return null;
	}

	@Override
	public V get(final K key, final SerSupplier<V> supplier) {
		return get(key, true, supplier);
	}

	@Override
	public V get(final K key, final boolean isUpdateLastAccess, final SerSupplier<V> supplier) {
		return get(key, isUpdateLastAccess, 0, supplier);
	}

	@Override
	public V get(final K key, final boolean isUpdateLastAccess, final long timeout, final SerSupplier<V> supplier) {
		return (null == supplier) ? null : supplier.get();
	}

	@Override
	public Iterator<V> iterator() {
		return new Iterator<V>() {
			@Override
			public boolean hasNext() {
				return false;
			}

			@Override
			public V next() {
				return null;
			}
		};
	}

	@Override
	public Iterator<CacheObj<K, V>> cacheObjIterator() {
		return null;
	}

	@Override
	public int prune() {
		return 0;
	}

	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public void remove(final K key) {
		// 跳过
	}

	@Override
	public void clear() {
		// 跳过
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

}
