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

package cn.hutool.core.map;

import cn.hutool.core.util.JdkUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 安全的ConcurrentHashMap实现<br>
 * 此类用于解决在JDK8中调用{@link ConcurrentHashMap#computeIfAbsent(Object, Function)}可能造成的死循环问题。<br>
 * 方法来自Dubbo，见：issues#2349<br>
 * <p>
 * 相关bug见：@see <a href="https://bugs.openjdk.java.net/browse/JDK-8161372">https://bugs.openjdk.java.net/browse/JDK-8161372</a>
 *
 * @param <K> 键类型
 * @param <V> 值类型
 */
public class SafeConcurrentHashMap<K, V> extends ConcurrentHashMap<K, V> {
	private static final long serialVersionUID = 1L;

	// region == 构造 ==

	/**
	 * 构造，默认初始大小（16）
	 */
	public SafeConcurrentHashMap() {
		super();
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 预估初始大小
	 */
	public SafeConcurrentHashMap(final int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * 构造
	 *
	 * @param m 初始键值对
	 */
	public SafeConcurrentHashMap(final Map<? extends K, ? extends V> m) {
		super(m);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始容量
	 * @param loadFactor      增长系数
	 */
	public SafeConcurrentHashMap(final int initialCapacity, final float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity  初始容量
	 * @param loadFactor       增长系数
	 * @param concurrencyLevel 并发级别，即Segment的个数
	 */
	public SafeConcurrentHashMap(final int initialCapacity,
								 final float loadFactor, final int concurrencyLevel) {
		super(initialCapacity, loadFactor, concurrencyLevel);
	}
	// endregion == 构造 ==

	@Override
	public V computeIfAbsent(final K key, final Function<? super K, ? extends V> mappingFunction) {
		if (JdkUtil.IS_JDK8) {
			V value = get(key);
			if (null == value) {
				putIfAbsent(key, mappingFunction.apply(key));
				value = get(key);

				// 判空后调用依旧无法解决死循环问题
				// 见：Issue2349Test
				//value = map.computeIfAbsent(key, mappingFunction);
			}
			return value;
		} else {
			return super.computeIfAbsent(key, mappingFunction);
		}
	}
}
