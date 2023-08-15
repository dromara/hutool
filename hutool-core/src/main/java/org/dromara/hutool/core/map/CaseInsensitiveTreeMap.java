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

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 忽略大小写的{@link TreeMap}<br>
 * 对KEY忽略大小写，get("Value")和get("value")获得的值相同，put进入的值也会被覆盖
 *
 * @author Looly
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @since 3.3.1
 */
public class CaseInsensitiveTreeMap<K, V> extends CaseInsensitiveMap<K, V> {
	private static final long serialVersionUID = 4043263744224569870L;

	// ------------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 */
	public CaseInsensitiveTreeMap() {
		this((Comparator<? super K>) null);
	}

	/**
	 * 构造
	 *
	 * @param m Map
	 * @since 3.1.2
	 */
	public CaseInsensitiveTreeMap(final Map<? extends K, ? extends V> m) {
		this();
		this.putAll(m);
	}

	/**
	 * 构造
	 *
	 * @param m Map，初始Map，键值对会被复制到新的TreeMap中
	 * @since 3.1.2
	 */
	public CaseInsensitiveTreeMap(final SortedMap<? extends K, ? extends V> m) {
		super(new TreeMap<K, V>(m));
	}

	/**
	 * 构造
	 *
	 * @param comparator 比较器，{@code null}表示使用默认比较器
	 */
	public CaseInsensitiveTreeMap(final Comparator<? super K> comparator) {
		super(new TreeMap<>(comparator));
	}
	// ------------------------------------------------------------------------- Constructor end
}
