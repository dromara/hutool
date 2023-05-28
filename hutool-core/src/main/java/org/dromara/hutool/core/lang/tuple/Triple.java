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

	protected M middle;

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
