package cn.hutool.core.stream;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.mutable.MutableInt;
import cn.hutool.core.lang.mutable.MutableObj;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * <p>{@link Stream}的扩展实现，基于原生Stream进行了封装和增强。<br>
 * 作者经对比了vavr、eclipse-collection、stream-ex以及其他语言的api，结合日常使用习惯，进行封装和拓展
 * Stream为集合提供了一些易用api，它让开发人员能使用声明式编程的方式去编写代码。
 *
 * <p>中间操作和结束操作</p>
 * <p>针对流的操作分为分为<em>中间操作</em>和<em>结束操作</em>,
 * 流只有在<em>结束操作</em>时才会真正触发执行以往的<em>中间操作</em>。<br>
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
 * <p>串行流与并行流</p>
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
public class EasyStream<T> extends AbstractEnhancedStreamWrapper<T, EasyStream<T>> {

	/**
	 * 构造
	 *
	 * @param stream {@link Stream}
	 */
	EasyStream(final Stream<T> stream) {
		super(ObjUtil.isNull(stream) ? Stream.empty() : stream);
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
	 * 与给定元素组成的流合并，成为新的流
	 *
	 * @param obj 元素
	 * @return 流
	 */
	public EasyStream<T> push(final T obj) {
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
	 * 根据一个原始的流，返回一个新包装类实例
	 *
	 * @param stream 流
	 * @return 实现类
	 */
	@Override
	public EasyStream<T> wrapping(Stream<T> stream) {
		return new EasyStream<>(stream);
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
