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

import org.dromara.hutool.collection.ConcurrentHashSet;
import org.dromara.hutool.collection.iter.IterUtil;
import org.dromara.hutool.map.multi.RowKeyTable;
import org.dromara.hutool.map.multi.Table;
import org.dromara.hutool.util.ObjUtil;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * <p>参考StreamEx的EntryStream与vavr的Map，是针对键值对对象{@link Map.Entry}特化的单元素增强流实现。<br>
 * 本身可视为一个元素类型为{@link Map.Entry}的{@link Stream}，
 * 用于支持流式处理{@link Map}集合中的、或其他键值对类型的数据。
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author huangchengxing
 * @since 6.0.0
 */
public class EntryStream<K, V> extends AbstractEnhancedWrappedStream<Map.Entry<K, V>, EntryStream<K, V>> {

	/**
	 * 默认的空键值对
	 */
	private static final Map.Entry<?, ?> EMPTY_ENTRY = new AbstractMap.SimpleImmutableEntry<>(null, null);

	/**
	 * 根据键与值的集合创建键值对流，若两集合在相同下标的位置找不到对应的键或值，则使用{@code null}填充。<br>
	 * 比如: {@code [1, 2, 3]}与{@code [1, 2]}合并，则得到{@code [{1=1}, {2=2}, {3=null}]}。
	 *
	 * @param <K>    键类型
	 * @param <V>    值类型
	 * @param keys   键集合
	 * @param values 值集合
	 * @return {@code EntryStream}实例
	 */
	public static <K, V> EntryStream<K, V> merge(final Iterable<K> keys, final Iterable<V> values) {
		final boolean hasKeys = ObjUtil.isNotNull(keys);
		final boolean hasValues = ObjUtil.isNotNull(values);
		// 皆为空
		if (!hasKeys && !hasValues) {
			return empty();
		}
		// 值为空
		if (hasKeys && !hasValues) {
			return of(keys, Function.identity(), k -> null);
		}
		// 键为空
		if (!hasKeys) {
			return of(values, v -> null, Function.identity());
		}
		// 皆不为空
		final List<Map.Entry<K, V>> entries = new ArrayList<>();
		final Iterator<K> keyItr = keys.iterator();
		final Iterator<V> valueItr = values.iterator();
		while (keyItr.hasNext() || valueItr.hasNext()) {
			entries.add(ofEntry(
					keyItr.hasNext() ? keyItr.next() : null,
					valueItr.hasNext() ? valueItr.next() : null
			));
		}
		return of(entries);
	}

	/**
	 * 根据一个{@link Map}集合中的键值对创建一个串行流，
	 * 对流的操作不会影响到入参的{@code map}实例本身
	 *
	 * @param map 集合
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @return {@code EntryStream}实例
	 */
	public static <K, V> EntryStream<K, V> of(final Map<K, V> map) {
		return ObjUtil.isNull(map) ?
				empty() : of(map.entrySet());
	}

	/**
	 * 根据一个{@link Map.Entry}类型的{@link Iterable}创建一个串行流，
	 * 对流的操作不会影响到入参的{@code entries}实例本身。<br>
	 * 若输入流中存在元素为{@code null}，则会映射为一个键值皆为{@code null}的键值对。
	 *
	 * @param entries {@link Iterable}实例
	 * @param <K>     键类型
	 * @param <V>     值类型
	 * @return {@code EntryStream}实例
	 */
	public static <K, V> EntryStream<K, V> of(final Iterable<? extends Map.Entry<K, V>> entries) {
		return ObjUtil.isNull(entries) ?
				empty() : of(StreamSupport.stream(entries.spliterator(), false));
	}

	/**
	 * 根据一个{@link Collection}集合中创建一个串行流
	 *
	 * @param source      原始集合
	 * @param keyMapper   键的映射方法
	 * @param valueMapper 值的映射方法
	 * @param <T>         元素类型
	 * @param <K>         键类型
	 * @param <V>         值类型
	 * @return {@code EntryStream}实例
	 */
	public static <T, K, V> EntryStream<K, V> of(
			final Iterable<T> source, final Function<? super T, ? extends K> keyMapper, final Function<? super T, ? extends V> valueMapper) {
		Objects.requireNonNull(keyMapper);
		Objects.requireNonNull(valueMapper);
		if (ObjUtil.isNull(source)) {
			return empty();
		}
		final Stream<Map.Entry<K, V>> stream = StreamSupport.stream(source.spliterator(), false)
				.map(t -> ofEntry(keyMapper.apply(t), valueMapper.apply(t)));
		return new EntryStream<>(stream);
	}

