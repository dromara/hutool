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

package org.dromara.hutool.core.map;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.wrapper.Wrapper;
import org.dromara.hutool.core.util.ObjUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Map包装类，通过包装一个已有Map实现特定功能。例如自定义Key的规则或Value规则
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author looly
 * @since 4.3.3
 */
public class MapWrapper<K, V> implements Map<K, V>, Iterable<Map.Entry<K, V>>, Wrapper<Map<K, V>>, Serializable, Cloneable {
	private static final long serialVersionUID = -7524578042008586382L;

	/**
	 * 默认增长因子
	 */
	protected static final float DEFAULT_LOAD_FACTOR = 0.75f;
	/**
	 * 默认初始大小
	 */
	protected static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
	/**
	 * 原始集合
	 */
	private Map<K, V> raw;

	/**
	 * 构造<br>
	 * 通过传入一个Map从而确定Map的类型，子类需创建一个空的Map，而非传入一个已有Map，否则值可能会被修改
	 *
	 * @param mapFactory 空Map创建工厂
	 * @since 5.8.0
	 */
	public MapWrapper(final Supplier<Map<K, V>> mapFactory) {
		this(mapFactory.get());
	}

	/**
	 * 构造
	 *
	 * @param raw 被包装的Map，不允许为{@code null}
	 * @throws NullPointerException 当被包装的集合为{@code null}时抛出
	 */
	public MapWrapper(final Map<K, V> raw) {
		Assert.notNull(raw, "raw must not null");
		this.raw = raw;
	}

	/**
	 * 获取原始的Map
	 *
	 * @return Map
	 */
	@Override
	public Map<K, V> getRaw() {
		return this.raw;
	}

	@Override
	public int size() {
		return raw.size();
	}

	@Override
	public boolean isEmpty() {
		return raw.isEmpty();
	}

	@Override
	public boolean containsKey(final Object key) {
		return raw.containsKey(key);
	}

	@Override
	public boolean containsValue(final Object value) {
		return raw.containsValue(value);
	}

	@Override
	public V get(final Object key) {
		return raw.get(key);
	}

	@Override
	public V put(final K key, final V value) {
		return raw.put(key, value);
	}

	@Override
	public V remove(final Object key) {
		return raw.remove(key);
	}

	@Override
	public void putAll(final Map<? extends K, ? extends V> m) {
		raw.putAll(m);
	}

	@Override
	public void clear() {
		raw.clear();
	}

	@Override
	public Collection<V> values() {
		return raw.values();
	}

	@Override
	public Set<K> keySet() {
		return raw.keySet();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return raw.entrySet();
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return this.entrySet().iterator();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final MapWrapper<?, ?> that = (MapWrapper<?, ?>) o;
		return Objects.equals(raw, that.raw);
	}

	@Override
	public int hashCode() {
		return Objects.hash(raw);
	}

	@Override
	public String toString() {
		return raw.toString();
	}


	@Override
	public void forEach(final BiConsumer<? super K, ? super V> action) {
		raw.forEach(action);
	}

	@Override
	public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function) {
		raw.replaceAll(function);
	}

	@Override
	public V putIfAbsent(final K key, final V value) {
		return raw.putIfAbsent(key, value);
	}

	@Override
	public boolean remove(final Object key, final Object value) {
		return raw.remove(key, value);
	}

	@Override
	public boolean replace(final K key, final V oldValue, final V newValue) {
		return raw.replace(key, oldValue, newValue);
	}

	@Override
	public V replace(final K key, final V value) {
		return raw.replace(key, value);
	}

	@Override
	public V computeIfAbsent(final K key, final Function<? super K, ? extends V> mappingFunction) {
		return raw.computeIfAbsent(key, mappingFunction);
	}

	// 重写默认方法的意义在于，如果被包装的Map自定义了这些默认方法，包装类就可以保持这些行为的一致性
	//---------------------------------------------------------------------------- Override default methods start
	@Override
	public V getOrDefault(final Object key, final V defaultValue) {
		return raw.getOrDefault(key, defaultValue);
	}

	@Override
	public V computeIfPresent(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return raw.computeIfPresent(key, remappingFunction);
	}

	@Override
	public V compute(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return raw.compute(key, remappingFunction);
	}

	@Override
	public V merge(final K key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		return raw.merge(key, value, remappingFunction);
	}

	@Override
	public MapWrapper<K, V> clone() throws CloneNotSupportedException {
		@SuppressWarnings("unchecked") final MapWrapper<K, V> clone = (MapWrapper<K, V>) super.clone();
		clone.raw = ObjUtil.clone(raw);
		return clone;
	}

	//---------------------------------------------------------------------------- Override default methods end

	// region 序列化与反序列化重写
	private void writeObject(final ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeObject(this.raw);
	}

	@SuppressWarnings("unchecked")
	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		raw = (Map<K, V>) in.readObject();
	}
	// endregion
}
