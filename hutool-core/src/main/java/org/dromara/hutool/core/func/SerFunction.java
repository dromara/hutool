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
	 * @throws Exception wrapped checked exception
	 */
	R applying(T t) throws Throwable;

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
		} catch (final Throwable e) {
			throw ExceptionUtil.wrapRuntime(e);
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
