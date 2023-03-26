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

package cn.hutool.core.lang.func;

import cn.hutool.core.exceptions.UtilException;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 可序列化的Function
 *
 * @param <T> 参数类型
 * @param <R> 返回值类型
 * @author VampireAchao
 * @see Function
 */
@FunctionalInterface
public interface SerFunction<T, R> extends Function<T, R>, Serializable {

	/**
	 * Applies this function to the given argument.
	 *
	 * @param t the function argument
	 * @return the function result
	 * @throws Exception wrapped checked exceptions
	 */
	R applying(T t) throws Exception;

	/**
	 * Applies this function to the given argument.
	 *
	 * @param t the function argument
	 * @return the function result
	 */
	@Override
	default R apply(final T t) {
		try {
			return applying(t);
		} catch (final Exception e) {
			throw new UtilException(e);
		}
	}

	/**
	 * Returns a function that always returns its input argument.
	 *
	 * @param <T> the type of the input and output objects to the function
	 * @return a function that always returns its input argument
	 */
	static <T> SerFunction<T, T> identity() {
		return t -> t;
	}

	/**
	 * casting identity
	 *
	 * @param <T> param type
	 * @param <R> result type
	 * @return identity after casting
	 */
	@SuppressWarnings("unchecked")
	static <T, R> Function<T, R> castingIdentity() {
		return t -> (R) t;
	}
}
