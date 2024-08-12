/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.core.lang.mutable;

import org.dromara.hutool.core.util.ObjUtil;

import java.io.Serializable;
import java.util.Objects;

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
		return Objects.hashCode(value);
	}

	// -----------------------------------------------------------------------
	@Override
	public String toString() {
		return value == null ? "null" : value.toString();
	}

}
