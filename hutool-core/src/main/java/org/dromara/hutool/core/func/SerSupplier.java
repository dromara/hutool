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

import org.dromara.hutool.core.exception.ExceptionUtil;
import org.dromara.hutool.core.exception.HutoolException;

import java.io.Serializable;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 可序列化的Supplier
 *
 * @param <R> 返回值类型
 * @author VampireAchao
 * @see Supplier
 */
@FunctionalInterface
public interface SerSupplier<R> extends Supplier<R>, Serializable {

	/**
	 * Gets a result.
	 *
	 * @return a result
	 * @throws Exception wrapped checked exception
	 */
	R getting() throws Exception;

	/**
	 * Gets a result.
	 *
	 * @return a result
	 */
	@Override
	default R get() {
		try {
			return getting();
		} catch (final Exception e) {
			throw ExceptionUtil.wrapRuntime(e);
		}
	}

	/**
	 * last
	 *
	 * @param serSups lambda
	 * @param <T>     type
	 * @return lambda
	 */
	@SafeVarargs
	static <T> SerSupplier<T> last(final SerSupplier<T>... serSups) {
		return Stream.of(serSups).reduce((l, r) -> r).orElseGet(() -> () -> null);
	}

}