	/**
	 * 包装一个已有的流，若入参为空则返回一个空的串行流。<br>
	 * 若输入流中存在元素为{@code null}，则会映射为一个键值皆为{@code null}的键值对。
	 *
	 * @param stream 流
	 * @param <K>    键类型
	 * @param <V>    值类型
	 * @return {@code EntryStream}实例
	 */
	public static <K, V> EntryStream<K, V> of(final Stream<? extends Map.Entry<K, V>> stream) {
		return ObjUtil.isNull(stream) ?
				empty() : new EntryStream<>(stream.map(EntryStream::ofEntry));
	}

	/**
	 * 创建一个空的串行流
	 *
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @return {@code EntryStream}实例
	 */
	public static <K, V> EntryStream<K, V> empty() {
		return new EntryStream<>(Stream.empty());
	}

	/**
	 * 构造
	 */
	EntryStream(final Stream<Map.Entry<K, V>> stream) {
		super(stream);
	}

	// ================================ 中间操作 ================================

	/**
	 * 根据键去重，默认丢弃靠后的
	 *
	 * @return {@code EntryStream}实例
	 */
	public EntryStream<K, V> distinctByKey() {
		// FIXME fix happen NPE when has null key
		final Set<K> accessed = new ConcurrentHashSet<>(16);
		return wrap(stream.filter(e -> {
			final K key = e.getKey();
			if (accessed.contains(key)) {
				return false;
			}
			accessed.add(key);
			return true;
		}));
	}

	/**
	 * 根据值去重，默认丢弃靠后的
	 *
	 * @return {@code EntryStream}实例
	 */
	public EntryStream<K, V> distinctByValue() {
		// FIXME fix happen NPE when has null value
		final Set<V> accessed = new ConcurrentHashSet<>(16);
		return wrap(stream.filter(e -> {
			final V val = e.getValue();
			if (accessed.contains(val)) {
				return false;
			}
			accessed.add(val);
			return true;
		}));
	}

	/**
	 * 根据键和值过滤键值对
	 *
	 * @param filter 判断条件
	 * @return {@code EntryStream}实例
	 */
	public EntryStream<K, V> filter(final BiPredicate<? super K, ? super V> filter) {
		Objects.requireNonNull(filter);
		return super.filter(e -> filter.test(e.getKey(), e.getValue()));
	}

	/**
	 * 根据键过滤键值对
	 *
	 * @param filter 判断条件
	 * @return {@code EntryStream}实例
	 */
	public EntryStream<K, V> filterByKey(final Predicate<? super K> filter) {
		Objects.requireNonNull(filter);
		return super.filter(e -> filter.test(e.getKey()));
	}

	/**
	 * 根据值过滤键值对
	 *
	 * @param filter 判断条件
	 * @return {@code EntryStream}实例
	 */
	public EntryStream<K, V> filterByValue(final Predicate<? super V> filter) {
		Objects.requireNonNull(filter);
		return super.filter(e -> filter.test(e.getValue()));
	}

	/**
	 * 过滤流中键值对本身、键值对中的值或键为{@code null}的元素
	 *
	 * @return {@code EntryStream}实例
	 */
	public EntryStream<K, V> nonNullKeyValue() {
		return super.filter(e -> ObjUtil.isNotNull(e) && ObjUtil.isNotNull(e.getKey()) && ObjUtil.isNotNull(e.getValue()));
	}

	/**
	 * 过滤流中键值对本身，或键值对的键为{@code null}的元素
	 *
	 * @return {@code EntryStream}实例
	 */
	public EntryStream<K, V> nonNullKey() {
		return super.filter(e -> ObjUtil.isNotNull(e) && ObjUtil.isNotNull(e.getKey()));
	}

	/**
	 * 过滤流中键值对本身，或键值对的值为{@code null}的元素
	 *
	 * @return {@code EntryStream}实例
	 */
	public EntryStream<K, V> nonNullValue() {
		return super.filter(e -> ObjUtil.isNotNull(e) && ObjUtil.isNotNull(e.getValue()));
	}

	/**
	 * 检查键
	 *
	 * @param consumer 操作
	 * @return {@code EntryStream}实例
	 */
	public EntryStream<K, V> peekKey(final Consumer<? super K> consumer) {
		Objects.requireNonNull(consumer);
		return super.peek(e -> consumer.accept(e.getKey()));
	}

