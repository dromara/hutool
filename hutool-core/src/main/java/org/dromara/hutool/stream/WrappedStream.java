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

package org.dromara.hutool.stream;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * <p>{@link Stream}实例的包装器，用于增强原始的{@link Stream}，提供一些额外的中间与终端操作。 <br>
 * 默认提供两个可用实现：
 * <ul>
 *     <li>{@link EasyStream}：针对单元素的通用增强流实现；</li>
 *     <li>{@link EntryStream}：针对键值对类型元素的增强流实现；</li>
 * </ul>
 *
 * @param <T> 流中的元素类型
 * @param <S> {@link WrappedStream}的实现类类型
 * @author huangchengxing
 * @see TerminableWrappedStream
 * @see TransformableWrappedStream
 * @see AbstractEnhancedWrappedStream
 * @see EasyStream
 * @see EntryStream
 * @since 6.0.0
 */
public interface WrappedStream<T, S extends WrappedStream<T, S>> extends Stream<T>, Iterable<T> {

	/**
	 * 代表不存在的下标, 或者未找到元素时的下标
	 */
	int NOT_FOUND_ELEMENT_INDEX = -1;

	/**
	 * 获取被当前实例包装的流对象
	 *
	 * @return 被当前实例包装的流对象
	 */
	Stream<T> unwrap();

	/**
	 * 将一个原始流包装为指定类型的增强流 <br>
	 * 若{@code source}于当前实例包装的流并不相同，则该增强流与当前实例无关联关系
	 *
	 * @param source 被包装的流
	 * @return 包装后的流
	 */
	S wrap(final Stream<T> source);

	/**
	 * 过滤元素，返回与指定断言匹配的元素组成的流
	 * 这是一个无状态中间操作
	 *
	 * @param predicate 断言
	 * @return 返回叠加过滤操作后的流
	 */
	@Override
	default S filter(final Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		return wrap(unwrap().filter(predicate));
	}

	/**
	 * 和{@link EasyStream#map(Function)}一样，只不过函数的返回值必须为int类型
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 返回值为int类型的函数
	 * @return 叠加操作后元素类型全为int的流
	 */
	@Override
	default IntStream mapToInt(final ToIntFunction<? super T> mapper) {
		Objects.requireNonNull(mapper);
		return unwrap().mapToInt(mapper);
	}

	/**
	 * 和{@link EasyStream#map(Function)}一样，只不过函数的返回值必须为long类型
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 返回值为long类型的函数
	 * @return 叠加操作后元素类型全为long的流
	 */
	@Override
	default LongStream mapToLong(final ToLongFunction<? super T> mapper) {
		Objects.requireNonNull(mapper);
		return unwrap().mapToLong(mapper);
	}

	/**
	 * 和{@link EasyStream#map(Function)}一样，只不过函数的返回值必须为double类型
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 返回值为double类型的函数
	 * @return 叠加操作后元素类型全为double的流
	 */
	@Override
	default DoubleStream mapToDouble(final ToDoubleFunction<? super T> mapper) {
		Objects.requireNonNull(mapper);
		return unwrap().mapToDouble(mapper);
	}

	/**
	 * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 操作，返回IntStream
	 * @return 返回叠加拆分操作后的IntStream
	 */
	@Override
	default IntStream flatMapToInt(final Function<? super T, ? extends IntStream> mapper) {
		Objects.requireNonNull(mapper);
		return unwrap().flatMapToInt(mapper);
	}

	/**
	 * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 操作，返回LongStream
	 * @return 返回叠加拆分操作后的LongStream
	 */
	@Override
	default LongStream flatMapToLong(final Function<? super T, ? extends LongStream> mapper) {
		Objects.requireNonNull(mapper);
		return unwrap().flatMapToLong(mapper);
	}

	/**
	 * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 操作，返回DoubleStream
	 * @return 返回叠加拆分操作后的DoubleStream
	 */
	@Override
	default DoubleStream flatMapToDouble(final Function<? super T, ? extends DoubleStream> mapper) {
		Objects.requireNonNull(mapper);
		return unwrap().flatMapToDouble(mapper);
	}

	/**
	 * 返回一个具有去重特征的流 非并行流(顺序流)下对于重复元素，保留遇到顺序中最先出现的元素，并行流情况下不能保证具体保留哪一个
	 * 这是一个有状态中间操作
	 *
	 * @return 一个具有去重特征的流
	 */
	@Override
	default S distinct() {
		return wrap(unwrap().distinct());
	}

