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

package org.dromara.hutool.cache.impl;

import java.io.Serializable;
import java.util.Iterator;

/**
 * {@link AbstractCache} 的值迭代器.
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
	CacheValuesIterator(final CacheObjIterator<?, V> iterator) {
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
