package com.xiaoleilu.hutool.cache.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时缓存<br>
 * 此缓存没有容量限制，对象只有在过期后才会被移除
 * 
 * @author Looly
 *
 * @param <K> 键类型
 * @param <V> 值类型
 */
public class TimedCache<K, V> extends AbstractCache<K, V> {
	
	/** 定时器 */
	protected Timer pruneTimer;
	
	/**
	 * 构造
	 * 
	 * @param timeout 过期时长
	 */
	public TimedCache(long timeout) {
		this(timeout, new HashMap<K, CacheObj<K, V>>());
	}

	/**
	 * 构造
	 * 
	 * @param timeout 过期时长
	 * @param map 存储缓存对象的map
	 */
	public TimedCache(long timeout, Map<K, CacheObj<K, V>> map) {
		this.capacity = 0;
		this.timeout = timeout;
		this.cacheMap = map;
	}

	// ---------------------------------------------------------------- prune
	/**
	 * 清理过期对象
	 * @return  清理数
	 */
	@Override
	protected int pruneCache() {
		int count = 0;
		Iterator<CacheObj<K, V>> values = cacheMap.values().iterator();
		CacheObj<K, V> co;
		while (values.hasNext()) {
			co = values.next();
			if (co.isExpired()) {
				values.remove();
				count++;
			}
		}
		return count;
	}

	// ---------------------------------------------------------------- auto prune
	/**
	 * 定时清理
	 * @param delay 间隔时长
	 */
	public void schedulePrune(long delay) {
		if (pruneTimer != null) {
			pruneTimer.cancel();
		}
		pruneTimer = new Timer();
		pruneTimer.schedule(new TimerTask(){
			@Override
			public void run() {
				prune();
			}
		}, delay, delay);
	}

	/**
	 * 取消定时清理
	 */
	public void cancelPruneSchedule() {
		if (pruneTimer != null) {
			pruneTimer.cancel();
			pruneTimer = null;
		}
	}

}
