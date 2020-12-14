package cn.hutool.cache.impl;

import cn.hutool.cache.Cache;
import cn.hutool.core.lang.func.Func0;

import java.util.Iterator;

/**
 * 无缓存实现，用于快速关闭缓存
 * 
 * @param <K> 键类型
 * @param <V> 值类型
 * @author Looly,jodd
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
	public void put(K key, V object) {
		// 跳过
	}

	@Override
	public void put(K key, V object, long timeout) {
		// 跳过
	}

	@Override
	public boolean containsKey(K key) {
		return false;
	}

	@Override
	public V get(K key) {
		return null;
	}

	@Override
	public V get(K key, boolean isUpdateLastAccess) {
		return null;
	}
	
	@Override
	public V get(K key, Func0<V> supplier) {
		return get(key, true, supplier);
	}

	@Override
	public V get(K key, boolean isUpdateLastAccess, Func0<V> supplier) {
		try {
			return (null == supplier) ? null : supplier.call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
	public void remove(K key) {
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
