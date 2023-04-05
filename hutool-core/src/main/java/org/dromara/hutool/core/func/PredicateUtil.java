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

import org.dromara.hutool.core.stream.StreamUtil;

import java.util.function.Predicate;

/**
 * 一些{@link Predicate}相关封装
 *
 * @author looly VampireAchao
 * @since 6.0.0
 */
public class PredicateUtil {

	/**
	 * 反向条件
	 *
	 * @param predicate 条件
	 * @param <T> 参数类型
	 * @return 反向条件 {@link Predicate}
	 */
	public static <T> Predicate<T> negate(final Predicate<T> predicate) {
		return predicate.negate();
	}

	/**
	 * 多个条件转换为”与“复合条件，即所有条件都为true时，才返回true
	 *
	 * @param <T>        判断条件的对象类型
	 * @param components 多个条件
	 * @return 复合条件
	 */
	public static <T> Predicate<T> and(final Iterable<Predicate<T>> components) {
		return StreamUtil.of(components, false).reduce(Predicate::and).orElseGet(() -> o -> true);
	}

	/**
	 * 多个条件转换为”与“复合条件，即所有条件都为true时，才返回true
	 *
	 * @param <T>        判断条件的对象类型
	 * @param components 多个条件
	 * @return 复合条件
	 */
	@SafeVarargs
	public static <T> Predicate<T> and(final Predicate<T>... components) {
		return StreamUtil.of(components).reduce(Predicate::and).orElseGet(() -> o -> true);
	}

	/**
	 * 多个条件转换为”或“复合条件，即任意一个条件都为true时，返回true
	 *
	 * @param <T>        判断条件的对象类型
	 * @param components 多个条件
	 * @return 复合条件
	 */
	public static <T> Predicate<T> or(final Iterable<Predicate<T>> components) {
		return StreamUtil.of(components, false).reduce(Predicate::or).orElseGet(() -> o -> false);
	}

	/**
	 * 多个条件转换为”或“复合条件，即任意一个条件都为true时，返回true
	 *
	 * @param <T>        判断条件的对象类型
	 * @param components 多个条件
	 * @return 复合条件
	 */
	@SafeVarargs
	public static <T> Predicate<T> or(final Predicate<T>... components) {
		return StreamUtil.of(components).reduce(Predicate::or).orElseGet(() -> o -> false);
	}
}
