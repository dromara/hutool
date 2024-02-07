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

package org.dromara.hutool.core.map;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.collection.iter.ArrayIter;
import org.dromara.hutool.core.collection.iter.IterUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * Map相关工具类
 *
 * @author Looly
 * @since 3.1.1
 */
public class MapUtil extends MapGetUtil {

	/**
	 * 默认初始大小
	 */
	public static final int DEFAULT_INITIAL_CAPACITY = 16;
	/**
	 * 默认增长因子，当Map的size达到 容量*增长因子时，开始扩充Map
	 */
	public static final float DEFAULT_LOAD_FACTOR = 0.75f;

	/**
	 * Map是否为空
	 *
	 * @param map 集合
	 * @return 是否为空
	 */
	public static boolean isEmpty(final Map<?, ?> map) {
		return null == map || map.isEmpty();
	}

	/**
	 * Map是否为非空
	 *
	 * @param map 集合
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final Map<?, ?> map) {
		return !isEmpty(map);
	}

	/**
	 * 如果提供的集合为{@code null}，返回一个不可变的默认空集合，否则返回原集合<br>
	 * 空集合使用{@link Collections#emptyMap()}
	 *
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param set 提供的集合，可能为null
	 * @return 原集合，若为null返回空集合
	 * @since 4.6.3
	 */
	public static <K, V> Map<K, V> emptyIfNull(final Map<K, V> set) {
		return (null == set) ? Collections.emptyMap() : set;
	}

	/**
	 * 如果给定Map为空，返回默认Map
	 *
	 * @param <T>        集合类型
	 * @param <K>        键类型
	 * @param <V>        值类型
	 * @param map        Map
	 * @param defaultMap 默认Map
	 * @return 非空（empty）的原Map或默认Map
	 * @since 4.6.9
	 */
	public static <T extends Map<K, V>, K, V> T defaultIfEmpty(final T map, final T defaultMap) {
		return isEmpty(map) ? defaultMap : map;
	}

	// ----------------------------------------------------------------------------------------------- new HashMap

