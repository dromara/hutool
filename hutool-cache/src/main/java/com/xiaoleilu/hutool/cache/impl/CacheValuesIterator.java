package com.xiaoleilu.hutool.cache.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  {@link com.xiaoleilu.hutool.cache.impl.AbstractCache} 的值迭代器.
 * @author looly
 *
 * @param <V> 迭代对象类型
 */
public class CacheValuesIterator<V> implements Iterator<V> {

	private final Iterator<? extends CacheObj<?, V>> iterator;
	private CacheObj<?,V> nextValue;

	/**
	 * 构造
	 * @param iterator 原{@link Iterator}
	 * @param readLock 读锁
	 */
	CacheValuesIterator(Iterator<? extends CacheObj<?, V>> iterator) {
		this.iterator = iterator;
		nextValue();
	}

	/**
	 * @return 是否有下一个值
	 */
	@Override
	public boolean hasNext() {
		return nextValue != null;
	}

	/**
	 * @return 下一个值
	 */
	@Override
	public V next() {
		if (false == hasNext()) {
			throw new NoSuchElementException();
		}
		final V cachedObject = nextValue.obj;
		nextValue();
		return cachedObject;
	}

	/**
	 * 从缓存中移除没有过期的当前值
	 */
	@Override
	public void remove() {
//		iterator.remove();
		throw new UnsupportedOperationException("Cache values Iterator is not support to modify.");
	}
	
	/**
	 * 下一个值，当不存在则下一个值为null
	 */
	private void nextValue() {
		while (iterator.hasNext()) {
			nextValue = iterator.next();
			if (nextValue.isExpired() == false) {
				return;
			}
		}
		nextValue = null;
	}
}
