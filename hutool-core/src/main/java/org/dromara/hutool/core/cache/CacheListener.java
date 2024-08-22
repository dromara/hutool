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
