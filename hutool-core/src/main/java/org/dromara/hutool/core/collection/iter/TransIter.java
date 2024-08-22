/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.collection.iter;

import org.dromara.hutool.core.lang.Assert;

import java.util.Iterator;
import java.util.function.Function;

/**
 * 使用给定的转换函数，转换源{@link Iterator}为新类型的{@link Iterator}
 *
 * @param <F> 源元素类型
 * @param <T> 目标元素类型
 * @author looly
 * @since 5.4.3
 */
public class TransIter<F, T> implements Iterator<T> {

	private final Iterator<? extends F> backingIterator;
	private final Function<? super F, ? extends T> func;

	/**
	 * 构造
	 *
	 * @param backingIterator 源{@link Iterator}
	 * @param func            转换函数
	 */
	public TransIter(final Iterator<? extends F> backingIterator, final Function<? super F, ? extends T> func) {
		this.backingIterator = Assert.notNull(backingIterator);
		this.func = Assert.notNull(func);
	}

	@Override
	public final boolean hasNext() {
		return backingIterator.hasNext();
	}

	@Override
	public final T next() {
		return func.apply(backingIterator.next());
	}

	@Override
	public final void remove() {
		backingIterator.remove();
	}
}
