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
	 * @return
	 */
	public static <K, V> MutableEntry<K, V> of(final K key, final V value) {
		return new MutableEntry<>(key, value);
	}

	protected K key;
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
