package cn.hutool.cache.impl;

import cn.hutool.core.collection.CopiedIter;

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
	// 最优的解决方案是使用Guava的ConcurrentLinkedHashMap，此处使用简化的互斥锁
	protected final ReentrantLock lock = new ReentrantLock();

	@Override
	public void put(K key, V object, long timeout) {
		lock.lock();
		try {
			putWithoutLock(key, object, timeout);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean containsKey(K key) {
		lock.lock();
		try {
			// 不存在或已移除
			final CacheObj<K, V> co = cacheMap.get(key);
			if (co == null) {
				return false;
			}

			if (false == co.isExpired()) {
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
	public V get(K key, boolean isUpdateLastAccess) {
		CacheObj<K, V> co;
		lock.lock();
		try {
			co = cacheMap.get(key);
		} finally {
			lock.unlock();
		}

		// 未命中
		if (null == co) {
			missCount.increment();
			return null;
		} else if (false == co.isExpired()) {
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
			copiedIterator = CopiedIter.copyOf(this.cacheMap.values().iterator());
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
	public void remove(K key) {
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

	/**
	 * 移除key对应的对象
	 *
	 * @param key           键
	 * @param withMissCount 是否计数丢失数
	 */
	private void remove(K key, boolean withMissCount) {
		lock.lock();
		CacheObj<K, V> co;
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
