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

package org.dromara.hutool.core.func;

import org.dromara.hutool.core.lang.Assert;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 两个函数的叠加函数. 叠加 {@code f: A->B} 和 {@code g: B->C}，效果等同于：{@code h(a) == g(f(a))}
 *
 * @param <A> 第一个函数的传入参数类型
 * @param <B> 第一个函数的返回类型（第二个函数有的参数类型）
 * @param <C> 最终结果类型
 * @author Guava
 * @since 6.0.0
 */
public class ComposeFunction<A, B, C> implements Function<A, C>, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 两个函数的叠加函数. 叠加 {@code f: A->B} 和 {@code g: B->C}，效果等同于：{@code h(a) == g(f(a))}
	 *
	 * @param g   第二个函数
	 * @param f   第一个函数
	 * @param <A> 第一个函数的传入参数类型
	 * @param <B> 第一个函数的返回类型（第二个函数有的参数类型）
	 * @param <C> 最终结果类型
	 * @return 叠加函数
	 */
	public static <A, B, C> ComposeFunction<A, B, C> of(final Function<B, C> g, final Function<A, ? extends B> f) {
		return new ComposeFunction<>(g, f);
	}

	private final Function<B, C> g;
	private final Function<A, ? extends B> f;

	/**
	 * 构造
	 * @param g 函数1
	 * @param f 函数2
	 */
	public ComposeFunction(final Function<B, C> g, final Function<A, ? extends B> f) {
		this.g = Assert.notNull(g);
		this.f = Assert.notNull(f);
	}

	@Override
	public C apply(final A a) {
		return g.apply(f.apply(a));
	}
}
