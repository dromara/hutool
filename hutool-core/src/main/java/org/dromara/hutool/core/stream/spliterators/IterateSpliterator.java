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

package org.dromara.hutool.core.stream.spliterators;

import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * 无限有序流 的Spliterator
 *
 * @author VampireAchao
 * @since 6.0.0
 */
public class IterateSpliterator<T> extends Spliterators.AbstractSpliterator<T> {
	private final T seed;
	private final Predicate<? super T> hasNext;
	private final UnaryOperator<T> next;
	private T prev;
	private boolean started;
	private boolean finished;

	/**
	 * Creates a spliterator reporting the given estimated size and
	 * additionalCharacteristics.
	 */
	IterateSpliterator(final T seed, final Predicate<? super T> hasNext, final UnaryOperator<T> next) {
		super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.IMMUTABLE);
		this.seed = seed;
		this.hasNext = hasNext;
		this.next = next;
	}

	public static <T> IterateSpliterator<T> create(final T seed, final Predicate<? super T> hasNext, final UnaryOperator<T> next) {
		return new IterateSpliterator<>(seed, hasNext, next);
	}

	@Override
	public boolean tryAdvance(final Consumer<? super T> action) {
		Objects.requireNonNull(action);
		if (finished) {
			return false;
		}
		final T t;
		if (started) {
			t = next.apply(prev);
		} else {
			t = seed;
			started = true;
		}
		if (!hasNext.test(t)) {
			prev = null;
			finished = true;
			return false;
		}
		prev = t;
		action.accept(prev);
		return true;
	}

	@Override
	public void forEachRemaining(final Consumer<? super T> action) {
		Objects.requireNonNull(action);
		if (finished) {
			return;
		}
		finished = true;
		T t = started ? next.apply(prev) : seed;
		prev = null;
		while (hasNext.test(t)) {
			action.accept(t);
			t = next.apply(t);
		}
	}
}
