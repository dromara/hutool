/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.map.reference;

import org.dromara.hutool.core.lang.ref.Ref;
import org.dromara.hutool.core.lang.ref.SoftObj;
import org.dromara.hutool.core.map.concurrent.SafeConcurrentHashMap;

import java.lang.ref.ReferenceQueue;
import java.util.concurrent.ConcurrentMap;

/**
 * 线程安全的SoftMap实现<br>
 * 键和值都为Soft引用，即，在GC报告内存不足时会被GC回收
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author looly
 * @since 6.0.0
 */
public class SoftConcurrentMap<K, V> extends ReferenceConcurrentMap<K, V> {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 */
	public SoftConcurrentMap() {
		this(new SafeConcurrentHashMap<>());
	}

	/**
	 * 构造
	 *
	 * @param raw {@link ConcurrentMap}实现
	 */
	public SoftConcurrentMap(final ConcurrentMap<Ref<K>, Ref<V>> raw) {
		super(raw);
	}

	@Override
	Ref<K> wrapKey(final K key, final ReferenceQueue<? super K> queue) {
		return new SoftObj<>(key, queue);
	}

	@Override
	Ref<V> wrapValue(final V value, final ReferenceQueue<? super V> queue) {
		return new SoftObj<>(value, queue);
	}
}
