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

package org.dromara.hutool.cache.impl;

import org.dromara.hutool.cache.Cache;
import org.dromara.hutool.lang.func.SerSupplier;

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
