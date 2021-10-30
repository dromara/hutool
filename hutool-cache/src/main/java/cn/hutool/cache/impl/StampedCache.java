package cn.hutool.cache.impl;

import cn.hutool.core.collection.CopiedIter;

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
public abstract class StampedCache<K, V> extends AbstractCache<K, V>{
	private static final long serialVersionUID = 1L;

	// 乐观锁，此处使用乐观锁解决读多写少的场景
	// get时乐观读，再检查是否修改，修改则转入悲观读重新读一遍，可以有效解决在写时阻塞大量读操作的情况。
	// see: https://www.cnblogs.com/jiagoushijuzi/p/13721319.html
	protected final StampedLock lock = new StampedLock();

	@Override
	public void put(K key, V object, long timeout) {
		final long stamp = lock.writeLock();
		try {
			putWithoutLock(key, object, timeout);
		} finally {
			lock.unlockWrite(stamp);
		}
	}

	@Override
	public boolean containsKey(K key) {
		final long stamp = lock.readLock();
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
			lock.unlockRead(stamp);
		}

		// 过期
		remove(key, true);
		return false;
	}

	@Override
	public V get(K key, boolean isUpdateLastAccess) {
		// 尝试读取缓存，使用乐观读锁
		long stamp = lock.tryOptimisticRead();
		CacheObj<K, V> co = cacheMap.get(key);
		if(false == lock.validate(stamp)){
			// 有写线程修改了此对象，悲观读
			stamp = lock.readLock();
			try {
				co = cacheMap.get(key);
			} finally {
				lock.unlockRead(stamp);
			}
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
		final long stamp = lock.readLock();
		try {
			copiedIterator = CopiedIter.copyOf(this.cacheMap.values().iterator());
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
	public void remove(K key) {
		remove(key, false);
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
	 * 移除key对应的对象
	 *
	 * @param key           键
	 * @param withMissCount 是否计数丢失数
	 */
	private void remove(K key, boolean withMissCount) {
		final long stamp = lock.writeLock();
		CacheObj<K, V> co;
		try {
			co = removeWithoutLock(key, withMissCount);
		} finally {
			lock.unlockWrite(stamp);
		}
		if (null != co) {
			onRemove(co.key, co.obj);
		}
	}
}
