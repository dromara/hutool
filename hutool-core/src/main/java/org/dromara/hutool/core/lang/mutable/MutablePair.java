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
