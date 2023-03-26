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

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 自定义键值函数风格的Map
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author Looly
 * @since 5.8.0
 */
public class FuncMap<K, V> extends TransMap<K, V> {
	private static final long serialVersionUID = 1L;

	private final Function<Object, K> keyFunc;
	private final Function<Object, V> valueFunc;

	// ------------------------------------------------------------------------- Constructor start

	/**
	 * 构造<br>
	 * 注意提供的Map中不能有键值对，否则可能导致自定义key失效
	 *
	 * @param mapFactory  Map，提供的空map
	 * @param keyFunc   自定义KEY的函数
	 * @param valueFunc 自定义value函数
	 */
	public FuncMap(final Supplier<Map<K, V>> mapFactory, final Function<Object, K> keyFunc, final Function<Object, V> valueFunc) {
		this(mapFactory.get(), keyFunc, valueFunc);
	}

	/**
	 * 构造<br>
	 * 注意提供的Map中不能有键值对，否则可能导致自定义key失效
	 *
	 * @param emptyMap  Map，提供的空map
	 * @param keyFunc   自定义KEY的函数
	 * @param valueFunc 自定义value函数
	 */
	public FuncMap(final Map<K, V> emptyMap, final Function<Object, K> keyFunc, final Function<Object, V> valueFunc) {
		super(emptyMap);
		this.keyFunc = keyFunc;
		this.valueFunc = valueFunc;
	}
	// ------------------------------------------------------------------------- Constructor end

	/**
	 * 根据函数自定义键
	 *
	 * @param key KEY
	 * @return 驼峰Key
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected K customKey(final Object key) {
		if (null != this.keyFunc) {
			return keyFunc.apply(key);
		}
		return (K) key;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected V customValue(final Object value) {
		if (null != this.valueFunc) {
			return valueFunc.apply(value);
		}
		return (V) value;
	}
}
