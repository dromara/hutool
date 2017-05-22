package com.xiaoleilu.hutool.cache;

import com.xiaoleilu.hutool.cache.impl.FIFOCache;
import com.xiaoleilu.hutool.cache.impl.LFUCache;
import com.xiaoleilu.hutool.cache.impl.LRUCache;
import com.xiaoleilu.hutool.cache.impl.NoCache;
import com.xiaoleilu.hutool.cache.impl.TimedCache;

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
	 * @param timeout 过期时长
	 * @return {@link FIFOCache}
	 */
	public static <K, V> Cache<K, V> newFIFOCache(int capacity, long timeout){
		return new FIFOCache<K, V>(capacity, timeout);
	}
	
	/**
	 * 创建FIFO(first in first out) 先进先出缓存.
	 * 
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param capacity 容量
	 * @return {@link FIFOCache}
	 */
	public static <K, V> Cache<K, V> newFIFOCache(int capacity){
		return new FIFOCache<K, V>(capacity);
	}
	
	/**
	 * 创建LFU(least frequently used) 最少使用率缓存.
	 * 
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param capacity 容量
	 * @param timeout 过期时长
	 * @return {@link LFUCache}
	 */
	public static <K, V> Cache<K, V> newLFUCache(int capacity, long timeout){
		return new LFUCache<K, V>(capacity, timeout);
	}
	
	/**
	 * 创建LFU(least frequently used) 最少使用率缓存.
	 * 
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param capacity 容量
	 * @return {@link LFUCache}
	 */
	public static <K, V> Cache<K, V> newLFUCache(int capacity){
		return new LFUCache<K, V>(capacity);
	}
	
	
	/**
	 * 创建LRU (least recently used)最近最久未使用缓存.
	 * 
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param capacity 容量
	 * @param timeout 过期时长
	 * @return {@link LRUCache}
	 */
	public static <K, V> Cache<K, V> newLRUCache(int capacity, long timeout){
		return new LRUCache<K, V>(capacity, timeout);
	}
	
	/**
	 * 创建LRU (least recently used)最近最久未使用缓存.
	 * 
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param capacity 容量
	 * @return {@link LRUCache}
	 */
	public static <K, V> Cache<K, V> newLRUCache(int capacity){
		return new LRUCache<K, V>(capacity);
	}
	
	/**
	 * 创建定时缓存.
	 * 
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param timeout 过期时长
	 * @return {@link TimedCache}
	 */
	public static <K, V> Cache<K, V> newTimedCache(long timeout){
		return new TimedCache<K, V>(timeout);
	}
	
	/**
	 * 创建无缓存实现.
	 * 
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @return {@link NoCache}
	 */
	public static <K, V> Cache<K, V> newNoCache(){
		return new NoCache<K, V>();
	}
}
