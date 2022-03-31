package cn.hutool.core.collection;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

/**
 * 以迭代器的形式获取数据
 * 每迭代一次可以获取一页数据(一页中包含多个数据)
 * 可以像操作{@link java.util.Iterator}迭代器一样,进行迭代获取每页的数据, 并对每个数据进行执行的操作
 *
 * @author luozongle
 */
public class ListData<E> implements ListDataIterable<E> {

	/**
	 * 默认的offset
	 */
	private static final long DEFAULT_OFFSET = 0;

	/**
	 * 默认的pageSize
	 */
	private static final long DEFAULT_PAGE_SIZE = 200;

	/**
	 * 每次迭代时的偏移量
	 */
	private long offset;

	/**
	 * 每次迭代时获取的数据量
	 */
	private final long pageSize;

	/**
	 * 每次迭代时获取的List集合
	 */
	private List<E> element;

	/**
	 * 获取数据的方式
	 */
	private final BiFunction<Long, Long, List<E>> getDataBiFunction;

	/**
	 * 计算偏移量的方式
	 */
	private final BiFunction<Long, List<E>, Long> increaseOffsetBiFunction;


	/**
	 * 创建一个ListData
	 *
	 * @param getDataBiFunction        每次获取数据的具体方式,T: 偏移量, U: 每次获取数据的数量, R: 返回获取的数据集合
	 * @param offset                   初始化偏移量
	 * @param pageSize                 每次获取数据的数量
	 * @param increaseOffsetBiFunction 每迭代一次之后计算增加偏移量的方式, T:当前偏移量, U: 当前获取的数据集合, R: 返回下一次的偏移量
	 */
	protected ListData(BiFunction<Long, Long, List<E>> getDataBiFunction,
					   long offset,
					   long pageSize,
					   BiFunction<Long, List<E>, Long> increaseOffsetBiFunction) {
		this.getDataBiFunction = getDataBiFunction;
		this.offset = offset;
		this.pageSize = pageSize;
		this.increaseOffsetBiFunction = increaseOffsetBiFunction;
	}

	/**
	 * 创建一个ListData，默认的策略如下：<br>
	 * <pre>
	 *     1. Offset默认为{@link #DEFAULT_OFFSET}
	 *     2. PageSize默认为{@link #DEFAULT_PAGE_SIZE}
	 *     3. offset计算方式为: 上一次迭代的offset + 这一次获取的数据size
	 * </pre>
	 *
	 * @param getDataBiFunction 每次获取数据的具体方式,T: 偏移量, U: 每次获取数据的数量, R: 返回获取的数据集合
	 */
	public static <E> ListData<E> createIncrementDataOffset(BiFunction<Long, Long, List<E>> getDataBiFunction) {
		return create(getDataBiFunction,
				DEFAULT_OFFSET,
				DEFAULT_PAGE_SIZE,
				(lastTimeOffset, element) -> lastTimeOffset + element.size());
	}

	/**
	 * 创建一个ListData默认的策略如下：<br>
	 * <pre>
	 *     1. offset计算方式为: 上一次迭代的offset + 这一次获取的数据size
	 * </pre>
	 *
	 * @param getDataBiFunction 每次获取数据的具体方式,T: 偏移量, U: 每次获取数据的数量, R: 返回获取的数据集合
	 * @param offset            初始化偏移量
	 * @param pageSize          每次获取数据的数量
	 */
	public static <E> ListData<E> createIncrementDataOffset(BiFunction<Long, Long, List<E>> getDataBiFunction,
															long offset,
															int pageSize) {

		return create(getDataBiFunction,
				offset,
				pageSize,
				(lastTimeOffset, element) -> lastTimeOffset + element.size());
	}

	/**
	 * 创建一个ListData，默认的策略如下：<br>
	 * <pre>
	 *     1. Offset默认为{@link #DEFAULT_OFFSET}
	 *     2. PageSize默认为{@link #DEFAULT_PAGE_SIZE}
	 *     3. 每迭代一次offset会增加1
	 * </pre>
	 *
	 * @param getDataBiFunction 每次获取数据的具体方式,T: 偏移量, U: 每次获取数据的数量, R: 返回获取的数据集合
	 */
	public static <E> ListData<E> createIncrementPageOffset(BiFunction<Long, Long, List<E>> getDataBiFunction) {
		return create(getDataBiFunction,
				DEFAULT_OFFSET,
				DEFAULT_PAGE_SIZE,
				(lastTimeOffset, element) -> ++lastTimeOffset);
	}

