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

import org.dromara.hutool.core.collection.iter.CopiedIter;

import java.util.Iterator;
import java.util.concurrent.locks.StampedLock;

/**
 * 使用{@link StampedLock}保护的缓存，使用读写乐观锁
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author looly
 * @since 5.7.15
 */
public abstract class StampedCache<K, V> extends AbstractCache<K, V> {
	private static final long serialVersionUID = 1L;

	/**
	 * 乐观锁，此处使用乐观锁解决读多写少的场景<br>
	 * get时乐观读，再检查是否修改，修改则转入悲观读重新读一遍，可以有效解决在写时阻塞大量读操作的情况。<br>
	 * see: https://www.cnblogs.com/jiagoushijuzi/p/13721319.html
	 */
	protected final StampedLock lock = new StampedLock();

	@Override
	public void put(final K key, final V object, final long timeout) {
		final long stamp = lock.writeLock();
		try {
			putWithoutLock(key, object, timeout);
		} finally {
			lock.unlockWrite(stamp);
		}
	}

	@Override
	public boolean containsKey(final K key) {
		return null != get(key, false, false);
	}

	@Override
	public V get(final K key, final boolean isUpdateLastAccess) {
		return get(key, isUpdateLastAccess, true);
	}

	@Override
	public Iterator<CacheObj<K, V>> cacheObjIterator() {
		CopiedIter<CacheObj<K, V>> copiedIterator;
		final long stamp = lock.readLock();
		try {
			copiedIterator = CopiedIter.copyOf(cacheObjIter());
		} finally {
			lock.unlockRead(stamp);
		}
		return new CacheObjIterator<>(copiedIterator);
	}

	@Override
	public final int prune() {
		final long stamp = lock.writeLock();
		try {
			return pruneCache();
		} finally {
			lock.unlockWrite(stamp);
		}
	}

	@Override
	public void remove(final K key) {
		final long stamp = lock.writeLock();
		CacheObj<K, V> co;
		try {
			co = removeWithoutLock(key);
		} finally {
			lock.unlockWrite(stamp);
		}
		if (null != co) {
			onRemove(co.key, co.obj);
		}
	}

	@Override
	public void clear() {
		final long stamp = lock.writeLock();
		try {
			cacheMap.clear();
		} finally {
			lock.unlockWrite(stamp);
		}
	}

	/**
	 * 获取值
	 *
	 * @param key                键
	 * @param isUpdateLastAccess 是否更新最后修改时间
	 * @param isUpdateCount      是否更新命中数，get时更新，contains时不更新
	 * @return 值或null
	 */
	private V get(final K key, final boolean isUpdateLastAccess, final boolean isUpdateCount) {
		// 尝试读取缓存，使用乐观读锁
		long stamp = lock.tryOptimisticRead();
		CacheObj<K, V> co = getWithoutLock(key);
		if (false == lock.validate(stamp)) {
			// 有写线程修改了此对象，悲观读
			stamp = lock.readLock();
			try {
				co = getWithoutLock(key);
			} finally {
				lock.unlockRead(stamp);
			}
		}

		// 未命中
		if (null == co) {
			if (isUpdateCount) {
				missCount.increment();
			}
			return null;
		} else if (false == co.isExpired()) {
			if (isUpdateCount) {
				hitCount.increment();
			}
			return co.get(isUpdateLastAccess);
		}

		// 悲观锁，二次检查
		return getOrRemoveExpired(key, isUpdateCount);
	}

	/**
	 * 同步获取值，如果过期则移除之
	 *
	 * @param key           键
	 * @param isUpdateCount 是否更新命中数，get时更新，contains时不更新
	 * @return 有效值或null
	 */
	private V getOrRemoveExpired(final K key, final boolean isUpdateCount) {
		final long stamp = lock.writeLock();
		CacheObj<K, V> co;
		try {
			co = getWithoutLock(key);
			if (null == co) {
				return null;
			}
			if (false == co.isExpired()) {
				// 首先尝试获取值，如果值存在且有效，返回之
				if (isUpdateCount) {
					hitCount.increment();
				}
				return co.getValue();
			}

			// 无效移除
			co = removeWithoutLock(key);
			if (isUpdateCount) {
				missCount.increment();
			}
		} finally {
			lock.unlockWrite(stamp);
		}
		if (null != co) {
			onRemove(co.key, co.obj);
		}
		return null;
	}
}
