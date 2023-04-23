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

import org.dromara.hutool.core.exception.HutoolException;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * 可序列化的UnaryOperator
 *
 * @param <T> 参数类型
 * @author VampireAchao
 * @see UnaryOperator
 */
@FunctionalInterface
public interface SerUnaryOperator<T> extends UnaryOperator<T>, Serializable {

	/**
	 * Applies this function to the given argument.
	 *
	 * @param t the function argument
	 * @return the function result
	 * @throws Exception wrapped checked exception
	 */
	T applying(T t) throws Exception;

	/**
	 * Applies this function to the given argument.
	 *
	 * @param t the function argument
	 * @return the function result
	 */
	@Override
	default T apply(final T t) {
		try {
			return applying(t);
		} catch (final Exception e) {
			throw new HutoolException(e);
		}
	}

	/**
	 * Returns a unary operator that always returns its input argument.
	 *
	 * @param <T> the type of the input and output of the operator
	 * @return a unary operator that always returns its input argument
	 */
	static <T> SerUnaryOperator<T> identity() {
		return t -> t;
	}


	/**
	 * casting identity
	 *
	 * @param function source function
	 * @param <T>      param type
	 * @param <R>      result type
	 * @param <F>      lambda type
	 * @return identity after casting
	 */
	@SuppressWarnings("unchecked")
	static <T, R, F extends Function<T, R>> SerUnaryOperator<T> casting(final F function) {
		return t -> (T) function.apply(t);
	}

}
