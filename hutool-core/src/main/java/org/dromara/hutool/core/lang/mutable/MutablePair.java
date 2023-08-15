/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.lang.mutable;

import org.dromara.hutool.core.lang.tuple.Pair;

/**
 * 可变二元组对象
 *
 * @param <L> 左值类型
 * @param <R> 右值类型
 * @author looly
 * @since 6.0.0
 */
public class MutablePair<L, R> extends Pair<L, R> implements Mutable<MutablePair<L, R>>{
	private static final long serialVersionUID = 1L;

	/**
	 * 构建MutablePair对象
	 *
	 * @param <L>    左值类型
	 * @param <R>    右值类型
	 * @param left   左值
	 * @param right  右值
	 * @return MutablePair
	 * @since 6.0.0
	 */
	public static <L, R> MutablePair<L, R> of(final L left, final R right) {
		return new MutablePair<>(left, right);
	}

	/**
	 * 构造
	 *
	 * @param left   左值
	 * @param right  右值
	 */
	public MutablePair(final L left, final R right) {
		super(left, right);
	}

	@Override
	public MutablePair<L, R> get() {
		return this;
	}

	@Override
	public void set(final MutablePair<L, R> value) {
		this.left = value.left;
		this.right = value.right;
	}

	/**
	 * 设置左值
	 *
	 * @param left 左值
	 */
	public void setLeft(final L left) {
		this.left = left;
	}

	/**
	 * 设置右值
	 *
	 * @param right 右值
	 */
	public void setRight(final R right) {
		this.right = right;
	}
}
