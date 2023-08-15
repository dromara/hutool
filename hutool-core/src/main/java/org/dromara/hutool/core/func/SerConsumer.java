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

package org.dromara.hutool.core.func;

import org.dromara.hutool.core.exception.ExceptionUtil;
import org.dromara.hutool.core.exception.HutoolException;

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
	void accepting(T t) throws Exception;

	/**
	 * Performs this operation on the given argument.
	 *
	 * @param t the input argument
	 */
	@Override
	default void accept(final T t) {
		try {
			accepting(t);
		} catch (final Exception e) {
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
