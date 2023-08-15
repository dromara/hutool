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

package org.dromara.hutool.core.collection;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 使用给定的转换函数，转换源{@link Spliterator}为新类型的{@link Spliterator}
 *
 * @param <F> 源元素类型
 * @param <T> 目标元素类型
 * @author looly
 * @since 5.4.3
 */
public class TransSpliterator<F, T> implements Spliterator<T> {
	private final Spliterator<F> fromSpliterator;
	private final Function<? super F, ? extends T> function;

	/**
	 * 构造
	 *
	 * @param fromSpliterator 源iterator
	 * @param function 函数
	 */
	public TransSpliterator(final Spliterator<F> fromSpliterator, final Function<? super F, ? extends T> function) {
		this.fromSpliterator = fromSpliterator;
		this.function = function;
	}

	@Override
	public boolean tryAdvance(final Consumer<? super T> action) {
		return fromSpliterator.tryAdvance(
				fromElement -> action.accept(function.apply(fromElement)));
	}

	@Override
	public void forEachRemaining(final Consumer<? super T> action) {
		fromSpliterator.forEachRemaining(fromElement -> action.accept(function.apply(fromElement)));
	}

	@Override
	public Spliterator<T> trySplit() {
		final Spliterator<F> fromSplit = fromSpliterator.trySplit();
		return (fromSplit != null) ? new TransSpliterator<>(fromSplit, function) : null;
	}

	@Override
	public long estimateSize() {
		return fromSpliterator.estimateSize();
	}

	@Override
	public int characteristics() {
		return fromSpliterator.characteristics()
				& ~(Spliterator.DISTINCT | Spliterator.NONNULL | Spliterator.SORTED);
	}
}
