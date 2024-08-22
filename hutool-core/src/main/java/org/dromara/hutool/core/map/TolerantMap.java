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

package org.dromara.hutool.core.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 一个可以提供默认值的Map
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author pantao, looly
 */
public class TolerantMap<K, V> extends MapWrapper<K, V> {
	private static final long serialVersionUID = -4158133823263496197L;

	private final V defaultValue;

	/**
	 * 构造
	 *
	 * @param defaultValue 默认值
	 */
	public TolerantMap(final V defaultValue) {
		this(new HashMap<>(), defaultValue);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始容量
	 * @param loadFactor      增长因子
	 * @param defaultValue    默认值
	 */
	public TolerantMap(final int initialCapacity, final float loadFactor, final V defaultValue) {
		this(new HashMap<>(initialCapacity, loadFactor), defaultValue);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始容量
	 * @param defaultValue    默认值
	 */
	public TolerantMap(final int initialCapacity, final V defaultValue) {
		this(new HashMap<>(initialCapacity), defaultValue);
	}

	/**
	 * 构造
	 *
	 * @param map          Map实现
	 * @param defaultValue 默认值
	 */
	public TolerantMap(final Map<K, V> map, final V defaultValue) {
		super(map);
		this.defaultValue = defaultValue;
	}

	/**
	 * 构建TolerantMap
	 *
	 * @param map          map实现
	 * @param defaultValue 默认值
	 * @param <K>          键类型
	 * @param <V>          值类型
	 * @return TolerantMap
	 */
	public static <K, V> TolerantMap<K, V> of(final Map<K, V> map, final V defaultValue) {
		return new TolerantMap<>(map, defaultValue);
	}

	@Override
	public V get(final Object key) {
		return getOrDefault(key, defaultValue);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		final TolerantMap<?, ?> that = (TolerantMap<?, ?>) o;
		return getRaw().equals(that.getRaw())
				&& Objects.equals(defaultValue, that.defaultValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getRaw(), defaultValue);
	}

	@Override
	public String toString() {
		return "TolerantMap{" + "map=" + getRaw() + ", defaultValue=" + defaultValue + '}';
	}
}
