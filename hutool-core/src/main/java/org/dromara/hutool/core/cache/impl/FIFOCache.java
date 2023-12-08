/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
