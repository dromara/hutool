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
 * @param <T> 流元素类型
 * @author VampireAchao
 * @since 6.0.0
 */
public class IterateSpliterator<T> extends Spliterators.AbstractSpliterator<T> {

	/**
	 * @param seed    初始值
	 * @param hasNext 是否有下一个断言
	 * @param next    下一个值生产者
	 * @param <T>     流元素类型
	 * @return IterateSpliterator
	 */
	public static <T> IterateSpliterator<T> of(final T seed, final Predicate<? super T> hasNext, final UnaryOperator<T> next) {
		return new IterateSpliterator<>(seed, hasNext, next);
	}

	private final T seed;
	private final Predicate<? super T> hasNext;
	private final UnaryOperator<T> next;
	private T prev;
	private boolean started;
	private boolean finished;

	/**
	 * 构造
	 *
	 * @param seed    初始值
	 * @param hasNext 是否有下一个断言
	 * @param next    下一个值生产者
	 */
	public IterateSpliterator(final T seed, final Predicate<? super T> hasNext, final UnaryOperator<T> next) {
		super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.IMMUTABLE);
		this.seed = seed;
		this.hasNext = hasNext;
		this.next = next;
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
