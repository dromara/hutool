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

package org.dromara.hutool.core.map.multi;

import org.dromara.hutool.core.func.SerSupplier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * <p>{@link MultiValueMap}的通用实现，可视为值为{@link Collection}集合的{@link Map}集合。<br>
 * 构建时指定一个工厂方法用于生成原始的{@link Map}集合，然后再指定一个工厂方法用于生成自定义类型的值集合。<br>
 * 当调用{@link MultiValueMap}中格式为“putXXX”的方法时，将会为key创建值集合，并将key相同的值追加到集合中
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author looly
 * @since 4.3.3
 */
public class CollectionValueMap<K, V> extends AbsCollValueMap<K, V> {

	private static final long serialVersionUID = 9012989578038102983L;

	private final SerSupplier<Collection<V>> collFactory;

	// ------------------------------------------------------------------------- Constructor start

	/**
	 * 创建一个多值映射集合，基于{@code mapFactory}与{@code collFactory}实现
	 *
	 * @param mapFactory  生成集合的工厂方法
	 * @param collFactory 生成值集合的工厂方法
	 */
	public CollectionValueMap(final Supplier<Map<K, Collection<V>>> mapFactory, final SerSupplier<Collection<V>> collFactory) {
		super(mapFactory);
		this.collFactory = collFactory;
	}

	/**
	 * 创建一个多值映射集合，默认基于{@link HashMap}与{@code collFactory}生成的集合实现
	 *
	 * @param collFactory 生成值集合的工厂方法
	 */
	public CollectionValueMap(final SerSupplier<Collection<V>> collFactory) {
		this.collFactory = collFactory;
	}

	/**
	 * 创建一个多值映射集合，默认基于{@link HashMap}与{@link ArrayList}实现
	 */
	public CollectionValueMap() {
		this.collFactory = ArrayList::new;
	}

	/**
	 * 创建一个多值映射集合，默认基于指定Map与指定List类型实现
	 *
	 * @param map 提供数据的原始集合
	 */
	public CollectionValueMap(final Map<K, Collection<V>> map) {
		super(map);
		this.collFactory = ArrayList::new;
	}

	// ------------------------------------------------------------------------- Constructor end

	@Override
	protected Collection<V> createCollection() {
		return collFactory.get();
	}
}
