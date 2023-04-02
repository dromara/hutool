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

package org.dromara.hutool.lang.func;

import org.dromara.hutool.exceptions.UtilException;

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
	 * @throws Exception wrapped checked exceptions
	 */
	T applying(T t, T u) throws Exception;

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
		} catch (final Exception e) {
			throw new UtilException(e);
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