	/**
	 * 返回一个元素按自然顺序排序的流
	 * 如果此流的元素不是{@code Comparable} ，则在执行终端操作时可能会抛出 {@code java.lang.ClassCastException}
	 * 对于顺序流，排序是稳定的。 对于无序流，没有稳定性保证。
	 * 这是一个有状态中间操作
	 *
	 * @return 一个元素按自然顺序排序的流
	 */
	@Override
	default S sorted() {
		return wrap(unwrap().sorted());
	}

	/**
	 * 返回一个元素按指定的{@link Comparator}排序的流
	 * 如果此流的元素不是{@code Comparable} ，则在执行终端操作时可能会抛出{@code java.lang.ClassCastException}
	 * 对于顺序流，排序是稳定的。 对于无序流，没有稳定性保证。
	 * 这是一个有状态中间操作
	 *
	 * @param comparator 排序规则
	 * @return 一个元素按指定的Comparator排序的流
	 */
	@Override
	default S sorted(final Comparator<? super T> comparator) {
		Objects.requireNonNull(comparator);
		return wrap(unwrap().sorted(comparator));
	}

	/**
	 * 返回与指定函数将元素作为参数执行后组成的流。
	 * 这是一个无状态中间操作
	 *
	 * @param action 指定的函数
	 * @return 返回叠加操作后的FastStream
	 * @apiNote 该方法存在的意义主要是用来调试
	 * 当你需要查看经过操作管道某处的元素，可以执行以下操作:
	 * <pre>{@code
	 *     .of("one", "two", "three", "four")
	 *         .filter(e -> e.length() > 3)
	 *         .peek(e -> System.out.println("Filtered value: " + e))
	 *         .map(String::toUpperCase)
	 *         .peek(e -> System.out.println("Mapped value: " + e))
	 *         .collect(Collectors.toList());
	 * }</pre>
	 */
	@Override
	default S peek(final Consumer<? super T> action) {
		Objects.requireNonNull(action);
		return wrap(unwrap().peek(action));
	}

	/**
	 * 返回截取后面一些元素的流
	 * 这是一个短路状态中间操作
	 *
	 * @param maxSize 元素截取后的个数
	 * @return 截取后的流
	 */
	@Override
	default S limit(final long maxSize) {
		return wrap(unwrap().limit(maxSize));
	}

	/**
	 * 返回丢弃前面n个元素后的剩余元素组成的流，如果当前元素个数小于n，则返回一个元素为空的流
	 * 这是一个有状态中间操作
	 *
	 * @param n 需要丢弃的元素个数
	 * @return 丢弃前面n个元素后的剩余元素组成的流
	 */
	@Override
	default S skip(final long n) {
		return wrap(unwrap().skip(n));
	}

	/**
	 * 对流里面的每一个元素执行一个操作
	 * 这是一个终端操作
	 *
	 * @param action 操作
	 */
	@Override
	default void forEach(final Consumer<? super T> action) {
		Objects.requireNonNull(action);
		unwrap().forEach(action);
	}

	/**
	 * 对流里面的每一个元素按照顺序执行一个操作
	 * 这是一个终端操作
	 *
	 * @param action 操作
	 */
	@Override
	default void forEachOrdered(final Consumer<? super T> action) {
		Objects.requireNonNull(action);
		unwrap().forEachOrdered(action);
	}

	/**
	 * 返回一个包含此流元素的数组
	 * 这是一个终端操作
	 *
	 * @return 包含此流元素的数组
	 */
	@Override
	default Object[] toArray() {
		return unwrap().toArray();
	}

	/**
	 * 返回一个包含此流元素的指定的数组，例如以下代码编译正常，但运行时会抛出 {@link ArrayStoreException}
	 * <pre>{@code String[] strings = Stream.<Integer>builder().add(1).build().toArray(String[]::new); }</pre>
	 *
	 * @param generator 这里的IntFunction的参数是元素的个数，返回值为数组类型
	 * @param <A>       给定的数组类型
	 * @return 包含此流元素的指定的数组
	 * @throws ArrayStoreException 如果元素转换失败，例如不是该元素类型及其父类，则抛出该异常
	 */
	@Override
	default <A> A[] toArray(final IntFunction<A[]> generator) {
		Objects.requireNonNull(generator);
		//noinspection SuspiciousToArrayCall
		return unwrap().toArray(generator);
	}

