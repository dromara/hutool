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

import org.dromara.hutool.core.exceptions.CloneException;

import java.io.Serializable;
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
public class Triple<L, M, R> implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	protected L left;
	protected M middle;
	protected R right;

	/**
	 * 构建ImmutableTriple对象
	 *
	 * @param <L>    左值类型
	 * @param <M>    中值类型
	 * @param <R>    右值类型
	 * @param left   左值
	 * @param middle 中值
	 * @param right  右值
	 * @return ImmutableTriple
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
		this.left = left;
		this.middle = middle;
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
	 * 获取中值
	 *
	 * @return 中值
	 */
	public M getMiddle() {
		return this.middle;
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
		return Objects.hashCode(getLeft())
			^ Objects.hashCode(getMiddle())
			^ Objects.hashCode(getRight());
	}

	@Override
	public String toString() {
		return "Triple {" + "left=" + getLeft() + ", middle=" + getMiddle() + ", right=" + getRight() + '}';
	}

	@SuppressWarnings("unchecked")
	@Override
	public Triple<L, M, R> clone() {
		try {
			return (Triple<L, M, R>) super.clone();
		} catch (final CloneNotSupportedException e) {
			throw new CloneException(e);
		}
	}
}