	/**
	 * 检查值
	 *
	 * @param consumer 操作
	 * @return {@code EntryStream}实例
	 */
	public EntryStream<K, V> peekValue(final Consumer<? super V> consumer) {
		Objects.requireNonNull(consumer);
		return super.peek(e -> consumer.accept(e.getValue()));
	}

	/**
	 * 根据键排序
	 *
	 * @param comparator 排序器
	 * @return {@code EntryStream}实例
	 */
	public EntryStream<K, V> sortByKey(final Comparator<? super K> comparator) {
		Objects.requireNonNull(comparator);
		return sorted(Map.Entry.comparingByKey(comparator));
	}

	/**
	 * 根据值排序
	 *
	 * @param comparator 排序器
	 * @return {@code EntryStream}实例
	 */
	public EntryStream<K, V> sortByValue(final Comparator<? super V> comparator) {
		Objects.requireNonNull(comparator);
		return sorted(Map.Entry.comparingByValue(comparator));
	}

	// ================================ 转换操作 ================================

	/**
	 * 向当前流末尾追加元素
	 *
	 * @param key   键
	 * @param value 值
	 * @return {@code EntryStream}实例
	 */
	public EntryStream<K, V> push(final K key, final V value) {
		return wrap(Stream.concat(stream, Stream.of(ofEntry(key, value))));
	}

	/**
	 * 项当前流队首追加元素
	 *
	 * @param key   键
	 * @param value 值
	 * @return {@code EntryStream}实例
	 */
	public EntryStream<K, V> unshift(final K key, final V value) {
		return wrap(Stream.concat(Stream.of(ofEntry(key, value)), stream));
	}

	/**
	 * 将输入元素转为流，返回一个前半段为当前流，后半段为新流的新{@link EasyStream}实例
	 *
	 * @param entries 键值对
	 * @return {@code EntryStream}实例
	 */
	@Override
	public EntryStream<K, V> append(final Iterable<? extends Map.Entry<K, V>> entries) {
		if (IterUtil.isEmpty(entries)) {
			return this;
		}
		final Stream<Map.Entry<K, V>> contacted = StreamSupport.stream(entries.spliterator(), isParallel())
				.map(EntryStream::ofEntry);
		return wrap(Stream.concat(stream, contacted));
	}

	/**
	 * 将输入元素转为流，返回一个前半段为新流，后半段为当前流的新{@link EasyStream}实例
	 *
	 * @param entries 键值对
	 * @return {@code EntryStream}实例
	 */
	@Override
	public EntryStream<K, V> prepend(final Iterable<? extends Map.Entry<K, V>> entries) {
		if (IterUtil.isEmpty(entries)) {
			return this;
		}
		final Stream<Map.Entry<K, V>> contacted = StreamSupport.stream(entries.spliterator(), isParallel())
				.map(EntryStream::ofEntry);
		return wrap(Stream.concat(contacted, stream));
	}

	/**
	 * 转为值的流
	 *
	 * @return 值的流
	 */
	public EasyStream<V> toValueStream() {
		return EasyStream.of(stream.map(Map.Entry::getValue));
	}

	/**
	 * 转为键的流
	 *
	 * @return 值的流
	 */
	public EasyStream<K> toKeyStream() {
		return EasyStream.of(stream.map(Map.Entry::getKey));
	}

	/**
	 * 将键映射为另一类型
	 *
	 * @param mapper 映射方法
	 * @param <N>    新的键类型
	 * @return {@code EntryStream}实例
	 */
	public <N> EntryStream<N, V> mapKeys(final Function<? super K, ? extends N> mapper) {
		Objects.requireNonNull(mapper);
		return new EntryStream<>(
				stream.map(e -> ofEntry(mapper.apply(e.getKey()), e.getValue()))
		);
	}

	/**
	 * 将值映射为另一类型
	 *
	 * @param mapper 映射方法
	 * @param <N>    新的值类型
	 * @return {@code EntryStream}实例
	 */
	public <N> EntryStream<K, N> mapValues(final Function<? super V, ? extends N> mapper) {
		Objects.requireNonNull(mapper);
		return new EntryStream<>(
				stream.map(e -> ofEntry(e.getKey(), mapper.apply(e.getValue())))
		);
	}