	/**
	 * 新建一个HashMap
	 *
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @return HashMap对象
	 */
	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<>();
	}

	/**
	 * 新建一个HashMap
	 *
	 * @param <K>      Key类型
	 * @param <V>      Value类型
	 * @param size     初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75 + 1
	 * @param isLinked Map的Key是否有序，有序返回 {@link LinkedHashMap}，否则返回 {@link HashMap}
	 * @return HashMap对象
	 * @since 3.0.4
	 */
	public static <K, V> HashMap<K, V> newHashMap(final int size, final boolean isLinked) {
		final int initialCapacity = (int) (size / DEFAULT_LOAD_FACTOR) + 1;
		return isLinked ? new LinkedHashMap<>(initialCapacity) : new HashMap<>(initialCapacity);
	}

	/**
	 * 新建一个HashMap
	 *
	 * @param <K>  Key类型
	 * @param <V>  Value类型
	 * @param size 初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75 + 1
	 * @return HashMap对象
	 */
	public static <K, V> HashMap<K, V> newHashMap(final int size) {
		return newHashMap(size, false);
	}

	/**
	 * 新建一个HashMap
	 *
	 * @param <K>      Key类型
	 * @param <V>      Value类型
	 * @param isLinked Map的Key是否有序，有序返回 {@link LinkedHashMap}，否则返回 {@link HashMap}
	 * @return HashMap对象
	 */
	public static <K, V> HashMap<K, V> newHashMap(final boolean isLinked) {
		return newHashMap(DEFAULT_INITIAL_CAPACITY, isLinked);
	}

	/**
	 * 新建TreeMap，Key有序的Map
	 *
	 * @param <K>        key的类型
	 * @param <V>        value的类型
	 * @param comparator Key比较器
	 * @return TreeMap
	 * @since 3.2.3
	 */
	public static <K, V> TreeMap<K, V> newTreeMap(final Comparator<? super K> comparator) {
		return new TreeMap<>(comparator);
	}

	/**
	 * 新建TreeMap，Key有序的Map
	 *
	 * @param <K>        key的类型
	 * @param <V>        value的类型
	 * @param map        Map
	 * @param comparator Key比较器
	 * @return TreeMap
	 * @since 3.2.3
	 */
	public static <K, V> TreeMap<K, V> newTreeMap(final Map<K, V> map, final Comparator<? super K> comparator) {
		final TreeMap<K, V> treeMap = new TreeMap<>(comparator);
		if (isNotEmpty(map)) {
			treeMap.putAll(map);
		}
		return treeMap;
	}

	/**
	 * 创建键不重复Map
	 *
	 * @param <K>  key的类型
	 * @param <V>  value的类型
	 * @param size 初始容量
	 * @return {@link IdentityHashMap}
	 * @since 4.5.7
	 */
	public static <K, V> Map<K, V> newIdentityMap(final int size) {
		return new IdentityHashMap<>(size);
	}

	/**
	 * 新建一个初始容量为{@link MapUtil#DEFAULT_INITIAL_CAPACITY} 的{@link SafeConcurrentHashMap}
	 *
	 * @param <K> key的类型
	 * @param <V> value的类型
	 * @return {@link SafeConcurrentHashMap}
	 */
	public static <K, V> ConcurrentHashMap<K, V> newSafeConcurrentHashMap() {
		return new SafeConcurrentHashMap<>(DEFAULT_INITIAL_CAPACITY);
	}

	/**
	 * 新建一个{@link SafeConcurrentHashMap}
	 *
	 * @param size 初始容量，当传入的容量小于等于0时，容量为{@link MapUtil#DEFAULT_INITIAL_CAPACITY}
	 * @param <K>  key的类型
	 * @param <V>  value的类型
	 * @return {@link SafeConcurrentHashMap}
	 */
	public static <K, V> ConcurrentHashMap<K, V> newSafeConcurrentHashMap(final int size) {
		final int initCapacity = size <= 0 ? DEFAULT_INITIAL_CAPACITY : size;
		return new SafeConcurrentHashMap<>(initCapacity);
	}

	/**
	 * 传入一个Map将其转化为{@link SafeConcurrentHashMap}类型
	 *
	 * @param map map
	 * @param <K> key的类型
	 * @param <V> value的类型
	 * @return {@link SafeConcurrentHashMap}
	 */
	public static <K, V> ConcurrentHashMap<K, V> newSafeConcurrentHashMap(final Map<K, V> map) {
		if (isEmpty(map)) {
			return new ConcurrentHashMap<>(DEFAULT_INITIAL_CAPACITY);
		}
		return new SafeConcurrentHashMap<>(map);
	}

	/**
	 * 创建Map<br>
	 * 传入抽象Map{@link AbstractMap}和{@link Map}类将默认创建{@link HashMap}
	 *
	 * @param <K>     map键类型
	 * @param <V>     map值类型
	 * @param mapType map类型
	 * @return {@link Map}实例
	 */
	public static <K, V> Map<K, V> createMap(final Class<?> mapType) {
		return createMap(mapType, HashMap::new);
	}

	/**
	 * 创建Map<br>
	 * 传入抽象Map{@link AbstractMap}和{@link Map}类将默认创建{@link HashMap}
	 *
	 * @param <K>        map键类型
	 * @param <V>        map值类型
	 * @param mapType    map类型
	 * @param defaultMap 如果通过反射创建失败或提供的是抽象Map，则创建的默认Map
	 * @return {@link Map}实例
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> createMap(final Class<?> mapType, final Supplier<Map<K, V>> defaultMap) {
		Map<K, V> result = null;
		if (null != mapType && !mapType.isAssignableFrom(AbstractMap.class)) {
			try {
				result = (Map<K, V>) ConstructorUtil.newInstanceIfPossible(mapType);
			} catch (final Exception ignore) {
				// JDK9+抛出java.lang.reflect.InaccessibleObjectException
				// 跳过
			}
		}

		if (null == result) {
			result = defaultMap.get();
		}

		if (!result.isEmpty()) {
			// issue#3162@Github，在构造中put值，会导致新建map带有值内容，此处清空
			result.clear();
		}

		return result;
	}

	// ----------------------------------------------------------------------------------------------- value of

	/**
	 * 将单一键值对转换为Map
	 *
	 * @param <K>   键类型
	 * @param <V>   值类型
	 * @param key   键
	 * @param value 值
	 * @return {@link HashMap}
	 */
	public static <K, V> HashMap<K, V> of(final K key, final V value) {
		return of(key, value, false);
	}

	/**
	 * 将单一键值对转换为Map
	 *
	 * @param <K>     键类型
	 * @param <V>     值类型
	 * @param key     键
	 * @param value   值
	 * @param isOrder 是否有序
	 * @return {@link HashMap}
	 */
	public static <K, V> HashMap<K, V> of(final K key, final V value, final boolean isOrder) {
		final HashMap<K, V> map = newHashMap(isOrder);
		map.put(key, value);
		return map;
	}

	/**
	 * 根据给定的键值对数组创建HashMap对象，传入参数必须为key,value,key,value...
	 *
	 * <p>奇数参数必须为key，key最后会转换为String类型。</p>
	 * <p>偶数参数必须为value，可以为任意类型。</p>
	 *
	 * <pre>
	 * LinkedHashMap map = MapUtil.ofKvs(false,
	 * 	"RED", "#FF0000",
	 * 	"GREEN", "#00FF00",
	 * 	"BLUE", "#0000FF"
	 * );
	 * </pre>
	 *
	 * @param isLinked      是否使用{@link LinkedHashMap}
	 * @param keysAndValues 键值对列表，必须奇数参数为key，偶数参数为value
	 * @param <K>           键类型
	 * @param <V>           值类型
	 * @return LinkedHashMap
	 * @see Dict#ofKvs(Object...)
	 * @see Dict#ofKvs(Object...)
	 * @since 6.0.0
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> ofKvs(final boolean isLinked, final Object... keysAndValues) {
		if (ArrayUtil.isEmpty(keysAndValues)) {
			return newHashMap(0, isLinked);
		}

		Assert.isTrue(keysAndValues.length % 2 == 0, "keysAndValues not in pairs!");

		final Map<K, V> map = newHashMap(keysAndValues.length / 2, isLinked);
		for (int i = 0; i < keysAndValues.length; i += 2) {
			map.put((K) keysAndValues[i], (V) keysAndValues[i + 1]);
		}
		return map;
	}

	/**
	 * 根据给定的Pair数组创建Map对象
	 *
	 * @param <K>     键类型
	 * @param <V>     值类型
	 * @param entries 键值对
	 * @return Map
	 * @see #entry(Object, Object)
	 * @since 5.8.0
	 */
	@SafeVarargs
	public static <K, V> Map<K, V> ofEntries(final Map.Entry<K, V>... entries) {
		return ofEntries((Iterator<Entry<K, V>>) new ArrayIter<>(entries));
	}

	/**
	 * 将Entry集合转换为HashMap
	 *
	 * @param <K>       键类型
	 * @param <V>       值类型
	 * @param entryIter entry集合
	 * @return Map
	 */
	public static <K, V> HashMap<K, V> ofEntries(final Iterable<Entry<K, V>> entryIter) {
		return ofEntries(IterUtil.getIter(entryIter));
	}

	/**
	 * 将Entry集合转换为HashMap
	 *
	 * @param <K>       键类型
	 * @param <V>       值类型
	 * @param entryIter entry集合
	 * @return Map
	 */
	public static <K, V> HashMap<K, V> ofEntries(final Iterator<Entry<K, V>> entryIter) {
		final HashMap<K, V> map = new HashMap<>();
		if (IterUtil.isNotEmpty(entryIter)) {
			Entry<K, V> entry;
			while (entryIter.hasNext()) {
				entry = entryIter.next();
				map.put(entry.getKey(), entry.getValue());
			}
		}
		return map;
	}

	/**
	 * 将数组转换为Map（HashMap），支持数组元素类型为：
	 *
	 * <pre>
	 * Map.Entry
	 * 长度大于1的数组（取前两个值），如果不满足跳过此元素
	 * Iterable 长度也必须大于1（取前两个值），如果不满足跳过此元素
	 * Iterator 长度也必须大于1（取前两个值），如果不满足跳过此元素
	 * </pre>
	 *
	 * <pre>
	 * Map&lt;Object, Object&gt; colorMap = MapUtil.of(new String[][] {
	 *    { "RED", "#FF0000" },
	 *    { "GREEN", "#00FF00" },
	 *    { "BLUE", "#0000FF" }
	 * });
	 * </pre>
	 * <p>
	 * 参考：commons-lang
	 *
	 * @param array 数组。元素类型为Map.Entry、数组、Iterable、Iterator
	 * @return {@link HashMap}
	 * @since 3.0.8
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap<Object, Object> of(final Object[] array) {
		if (array == null) {
			return null;
		}
		final HashMap<Object, Object> map = new HashMap<>((int) (array.length * 1.5));
		for (int i = 0; i < array.length; i++) {
			final Object object = array[i];
			if (object instanceof Map.Entry) {
				final Map.Entry entry = (Map.Entry) object;
				map.put(entry.getKey(), entry.getValue());
			} else if (object instanceof Object[]) {
				final Object[] entry = (Object[]) object;
				if (entry.length > 1) {
					map.put(entry[0], entry[1]);
				}
			} else if (object instanceof Iterable) {
				final Iterator iter = ((Iterable) object).iterator();
				if (iter.hasNext()) {
					final Object key = iter.next();
					if (iter.hasNext()) {
						final Object value = iter.next();
						map.put(key, value);
					}
				}
			} else if (object instanceof Iterator) {
				final Iterator iter = ((Iterator) object);
				if (iter.hasNext()) {
					final Object key = iter.next();
					if (iter.hasNext()) {
						final Object value = iter.next();
						map.put(key, value);
					}
				}
			} else {
				throw new IllegalArgumentException(StrUtil.format("Array element {}, '{}', is not type of Map.Entry or Array or Iterable or Iterator", i, object));
			}
		}
		return map;
	}

	/**
	 * 行转列，合并相同的键，值合并为列表<br>
	 * 将Map列表中相同key的值组成列表做为Map的value<br>
	 * 是{@link #toMapList(Map)}的逆方法<br>
	 * 比如传入数据：
	 *
	 * <pre>
	 * [
	 *  {a: 1, b: 1, c: 1}
	 *  {a: 2, b: 2}
	 *  {a: 3, b: 3}
	 *  {a: 4}
	 * ]
	 * </pre>
	 * <p>
	 * 结果是：
	 *
	 * <pre>
	 * {
	 *   a: [1,2,3,4]
	 *   b: [1,2,3,]
	 *   c: [1]
	 * }
	 * </pre>
	 *
	 * @param <K>     键类型
	 * @param <V>     值类型
	 * @param mapList Map列表
	 * @return Map
	 */
	public static <K, V> Map<K, List<V>> toListMap(final Iterable<? extends Map<K, V>> mapList) {
		final Map<K, List<V>> resultMap = new HashMap<>();
		if (CollUtil.isEmpty(mapList)) {
			return resultMap;
		}

		for (final Map<K, V> map : mapList) {
			for (final Entry<K, V> entry : map.entrySet()) {
				resultMap.computeIfAbsent(entry.getKey(), k -> new ArrayList<>())
					.add(entry.getValue());
			}
		}

		return resultMap;
	}

	/**
	 * 列转行。将Map中值列表分别按照其位置与key组成新的map。<br>
	 * 是{@link #toListMap(Iterable)}的逆方法<br>
	 * 比如传入数据：
	 *
	 * <pre>
	 * {
	 *   a: [1,2,3,4]
	 *   b: [1,2,3,]
	 *   c: [1]
	 * }
	 * </pre>
	 * <p>
	 * 结果是：
	 *
	 * <pre>
	 * [
	 *  {a: 1, b: 1, c: 1}
	 *  {a: 2, b: 2}
	 *  {a: 3, b: 3}
	 *  {a: 4}
	 * ]
	 * </pre>
	 *
	 * @param <K>     键类型
	 * @param <V>     值类型
	 * @param listMap 列表Map
	 * @return Map列表
	 */
	public static <K, V> List<Map<K, V>> toMapList(final Map<K, ? extends Iterable<V>> listMap) {
		if (isEmpty(listMap)) {
			return ListUtil.zero();
		}

		final List<Map<K, V>> resultList = new ArrayList<>();
		for (final Entry<K, ? extends Iterable<V>> entry : listMap.entrySet()) {
			final Iterator<V> iterator = IterUtil.getIter(entry.getValue());
			if (IterUtil.isEmpty(iterator)) {
				continue;
			}
			final K key = entry.getKey();
			// 对已经存在的map添加元素
			for (final Map<K, V> map : resultList) {
				// 还可以继续添加元素
				if (iterator.hasNext()) {
					map.put(key, iterator.next());
				} else {
					break;
				}
			}
			// entry的value的个数 大于 当前列表的size, 直接新增map
			while (iterator.hasNext()) {
				resultList.add(MapUtil.of(key, iterator.next()));
			}
		}

		return resultList;
	}

	/**
	 * 将已知Map转换为key为驼峰风格的Map<br>
	 * 如果KEY为非String类型，保留原值
	 *
	 * @param <K> key的类型
	 * @param <V> value的类型
	 * @param map 原Map
	 * @return 驼峰风格Map
	 * @since 3.3.1
	 */
	public static <K, V> Map<K, V> toCamelCaseMap(final Map<K, V> map) {
		return (map instanceof LinkedHashMap) ? new CamelCaseLinkedMap<>(map) : new CamelCaseMap<>(map);
	}

	/**
	 * 将键值对转换为二维数组，第一维是key，第二纬是value
	 *
	 * @param map map
	 * @return 数组
	 * @since 4.1.9
	 */
	public static Object[][] toObjectArray(final Map<?, ?> map) {
		if (map == null) {
			return null;
		}
		final Object[][] result = new Object[map.size()][2];
		if (map.isEmpty()) {
			return result;
		}
		int index = 0;
		for (final Entry<?, ?> entry : map.entrySet()) {
			result[index][0] = entry.getKey();
			result[index][1] = entry.getValue();
			index++;
		}
		return result;
	}

	// ----------------------------------------------------------------------------------------------- join

	/**
	 * 将map转成字符串
	 *
	 * @param <K>               键类型
	 * @param <V>               值类型
	 * @param map               Map
	 * @param separator         entry之间的连接符
	 * @param keyValueSeparator kv之间的连接符
	 * @param otherParams       其它附加参数字符串（例如密钥）
	 * @return 连接字符串
	 * @since 3.1.1
	 */
	public static <K, V> String join(final Map<K, V> map, final String separator, final String keyValueSeparator, final String... otherParams) {
		return join(map, separator, keyValueSeparator, false, otherParams);
	}

	/**
	 * 根据参数排序后拼接为字符串，常用于签名
	 *
	 * @param params            参数
	 * @param separator         entry之间的连接符
	 * @param keyValueSeparator kv之间的连接符
	 * @param isIgnoreNull      是否忽略null的键和值
	 * @param otherParams       其它附加参数字符串（例如密钥）
	 * @return 签名字符串
	 * @since 5.0.4
	 */
	public static String sortJoin(final Map<?, ?> params, final String separator, final String keyValueSeparator, final boolean isIgnoreNull,
								  final String... otherParams) {
		return join(sort(params), separator, keyValueSeparator, isIgnoreNull, otherParams);
	}

	/**
	 * 将map转成字符串，忽略null的键和值
	 *
	 * @param <K>               键类型
	 * @param <V>               值类型
	 * @param map               Map
	 * @param separator         entry之间的连接符
	 * @param keyValueSeparator kv之间的连接符
	 * @param otherParams       其它附加参数字符串（例如密钥）
	 * @return 连接后的字符串
	 * @since 3.1.1
	 */
	public static <K, V> String joinIgnoreNull(final Map<K, V> map, final String separator, final String keyValueSeparator, final String... otherParams) {
		return join(map, separator, keyValueSeparator, true, otherParams);
	}

	/**
	 * 将map转成字符串
	 *
	 * @param <K>               键类型
	 * @param <V>               值类型
	 * @param map               Map，为空返回otherParams拼接
	 * @param separator         entry之间的连接符
	 * @param keyValueSeparator kv之间的连接符
	 * @param isIgnoreNull      是否忽略null的键和值
	 * @param otherParams       其它附加参数字符串（例如密钥）
	 * @return 连接后的字符串，map和otherParams为空返回""
	 * @since 3.1.1
	 */
	public static <K, V> String join(final Map<K, V> map, final String separator, final String keyValueSeparator,
									 final boolean isIgnoreNull, final String... otherParams) {
		return join(map, separator, keyValueSeparator, (entry) -> !isIgnoreNull || entry.getKey() != null && entry.getValue() != null, otherParams);
	}

	/**
	 * 将map转成字符串
	 *
	 * @param <K>               键类型
	 * @param <V>               值类型
	 * @param map               Map，为空返回otherParams拼接
	 * @param separator         entry之间的连接符
	 * @param keyValueSeparator kv之间的连接符
	 * @param predicate         键值对过滤
	 * @param otherParams       其它附加参数字符串（例如密钥）
	 * @return 连接后的字符串，map和otherParams为空返回""
	 * @since 3.1.1
	 */
	public static <K, V> String join(final Map<K, V> map, final String separator, final String keyValueSeparator,
									 final Predicate<Entry<K, V>> predicate, final String... otherParams) {
		return MapJoiner.of(separator, keyValueSeparator)
			.append(map, predicate)
			.append(otherParams)
			.toString();
	}

	// ----------------------------------------------------------------------------------------------- filter

	/**
	 * 编辑Map<br>
	 * 编辑过程通过传入的Editor实现来返回需要的元素内容，这个Editor实现可以实现以下功能：
	 *
	 * <pre>
	 * 1、过滤出需要的对象，如果返回{@code null}表示这个元素对象抛弃
	 * 2、修改元素对象，返回集合中为修改后的对象
	 * </pre>
	 *
	 * @param <K>    Key类型
	 * @param <V>    Value类型
	 * @param map    Map
	 * @param editor 编辑器接口
	 * @return 编辑后的Map
	 */
	public static <K, V> Map<K, V> edit(final Map<K, V> map, final UnaryOperator<Entry<K, V>> editor) {
		if (null == map || null == editor) {
			return map;
		}

		final Map<K, V> map2 = createMap(map.getClass(), () -> new HashMap<>(map.size(), 1f));
		if (isEmpty(map)) {
			return map2;
		}

		Entry<K, V> modified;
		for (final Entry<K, V> entry : map.entrySet()) {
			modified = editor.apply(entry);
			if (null != modified) {
				map2.put(modified.getKey(), modified.getValue());
			}
		}
		return map2;
	}


	/**
	 * 过滤<br>
	 * 过滤过程通过传入的Editor实现来返回需要的元素内容，这个Filter实现可以实现以下功能：
	 *
	 * <pre>
	 * 1、过滤出需要的对象，如果返回null表示这个元素对象抛弃
	 * </pre>
	 *
	 * @param <K>       Key类型
	 * @param <V>       Value类型
	 * @param map       Map
	 * @param predicate 过滤器接口，{@link Predicate#test(Object)}为{@code true}保留，{@code null}返回原Map
	 * @return 过滤后的Map
	 * @since 3.1.0
	 */
	public static <K, V> Map<K, V> filter(final Map<K, V> map, final Predicate<Entry<K, V>> predicate) {
		if (null == map || null == predicate) {
			return map;
		}
		return edit(map, t -> predicate.test(t) ? t : null);
	}


	/**
	 * 通过biFunction自定义一个规则，此规则将原Map中的元素转换成新的元素，生成新的Map返回<br>
	 * 变更过程通过传入的 {@link BiFunction} 实现来返回一个值可以为不同类型的 {@link Map}
	 *
	 * @param map        原有的map
	 * @param biFunction {@code lambda}，参数包含{@code key},{@code value}，返回值会作为新的{@code value}
	 * @param <K>        {@code key}的类型
	 * @param <V>        {@code value}的类型
	 * @param <R>        新的，修改后的{@code value}的类型
	 * @return 值可以为不同类型的 {@link Map}
	 * @since 5.8.0
	 */
	public static <K, V, R> Map<K, R> map(final Map<K, V> map, final BiFunction<K, V, R> biFunction) {
		if (null == map || null == biFunction) {
			return MapUtil.newHashMap();
		}
		return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, m -> biFunction.apply(m.getKey(), m.getValue())));
	}

	/**
	 * 过滤Map保留指定键值对，如果键不存在跳过
	 *
	 * @param <K>  Key类型
	 * @param <V>  Value类型
	 * @param map  原始Map
	 * @param keys 键列表，{@code null}返回原Map
	 * @return Map 结果，结果的Map类型与原Map保持一致
	 * @since 4.0.10
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> filter(final Map<K, V> map, final K... keys) {
		if (null == map || null == keys) {
			return map;
		}

		final Map<K, V> map2 = createMap(map.getClass(), () -> new HashMap<>(map.size(), 1f));
		if (isEmpty(map)) {
			return map2;
		}

		for (final K key : keys) {
			if (map.containsKey(key)) {
				map2.put(key, map.get(key));
			}
		}
		return map2;
	}

	/**
	 * Map的键和值互换
	 * 互换键值对不检查值是否有重复，如果有则后加入的元素替换先加入的元素<br>
	 * 值的顺序在HashMap中不确定，所以谁覆盖谁也不确定，在有序的Map中按照先后顺序覆盖，保留最后的值
	 *
	 * @param <T> 键和值类型
	 * @param map Map对象，键值类型必须一致
	 * @return 互换后的Map
	 * @see #inverse(Map)
	 * @since 3.2.2
	 */
	public static <T> Map<T, T> reverse(final Map<T, T> map) {
		return edit(map, t -> new Entry<T, T>() {

			@Override
			public T getKey() {
				return t.getValue();
			}

			@Override
			public T getValue() {
				return t.getKey();
			}

			@Override
			public T setValue(final T value) {
				throw new UnsupportedOperationException("Unsupported setValue method !");
			}
		});
	}

	/**
	 * Map的键和值互换<br>
	 * 互换键值对不检查值是否有重复，如果有则后加入的元素替换先加入的元素<br>
	 * 值的顺序在HashMap中不确定，所以谁覆盖谁也不确定，在有序的Map中按照先后顺序覆盖，保留最后的值
	 *
	 * @param <K> 键和值类型
	 * @param <V> 键和值类型
	 * @param map Map对象
	 * @return 互换后的Map
	 * @since 5.2.6
	 */
	public static <K, V> Map<V, K> inverse(final Map<K, V> map) {
		final Map<V, K> result = createMap(map.getClass());
		map.forEach((key, value) -> result.put(value, key));
		return result;
	}

	/**
	 * 排序已有Map，Key有序的Map，使用默认Key排序方式（字母顺序）
	 *
	 * @param <K> key的类型
	 * @param <V> value的类型
	 * @param map Map
	 * @return TreeMap
	 * @see #newTreeMap(Map, Comparator)
	 * @since 4.0.1
	 */
	public static <K, V> TreeMap<K, V> sort(final Map<K, V> map) {
		return sort(map, null);
	}

	/**
	 * 排序已有Map，Key有序的Map
	 *
	 * @param <K>        key的类型
	 * @param <V>        value的类型
	 * @param map        Map，为null返回null
	 * @param comparator Key比较器
	 * @return TreeMap，map为null返回null
	 * @see #newTreeMap(Map, Comparator)
	 * @since 4.0.1
	 */
	public static <K, V> TreeMap<K, V> sort(final Map<K, V> map, final Comparator<? super K> comparator) {
		if (null == map) {
			return null;
		}

		if (map instanceof TreeMap) {
			// 已经是可排序Map，此时只有比较器一致才返回原map
			final TreeMap<K, V> result = (TreeMap<K, V>) map;
			if (null == comparator || comparator.equals(result.comparator())) {
				return result;
			}
		}

		return newTreeMap(map, comparator);
	}

	/**
	 * 按照值排序，可选是否倒序
	 *
	 * @param map    需要对值排序的map
	 * @param <K>    键类型
	 * @param <V>    值类型
	 * @param isDesc 是否倒序
	 * @return 排序后新的Map
	 * @since 5.5.8
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(final Map<K, V> map, final boolean isDesc) {
		final Map<K, V> result = new LinkedHashMap<>();
		Comparator<Entry<K, V>> entryComparator = Entry.comparingByValue();
		if (isDesc) {
			entryComparator = entryComparator.reversed();
		}
		map.entrySet().stream().sorted(entryComparator).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
		return result;
	}

	/**
	 * 创建代理Map<br>
	 * {@link MapProxy}对Map做一次包装，提供各种getXXX方法
	 *
	 * @param map 被代理的Map
	 * @return {@link MapProxy}
	 * @since 3.2.0
	 */
	public static MapProxy createProxy(final Map<?, ?> map) {
		return MapProxy.of(map);
	}

	/**
	 * 创建Map包装类MapWrapper<br>
	 * {@link MapWrapper}对Map做一次包装
	 *
	 * @param <K> key的类型
	 * @param <V> value的类型
	 * @param map 被代理的Map
	 * @return {@link MapWrapper}
	 * @since 4.5.4
	 */
	public static <K, V> MapWrapper<K, V> wrap(final Map<K, V> map) {
		return new MapWrapper<>(map);
	}

	/**
	 * 将对应Map转换为不可修改的Map
	 *
	 * @param map Map
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @return 不修改Map
	 * @since 5.2.6
	 */
	public static <K, V> Map<K, V> view(final Map<K, V> map) {
		return Collections.unmodifiableMap(map);
	}

	// ----------------------------------------------------------------------------------------------- builder

	/**
	 * 创建链接调用map
	 *
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @return map创建类
	 */
	public static <K, V> MapBuilder<K, V> builder() {
		return builder(new HashMap<>());
	}

	/**
	 * 创建链接调用map
	 *
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param map 实际使用的map
	 * @return map创建类
	 */
	public static <K, V> MapBuilder<K, V> builder(final Map<K, V> map) {
		return new MapBuilder<>(map);
	}

	/**
	 * 创建链接调用map
	 *
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param k   key
	 * @param v   value
	 * @return map创建类
	 */
	public static <K, V> MapBuilder<K, V> builder(final K k, final V v) {
		return (builder(new HashMap<K, V>())).put(k, v);
	}

	/**
	 * 获取Map的部分key生成新的Map
	 *
	 * @param <K>  Key类型
	 * @param <V>  Value类型
	 * @param map  Map
	 * @param keys 键列表
	 * @return 新Map，只包含指定的key
	 * @since 4.0.6
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> getAny(final Map<K, V> map, final K... keys) {
		return filter(map, entry -> ArrayUtil.contains(keys, entry.getKey()));
	}

	/**
	 * 去掉Map中指定key的键值对，修改原Map
	 *
	 * @param <K>  Key类型
	 * @param <V>  Value类型
	 * @param <T>  Map类型
	 * @param map  Map
	 * @param keys 键列表
	 * @return 修改后的key
	 */
	@SuppressWarnings("unchecked")
	public static <K, V, T extends Map<K, V>> T removeAny(final T map, final K... keys) {
		for (final K key : keys) {
			map.remove(key);
		}
		return map;
	}

	/**
	 * 重命名键<br>
	 * 实现方式为移除然后重新put，当旧的key不存在直接返回<br>
	 * 当新的key存在，抛出{@link IllegalArgumentException} 异常
	 *
	 * @param <K>    key的类型
	 * @param <V>    value的类型
	 * @param map    Map
	 * @param oldKey 原键
	 * @param newKey 新键
	 * @return map
	 * @throws IllegalArgumentException 新key存在抛出此异常
	 * @since 4.5.16
	 */
	public static <K, V> Map<K, V> renameKey(final Map<K, V> map, final K oldKey, final K newKey) {
		if (isNotEmpty(map) && map.containsKey(oldKey)) {
			if (map.containsKey(newKey)) {
				throw new IllegalArgumentException(StrUtil.format("The key '{}' exist !", newKey));
			}
			map.put(newKey, map.remove(oldKey));
		}
		return map;
	}

	/**
	 * 去除Map中值为{@code null}的键值对<br>
	 * 注意：此方法在传入的Map上直接修改。
	 *
	 * @param <K> key的类型
	 * @param <V> value的类型
	 * @param map Map
	 * @return map
	 * @since 4.6.5
	 */
	public static <K, V> Map<K, V> removeNullValue(final Map<K, V> map) {
		if (isEmpty(map)) {
			return map;
		}

		map.entrySet().removeIf(entry -> null == entry.getValue());
		return map;
	}

	/**
	 * 返回一个空Map
	 *
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @return 空Map
	 * @see Collections#emptyMap()
	 * @since 5.3.1
	 */
	public static <K, V> Map<K, V> empty() {
		return Collections.emptyMap();
	}

	/**
	 * 返回一个初始大小为0的HashMap(初始为0，可加入元素)
	 *
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @return 初始大小为0的HashMap
	 */
	public static <K, V> Map<K, V> zero() {
		return new HashMap<>(0, 1);
	}

	/**
	 * 根据传入的Map类型不同，返回对应类型的空Map，支持类型包括：
	 *
	 * <pre>
	 *     1. NavigableMap
	 *     2. SortedMap
	 *     3. Map
	 * </pre>
	 *
	 * @param <K>      键类型
	 * @param <V>      值类型
	 * @param <T>      Map类型
	 * @param mapClass Map类型，null返回默认的Map
	 * @return 空Map
	 * @since 5.3.1
	 */
	@SuppressWarnings("unchecked")
	public static <K, V, T extends Map<K, V>> T empty(final Class<?> mapClass) {
		if (null == mapClass) {
			return (T) Collections.emptyMap();
		}
		if (NavigableMap.class == mapClass) {
			return (T) Collections.emptyNavigableMap();
		} else if (SortedMap.class == mapClass) {
			return (T) Collections.emptySortedMap();
		} else if (Map.class == mapClass) {
			return (T) Collections.emptyMap();
		}

		// 不支持空集合的集合类型
		throw new IllegalArgumentException(StrUtil.format("[{}] is not support to get empty!", mapClass));
	}

	/**
	 * 清除一个或多个Map集合内的元素，每个Map调用clear()方法
	 *
	 * @param maps 一个或多个Map
	 */
	public static void clear(final Map<?, ?>... maps) {
		for (final Map<?, ?> map : maps) {
			if (isNotEmpty(map)) {
				map.clear();
			}
		}
	}

	/**
	 * 从Map中获取指定键列表对应的值列表<br>
	 * 如果key在map中不存在或key对应值为null，则返回值列表对应位置的值也为null
	 *
	 * @param <K>  键类型
	 * @param <V>  值类型
	 * @param map  {@link Map}
	 * @param keys 键列表
	 * @return 值列表
	 * @since 3.0.8
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> ArrayList<V> valuesOfKeys(final Map<K, V> map, final K... keys) {
		return valuesOfKeys(map, (Iterator<K>) new ArrayIter<>(keys));
	}

	/**
	 * 从Map中获取指定键列表对应的值列表<br>
	 * 如果key在map中不存在或key对应值为null，则返回值列表对应位置的值也为null
	 *
	 * @param <K>  键类型
	 * @param <V>  值类型
	 * @param map  {@link Map}
	 * @param keys 键列表
	 * @return 值列表
	 * @since 3.0.9
	 */
	public static <K, V> ArrayList<V> valuesOfKeys(final Map<K, V> map, final Iterable<K> keys) {
		return valuesOfKeys(map, keys.iterator());
	}

	/**
	 * 从Map中获取指定键列表对应的值列表<br>
	 * 如果key在map中不存在或key对应值为null，则返回值列表对应位置的值也为null
	 *
	 * @param <K>  键类型
	 * @param <V>  值类型
	 * @param map  {@link Map}
	 * @param keys 键列表
	 * @return 值列表
	 * @since 5.7.20
	 */
	public static <K, V> ArrayList<V> valuesOfKeys(final Map<K, V> map, final Iterator<K> keys) {
		final ArrayList<V> values = new ArrayList<>();
		while (keys.hasNext()) {
			values.add(map.get(keys.next()));
		}
		return values;
	}

	/**
	 * 将键和值转换为{@link AbstractMap.SimpleImmutableEntry}<br>
	 * 返回的Entry不可变
	 *
	 * @param key   键
	 * @param value 值
	 * @param <K>   键类型
	 * @param <V>   值类型
	 * @return {@link AbstractMap.SimpleImmutableEntry}
	 * @since 5.8.0
	 */
	public static <K, V> Map.Entry<K, V> entry(final K key, final V value) {
		return entry(key, value, true);
	}

	/**
	 * 将键和值转换为{@link AbstractMap.SimpleEntry} 或者 {@link AbstractMap.SimpleImmutableEntry}
	 *
	 * @param key         键
	 * @param value       值
	 * @param <K>         键类型
	 * @param <V>         值类型
	 * @param isImmutable 是否不可变Entry
	 * @return {@link AbstractMap.SimpleEntry} 或者 {@link AbstractMap.SimpleImmutableEntry}
	 * @since 5.8.0
	 */
	public static <K, V> Map.Entry<K, V> entry(final K key, final V value, final boolean isImmutable) {
		return isImmutable ?
			new AbstractMap.SimpleImmutableEntry<>(key, value) :
			new AbstractMap.SimpleEntry<>(key, value);
	}

	/**
	 * 将列表按照给定的键生成器规则和值生成器规则，加入到给定的Map中
	 *
	 * @param resultMap 结果Map，通过传入map对象决定结果的Map类型，如果为{@code null}，默认使用HashMap
	 * @param iterable  值列表
	 * @param keyMapper Map的键映射
	 * @param <K>       键类型
	 * @param <V>       值类型
	 * @return HashMap
	 * @since 5.3.6
	 */
	public static <K, V> Map<K, V> putAll(final Map<K, V> resultMap, final Iterable<V> iterable, final Function<V, K> keyMapper) {
		return putAll(resultMap, iterable, keyMapper, Function.identity());
	}

	/**
	 * 将列表按照给定的键生成器规则和值生成器规则，加入到给定的Map中
	 *
	 * @param resultMap   结果Map，通过传入map对象决定结果的Map类型
	 * @param iterable    值列表
	 * @param keyMapper   Map的键映射
	 * @param valueMapper Map的值映射
	 * @param <T>         列表值类型
	 * @param <K>         键类型
	 * @param <V>         值类型
	 * @return HashMap
	 * @since 5.3.6
	 */
	public static <T, K, V> Map<K, V> putAll(final Map<K, V> resultMap, final Iterable<T> iterable, final Function<T, K> keyMapper, final Function<T, V> valueMapper) {
		return putAll(resultMap, IterUtil.getIter(iterable), keyMapper, valueMapper);
	}

	/**
	 * 将列表按照给定的键生成器规则和值生成器规则，加入到给定的Map中
	 *
	 * @param resultMap 结果Map，通过传入map对象决定结果的Map类型，如果为{@code null}，默认使用HashMap
	 * @param iterator  值列表
	 * @param keyMapper Map的键映射
	 * @param <K>       键类型
	 * @param <V>       值类型
	 * @return HashMap
	 * @since 5.3.6
	 */
	public static <K, V> Map<K, V> putAll(final Map<K, V> resultMap, final Iterator<V> iterator, final Function<V, K> keyMapper) {
		return putAll(resultMap, iterator, keyMapper, Function.identity());
	}

	/**
	 * 将列表按照给定的键生成器规则和值生成器规则，加入到给定的Map中
	 *
	 * @param resultMap   结果Map，通过传入map对象决定结果的Map类型，如果为{@code null}，默认使用HashMap
	 * @param iterator    值列表
	 * @param keyMapper   Map的键映射
	 * @param valueMapper Map的值映射
	 * @param <T>         列表值类型
	 * @param <K>         键类型
	 * @param <V>         值类型
	 * @return HashMap
	 * @since 5.3.6
	 */
	public static <T, K, V> Map<K, V> putAll(Map<K, V> resultMap, final Iterator<T> iterator, final Function<T, K> keyMapper, final Function<T, V> valueMapper) {
		if (null == resultMap) {
			resultMap = MapUtil.newHashMap();
		}
		if (ObjUtil.isNull(iterator)) {
			return resultMap;
		}

		T value;
		while (iterator.hasNext()) {
			value = iterator.next();
			resultMap.put(keyMapper.apply(value), valueMapper.apply(value));
		}
		return resultMap;
	}

	/**
	 * 根据给定的entry列表，根据entry的key进行分组;
	 *
	 * @param <K>     键类型
	 * @param <V>     值类型
	 * @param entries entry列表
	 * @return entries
	 * @since 5.8.6
	 */
	public static <K, V> Map<K, List<V>> grouping(final Iterable<Map.Entry<K, V>> entries) {
		if (CollUtil.isEmpty(entries)) {
			return zero();
		}

		final Map<K, List<V>> map = new HashMap<>();
		for (final Map.Entry<K, V> pair : entries) {
			final List<V> values = map.computeIfAbsent(pair.getKey(), k -> new ArrayList<>());
			values.add(pair.getValue());
		}
		return map;
	}

	/**
	 * 如果 key 对应的 value 不存在，则使用获取 mappingFunction 重新计算后的值，并保存为该 key 的 value，否则返回 value。<br>
	 * 解决使用ConcurrentHashMap.computeIfAbsent导致的死循环问题。（issues#2349）<br>
	 * A temporary workaround for Java 8 specific performance issue JDK-8161372 .<br>
	 * This class should be removed once we drop Java 8 support.
	 *
	 * <p>
	 * 注意此方法只能用于JDK8
	 * </p>
	 *
	 * @param <K>             键类型
	 * @param <V>             值类型
	 * @param map             Map，一般用于线程安全的Map
	 * @param key             键
	 * @param mappingFunction 值计算函数
	 * @return 值
	 * @see <a href="https://bugs.openjdk.java.net/browse/JDK-8161372">https://bugs.openjdk.java.net/browse/JDK-8161372</a>
	 */
	public static <K, V> V computeIfAbsentForJdk8(final Map<K, V> map, final K key, final Function<? super K, ? extends V> mappingFunction) {
		V value = map.get(key);
		if (null == value) {
			value = mappingFunction.apply(key);
			final V res = map.putIfAbsent(key, value);
			if (null != res) {
				// issues#I6RVMY
				// 如果旧值存在，说明其他线程已经赋值成功，putIfAbsent没有执行，返回旧值
				return res;
			}
			// 如果旧值不存在，说明赋值成功，返回当前值

			// Dubbo的解决方式，判空后调用依旧无法解决死循环问题
			// 见：Issue2349Test
			//value = map.computeIfAbsent(key, mappingFunction);
		}
		return value;
	}

	/**
	 * 将一个Map按照固定大小拆分成多个子Map
	 *
	 * @param <K>  键类型
	 * @param <V>  值类型
	 * @param map  Map
	 * @param size 子Map的大小
	 * @return 子Map列表
	 * @since 5.8.26
	 */
	public static <K, V> List<Map<K, V>> partition(final Map<K, V> map, final int size) {
		Assert.notNull(map);
		if (size <= 0) {
			throw new IllegalArgumentException("Size must be greater than 0");
		}
		final List<Map<K, V>> list = new ArrayList<>();
		final Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			final Map<K, V> subMap = new HashMap<>(size);
			for (int i = 0; i < size && iterator.hasNext(); i++) {
				final Map.Entry<K, V> entry = iterator.next();
				subMap.put(entry.getKey(), entry.getValue());
			}
			list.add(subMap);
		}
		return list;
	}
}
