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

package org.dromara.hutool.core.cache;

/**
 * 缓存监听，用于实现缓存操作时的回调监听，例如缓存对象的移除事件等
 *
 * @param <K> 缓存键
 * @param <V> 缓存值
 * @author looly
 * @since 5.5.2
 */
public interface CacheListener<K, V> {

	/**
	 * 对象移除回调
	 *
	 * @param key          键
	 * @param cachedObject 被缓存的对象
	 */
	void onRemove(K key, V cachedObject);
}
