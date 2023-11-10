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

import java.io.Serializable;
import java.util.Objects;

/**
 * 3参数Consumer
 *
 * @param <P1> 参数一类型
 * @param <P2> 参数二类型
 * @param <P3> 参数三类型
 * @author TomXin, VampireAchao
 * @since 5.7.22
 */
@FunctionalInterface
public interface SerConsumer3<P1, P2, P3> extends Serializable {

	/**
	 * 接收参数方法
	 *
	 * @param p1 参数一
	 * @param p2 参数二
	 * @param p3 参数三
	 * @throws Exception wrapped checked exception
	 */
	void accepting(P1 p1, P2 p2, P3 p3) throws Throwable;

	/**
	 * 接收参数方法
	 *
	 * @param p1 参数一
	 * @param p2 参数二
	 * @param p3 参数三
	 */
	default void accept(final P1 p1, final P2 p2, final P3 p3) {
		try {
			accepting(p1, p2, p3);
		} catch (final Throwable e) {
			throw ExceptionUtil.wrapRuntime(e);
		}
	}

	/**
	 * Returns a composed {@code SerConsumer3} that performs, in sequence, this
	 * operation followed by the {@code after} operation. If performing either
	 * operation throws an exception, it is relayed to the caller of the
	 * composed operation.  If performing this operation throws an exception,
	 * the {@code after} operation will not be performed.
	 *
	 * @param after the operation to perform after this operation
	 * @return a composed {@code SerConsumer3} that performs in sequence this
	 * operation followed by the {@code after} operation
	 * @throws NullPointerException if {@code after} is null
	 */
	default SerConsumer3<P1, P2, P3> andThen(final SerConsumer3<P1, P2, P3> after) {
		Objects.requireNonNull(after);
		return (final P1 p1, final P2 p2, final P3 p3) -> {
			accept(p1, p2, p3);
			after.accept(p1, p2, p3);
		};
	}
}