	/**
	 * 创建一个ListData，默认的策略如下：<br>
	 * <pre>
	 *     1. 每迭代一次offset会增加1
	 * </pre>
	 *
	 * @param getDataBiFunction 每次获取数据的具体方式,T: 偏移量, U: 每次获取数据的数量, R: 返回获取的数据集合
	 * @param offset            初始化偏移量
	 * @param pageSize          每次获取数据的数量
	 */
	public static <E> ListData<E> createIncrementPageOffset(BiFunction<Long, Long, List<E>> getDataBiFunction,
															long offset,
															int pageSize) {
		return create(getDataBiFunction,
				offset,
				pageSize,
				(lastTimeOffset, element) -> ++lastTimeOffset);
	}

	/**
	 * 创建一个ListData，默认的策略如下：<br>
	 * <pre>
	 *     1. Offset默认为{@link #DEFAULT_OFFSET}
	 *     2. PageSize默认为{@link #DEFAULT_PAGE_SIZE}
	 * </pre>
	 *
	 * @param getDataBiFunction        每次获取数据的具体方式,T: 偏移量, U: 每次获取数据的数量, R: 返回获取的数据集合
	 * @param increaseOffsetBiFunction 每迭代一次之后计算增加偏移量的方式, T:当前偏移量, U: 当前获取的数据集合, R: 返回下一次的偏移量
	 */
	public static <E> ListData<E> create(BiFunction<Long, Long, List<E>> getDataBiFunction,
										 BiFunction<Long, List<E>, Long> increaseOffsetBiFunction) {

		return create(getDataBiFunction,
				DEFAULT_OFFSET,
				DEFAULT_PAGE_SIZE,
				increaseOffsetBiFunction);
	}

	/**
	 * 创建一个ListData，默认的策略如下：<br>
	 * <pre>
	 *     1. Offset默认为{@link #DEFAULT_OFFSET}
	 * </pre>
	 *
	 * @param getDataBiFunction        每次获取数据的具体方式,T: 偏移量, U: 每次获取数据的数量, R: 返回获取的数据集合
	 * @param pageSize                 每次获取数据的数量
	 * @param increaseOffsetBiFunction 每迭代一次之后计算增加偏移量的方式, T:当前偏移量, U: 当前获取的数据集合, R: 返回下一次的偏移量
	 */
	public static <E> ListData<E> create(BiFunction<Long, Long, List<E>> getDataBiFunction,
										 int pageSize,
										 BiFunction<Long, List<E>, Long> increaseOffsetBiFunction) {

		return new ListData<>(getDataBiFunction,
				DEFAULT_OFFSET,
				pageSize,
				increaseOffsetBiFunction);
	}


	/**
	 * 创建一个ListData<br>
	 * <p>
	 *
	 * @param getDataBiFunction        每次获取数据的具体方式,T: 偏移量, U: 每次获取数据的数量, R: 返回获取的数据集合
	 * @param offset                   初始化偏移量
	 * @param pageSize                 每次获取数据的数量
	 * @param increaseOffsetBiFunction 每迭代一次之后计算增加偏移量的方式, T:当前偏移量, U: 当前获取的数据集合, R: 返回下一次的偏移量
	 */
	public static <E> ListData<E> create(BiFunction<Long, Long, List<E>> getDataBiFunction,
										 long offset,
										 long pageSize,
										 BiFunction<Long, List<E>, Long> increaseOffsetBiFunction) {

		return new ListData<>(getDataBiFunction, offset, pageSize, increaseOffsetBiFunction);
	}

	@Override
	public ListDataIterator<E> iterator() {
		return new Itr();
	}

	private class Itr implements ListDataIterator<E> {

		long iteratorCount;

		boolean updateElement = false;

		@Override
		public boolean hasNext() {
			if (updateElement) {
				return true;
			}

			// 如果返回的下一个Page不为空, 那么就认为没有迭代完，可以继续迭代
			boolean hasNext = CollectionUtil.isNotEmpty(element = getDataBiFunction.apply(offset, pageSize));

			updateElement = true;

			return hasNext;
		}

		@Override
		public List<E> next() {
			if (CollectionUtil.isEmpty(element)) {
				throw new NoSuchElementException();
			}
			offset = increaseOffsetBiFunction.apply(offset, element);
			updateElement = false;
			iteratorCount += element.size();
			List<E> currentElement = element;
			element = null;
			return currentElement;
		}

		@Override
		public long iteratorCount() {
			return iteratorCount;
		}
	}

}
