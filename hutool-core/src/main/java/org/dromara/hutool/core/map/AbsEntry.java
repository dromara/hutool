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

package org.dromara.hutool.core.map;

import org.dromara.hutool.core.util.ObjUtil;

import java.util.Map;

/**
 * 抽象的{@link Map.Entry}实现，来自Guava<br>
 * 实现了默认的{@link #equals(Object)}、{@link #hashCode()}、{@link #toString()}方法。<br>
 * 默认{@link #setValue(Object)}抛出异常。
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author Guava
 * @since 5.7.23
 */
public abstract class AbsEntry<K, V> implements Map.Entry<K, V> {

	@Override
	public V setValue(final V value) {
		throw new UnsupportedOperationException("Entry is read only.");
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof Map.Entry) {
			final Map.Entry<?, ?> that = (Map.Entry<?, ?>) object;
			return ObjUtil.equals(this.getKey(), that.getKey())
					&& ObjUtil.equals(this.getValue(), that.getValue());
		}
		return false;
	}

	@Override
	public int hashCode() {
		//copy from 1.8 HashMap.Node
		final K k = getKey();
		final V v = getValue();
		return ((k == null) ? 0 : k.hashCode()) ^ ((v == null) ? 0 : v.hashCode());
	}

	@Override
	public String toString() {
		return getKey() + "=" + getValue();
	}
}
