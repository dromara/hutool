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

package org.dromara.hutool.core.lang.mutable;

import org.dromara.hutool.core.util.ObjUtil;

import java.io.Serializable;

/**
 * 可变{@code Object}
 *
 * @param <T> 可变的类型
 * @since 3.0.1
 */
public class MutableObj<T> implements Mutable<T>, Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * 构建MutableObj
	 * @param value 被包装的值
	 * @param <T> 值类型
	 * @return MutableObj
	 * @since 5.8.0
	 */
	public static <T> MutableObj<T> of(final T value){
		return new MutableObj<>(value);
	}

	private T value;

	/**
	 * 构造，空值
	 */
	public MutableObj() {
	}

	/**
	 * 构造
	 *
	 * @param value 值
	 */
	public MutableObj(final T value) {
		this.value = value;
	}

	// -----------------------------------------------------------------------
	@Override
	public T get() {
		return this.value;
	}

	@Override
	public void set(final T value) {
		this.value = value;
	}

	// -----------------------------------------------------------------------
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (this.getClass() == obj.getClass()) {
			final MutableObj<?> that = (MutableObj<?>) obj;
			return ObjUtil.equals(this.value, that.value);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return value == null ? 0 : value.hashCode();
	}

	// -----------------------------------------------------------------------
	@Override
	public String toString() {
		return value == null ? "null" : value.toString();
	}

}
