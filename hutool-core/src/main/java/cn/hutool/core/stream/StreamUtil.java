package cn.hutool.core.stream;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.stream.spliterators.DropWhileSpliterator;
import cn.hutool.core.stream.spliterators.IterateSpliterator;
import cn.hutool.core.stream.spliterators.TakeWhileSpliterator;
import cn.hutool.core.util.CharsetUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;

/**
 * {@link Stream} 工具类
 *
 * @author looly emptypoint VampireAchao
 * @since 5.6.7
 */
public class StreamUtil {

	@SafeVarargs
	public static <T> Stream<T> of(final T... array) {
		Assert.notNull(array, "Array must be not null!");
		return Stream.of(array);
	}

	/**
	 * {@link Iterable}转换为{@link Stream}，默认非并行
	 *
	 * @param iterable 集合
	 * @param <T>      集合元素类型
	 * @return {@link Stream}
	 */
	public static <T> Stream<T> of(final Iterable<T> iterable) {
		return of(iterable, false);
	}

	/**
	 * {@link Iterable}转换为{@link Stream}
	 *
	 * @param iterable 集合
	 * @param parallel 是否并行
	 * @param <T>      集合元素类型
	 * @return {@link Stream}
	 */
	public static <T> Stream<T> of(final Iterable<T> iterable, final boolean parallel) {
		Assert.notNull(iterable, "Iterable must be not null!");
		return StreamSupport.stream(
				Spliterators.spliterator(CollUtil.toCollection(iterable), 0),
				parallel);
	}

	/**
	 * 按行读取文件为{@link Stream}
	 *
	 * @param file 文件
	 * @return {@link Stream}
	 */
	public static Stream<String> of(final File file) {
		return of(file, CharsetUtil.UTF_8);
	}

	/**
	 * 按行读取文件为{@link Stream}
	 *
	 * @param path 路径
	 * @return {@link Stream}
	 */
	public static Stream<String> of(final Path path) {
		return of(path, CharsetUtil.UTF_8);
	}

	/**
	 * 按行读取文件为{@link Stream}
	 *
	 * @param file    文件
	 * @param charset 编码
	 * @return {@link Stream}
	 */
	public static Stream<String> of(final File file, final Charset charset) {
		Assert.notNull(file, "File must be not null!");
		return of(file.toPath(), charset);
	}

	/**
	 * 按行读取文件为{@link Stream}
	 *
	 * @param path    路径
	 * @param charset 编码
	 * @return {@link Stream}
	 */
	public static Stream<String> of(final Path path, final Charset charset) {
		try {
			return Files.lines(path, charset);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 通过函数创建Stream
	 *
	 * @param seed           初始值
	 * @param elementCreator 递进函数，每次调用此函数获取下一个值
	 * @param limit          限制个数
	 * @param <T>            创建元素类型
	 * @return {@link Stream}
	 */
	public static <T> Stream<T> of(final T seed, final UnaryOperator<T> elementCreator, final int limit) {
		return Stream.iterate(seed, elementCreator).limit(limit);
	}

	/**
	 * 将Stream中所有元素以指定分隔符，合并为一个字符串，对象默认调用toString方法
	 *
	 * @param stream    {@link Stream}
	 * @param delimiter 分隔符
	 * @param <T>       元素类型
	 * @return 字符串
	 */
	public static <T> String join(final Stream<T> stream, final CharSequence delimiter) {
		return stream.collect(CollectorUtil.joining(delimiter));
	}

	/**
	 * 将Stream中所有元素以指定分隔符，合并为一个字符串
	 *
	 * @param stream       {@link Stream}
	 * @param delimiter    分隔符
	 * @param toStringFunc 元素转换为字符串的函数
	 * @param <T>          元素类型
	 * @return 字符串
	 */
	public static <T> String join(final Stream<T> stream, final CharSequence delimiter,
								  final Function<T, ? extends CharSequence> toStringFunc) {
		return stream.collect(CollectorUtil.joining(delimiter, toStringFunc));
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