	/**
	 * 返回与指定函数将元素作为参数执行的结果组成的流
	 * 这是一个无状态中间操作
	 *
	 * @param mapper 指定的函数
	 * @param <R>    函数执行后返回流中元素的类型
	 * @return 返回叠加操作后的流
	 */
	@Override
	public <R> EasyStream<R> map(final Function<? super Map.Entry<K, V>, ? extends R> mapper) {
		Objects.requireNonNull(mapper);
		return EasyStream.of(stream.map(mapper));
	}

	/**
	 * 将实例转为根据键值对生成的单对象{@link Stream}实例
	 *
	 * @param mapper 映射方法
	 * @param <N>    函数执行后返回流中元素的类型
	 * @return 映射后的单对象组成的流
	 */
	public <N> EasyStream<N> map(final BiFunction<? super K, ? super V, ? extends N> mapper) {
		Objects.requireNonNull(mapper);
		return EasyStream.of(stream.map(e -> mapper.apply(e.getKey(), e.getValue())));
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
	public <R> EasyStream<R> flatMap(final Function<? super Map.Entry<K, V>, ? extends Stream<? extends R>> mapper) {
		Objects.requireNonNull(mapper);
		return EasyStream.of(stream.flatMap(mapper));
	}

	/**
	 * <p>将原有流的键执行mapper操作映射为流，流中的所有所有元素仍然对应原本的值，
	 * 然后再返回由这些流中所有元素组成的流新{@code EntryStream}串行流。<br>
	 * 效果类似：
	 * <pre>{@code
	 * // unwrap = [{a = 1}, {b = 2}, {c = 3}]
	 * unwrap.flatMapKey(key -> Stream.of(key + "1", key + "2"));
	 * // unwrap = [{a1 = 1}, {a2 = 1}, {b1 = 2}, {b2 = 2}, {c1 = 3}, {c2 = 3}]
	 * }</pre>
	 *
	 * @param keyMapper 值转映射方法
	 * @param <N>       新的键类型
	 * @return 返回叠加拆分操作后的流
	 */
	public <N> EntryStream<N, V> flatMapKey(final Function<? super K, Stream<? extends N>> keyMapper) {
		Objects.requireNonNull(keyMapper);
		return new EntryStream<>(
				stream.flatMap(e -> keyMapper
						.apply(e.getKey())
						.map(newKey -> ofEntry(newKey, e.getValue()))
				)
		);
	}

	/**
	 * <p>将原有流的值执行mapper操作映射为流，流中的所有所有元素仍然对应原本的键，
	 * 然后再返回由这些流中所有元素组成的流新{@code EntryStream}串行流。<br>
	 * 效果类似：
	 * <pre>{@code
	 * // unwrap = [{a = 1}, {b = 2}, {c = 3}]
	 * unwrap.flatMapValue(num -> Stream.of(num, num+1));
	 * // unwrap = [{a = 1}, {a = 2}, {b = 2}, {b = 3}, {c = 3}, {c = 4}]
	 * }</pre>
	 *
	 * @param valueMapper 值转映射方法
	 * @param <N>         新的值类型
	 * @return 返回叠加拆分操作后的流
	 */
	public <N> EntryStream<K, N> flatMapValue(final Function<? super V, Stream<? extends N>> valueMapper) {
		Objects.requireNonNull(valueMapper);
		return new EntryStream<>(
				stream.flatMap(e -> valueMapper
						.apply(e.getValue())
						.map(newVal -> ofEntry(e.getKey(), newVal))
				)
		);
	}

	// ================================ 结束操作 ================================

	/**
	 * 转为{@link Map}集合
	 *
	 * @param mapFactory 获取集合的工厂方法
	 * @param operator   当存在重复键时的处理
	 * @return 集合
	 * @see Collectors#toMap(Function, Function, BinaryOperator, Supplier)
	 */
	public Map<K, V> toMap(final Supplier<Map<K, V>> mapFactory, final BinaryOperator<V> operator) {
		Objects.requireNonNull(mapFactory);
		Objects.requireNonNull(operator);
		return super.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, operator, mapFactory));
	}

	/**
	 * 转为{@link Map}集合
	 *
	 * @param mapFactory 获取集合的工厂方法
	 * @return 集合
	 * @see Collectors#toMap(Function, Function, BinaryOperator)
	 */
	public Map<K, V> toMap(final Supplier<Map<K, V>> mapFactory) {
		Objects.requireNonNull(mapFactory);
		return super.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (t1, t2) -> t2, mapFactory));
	}

	/**
	 * 转为{@link HashMap}集合
	 *
	 * @return 集合
	 * @throws IllegalArgumentException 当键重复时抛出
	 * @see Collectors#toMap(Function, Function)
	 */
	public Map<K, V> toMap() {
		return super.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	/**
	 * 将键值对分组后再转为二维{@link Map}集合，最终返回一个{@link Table}集合
	 *
	 * @param rowKeyMapper  将键映射为父集合中键的方法
	 * @param colMapFactory 创建子集合的工厂方法
	 * @param operator      当存在重复键时的处理
	 * @param <N>           父集合的键类型
	 * @return 集合
	 * @see Collectors#groupingBy(Function, Supplier, Collector)
	 */
	public <N> Table<N, K, V> toTable(
			final BiFunction<? super K, ? super V, ? extends N> rowKeyMapper, final Supplier<Map<K, V>> colMapFactory, final BinaryOperator<V> operator) {
		Objects.requireNonNull(rowKeyMapper);
		Objects.requireNonNull(colMapFactory);
		Objects.requireNonNull(operator);
		final Map<N, Map<K, V>> rawMap = collect(Collectors.groupingBy(
				e -> rowKeyMapper.apply(e.getKey(), e.getValue()),
				HashMap::new,
				Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, operator, colMapFactory)
		));
		return new RowKeyTable<>(rawMap, colMapFactory::get);
	}

	/**
	 * 将键值对分组后再转为二维{@link HashMap}集合，最终返回一个{@link Table}集合
	 *
	 * @param rowKeyMapper 创建父集合的工厂方法
	 * @param <N>          父集合的键类型
	 * @return 集合
	 * @throws IllegalArgumentException 当父集合或子集合中的键重复时抛出
	 */
	public <N> Table<N, K, V> toTable(final BiFunction<? super K, ? super V, ? extends N> rowKeyMapper) {
		return toTable(rowKeyMapper, HashMap::new, throwingMerger());
	}

	/**
	 * 将键值对按值分组后再转为二维{@link Map}集合，最终返回一个{@link Table}集合
	 *
	 * @param rowKeyMapper  将键映射为父集合中键的方法
	 * @param colMapFactory 创建子集合的工厂方法
	 * @param operator      当存在重复键时的处理
	 * @param <N>           父集合的键类型
	 * @return 集合
	 */
	public <N> Table<N, K, V> toTableByKey(
			final Function<? super K, ? extends N> rowKeyMapper, final Supplier<Map<K, V>> colMapFactory, final BinaryOperator<V> operator) {
		return toTable((k, v) -> rowKeyMapper.apply(k), colMapFactory, operator);
	}

	/**
	 * 将键值对按键分组后再转为二维{@link HashMap}集合，最终返回一个{@link Table}集合
	 *
	 * @param rowKeyMapper 创建父集合的工厂方法
	 * @param <N>          父集合的键类型
	 * @return 集合
	 * @throws IllegalArgumentException 当父集合或子集合中的键重复时抛出
	 */
	public <N> Table<N, K, V> toTableByKey(final Function<? super K, ? extends N> rowKeyMapper) {
		return toTable((k, v) -> rowKeyMapper.apply(k));
	}

	/**
	 * 将键值对按值分组后再转为二维{@link Map}集合，最终返回一个{@link Table}集合
	 *
	 * @param rowKeyMapper  将键映射为父集合中键的方法
	 * @param colMapFactory 创建子集合的工厂方法
	 * @param operator      当存在重复键时的处理
	 * @param <N>           父集合的键类型
	 * @return 集合
	 */
	public <N> Table<N, K, V> toTableByValue(
			final Function<? super V, ? extends N> rowKeyMapper, final Supplier<Map<K, V>> colMapFactory, final BinaryOperator<V> operator) {
		return toTable((k, v) -> rowKeyMapper.apply(v), colMapFactory, operator);
	}

	/**
	 * 将键值对按键分组后再转为二维{@link HashMap}集合，最终返回一个{@link Table}集合
	 *
	 * @param rowKeyMapper 创建父集合的工厂方法
	 * @param <N>          父集合的键类型
	 * @return 集合
	 * @throws IllegalArgumentException 当父集合或子集合中的键重复时抛出
	 */
	public <N> Table<N, K, V> toTableByValue(final Function<? super V, ? extends N> rowKeyMapper) {
		return toTable((k, v) -> rowKeyMapper.apply(v));
	}

	/**
	 * 将键值对按键分组
	 *
	 * @return 集合
	 */
	public Map<K, List<V>> groupByKey() {
		return groupByKey(Collectors.toList());
	}

	/**
	 * 将键值对按键分组
	 *
	 * @param collector 对具有相同键的值的收集器
	 * @param <C>       值集合的类型
	 * @return 集合
	 */
	public <C extends Collection<V>> Map<K, C> groupByKey(final Collector<V, ?, C> collector) {
		return groupByKey((Supplier<Map<K, C>>) HashMap::new, collector);
	}

	/**
	 * 将键值对按键分组
	 *
	 * @param mapFactory 创建map集合的工厂方法
	 * @param collector  对具有相同键的值的收集器
	 * @param <C>        值集合的类型
	 * @param <M>        返回的map集合类型
	 * @return 集合
	 */
	public <C extends Collection<V>, M extends Map<K, C>> M groupByKey(
			final Supplier<M> mapFactory, final Collector<V, ?, C> collector) {
		return super.collect(Collectors.groupingBy(
				Map.Entry::getKey, mapFactory,
				CollectorUtil.transform(ArrayList::new, s -> s.stream().map(Map.Entry::getValue).collect(collector))
		));
	}

	/**
	 * 遍历键值对
	 *
	 * @param consumer 操作
	 */
	public void forEach(final BiConsumer<K, V> consumer) {
		Objects.requireNonNull(consumer);
		super.forEach(e -> consumer.accept(e.getKey(), e.getValue()));
	}

	/**
	 * 将键值对翻转
	 *
	 * @return {@code EntryStream}实例
	 */
	public EntryStream<V, K> inverse() {
		return new EntryStream<>(
				stream.map(e -> ofEntry(e.getValue(), e.getKey()))
		);
	}

	/**
	 * 收集键
	 *
	 * @param collector 收集器
	 * @param <R>       返回值类型
	 * @return 收集容器
	 */
	public <R> R collectKeys(final Collector<K, ?, R> collector) {
		return toKeyStream().collect(collector);
	}

	/**
	 * 收集值
	 *
	 * @param collector 收集器
	 * @param <R>       返回值类型
	 * @return 收集容器
	 */
	public <R> R collectValues(final Collector<V, ?, R> collector) {
		return toValueStream().collect(collector);
	}

	/**
	 * 是否存在任意符合条件的键值对
	 *
	 * @param predicate 判断条件
	 * @return 是否
	 */
	public boolean anyMatch(final BiPredicate<? super K, ? super V> predicate) {
		return super.anyMatch(e -> predicate.test(e.getKey(), e.getValue()));
	}

	/**
	 * 所有键值对是否都符合条件
	 *
	 * @param predicate 判断条件
	 * @return 是否
	 */
	public boolean allMatch(final BiPredicate<? super K, ? super V> predicate) {
		Objects.requireNonNull(predicate);
		return super.allMatch(e -> predicate.test(e.getKey(), e.getValue()));
	}

	/**
	 * 所有键值对是否都不符合条件
	 *
	 * @param predicate 判断条件
	 * @return 是否
	 */
	public boolean noneMatch(final BiPredicate<? super K, ? super V> predicate) {
		Objects.requireNonNull(predicate);
		return super.noneMatch(e -> predicate.test(e.getKey(), e.getValue()));
	}

	// ========================= private =========================

	/**
	 * 将键值对转为{@link AbstractMap.SimpleImmutableEntry}
	 */
	@SuppressWarnings("unchecked")
	static <K, V> Map.Entry<K, V> ofEntry(final Map.Entry<K, V> entry) {
		return ObjUtil.defaultIfNull(
				entry, e -> ofEntry(e.getKey(), e.getValue()), (Map.Entry<K, V>) EMPTY_ENTRY
		);
	}

	/**
	 * 将键值对转为{@link AbstractMap.SimpleImmutableEntry}
	 */
	static <K, V> Map.Entry<K, V> ofEntry(final K key, final V value) {
		return new AbstractMap.SimpleImmutableEntry<>(key, value);
	}

	/**
	 * 根据一个原始的流，返回一个新包装类实例
	 *
	 * @param stream 流
	 * @return 实现类
	 */
	@Override
	public EntryStream<K, V> wrap(final Stream<Map.Entry<K, V>> stream) {
		return new EntryStream<>(stream);
	}

	/**
	 * key重复时直接抛出异常
	 */
	private static <T> BinaryOperator<T> throwingMerger() {
		return (u, v) -> {
			throw new IllegalStateException(String.format("Duplicate key %s", u));
		};
	}

}
