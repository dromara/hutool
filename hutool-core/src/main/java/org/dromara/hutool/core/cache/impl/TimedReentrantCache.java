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

import org.dromara.hutool.core.cache.GlobalPruneTimer;
import org.dromara.hutool.core.lang.mutable.Mutable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * 定时缓存<br>
 * 此缓存没有容量限制，对象只有在过期后才会被移除
 *
 * @author Looly
 *
 * @param <K> 键类型
 * @param <V> 值类型
 */
public class TimedReentrantCache<K, V> extends ReentrantCache<K, V> {
	private static final long serialVersionUID = 1L;

	/** 正在执行的定时任务 */
	private ScheduledFuture<?> pruneJobFuture;

	/**
	 * 构造
	 *
	 * @param timeout 超时（过期）时长，单位毫秒
	 */
	public TimedReentrantCache(final long timeout) {
		this(timeout, new HashMap<>());
	}

	/**
	 * 构造
	 *
	 * @param timeout 过期时长
	 * @param map 存储缓存对象的map
	 */
	public TimedReentrantCache(final long timeout, final Map<Mutable<K>, CacheObj<K, V>> map) {
		this.capacity = 0;
		this.timeout = timeout;
		this.cacheMap = map;
	}

	// ---------------------------------------------------------------- prune
	/**
	 * 清理过期对象
	 *
	 * @return 清理数
	 */
	@Override
	protected int pruneCache() {
		int count = 0;
		final Iterator<CacheObj<K, V>> values = cacheObjIter();
		CacheObj<K, V> co;
		while (values.hasNext()) {
			co = values.next();
			if (co.isExpired()) {
				values.remove();
				onRemove(co.key, co.obj);
				count++;
			}
		}
		return count;
	}

	// ---------------------------------------------------------------- auto prune
	/**
	 * 定时清理
	 *
	 * @param delay 间隔时长，单位毫秒
	 * @return this
	 */
	public TimedReentrantCache<K, V> schedulePrune(final long delay) {
		this.pruneJobFuture = GlobalPruneTimer.INSTANCE.schedule(this::prune, delay);
		return this;
	}

	/**
	 * 取消定时清理
	 */
	public void cancelPruneSchedule() {
		if (null != pruneJobFuture) {
			pruneJobFuture.cancel(true);
		}
	}

}
