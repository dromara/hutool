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
