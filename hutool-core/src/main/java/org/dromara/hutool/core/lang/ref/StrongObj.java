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

package org.dromara.hutool.core.lang.ref;

import org.dromara.hutool.core.util.ObjUtil;

import java.util.Objects;

/**
 * 弱引用对象，在GC时发现弱引用会回收其对象
 *
 * @param <T> 键类型
 */
public class StrongObj<T> implements Ref<T> {

	private final T obj;

	/**
	 * 构造
	 *
	 * @param obj 原始对象
	 */
	public StrongObj(final T obj) {
		this.obj = obj;
	}

	@Override
	public T get() {
		return this.obj;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(obj);
	}

	@Override
	public boolean equals(final Object other) {
		if (other == this) {
			return true;
		} else if (other instanceof StrongObj) {
			return ObjUtil.equals(((StrongObj<?>) other).get(), get());
		}
		return false;
	}
}
