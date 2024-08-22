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

import org.dromara.hutool.core.exception.ExceptionUtil;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * SerBiConsumer
 *
 * @param <T> 第一个参数类型
 * @param <U> 第二个参数类型
 * @author VampireAchao
 */
@FunctionalInterface
public interface SerBiConsumer<T, U> extends BiConsumer<T, U>, Serializable {
	/**
	 * multi
	 *
	 * @param consumers lambda
	 * @param <T>       type
	 * @param <U>       return type
	 * @return lambda
	 */
	@SafeVarargs
	static <T, U> SerBiConsumer<T, U> multi(final SerBiConsumer<T, U>... consumers) {
		return Stream.of(consumers).reduce(SerBiConsumer::andThen).orElseGet(() -> (o, q) -> {
		});
	}

	/**
	 * Performs this operation on the given arguments.
	 *
	 * @param t the first input argument
	 * @param u the second input argument
	 * @throws Exception wrapped checked exception for easy using
	 */
	void accepting(T t, U u) throws Throwable;

	/**
	 * Performs this operation on the given arguments.
	 *
	 * @param t the first input argument
	 * @param u the second input argument
	 */
	@Override
	default void accept(final T t, final U u) {
		try {
			accepting(t, u);
		} catch (final Throwable e) {
			throw ExceptionUtil.wrapRuntime(e);
		}
	}

	/**
	 * Returns a composed {@code SerBiCons} that performs, in sequence, this
	 * operation followed by the {@code after} operation. If performing either
	 * operation throws an exception, it is relayed to the caller of the
	 * composed operation.  If performing this operation throws an exception,
	 * the {@code after} operation will not be performed.
	 *
	 * @param after the operation to perform after this operation
	 * @return a composed {@code SerBiCons} that performs in sequence this
	 * operation followed by the {@code after} operation
	 * @throws NullPointerException if {@code after} is null
	 */
	default SerBiConsumer<T, U> andThen(final SerBiConsumer<? super T, ? super U> after) {
		Objects.requireNonNull(after);
		return (l, r) -> {
			accepting(l, r);
			after.accepting(l, r);
		};
	}

	/**
	 * 什么也不做，用于一些需要传入lambda的方法占位使用
	 *
	 * @param <T> 参数1类型
	 * @param <U> 参数2类型
	 * @return 什么也不做
	 */
	static <T, U> SerBiConsumer<T, U> nothing() {
		return (l, r) -> {
		};
	}
}
