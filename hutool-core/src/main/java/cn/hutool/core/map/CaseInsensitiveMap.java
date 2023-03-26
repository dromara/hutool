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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 忽略大小写的Map<br>
 * 对KEY忽略大小写，get("Value")和get("value")获得的值相同，put进入的值也会被覆盖
 *
 * @author Looly
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @since 3.0.2
 */
public class CaseInsensitiveMap<K, V> extends FuncKeyMap<K, V> {
	private static final long serialVersionUID = 4043263744224569870L;

	//------------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 */
	public CaseInsensitiveMap() {
		this(DEFAULT_INITIAL_CAPACITY);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始大小
	 */
	public CaseInsensitiveMap(final int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * 构造<br>
	 * 注意此构造将传入的Map作为被包装的Map，针对任何修改，传入的Map都会被同样修改。
	 *
	 * @param m 被包装的自定义Map创建器
	 */
	public CaseInsensitiveMap(final Map<? extends K, ? extends V> m) {
		this(DEFAULT_LOAD_FACTOR, m);
	}

	/**
	 * 构造
	 *
	 * @param loadFactor 加载因子
	 * @param m Map
	 * @since 3.1.2
	 */
	public CaseInsensitiveMap(final float loadFactor, final Map<? extends K, ? extends V> m) {
		this(m.size(), loadFactor);
		this.putAll(m);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始大小
	 * @param loadFactor 加载因子
	 */
	public CaseInsensitiveMap(final int initialCapacity, final float loadFactor) {
		this(MapBuilder.of(new HashMap<>(initialCapacity, loadFactor)));
	}

	/**
	 * 构造<br>
	 * 注意此构造将传入的Map作为被包装的Map，针对任何修改，传入的Map都会被同样修改。
	 *
	 * @param emptyMapBuilder 被包装的自定义Map创建器
	 */
	@SuppressWarnings("unchecked")
	CaseInsensitiveMap(final MapBuilder<K, V> emptyMapBuilder) {
		// issue#I5VRHW@Gitee 使Function可以被序列化
		super(emptyMapBuilder.build(), (Function<Object, K> & Serializable)(key)->{
			if (key instanceof CharSequence) {
				key = key.toString().toLowerCase();
			}
			return (K) key;
		});
	}
	//------------------------------------------------------------------------- Constructor end
}
