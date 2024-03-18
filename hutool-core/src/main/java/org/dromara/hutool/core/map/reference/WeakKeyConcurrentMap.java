/*
 * Copyright (c) 2023-2024. looly(loolly@aliyun.com)
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

import org.dromara.hutool.core.map.concurrent.SafeConcurrentHashMap;
import org.dromara.hutool.core.util.ReferenceUtil;

import java.lang.ref.Reference;
import java.util.concurrent.ConcurrentMap;

/**
 * 线程安全的WeakMap实现<br>
 * 参考：jdk.management.resource.internal.WeakKeyConcurrentHashMap
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author looly
 * @since 5.8.0
 */
public class WeakKeyConcurrentMap<K, V> extends ReferenceKeyConcurrentMap<K, V> {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 */
	public WeakKeyConcurrentMap() {
		this(new SafeConcurrentHashMap<>());
	}

	/**
	 * 构造
	 *
	 * @param raw {@link ConcurrentMap}实现
	 */
	public WeakKeyConcurrentMap(final ConcurrentMap<Reference<K>, V> raw) {
		super(raw, ReferenceUtil.ReferenceType.WEAK);
	}
}
