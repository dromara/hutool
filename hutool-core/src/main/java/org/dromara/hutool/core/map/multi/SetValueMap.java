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

import java.util.*;
import java.util.function.Supplier;

/**
 * 值作为集合Set（LinkedHashSet）的Map实现，通过调用putValue可以在相同key时加入多个值，多个值用集合表示
 *
 * @author looly
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @since 4.3.3
 */
public class SetValueMap<K, V> extends AbsCollValueMap<K, V> {
	private static final long serialVersionUID = 6044017508487827899L;

	// ------------------------------------------------------------------------- Constructor start

	/**
	 *  基于{@code mapFactory}创建一个值为{@link Set}的多值映射集合
	 *
	 * @param mapFactory 创建集合的工厂反方
	 */
	public SetValueMap(final Supplier<Map<K, Collection<V>>> mapFactory) {
		super(mapFactory);
	}

	/**
	 *  基于{@link HashMap}创建一个值为{@link Set}的多值映射集合
	 *
	 * @param map 提供数据的原始集合
	 */
	public SetValueMap(final Map<K, Collection<V>> map) {
		super(map);
	}

	/**
	 * 基于{@link HashMap}创建一个值为{@link Set}的多值映射集合
	 */
	public SetValueMap() {
	}

	// ------------------------------------------------------------------------- Constructor end

	@Override
	protected Set<V> createCollection() {
		return new LinkedHashSet<>(DEFAULT_COLLECTION_INITIAL_CAPACITY);
	}

}