	/**
	 * 对元素进行聚合，并返回聚合后的值，相当于在for循环里写sum=sum+ints[i]
	 * 这是一个终端操作<br>
	 * 求和、最小值、最大值、平均值和转换成一个String字符串均为聚合操作
	 * 例如这里对int进行求和可以写成：
	 *
	 * <pre>{@code
	 *     Integer sum = integers.reduce(0, (a, b) -> a+b);
	 * }</pre>
	 * <p>
	 * 或者写成:
	 *
	 * <pre>{@code
	 *     Integer sum = integers.reduce(0, Integer::sum);
	 * }</pre>
	 *
	 * @param identity    初始值，还用于限定泛型
	 * @param accumulator 你想要的聚合操作
	 * @return 聚合计算后的值
	 */
	@Override
	default T reduce(final T identity, final BinaryOperator<T> accumulator) {
		Objects.requireNonNull(accumulator);
		return unwrap().reduce(identity, accumulator);
	}

	/**
	 * 对元素进行聚合，并返回聚合后用 {@link Optional}包裹的值，相当于在for循环里写sum=sum+ints[i]
	 * 该操作相当于：
	 * <pre>{@code
	 *     boolean foundAny = false;
	 *     T result = null;
	 *     for (T element : this unwrap) {
	 *         if (!foundAny) {
	 *             foundAny = true;
	 *             result = element;
	 *         }
	 *         else
	 *             result = accumulator.apply(result, element);
	 *     }
	 *     return foundAny ? Optional.of(result) : Optional.empty();
	 * }</pre>
	 * 但它不局限于顺序执行，例如并行流等情况下
	 * 这是一个终端操作<br>
	 * 例如以下场景抛出 NPE ：
	 * <pre>{@code
	 *      Optional<Integer> reduce = Stream.<Integer>builder().add(1).add(1).build().reduce((a, b) -> null);
	 * }</pre>
	 *
	 * @param accumulator 你想要的聚合操作
	 * @return 聚合后用 {@link Optional}包裹的值
	 * @throws NullPointerException 如果给定的聚合操作中执行后结果为空，并用于下一次执行，则抛出该异常
	 * @see #reduce(Object, BinaryOperator)
	 * @see #min(Comparator)
	 * @see #max(Comparator)
	 */
	@Override
	default Optional<T> reduce(final BinaryOperator<T> accumulator) {
		Objects.requireNonNull(accumulator);
		return unwrap().reduce(accumulator);
	}

	/**
	 * 对元素进行聚合，并返回聚合后的值，并行流时聚合拿到的初始值不稳定
	 * 这是一个终端操作
	 *
	 * @param identity    初始值
	 * @param accumulator 累加器，具体为你要的聚合操作
	 * @param combiner    用于并行流时组合多个结果
	 * @param <U>         初始值
	 * @return 聚合操作的结果
	 * @see #reduce(BinaryOperator)
	 * @see #reduce(Object, BinaryOperator)
	 */
	@Override
	default <U> U reduce(final U identity, final BiFunction<U, ? super T, U> accumulator, final BinaryOperator<U> combiner) {
		Objects.requireNonNull(accumulator);
		Objects.requireNonNull(combiner);
		return unwrap().reduce(identity, accumulator, combiner);
	}

	/**
	 * 对元素进行收集，并返回收集后的容器
	 * 这是一个终端操作
	 *
	 * @param supplier    提供初始值的函数式接口，一般可以传入构造参数
	 * @param accumulator 具体收集操作
	 * @param combiner    用于并行流时组合多个结果
	 * @param <R>         用于收集元素的容器，大多是集合
	 * @return 收集后的容器
	 * <pre>{@code
	 *  List<Integer> collect = Stream.iterate(1, i -> ++i).limit(10).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	 * }</pre>
	 */
	@Override
	default <R> R collect(final Supplier<R> supplier, final BiConsumer<R, ? super T> accumulator, final BiConsumer<R, R> combiner) {
		Objects.requireNonNull(supplier);
		Objects.requireNonNull(accumulator);
		Objects.requireNonNull(combiner);
		return unwrap().collect(supplier, accumulator, combiner);
	}

	/**
	 * 对元素进行收集，并返回收集后的元素
	 * 这是一个终端操作
	 *
	 * @param collector 收集器
	 * @param <R>       容器类型
	 * @param <A>       具体操作时容器类型，例如 {@code List::add} 时它为 {@code List}
	 * @return 收集后的容器
	 */
	@Override
	default <R, A> R collect(final Collector<? super T, A, R> collector) {
		Objects.requireNonNull(collector);
		return unwrap().collect(collector);
	}

