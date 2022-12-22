package cn.hutool.cache.impl;

import java.io.Serializable;
import java.util.Iterator;

/**
 *  {@link cn.hutool.cache.impl.AbstractCache} 的值迭代器.
 * @author looly
 *
 * @param <V> 迭代对象类型
 */
public class CacheValuesIterator<V> implements Iterator<V>, Serializable {
	private static final long serialVersionUID = 1L;

	private final CacheObjIterator<?, V> cacheObjIter;

	/**
	 * 构造
	 * @param iterator 原{@link CacheObjIterator}
	 */
	CacheValuesIterator(CacheObjIterator<?, V> iterator) {
		this.cacheObjIter = iterator;
	}

	/**
	 * @return 是否有下一个值
	 */
	@Override
	public boolean hasNext() {
		return this.cacheObjIter.hasNext();
	}

	/**
	 * @return 下一个值
	 */
	@Override
	public V next() {
		return cacheObjIter.next().getValue();
	}

	/**
	 * 从缓存中移除没有过期的当前值，不支持此方法
	 */
	@Override
	public void remove() {
		cacheObjIter.remove();
	}
}
