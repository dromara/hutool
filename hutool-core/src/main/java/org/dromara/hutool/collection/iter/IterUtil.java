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

package org.dromara.hutool.collection.iter;

import org.dromara.hutool.collection.ListUtil;
import org.dromara.hutool.lang.Assert;
import org.dromara.hutool.map.MapUtil;
import org.dromara.hutool.reflect.FieldUtil;
import org.dromara.hutool.reflect.MethodUtil;
import org.dromara.hutool.text.StrJoiner;
import org.dromara.hutool.array.ArrayUtil;
import org.dromara.hutool.util.ObjUtil;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * {@link Iterable} 和 {@link Iterator} 相关工具类
 *
 * @author Looly
 * @since 3.1.0
 */
public class IterUtil {

	/**
	 * 获取{@link Iterator}
	 *
	 * @param iterable {@link Iterable}
	 * @param <T>      元素类型
	 * @return 当iterable为null返回{@code null}，否则返回对应的{@link Iterator}
	 * @since 5.7.2
	 */
	public static <T> Iterator<T> getIter(final Iterable<T> iterable) {
		return null == iterable ? null : iterable.iterator();
	}

	/**
	 * Iterable是否为空
	 *
	 * @param iterable Iterable对象
	 * @return 是否为空
	 */
	public static boolean isEmpty(final Iterable<?> iterable) {
		return null == iterable || isEmpty(iterable.iterator());
	}

	/**
	 * Iterator是否为空
	 *
	 * @param iterator Iterator对象
	 * @return 是否为空
	 */
	public static boolean isEmpty(final Iterator<?> iterator) {
		return null == iterator || false == iterator.hasNext();
	}

	/**
	 * Iterable是否为空
	 *
	 * @param iterable Iterable对象
	 * @return 是否为空
	 */
	public static boolean isNotEmpty(final Iterable<?> iterable) {
		return null != iterable && isNotEmpty(iterable.iterator());
	}

	/**
	 * Iterator是否为空
	 *
	 * @param iterator Iterator对象
	 * @return 是否为空
	 */
	public static boolean isNotEmpty(final Iterator<?> iterator) {
		return null != iterator && iterator.hasNext();
	}