	/**
	 * 获取最小值
	 *
	 * @param comparator 一个用来比较大小的比较器{@link Comparator}
	 * @return 最小值
	 */
	@Override
	default Optional<T> min(final Comparator<? super T> comparator) {
		Objects.requireNonNull(comparator);
		return unwrap().min(comparator);
	}

	/**
	 * 获取最大值
	 *
	 * @param comparator 一个用来比较大小的比较器{@link Comparator}
	 * @return 最大值
	 */
	@Override
	default Optional<T> max(final Comparator<? super T> comparator) {
		Objects.requireNonNull(comparator);
		return unwrap().max(comparator);
	}

	/**
	 * 返回流元素个数
	 *
	 * @return 流元素个数
	 */
	@Override
	default long count() {
		return unwrap().count();
	}

	/**
	 * 判断是否有任何一个元素满足给定断言
	 *
	 * @param predicate 断言
	 * @return 是否有任何一个元素满足给定断言
	 */
	@Override
	default boolean anyMatch(final Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		return unwrap().anyMatch(predicate);
	}

	/**
	 * 判断是否所有元素满足给定断言
	 *
	 * @param predicate 断言
	 * @return 是否所有元素满足给定断言
	 */
	@Override
	default boolean allMatch(final Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		return unwrap().allMatch(predicate);
	}

	/**
	 * 判断是否没有元素满足给定断言
	 *
	 * @param predicate 断言
	 * @return 是否没有元素满足给定断言
	 */
	@Override
	default boolean noneMatch(final Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		return unwrap().noneMatch(predicate);
	}

	/**
	 * 获取第一个元素
	 *
	 * @return 第一个元素
	 */
	@Override
	default Optional<T> findFirst() {
		return unwrap().findFirst();
	}

	/**
	 * 考虑性能，随便取一个，这里不是随机取一个，是随便取一个
	 *
	 * @return 随便取一个
	 */
	@Override
	default Optional<T> findAny() {
		return unwrap().findAny();
	}

	/**
	 * 返回流的迭代器
	 *
	 * @return 流的迭代器
	 */
	@Override
	default Iterator<T> iterator() {
		return unwrap().iterator();
	}

	/**
	 * 返回流的拆分器
	 *
	 * @return 流的拆分器
	 */
	@Override
	default Spliterator<T> spliterator() {
		return unwrap().spliterator();
	}

	/**
	 * 返回流的并行状态
	 *
	 * @return 流的并行状态
	 */
	@Override
	default boolean isParallel() {
		return unwrap().isParallel();
	}

	/**
	 * 返回一个串行流，该方法可以将并行流转换为串行流
	 *
	 * @return 串行流
	 */
	@Override
	default S sequential() {
		return wrap(unwrap().sequential());
	}

	/**
	 * 将流转换为并行
	 *
	 * @return 并行流
	 */
	@Override
	default S parallel() {
		return wrap(unwrap().parallel());
	}

	/**
	 * 返回一个无序流(无手动排序)
	 * <p>标记一个流是不在意元素顺序的, 在并行流的某些情况下可以提高性能</p>
	 *
	 * @return 无序流
	 */
	@Override
	default S unordered() {
		return wrap(unwrap().unordered());
	}

	/**
	 * 在流关闭时执行操作
	 *
	 * @param closeHandler 在流关闭时执行的操作
	 * @return 流
	 */
	@Override
	default S onClose(final Runnable closeHandler) {
		return wrap(unwrap().onClose(closeHandler));
	}

	/**
	 * 关闭流
	 *
	 * @see AutoCloseable#close()
	 */
	@Override
	default void close() {
		unwrap().close();
	}

	/**
	 * 获取当前实例的哈希值
	 *
	 * @return 哈希值
	 */
	@Override
	int hashCode();

	/**
	 * 比较实例是否相等
	 *
	 * @param obj 对象
	 * @return 是否相等
	 */
	@Override
	boolean equals(final Object obj);

	/**
	 * 将当前实例转为字符串
	 *
	 * @return 字符串
	 */
	@Override
	String toString();

	/**
	 * 转换为EasyStream
	 *
	 * @return 转换后的EasyStream
	 */
	@SuppressWarnings("unchecked")
	default EasyStream<T> easyStream() {
		if (this instanceof EasyStream) {
			return (EasyStream<T>) this;
		} else if (this instanceof Iterator) {
			return (EasyStream<T>) EasyStream.of((Iterator<T>) this);
		} else {
			return EasyStream.of(collect(Collectors.toList()));
		}
	}

}
