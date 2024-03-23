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

import org.dromara.hutool.core.util.ObjUtil;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * 弱键
 *
 * @param <T> 键类型
 */
public class WeakObj<T> extends WeakReference<T> {
	private final int hashCode;

	/**
	 * 构造
	 *
	 * @param key   原始Key，不能为{@code null}
	 * @param queue {@link ReferenceQueue}
	 */
	public WeakObj(final T key, final ReferenceQueue<? super T> queue) {
		super(key, queue);
		hashCode = key.hashCode();
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(final Object other) {
		if (other == this) {
			return true;
		} else if (other instanceof WeakObj) {
			return ObjUtil.equals(((WeakObj<?>) other).get(), get());
		}
		return false;
	}
}
