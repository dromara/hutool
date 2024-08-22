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

import java.util.Objects;

/**
 * 不可变三元组对象
 *
 * @param <L> 左值类型
 * @param <M> 中值类型
 * @param <R> 右值类型
 * @author kirno7
 * @since 6.0.0
 */
public class Triple<L, M, R> extends Pair<L, R> {
	private static final long serialVersionUID = 1L;

	/**
	 * 构建Triple对象
	 *
	 * @param <L>    左值类型
	 * @param <M>    中值类型
	 * @param <R>    右值类型
	 * @param left   左值
	 * @param middle 中值
	 * @param right  右值
	 * @return Triple
	 * @since 6.0.0
	 */
	public static <L, M, R> Triple<L, M, R> of(final L left, final M middle, final R right) {
		return new Triple<>(left, middle, right);
	}

	/**
	 * 中值
	 */
	protected M middle;

	/**
	 * 构造
	 *
	 * @param left   左值
	 * @param middle 中值
	 * @param right  右值
	 */
	public Triple(final L left, final M middle, final R right) {
		super(left, right);
		this.middle = middle;
	}

	// region ----- getXXX

	/**
	 * 获取中值
	 *
	 * @return 中值
	 */
	public M getMiddle() {
		return this.middle;
	}
	// endregion

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o instanceof Triple) {
			final Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
			return Objects.equals(getLeft(), triple.getLeft()) &&
				Objects.equals(getMiddle(), triple.getMiddle()) &&
				Objects.equals(getRight(), triple.getRight());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.left, this.middle, this.right);
	}

	@Override
	public String toString() {
		return "Triple{" + "left=" + getLeft() + ", middle=" + getMiddle() + ", right=" + getRight() + '}';
	}

	@Override
	public Triple<L, M, R> clone() {
		return (Triple<L, M, R>) super.clone();
	}
}
