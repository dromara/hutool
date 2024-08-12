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
	 * @throws Exception wrapped checked exception
	 */
	boolean testing(T t, U u) throws Throwable;

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
		} catch (final Throwable e) {
			throw ExceptionUtil.wrapRuntime(e);
		}
	}


	/**
	 * Returns a composed predicate that represents a short-circuiting logical
	 * AND of this predicate and another.  When evaluating the composed
	 * predicate, if this predicate is {@code false}, then the {@code other}
	 * predicate is not evaluated.
	 *
	 * <p>Any exception thrown during evaluation of either predicate are relayed
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
	 * <p>Any exception thrown during evaluation of either predicate are relayed
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

