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

package org.dromara.hutool.core.collection;

import java.util.Spliterator;
import java.util.function.Function;

/**
 * {@link Spliterator}相关工具类
 *
 * @author looly
 * @since 5.4.3
 */
public class SpliteratorUtil {

	/**
	 * 使用给定的转换函数，转换源{@link Spliterator}为新类型的{@link Spliterator}
	 *
	 * @param <F> 源元素类型
	 * @param <T> 目标元素类型
	 * @param fromSpliterator 源{@link Spliterator}
	 * @param function 转换函数
	 * @return 新类型的{@link Spliterator}
	 */
	public static <F, T> Spliterator<T> trans(final Spliterator<F> fromSpliterator, final Function<? super F, ? extends T> function) {
		return new TransSpliterator<>(fromSpliterator, function);
	}
}
