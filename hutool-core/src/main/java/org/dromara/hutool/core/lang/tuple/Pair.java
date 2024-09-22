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

package org.dromara.hutool.core.lang.tuple;

import org.dromara.hutool.core.exception.CloneException;

import java.io.Serializable;
import java.util.Objects;

/**
 * 不可变二元组对象
 *
 * @param <L> 左值类型
 * @param <R> 右值类型
 * @author looly
 * @since 6.0.0
 */
public class Pair<L, R> implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	/**
	 * 构建Pair对象
	 *
	 * @param <L>   左值类型
	 * @param <R>   右值类型
	 * @param left  左值
	 * @param right 右值
	 * @return Pair
	 * @since 6.0.0
	 */
	public static <L, R> Pair<L, R> of(final L left, final R right) {
		return new Pair<>(left, right);
	}

	/**
	 * 左值（第一个值）
	 */
	protected L left;
	/**
	 * 右值（第二个值）
	 */
	protected R right;

	/**
	 * 构造
	 *
	 * @param left  左值
	 * @param right 右值
	 */
	public Pair(final L left, final R right) {
		this.left = left;
		this.right = right;
	}

	// region ----- getXXX

	/**
	 * 获取左值
	 *
	 * @return 左值
	 */
	public L getLeft() {
		return this.left;
	}

	/**
	 * 获取右值
	 *
	 * @return 右值
	 */
	public R getRight() {
		return this.right;
	}
	// endregion


	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final Pair<?, ?> pair = (Pair<?, ?>) o;
		return Objects.equals(left, pair.left) && Objects.equals(right, pair.right);
	}

	@Override
	public int hashCode() {
		return Objects.hash(left, right);
	}

	@Override
	public String toString() {
		return "Pair{" +
			"left=" + left +
			", right=" + right +
			'}';
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pair<L, R> clone() {
		try {
			return (Pair<L, R>) super.clone();
		} catch (final CloneNotSupportedException e) {
			throw new CloneException(e);
		}
	}
}
