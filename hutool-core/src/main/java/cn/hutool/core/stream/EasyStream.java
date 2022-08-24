package cn.hutool.core.stream;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.mutable.MutableInt;
import cn.hutool.core.lang.mutable.MutableObj;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.ArrayUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * <p>{@link Stream}的扩展实现，基于原生Stream进行了封装和增强。<br />
 * 作者经对比了vavr、eclipse-collection、stream-ex以及其他语言的api，结合日常使用习惯，进行封装和拓展
 * Stream为集合提供了一些易用api，它让开发人员能使用声明式编程的方式去编写代码。
 *
 * <h3>中间操作和结束操作</h3>
 * <p>针对流的操作分为分为<em>中间操作</em>和<em>结束操作</em>,
 * 流只有在<em>结束操作</em>时才会真正触发执行以往的<em>中间操作</em>。<br />
 * <strong>中间操作</strong>：
 * <ul>
 *     <li>无状态中间操作: 表示不用等待 所有元素的当前操作执行完 就可以执行的操作，不依赖之前历史操作的流的状态；</li>
 *     <li>有状态中间操作: 表示需要等待 所有元素的当前操作执行完 才能执行的操作,依赖之前历史操作的流的状态；</li>
 * </ul>
 * <strong>结束操作</strong>：
 * <ul>
 *     <li>短路结束操作: 表示不用等待 所有元素的当前操作执行完 就可以执行的操作；</li>
 *     <li>非短路结束操作: 表示需要等待 所有元素的当前操作执行完 才能执行的操作；</li>
 * </ul>
 *
 * <h3>串行流与并行流</h3>
 * <p>流分为<em>串行流</em>和<em>并行流</em>两类：
 * <ul>
 *     <li>串行流：针对流的所有操作都会通过当前线程完成；</li>
 *     <li>
 *         并行流：针对流的操作会通过拆分器{@link Spliterator}拆分为多个异步任务{@link java.util.concurrent.ForkJoinTask}执行，
 *         这些异步任务默认使用{@link java.util.concurrent.ForkJoinPool}线程池进行管理；
 *     </li>
 * </ul>
 * 不同类型的流可以通过{@link #sequential()}或{@link #parallel()}互相转换。
 *
 * @author VampireAchao
 * @author emptypoint
 * @see java.util.stream.Stream
 * @since 6.0.0
 */
public class EasyStream<T> implements Stream<T>, Iterable<T> {
	/**
	 * 代表不存在的下标, 一般用于并行流的下标, 或者未找到元素时的下标
	 */
	private static final int NOT_FOUND_INDEX = -1;

	protected final Stream<T> stream;

	/**
	 * 构造
	 *
	 * @param stream {@link Stream}
	 */
	EasyStream(final Stream<T> stream) {
		this.stream = null == stream ? Stream.empty() : stream;
	}

	// region Static method
	// --------------------------------------------------------------- Static method start

	/**
	 * 返回{@code FastStream}的建造器
	 *
	 * @param <T> 元素的类型
	 * @return a stream builder
	 */
	public static <T> FastStreamBuilder<T> builder() {
		return new FastStreamBuilder<T>() {
			private static final long serialVersionUID = 1L;
			private final Builder<T> streamBuilder = Stream.builder();

			@Override
			public void accept(final T t) {
				streamBuilder.accept(t);
			}

			@Override
			public EasyStream<T> build() {
				return new EasyStream<>(streamBuilder.build());
			}
		};
	}

	/**
	 * 返回空的串行流
	 *
	 * @param <T> 元素类型
	 * @return 一个空的串行流
	 */
	public static <T> EasyStream<T> empty() {
		return new EasyStream<>(Stream.empty());
	}

	/**
	 * 返回包含单个元素的串行流
	 *
	 * @param t   单个元素
	 * @param <T> 元素类型
	 * @return 包含单个元素的串行流
	 */
	public static <T> EasyStream<T> of(final T t) {
		return new EasyStream<>(Stream.of(t));
	}

	/**
	 * 返回包含指定元素的串行流，若输入数组为{@code null}或空，则返回一个空的串行流
	 *
	 * @param values 指定元素
	 * @param <T>    元素类型
	 * @return 包含指定元素的串行流
	 * 从一个安全数组中创建流
	 */
	@SafeVarargs
	@SuppressWarnings("varargs")
	public static <T> EasyStream<T> of(final T... values) {
		return ArrayUtil.isEmpty(values) ? EasyStream.empty() : new EasyStream<>(Stream.of(values));
	}

	/**
	 * 通过实现了{@link Iterable}接口的对象创建串行流，若输入对象为{@code null}，则返回一个空的串行流
	 *
	 * @param iterable 实现了{@link Iterable}接口的对象
	 * @param <T>      元素类型
	 * @return 流
	 */
	public static <T> EasyStream<T> of(final Iterable<T> iterable) {
		return of(iterable, false);
	}

	/**
	 * 通过传入的{@link Iterable}创建流，若输入对象为{@code null}，则返回一个空的串行流
	 *
	 * @param iterable {@link Iterable}
	 * @param parallel 是否并行
	 * @param <T>      元素类型
	 * @return 流
	 */
	public static <T> EasyStream<T> of(final Iterable<T> iterable, final boolean parallel) {
		return Opt.ofNullable(iterable)
				.map(Iterable::spliterator)
				.map(spliterator -> StreamSupport.stream(spliterator, parallel))
				.map(EasyStream::new)
				.orElseGet(EasyStream::empty);
	}

	/**
	 * 通过传入的{@link Stream}创建流，若输入对象为{@code null}，则返回一个空的串行流
	 *
	 * @param stream {@link Stream}
	 * @param <T>    元素类型
	 * @return 流
	 */
	public static <T> EasyStream<T> of(final Stream<T> stream) {
		return new EasyStream<>(stream);
	}

	/**
	 * 返回无限有序流
	 * 该流由 初始值 以及执行 迭代函数 进行迭代获取到元素
	 * <p>
	 * 例如
	 * {@code FastStream.iterate(0, i -> i + 1)}
	 * 就可以创建从0开始，每次+1的无限流，使用{@link EasyStream#limit(long)}可以限制元素个数
	 * </p>
	 *
	 * @param <T>  元素类型
	 * @param seed 初始值
	 * @param f    用上一个元素作为参数执行并返回一个新的元素
	 * @return 无限有序流
	 */
	public static <T> EasyStream<T> iterate(final T seed, final UnaryOperator<T> f) {
		return new EasyStream<>(Stream.iterate(seed, f));
	}

	/**
	 * 返回无限有序流
	 * 该流由 初始值 然后判断条件 以及执行 迭代函数 进行迭代获取到元素
	 * <p>
	 * 例如
	 * {@code FastStream.iterate(0, i -> i < 3, i -> ++i)}
	 * 就可以创建包含元素0,1,2的流，使用{@link EasyStream#limit(long)}可以限制元素个数
	 * </p>
	 *
	 * @param <T>     元素类型
	 * @param seed    初始值
	 * @param hasNext 条件值
	 * @param next    用上一个元素作为参数执行并返回一个新的元素
	 * @return 无限有序流
	 */
	public static <T> EasyStream<T> iterate(final T seed, final Predicate<? super T> hasNext, final UnaryOperator<T> next) {
		Objects.requireNonNull(next);
		Objects.requireNonNull(hasNext);
		return new EasyStream<>(StreamUtil.iterate(seed, hasNext, next));
	}

	/**
	 * 返回无限串行无序流
	 * 其中每一个元素都由给定的{@code Supplier}生成
	 * 适用场景在一些生成常量流、随机元素等
	 *
	 * @param <T> 元素类型
	 * @param s   用来生成元素的 {@code Supplier}
	 * @return 无限串行无序流
	 */
	public static <T> EasyStream<T> generate(final Supplier<T> s) {
		return new EasyStream<>(Stream.generate(s));
	}

	/**
	 * 创建一个惰性拼接流，其元素是第一个流的所有元素，然后是第二个流的所有元素。
	 * 如果两个输入流都是有序的，则结果流是有序的，如果任一输入流是并行的，则结果流是并行的。
	 * 当结果流关闭时，两个输入流的关闭处理程序都会被调用。
	 *
	 * <p>从重复串行流进行拼接时可能会导致深度调用链甚至抛出 {@code StackOverflowException}</p>
	 *
	 * @param <T> 元素类型
	 * @param a   第一个流
	 * @param b   第二个流
	 * @return 拼接两个流之后的流
	 */
	public static <T> EasyStream<T> concat(final Stream<? extends T> a, final Stream<? extends T> b) {
		return new EasyStream<>(Stream.concat(a, b));
	}

	/**
	 * 拆分字符串，转换为串行流
	 *
	 * @param str   字符串
	 * @param regex 正则
	 * @return 拆分后元素组成的流
	 */
	public static EasyStream<String> split(final CharSequence str, final String regex) {
		return Opt.ofBlankAble(str).map(CharSequence::toString).map(s -> s.split(regex)).map(EasyStream::of).orElseGet(EasyStream::empty);
	}

	// --------------------------------------------------------------- Static method end
	// endregion

	/**
	 * 过滤元素，返回与指定断言匹配的元素组成的流
	 * 这是一个无状态中间操作
	 *
	 * @param predicate 断言
	 * @return 返回叠加过滤操作后的流
	 */
	@Override
	public EasyStream<T> filter(final Predicate<? super T> predicate) {
		return new EasyStream<>(stream.filter(predicate));
	}

	/**
	 * 过滤元素，返回与 指定操作结果 匹配 指定值 的元素组成的流
	 * 这是一个无状态中间操作
	 *
	 * @param <R>    返回类型
	 * @param mapper 操作
	 * @param value  用来匹配的值
	 * @return 与 指定操作结果 匹配 指定值 的元素组成的流
	 */
	public <R> EasyStream<T> filter(final Function<? super T, ? extends R> mapper, final R value) {
		Objects.requireNonNull(mapper);
		return filter(e -> Objects.equals(mapper.apply(e), value));
	}

	/**
	 * 过滤元素，返回与指定断言匹配的元素组成的流，断言带下标，并行流时下标永远为-1
	 * 这是一个无状态中间操作
	 *
	 * @param predicate 断言
	 * @return 返回叠加过滤操作后的流
	 */
	public EasyStream<T> filterIdx(final BiPredicate<? super T, Integer> predicate) {
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
	public EasyStream<T> nonNull() {
		return new EasyStream<>(stream.filter(Objects::nonNull));
	}

	/**
	 * 返回与指定函数将元素作为参数执行的结果组成的流
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 指定的函数
	 * @param <R>    函数执行后返回的类型
	 * @return 返回叠加操作后的流
	 */
	@Override
	public <R> EasyStream<R> map(final Function<? super T, ? extends R> mapper) {
		return new EasyStream<>(stream.map(mapper));
	}

	/**
	 * 返回 元素 转换后 并且不为 {@code null} 的 新元素组成的流<br>
	 * 这是一个无状态中间操作<br>
	 * <pre>{@code
	 * // 等价于先调用map再调用nonNull
	 * .map(...).nonNull()...
	 * }</pre>
	 *
	 * @param mapper 指定的函数
	 * @param <R>    函数执行后返回的类型
	 * @return 新元素组成的流
	 */
	public <R> EasyStream<R> mapNonNull(final Function<? super T, ? extends R> mapper) {
		return nonNull().<R>map(mapper).nonNull();
	}

	/**
	 * 返回与指定函数将元素作为参数执行的结果组成的流，操作带下标，并行流时下标永远为-1
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 指定的函数
	 * @param <R>    函数执行后返回的类型
	 * @return 返回叠加操作后的流
	 */
	public <R> EasyStream<R> mapIdx(final BiFunction<? super T, Integer, ? extends R> mapper) {
		Objects.requireNonNull(mapper);
		if (isParallel()) {
			return map(e -> mapper.apply(e, NOT_FOUND_INDEX));
		} else {
			final MutableInt index = new MutableInt(NOT_FOUND_INDEX);
			return map(e -> mapper.apply(e, index.incrementAndGet()));
		}
	}

	/**
	 * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流<br>
	 * 这是一个无状态中间操作<br>
	 * 例如，将users里所有user的id和parentId组合在一起，形成一个新的流:
	 * <pre>{@code
	 *     FastStream<Long> ids = FastStream.of(users).flatMap(user -> FastStream.of(user.getId(), user.getParentId()));
	 * }</pre>
	 *
	 * @param mapper 操作，返回流
	 * @param <R>    拆分后流的元素类型
	 * @return 返回叠加拆分操作后的流
	 */
	@Override
	public <R> EasyStream<R> flatMap(final Function<? super T, ? extends Stream<? extends R>> mapper) {
		return new EasyStream<>(stream.flatMap(mapper));
	}

	/**
	 * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流，操作带下标，并行流时下标永远为-1
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 操作，返回流
	 * @param <R>    拆分后流的元素类型
	 * @return 返回叠加拆分操作后的流
	 */
	public <R> EasyStream<R> flatMapIdx(final BiFunction<? super T, Integer, ? extends Stream<? extends R>> mapper) {
		Objects.requireNonNull(mapper);
		if (isParallel()) {
			return flatMap(e -> mapper.apply(e, NOT_FOUND_INDEX));
		} else {
			final MutableInt index = new MutableInt(NOT_FOUND_INDEX);
			return flatMap(e -> mapper.apply(e, index.incrementAndGet()));
		}
	}

	/**
	 * 和{@link EasyStream#map(Function)}一样，只不过函数的返回值必须为int类型
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 返回值为int类型的函数
	 * @return 叠加操作后元素类型全为int的流
	 */
	@Override
	public IntStream mapToInt(final ToIntFunction<? super T> mapper) {
		return stream.mapToInt(mapper);
	}

	/**
	 * 和{@link EasyStream#map(Function)}一样，只不过函数的返回值必须为long类型
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 返回值为long类型的函数
	 * @return 叠加操作后元素类型全为long的流
	 */
	@Override
	public LongStream mapToLong(final ToLongFunction<? super T> mapper) {
		return stream.mapToLong(mapper);
	}

	/**
	 * 和{@link EasyStream#map(Function)}一样，只不过函数的返回值必须为double类型
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 返回值为double类型的函数
	 * @return 叠加操作后元素类型全为double的流
	 */
	@Override
	public DoubleStream mapToDouble(final ToDoubleFunction<? super T> mapper) {
		return stream.mapToDouble(mapper);
	}

	/**
	 * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作, 转换为迭代器元素,
	 * 最后返回所有迭代器的所有元素组成的流<br>
	 * 这是一个无状态中间操作<br>
	 * 例如，将users里所有user的id和parentId组合在一起，形成一个新的流:
	 * <pre>{@code
	 *     FastStream<Long> ids = FastStream.of(users).flat(user -> FastStream.of(user.getId(), user.getParentId()));
	 * }</pre>
	 *
	 * @param mapper 操作，返回可迭代对象
	 * @param <R>    拆分后流的元素类型
	 * @return 返回叠加拆分操作后的流
	 */
	public <R> EasyStream<R> flat(final Function<? super T, ? extends Iterable<? extends R>> mapper) {
		Objects.requireNonNull(mapper);
		return flatMap(w -> of(mapper.apply(w)));
	}

	/**
	 * 扩散流操作，可能影响流元素个数，对过滤后的非{@code null}元素执行mapper操作，转换为迭代器,
	 * 并过滤迭代器中为{@code null}的元素, 返回所有迭代器的所有非空元素组成的流<br>
	 * 这是一个无状态中间操作<br>
	 *
	 * @param mapper 操作，返回流
	 * @param <R>    拆分后流的元素类型
	 * @return 返回叠加拆分操作后的流
	 * @see #flat(Function)
	 * @see #nonNull()
	 */
	public <R> EasyStream<R> flatNonNull(final Function<? super T, ? extends Iterable<? extends R>> mapper) {
		return nonNull().flat(mapper).nonNull();
	}

	/**
	 * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 操作，返回IntStream
	 * @return 返回叠加拆分操作后的IntStream
	 */
	@Override
	public IntStream flatMapToInt(final Function<? super T, ? extends IntStream> mapper) {
		return stream.flatMapToInt(mapper);
	}

	/**
	 * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 操作，返回LongStream
	 * @return 返回叠加拆分操作后的LongStream
	 */
	@Override
	public LongStream flatMapToLong(final Function<? super T, ? extends LongStream> mapper) {
		return stream.flatMapToLong(mapper);
	}

	/**
	 * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 操作，返回DoubleStream
	 * @return 返回叠加拆分操作后的DoubleStream
	 */
	@Override
	public DoubleStream flatMapToDouble(final Function<? super T, ? extends DoubleStream> mapper) {
		return stream.flatMapToDouble(mapper);
	}

	/**
	 * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流，操作带一个方法，调用该方法可增加元素
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 操作，返回流
	 * @param <R>    拆分后流的元素类型
	 * @return 返回叠加拆分操作后的流
	 */
	public <R> EasyStream<R> mapMulti(final BiConsumer<? super T, ? super Consumer<R>> mapper) {
		Objects.requireNonNull(mapper);
		return flatMap(e -> {
			final FastStreamBuilder<R> buffer = EasyStream.builder();
			mapper.accept(e, buffer);
			return buffer.build();
		});
	}

	/**
	 * 返回一个具有去重特征的流 非并行流(顺序流)下对于重复元素，保留遇到顺序中最先出现的元素，并行流情况下不能保证具体保留哪一个
	 * 这是一个有状态中间操作
	 *
	 * @return 一个具有去重特征的流
	 */
	@Override
	public EasyStream<T> distinct() {
		return new EasyStream<>(stream.distinct());
	}

	/**
	 * 返回一个具有去重特征的流 非并行流(顺序流)下对于重复元素，保留遇到顺序中最先出现的元素，并行流情况下不能保证具体保留哪一个
	 * 这是一个有状态中间操作
	 *
	 * @param <F>          参数类型
	 * @param keyExtractor 去重依据
	 * @return 一个具有去重特征的流
	 */
	public <F> EasyStream<T> distinct(final Function<? super T, F> keyExtractor) {
		Objects.requireNonNull(keyExtractor);
		if (isParallel()) {
			final ConcurrentHashMap<F, Boolean> exists = MapUtil.newConcurrentHashMap();
			// 标记是否出现过null值，用于保留第一个出现的null
			// 由于ConcurrentHashMap的key不能为null，所以用此变量来标记
			final AtomicBoolean hasNull = new AtomicBoolean(false);
			return of(stream.filter(e -> {
				final F key = keyExtractor.apply(e);
				if (key == null) {
					// 已经出现过null值，跳过该值
					if (hasNull.get()) {
						return false;
					}
					hasNull.set(Boolean.TRUE);
					return true;
				} else {
					// 第一次出现的key返回true
					return null == exists.putIfAbsent(key, Boolean.TRUE);
				}
			})).parallel();
		} else {
			final Set<F> exists = new HashSet<>();
			return of(stream.filter(e -> exists.add(keyExtractor.apply(e))));
		}
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
	public EasyStream<T> sorted() {
		return new EasyStream<>(stream.sorted());
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
	public EasyStream<T> sorted(final Comparator<? super T> comparator) {
		return new EasyStream<>(stream.sorted(comparator));
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
	public EasyStream<T> peek(final Consumer<? super T> action) {
		return new EasyStream<>(stream.peek(action));
	}

	/**
	 * 返回与指定函数将元素作为参数执行后组成的流。操作带下标，并行流时下标永远为-1
	 * 这是一个无状态中间操作
	 * @param action 指定的函数
	 * @return 返回叠加操作后的FastStream
	 * @apiNote 该方法存在的意义主要是用来调试
	 * 当你需要查看经过操作管道某处的元素和下标，可以执行以下操作:
	 * <pre>{@code
	 *     .of("one", "two", "three", "four")
	 * 				.filter(e -> e.length() > 3)
	 * 				.peekIdx((e,i) -> System.out.println("Filtered value: " + e + " Filtered idx:" + i))
	 * 				.map(String::toUpperCase)
	 * 				.peekIdx((e,i) -> System.out.println("Mapped value: " + e + " Filtered idx:" + i))
	 * 				.collect(Collectors.toList());
	 * }</pre>
	 */
	public EasyStream<T> peekIdx(BiConsumer<? super T, Integer> action) {
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
	public EasyStream<T> log() {
		return peek(Console::log);
	}

	/**
	 * 返回截取后面一些元素的流
	 * 这是一个短路状态中间操作
	 *
	 * @param maxSize 元素截取后的个数
	 * @return 截取后的流
	 */
	@Override
	public EasyStream<T> limit(final long maxSize) {
		return new EasyStream<>(stream.limit(maxSize));
	}

	/**
	 * 返回丢弃前面n个元素后的剩余元素组成的流，如果当前元素个数小于n，则返回一个元素为空的流
	 * 这是一个有状态中间操作
	 *
	 * @param n 需要丢弃的元素个数
	 * @return 丢弃前面n个元素后的剩余元素组成的流
	 */
	@Override
	public EasyStream<T> skip(final long n) {
		return new EasyStream<>(stream.skip(n));
	}

	/**
	 * 返回一个串行流，该方法可以将并行流转换为串行流
	 *
	 * @return 串行流
	 */
	@Override
	public EasyStream<T> sequential() {
		//noinspection ResultOfMethodCallIgnored
		stream.sequential();
		return this;
	}

	/**
	 * 对流里面的每一个元素执行一个操作
	 * 这是一个终端操作
	 *
	 * @param action 操作
	 */
	@Override
	public void forEach(final Consumer<? super T> action) {
		stream.forEach(action);
	}

	/**
	 * 对流里面的每一个元素执行一个操作，操作带下标，并行流时下标永远为-1
	 * 这是一个终端操作
	 *
	 * @param action 操作
	 */
	public void forEachIdx(final BiConsumer<? super T, Integer> action) {
		Objects.requireNonNull(action);
		if (isParallel()) {
			stream.forEach(e -> action.accept(e, NOT_FOUND_INDEX));
		} else {
			final MutableInt index = new MutableInt(NOT_FOUND_INDEX);
			stream.forEach(e -> action.accept(e, index.incrementAndGet()));
		}
	}

	/**
	 * 对流里面的每一个元素按照顺序执行一个操作
	 * 这是一个终端操作
	 *
	 * @param action 操作
	 */
	@Override
	public void forEachOrdered(final Consumer<? super T> action) {
		stream.forEachOrdered(action);
	}

	/**
	 * 对流里面的每一个元素按照顺序执行一个操作，操作带下标，并行流时下标永远为-1
	 * 这是一个终端操作
	 *
	 * @param action 操作
	 */
	public void forEachOrderedIdx(final BiConsumer<? super T, Integer> action) {
		Objects.requireNonNull(action);
		if (isParallel()) {
			stream.forEachOrdered(e -> action.accept(e, NOT_FOUND_INDEX));
		} else {
			final MutableInt index = new MutableInt(NOT_FOUND_INDEX);
			stream.forEachOrdered(e -> action.accept(e, index.incrementAndGet()));
		}
	}

	/**
	 * 返回一个包含此流元素的数组
	 * 这是一个终端操作
	 *
	 * @return 包含此流元素的数组
	 */
	@Override
	public Object[] toArray() {
		return stream.toArray();
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
	public <A> A[] toArray(final IntFunction<A[]> generator) {
		//noinspection SuspiciousToArrayCall
		return stream.toArray(generator);
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
	public T reduce(final T identity, final BinaryOperator<T> accumulator) {
		return stream.reduce(identity, accumulator);
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
	public Optional<T> reduce(final BinaryOperator<T> accumulator) {
		return stream.reduce(accumulator);
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
	public <U> U reduce(final U identity, final BiFunction<U, ? super T, U> accumulator, final BinaryOperator<U> combiner) {
		return stream.reduce(identity, accumulator, combiner);
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
	public <R> R collect(final Supplier<R> supplier, final BiConsumer<R, ? super T> accumulator, final BiConsumer<R, R> combiner) {
		return stream.collect(supplier, accumulator, combiner);
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
	public <R, A> R collect(final Collector<? super T, A, R> collector) {
		return stream.collect(collector);
	}

	/**
	 * 获取最小值
	 *
	 * @param comparator 一个用来比较大小的比较器{@link Comparator}
	 * @return 最小值
	 */
	@Override
	public Optional<T> min(final Comparator<? super T> comparator) {
		return stream.min(comparator);
	}

	/**
	 * 获取最大值
	 *
	 * @param comparator 一个用来比较大小的比较器{@link Comparator}
	 * @return 最大值
	 */
	@Override
	public Optional<T> max(final Comparator<? super T> comparator) {
		return stream.max(comparator);
	}

	/**
	 * 返回流元素个数
	 *
	 * @return 流元素个数
	 */
	@Override
	public long count() {
		return stream.count();
	}

	/**
	 * 判断是否有任何一个元素满足给定断言
	 *
	 * @param predicate 断言
	 * @return 是否有任何一个元素满足给定断言
	 */
	@Override
	public boolean anyMatch(final Predicate<? super T> predicate) {
		return stream.anyMatch(predicate);
	}

	/**
	 * 判断是否所有元素满足给定断言
	 *
	 * @param predicate 断言
	 * @return 是否所有元素满足给定断言
	 */
	@Override
	public boolean allMatch(final Predicate<? super T> predicate) {
		return stream.allMatch(predicate);
	}

	/**
	 * 判断是否没有元素满足给定断言
	 *
	 * @param predicate 断言
	 * @return 是否没有元素满足给定断言
	 */
	@Override
	public boolean noneMatch(final Predicate<? super T> predicate) {
		return stream.noneMatch(predicate);
	}

	/**
	 * 获取第一个元素
	 *
	 * @return 第一个元素
	 */
	@Override
	public Optional<T> findFirst() {
		return stream.findFirst();
	}

	/**
	 * 获取与给定断言匹配的第一个元素
	 *
	 * @param predicate 断言
	 * @return 与给定断言匹配的第一个元素
	 */
	public Optional<T> findFirst(final Predicate<? super T> predicate) {
		return stream.filter(predicate).findFirst();
	}

	/**
	 * 获取与给定断言匹配的第一个元素的下标，并行流下标永远为-1
	 *
	 * @param predicate 断言
	 * @return 与给定断言匹配的第一个元素的下标，如果不存在则返回-1
	 */
	public int findFirstIdx(final Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		if (isParallel()) {
			return NOT_FOUND_INDEX;
		} else {
			final MutableInt index = new MutableInt(NOT_FOUND_INDEX);
			//noinspection ResultOfMethodCallIgnored
			stream.filter(e -> {
				index.increment();
				return predicate.test(e);
			}).findFirst();
			return index.get();
		}
	}

	/**
	 * 获取最后一个元素
	 *
	 * @return 最后一个元素
	 */
	public Optional<T> findLast() {
		final MutableObj<T> last = new MutableObj<>(null);
		spliterator().forEachRemaining(last::set);
		return Optional.ofNullable(last.get());
	}

	/**
	 * 获取与给定断言匹配的最后一个元素
	 *
	 * @param predicate 断言
	 * @return 与给定断言匹配的最后一个元素
	 */
	public Optional<T> findLast(final Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		final MutableObj<T> last = new MutableObj<>(null);
		spliterator().forEachRemaining(e -> {
			if (predicate.test(e)) {
				last.set(e);
			}
		});
		return Optional.ofNullable(last.get());
	}

	/**
	 * 获取与给定断言匹配的最后一个元素的下标，并行流下标永远为-1
	 *
	 * @param predicate 断言
	 * @return 与给定断言匹配的最后一个元素的下标，如果不存在则返回-1
	 */
	public int findLastIdx(final Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		if (isParallel()) {
			return NOT_FOUND_INDEX;
		} else {
			final MutableInt idxRef = new MutableInt(NOT_FOUND_INDEX);
			forEachIdx((e, i) -> {
				if (predicate.test(e)) {
					idxRef.set(i);
				}
			});
			return idxRef.get();
		}
	}

	/**
	 * 反转顺序
	 *
	 * @return 反转元素顺序
	 */
	public EasyStream<T> reverse() {
		//noinspection unchecked
		final T[] array = (T[]) toArray();
		ArrayUtil.reverse(array);
		return of(array).parallel(isParallel()).onClose(stream::close);
	}

	/**
	 * 考虑性能，随便取一个，这里不是随机取一个，是随便取一个
	 *
	 * @return 随便取一个
	 */
	@Override
	public Optional<T> findAny() {
		return stream.findAny();
	}

	/**
	 * 返回流的迭代器
	 *
	 * @return 流的迭代器
	 */
	@Override
	public Iterator<T> iterator() {
		return stream.iterator();
	}

	/**
	 * 返回流的拆分器
	 *
	 * @return 流的拆分器
	 */
	@Override
	public Spliterator<T> spliterator() {
		return stream.spliterator();
	}

	/**
	 * 将流转换为并行
	 *
	 * @return 并行流
	 */
	@Override
	public EasyStream<T> parallel() {
		//noinspection ResultOfMethodCallIgnored
		stream.parallel();
		return this;
	}

	/**
	 * 更改流的并行状态
	 *
	 * @param parallel 是否并行
	 * @return 流
	 */
	public EasyStream<T> parallel(final boolean parallel) {
		return parallel ? parallel() : sequential();
	}

	/**
	 * 返回一个无序流(无手动排序)
	 * <p>标记一个流是不在意元素顺序的, 在并行流的某些情况下可以提高性能</p>
	 *
	 * @return 无序流
	 */
	@Override
	public EasyStream<T> unordered() {
		return new EasyStream<>(stream.unordered());
	}

	/**
	 * 在流关闭时执行操作
	 *
	 * @param closeHandler 在流关闭时执行的操作
	 * @return 流
	 */
	@Override
	public EasyStream<T> onClose(final Runnable closeHandler) {
		//noinspection ResultOfMethodCallIgnored
		stream.onClose(closeHandler);
		return this;
	}

	/**
	 * 与给定元素组成的流合并，成为新的流
	 *
	 * @param obj 元素
	 * @return 流
	 */
	public EasyStream<T> push(final T obj) {
		return EasyStream.concat(this.stream, of(obj));
	}

	/**
	 * 与给定元素组成的流合并，成为新的流
	 *
	 * @param obj 元素
	 * @return 流
	 */
	@SuppressWarnings("unchecked")
	public EasyStream<T> push(final T... obj) {
		return EasyStream.concat(this.stream, of(obj));
	}

	/**
	 * 给定元素组成的流与当前流合并，成为新的流
	 *
	 * @param obj 元素
	 * @return 流
	 */
	public EasyStream<T> unshift(final T obj) {
		return EasyStream.concat(of(obj), this.stream);
	}

	/**
	 * 给定元素组成的流与当前流合并，成为新的流
	 *
	 * @param obj 元素
	 * @return 流
	 */
	@SafeVarargs
	public final EasyStream<T> unshift(final T... obj) {
		return EasyStream.concat(of(obj), this.stream);
	}

	/**
	 * 获取流中指定下标的元素，如果是负数，则从最后一个开始数起
	 *
	 * @param idx 下标
	 * @return 指定下标的元素
	 */
	public Optional<T> at(final Integer idx) {
		return Opt.ofNullable(idx).map(i -> {
			//noinspection unchecked
			return (T) ArrayUtil.get(toArray(), i);
		}).toOptional();
	}

	/**
	 * 返回流的并行状态
	 *
	 * @return 流的并行状态
	 */
	@Override
	public boolean isParallel() {
		return stream.isParallel();
	}

	/**
	 * 关闭流
	 *
	 * @see AutoCloseable#close()
	 */
	@Override
	public void close() {
		stream.close();
	}

	/**
	 * hashcode
	 *
	 * @return hashcode
	 */
	@Override
	public int hashCode() {
		return stream.hashCode();
	}

	/**
	 * equals
	 *
	 * @param obj 对象
	 * @return 结果
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Stream) {
			return stream.equals(obj);
		}
		return false;
	}

	/**
	 * toString
	 *
	 * @return string
	 */
	@Override
	public String toString() {
		return stream.toString();
	}

	/**
	 * 转换成集合
	 *
	 * @param collectionFactory 集合工厂(可以是集合构造器)
	 * @param <C>               集合类型
	 * @return 集合
	 */
	public <C extends Collection<T>> C toColl(final Supplier<C> collectionFactory) {
		return collect(Collectors.toCollection(collectionFactory));
	}

	/**
	 * 转换为ArrayList
	 *
	 * @return list
	 */
	public List<T> toList() {
		return collect(Collectors.toList());
	}

	/**
	 * 转换为HashSet
	 *
	 * @return hashSet
	 */
	public Set<T> toSet() {
		return collect(Collectors.toSet());
	}

	/**
	 * 与给定的可迭代对象转换成Map，key为现有元素，value为给定可迭代对象迭代的元素<br>
	 * Map的大小与两个集合中较小的数量一致, 即, 只合并下标位置相同的部分
	 *
	 * @param other 可迭代对象
	 * @param <R>   可迭代对象迭代的元素类型
	 * @return map，key为现有元素，value为给定可迭代对象迭代的元素
	 */
	public <R> Map<T, R> toZip(final Iterable<R> other) {
		final Spliterator<T> keys = spliterator();
		final Spliterator<R> values = Opt.ofNullable(other).map(Iterable::spliterator).orElseGet(Spliterators::emptySpliterator);
		// 获取两个Spliterator的中较小的数量
		// 如果Spliterator经过流操作, getExactSizeIfKnown()可能会返回-1, 所以默认大小为 MapUtil.DEFAULT_INITIAL_CAPACITY
		final int sizeIfKnown = (int) Math.max(Math.min(keys.getExactSizeIfKnown(), values.getExactSizeIfKnown()), MapUtil.DEFAULT_INITIAL_CAPACITY);
		final Map<T, R> map = MapUtil.newHashMap(sizeIfKnown);
		// 保存第一个Spliterator的值
		final MutableObj<T> key = new MutableObj<>();
		// 保存第二个Spliterator的值
		final MutableObj<R> value = new MutableObj<>();
		// 当两个Spliterator中都还有剩余元素时
		while (keys.tryAdvance(key::set) && values.tryAdvance(value::set)) {
			map.put(key.get(), value.get());
		}
		return map;
	}

	/**
	 * 返回拼接后的字符串
	 *
	 * @return 拼接后的字符串
	 */
	public String join() {
		return join(StrUtil.EMPTY);
	}

	/**
	 * 返回拼接后的字符串
	 *
	 * @param delimiter 分隔符
	 * @return 拼接后的字符串
	 */
	public String join(final CharSequence delimiter) {
		return join(delimiter, StrUtil.EMPTY, StrUtil.EMPTY);
	}

	/**
	 * 返回拼接后的字符串
	 *
	 * @param delimiter 分隔符
	 * @param prefix    前缀
	 * @param suffix    后缀
	 * @return 拼接后的字符串
	 */
	public String join(final CharSequence delimiter,
					   final CharSequence prefix,
					   final CharSequence suffix) {
		return map(String::valueOf).collect(Collectors.joining(delimiter, prefix, suffix));
	}

	/**
	 * 转换为map，key为给定操作执行后的返回值,value为当前元素
	 *
	 * @param keyMapper 指定的key操作
	 * @param <K>       key类型
	 * @return map
	 */
	public <K> Map<K, T> toMap(final Function<? super T, ? extends K> keyMapper) {
		return toMap(keyMapper, Function.identity());
	}

	/**
	 * 转换为map，key,value为给定操作执行后的返回值
	 *
	 * @param keyMapper   指定的key操作
	 * @param valueMapper 指定value操作
	 * @param <K>         key类型
	 * @param <U>         value类型
	 * @return map
	 */
	public <K, U> Map<K, U> toMap(final Function<? super T, ? extends K> keyMapper,
								  final Function<? super T, ? extends U> valueMapper) {
		return toMap(keyMapper, valueMapper, (l, r) -> r);
	}

	/**
	 * 转换为map，key,value为给定操作执行后的返回值
	 *
	 * @param keyMapper     指定的key操作
	 * @param valueMapper   指定value操作
	 * @param mergeFunction 合并操作
	 * @param <K>           key类型
	 * @param <U>           value类型
	 * @return map
	 */
	public <K, U> Map<K, U> toMap(final Function<? super T, ? extends K> keyMapper,
								  final Function<? super T, ? extends U> valueMapper,
								  final BinaryOperator<U> mergeFunction) {
		return toMap(keyMapper, valueMapper, mergeFunction, HashMap::new);
	}

	/**
	 * 转换为map，key,value为给定操作执行后的返回值
	 *
	 * @param keyMapper     指定的key操作
	 * @param valueMapper   指定value操作
	 * @param mergeFunction 合并操作
	 * @param mapSupplier   map工厂
	 * @param <K>           key类型
	 * @param <U>           value类型
	 * @param <M>           map类型
	 * @return map
	 */
	public <K, U, M extends Map<K, U>> M toMap(final Function<? super T, ? extends K> keyMapper,
											   final Function<? super T, ? extends U> valueMapper,
											   final BinaryOperator<U> mergeFunction,
											   final Supplier<M> mapSupplier) {
		return collect(CollectorUtil.toMap(keyMapper, valueMapper, mergeFunction, mapSupplier));
	}

	/**
	 * 将集合转换为树，默认用 {@code parentId == null} 作为顶部，内置一个小递归
	 * 因为需要在当前传入数据里查找，所以这是一个结束操作
	 *
	 * @param idGetter       id的getter对应的lambda，可以写作 {@code Student::getId}
	 * @param pIdGetter      parentId的getter对应的lambda，可以写作 {@code Student::getParentId}
	 * @param childrenSetter children的setter对应的lambda，可以写作{ @code Student::setChildren}
	 * @param <R>            此处是id、parentId的泛型限制
	 * @return list 组装好的树
	 * eg:
	 * {@code List studentTree = EasyStream.of(students).toTree(Student::getId, Student::getParentId, Student::setChildren) }
	 */
	public <R extends Comparable<R>> List<T> toTree(Function<T, R> idGetter,
													Function<T, R> pIdGetter,
													BiConsumer<T, List<T>> childrenSetter) {
		Map<R, List<T>> pIdValuesMap = group(pIdGetter);
		return getChildrenFromMapByPidAndSet(idGetter, childrenSetter, pIdValuesMap, pIdValuesMap.get(null));
	}

	/**
	 * 将集合转换为树，自定义树顶部的判断条件，内置一个小递归(没错，lambda可以写递归)
	 * 因为需要在当前传入数据里查找，所以这是一个结束操作
	 *
	 * @param idGetter        id的getter对应的lambda，可以写作 {@code Student::getId}
	 * @param pIdGetter       parentId的getter对应的lambda，可以写作 {@code Student::getParentId}
	 * @param childrenSetter  children的setter对应的lambda，可以写作 {@code Student::setChildren}
	 * @param parentPredicate 树顶部的判断条件，可以写作 {@code s -> Objects.equals(s.getParentId(),0L) }
	 * @param <R>             此处是id、parentId的泛型限制
	 * @return list 组装好的树
	 * eg:
	 * {@code List studentTree = EasyStream.of(students).toTree(Student::getId, Student::getParentId, Student::setChildren, Student::getMatchParent) }
	 */

	public <R extends Comparable<R>> List<T> toTree(Function<T, R> idGetter,
													Function<T, R> pIdGetter,
													BiConsumer<T, List<T>> childrenSetter,
													Predicate<T> parentPredicate) {
		List<T> list = toList();
		List<T> parents = EasyStream.of(list).filter(e ->
						// 此处是为了适配 parentPredicate.test空指针 情况
						// 因为Predicate.test的返回值是boolean，所以如果 e -> null 这种返回null的情况，会直接抛出NPE
						Opt.ofTry(() -> parentPredicate.test(e)).filter(Boolean::booleanValue).isPresent())
				.toList();
		return getChildrenFromMapByPidAndSet(idGetter, childrenSetter, EasyStream.of(list).group(pIdGetter), parents);
	}

	/**
	 * toTree的内联函数，内置一个小递归(没错，lambda可以写递归)
	 * 因为需要在当前传入数据里查找，所以这是一个结束操作
	 *
	 * @param idGetter       id的getter对应的lambda，可以写作 {@code Student::getId}
	 * @param childrenSetter children的setter对应的lambda，可以写作 {@code Student::setChildren}
	 * @param pIdValuesMap   parentId和值组成的map，用来降低复杂度
	 * @param parents        顶部数据
	 * @param <R>            此处是id的泛型限制
	 * @return list 组装好的树
	 */
	private <R extends Comparable<R>> List<T> getChildrenFromMapByPidAndSet(Function<T, R> idGetter,
																			BiConsumer<T, List<T>> childrenSetter,
																			Map<R, List<T>> pIdValuesMap,
																			List<T> parents) {
		MutableObj<Consumer<List<T>>> recursiveRef = new MutableObj<>();
		Consumer<List<T>> recursive = values -> EasyStream.of(values, isParallel()).forEach(value -> {
			List<T> children = pIdValuesMap.get(idGetter.apply(value));
			childrenSetter.accept(value, children);
			recursiveRef.get().accept(children);
		});
		recursiveRef.set(recursive);
		recursive.accept(parents);
		return parents;
	}

	/**
	 * 将树递归扁平化为集合，内置一个小递归(没错，lambda可以写递归)
	 * 这是一个无状态中间操作
	 *
	 * @param childrenGetter 获取子节点的lambda，可以写作 {@code Student::getChildren}
	 * @param childrenSetter 设置子节点的lambda，可以写作 {@code Student::setChildren}
	 * @return EasyStream 一个流
	 * eg:
	 * {@code List students = EasyStream.of(studentTree).flatTree(Student::getChildren, Student::setChildren).toList() }
	 */
	public EasyStream<T> flatTree(Function<T, List<T>> childrenGetter, BiConsumer<T, List<T>> childrenSetter) {
		MutableObj<Function<T, EasyStream<T>>> recursiveRef = new MutableObj<>();
		Function<T, EasyStream<T>> recursive = e -> EasyStream.of(childrenGetter.apply(e)).flat(recursiveRef.get()).unshift(e);
		recursiveRef.set(recursive);
		return flat(recursive).peek(e -> childrenSetter.accept(e, null));
	}


	/**
	 * 通过给定分组依据进行分组
	 *
	 * @param classifier 分组依据
	 * @param <K>        实体中的分组依据对应类型，也是Map中key的类型
	 * @return {@link Collector}
	 */
	public <K> Map<K, List<T>> group(final Function<? super T, ? extends K> classifier) {
		return group(classifier, Collectors.toList());
	}

	/**
	 * 通过给定分组依据进行分组
	 *
	 * @param classifier 分组依据
	 * @param downstream 下游操作
	 * @param <K>        实体中的分组依据对应类型，也是Map中key的类型
	 * @param <D>        下游操作对应返回类型，也是Map中value的类型
	 * @param <A>        下游操作在进行中间操作时对应类型
	 * @return {@link Collector}
	 */
	public <K, A, D> Map<K, D> group(final Function<? super T, ? extends K> classifier,
									 final Collector<? super T, A, D> downstream) {
		return group(classifier, HashMap::new, downstream);
	}

	/**
	 * 通过给定分组依据进行分组
	 *
	 * @param classifier 分组依据
	 * @param mapFactory 提供的map
	 * @param downstream 下游操作
	 * @param <K>        实体中的分组依据对应类型，也是Map中key的类型
	 * @param <D>        下游操作对应返回类型，也是Map中value的类型
	 * @param <A>        下游操作在进行中间操作时对应类型
	 * @param <M>        最后返回结果Map类型
	 * @return {@link Collector}
	 */
	public <K, D, A, M extends Map<K, D>> M group(final Function<? super T, ? extends K> classifier,
												  final Supplier<M> mapFactory,
												  final Collector<? super T, A, D> downstream) {
		return collect(CollectorUtil.groupingBy(classifier, mapFactory, downstream));
	}

	/**
	 * 将 现有元素 与 给定迭代器中对应位置的元素 使用 zipper 转换为新的元素，并返回新元素组成的流<br>
	 * 新流的数量为两个集合中较小的数量, 即, 只合并下标位置相同的部分<br>
	 *
	 * @param other  给定的迭代器
	 * @param zipper 两个元素的合并器
	 * @param <U>    给定的迭代对象类型
	 * @param <R>    合并后的结果对象类型
	 * @return 合并后的结果对象的流
	 */
	public <U, R> EasyStream<R> zip(final Iterable<U> other,
									final BiFunction<? super T, ? super U, ? extends R> zipper) {
		Objects.requireNonNull(zipper);
		final Spliterator<T> keys = spliterator();
		final Spliterator<U> values = Opt.ofNullable(other).map(Iterable::spliterator).orElseGet(Spliterators::emptySpliterator);
		// 获取两个Spliterator的中较小的数量
		// 如果Spliterator经过流操作, getExactSizeIfKnown()可能会返回-1, 所以默认大小为 ArrayList.DEFAULT_CAPACITY
		final int sizeIfKnown = (int) Math.max(Math.min(keys.getExactSizeIfKnown(), values.getExactSizeIfKnown()), 10);
		final List<R> list = new ArrayList<>(sizeIfKnown);
		// 保存第一个Spliterator的值
		final MutableObj<T> key = new MutableObj<>();
		// 保存第二个Spliterator的值
		final MutableObj<U> value = new MutableObj<>();
		// 当两个Spliterator中都还有剩余元素时
		while (keys.tryAdvance(key::set) && values.tryAdvance(value::set)) {
			list.add(zipper.apply(key.get(), value.get()));
		}
		return of(list).parallel(isParallel()).onClose(stream::close);
	}

	/**
	 * 类似js的<a href="https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Array/splice">splice</a>函数
	 *
	 * @param start       起始下标
	 * @param deleteCount 删除个数，正整数
	 * @param items       放入值
	 * @return 操作后的流
	 */
	@SafeVarargs
	public final EasyStream<T> splice(final int start, final int deleteCount, final T... items) {
		return of(ListUtil.splice(toList(), start, deleteCount, items))
				.parallel(isParallel())
				.onClose(stream::close);
	}

	/**
	 * 按指定长度切分为双层流
	 * <p>
	 * 形如：[1,2,3,4,5] -&gt; [[1,2], [3,4], [5,6]]
	 * </p>
	 *
	 * @param batchSize 指定长度, 正整数
	 * @return 切好的流
	 */
	public EasyStream<EasyStream<T>> split(final int batchSize) {
		final List<T> list = toList();
		final int size = list.size();
		// 指定长度 大于等于 列表长度
		if (size <= batchSize) {
			// 返回第一层只有单个元素的双层流，形如：[[1,2,3,4,5]]
			return EasyStream.<EasyStream<T>>of(of(list, isParallel()));
		}
		return iterate(0, i -> i < size, i -> i + batchSize)
				.map(skip -> of(list.subList(skip, Math.min(size, skip + batchSize)), isParallel()))
				.parallel(isParallel())
				.onClose(stream::close);
	}

	/**
	 * 按指定长度切分为元素为list的流
	 * <p>
	 * 形如：[1,2,3,4,5] -&gt; [[1,2], [3,4], [5,6]]
	 * </p>
	 *
	 * @param batchSize 指定长度, 正整数
	 * @return 切好的流
	 */
	public EasyStream<List<T>> splitList(final int batchSize) {
		return split(batchSize).map(EasyStream::toList);
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
	public EasyStream<T> takeWhile(final Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		return of(StreamUtil.takeWhile(stream, predicate));
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
	public EasyStream<T> dropWhile(final Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		return of(StreamUtil.dropWhile(stream, predicate));
	}

	/**
	 * 流是否为空
	 *
	 * @return 流是否为空
	 */
	public boolean isEmpty() {
		return !findAny().isPresent();
	}

	/**
	 * 流是否不为空
	 *
	 * @return 流是否不为空
	 */
	public boolean isNotEmpty() {
		return !isEmpty();
	}

	/**
	 * 将当前流转为另一对象。用于提供针对流本身而非流中元素的操作
	 *
	 * @param <R>       转换类型
	 * @param transform 转换
	 * @return 转换后的流
	 */
	public <R> Optional<R> transform(final Function<EasyStream<T>, R> transform) {
		Assert.notNull(transform, "transform must not null");
		return Optional.ofNullable(transform.apply(this));
	}

	public interface FastStreamBuilder<T> extends Consumer<T>, cn.hutool.core.builder.Builder<EasyStream<T>> {

		/**
		 * Adds an element to the stream being built.
		 *
		 * @param t the element to add
		 * @return {@code this} builder
		 * @throws IllegalStateException if the builder has already transitioned to
		 *                               the built state
		 * @implSpec The default implementation behaves as if:
		 * <pre>{@code
		 *     accept(t)
		 *     return this;
		 * }</pre>
		 */
		default FastStreamBuilder<T> add(final T t) {
			accept(t);
			return this;
		}
	}

}
