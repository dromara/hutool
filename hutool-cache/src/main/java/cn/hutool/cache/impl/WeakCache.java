package cn.hutool.cache.impl;

import cn.hutool.cache.Cache;
import cn.hutool.core.lang.func.Func0;
import cn.hutool.core.lang.mutable.MutableObj;

import java.util.Iterator;
import java.util.WeakHashMap;

/**
 * 弱引用缓存<br>
 * 对于一个给定的键，其映射的存在并不阻止垃圾回收器对该键的丢弃，这就使该键成为可终止的，被终止，然后被回收。<br>
 * 丢弃某个键时，其条目从映射中有效地移除。<br>
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author Looly
 * @since 3.0.7
 */
public class WeakCache<K, V> implements Cache<K, V> {
	private static final long serialVersionUID = 1L;

	TimedCache<MutableObj<K>, V> timedCache;

	/**
	 * 构造
	 *
	 * @param timeout 超时
	 */
	public WeakCache(long timeout) {
		this.timedCache = new TimedCache<>(timeout, new WeakHashMap<>());
	}

	@Override
	public int capacity() {
		return timedCache.capacity();
	}

	@Override
	public long timeout() {
		return timedCache.timeout();
	}

	@Override
	public void put(K key, V object) {
		timedCache.put(new MutableObj<>(key), object);
	}

	@Override
	public void put(K key, V object, long timeout) {
		timedCache.put(new MutableObj<>(key), object, timeout);
	}

	@Override
	public V get(K key, boolean isUpdateLastAccess, Func0<V> supplier) {
		return timedCache.get(new MutableObj<>(key), isUpdateLastAccess, supplier);
	}

	@Override
	public V get(K key, boolean isUpdateLastAccess) {
		return timedCache.get(new MutableObj<>(key), isUpdateLastAccess);
	}

	@Override
	public Iterator<CacheObj<K, V>> cacheObjIterator() {
		final Iterator<CacheObj<MutableObj<K>, V>> timedIter = timedCache.cacheObjIterator();
		return new Iterator<CacheObj<K, V>>() {
			@Override
			public boolean hasNext() {
				return timedIter.hasNext();
			}

			@Override
			public CacheObj<K, V> next() {
				final CacheObj<MutableObj<K>, V> next = timedIter.next();
				final CacheObj<K, V> nextNew = new CacheObj<>(next.key.get(), next.obj, next.ttl);
				nextNew.lastAccess = next.lastAccess;
				nextNew.accessCount = next.accessCount;
				return nextNew;
			}
		};
	}

	@Override
	public int prune() {
		return timedCache.prune();
	}

	@Override
	public boolean isFull() {
		return timedCache.isFull();
	}

	@Override
	public void remove(K key) {
		timedCache.remove(new MutableObj<>(key));
	}

	@Override
	public void clear() {
		timedCache.clear();
	}

	@Override
	public int size() {
		return timedCache.size();
	}

	@Override
	public boolean isEmpty() {
		return timedCache.isEmpty();
	}

	@Override
	public boolean containsKey(K key) {
		return timedCache.containsKey(new MutableObj<>(key));
	}

	@Override
	public Iterator<V> iterator() {
		return timedCache.iterator();
	}
}
