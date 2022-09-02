package cn.hutool.core.stream;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.mutable.MutableInt;
import cn.hutool.core.util.ArrayUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link StreamWrapper}的扩展，为实现类提供更多中间操作方法
 *
 * @param <T> 流中的元素类型
 * @param <I> 链式调用获得的实现类类型
 * @author huangchengxing
 */
public interface TransformableStreamWrapper<T, I extends TransformableStreamWrapper<T, I>> extends StreamWrapper<T, I> {

	/**
	 * 过滤元素，返回与指定断言匹配的元素组成的流，断言带下标，并行流时下标永远为-1
	 * 这是一个无状态中间操作
	 *
	 * @param predicate 断言
	 * @return 返回叠加过滤操作后的流
	 */
	default I filterIdx(final BiPredicate<? super T, Integer> predicate) {
		Objects.requireNonNull(predicate);
		if (isParallel()) {
			return filter(e -> predicate.test(e, NOT_FOUND_INDEX));
		} else {
			final MutableInt index = new MutableInt(NOT_FOUND_INDEX);
			return filter(e -> predicate.test(e, index.incrementAndGet()));
		}
	}

	/**
	 * 过滤掉空元素
	 *
	 * @return 过滤后的流
	 */
	default I nonNull() {
		return filter(Objects::nonNull);
	}

	/**
	 * 返回与指定函数将元素作为参数执行后组成的流。操作带下标，并行流时下标永远为-1
	 * 这是一个无状态中间操作
	 * @param action 指定的函数
	 * @return 返回叠加操作后的FastStream
	 * @apiNote 该方法存在的意义主要是用来调试
	 * 当你需要查看经过操作管道某处的元素和下标，可以执行以下操作:
	 * <pre>{@code
	 *     Stream.of("one", "two", "three", "four")
	 * 				.filter(e -> e.length() > 3)
	 * 				.peekIdx((e,i) -> System.out.println("Filtered value: " + e + " Filtered idx:" + i))
	 * 				.map(String::toUpperCase)
	 * 				.peekIdx((e,i) -> System.out.println("Mapped value: " + e + " Mapped idx:" + i))
	 * 				.collect(Collectors.toList());
	 * }</pre>
	 */
	default I peekIdx(BiConsumer<? super T, Integer> action) {
		Objects.requireNonNull(action);
		if (isParallel()) {
			return peek(e -> action.accept(e, NOT_FOUND_INDEX));
		} else {
			AtomicInteger index = new AtomicInteger(NOT_FOUND_INDEX);
			return peek(e -> action.accept(e, index.incrementAndGet()));
		}
	}

	/**
	 * 返回叠加调用{@link Console#log(Object)}打印出结果的流
	 *
	 * @return 返回叠加操作后的FastStream
	 */
	default I log() {
		return peek(Console::log);
	}

	/**
	 * 反转顺序
	 *
	 * @return 反转元素顺序
	 */
	@SuppressWarnings("unchecked")
	default I reverse() {
		final T[] array = (T[]) toArray();
		ArrayUtil.reverse(array);
		return wrapping(Stream.of(array)).parallel(isParallel());
	}

	/**
	 * 更改流的并行状态
	 *
	 * @param parallel 是否并行
	 * @return 流
	 */
	default I parallel(final boolean parallel) {
		return parallel ? parallel() : sequential();
	}

	/**
	 * 与给定元素组成的流合并，成为新的流
	 *
	 * @param obj 元素
	 * @return 流
	 */
	@SuppressWarnings("unchecked")
	default I push(final T... obj) {
		Stream<T> result = stream();
		if (ArrayUtil.isNotEmpty(obj)) {
			result = Stream.concat(stream(), Stream.of(obj));
		}
		return wrapping(result);
	}

	/**
	 * 给定元素组成的流与当前流合并，成为新的流
	 *
	 * @param obj 元素
	 * @return 流
	 */
	default I unshift(final T... obj) {
		Stream<T> result = stream();
		if (ArrayUtil.isNotEmpty(obj)) {
			result = Stream.concat(Stream.of(obj), stream());
		}
		return wrapping(result);
	}

	/**
	 * 通过删除或替换现有元素或者原地添加新的元素来修改列表，并以列表形式返回被修改的内容。此方法不会改变原列表。
	 * 类似js的<a href="https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Array/splice">splice</a>函数
	 *
	 * @param start       起始下标
	 * @param deleteCount 删除个数，正整数
	 * @param items       放入值
	 * @return 操作后的流
	 */
	default I splice(final int start, final int deleteCount, final T... items) {
		final List<T> elements = stream().collect(Collectors.toList());
		return wrapping(ListUtil.splice(elements, start, deleteCount, items).stream())
			.parallel(isParallel());
	}

	/**
	 * 保留 与指定断言 匹配时的元素, 在第一次不匹配时终止, 抛弃当前(第一个不匹配元素)及后续所有元素
	 * <p>与 jdk9 中的 takeWhile 方法不太一样, 这里的实现是个 顺序的、有状态的中间操作</p>
	 * <pre>本环节中是顺序执行的, 但是后续操作可以支持并行流: {@code
	 * FastStream.iterate(1, i -> i + 1)
	 * 	.parallel()
	 * 	// 顺序执行
	 * 	.takeWhile(e -> e < 50)
	 * 	// 并发
	 * 	.map(e -> e + 1)
	 * 	// 并发
	 * 	.map(String::valueOf)
	 * 	.toList();
	 * }</pre>
	 * <p>但是不建议在并行流中使用, 除非你确定 takeWhile 之后的操作能在并行流中受益很多</p>
	 *
	 * @param predicate 断言
	 * @return 与指定断言匹配的元素组成的流
	 */
	default I takeWhile(final Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		return wrapping(StreamUtil.takeWhile(stream(), predicate));
	}

	/**
	 * 删除 与指定断言 匹配的元素, 在第一次不匹配时终止, 返回当前(第一个不匹配元素)及剩余元素组成的新流
	 * <p>与 jdk9 中的 dropWhile 方法不太一样, 这里的实现是个 顺序的、有状态的中间操作</p>
	 * <pre>本环节中是顺序执行的, 但是后续操作可以支持并行流: {@code
	 * FastStream.iterate(1, i <= 100, i -> i + 1)
	 * 	.parallel()
	 * 	// 顺序执行
	 * 	.dropWhile(e -> e < 50)
	 * 	// 并发
	 * 	.map(e -> e + 1)
	 * 	// 并发
	 * 	.map(String::valueOf)
	 * 	.toList();
	 * }</pre>
	 * <p>但是不建议在并行流中使用, 除非你确定 dropWhile 之后的操作能在并行流中受益很多</p>
	 *
	 * @param predicate 断言
	 * @return 剩余元素组成的流
	 */
	default I dropWhile(final Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		return wrapping(StreamUtil.dropWhile(stream(), predicate));
	}

	/**
	 * 将当前流转为另一对象。用于提供针对流本身而非流中元素的操作
	 *
	 * @param <R>       转换类型
	 * @param transform 转换
	 * @return 转换后的流
	 */
	default <R> Optional<R> transform(final Function<? super I, R> transform) {
		Assert.notNull(transform, "transform must not null");
		return Optional.ofNullable(transform.apply(wrapping(this)));
	}

}
