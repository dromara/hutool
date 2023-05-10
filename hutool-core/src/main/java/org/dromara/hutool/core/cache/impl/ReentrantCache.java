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

import org.dromara.hutool.core.collection.iter.CopiedIter;

import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用{@link ReentrantLock}保护的缓存，读写都使用悲观锁完成，主要避免某些Map无法使用读写锁的问题<br>
 * 例如使用了LinkedHashMap的缓存，由于get方法也会改变Map的结构，因此读写必须加互斥锁
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author looly
 * @since 5.7.15
 */
public abstract class ReentrantCache<K, V> extends AbstractCache<K, V> {
	private static final long serialVersionUID = 1L;

	// 一些特殊缓存，例如使用了LinkedHashMap的缓存，由于get方法也会改变Map的结构，导致无法使用读写锁
	// TODO 最优的解决方案是使用Guava的ConcurrentLinkedHashMap，此处使用简化的互斥锁
	protected final ReentrantLock lock = new ReentrantLock();

	@Override
	public void put(final K key, final V object, final long timeout) {
		lock.lock();
		try {
			putWithoutLock(key, object, timeout);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean containsKey(final K key) {
		lock.lock();
		try {
			// 不存在或已移除
			final CacheObj<K, V> co = getWithoutLock(key);
			if (co == null) {
				return false;
			}

			if (!co.isExpired()) {
				// 命中
				return true;
			}
		} finally {
			lock.unlock();
		}

		// 过期
		remove(key, true);
		return false;
	}

	@Override
	public V get(final K key, final boolean isUpdateLastAccess) {
		CacheObj<K, V> co;
		lock.lock();
		try {
			co = getWithoutLock(key);
		} finally {
			lock.unlock();
		}

		// 未命中
		if (null == co) {
			missCount.increment();
			return null;
		} else if (!co.isExpired()) {
			hitCount.increment();
			return co.get(isUpdateLastAccess);
		}

		// 过期，既不算命中也不算非命中
		remove(key, true);
		return null;
	}

	@Override
	public Iterator<CacheObj<K, V>> cacheObjIterator() {
		CopiedIter<CacheObj<K, V>> copiedIterator;
		lock.lock();
		try {
			copiedIterator = CopiedIter.copyOf(cacheObjIter());
		} finally {
			lock.unlock();
		}
		return new CacheObjIterator<>(copiedIterator);
	}

	@Override
	public final int prune() {
		lock.lock();
		try {
			return pruneCache();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void remove(final K key) {
		remove(key, false);
	}

	@Override
	public void clear() {
		lock.lock();
		try {
			cacheMap.clear();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public String toString() {
		lock.lock();
		try {
			return super.toString();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 移除key对应的对象
	 *
	 * @param key           键
	 * @param withMissCount 是否计数丢失数
	 */
	private void remove(final K key, final boolean withMissCount) {
		CacheObj<K, V> co;
		lock.lock();
		try {
			co = removeWithoutLock(key, withMissCount);
		} finally {
			lock.unlock();
		}
		if (null != co) {
			onRemove(co.key, co.obj);
		}
	}
}
