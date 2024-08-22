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

package org.dromara.hutool.core.cache.impl;

import org.dromara.hutool.core.cache.CacheListener;
import org.dromara.hutool.core.lang.Opt;
import org.dromara.hutool.core.lang.mutable.Mutable;
import org.dromara.hutool.core.lang.ref.Ref;
import org.dromara.hutool.core.map.reference.WeakConcurrentMap;

/**
 * 弱引用缓存<br>
 * 对于一个给定的键，其映射的存在并不阻止垃圾回收器对该键的丢弃，这就使该键成为可终止的，被终止，然后被回收。<br>
 * 丢弃某个键时，其条目从映射中有效地移除。<br>
 *
 * @author Looly
 *
 * @param <K> 键
 * @param <V> 值
 * @author looly
 * @since 3.0.7
 */
public class WeakCache<K, V> extends TimedCache<K, V>{
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 * @param timeout 超时时常，单位毫秒，-1或0表示无限制
	 */
	public WeakCache(final long timeout) {
		super(timeout, new WeakConcurrentMap<>());
	}

	@Override
	public WeakCache<K, V> setListener(final CacheListener<K, V> listener) {
		super.setListener(listener);

		final WeakConcurrentMap<Mutable<K>, CacheObj<K, V>> map = (WeakConcurrentMap<Mutable<K>, CacheObj<K, V>>) this.cacheMap;
		// WeakKey回收之后，key对应的值已经是null了，因此此处的key也为null
		map.setPurgeListener((key, value)-> listener.onRemove(
			Opt.ofNullable(key).map(Ref::get).map(Mutable::get).getOrNull(),
			Opt.ofNullable(value).map(Ref::get).map(CacheObj::getValue).getOrNull()));

		return this;
	}
}
