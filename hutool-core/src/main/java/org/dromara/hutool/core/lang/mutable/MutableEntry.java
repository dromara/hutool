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

package org.dromara.hutool.core.lang.mutable;

import org.dromara.hutool.core.map.AbsEntry;

import java.io.Serializable;
import java.util.Map;

/**
 * 可变键和值的{@link Map.Entry}实现，可以修改键和值
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author looly
 */
public class MutableEntry<K, V> extends AbsEntry<K, V> implements Mutable<Map.Entry<K, V>>, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建{@code MutableEntry}
	 *
	 * @param key   键
	 * @param value 值
	 * @param <K>   键类型
	 * @param <V>   值类型
	 * @return {@code MutableEntry}
	 */
	public static <K, V> MutableEntry<K, V> of(final K key, final V value) {
		return new MutableEntry<>(key, value);
	}

	/**
	 * 键
	 */
	protected K key;
	/**
	 * 值
	 */
	protected V value;

	/**
	 * 构造
	 *
	 * @param key   键
	 * @param value 值
	 */
	public MutableEntry(final K key, final V value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * 获取键
	 *
	 * @return 键
	 */
	@Override
	public K getKey() {
		return this.key;
	}

	/**
	 * 获取值
	 *
	 * @return 值
	 */
	@Override
	public V getValue() {
		return this.value;
	}

	/**
	 * 设置键
	 *
	 * @param key 新键
	 * @return old key
	 */
	public K setKey(final K key) {
		final K oldKey = this.key;
		this.key = key;
		return oldKey;
	}

	/**
	 * 设置值
	 *
	 * @param value 新值
	 * @return old value
	 */
	@Override
	public V setValue(final V value) {
		final V oldValue = this.value;
		this.value = value;
		return oldValue;
	}

	@Override
	public Map.Entry<K, V> get() {
		return this;
	}

	@Override
	public void set(final Map.Entry<K, V> pair) {
		this.key = pair.getKey();
		this.value = pair.getValue();
	}
}
