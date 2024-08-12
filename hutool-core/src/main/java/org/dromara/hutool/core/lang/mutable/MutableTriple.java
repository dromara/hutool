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

import org.dromara.hutool.core.lang.tuple.Triple;

/**
 * 可变三元组对象
 *
 * @param <L> 左值类型
 * @param <M> 中值类型
 * @param <R> 右值类型
 * @author kirno7
 * @since 6.0.0
 */
public class MutableTriple<L, M, R> extends Triple<L, M, R> implements Mutable<MutableTriple<L, M, R>>{
	private static final long serialVersionUID = 1L;

	/**
	 * 构建MutableTriple对象
	 *
	 * @param <L>    左值类型
	 * @param <M>    中值类型
	 * @param <R>    右值类型
	 * @param left   左值
	 * @param middle 中值
	 * @param right  右值
	 * @return MutableTriple
	 * @since 6.0.0
	 */
	public static <L, M, R> MutableTriple<L, M, R> of(final L left, final M middle, final R right) {
		return new MutableTriple<>(left, middle, right);
	}

	/**
	 * 构造
	 *
	 * @param left   左值
	 * @param middle 中值
	 * @param right  右值
	 */
	public MutableTriple(final L left, final M middle, final R right) {
		super(left, middle, right);
	}

	@Override
	public MutableTriple<L, M, R> get() {
		return this;
	}

	@Override
	public void set(final MutableTriple<L, M, R> value) {
		this.left = value.left;
		this.middle = value.middle;
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
	 * 设置中值
	 *
	 * @param middle 中值
	 */
	public void setMiddle(final M middle) {
		this.middle = middle;
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
