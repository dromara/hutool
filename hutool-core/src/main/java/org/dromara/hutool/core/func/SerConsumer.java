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

package org.dromara.hutool.core.func;

import org.dromara.hutool.core.exception.ExceptionUtil;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 可序列化的Consumer
 *
 * @param <T> 参数类型
 * @author VampireAchao
 * @see Consumer
 */
@FunctionalInterface
public interface SerConsumer<T> extends Consumer<T>, Serializable {

	/**
	 * Performs this operation on the given argument.
	 *
	 * @param t the input argument
	 * @throws Exception wrapped checked exception
	 */
	void accepting(T t) throws Throwable;

	/**
	 * Performs this operation on the given argument.
	 *
	 * @param t the input argument
	 */
	@Override
	default void accept(final T t) {
		try {
			accepting(t);
		} catch (final Throwable e) {
			throw ExceptionUtil.wrapRuntime(e);
		}
	}

	/**
	 * multi
	 *
	 * @param consumers lambda
	 * @param <T>       type
	 * @return lambda
	 */
	@SafeVarargs
	static <T> SerConsumer<T> multi(final SerConsumer<T>... consumers) {
		return Stream.of(consumers).reduce(SerConsumer::andThen).orElseGet(() -> o -> {
		});
	}

	/**
	 * Returns a composed {@code Consumer} that performs, in sequence, this
	 * operation followed by the {@code after} operation. If performing either
	 * operation throws an exception, it is relayed to the caller of the
	 * composed operation.  If performing this operation throws an exception,
	 * the {@code after} operation will not be performed.
	 *
	 * @param after the operation to perform after this operation
	 * @return a composed {@code Consumer} that performs in sequence this
	 * operation followed by the {@code after} operation
	 * @throws NullPointerException if {@code after} is null
	 */
	default SerConsumer<T> andThen(final SerConsumer<? super T> after) {
		Objects.requireNonNull(after);
		return (final T t) -> {
			accept(t);
			after.accept(t);
		};
	}

	/**
	 * nothing
	 *
	 * @param <T> type
	 * @return nothing
	 */
	static <T> SerConsumer<T> nothing() {
		return t -> {
		};
	}
}
