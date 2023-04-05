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

import org.dromara.hutool.core.exceptions.UtilException;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * 可序列化的BiPredicate
 *
 * @param <T> 参数1的类型
 * @param <U> 参数2的类型
 * @author VampireAchao
 * @since 6.0.0
 */
@FunctionalInterface
public interface SerBiPredicate<T, U> extends BiPredicate<T, U>, Serializable {


	/**
	 * Evaluates this predicate on the given arguments.
	 *
	 * @param t the first input argument
	 * @param u the second input argument
	 * @return {@code true} if the input arguments match the predicate,
	 * otherwise {@code false}
	 * @throws Exception wrapped checked exceptions
	 */
	boolean testing(T t, U u) throws Exception;

	/**
	 * Evaluates this predicate on the given arguments.
	 *
	 * @param t the first input argument
	 * @param u the second input argument
	 * @return {@code true} if the input arguments match the predicate,
	 * otherwise {@code false}
	 */
	@Override
	default boolean test(final T t, final U u) {
		try {
			return testing(t, u);
		} catch (final Exception e) {
			throw new UtilException(e);
		}
	}


	/**
	 * Returns a composed predicate that represents a short-circuiting logical
	 * AND of this predicate and another.  When evaluating the composed
	 * predicate, if this predicate is {@code false}, then the {@code other}
	 * predicate is not evaluated.
	 *
	 * <p>Any exceptions thrown during evaluation of either predicate are relayed
	 * to the caller; if evaluation of this predicate throws an exception, the
	 * {@code other} predicate will not be evaluated.
	 *
	 * @param other a predicate that will be logically-ANDed with this
	 *              predicate
	 * @return a composed predicate that represents the short-circuiting logical
	 * AND of this predicate and the {@code other} predicate
	 * @throws NullPointerException if other is null
	 */
	default SerBiPredicate<T, U> and(final SerBiPredicate<? super T, ? super U> other) {
		Objects.requireNonNull(other);
		return (T t, U u) -> test(t, u) && other.test(t, u);
	}

	/**
	 * Returns a predicate that represents the logical negation of this
	 * predicate.
	 *
	 * @return a predicate that represents the logical negation of this
	 * predicate
	 */
	@Override
	default SerBiPredicate<T, U> negate() {
		return (T t, U u) -> !test(t, u);
	}

	/**
	 * Returns a composed predicate that represents a short-circuiting logical
	 * OR of this predicate and another.  When evaluating the composed
	 * predicate, if this predicate is {@code true}, then the {@code other}
	 * predicate is not evaluated.
	 *
	 * <p>Any exceptions thrown during evaluation of either predicate are relayed
	 * to the caller; if evaluation of this predicate throws an exception, the
	 * {@code other} predicate will not be evaluated.
	 *
	 * @param other a predicate that will be logically-ORed with this
	 *              predicate
	 * @return a composed predicate that represents the short-circuiting logical
	 * OR of this predicate and the {@code other} predicate
	 * @throws NullPointerException if other is null
	 */
	default SerBiPredicate<T, U> or(final SerBiPredicate<? super T, ? super U> other) {
		Objects.requireNonNull(other);
		return (T t, U u) -> test(t, u) || other.test(t, u);
	}
}

