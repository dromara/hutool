package cn.hutool.cache.impl;

import cn.hutool.cache.Cache;
import cn.hutool.core.collection.CopiedIter;
import cn.hutool.core.lang.func.Func0;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

/**
 * 超时和限制大小的缓存的默认实现<br>
 * 继承此抽象缓存需要：<br>
 * <ul>
 * <li>创建一个新的Map</li>
 * <li>实现 <code>prune</code> 策略</li>
 * </ul>
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author Looly, jodd
 */
public abstract class AbstractCache<K, V> implements Cache<K, V> {
	private static final long serialVersionUID = 1L;

	protected Map<K, CacheObj<K, V>> cacheMap;

	private final StampedLock lock = new StampedLock();

	/**
	 * 返回缓存容量，<code>0</code>表示无大小限制
	 */
	protected int capacity;
	/**
	 * 缓存失效时长， <code>0</code> 表示无限制，单位毫秒
	 */
	protected long timeout;

	/**
	 * 每个对象是否有单独的失效时长，用于决定清理过期对象是否有必要。
	 */
	protected boolean existCustomTimeout;

	/**
	 * 命中数
	 */
	protected int hitCount;
	/**
	 * 丢失数
	 */
	protected int missCount;

	// ---------------------------------------------------------------- put start
	@Override
	public void put(K key, V object) {
		put(key, object, timeout);
	}

	@Override
	public void put(K key, V object, long timeout) {
		final long stamp = lock.writeLock();
		try {
			putWithoutLock(key, object, timeout);
		} finally {
			lock.unlockWrite(stamp);
		}
	}

	/**
	 * 加入元素，无锁
	 *
	 * @param key     键
	 * @param object  值
	 * @param timeout 超时时长
	 * @since 4.5.16
	 */
	private void putWithoutLock(K key, V object, long timeout) {
		CacheObj<K, V> co = new CacheObj<>(key, object, timeout);
		if (timeout != 0) {
			existCustomTimeout = true;
		}
		if (isFull()) {
			pruneCache();
		}
		cacheMap.put(key, co);
	}
	// ---------------------------------------------------------------- put end

	// ---------------------------------------------------------------- get start
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

	/**
	 * @return 命中数
	 */
	public int getHitCount() {
		return hitCount;
	}

	/**
	 * @return 丢失数
	 */
	public int getMissCount() {
		return missCount;
	}

	@Override
	public V get(K key, boolean isUpdateLastAccess, Func0<V> supplier) {
		V v = get(key, isUpdateLastAccess);
		if (null == v && null != supplier) {
			final long stamp = lock.writeLock();
			try {
				// 双重检查锁
				final CacheObj<K, V> co = cacheMap.get(key);
				if (null == co || co.isExpired()) {
					try {
						v = supplier.call();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					putWithoutLock(key, v, this.timeout);
				} else {
					v = co.get(true);
				}
			} finally {
				lock.unlockWrite(stamp);
			}
		}
		return v;
	}

	@Override
	public V get(K key, boolean isUpdateLastAccess) {
		// 尝试读取缓存，使用乐观读锁
		long stamp = lock.readLock();
		try {
			// 不存在或已移除
			final CacheObj<K, V> co = cacheMap.get(key);
			if (null == co) {
				missCount++;
				return null;
			}

			if (co.isExpired()) {
				missCount++;
			} else{
				// 命中
				hitCount++;
				return co.get(isUpdateLastAccess);
			}
		} finally {
			lock.unlock(stamp);
		}

		// 过期
		remove(key, true);
		return null;
	}

	// ---------------------------------------------------------------- get end

	@Override
	public Iterator<V> iterator() {
		CacheObjIterator<K, V> copiedIterator = (CacheObjIterator<K, V>) this.cacheObjIterator();
		return new CacheValuesIterator<>(copiedIterator);
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

	// ---------------------------------------------------------------- prune start

	/**
	 * 清理实现<br>
	 * 子类实现此方法时无需加锁
	 *
	 * @return 清理数
	 */
	protected abstract int pruneCache();

	@Override
	public final int prune() {
		final long stamp = lock.writeLock();
		try {
			return pruneCache();
		} finally {
			lock.unlockWrite(stamp);
		}
	}
	// ---------------------------------------------------------------- prune end

	// ---------------------------------------------------------------- common start
	@Override
	public int capacity() {
		return capacity;
	}

	/**
	 * @return 默认缓存失效时长。<br>
	 * 每个对象可以单独设置失效时长
	 */
	@Override
	public long timeout() {
		return timeout;
	}

	/**
	 * 只有设置公共缓存失效时长或每个对象单独的失效时长时清理可用
	 *
	 * @return 过期对象清理是否可用，内部使用
	 */
	protected boolean isPruneExpiredActive() {
		return (timeout != 0) || existCustomTimeout;
	}

	@Override
	public boolean isFull() {
		return (capacity > 0) && (cacheMap.size() >= capacity);
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

	@Override
	public int size() {
		return cacheMap.size();
	}

	@Override
	public boolean isEmpty() {
		return cacheMap.isEmpty();
	}

	@Override
	public String toString() {
		return this.cacheMap.toString();
	}
	// ---------------------------------------------------------------- common end

	/**
	 * 对象移除回调。默认无动作
	 *
	 * @param key          键
	 * @param cachedObject 被缓存的对象
	 */
	protected void onRemove(K key, V cachedObject) {
		// ignore
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

	/**
	 * 移除key对应的对象，不加锁
	 *
	 * @param key           键
	 * @param withMissCount 是否计数丢失数
	 * @return 移除的对象，无返回null
	 */
	private CacheObj<K, V> removeWithoutLock(K key, boolean withMissCount) {
		final CacheObj<K, V> co = cacheMap.remove(key);
		if (withMissCount) {
			// 在丢失计数有效的情况下，移除一般为get时的超时操作，此处应该丢失数+1
			this.missCount++;
		}
		return co;
	}
}
