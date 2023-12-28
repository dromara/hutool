/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.stream;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.lang.Opt;
import org.dromara.hutool.core.math.NumberUtil;
import org.dromara.hutool.core.util.ObjUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * <p>单元素的扩展流实现。基于原生Stream进行了封装和增强。<br>
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
 * @param <T> 对象类型
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

	// ----- region Static method
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
	public static <T> EasyStream<T> iterate(final T seed, final Predicate<? super T> hasNext,
											final UnaryOperator<T> next) {
		Objects.requireNonNull(next);
		Objects.requireNonNull(hasNext);
		return new EasyStream<>(StreamUtil.iterate(seed, hasNext, next));
	}

	/**
	 * <p>指定一个层级结构的根节点（通常是树或图），
	 * 然后获取包含根节点在内，根节点所有层级结构中的节点组成的流。<br>
	 * 该方法用于以平铺的方式对图或树节点进行访问，可以使用并行流提高效率。
	 *
	 * <p>eg:
	 * <pre>{@code
	 * Tree root = // 构建树结构
	 * // 搜索树结构中所有级别为3的节点，并按权重排序
	 * List<Tree> thirdLevelNodes = StreamUtil.iterateHierarchies(root, Tree:getChildren)
	 * 	.filter(node -> node.getLevel() == 3)
	 * 	.sorted(Comparator.comparing(Tree::getWeight))
	 * 	.toList();
	 * }</pre>
	 *
	 * @param root       根节点
	 * @param discoverer 下一层级节点的获取方法
	 * @param filter     节点过滤器，不匹配的节点与以其作为根节点的子树将将会被忽略
	 * @param <T>        对象类型
	 * @return 包含根节点在内，根节点所有层级结构中的节点组成的流
	 */
	public static <T> EasyStream<T> iterateHierarchies(
		final T root, final Function<T, Collection<T>> discoverer, final Predicate<T> filter) {
		return of(StreamUtil.iterateHierarchies(root, discoverer, filter));
	}

	/**
	 * <p>指定一个层级结构的根节点（通常是树或图），
	 * 然后获取包含根节点在内，根节点所有层级结构中的节点组成的流。<br>
	 * 该方法用于以平铺的方式对图或树节点进行访问，可以使用并行流提高效率。
	 *
	 * <p>eg:
	 * <pre>{@code
	 * Tree root = // 构建树结构
	 * // 搜索树结构中所有级别为3的节点，并按权重排序
	 * List<Tree> thirdLevelNodes = StreamUtil.iterateHierarchies(root, Tree:getChildren)
	 * 	.filter(node -> node.getLevel() == 3)
	 * 	.sorted(Comparator.comparing(Tree::getWeight))
	 * 	.toList();
	 * }</pre>
	 *
	 * @param root       根节点
	 * @param discoverer 下一层级节点的获取方法
	 * @param <T>        对象类型
	 * @return 包含根节点在内，根节点所有层级结构中的节点组成的流
	 */
	public static <T> EasyStream<T> iterateHierarchies(
		final T root, final Function<T, Collection<T>> discoverer) {
		return of(StreamUtil.iterateHierarchies(root, discoverer));
	}

	/**
	 * 返回无限串行无序流
	 * 其中每一个元素都由给定的{@link Supplier}生成
	 * 适用场景在一些生成常量流、随机元素等
	 *
	 * @param <T> 元素类型
	 * @param s   用来生成元素的 {@link  Supplier}
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
		return Opt.ofBlankAble(str).map(CharSequence::toString).map(s -> s.split(regex)).map(EasyStream::of)
			.orElseGet(EasyStream::empty);
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
	 * 计算int类型的总和
	 *
	 * @param mapper 将对象转换为int的 {@link Function}
	 * @return int 总和
	 */
	public int sum(final ToIntFunction<? super T> mapper) {
		return stream.mapToInt(mapper).sum();
	}

	/**
	 * 计算long类型的总和
	 *
	 * @param mapper 将对象转换为long的 {@link Function}
	 * @return long 总和
	 */
	public long sum(final ToLongFunction<? super T> mapper) {
		return stream.mapToLong(mapper).sum();
	}


	/**
	 * 计算double类型的总和
	 *
	 * @param mapper 将对象转换为double的 {@link Function}
	 * @return double 总和
	 */
	public double sum(final ToDoubleFunction<? super T> mapper) {
		return stream.mapToDouble(mapper).sum();
	}


	/**
	 * 计算 {@link Number} 类型的总和
	 *
	 * @param <R>    数字
	 * @param mapper 将对象转换为{@link Number} 的 {@link Function}
	 * @return {@link BigDecimal} , 如果流为空, 返回 {@link BigDecimal#ZERO}
	 */
	public <R extends Number> BigDecimal sum(final Function<? super T, R> mapper) {
		return stream.map(mapper).reduce(BigDecimal.ZERO, NumberUtil::add, NumberUtil::add);
	}


	/**
	 * 计算 {@link BigDecimal} 类型的平均值 并以四舍五入的方式保留2位精度
	 *
	 * @param mapper 将对象转换为{@link BigDecimal}的 {@link Function}
	 * @return 计算后的平均值 如果流的长度为0, 返回 {@link Opt#empty()}
	 */
	public Opt<BigDecimal> avg(final Function<? super T, BigDecimal> mapper) {
		return avg(mapper, 2);
	}


	/**
	 * {@link BigDecimal} 类型的平均值 并以四舍五入的方式保留小数点后scale位
	 *
	 * @param mapper 将对象转换为{@link BigDecimal} 的 {@link Function}
	 * @param scale  保留精度
	 * @return 计算后的平均值  如果流的长度为0, 返回 {@link Opt#empty()}
	 */
	public Opt<BigDecimal> avg(final Function<? super T, BigDecimal> mapper, final int scale) {
		return avg(mapper, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 计算 {@link BigDecimal} 类型的平均值
	 *
	 * @param mapper       将对象转换为{@link BigDecimal} 的 {@link Function}
	 * @param scale        保留精度
	 * @param roundingMode 舍入模式
	 * @return 计算后的平均值 如果元素的长度为0 那么会返回 {@link Opt#empty()}
	 */
	public Opt<BigDecimal> avg(final Function<? super T, BigDecimal> mapper, final int scale,
							   final RoundingMode roundingMode) {
		//元素列表
		final List<BigDecimal> bigDecimalList = stream.map(mapper).collect(Collectors.toList());
		if (CollUtil.isEmpty(bigDecimalList)) {
			return Opt.empty();
		}
		return Opt.ofNullable(EasyStream.of(bigDecimalList).reduce(BigDecimal.ZERO, BigDecimal::add)
			.divide(NumberUtil.toBigDecimal(bigDecimalList.size()), scale, roundingMode));
	}


	/**
	 * 计算int类型的平均值
	 *
	 * @param mapper 将对象转换为int 的 {@link Function}
	 * @return {@link OptionalDouble} 如果流的长度为0 那么会返回{@link OptionalDouble#empty()}
	 */
	public OptionalDouble avg(final ToIntFunction<? super T> mapper) {
		return stream.mapToInt(mapper).average();
	}

	/**
	 * 计算double类型的平均值
	 *
	 * @param mapper 将对象转换为double 的 {@link Function}
	 * @return {@link OptionalDouble} 如果流的长度为0 那么会返回{@link OptionalDouble#empty()}
	 */
	public OptionalDouble avg(final ToDoubleFunction<? super T> mapper) {
		return stream.mapToDouble(mapper).average();
	}


	/**
	 * 计算double平均值
	 *
	 * @param mapper 将对象转换为long 的 {@link Function}
	 * @return {@link OptionalDouble}  如果流的长度为0 那么会返回{@link OptionalDouble#empty()}
	 */
	public OptionalDouble avg(final ToLongFunction<? super T> mapper) {
		return stream.mapToLong(mapper).average();
	}


	/**
	 * 建造者
	 *
	 * @author VampireAchao
	 */
	public interface Builder<T> extends Consumer<T>, org.dromara.hutool.core.lang.builder.Builder<EasyStream<T>> {

		/**
		 * Adds an element to the unwrap being built.
		 *
		 * @param t the element to add
		 * @return {@code this} builder
		 * @throws IllegalStateException if the builder has already transitioned to
		 *                               the built state
		 * @implSpec The default implementation behaves as if:
		 * <pre>{@code
		 * 										    accept(t)
		 * 										    return this;
		 *                                    }</pre>
		 */
		default Builder<T> add(final T t) {
			accept(t);
			return this;
		}
	}

}
