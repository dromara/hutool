package cn.hutool.core.stream;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * 简单{@link Collector}接口实现
 *
 * @param <T> 输入数据类型
 * @param <A> 累积结果的容器类型
 * @param <R> 数据结果类型
 * @since 5.6.7
 */
public class SimpleCollector<T, A, R> implements Collector<T, A, R> {
	/**
	 * 创建新的结果容器，容器类型为A
	 */
	private final Supplier<A> supplier;
	/**
	 * 将输入元素合并到结果容器中
	 */
	private final BiConsumer<A, T> accumulator;
	/**
	 * 合并两个结果容器（并行流使用，将多个线程产生的结果容器合并）
	 */
	private final BinaryOperator<A> combiner;
	/**
	 * 将结果容器转换成最终的表示
	 */
	private final Function<A, R> finisher;
	/**
	 * 特征值枚举，见{@link Characteristics}
	 * <ul>
	 *     <li>CONCURRENT：     表示结果容器只有一个（即使是在并行流的情况下）。
	 *     只有在并行流且收集器不具备此特性的情况下，combiner()返回的lambda表达式才会执行（中间结果容器只有一个就无需合并）。
	 *     设置此特性时意味着多个线程可以对同一个结果容器调用，因此结果容器必须是线程安全的。</li>
	 *     <li>UNORDERED：      表示流中的元素无序</li>
	 *     <li>IDENTITY_FINISH：表示中间结果容器类型与最终结果类型一致。设置此特性时finiser()方法不会被调用</li>
	 * </ul>
	 */
	private final Set<Characteristics> characteristics;

	/**
	 * 构造
	 *
	 * @param supplier        创建新的结果容器函数
	 * @param accumulator     将输入元素合并到结果容器中函数
	 * @param combiner        合并两个结果容器函数（并行流使用，将多个线程产生的结果容器合并）
	 * @param finisher        将结果容器转换成最终的表示函数
	 * @param characteristics 特征值枚举
	 */
	public SimpleCollector(Supplier<A> supplier,
						   BiConsumer<A, T> accumulator,
						   BinaryOperator<A> combiner,
						   Function<A, R> finisher,
						   Set<Characteristics> characteristics) {
		this.supplier = supplier;
		this.accumulator = accumulator;
		this.combiner = combiner;
		this.finisher = finisher;
		this.characteristics = characteristics;
	}

	/**
	 * 构造
	 *
	 * @param supplier        创建新的结果容器函数
	 * @param accumulator     将输入元素合并到结果容器中函数
	 * @param combiner        合并两个结果容器函数（并行流使用，将多个线程产生的结果容器合并）
	 * @param characteristics 特征值枚举
	 */
	@SuppressWarnings("unchecked")
	public SimpleCollector(Supplier<A> supplier,
						   BiConsumer<A, T> accumulator,
						   BinaryOperator<A> combiner,
						   Set<Characteristics> characteristics) {
		this(supplier, accumulator, combiner, i -> (R) i, characteristics);
	}

	@Override
	public BiConsumer<A, T> accumulator() {
		return accumulator;
	}

	@Override
	public Supplier<A> supplier() {
		return supplier;
	}

	@Override
	public BinaryOperator<A> combiner() {
		return combiner;
	}

	@Override
	public Function<A, R> finisher() {
		return finisher;
	}

	@Override
	public Set<Characteristics> characteristics() {
		return characteristics;
	}

}
