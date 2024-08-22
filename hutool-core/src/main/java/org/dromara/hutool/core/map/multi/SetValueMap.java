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
