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

/**
 * This package contains an implementation of a bounded
 * {@link java.util.concurrent.ConcurrentMap} data structure.
 * <p>
 * {@link org.dromara.hutool.core.map.concurrent.Weigher} is a simple interface
 * for determining how many units of capacity an entry consumes. Depending on
 * which concrete Weigher class is used, an entry may consume a different amount
 * of space within the cache. The
 * {@link org.dromara.hutool.core.map.concurrent.Weighers} class provides
 * utility methods for obtaining the most common kinds of implementations.
 * <p>
 * {@link org.dromara.hutool.core.map.concurrent.ConcurrentLinkedHashMap#listener} provides the
 * ability to be notified when an entry is evicted from the map. An eviction
 * occurs when the entry was automatically removed due to the map exceeding a
 * capacity threshold. It is not called when an entry was explicitly removed.
 * <p>
 * The {@link org.dromara.hutool.core.map.concurrent.ConcurrentLinkedHashMap}
 * class supplies an efficient, scalable, thread-safe, bounded map. As with the
 * <tt>Java Collections Framework</tt> the "Concurrent" prefix is used to
 * indicate that the map is not governed by a single exclusion lock.
 *
 * @see <a href="http://code.google.com/p/concurrentlinkedhashmap/">
 *      http://code.google.com/p/concurrentlinkedhashmap/</a>
 */
package org.dromara.hutool.core.map.concurrent;
