package com.xiaoleilu.hutool.cache;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 超时和限制大小的缓存的默认实现<br>
 * 继承此抽象缓存需要：<br>
 * <ul>
 * 		<li>创建一个新的Map</li>
 * 		<li>实现 <code>prune</code> 策略</li>
 * </ul>
 * 
 * @author Looly,jodd
 *
 * @param <K> 键类型
 * @param <V> 值类型
 */
public abstract class AbstractCache<K, V> implements Cache<K, V>, Iterable<V>{

	protected Map<K, CacheObj<K, V>> cacheMap;

	private final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();
	private final Lock readLock = cacheLock.readLock();
	private final Lock writeLock = cacheLock.writeLock();

	/** 返回缓存容量，<code>0</code>表示无大小限制 */
	protected int capacity;
	/** 缓存失效时长， <code>0</code> 表示没有设置 */
	protected long timeout;
	
	/** 每个对象是否有单独的失效时长，用于决定清理过期对象是否有必要。 */
	protected boolean existCustomTimeout;
	
	/** 命中数 */
	protected int hitCount;
	/** 丢失数 */
	protected int missCount;
	
	// ---------------------------------------------------------------- put start
	@Override
	public void put(K key, V object) {
		put(key, object, timeout);
	}

	@Override
	public void put(K key, V object, long timeout) {
		writeLock.lock();

		try {
			CacheObj<K, V> co = new CacheObj<K, V>(key, object, timeout);
			if (timeout != 0) {
				existCustomTimeout = true;
			}
			if (isFull()) {
				pruneCache();
			}
			cacheMap.put(key, co);
		} finally {
			writeLock.unlock();
		}
	}
	// ---------------------------------------------------------------- put end

	// ---------------------------------------------------------------- get start
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
	public V get(K key) {
		readLock.lock();

		try {
			//不存在或已移除
			final CacheObj<K, V> co = cacheMap.get(key);
			if (co == null) {
				missCount++;
				return null;
			}
			
			//过期
			if (co.isExpired() == true) {
				// remove(key); // 此方法无法获得锁
				cacheMap.remove(key);

				missCount++;
				return null;
			}

			//命中
			hitCount++;
			return co.get();
		} finally {
			readLock.unlock();
		}
	}
	// ---------------------------------------------------------------- get end

	@Override
	public Iterator<V> iterator() {
		return new CacheValuesIterator<V>(this);
	}

	// ---------------------------------------------------------------- prune start
	/**
	 * 清理实现
	 * @return 清理数
	 */
	protected abstract int pruneCache();

	@Override 
	public final int prune() {
		writeLock.lock();
		try {
			return pruneCache();
		} finally {
			writeLock.unlock();
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
		writeLock.lock();
		try {
			cacheMap.remove(key);
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public void clear() {
		writeLock.lock();
		try {
			cacheMap.clear();
		} finally {
			writeLock.unlock();
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
}
