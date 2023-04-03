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

