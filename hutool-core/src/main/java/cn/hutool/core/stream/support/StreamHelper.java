package cn.hutool.core.stream.support;

import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;

/**
 * FastStream 辅助工具类
 *
 * @author emptypoint
 * @since 6.0.0
 */
public final class StreamHelper {
	private StreamHelper() {
	}

	/**
	 * 返回无限有序流
	 * 该流由 初始值 然后判断条件 以及执行 迭代函数 进行迭代获取到元素
	 *
	 * @param <T>     元素类型
	 * @param seed    初始值
	 * @param hasNext 条件值
	 * @param next    用上一个元素作为参数执行并返回一个新的元素
	 * @return 无限有序流
	 */
	public static <T> Stream<T> iterate(T seed, Predicate<? super T> hasNext, UnaryOperator<T> next) {
		requireNonNull(next);
		requireNonNull(hasNext);
		return StreamSupport.stream(IterateSpliterator.create(seed, hasNext, next), false);
	}

	/**
	 * 保留 与指定断言 匹配时的元素, 在第一次不匹配时终止, 抛弃当前(第一个不匹配元素)及后续所有元素
	 * <p>与 jdk9 中的 takeWhile 方法不太一样, 这里的实现是个 顺序的、有状态的中间操作</p>
	 * <p>本环节中是顺序执行的, 但是后续操作可以支持并行流</p>
	 * <p>但是不建议在并行流中使用, 除非你确定 takeWhile 之后的操作能在并行流中受益很多</p>
	 *
	 * @param source    源流
	 * @param <T>       元素类型
	 * @param predicate 断言
	 * @return 与指定断言匹配的元素组成的流
	 */
	public static <T> Stream<T> takeWhile(Stream<T> source, Predicate<? super T> predicate) {
		requireNonNull(source);
		requireNonNull(predicate);
		return createStatefulNewStream(source, TakeWhileSpliterator.create(source.spliterator(), predicate));
	}

	/**
	 * 删除 与指定断言 匹配的元素, 在第一次不匹配时终止, 返回当前(第一个不匹配元素)及剩余元素组成的新流
	 * <p>与 jdk9 中的 dropWhile 方法不太一样, 这里的实现是个 顺序的、有状态的中间操作</p>
	 * <p>本环节中是顺序执行的, 但是后续操作可以支持并行流</p>
	 * <p>但是不建议在并行流中使用, 除非你确定 dropWhile 之后的操作能在并行流中受益很多</p>
	 *
	 * @param source    源流
	 * @param <T>       元素类型
	 * @param predicate 断言
	 * @return 剩余元素组成的流
	 */
	public static <T> Stream<T> dropWhile(Stream<T> source, Predicate<? super T> predicate) {
		requireNonNull(source);
		requireNonNull(predicate);
		return createStatefulNewStream(source, DropWhileSpliterator.create(source.spliterator(), predicate));
	}

	// region 私有方法
	/* ================================================== 私有方法 =================================================== */

	/**
	 * 根据 源流 和 新的Spliterator 生成新的流
	 * <p>这是一个 顺序的、有状态的流</p>
	 * <p>在新流的第一个节点是顺序执行的, 但是后续操作可以支持并行流</p>
	 *
	 * @param source         源流
	 * @param newSpliterator 新流的Spliterator
	 * @param <T>            旧流的元素类型
	 * @param <R>            新流的元素类型
	 * @return 新流
	 */
	private static <T, R> Stream<R> createStatefulNewStream(Stream<T> source, Spliterator<R> newSpliterator) {
		// 创建新流
		Stream<R> newStream = StreamSupport.stream(newSpliterator, source.isParallel());
		// 如果旧流是并行流, 新流主动调用一个有状态的操作, 虽然没有意义, 但是可以让后续的无状态节点正常并发
		if (source.isParallel()) {
			newStream = newStream.limit(Long.MAX_VALUE);
		}
		// 由于新流不与旧流的节点关联, 所以需要主动设置旧流的close方法, 哪怕几乎不可能有人在旧流上设置onClose操作
		return newStream.onClose(source::close);
	}

	/* ============================================================================================================== */
	// endregion

}
