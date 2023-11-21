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

package org.dromara.hutool.core.cache;

import org.dromara.hutool.core.collection.iter.TransIter;
import org.dromara.hutool.core.func.SerSupplier;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.mutable.Mutable;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.dromara.hutool.core.map.SafeConcurrentHashMap;
import org.dromara.hutool.core.map.WeakConcurrentMap;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 简单缓存，无超时实现，默认使用{@link WeakConcurrentMap}实现缓存自动清理
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author Looly, VampireAchao
 */
public class SimpleCache<K, V> implements Iterable<Map.Entry<K, V>>, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 池
	 */
	private final Map<Mutable<K>, V> rawMap;
	// 乐观读写锁
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	/**
	 * 写的时候每个key一把锁，降低锁的粒度
	 */
	protected final Map<K, Lock> keyLockMap = new SafeConcurrentHashMap<>();

	/**
	 * 构造，默认使用{@link WeakHashMap}实现缓存自动清理
	 */
	public SimpleCache() {
		this(new WeakConcurrentMap<>());
	}

	/**
	 * 构造
	 * <p>
	 * 通过自定义Map初始化，可以自定义缓存实现。<br>
	 * 比如使用{@link WeakHashMap}则会自动清理key，使用HashMap则不会清理<br>
	 * 同时，传入的Map对象也可以自带初始化的键值对，防止在get时创建
	 * </p>
	 *
	 * @param initMap 初始Map，用于定义Map类型
	 */
	public SimpleCache(final Map<Mutable<K>, V> initMap) {
		this.rawMap = initMap;
	}

	/**
	 * 是否包含键
	 *
	 * @param key 键
	 * @return 是否包含
	 */
	public boolean containsKey(final K key) {
		lock.readLock().lock();
		try {
			return rawMap.containsKey(MutableObj.of(key));
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * 从缓存池中查找值
	 *
	 * @param key 键
	 * @return 值
	 */
	public V get(final K key) {
		lock.readLock().lock();
		try {
			return rawMap.get(MutableObj.of(key));
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * 从缓存中获得对象，当对象不在缓存中或已经过期返回SerSupplier回调产生的对象
	 *
	 * @param key      键
	 * @param supplier 如果不存在回调方法，用于生产值对象
	 * @return 值对象
	 */
	public V get(final K key, final SerSupplier<V> supplier) {
		return get(key, null, supplier);
	}

	/**
	 * 从缓存中获得对象，当对象不在缓存中或已经过期返回SerSupplier回调产生的对象
	 *
	 * @param key            键
	 * @param validPredicate 检查结果对象是否可用，如是否断开连接等
	 * @param supplier       如果不存在回调方法或结果不可用，用于生产值对象
	 * @return 值对象
	 * @since 5.7.9
	 */
	public V get(final K key, final Predicate<V> validPredicate, final SerSupplier<V> supplier) {
		V v = get(key);
		if ((null != validPredicate && null != v && !validPredicate.test(v))) {
			v = null;
		}
		if (null == v && null != supplier) {
			//每个key单独获取一把锁，降低锁的粒度提高并发能力，see pr#1385@Github
			final Lock keyLock = this.keyLockMap.computeIfAbsent(key, k -> new ReentrantLock());
			keyLock.lock();
			try {
				// 双重检查，防止在竞争锁的过程中已经有其它线程写入
				v = get(key);
				if (null == v || (null != validPredicate && !validPredicate.test(v))) {
					v = supplier.get();
					put(key, v);
				}
			} finally {
				keyLock.unlock();
				keyLockMap.remove(key);
			}
		}

		return v;
	}

	/**
	 * 放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @return 值
	 */
	public V put(final K key, final V value) {
		Assert.notNull(value, "'value' must not be null");
		// 独占写锁
		lock.writeLock().lock();
		try {
			rawMap.put(MutableObj.of(key), value);
		} finally {
			lock.writeLock().unlock();
		}
		return value;
	}

	/**
	 * 移除缓存
	 *
	 * @param key 键
	 * @return 移除的值
	 */
	public V remove(final K key) {
		// 独占写锁
		lock.writeLock().lock();
		try {
			return rawMap.remove(MutableObj.of(key));
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * 清空缓存池
	 */
	public void clear() {
		// 独占写锁
		lock.writeLock().lock();
		try {
			this.rawMap.clear();
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public Iterator<Map.Entry<K, V>> iterator() {
		return new TransIter<>(this.rawMap.entrySet().iterator(), (entry) -> new Map.Entry<K, V>() {
			@Override
			public K getKey() {
				return entry.getKey().get();
			}

			@Override
			public V getValue() {
				return entry.getValue();
			}

			@Override
			public V setValue(final V value) {
				return entry.setValue(value);
			}
		});
	}

	/**
	 * 获取所有键
	 *
	 * @return 所有键
	 */
	public List<K> keys(){
		return this.rawMap.keySet().stream().map(Mutable::get).collect(Collectors.toList());
	}
}
