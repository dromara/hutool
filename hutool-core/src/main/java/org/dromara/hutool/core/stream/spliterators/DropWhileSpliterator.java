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

package org.dromara.hutool.core.stream.spliterators;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * dropWhile 的 Spliterator
 * <p>借鉴自StreamEx</p>
 *
 * @param <T> 元素类型
 * @author emptypoint
 * @since 6.0.0
 */
public class DropWhileSpliterator<T> implements Spliterator<T> {

	/**
	 * 创建
	 *
	 * @param source    {@link Spliterator}
	 * @param predicate 断言
	 * @param <T>       元素类型
	 * @return DropWhileSpliterator
	 */
	public static <T> DropWhileSpliterator<T> of(final Spliterator<T> source, final Predicate<? super T> predicate) {
		return new DropWhileSpliterator<>(source, predicate);
	}

	private final Spliterator<T> source;
	private final Predicate<? super T> predicate;
	private boolean isFound = false;

	/**
	 * 构造
	 *
	 * @param source    {@link Spliterator}
	 * @param predicate 断言
	 */
	public DropWhileSpliterator(final Spliterator<T> source, final Predicate<? super T> predicate) {
		this.source = source;
		this.predicate = predicate;
	}

	@Override
	public boolean tryAdvance(final Consumer<? super T> action) {
		boolean hasNext = true;
		// 如果 还没找到 并且 流中还有元素 继续找
		while (!isFound && hasNext) {
			hasNext = source.tryAdvance(e -> {
				if (!predicate.test(e)) {
					// 第一次不匹配
					isFound = true;
					action.accept(e);
				}
			});
		}

		// 对找到的元素进行后续处理
		if (isFound) {
			source.forEachRemaining(action);
		}

		// 该环节已经处理完成
		return false;
	}

	@Override
	public Spliterator<T> trySplit() {
		return null;
	}

	@Override
	public long estimateSize() {
		return Long.MAX_VALUE;
	}

	@Override
	public int characteristics() {
		return source.characteristics() & ~Spliterator.SIZED;
	}

	@Override
	public Comparator<? super T> getComparator() {
		return source.getComparator();
	}
}
