package cn.hutool.core.stream;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * <p>{@link Stream}实例的包装器，用于增强原始的{@link Stream}，提供一些额外的中间与终端操作 <br>
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
	 * 代表不存在的下标, 一般用于并行流的下标, 或者未找到元素时的下标
	 */
	int NOT_FOUND_ELEMENT_INDEX = -1;

	/**
	 * 获取被包装的原始流
	 *
	 * @return 被包装的原始流
	 */
	Stream<T> stream();

	/**
	 * 将一个原始流包装为指定类型的增强流
	 *
	 * @param source 被包装的流
	 * @return S
	 */
	S wrapping(Stream<T> source);

	/**
	 * 过滤元素，返回与指定断言匹配的元素组成的流
	 * 这是一个无状态中间操作
	 *
	 * @param predicate 断言
	 * @return 返回叠加过滤操作后的流
	 */
	@Override
	default S filter(Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		return wrapping(stream().filter(predicate));
	}

	/**
	 * 和{@link EasyStream#map(Function)}一样，只不过函数的返回值必须为int类型
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 返回值为int类型的函数
	 * @return 叠加操作后元素类型全为int的流
	 */
	@Override
	default IntStream mapToInt(ToIntFunction<? super T> mapper) {
		Objects.requireNonNull(mapper);
		return stream().mapToInt(mapper);
	}

	/**
	 * 和{@link EasyStream#map(Function)}一样，只不过函数的返回值必须为long类型
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 返回值为long类型的函数
	 * @return 叠加操作后元素类型全为long的流
	 */
	@Override
	default LongStream mapToLong(ToLongFunction<? super T> mapper) {
		Objects.requireNonNull(mapper);
		return stream().mapToLong(mapper);
	}

	/**
	 * 和{@link EasyStream#map(Function)}一样，只不过函数的返回值必须为double类型
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 返回值为double类型的函数
	 * @return 叠加操作后元素类型全为double的流
	 */
	@Override
	default DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
		Objects.requireNonNull(mapper);
		return stream().mapToDouble(mapper);
	}

	/**
	 * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 操作，返回IntStream
	 * @return 返回叠加拆分操作后的IntStream
	 */
	@Override
	default IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
		Objects.requireNonNull(mapper);
		return stream().flatMapToInt(mapper);
	}

	/**
	 * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 操作，返回LongStream
	 * @return 返回叠加拆分操作后的LongStream
	 */
	@Override
	default LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
		Objects.requireNonNull(mapper);
		return stream().flatMapToLong(mapper);
	}

	/**
	 * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 操作，返回DoubleStream
	 * @return 返回叠加拆分操作后的DoubleStream
	 */
	@Override
	default DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
		Objects.requireNonNull(mapper);
		return stream().flatMapToDouble(mapper);
	}

	/**
	 * 返回一个具有去重特征的流 非并行流(顺序流)下对于重复元素，保留遇到顺序中最先出现的元素，并行流情况下不能保证具体保留哪一个
	 * 这是一个有状态中间操作
	 *
	 * @return 一个具有去重特征的流
	 */
	@Override
	default S distinct() {
		return wrapping(stream().distinct());
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
		return wrapping(stream().sorted());
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
	default S sorted(Comparator<? super T> comparator) {
		Objects.requireNonNull(comparator);
		return wrapping(stream().sorted(comparator));
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
	default S peek(Consumer<? super T> action) {
		Objects.requireNonNull(action);
		return wrapping(stream().peek(action));
	}

	/**
	 * 返回截取后面一些元素的流
	 * 这是一个短路状态中间操作
	 *
	 * @param maxSize 元素截取后的个数
	 * @return 截取后的流
	 */
	@Override
	default S limit(long maxSize) {
		return wrapping(stream().limit(maxSize));
	}

	/**
	 * 返回丢弃前面n个元素后的剩余元素组成的流，如果当前元素个数小于n，则返回一个元素为空的流
	 * 这是一个有状态中间操作
	 *
	 * @param n 需要丢弃的元素个数
	 * @return 丢弃前面n个元素后的剩余元素组成的流
	 */
	@Override
	default S skip(long n) {
		return wrapping(stream().skip(n));
	}

	/**
	 * 对流里面的每一个元素执行一个操作
	 * 这是一个终端操作
	 *
	 * @param action 操作
	 */
	@Override
	default void forEach(Consumer<? super T> action) {
		Objects.requireNonNull(action);
		stream().forEach(action);
	}

	/**
	 * 对流里面的每一个元素按照顺序执行一个操作
	 * 这是一个终端操作
	 *
	 * @param action 操作
	 */
	@Override
	default void forEachOrdered(Consumer<? super T> action) {
		Objects.requireNonNull(action);
		stream().forEachOrdered(action);
	}

	/**
	 * 返回一个包含此流元素的数组
	 * 这是一个终端操作
	 *
	 * @return 包含此流元素的数组
	 */
	@Override
	default Object[] toArray() {
		return stream().toArray();
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
	default <A> A[] toArray(IntFunction<A[]> generator) {
		Objects.requireNonNull(generator);
		return stream().toArray(generator);
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
	default T reduce(T identity, BinaryOperator<T> accumulator) {
		Objects.requireNonNull(accumulator);
		return stream().reduce(identity, accumulator);
	}

	/**
	 * 对元素进行聚合，并返回聚合后用 {@link Optional}包裹的值，相当于在for循环里写sum=sum+ints[i]
	 * 该操作相当于：
	 * <pre>{@code
	 *     boolean foundAny = false;
	 *     T result = null;
	 *     for (T element : this stream) {
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
	default Optional<T> reduce(BinaryOperator<T> accumulator) {
		Objects.requireNonNull(accumulator);
		return stream().reduce(accumulator);
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
	default <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
		Objects.requireNonNull(accumulator);
		Objects.requireNonNull(combiner);
		return stream().reduce(identity, accumulator, combiner);
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
	default <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
		Objects.requireNonNull(supplier);
		Objects.requireNonNull(accumulator);
		Objects.requireNonNull(combiner);
		return stream().collect(supplier, accumulator, combiner);
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
	default <R, A> R collect(Collector<? super T, A, R> collector) {
		Objects.requireNonNull(collector);
		return stream().collect(collector);
	}

	/**
	 * 获取最小值
	 *
	 * @param comparator 一个用来比较大小的比较器{@link Comparator}
	 * @return 最小值
	 */
	@Override
	default Optional<T> min(Comparator<? super T> comparator) {
		Objects.requireNonNull(comparator);
		return stream().min(comparator);
	}

	/**
	 * 获取最大值
	 *
	 * @param comparator 一个用来比较大小的比较器{@link Comparator}
	 * @return 最大值
	 */
	@Override
	default Optional<T> max(Comparator<? super T> comparator) {
		Objects.requireNonNull(comparator);
		return stream().max(comparator);
	}

	/**
	 * 返回流元素个数
	 *
	 * @return 流元素个数
	 */
	@Override
	default long count() {
		return stream().count();
	}

	/**
	 * 判断是否有任何一个元素满足给定断言
	 *
	 * @param predicate 断言
	 * @return 是否有任何一个元素满足给定断言
	 */
	@Override
	default boolean anyMatch(Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		return stream().anyMatch(predicate);
	}

	/**
	 * 判断是否所有元素满足给定断言
	 *
	 * @param predicate 断言
	 * @return 是否所有元素满足给定断言
	 */
	@Override
	default boolean allMatch(Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		return stream().allMatch(predicate);
	}

	/**
	 * 判断是否没有元素满足给定断言
	 *
	 * @param predicate 断言
	 * @return 是否没有元素满足给定断言
	 */
	@Override
	default boolean noneMatch(Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		return stream().noneMatch(predicate);
	}

	/**
	 * 获取第一个元素
	 *
	 * @return 第一个元素
	 */
	@Override
	default Optional<T> findFirst() {
		return stream().findFirst();
	}

	/**
	 * 考虑性能，随便取一个，这里不是随机取一个，是随便取一个
	 *
	 * @return 随便取一个
	 */
	@Override
	default Optional<T> findAny() {
		return stream().findAny();
	}

	/**
	 * 返回流的迭代器
	 *
	 * @return 流的迭代器
	 */
	@Override
	default Iterator<T> iterator() {
		return stream().iterator();
	}

	/**
	 * 返回流的拆分器
	 *
	 * @return 流的拆分器
	 */
	@Override
	default Spliterator<T> spliterator() {
		return stream().spliterator();
	}

	/**
	 * 返回流的并行状态
	 *
	 * @return 流的并行状态
	 */
	@Override
	default boolean isParallel() {
		return stream().isParallel();
	}

	/**
	 * 返回一个串行流，该方法可以将并行流转换为串行流
	 *
	 * @return 串行流
	 */
	@Override
	default S sequential() {
		return wrapping(stream().sequential());
	}

	/**
	 * 将流转换为并行
	 *
	 * @return 并行流
	 */
	@Override
	default S parallel() {
		return wrapping(stream().parallel());
	}

	/**
	 * 返回一个无序流(无手动排序)
	 * <p>标记一个流是不在意元素顺序的, 在并行流的某些情况下可以提高性能</p>
	 *
	 * @return 无序流
	 */
	@Override
	default S unordered() {
		return wrapping(stream().unordered());
	}

	/**
	 * 在流关闭时执行操作
	 *
	 * @param closeHandler 在流关闭时执行的操作
	 * @return 流
	 */
	@Override
	default S onClose(Runnable closeHandler) {
		return wrapping(stream().onClose(closeHandler));
	}

	/**
	 * 关闭流
	 *
	 * @see AutoCloseable#close()
	 */
	@Override
	default void close() {
		stream().close();
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

}
