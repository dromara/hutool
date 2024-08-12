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

package org.dromara.hutool.core.cache;

import org.dromara.hutool.core.cache.impl.FIFOCache;
import org.dromara.hutool.core.cache.impl.LFUCache;
import org.dromara.hutool.core.cache.impl.LRUCache;
import org.dromara.hutool.core.cache.impl.NoCache;
import org.dromara.hutool.core.cache.impl.TimedCache;
import org.dromara.hutool.core.cache.impl.WeakCache;

/**
 * 缓存工具类
 * @author Looly
 *@since 3.0.1
 */
public class CacheUtil {

	/**
	 * 创建FIFO(first in first out) 先进先出缓存.
	 *
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param capacity 容量
	 * @param timeout 过期时长，单位：毫秒
	 * @return {@link FIFOCache}
	 */
	public static <K, V> FIFOCache<K, V> newFIFOCache(final int capacity, final long timeout){
		return new FIFOCache<>(capacity, timeout);
	}

	/**
	 * 创建FIFO(first in first out) 先进先出缓存.
	 *
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param capacity 容量
	 * @return {@link FIFOCache}
	 */
	public static <K, V> FIFOCache<K, V> newFIFOCache(final int capacity){
		return new FIFOCache<>(capacity);
	}

	/**
	 * 创建LFU(least frequently used) 最少使用率缓存.
	 *
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param capacity 容量
	 * @param timeout 过期时长，单位：毫秒
	 * @return {@link LFUCache}
	 */
	public static <K, V> LFUCache<K, V> newLFUCache(final int capacity, final long timeout){
		return new LFUCache<>(capacity, timeout);
	}

	/**
	 * 创建LFU(least frequently used) 最少使用率缓存.
	 *
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param capacity 容量
	 * @return {@link LFUCache}
	 */
	public static <K, V> LFUCache<K, V> newLFUCache(final int capacity){
		return new LFUCache<>(capacity);
	}


	/**
	 * 创建LRU (least recently used)最近最久未使用缓存.
	 *
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param capacity 容量
	 * @param timeout 过期时长，单位：毫秒
	 * @return {@link LRUCache}
	 */
	public static <K, V> LRUCache<K, V> newLRUCache(final int capacity, final long timeout){
		return new LRUCache<>(capacity, timeout);
	}

	/**
	 * 创建LRU (least recently used)最近最久未使用缓存.
	 *
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param capacity 容量
	 * @return {@link LRUCache}
	 */
	public static <K, V> LRUCache<K, V> newLRUCache(final int capacity){
		return new LRUCache<>(capacity);
	}

	/**
	 * 创建定时缓存，通过定时任务自动清除过期缓存对象
	 *
	 * @param <K>                Key类型
	 * @param <V>                Value类型
	 * @param timeout            过期时长，单位：毫秒
	 * @param schedulePruneDelay 间隔时长，单位毫秒
	 * @return {@link TimedCache}
	 * @since 5.8.28
	 */
	public static <K, V> TimedCache<K, V> newTimedCache(final long timeout, final long schedulePruneDelay) {
		final TimedCache<K, V> cache = newTimedCache(timeout);
		return cache.schedulePrune(schedulePruneDelay);
	}

	/**
	 * 创建定时缓存.
	 *
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param timeout 过期时长，单位：毫秒
	 * @return {@link TimedCache}
	 */
	public static <K, V> TimedCache<K, V> newTimedCache(final long timeout){
		return new TimedCache<>(timeout);
	}

	/**
	 * 创建弱引用缓存.
	 *
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param timeout 过期时长，单位：毫秒
	 * @return {@link WeakCache}
	 * @since 3.0.7
	 */
	public static <K, V> WeakCache<K, V> newWeakCache(final long timeout){
		return new WeakCache<>(timeout);
	}

	/**
	 * 创建无缓存实现.
	 *
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @return {@link NoCache}
	 */
	public static <K, V> NoCache<K, V> newNoCache(){
		return new NoCache<>();
	}
}
