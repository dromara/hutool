package cn.hutool.core.stream;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * 可变的汇聚操作{@link Collector} 相关工具封装
 *
 * @author looly
 * @since 5.6.7
 */
public class CollectorUtil {

	/**
	 * 提供任意对象的Join操作的{@link Collector}实现，对象默认调用toString方法
	 *
	 * @param delimiter 分隔符
	 * @param <T>       对象类型
	 * @return {@link Collector}
	 */
	public static <T> Collector<T, ?, String> joining(CharSequence delimiter) {
		return joining(delimiter, Object::toString);
	}

	/**
	 * 提供任意对象的Join操作的{@link Collector}实现
	 *
	 * @param delimiter    分隔符
	 * @param toStringFunc 自定义指定对象转换为字符串的方法
	 * @param <T>          对象类型
	 * @return {@link Collector}
	 */
	public static <T> Collector<T, ?, String> joining(CharSequence delimiter,
													  Function<T, ? extends CharSequence> toStringFunc) {
		return joining(delimiter, StrUtil.EMPTY, StrUtil.EMPTY, toStringFunc);
	}

	/**
	 * 提供任意对象的Join操作的{@link Collector}实现
	 *
	 * @param delimiter    分隔符
	 * @param prefix       前缀
	 * @param suffix       后缀
	 * @param toStringFunc 自定义指定对象转换为字符串的方法
	 * @param <T>          对象类型
	 * @return {@link Collector}
	 */
	public static <T> Collector<T, ?, String> joining(CharSequence delimiter,
													  CharSequence prefix,
													  CharSequence suffix,
													  Function<T, ? extends CharSequence> toStringFunc) {
		return new SimpleCollector<>(
				() -> new StringJoiner(delimiter, prefix, suffix),
				(joiner, ele) -> joiner.add(toStringFunc.apply(ele)),
				StringJoiner::merge,
				StringJoiner::toString,
				Collections.emptySet()
		);
	}


	/**
	 * 提供对null值友好的groupingBy操作的{@link Collector}实现，可指定map类型
	 *
	 * @param classifier 分组依据
	 * @param mapFactory 提供的map
	 * @param downstream 下游操作
	 * @param <T>        实体类型
	 * @param <K>        实体中的分组依据对应类型，也是Map中key的类型
	 * @param <D>        下游操作对应返回类型，也是Map中value的类型
	 * @param <A>        下游操作在进行中间操作时对应类型
	 * @param <M>        最后返回结果Map类型
	 * @return {@link Collector}
	 */
	public static <T, K, D, A, M extends Map<K, D>> Collector<T, ?, M> groupingBy(Function<? super T, ? extends K> classifier,
																				  Supplier<M> mapFactory,
																				  Collector<? super T, A, D> downstream) {
		Supplier<A> downstreamSupplier = downstream.supplier();
		BiConsumer<A, ? super T> downstreamAccumulator = downstream.accumulator();
		BiConsumer<Map<K, A>, T> accumulator = (m, t) -> {
			K key = Opt.ofNullable(t).map(classifier).orElse(null);
			A container = m.computeIfAbsent(key, k -> downstreamSupplier.get());
			downstreamAccumulator.accept(container, t);
		};
		BinaryOperator<Map<K, A>> merger = (m1, m2) -> {
			for (Map.Entry<K, A> e : m2.entrySet()) {
				m1.merge(e.getKey(), e.getValue(), downstream.combiner());
			}
			return m1;
		};
		@SuppressWarnings("unchecked")
		Supplier<Map<K, A>> mangledFactory = (Supplier<Map<K, A>>) mapFactory;

		if (downstream.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)) {
			return new SimpleCollector<>(mangledFactory, accumulator, merger, Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH)));
		} else {
			@SuppressWarnings("unchecked")
			Function<A, A> downstreamFinisher = (Function<A, A>) downstream.finisher();
			Function<Map<K, A>, M> finisher = intermediate -> {
				intermediate.replaceAll((k, v) -> downstreamFinisher.apply(v));
				@SuppressWarnings("unchecked")
				M castResult = (M) intermediate;
				return castResult;
			};
			return new SimpleCollector<>(mangledFactory, accumulator, merger, finisher, Collections.emptySet());
		}
	}

	/**
	 * 提供对null值友好的groupingBy操作的{@link Collector}实现
	 *
	 * @param classifier 分组依据
	 * @param downstream 下游操作
	 * @param <T>        实体类型
	 * @param <K>        实体中的分组依据对应类型，也是Map中key的类型
	 * @param <D>        下游操作对应返回类型，也是Map中value的类型
	 * @param <A>        下游操作在进行中间操作时对应类型
	 * @return {@link Collector}
	 */
	public static <T, K, A, D>
	Collector<T, ?, Map<K, D>> groupingBy(Function<? super T, ? extends K> classifier,
										  Collector<? super T, A, D> downstream) {
		return groupingBy(classifier, HashMap::new, downstream);
	}

}
