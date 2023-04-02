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
