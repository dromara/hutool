/*
 * Copyright (c) 2013-2024 Hutool Team.
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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 忽略大小写的LinkedHashMap<br>
 * 对KEY忽略大小写，get("Value")和get("value")获得的值相同，put进入的值也会被覆盖
 *
 * @author Looly
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @since 3.3.1
 */
public class CaseInsensitiveLinkedMap<K, V> extends CaseInsensitiveMap<K, V> {
	private static final long serialVersionUID = 4043263744224569870L;

	// ------------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 */
	public CaseInsensitiveLinkedMap() {
		this(DEFAULT_INITIAL_CAPACITY);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始大小
	 */
	public CaseInsensitiveLinkedMap(final int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * 构造
	 *
	 * @param m Map
	 */
	public CaseInsensitiveLinkedMap(final Map<? extends K, ? extends V> m) {
		this(DEFAULT_LOAD_FACTOR, m);
	}

	/**
	 * 构造
	 *
	 * @param loadFactor 加载因子
	 * @param m Map
	 * @since 3.1.2
	 */
	public CaseInsensitiveLinkedMap(final float loadFactor, final Map<? extends K, ? extends V> m) {
		this(m.size(), loadFactor);
		this.putAll(m);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始大小
	 * @param loadFactor 加载因子
	 */
	public CaseInsensitiveLinkedMap(final int initialCapacity, final float loadFactor) {
		super(MapBuilder.of(new LinkedHashMap<>(initialCapacity, loadFactor)));
	}
	// ------------------------------------------------------------------------- Constructor end
}
