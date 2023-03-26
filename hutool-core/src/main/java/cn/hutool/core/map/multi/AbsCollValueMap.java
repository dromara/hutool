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

package cn.hutool.core.map.multi;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapWrapper;
import cn.hutool.core.util.ObjUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * {@link MultiValueMap}的基本实现
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author looly
 * @since 5.7.4
 * @see CollectionValueMap
 * @see SetValueMap
 * @see ListValueMap
 */
public abstract class AbsCollValueMap<K, V> extends MapWrapper<K, Collection<V>> implements MultiValueMap<K, V> {
	private static final long serialVersionUID = 1L;

	/**
	 * 默认集合初始大小
	 */
	protected static final int DEFAULT_COLLECTION_INITIAL_CAPACITY = 3;

	// ------------------------------------------------------------------------- Constructor start

	/**
	 * 使用{@code mapFactory}创建的集合构造一个多值映射Map集合
	 *
	 * @param mapFactory 生成集合的工厂方法
	 */
	protected AbsCollValueMap(final Supplier<Map<K, Collection<V>>> mapFactory) {
		super(mapFactory);
	}

	/**
	 * 基于{@link HashMap}构造一个多值映射集合
	 *
	 * @param map 提供初始数据的集合
	 */
	protected AbsCollValueMap(final Map<K, Collection<V>> map) {
		super(new HashMap<>(map));
	}

	/**
	 * 基于{@link HashMap}构造一个多值映射集合
	 */
	protected AbsCollValueMap() {
		super(new HashMap<>(16));
	}

	// ------------------------------------------------------------------------- Constructor end

	/**
	 * 将集合中的全部元素对追加到指定键对应的值集合中，效果等同于：
	 * <pre>{@code
	 * coll.forEach(t -> map.putValue(key, t))
	 * }</pre>
	 *
	 * @param key  键
	 * @param coll 待添加的值集合
	 * @return 是否成功添加
	 */
	@Override
	public boolean putAllValues(final K key, final Collection<V> coll) {
		if (ObjUtil.isNull(coll)) {
			return false;
		}
		return super.computeIfAbsent(key, k -> createCollection())
			.addAll(coll);
	}

	/**
	 * 向指定键对应的值集合追加值，效果等同于：
	 * <pre>{@code
	 * map.computeIfAbsent(key, k -> new Collection()).add(value)
	 * }</pre>
	 *
	 * @param key   键
	 * @param value 值
	 * @return 是否成功添加
	 */
	@Override
	public boolean putValue(final K key, final V value) {
		return super.computeIfAbsent(key, k -> createCollection())
			.add(value);
	}

	/**
	 * 将值从指定键下的值集合中删除
	 *
	 * @param key   键
	 * @param value 值
	 * @return 是否成功删除
	 */
	@Override
	public boolean removeValue(final K key, final V value) {
		return Opt.ofNullable(super.get(key))
			.map(t -> t.remove(value))
			.orElse(false);
	}

	/**
	 * 将一批值从指定键下的值集合中删除
	 *
	 * @param key   键
	 * @param values 值
	 * @return 是否成功删除
	 */
	@Override
	public boolean removeAllValues(final K key, final Collection<V> values) {
		if (CollUtil.isEmpty(values)) {
			return false;
		}
		final Collection<V> coll = get(key);
		return ObjUtil.isNotNull(coll) && coll.removeAll(values);
	}

	/**
	 * 根据条件过滤所有值集合中的值，并以新值生成新的值集合，新集合中的值集合类型与当前实例的默认值集合类型保持一致
	 *
	 * @param filter 判断方法
	 * @return 当前实例
	 */
	@Override
	public MultiValueMap<K, V> filterAllValues(final BiPredicate<K, V> filter) {
		entrySet().forEach(e -> {
			final K k = e.getKey();
			final Collection<V> coll = e.getValue().stream()
				.filter(v -> filter.test(k, v))
				.collect(Collectors.toCollection(this::createCollection));
			e.setValue(coll);
		});
		return this;
	}

	/**
	 * 根据条件替换所有值集合中的值，并以新值生成新的值集合，新集合中的值集合类型与当前实例的默认值集合类型保持一致
	 *
	 * @param operate 替换方法
	 * @return 当前实例
	 */
	@Override
	public MultiValueMap<K, V> replaceAllValues(final BiFunction<K, V, V> operate) {
		entrySet().forEach(e -> {
			final K k = e.getKey();
			final Collection<V> coll = e.getValue().stream()
				.map(v -> operate.apply(k, v))
				.collect(Collectors.toCollection(this::createCollection));
			e.setValue(coll);
		});
		return this;
	}

	/**
	 * 创建集合<br>
	 * 此方法用于创建在putValue后追加值所在的集合，子类实现此方法创建不同类型的集合
	 *
	 * @return {@link Collection}
	 */
	protected abstract Collection<V> createCollection();

}
