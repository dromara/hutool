package cn.hutool.core.collection;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Editor;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.lang.Matcher;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

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
	public static <T> Iterator<T> getIter(Iterable<T> iterable) {
		return null == iterable ? null : iterable.iterator();
	}

	/**
	 * Iterable是否为空
	 *
	 * @param iterable Iterable对象
	 * @return 是否为空
	 */
	public static boolean isEmpty(Iterable<?> iterable) {
		return null == iterable || isEmpty(iterable.iterator());
	}

	/**
	 * Iterator是否为空
	 *
	 * @param Iterator Iterator对象
	 * @return 是否为空
	 */
	public static boolean isEmpty(Iterator<?> Iterator) {
		return null == Iterator || false == Iterator.hasNext();
	}

	/**
	 * Iterable是否为空
	 *
	 * @param iterable Iterable对象
	 * @return 是否为空
	 */
	public static boolean isNotEmpty(Iterable<?> iterable) {
		return null != iterable && isNotEmpty(iterable.iterator());
	}

	/**
	 * Iterator是否为空
	 *
	 * @param Iterator Iterator对象
	 * @return 是否为空
	 */
	public static boolean isNotEmpty(Iterator<?> Iterator) {
		return null != Iterator && Iterator.hasNext();
	}

	/**
	 * 是否包含{@code null}元素
	 *
	 * @param iter 被检查的{@link Iterable}对象，如果为{@code null} 返回true
	 * @return 是否包含{@code null}元素
	 */
	public static boolean hasNull(Iterable<?> iter) {
		return hasNull(null == iter ? null : iter.iterator());
	}

	/**
	 * 是否包含{@code null}元素
	 *
	 * @param iter 被检查的{@link Iterator}对象，如果为{@code null} 返回true
	 * @return 是否包含{@code null}元素
	 */
	public static boolean hasNull(Iterator<?> iter) {
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
	public static boolean isAllNull(Iterable<?> iter) {
		return isAllNull(null == iter ? null : iter.iterator());
	}

	/**
	 * 是否全部元素为null
	 *
	 * @param iter iter 被检查的{@link Iterator}对象，如果为{@code null} 返回true
	 * @return 是否全部元素为null
	 * @since 3.3.0
	 */
	public static boolean isAllNull(Iterator<?> iter) {
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
	public static <T> Map<T, Integer> countMap(Iterator<T> iter) {
		final HashMap<T, Integer> countMap = new HashMap<>();
		if (null != iter) {
			T t;
			while (iter.hasNext()) {
				t = iter.next();
				countMap.put(t, countMap.getOrDefault(t, 0) + 1);
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
	public static <K, V> Map<K, V> fieldValueMap(Iterator<V> iter, String fieldName) {
		return toMap(iter, new HashMap<>(), (value) -> (K) ReflectUtil.getFieldValue(value, fieldName));
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
	public static <K, V> Map<K, V> fieldValueAsMap(Iterator<?> iter, String fieldNameForKey, String fieldNameForValue) {
		return toMap(iter, new HashMap<>(),
				(value) -> (K) ReflectUtil.getFieldValue(value, fieldNameForKey),
				(value) -> (V) ReflectUtil.getFieldValue(value, fieldNameForValue)
		);
	}

	/**
	 * 获取指定Bean列表中某个字段，生成新的列表
	 *
	 * @param <V>       对象类型
	 * @param iterable  对象列表
	 * @param fieldName 字段名（会通过反射获取其值）
	 * @return 某个字段值与对象对应Map
	 * @since 4.6.2
	 */
	public static <V> List<Object> fieldValueList(Iterable<V> iterable, String fieldName) {
		return fieldValueList(getIter(iterable), fieldName);
	}

	/**
	 * 获取指定Bean列表中某个字段，生成新的列表
	 *
	 * @param <V>       对象类型
	 * @param iter      对象列表
	 * @param fieldName 字段名（会通过反射获取其值）
	 * @return 某个字段值与对象对应Map
	 * @since 4.0.10
	 */
	public static <V> List<Object> fieldValueList(Iterator<V> iter, String fieldName) {
		final List<Object> result = new ArrayList<>();
		if (null != iter) {
			V value;
			while (iter.hasNext()) {
				value = iter.next();
				result.add(ReflectUtil.getFieldValue(value, fieldName));
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
	public static <T> String join(Iterator<T> iterator, CharSequence conjunction) {
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
	public static <T> String join(Iterator<T> iterator, CharSequence conjunction, String prefix, String suffix) {
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
	public static <T> String join(Iterator<T> iterator, CharSequence conjunction, Function<T, ? extends CharSequence> func) {
		if (null == iterator) {
			return null;
		}

		return StrJoiner.of(conjunction).append(iterator, func).toString();
	}

	/**
	 * 将Entry集合转换为HashMap
	 *
	 * @param <K>       键类型
	 * @param <V>       值类型
	 * @param entryIter entry集合
	 * @return Map
	 */
	public static <K, V> HashMap<K, V> toMap(Iterable<Entry<K, V>> entryIter) {
		final HashMap<K, V> map = new HashMap<>();
		if (isNotEmpty(entryIter)) {
			for (Entry<K, V> entry : entryIter) {
				map.put(entry.getKey(), entry.getValue());
			}
		}
		return map;
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
	public static <K, V> Map<K, V> toMap(Iterable<K> keys, Iterable<V> values) {
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
	public static <K, V> Map<K, V> toMap(Iterable<K> keys, Iterable<V> values, boolean isOrder) {
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
	public static <K, V> Map<K, V> toMap(Iterator<K> keys, Iterator<V> values) {
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
	public static <K, V> Map<K, V> toMap(Iterator<K> keys, Iterator<V> values, boolean isOrder) {
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
	public static <K, V> Map<K, List<V>> toListMap(Iterable<V> iterable, Function<V, K> keyMapper) {
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
	public static <T, K, V> Map<K, List<V>> toListMap(Iterable<T> iterable, Function<T, K> keyMapper, Function<T, V> valueMapper) {
		return toListMap(MapUtil.newHashMap(), iterable, keyMapper, valueMapper);
	}

	/**
	 * 将列表转成值为List的HashMap
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
	public static <T, K, V> Map<K, List<V>> toListMap(Map<K, List<V>> resultMap, Iterable<T> iterable, Function<T, K> keyMapper, Function<T, V> valueMapper) {
		if (null == resultMap) {
			resultMap = MapUtil.newHashMap();
		}
		if (ObjectUtil.isNull(iterable)) {
			return resultMap;
		}

		for (T value : iterable) {
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
	public static <K, V> Map<K, V> toMap(Iterable<V> iterable, Function<V, K> keyMapper) {
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
	public static <T, K, V> Map<K, V> toMap(Iterable<T> iterable, Function<T, K> keyMapper, Function<T, V> valueMapper) {
		return toMap(MapUtil.newHashMap(), iterable, keyMapper, valueMapper);
	}

	/**
	 * 将列表转成Map
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
	public static <T, K, V> Map<K, V> toMap(Map<K, V> resultMap, Iterable<T> iterable, Function<T, K> keyMapper, Function<T, V> valueMapper) {
		if (null == resultMap) {
			resultMap = MapUtil.newHashMap();
		}
		if (ObjectUtil.isNull(iterable)) {
			return resultMap;
		}

		for (T value : iterable) {
			resultMap.put(keyMapper.apply(value), valueMapper.apply(value));
		}

		return resultMap;
	}

	/**
	 * Iterator转List<br>
	 * 不判断，直接生成新的List
	 *
	 * @param <E>  元素类型
	 * @param iter {@link Iterator}
	 * @return List
	 * @since 4.0.6
	 */
	public static <E> List<E> toList(Iterable<E> iter) {
		if (null == iter) {
			return null;
		}
		return toList(iter.iterator());
	}

	/**
	 * Iterator转List<br>
	 * 不判断，直接生成新的List
	 *
	 * @param <E>  元素类型
	 * @param iter {@link Iterator}
	 * @return List
	 * @since 4.0.6
	 */
	public static <E> List<E> toList(Iterator<E> iter) {
		return ListUtil.toList(iter);
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
	public static <E> Iterator<E> asIterator(Enumeration<E> e) {
		return new EnumerationIter<>(e);
	}

	/**
	 * {@link Iterator} 转为 {@link Iterable}
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
	 * @since 5.8.0
	 */
	public static <E> E get(final Iterator<E> iterator, int index) throws IndexOutOfBoundsException {
		if(null == iterator){
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
	 * 获取集合的第一个元素，如果集合为空（null或者空集合），返回{@code null}
	 *
	 * @param <T>      集合元素类型
	 * @param iterable {@link Iterable}
	 * @return 第一个元素，为空返回{@code null}
	 */
	public static <T> T getFirst(Iterable<T> iterable) {
		if (iterable instanceof List) {
			final List<T> list = (List<T>) iterable;
			return CollUtil.isEmpty(list) ? null: list.get(0);
		}

		return getFirst(getIter(iterable));
	}

	/**
	 * 获取集合的第一个非空元素
	 *
	 * @param <T>      集合元素类型
	 * @param iterable {@link Iterable}
	 * @return 第一个元素
	 * @since 5.7.2
	 */
	public static <T> T getFirstNoneNull(Iterable<T> iterable) {
		if (null == iterable) {
			return null;
		}
		return getFirstNoneNull(iterable.iterator());
	}

	/**
	 * 获取集合的第一个元素
	 *
	 * @param <T>      集合元素类型
	 * @param iterator {@link Iterator}
	 * @return 第一个元素
	 */
	public static <T> T getFirst(Iterator<T> iterator) {
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
	public static <T> T getFirstNoneNull(Iterator<T> iterator) {
		return firstMatch(iterator, Objects::nonNull);
	}

	/**
	 * 返回{@link Iterator}中第一个匹配规则的值
	 *
	 * @param <T>      数组元素类型
	 * @param iterator {@link Iterator}
	 * @param matcher  匹配接口，实现此接口自定义匹配规则
	 * @return 匹配元素，如果不存在匹配元素或{@link Iterator}为空，返回 {@code null}
	 * @since 5.7.5
	 */
	public static <T> T firstMatch(Iterator<T> iterator, Matcher<T> matcher) {
		Assert.notNull(matcher, "Matcher must be not null !");
		if (null != iterator) {
			while (iterator.hasNext()) {
				final T next = iterator.next();
				if (matcher.match(next)) {
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
	public static Class<?> getElementType(Iterable<?> iterable) {
		return getElementType(getIter(iterable));
	}

	/**
	 * 获得{@link Iterator}对象的元素类型（通过第一个非空元素判断）<br>
	 * 注意，此方法至少会调用多次next方法
	 *
	 * @param iterator {@link Iterator}，为 {@code null}返回{@code null}
	 * @return 元素类型，当列表为空或元素全部为{@code null}时，返回{@code null}
	 */
	public static Class<?> getElementType(Iterator<?> iterator) {
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
	public static <T> List<T> edit(Iterable<T> iter, Editor<T> editor) {
		final List<T> result = new ArrayList<>();
		if (null == iter) {
			return result;
		}

		T modified;
		for (T t : iter) {
			modified = (null == editor) ? t : editor.edit(t);
			if (null != modified) {
				result.add(modified);
			}
		}
		return result;
	}

	/**
	 * 过滤集合，此方法在原集合上直接修改<br>
	 * 通过实现Filter接口，完成元素的过滤，这个Filter实现可以实现以下功能：
	 *
	 * <pre>
	 * 1、过滤出需要的对象，{@link Filter#accept(Object)}方法返回false的对象将被使用{@link Iterator#remove()}方法移除
	 * </pre>
	 *
	 * @param <T>    集合类型
	 * @param <E>    集合元素类型
	 * @param iter   集合
	 * @param filter 过滤器接口
	 * @return 编辑后的集合
	 * @since 4.6.5
	 */
	public static <T extends Iterable<E>, E> T filter(T iter, Filter<E> filter) {
		if (null == iter) {
			return null;
		}

		filter(iter.iterator(), filter);

		return iter;
	}

	/**
	 * 过滤集合，此方法在原集合上直接修改<br>
	 * 通过实现Filter接口，完成元素的过滤，这个Filter实现可以实现以下功能：
	 *
	 * <pre>
	 * 1、过滤出需要的对象，{@link Filter#accept(Object)}方法返回false的对象将被使用{@link Iterator#remove()}方法移除
	 * </pre>
	 *
	 * @param <E>    集合元素类型
	 * @param iter   集合
	 * @param filter 过滤器接口，删除{@link Filter#accept(Object)}为{@code false}的元素
	 * @return 编辑后的集合
	 * @since 4.6.5
	 */
	public static <E> Iterator<E> filter(Iterator<E> iter, Filter<E> filter) {
		if (null == iter || null == filter) {
			return iter;
		}

		while (iter.hasNext()) {
			if (false == filter.accept(iter.next())) {
				iter.remove();
			}
		}
		return iter;
	}

	/**
	 * 过滤{@link Iterator}并将过滤后满足条件的元素添加到List中
	 *
	 * @param <E>    元素类型
	 * @param iter   {@link Iterator}
	 * @param filter 过滤器，保留{@link Filter#accept(Object)}为{@code true}的元素
	 * @return ArrayList
	 * @since 5.7.22
	 */
	public static <E> List<E> filterToList(Iterator<E> iter, Filter<E> filter) {
		return toList(filtered(iter, filter));
	}

	/**
	 * 获取一个新的 {@link FilterIter}，用于过滤指定元素
	 *
	 * @param iterator 被包装的 {@link Iterator}
	 * @param filter   过滤断言，当{@link Filter#accept(Object)}为{@code true}时保留元素，{@code false}抛弃元素
	 * @param <E>      元素类型
	 * @return {@link FilterIter}
	 * @since 5.8.0
	 */
	public static <E> FilterIter<E> filtered(final Iterator<? extends E> iterator, final Filter<? super E> filter) {
		return new FilterIter<>(iterator, filter);
	}

	/**
	 * Iterator转换为Map，转换规则为：<br>
	 * 按照keyFunc函数规则根据元素对象生成Key，元素作为值
	 *
	 * @param <K>      Map键类型
	 * @param <V>      Map值类型
	 * @param iterator 数据列表
	 * @param map      Map对象，转换后的键值对加入此Map，通过传入此对象自定义Map类型
	 * @param keyFunc  生成key的函数
	 * @return 生成的map
	 * @since 5.2.6
	 */
	public static <K, V> Map<K, V> toMap(Iterator<V> iterator, Map<K, V> map, Func1<V, K> keyFunc) {
		return toMap(iterator, map, keyFunc, (value) -> value);
	}

	/**
	 * 集合转换为Map，转换规则为：<br>
	 * 按照keyFunc函数规则根据元素对象生成Key，按照valueFunc函数规则根据元素对象生成value组成新的Map
	 *
	 * @param <K>       Map键类型
	 * @param <V>       Map值类型
	 * @param <E>       元素类型
	 * @param iterator  数据列表
	 * @param map       Map对象，转换后的键值对加入此Map，通过传入此对象自定义Map类型
	 * @param keyFunc   生成key的函数
	 * @param valueFunc 生成值的策略函数
	 * @return 生成的map
	 * @since 5.2.6
	 */
	public static <K, V, E> Map<K, V> toMap(Iterator<E> iterator, Map<K, V> map, Func1<E, K> keyFunc, Func1<E, V> valueFunc) {
		if (null == iterator) {
			return map;
		}

		if (null == map) {
			map = MapUtil.newHashMap(true);
		}

		E element;
		while (iterator.hasNext()) {
			element = iterator.next();
			try {
				map.put(keyFunc.call(element), valueFunc.call(element));
			} catch (Exception e) {
				throw new UtilException(e);
			}
		}
		return map;
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
	public static <F, T> Iterator<T> trans(Iterator<F> iterator, Function<? super F, ? extends T> function) {
		return new TransIter<>(iterator, function);
	}

	/**
	 * 返回 Iterable 对象的元素数量
	 *
	 * @param iterable Iterable对象
	 * @return Iterable对象的元素数量
	 * @since 5.5.0
	 */
	public static int size(Iterable<?> iterable) {
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
	public static int size(Iterator<?> iterator) {
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
	 * 判断两个{@link Iterable} 是否元素和顺序相同，返回{@code true}的条件是：
	 * <ul>
	 *     <li>两个{@link Iterable}必须长度相同</li>
	 *     <li>两个{@link Iterable}元素相同index的对象必须equals，满足{@link Objects#equals(Object, Object)}</li>
	 * </ul>
	 * 此方法来自Apache-Commons-Collections4。
	 *
	 * @param list1 列表1
	 * @param list2 列表2
	 * @return 是否相同
	 * @since 5.6.0
	 */
	public static boolean isEqualList(Iterable<?> list1, Iterable<?> list2) {
		if (list1 == list2) {
			return true;
		}

		final Iterator<?> it1 = list1.iterator();
		final Iterator<?> it2 = list2.iterator();
		Object obj1;
		Object obj2;
		while (it1.hasNext() && it2.hasNext()) {
			obj1 = it1.next();
			obj2 = it2.next();

			if (false == Objects.equals(obj1, obj2)) {
				return false;
			}
		}

		// 当两个Iterable长度不一致时返回false
		return false == (it1.hasNext() || it2.hasNext());
	}

	/**
	 * 清空指定{@link Iterator}，此方法遍历后调用{@link Iterator#remove()}移除每个元素
	 *
	 * @param iterator {@link Iterator}
	 * @since 5.7.23
	 */
	public static void clear(Iterator<?> iterator) {
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
		return toStr(iterator, ObjectUtil::toString);
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
			final Object iterator = ReflectUtil.invoke(obj, "iterator");
			if (iterator instanceof Iterator) {
				return (Iterator<?>) iterator;
			}
		} catch (final RuntimeException ignore) {
			// ignore
		}
		return new ArrayIter<>(new Object[]{obj});
	}
}
