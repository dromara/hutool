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

package cn.hutool.core.cache;

import cn.hutool.core.cache.impl.FIFOCache;
import cn.hutool.core.cache.impl.LFUCache;
import cn.hutool.core.cache.impl.LRUCache;
import cn.hutool.core.cache.impl.NoCache;
import cn.hutool.core.cache.impl.TimedCache;
import cn.hutool.core.cache.impl.WeakCache;

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
