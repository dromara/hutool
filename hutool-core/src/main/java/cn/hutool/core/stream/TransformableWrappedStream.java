package cn.hutool.core.stream;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.collection.iter.IterUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.mutable.MutableInt;
import cn.hutool.core.lang.mutable.MutableObj;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.map.SafeConcurrentHashMap;
import cn.hutool.core.util.ArrayUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * {@link WrappedStream}的扩展，用于为实现类提供更多中间操作方法的增强接口，
 * 该接口提供的方法，返回值类型都为{@link Stream}。
 *
 * @param <T> 流中的元素类型
 * @param <S> {@link TransformableWrappedStream}的实现类类型
 * @author huangchengxing VampireAchao
 * @since 6.0.0
 */
public interface TransformableWrappedStream<T, S extends TransformableWrappedStream<T, S>> extends WrappedStream<T, S> {

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
	default <U, R> EasyStream<R> zip(
			final Iterable<U> other,
			final BiFunction<? super T, ? super U, ? extends R> zipper) {
		Objects.requireNonNull(zipper);
		final Map<Integer, T> idxIdentityMap = mapIdx((e, idx) -> MapUtil.entry(idx, e)).collect(CollectorUtil.entryToMap());
		final Map<Integer, U> idxOtherMap = EasyStream.of(other).mapIdx((e, idx) -> MapUtil.entry(idx, e)).collect(CollectorUtil.entryToMap());
		if (idxIdentityMap.size() <= idxOtherMap.size()) {
			return EasyStream.of(idxIdentityMap.keySet(), isParallel()).map(k -> zipper.apply(idxIdentityMap.get(k), idxOtherMap.get(k)));
		}
		return EasyStream.of(idxOtherMap.keySet(), isParallel()).map(k -> zipper.apply(idxIdentityMap.get(k), idxOtherMap.get(k)));
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
	default EasyStream<EasyStream<T>> split(final int batchSize) {
		final List<T> list = this.collect(Collectors.toList());
		final int size = list.size();
		// 指定长度 大于等于 列表长度
		if (size <= batchSize) {
			// 返回第一层只有单个元素的双层流，形如：[[1,2,3,4,5]]
			return EasyStream.<EasyStream<T>>of(EasyStream.of(list, isParallel()));
		}
		return EasyStream.iterate(0, i -> i < size, i -> i + batchSize)
				.map(skip -> EasyStream.of(list.subList(skip, Math.min(size, skip + batchSize)), isParallel()))
				.parallel(isParallel())
				.onClose(unwrap()::close);
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
	default EasyStream<List<T>> splitList(final int batchSize) {
		return split(batchSize).map(EasyStream::toList);
	}

	/**
	 * 将当前流转为键值对流
	 *
	 * @param keyMapper   键的映射方法
	 * @param valueMapper 值的映射方法
	 * @param <K>         键类型
	 * @param <V>         值类型
	 * @return {@link EntryStream}实例
	 */
	default <K, V> EntryStream<K, V> toEntries(final Function<T, K> keyMapper, final Function<T, V> valueMapper) {
		Objects.requireNonNull(keyMapper);
		Objects.requireNonNull(valueMapper);
		return new EntryStream<>(map(t -> EntryStream.ofEntry(keyMapper.apply(t), valueMapper.apply(t))));
	}

	/**
	 * 将当前流转为键值对流
	 *
	 * @param keyMapper 键的映射方法
	 * @param <K>       键类型
	 * @return {@link EntryStream}实例
	 */
	default <K> EntryStream<K, T> toEntries(final Function<T, K> keyMapper) {
		return toEntries(keyMapper, Function.identity());
	}

	// region ============ generic ============

	/**
	 * 反转顺序
	 *
	 * @return 反转元素顺序
	 */
	@SuppressWarnings("unchecked")
	default S reverse() {
		final T[] array = (T[]) toArray();
		ArrayUtil.reverse(array);
		return wrap(Stream.of(array)).parallel(isParallel());
	}

	/**
	 * 更改流的并行状态
	 *
	 * @param parallel 是否并行
	 * @return 流
	 */
	default S parallel(final boolean parallel) {
		return parallel ? parallel() : sequential();
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
	@SuppressWarnings("unchecked")
	default S splice(final int start, final int deleteCount, final T... items) {
		final List<T> elements = unwrap().collect(Collectors.toList());
		return wrap(ListUtil.splice(elements, start, deleteCount, items).stream())
				.parallel(isParallel());
	}

	/**
	 * <p>遍历流中与断言匹配的元素，当遇到第一个不匹配的元素时终止，返回由匹配的元素组成的流。<br>
	 * eg:
	 * <pre>{@code
	 * EasyStream.of(1, 2, 3, 4, 5)
	 * 	.takeWhile(i -> Objects.equals(3, i)) // 获取元素，一直到遇到第一个3为止
	 * 	.toList(); // = [1, 2]
	 * }</pre>
	 *
	 * <p>与{@code JDK9}中的{@code takeWhile}方法不太一样，此操作为顺序且有状态的中间操作。
	 * 即使在并行流中，该操作仍然是顺序执行的，并且不影响后续的并行操作：
	 * <pre>{@code
	 * EasyStream.iterate(1, i -> i + 1)
	 * 	.parallel()
	 * 	.takeWhile(e -> e < 50) // 顺序执行
	 * 	.map(e -> e + 1) // 并发
	 * 	.map(String::valueOf) // 并发
	 * 	.toList();
	 * }</pre>
	 * 若非必要，不推荐在并行流中进行该操作。
	 *
	 * @param predicate 断言
	 * @return 与指定断言匹配的元素组成的流
	 */
	default S takeWhile(final Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		return wrap(StreamUtil.takeWhile(unwrap(), predicate));
	}

	/**
	 * 删除流中与断言匹配的元素，当遇到第一个不匹配的元素时终止，返回由剩余不匹配的元素组成的流。<br>
	 * eg:
	 * <pre>{@code
	 * EasyStream.of(1, 2, 3, 4, 5)
	 * 	.dropWhile(i -> !Objects.equals(3, i)) // 删除不为3的元素，一直到遇到第一个3为止
	 * 	.toList(); // = [3, 4, 5]
	 * }</pre>
	 *
	 * <p>与{@code JDK9}中的{@code dropWhile}方法不太一样，此操作为顺序且有状态的中间操作。
	 * 即使在并行流中，该操作仍然是顺序执行的，并且不影响后续的并行操作：
	 * <pre>{@code
	 * EasyStream.iterate(1, i -> i + 1)
	 * 	.parallel()
	 * 	.dropWhile(e -> e < 50) // 顺序执行
	 * 	.map(e -> e + 1) // 并发
	 * 	.map(String::valueOf) // 并发
	 * 	.toList();
	 * }</pre>
	 * 若非必要，不推荐在并行流中进行该操作。
	 *
	 * @param predicate 断言
	 * @return 剩余元素组成的流
	 */
	default S dropWhile(final Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		return wrap(StreamUtil.dropWhile(unwrap(), predicate));
	}

	/**
	 * 返回一个具有去重特征的流 非并行流(顺序流)下对于重复元素，保留遇到顺序中最先出现的元素，并行流情况下不能保证具体保留哪一个
	 * 这是一个有状态中间操作
	 *
	 * @param <F>          参数类型
	 * @param keyExtractor 去重依据
	 * @return 一个具有去重特征的流
	 */
	default <F> EasyStream<T> distinct(final Function<? super T, F> keyExtractor) {
		Objects.requireNonNull(keyExtractor);
		if (isParallel()) {
			final SafeConcurrentHashMap<F, Boolean> exists = new SafeConcurrentHashMap<>();
			// 标记是否出现过null值，用于保留第一个出现的null
			// 由于ConcurrentHashMap的key不能为null，所以用此变量来标记
			final AtomicBoolean hasNull = new AtomicBoolean(false);
			return EasyStream.of(unwrap().filter(e -> {
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
			return EasyStream.of(unwrap().filter(e -> exists.add(keyExtractor.apply(e))));
		}
	}

	// endregion

	// region ============ peek ============

	/**
	 * 返回与指定函数将元素作为参数执行后组成的流。操作带下标
	 *
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
	default S peekIdx(final BiConsumer<? super T, Integer> action) {
		Objects.requireNonNull(action);
		if (isParallel()) {
			final Map<Integer, T> idxMap = easyStream().toIdxMap();
			return transform(EasyStream.of(idxMap.entrySet())
					.parallel(isParallel())
					.peek(e -> action.accept(e.getValue(), e.getKey()))
					.map(Map.Entry::getValue));
		} else {
			final AtomicInteger index = new AtomicInteger(NOT_FOUND_ELEMENT_INDEX);
			return peek(e -> action.accept(e, index.incrementAndGet()));
		}
	}

	/**
	 * 返回叠加调用{@link Console#log(Object)}打印出结果的流
	 *
	 * @return 返回叠加操作后的FastStream
	 */
	default S log() {
		return peek(Console::log);
	}

	// endregion

	// region ============ concat ============

	/**
	 * 与给定元素组成的流合并，成为新的流
	 *
	 * @param obj 元素
	 * @return 流
	 */
	@SuppressWarnings("unchecked")
	default S push(final T... obj) {
		Stream<T> result = unwrap();
		if (ArrayUtil.isNotEmpty(obj)) {
			result = Stream.concat(unwrap(), Stream.of(obj));
		}
		return wrap(result);
	}

	/**
	 * 给定元素组成的流与当前流合并，成为新的流
	 *
	 * @param obj 元素
	 * @return 流
	 */
	@SuppressWarnings({"SpellCheckingInspection", "unchecked"})
	default S unshift(final T... obj) {
		Stream<T> result = unwrap();
		if (ArrayUtil.isNotEmpty(obj)) {
			result = Stream.concat(Stream.of(obj), unwrap());
		}
		return wrap(result);
	}

	/**
	 * 将输入元素转为流，返回一个前半段为当前流，后半段为新流的新实例
	 *
	 * @param iterable 集合
	 * @return {@link EntryStream}实例
	 */
	default S append(final Iterable<? extends T> iterable) {
		if (IterUtil.isEmpty(iterable)) {
			return wrap(this);
		}
		final Stream<? extends T> contacted = StreamSupport.stream(iterable.spliterator(), isParallel());
		return wrap(Stream.concat(this, contacted));
	}

	/**
	 * 将输入元素转为流，返回一个前半段为新流，后半段为当前流的新实例
	 *
	 * @param iterable 集合
	 * @return {@link EntryStream}实例
	 */
	default S prepend(final Iterable<? extends T> iterable) {
		if (IterUtil.isEmpty(iterable)) {
			return wrap(this);
		}
		final Stream<? extends T> contacted = StreamSupport.stream(iterable.spliterator(), isParallel());
		return wrap(Stream.concat(contacted, this));
	}

	// endregion

	// region ============ filter ============

	/**
	 * 过滤掉空元素
	 *
	 * @return 过滤后的流
	 */
	default S nonNull() {
		return filter(Objects::nonNull);
	}

	/**
	 * 过滤元素，返回与指定断言匹配的元素组成的流，断言带下标
	 *
	 * @param predicate 断言
	 * @return 返回叠加过滤操作后的流
	 */
	default S filterIdx(final BiPredicate<? super T, Integer> predicate) {
		Objects.requireNonNull(predicate);
		if (isParallel()) {
			final Map<Integer, T> idxMap = easyStream().toIdxMap();
			return transform(EasyStream.of(idxMap.entrySet())
					.parallel(isParallel())
					.filter(e -> predicate.test(e.getValue(), e.getKey()))
					.map(Map.Entry::getValue));
		} else {
			final MutableInt index = new MutableInt(NOT_FOUND_ELEMENT_INDEX);
			return filter(e -> predicate.test(e, index.incrementAndGet()));
		}
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
	default <R> S filter(final Function<? super T, ? extends R> mapper, final R value) {
		Objects.requireNonNull(mapper);
		return filter(e -> Objects.equals(mapper.apply(e), value));
	}

	// endregion

	// region  ============ flat ============

	/**
	 * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流<br>
	 * 这是一个无状态中间操作<br>
	 * 例如，将users里所有user的id和parentId组合在一起，形成一个新的流:
	 * <pre>{@code
	 *     EasyStream<Long> ids = EasyStream.of(users).flatMap(user -> FastStream.of(user.getId(), user.getParentId()));
	 * }</pre>
	 *
	 * @param mapper 操作，返回流
	 * @param <R>    拆分后流的元素类型
	 * @return 返回叠加拆分操作后的流
	 */
	@Override
	default <R> EasyStream<R> flatMap(final Function<? super T, ? extends Stream<? extends R>> mapper) {
		Objects.requireNonNull(mapper);
		return new EasyStream<>(unwrap().flatMap(mapper));
	}

	/**
	 * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流，操作带下标
	 *
	 * @param mapper 操作，返回流
	 * @param <R>    拆分后流的元素类型
	 * @return 返回叠加拆分操作后的流
	 */
	default <R> EasyStream<R> flatMapIdx(final BiFunction<? super T, Integer, ? extends Stream<? extends R>> mapper) {
		Objects.requireNonNull(mapper);
		if (isParallel()) {
			final Map<Integer, T> idxMap = easyStream().toIdxMap();
			return EasyStream.of(idxMap.entrySet())
					.parallel(isParallel())
					.flatMap(e -> mapper.apply(e.getValue(), e.getKey()));
		} else {
			final MutableInt index = new MutableInt(NOT_FOUND_ELEMENT_INDEX);
			return flatMap(e -> mapper.apply(e, index.incrementAndGet()));
		}
	}

	/**
	 * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作, 转换为迭代器元素,
	 * 最后返回所有迭代器的所有元素组成的流<br>
	 * 这是一个无状态中间操作<br>
	 * 例如，将users里所有user的id和parentId组合在一起，形成一个新的流:
	 * <pre>{@code
	 *     EasyStream<Long> ids = EasyStream.of(users).flat(user -> FastStream.of(user.getId(), user.getParentId()));
	 * }</pre>
	 *
	 * @param mapper 操作，返回可迭代对象
	 * @param <R>    拆分后流的元素类型
	 * @return 返回叠加拆分操作后的流
	 */
	default <R> EasyStream<R> flat(final Function<? super T, ? extends Iterable<? extends R>> mapper) {
		Objects.requireNonNull(mapper);
		return flatMap(w -> EasyStream.of(mapper.apply(w)));
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
	default <R> EasyStream<R> flatNonNull(final Function<? super T, ? extends Iterable<? extends R>> mapper) {
		return nonNull().flat(mapper).nonNull();
	}

	/**
	 * 将树递归扁平化为集合，内置一个小递归
	 * 这是一个无状态中间操作 <br>
	 * eg:
	 * <pre>{@code
	 * List<Student> students = EasyStream.of(studentTree)
	 * 	.flatTree(Student::getChildren, Student::setChildren)
	 * 	.toList();
	 * }</pre>
	 *
	 * @param childrenGetter 获取子节点的lambda，可以写作 {@code Student::getChildren}
	 * @param childrenSetter 设置子节点的lambda，可以写作 {@code Student::setChildren}
	 * @return EasyStream 一个流
	 */
	default S flatTree(final Function<T, List<T>> childrenGetter, final BiConsumer<T, List<T>> childrenSetter) {
		Objects.requireNonNull(childrenGetter);
		Objects.requireNonNull(childrenSetter);
		final MutableObj<Function<T, EasyStream<T>>> recursiveRef = new MutableObj<>();
		@SuppressWarnings("unchecked") final Function<T, EasyStream<T>> recursive = e -> EasyStream.of(childrenGetter.apply(e))
				.flat(recursiveRef.get())
				.unshift(e);
		recursiveRef.set(recursive);
		return wrap(flatMap(recursive).peek(e -> childrenSetter.accept(e, null)));
	}

	// endregion

	// region ============ map ============

	/**
	 * 返回与指定函数将元素作为参数执行的结果组成的流
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 指定的函数
	 * @param <R>    函数执行后返回的类型
	 * @return 返回叠加操作后的流
	 */
	@Override
	default <R> EasyStream<R> map(final Function<? super T, ? extends R> mapper) {
		Objects.requireNonNull(mapper);
		return new EasyStream<>(unwrap().map(mapper));
	}

	/**
	 * 返回 元素 转换后 并且不为 {@code null} 的 新元素组成的流<br>
	 * 这是一个无状态中间操作<br>
	 * <pre>{@code
	 * // 等价于先调用map再调用nonNull
	 * .nonNull().map(...).nonNull()...
	 * }</pre>
	 *
	 * @param mapper 指定的函数
	 * @param <R>    函数执行后返回的类型
	 * @return 新元素组成的流
	 */
	default <R> EasyStream<R> mapNonNull(final Function<? super T, ? extends R> mapper) {
		Objects.requireNonNull(mapper);
		return new EasyStream<>(nonNull().<R>map(mapper).nonNull());
	}

	/**
	 * 返回与指定函数将元素作为参数执行的结果组成的流，操作带下标
	 *
	 * @param mapper 指定的函数
	 * @param <R>    函数执行后返回的类型
	 * @return 返回叠加操作后的流
	 */
	default <R> EasyStream<R> mapIdx(final BiFunction<? super T, Integer, ? extends R> mapper) {
		Objects.requireNonNull(mapper);
		if (isParallel()) {
			final Map<Integer, T> idxMap = easyStream().toIdxMap();
			return EasyStream.of(idxMap.entrySet())
					.parallel(isParallel())
					.map(e -> mapper.apply(e.getValue(), e.getKey()));
		} else {
			final MutableInt index = new MutableInt(NOT_FOUND_ELEMENT_INDEX);
			return map(e -> mapper.apply(e, index.incrementAndGet()));
		}
	}

	/**
	 * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流，操作带一个方法，调用该方法可增加元素
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 操作，返回流
	 * @param <R>    拆分后流的元素类型
	 * @return 返回叠加拆分操作后的流
	 */
	default <R> EasyStream<R> mapMulti(final BiConsumer<? super T, ? super Consumer<R>> mapper) {
		Objects.requireNonNull(mapper);
		return flatMap(e -> {
			final EasyStream.Builder<R> buffer = EasyStream.builder();
			mapper.accept(e, buffer);
			return buffer.build();
		});
	}

	// endregion

}
