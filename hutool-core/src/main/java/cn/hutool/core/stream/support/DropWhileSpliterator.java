package cn.hutool.core.stream.support;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * dropWhile 的 Spliterator
 * <p>借鉴自StreamEx</p>
 *
 * @author emptypoint
 * @since 6.0.0
 */
class DropWhileSpliterator<T> implements Spliterator<T> {

	static <T> DropWhileSpliterator<T> create(Spliterator<T> source, Predicate<? super T> predicate) {
		return new DropWhileSpliterator<>(source, predicate);
	}

	private final Spliterator<T> source;
	private final Predicate<? super T> predicate;
	private boolean isFound = false;

	private DropWhileSpliterator(Spliterator<T> source, Predicate<? super T> predicate) {
		this.source = source;
		this.predicate = predicate;
	}

	@Override
	public boolean tryAdvance(Consumer<? super T> action) {
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
