package cn.hutool.core.collection;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

/**
 * 数据迭代器
 * <p>
 * 与{@link java.util.Iterator} 不同的是, {@link java.util.Iterator}每迭代一次返回的是单个元素
 * 而{@link ListDataIterator}每迭代一次返回的都是一个集合, 并且可以对集合内的每一个元素做操作
 *
 * @author luozongle
 */
public interface ListDataIterator<E> {

	/**
	 * 是否还有数据
	 *
	 * @return boolean
	 */
	boolean hasNext();

	/**
	 * 返回迭代中的下一个元素
	 *
	 * @return 迭代中的下一个元素集合
	 * @throws NoSuchElementException 如果迭代没有更多的元素
	 */
	List<E> next();

	/**
	 * 迭代过的元素数量
	 *
	 * @return 元素数量
	 */
	long iteratorCount();

	/**
	 * 对每个剩余元素执行给定的操作
	 *
	 * @param action 为每个元素执行的操作
	 */
	default void forEachRemaining(Consumer<? super E> action) {
		forEachRemaining(action, false);
	}

	/**
	 * 对每个剩余元素执行给定的操作
	 *
	 * @param action   为每个元素执行的操作
	 * @param parallel 是否使用并行流
	 */
	default void forEachRemaining(Consumer<? super E> action, boolean parallel) {
		Objects.requireNonNull(action);
		while (hasNext()) {
			StreamSupport.stream(next().spliterator(), parallel).forEach(action);
		}
	}

	/**
	 * 使用并行流对每个剩余元素执行给定的操作
	 *
	 * @param action 为每个元素执行的操作
	 */
	default void parallelForEachRemaining(Consumer<? super E> action) {
		forEachRemaining(action, true);
	}

}