	/**
	 * 是否包含{@code null}元素<br>
	 * <ul>
	 *     <li>Iterator为{@code null}，返回{@code true}</li>
	 *     <li>Iterator为空集合，即元素个数为0，返回{@code false}</li>
	 *     <li>Iterator中元素为""，返回{@code false}</li>
	 * </ul>
	 *
	 * @param iter 被检查的{@link Iterator}对象，如果为{@code null} 返回true
	 * @return 是否包含{@code null}元素
	 */
	public static boolean hasNull(final Iterator<?> iter) {
		if (null == iter) {
			return true;
		}
		while (iter.hasNext()) {
			if (null == iter.next()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 是否全部元素为null
	 *
	 * @param iter iter 被检查的{@link Iterable}对象，如果为{@code null} 返回true
	 * @return 是否全部元素为null
	 * @since 3.3.0
	 */
	public static boolean isAllNull(final Iterable<?> iter) {
		return isAllNull(null == iter ? null : iter.iterator());
	}

	/**
	 * 是否全部元素为null
	 *
	 * @param iter iter 被检查的{@link Iterator}对象，如果为{@code null} 返回true
	 * @return 是否全部元素为null
	 * @since 3.3.0
	 */
	public static boolean isAllNull(final Iterator<?> iter) {
		return null == getFirstNoneNull(iter);
	}

	/**
	 * 根据集合返回一个元素计数的 {@link Map}<br>
	 * 所谓元素计数就是假如这个集合中某个元素出现了n次，那将这个元素做为key，n做为value<br>
	 * 例如：[a,b,c,c,c] 得到：<br>
	 * a: 1<br>
	 * b: 1<br>
	 * c: 3<br>
	 *
	 * @param <T>  集合元素类型
	 * @param iter {@link Iterator}，如果为null返回一个空的Map
	 * @return {@link Map}
	 */
	public static <T> Map<T, Integer> countMap(final Iterator<T> iter) {
		final Map<T, Integer> countMap = new HashMap<>();
		if (null != iter) {
			while (iter.hasNext()) {
				countMap.merge(iter.next(), 1, Integer::sum);
			}
		}
		return countMap;
	}

	/**
	 * 字段值与列表值对应的Map，常用于元素对象中有唯一ID时需要按照这个ID查找对象的情况<br>
	 * 例如：车牌号 =》车
	 *
	 * @param <K>       字段名对应值得类型，不确定请使用Object
	 * @param <V>       对象类型
	 * @param iter      对象列表
	 * @param fieldName 字段名（会通过反射获取其值）
	 * @return 某个字段值与对象对应Map
	 * @since 4.0.4
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> fieldValueMap(final Iterator<V> iter, final String fieldName) {
		return MapUtil.putAll(new HashMap<>(), iter, (value) -> (K) FieldUtil.getFieldValue(value, fieldName));
	}

	/**
	 * 两个字段值组成新的Map
	 *
	 * @param <K>               字段名对应值得类型，不确定请使用Object
	 * @param <V>               值类型，不确定使用Object
	 * @param iter              对象列表
	 * @param fieldNameForKey   做为键的字段名（会通过反射获取其值）
	 * @param fieldNameForValue 做为值的字段名（会通过反射获取其值）
	 * @return 某个字段值与对象对应Map
	 * @since 4.0.10
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> fieldValueAsMap(final Iterator<?> iter, final String fieldNameForKey, final String fieldNameForValue) {
		return MapUtil.putAll(new HashMap<>(), iter,
				(value) -> (K) FieldUtil.getFieldValue(value, fieldNameForKey),
				(value) -> (V) FieldUtil.getFieldValue(value, fieldNameForValue)
		);
	}

	/**
	 * 获取指定Bean列表中某个字段，生成新的列表
	 *
	 * @param <R>       返回元素类型
	 * @param <V>       对象类型
	 * @param iterable  对象列表
	 * @param fieldName 字段名（会通过反射获取其值）
	 * @return 某个字段值与对象对应Map
	 * @since 4.6.2
	 */
	public static <V, R> List<R> fieldValueList(final Iterable<V> iterable, final String fieldName) {
		return fieldValueList(getIter(iterable), fieldName);
	}

	/**
	 * 获取指定Bean列表中某个字段，生成新的列表
	 *
	 * @param <R>       返回元素类型
	 * @param <V>       对象类型
	 * @param iter      对象列表
	 * @param fieldName 字段名（会通过反射获取其值）
	 * @return 某个字段值与对象对应Map
	 * @since 4.0.10
	 */
	@SuppressWarnings("unchecked")
	public static <V, R> List<R> fieldValueList(final Iterator<V> iter, final String fieldName) {
		final List<R> result = new ArrayList<>();
		if (null != iter) {
			V value;
			while (iter.hasNext()) {
				value = iter.next();
				result.add((R) FieldUtil.getFieldValue(value, fieldName));
			}
		}
		return result;
	}

	/**
	 * 以 conjunction 为分隔符将集合转换为字符串<br>
	 * 如果集合元素为数组、{@link Iterable}或{@link Iterator}，则递归组合其为字符串
	 *
	 * @param <T>         集合元素类型
	 * @param iterator    集合
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static <T> String join(final Iterator<T> iterator, final CharSequence conjunction) {
		return StrJoiner.of(conjunction).append(iterator).toString();
	}

	/**
	 * 以 conjunction 为分隔符将集合转换为字符串<br>
	 * 如果集合元素为数组、{@link Iterable}或{@link Iterator}，则递归组合其为字符串
	 *
	 * @param <T>         集合元素类型
	 * @param iterator    集合
	 * @param conjunction 分隔符
	 * @param prefix      每个元素添加的前缀，null表示不添加
	 * @param suffix      每个元素添加的后缀，null表示不添加
	 * @return 连接后的字符串
	 * @since 4.0.10
	 */
	public static <T> String join(final Iterator<T> iterator, final CharSequence conjunction, final String prefix, final String suffix) {
		return StrJoiner.of(conjunction, prefix, suffix)
				// 每个元素都添加前后缀
				.setWrapElement(true)
				.append(iterator)
				.toString();
	}

	/**
	 * 以 conjunction 为分隔符将集合转换为字符串<br>
	 * 如果集合元素为数组、{@link Iterable}或{@link Iterator}，则递归组合其为字符串
	 *
	 * @param <T>         集合元素类型
	 * @param iterator    集合
	 * @param conjunction 分隔符
	 * @param func        集合元素转换器，将元素转换为字符串
	 * @return 连接后的字符串
	 * @since 5.6.7
	 */
	public static <T> String join(final Iterator<T> iterator, final CharSequence conjunction, final Function<T, ? extends CharSequence> func) {
		if (null == iterator) {
			return null;
		}

		return StrJoiner.of(conjunction).append(iterator, func).toString();
	}

	/**
	 * 将键列表和值列表转换为Map<br>
	 * 以键为准，值与键位置需对应。如果键元素数多于值元素，多余部分值用null代替。<br>
	 * 如果值多于键，忽略多余的值。
	 *
	 * @param <K>    键类型
	 * @param <V>    值类型
	 * @param keys   键列表
	 * @param values 值列表
	 * @return 标题内容Map
	 * @since 3.1.0
	 */
	public static <K, V> Map<K, V> toMap(final Iterable<K> keys, final Iterable<V> values) {
		return toMap(keys, values, false);
	}

	/**
	 * 将键列表和值列表转换为Map<br>
	 * 以键为准，值与键位置需对应。如果键元素数多于值元素，多余部分值用null代替。<br>
	 * 如果值多于键，忽略多余的值。
	 *
	 * @param <K>     键类型
	 * @param <V>     值类型
	 * @param keys    键列表
	 * @param values  值列表
	 * @param isOrder 是否有序
	 * @return 标题内容Map
	 * @since 4.1.12
	 */
	public static <K, V> Map<K, V> toMap(final Iterable<K> keys, final Iterable<V> values, final boolean isOrder) {
		return toMap(null == keys ? null : keys.iterator(), null == values ? null : values.iterator(), isOrder);
	}

	/**
	 * 将键列表和值列表转换为Map<br>
	 * 以键为准，值与键位置需对应。如果键元素数多于值元素，多余部分值用null代替。<br>
	 * 如果值多于键，忽略多余的值。
	 *
	 * @param <K>    键类型
	 * @param <V>    值类型
	 * @param keys   键列表
	 * @param values 值列表
	 * @return 标题内容Map
	 * @since 3.1.0
	 */
	public static <K, V> Map<K, V> toMap(final Iterator<K> keys, final Iterator<V> values) {
		return toMap(keys, values, false);
	}

	/**
	 * 将键列表和值列表转换为Map<br>
	 * 以键为准，值与键位置需对应。如果键元素数多于值元素，多余部分值用null代替。<br>
	 * 如果值多于键，忽略多余的值。
	 *
	 * @param <K>     键类型
	 * @param <V>     值类型
	 * @param keys    键列表
	 * @param values  值列表
	 * @param isOrder 是否有序
	 * @return 标题内容Map
	 * @since 4.1.12
	 */
	public static <K, V> Map<K, V> toMap(final Iterator<K> keys, final Iterator<V> values, final boolean isOrder) {
		final Map<K, V> resultMap = MapUtil.newHashMap(isOrder);
		if (isNotEmpty(keys)) {
			while (keys.hasNext()) {
				resultMap.put(keys.next(), (null != values && values.hasNext()) ? values.next() : null);
			}
		}
		return resultMap;
	}

	/**
	 * 将列表转成值为List的HashMap
	 *
	 * @param iterable  值列表
	 * @param keyMapper Map的键映射
	 * @param <K>       键类型
	 * @param <V>       值类型
	 * @return HashMap
	 * @since 5.3.6
	 */
	public static <K, V> Map<K, List<V>> toListMap(final Iterable<V> iterable, final Function<V, K> keyMapper) {
		return toListMap(iterable, keyMapper, v -> v);
	}

	/**
	 * 将列表转成值为List的HashMap
	 *
	 * @param iterable    值列表
	 * @param keyMapper   Map的键映射
	 * @param valueMapper Map中List的值映射
	 * @param <T>         列表值类型
	 * @param <K>         键类型
	 * @param <V>         值类型
	 * @return HashMap
	 * @since 5.3.6
	 */
	public static <T, K, V> Map<K, List<V>> toListMap(final Iterable<T> iterable, final Function<T, K> keyMapper, final Function<T, V> valueMapper) {
		return toListMap(MapUtil.newHashMap(), iterable, keyMapper, valueMapper);
	}

	/**
	 * 将列表转成值为List的Map集合
	 *
	 * @param resultMap   结果Map，可自定义结果Map类型
	 * @param iterable    值列表
	 * @param keyMapper   Map的键映射
	 * @param valueMapper Map中List的值映射
	 * @param <T>         列表值类型
	 * @param <K>         键类型
	 * @param <V>         值类型
	 * @return HashMap
	 * @since 5.3.6
	 */
	public static <T, K, V> Map<K, List<V>> toListMap(Map<K, List<V>> resultMap, final Iterable<T> iterable, final Function<T, K> keyMapper, final Function<T, V> valueMapper) {
		if (null == resultMap) {
			resultMap = MapUtil.newHashMap();
		}
		if (ObjUtil.isNull(iterable)) {
			return resultMap;
		}

		for (final T value : iterable) {
			resultMap.computeIfAbsent(keyMapper.apply(value), k -> new ArrayList<>()).add(valueMapper.apply(value));
		}

		return resultMap;
	}

	/**
	 * 将列表转成HashMap
	 *
	 * @param iterable  值列表
	 * @param keyMapper Map的键映射
	 * @param <K>       键类型
	 * @param <V>       值类型
	 * @return HashMap
	 * @since 5.3.6
	 */
	public static <K, V> Map<K, V> toMap(final Iterable<V> iterable, final Function<V, K> keyMapper) {
		return toMap(iterable, keyMapper, v -> v);
	}

	/**
	 * 将列表转成HashMap
	 *
	 * @param iterable    值列表
	 * @param keyMapper   Map的键映射
	 * @param valueMapper Map的值映射
	 * @param <T>         列表值类型
	 * @param <K>         键类型
	 * @param <V>         值类型
	 * @return HashMap
	 * @since 5.3.6
	 */
	public static <T, K, V> Map<K, V> toMap(final Iterable<T> iterable, final Function<T, K> keyMapper, final Function<T, V> valueMapper) {
		return MapUtil.putAll(MapUtil.newHashMap(), iterable, keyMapper, valueMapper);
	}

	/**
	 * Enumeration转换为Iterator
	 * <p>
	 * Adapt the specified {@code Enumeration} to the {@code Iterator} interface
	 *
	 * @param <E> 集合元素类型
	 * @param e   {@link Enumeration}
	 * @return {@link Iterator}
	 */
	public static <E> Iterator<E> asIterator(final Enumeration<E> e) {
		return new EnumerationIter<>(Objects.requireNonNull(e));
	}

	/**
	 * {@link Iterator} 转为 {@link Iterable}, 但是仅可使用一次
	 *
	 * @param <E>  元素类型
	 * @param iter {@link Iterator}
	 * @return {@link Iterable}
	 */
	public static <E> Iterable<E> asIterable(final Iterator<E> iter) {
		return () -> iter;
	}

	/**
	 * 遍历{@link Iterator}，获取指定index位置的元素
	 *
	 * @param iterator {@link Iterator}
	 * @param index    位置
	 * @param <E>      元素类型
	 * @return 元素，找不到元素返回{@code null}
	 * @throws IndexOutOfBoundsException index &lt; 0时报错
	 * @since 5.8.0
	 */
	public static <E> E get(final Iterator<E> iterator, int index) throws IndexOutOfBoundsException {
		if (null == iterator) {
			return null;
		}
		Assert.isTrue(index >= 0, "[index] must be >= 0");
		while (iterator.hasNext()) {
			index--;
			if (-1 == index) {
				return iterator.next();
			}
			iterator.next();
		}
		return null;
	}

	/**
	 * 获取集合的第一个元素
	 *
	 * @param <T>      集合元素类型
	 * @param iterator {@link Iterator}
	 * @return 第一个元素
	 */
	public static <T> T getFirst(final Iterator<T> iterator) {
		return get(iterator, 0);
	}

	/**
	 * 获取集合的第一个非空元素
	 *
	 * @param <T>      集合元素类型
	 * @param iterator {@link Iterator}
	 * @return 第一个非空元素，null表示未找到
	 * @since 5.7.2
	 */
	public static <T> T getFirstNoneNull(final Iterator<T> iterator) {
		return getFirst(iterator, Objects::nonNull);
	}

	/**
	 * 返回{@link Iterator}中第一个匹配规则的值
	 *
	 * @param <T>       数组元素类型
	 * @param iterator  {@link Iterator}
	 * @param predicate 匹配接口，实现此接口自定义匹配规则
	 * @return 匹配元素，如果不存在匹配元素或{@link Iterator}为空，返回 {@code null}
	 * @since 5.7.5
	 */
	public static <T> T getFirst(final Iterator<T> iterator, final Predicate<T> predicate) {
		Assert.notNull(predicate, "Matcher must be not null !");
		if (null != iterator) {
			while (iterator.hasNext()) {
				final T next = iterator.next();
				if (predicate.test(next)) {
					return next;
				}
			}
		}
		return null;
	}

	/**
	 * 获得{@link Iterable}对象的元素类型（通过第一个非空元素判断）<br>
	 * 注意，此方法至少会调用多次next方法
	 *
	 * @param iterable {@link Iterable}
	 * @return 元素类型，当列表为空或元素全部为null时，返回null
	 */
	public static Class<?> getElementType(final Iterable<?> iterable) {
		return getElementType(getIter(iterable));
	}

	/**
	 * 获得{@link Iterator}对象的元素类型（通过第一个非空元素判断）<br>
	 * 注意，此方法至少会调用多次next方法
	 *
	 * @param iterator {@link Iterator}，为 {@code null}返回{@code null}
	 * @return 元素类型，当列表为空或元素全部为{@code null}时，返回{@code null}
	 */
	public static Class<?> getElementType(final Iterator<?> iterator) {
		if (null == iterator) {
			return null;
		}
		final Object ele = getFirstNoneNull(iterator);
		return null == ele ? null : ele.getClass();
	}

	/**
	 * 编辑，此方法产生一个新{@link ArrayList}<br>
	 * 编辑过程通过传入的Editor实现来返回需要的元素内容，这个Editor实现可以实现以下功能：
	 *
	 * <pre>
	 * 1、过滤出需要的对象，如果返回null表示这个元素对象抛弃
	 * 2、修改元素对象，返回集合中为修改后的对象
	 * </pre>
	 *
	 * @param <T>    集合元素类型
	 * @param iter   集合
	 * @param editor 编辑器接口, {@code null}表示不编辑
	 * @return 过滤后的集合
	 * @since 5.7.1
	 */
	public static <T> List<T> edit(final Iterator<T> iter, final UnaryOperator<T> editor) {
		final List<T> result = new ArrayList<>();
		if (null == iter) {
			return result;
		}

		T modified;
		while (iter.hasNext()) {
			modified = (null == editor) ? iter.next() : editor.apply(iter.next());
			if (null != modified) {
				result.add(modified);
			}
		}
		return result;
	}

	/**
	 * 移除集合中满足条件的所有元素，此方法在原集合上直接修改<br>
	 * 通过实现{@link Predicate}接口，完成元素的移除，可以实现以下功能：
	 *
	 * <pre>
	 * 1、移除指定对象，{@link Predicate#test(Object)}方法返回{@code true}的对象将被使用{@link Iterator#remove()}方法移除。
	 * </pre>
	 *
	 * @param <E>       集合元素类型
	 * @param iter      集合
	 * @param predicate 过滤器接口，删除{@link Predicate#test(Object)}为{@code true}的元素
	 * @return 编辑后的集合
	 * @since 6.0.0
	 */
	public static <E> Iterator<E> remove(final Iterator<E> iter, final Predicate<E> predicate) {
		if (null == iter || null == predicate) {
			return iter;
		}

		while (iter.hasNext()) {
			if (predicate.test(iter.next())) {
				iter.remove();
			}
		}
		return iter;
	}

	/**
	 * 过滤{@link Iterator}并将过滤后满足条件的元素添加到List中
	 *
	 * @param <E>       元素类型
	 * @param iter      {@link Iterator}
	 * @param predicate 过滤器，{@link Predicate#test(Object)}为{@code true}保留
	 * @return ArrayList
	 * @since 5.7.22
	 */
	public static <E> List<E> filterToList(final Iterator<E> iter, final Predicate<E> predicate) {
		return ListUtil.of(filtered(iter, predicate));
	}

	/**
	 * 获取一个新的 {@link FilterIter}，用于过滤指定元素
	 *
	 * @param iterator  被包装的 {@link Iterator}
	 * @param predicate 过滤断言，{@link Predicate#test(Object)}为{@code true}保留元素。
	 * @param <E>       元素类型
	 * @return {@link FilterIter}
	 * @since 5.8.0
	 */
	public static <E> FilterIter<E> filtered(final Iterator<? extends E> iterator, final Predicate<? super E> predicate) {
		return new FilterIter<>(iterator, predicate);
	}

	/**
	 * 返回一个空Iterator
	 *
	 * @param <T> 元素类型
	 * @return 空Iterator
	 * @see Collections#emptyIterator()
	 * @since 5.3.1
	 */
	public static <T> Iterator<T> empty() {
		return Collections.emptyIterator();
	}

	/**
	 * 按照给定函数，转换{@link Iterator}为另一种类型的{@link Iterator}
	 *
	 * @param <F>      源元素类型
	 * @param <T>      目标元素类型
	 * @param iterator 源{@link Iterator}
	 * @param function 转换函数
	 * @return 转换后的{@link Iterator}
	 * @since 5.4.3
	 */
	public static <F, T> Iterator<T> trans(final Iterator<F> iterator, final Function<? super F, ? extends T> function) {
		return new TransIter<>(iterator, function);
	}

	/**
	 * 返回 Iterable 对象的元素数量
	 *
	 * @param iterable Iterable对象
	 * @return Iterable对象的元素数量
	 * @since 5.5.0
	 */
	public static int size(final Iterable<?> iterable) {
		if (null == iterable) {
			return 0;
		}

		if (iterable instanceof Collection<?>) {
			return ((Collection<?>) iterable).size();
		} else {
			return size(iterable.iterator());
		}
	}

	/**
	 * 返回 Iterator 对象的元素数量
	 *
	 * @param iterator Iterator对象
	 * @return Iterator对象的元素数量
	 * @since 5.5.0
	 */
	public static int size(final Iterator<?> iterator) {
		int size = 0;
		if (iterator != null) {
			while (iterator.hasNext()) {
				iterator.next();
				size++;
			}
		}
		return size;
	}

	/**
	 * <p>判断两个{@link Iterable}中的元素与其顺序是否相同 <br>
	 * 当满足下列情况时返回{@code true}：
	 * <ul>
	 *     <li>两个{@link Iterable}都为{@code null}；</li>
	 *     <li>两个{@link Iterable}满足{@code iterable1 == iterable2}；</li>
	 *     <li>两个{@link Iterable}所有具有相同下标的元素皆满足{@link Objects#equals(Object, Object)}；</li>
	 * </ul>
	 * 此方法来自Apache-Commons-Collections4。
	 *
	 * @param iterable1 列表1
	 * @param iterable2 列表2
	 * @return 是否相同
	 * @since 5.6.0
	 */
	public static boolean isEqualList(final Iterable<?> iterable1, final Iterable<?> iterable2) {
		if (iterable1 == iterable2) {
			return true;
		}
		if (iterable1 == null || iterable2 == null) {
			return false;
		}
		final Iterator<?> iter1 = iterable1.iterator();
		final Iterator<?> iter2 = iterable2.iterator();
		Object obj1;
		Object obj2;
		while (iter1.hasNext() && iter2.hasNext()) {
			obj1 = iter1.next();
			obj2 = iter2.next();
			if (false == Objects.equals(obj1, obj2)) {
				return false;
			}
		}
		// 当两个Iterable长度不一致时返回false
		return false == (iter1.hasNext() || iter2.hasNext());
	}

	/**
	 * 清空指定{@link Iterator}，此方法遍历后调用{@link Iterator#remove()}移除每个元素
	 *
	 * @param iterator {@link Iterator}
	 * @since 5.7.23
	 */
	public static void clear(final Iterator<?> iterator) {
		if (null != iterator) {
			while (iterator.hasNext()) {
				iterator.next();
				iterator.remove();
			}
		}
	}

	/**
	 * 遍历{@link Iterator}<br>
	 * 当consumer为{@code null}表示不处理，但是依旧遍历{@link Iterator}
	 *
	 * @param iterator {@link Iterator}
	 * @param consumer 节点消费，{@code null}表示不处理
	 * @param <E>      元素类型
	 * @since 5.8.0
	 */
	public static <E> void forEach(final Iterator<E> iterator, final Consumer<? super E> consumer) {
		if (iterator != null) {
			while (iterator.hasNext()) {
				final E element = iterator.next();
				if (null != consumer) {
					consumer.accept(element);
				}
			}
		}
	}

	/**
	 * 拼接 {@link Iterator}为字符串
	 *
	 * @param iterator {@link Iterator}
	 * @param <E>      元素类型
	 * @return 字符串
	 * @since 5.8.0
	 */
	public static <E> String toStr(final Iterator<E> iterator) {
		return toStr(iterator, ObjUtil::toString);
	}

	/**
	 * 拼接 {@link Iterator}为字符串
	 *
	 * @param iterator  {@link Iterator}
	 * @param transFunc 元素转字符串函数
	 * @param <E>       元素类型
	 * @return 字符串
	 * @since 5.8.0
	 */
	public static <E> String toStr(final Iterator<E> iterator, final Function<? super E, String> transFunc) {
		return toStr(iterator, transFunc, ", ", "[", "]");
	}

	/**
	 * 拼接 {@link Iterator}为字符串
	 *
	 * @param iterator  {@link Iterator}
	 * @param transFunc 元素转字符串函数
	 * @param delimiter 分隔符
	 * @param prefix    前缀
	 * @param suffix    后缀
	 * @param <E>       元素类型
	 * @return 字符串
	 * @since 5.8.0
	 */
	public static <E> String toStr(final Iterator<E> iterator,
								   final Function<? super E, String> transFunc,
								   final String delimiter,
								   final String prefix,
								   final String suffix) {
		final StrJoiner strJoiner = StrJoiner.of(delimiter, prefix, suffix);
		strJoiner.append(iterator, transFunc);
		return strJoiner.toString();
	}

	/**
	 * 从给定的对象中获取可能存在的{@link Iterator}，规则如下：
	 * <ul>
	 *   <li>null - null</li>
	 *   <li>Iterator - 直接返回</li>
	 *   <li>Enumeration - {@link EnumerationIter}</li>
	 *   <li>Collection - 调用{@link Collection#iterator()}</li>
	 *   <li>Map - Entry的{@link Iterator}</li>
	 *   <li>Dictionary - values (elements) enumeration returned as iterator</li>
	 *   <li>array - {@link ArrayIter}</li>
	 *   <li>NodeList - {@link NodeListIter}</li>
	 *   <li>Node - 子节点</li>
	 *   <li>object with iterator() public method，通过反射访问</li>
	 *   <li>object - 单对象的{@link ArrayIter}</li>
	 * </ul>
	 *
	 * @param obj 可以获取{@link Iterator}的对象
	 * @return {@link Iterator}，如果提供对象为{@code null}，返回{@code null}
	 * @since 5.8.0
	 */
	public static Iterator<?> getIter(final Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof Iterator) {
			return (Iterator<?>) obj;
		} else if (obj instanceof Iterable) {
			return ((Iterable<?>) obj).iterator();
		} else if (ArrayUtil.isArray(obj)) {
			return new ArrayIter<>(obj);
		} else if (obj instanceof Enumeration) {
			return new EnumerationIter<>((Enumeration<?>) obj);
		} else if (obj instanceof Map) {
			return ((Map<?, ?>) obj).entrySet().iterator();
		} else if (obj instanceof NodeList) {
			return new NodeListIter((NodeList) obj);
		} else if (obj instanceof Node) {
			// 遍历子节点
			return new NodeListIter(((Node) obj).getChildNodes());
		} else if (obj instanceof Dictionary) {
			return new EnumerationIter<>(((Dictionary<?, ?>) obj).elements());
		}

		// 反射获取
		try {
			final Object iterator = MethodUtil.invoke(obj, "iterator");
			if (iterator instanceof Iterator) {
				return (Iterator<?>) iterator;
			}
		} catch (final RuntimeException ignore) {
			// ignore
		}
		return new ArrayIter<>(new Object[]{obj});
	}
}
