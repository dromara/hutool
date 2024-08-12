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
 * takeWhile 的 Spliterator
 * <p>借鉴自StreamEx</p>
 *
 * @author emptypoint
 * @since 6.0.0
 */
public class TakeWhileSpliterator<T> implements Spliterator<T> {

	public static <T> TakeWhileSpliterator<T> create(final Spliterator<T> source, final Predicate<? super T> predicate) {
		return new TakeWhileSpliterator<>(source, predicate);
	}

	private final Spliterator<T> source;
	private final Predicate<? super T> predicate;
	private boolean isContinue = true;

	TakeWhileSpliterator(final Spliterator<T> source, final Predicate<? super T> predicate) {
		this.source = source;
		this.predicate = predicate;
	}

	@Override
	public boolean tryAdvance(final Consumer<? super T> action) {
		boolean hasNext = true;
		// 如果 还可以继续 并且 流中还有元素 则继续遍历
		while (isContinue && hasNext) {
			hasNext = source.tryAdvance(e -> {
				if (predicate.test(e)) {
					action.accept(e);
				} else {
					// 终止遍历剩下的元素
					isContinue = false;
				}
			});
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
		return isContinue ? source.estimateSize() : 0;
	}

	@Override
	public int characteristics() {
		return source.characteristics() & ~(Spliterator.SIZED | Spliterator.SUBSIZED);
	}

	@Override
	public Comparator<? super T> getComparator() {
		return source.getComparator();
	}
}

