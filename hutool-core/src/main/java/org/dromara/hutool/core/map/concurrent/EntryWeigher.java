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
package org.dromara.hutool.core.map.concurrent;

/**
 * A class that can determine the selector of an entry. The total selector threshold
 * is used to determine when an eviction is required.
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author ben.manes@gmail.com (Ben Manes)
 * @see <a href="http://code.google.com/p/concurrentlinkedhashmap/">
 * http://code.google.com/p/concurrentlinkedhashmap/</a>
 */
public interface EntryWeigher<K, V> {

	/**
	 * Measures an entry's selector to determine how many units of capacity that
	 * the key and value consumes. An entry must consume a minimum of one unit.
	 *
	 * @param key   the key to weigh
	 * @param value the value to weigh
	 * @return the entry's selector
	 */
	int weightOf(K key, V value);
}
