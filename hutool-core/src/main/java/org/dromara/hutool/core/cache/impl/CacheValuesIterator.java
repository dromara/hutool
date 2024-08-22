/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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
