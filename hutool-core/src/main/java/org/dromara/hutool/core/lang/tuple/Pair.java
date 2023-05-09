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

import org.dromara.hutool.core.exception.CloneException;

import java.io.Serializable;
import java.util.Objects;

/**
 * 不可变二元组对象
 *
 * @param <L> 左值类型
 * @param <R> 右值类型
 * @author liuchao
 * @since 6.0.0
 */
public class Pair<L, R> implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	protected L left;
	protected R right;

	/**
	 * 构建Pair 对象
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
	 * 构造
	 *
	 * @param left  左值
	 * @param right 有值
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof Pair) {
			final Pair<?, ?> pair = (Pair<?, ?>) o;
			return Objects.equals(getLeft(), pair.getLeft()) && Objects.equals(getRight(), pair.getRight());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getLeft()) ^ Objects.hashCode(getRight());
	}

	@Override
	public String toString() {
		return "Pair{" + "left=" + getLeft() + ", right=" + getRight() + '}';
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
