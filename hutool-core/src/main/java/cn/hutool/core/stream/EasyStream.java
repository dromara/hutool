package cn.hutool.core.stream;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.mutable.MutableObj;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * <p>单元素的扩展流实现。基于原生Stream进行了封装和增强。<br>
 * 作者经对比了vavr、eclipse-collection、unwrap-ex以及其他语言的api，结合日常使用习惯，进行封装和拓展
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
 * @author huangchengxing
 * @see java.util.stream.Stream
 * @since 6.0.0
 */
public class EasyStream<T> extends AbstractEnhancedWrappedStream<T, EasyStream<T>> {

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
	 * @return a unwrap builder
	 */
	public static <T> Builder<T> builder() {
		return new Builder<T>() {
			private static final long serialVersionUID = 1L;
			private final Stream.Builder<T> streamBuilder = Stream.builder();

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
	 * 返回与指定函数将元素作为参数执行的结果组成的流
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 指定的函数
	 * @param <R>    函数执行后返回的类型
	 * @return 返回叠加操作后的流
	 */
	@Override
	public <R> EasyStream<R> map(final Function<? super T, ? extends R> mapper) {
		Objects.requireNonNull(mapper);
		return new EasyStream<>(stream.map(mapper));
	}

	/**
	 * 根据一个原始的流，返回一个新包装类实例
	 *
	 * @param stream 流
	 * @return 实现类
	 */
	@Override
	public EasyStream<T> wrap(final Stream<T> stream) {
		return new EasyStream<>(stream);
	}

	/**
	 * <p>将集合转换为树，默认用 {@code parentId == null} 作为顶部，内置一个小递归
	 * 因为需要在当前传入数据里查找，所以这是一个结束操作 <br>
	 *
	 * @param idGetter       id的getter对应的lambda，可以写作 {@code Student::getId}
	 * @param pIdGetter      parentId的getter对应的lambda，可以写作 {@code Student::getParentId}
	 * @param childrenSetter children的setter对应的lambda，可以写作{ @code Student::setChildren}
	 * @param <R>            此处是id、parentId的泛型限制
	 * @return list 组装好的树 <br>
	 * eg:
	 * <pre>{@code
	 * List<Student> studentTree = EasyStream.of(students).
	 * 	toTree(Student::getId, Student::getParentId, Student::setChildren);
	 * }</pre>
	 */
	public <R extends Comparable<R>> List<T> toTree(
		final Function<T, R> idGetter,
		final Function<T, R> pIdGetter,
		final BiConsumer<T, List<T>> childrenSetter) {
		final Map<R, List<T>> pIdValuesMap = group(pIdGetter);
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
	 * @return list 组装好的树 <br>
	 * eg:
	 * <pre>{@code
	 * List<Student> studentTree = EasyStream.of(students).
	 * 	.toTree(Student::getId, Student::getParentId, Student::setChildren, Student::getMatchParent);
	 * }</pre>
	 */

	public <R extends Comparable<R>> List<T> toTree(
		final Function<T, R> idGetter,
		final Function<T, R> pIdGetter,
		final BiConsumer<T, List<T>> childrenSetter,
		final Predicate<T> parentPredicate) {
		Objects.requireNonNull(parentPredicate);
		final List<T> list = toList();
		final List<T> parents = EasyStream.of(list).filter(e ->
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
	private <R extends Comparable<R>> List<T> getChildrenFromMapByPidAndSet(
		final Function<T, R> idGetter,
		final BiConsumer<T, List<T>> childrenSetter,
		final Map<R, List<T>> pIdValuesMap,
		final List<T> parents) {
		Objects.requireNonNull(idGetter);
		Objects.requireNonNull(childrenSetter);
		Objects.requireNonNull(pIdValuesMap);
		final MutableObj<Consumer<List<T>>> recursiveRef = new MutableObj<>();
		final Consumer<List<T>> recursive = values -> EasyStream.of(values, isParallel()).forEach(value -> {
			List<T> children = pIdValuesMap.get(idGetter.apply(value));
			childrenSetter.accept(value, children);
			recursiveRef.get().accept(children);
		});
		recursiveRef.set(recursive);
		recursive.accept(parents);
		return parents;
	}

	/**
	 * 建造者
	 *
	 * @author VampireAchao
	 */
	public interface Builder<T> extends Consumer<T>, cn.hutool.core.builder.Builder<EasyStream<T>> {

		/**
		 * Adds an element to the unwrap being built.
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
		default Builder<T> add(final T t) {
			accept(t);
			return this;
		}
	}

}
