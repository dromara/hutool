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
