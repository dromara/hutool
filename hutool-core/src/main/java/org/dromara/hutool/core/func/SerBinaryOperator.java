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
import java.util.Comparator;
import java.util.Objects;
import java.util.function.BinaryOperator;

/**
 * SerBinaryOperator
 *
 * @param <T> 参数和返回值类型
 * @author VampireAchao
 * @see BinaryOperator
 */
@FunctionalInterface
public interface SerBinaryOperator<T> extends BinaryOperator<T>, Serializable {

	/**
	 * Applies this function to the given arguments.
	 *
	 * @param t the first function argument
	 * @param u the second function argument
	 * @return the function result
	 * @throws Exception wrapped checked exception
	 */
	T applying(T t, T u) throws Throwable;

	/**
	 * Applies this function to the given arguments.
	 *
	 * @param t the first function argument
	 * @param u the second function argument
	 * @return the function result
	 */
	@Override
	default T apply(final T t, final T u) {
		try {
			return this.applying(t, u);
		} catch (final Throwable e) {
			throw ExceptionUtil.wrapRuntime(e);
		}
	}

	/**
	 * Returns a {@code SerBinaryOperator} which returns the lesser of two elements
	 * according to the specified {@code Comparator}.
	 *
	 * @param <T>        the type of the input arguments of the comparator
	 * @param comparator a {@code Comparator} for comparing the two values
	 * @return a {@code SerBiUnOp} which returns the lesser of its operands,
	 * according to the supplied {@code Comparator}
	 * @throws NullPointerException if the argument is null
	 */
	static <T> SerBinaryOperator<T> minBy(final Comparator<? super T> comparator) {
		Objects.requireNonNull(comparator);
		return (a, b) -> comparator.compare(a, b) <= 0 ? a : b;
	}

	/**
	 * Returns a {@code SerBinaryOperator} which returns the greater of two elements
	 * according to the specified {@code Comparator}.
	 *
	 * @param <T>        the type of the input arguments of the comparator
	 * @param comparator a {@code Comparator} for comparing the two values
	 * @return a {@code SerBiUnOp} which returns the greater of its operands,
	 * according to the supplied {@code Comparator}
	 * @throws NullPointerException if the argument is null
	 */
	static <T> SerBinaryOperator<T> maxBy(final Comparator<? super T> comparator) {
		Objects.requireNonNull(comparator);
		return (a, b) -> comparator.compare(a, b) >= 0 ? a : b;
	}

	/**
	 * just before
	 *
	 * @param <T> type
	 * @return before
	 */
	static <T> SerBinaryOperator<T> justBefore() {
		return (l, r) -> l;
	}

	/**
	 * just after
	 *
	 * @param <T> type
	 * @return after
	 */
	static <T> SerBinaryOperator<T> justAfter() {
		return (l, r) -> r;
	}
}

