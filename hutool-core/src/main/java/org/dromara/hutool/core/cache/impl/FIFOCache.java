/*
 * Copyright (c) 2013-2024 Hutool Team.
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

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * FIFO(first in first out) 先进先出缓存.
 *
 * <p>
 * 元素不停的加入缓存直到缓存满为止，当缓存满时，清理过期缓存对象，清理后依旧满则删除先入的缓存（链表首部对象）<br>
 * 优点：简单快速 <br>
 * 缺点：不灵活，不能保证最常用的对象总是被保留
 * </p>
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author Looly
 */
public class FIFOCache<K, V> extends StampedCache<K, V> {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造，默认对象不过期
	 *
	 * @param capacity 容量
	 */
	public FIFOCache(final int capacity) {
		this(capacity, 0);
	}

	/**
	 * 构造
	 *
	 * @param capacity 容量
	 * @param timeout  过期时长
	 */
	public FIFOCache(final int capacity, final long timeout) {
		this.capacity = capacity;
		this.timeout = timeout;
		cacheMap = new LinkedHashMap<>(capacity + 1, 1.0f, false);
	}

	/**
	 * 先进先出的清理策略<br>
	 * 先遍历缓存清理过期的缓存对象，如果清理后还是满的，则删除第一个缓存对象
	 */
	@Override
	protected int pruneCache() {
		int count = 0;
		CacheObj<K, V> first = null;

		// 清理过期对象并找出链表头部元素（先入元素）
		final Iterator<CacheObj<K, V>> values = cacheObjIter();
		if (isPruneExpiredActive()) {
			// 清理过期对象并找出链表头部元素（先入元素）
			while (values.hasNext()) {
				final CacheObj<K, V> co = values.next();
				if (co.isExpired()) {
					values.remove();
					onRemove(co.key, co.obj);
					count++;
					continue;
				}
				if (first == null) {
					first = co;
				}
			}
		} else {
			first = values.hasNext() ? values.next() : null;
		}

		// 清理结束后依旧是满的，则删除第一个被缓存的对象
		if (isFull() && null != first) {
			removeWithoutLock(first.key);
			onRemove(first.key, first.obj);
			count++;
		}
		return count;
	}
}
