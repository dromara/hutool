package cn.hutool.core.collection;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

/**
 * {@link Iterable} 和 {@link Iterator} 相关工具类
 *
 * @author Looly
 * @since 3.1.0
 */
public class IterUtil {

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
		if (null == iter) {
			return true;
		}

		while (iter.hasNext()) {
			if (null != iter.next()) {
				return false;
			}
		}
		return true;
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
	 * @param iter {@link Iterable}，如果为null返回一个空的Map
	 * @return {@link Map}
	 * @deprecated 如果对象同时实现Iterable和Iterator接口会产生歧义，请使用CollUtil.countMap
	 */
	@Deprecated
	public static <T> Map<T, Integer> countMap(Iterable<T> iter) {
		return countMap(null == iter ? null : iter.iterator());
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
			Integer count;
			T t;
			while (iter.hasNext()) {
				t = iter.next();
				count = countMap.get(t);
				if (null == count) {
					countMap.put(t, 1);
				} else {
					countMap.put(t, count + 1);
				}
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
	 * @deprecated 如果对象同时实现Iterable和Iterator接口会产生歧义，请使用CollUtil.fieldValueMap
	 */
	@Deprecated
	public static <K, V> Map<K, V> fieldValueMap(Iterable<V> iter, String fieldName) {
		return fieldValueMap(null == iter ? null : iter.iterator(), fieldName);
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
	 * @param iterable          对象列表
	 * @param fieldNameForKey   做为键的字段名（会通过反射获取其值）
	 * @param fieldNameForValue 做为值的字段名（会通过反射获取其值）
	 * @return 某个字段值与对象对应Map
	 * @since 4.6.2
	 * @deprecated 如果对象同时实现Iterable和Iterator接口会产生歧义，请使用CollUtil.fieldValueMap
	 */
	@Deprecated
	public static <K, V> Map<K, V> fieldValueAsMap(Iterable<?> iterable, String fieldNameForKey, String fieldNameForValue) {
		return fieldValueAsMap(null == iterable ? null : iterable.iterator(), fieldNameForKey, fieldNameForValue);
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
		return fieldValueList(null == iterable ? null : iterable.iterator(), fieldName);
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
	 * 以 conjunction 为分隔符将集合转换为字符串
	 *
	 * @param <T>         集合元素类型
	 * @param iterable    {@link Iterable}
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 * @deprecated 如果对象同时实现Iterable和Iterator接口会产生歧义，请使用CollUtil.join
	 */
	@Deprecated
	public static <T> String join(Iterable<T> iterable, CharSequence conjunction) {
		if (null == iterable) {
			return null;
		}
		return join(iterable.iterator(), conjunction);
	}

	/**
	 * 以 conjunction 为分隔符将集合转换为字符串
	 *
	 * @param <T>         集合元素类型
	 * @param iterable    {@link Iterable}
	 * @param conjunction 分隔符
	 * @param prefix      每个元素添加的前缀，null表示不添加
	 * @param suffix      每个元素添加的后缀，null表示不添加
	 * @return 连接后的字符串
	 * @since 4.0.10
	 * @deprecated 如果对象同时实现Iterable和Iterator接口会产生歧义，请使用CollUtil.join
	 */
	@Deprecated
	public static <T> String join(Iterable<T> iterable, CharSequence conjunction, String prefix, String suffix) {
		if (null == iterable) {
			return null;
		}
		return join(iterable.iterator(), conjunction, prefix, suffix);
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
		return join(iterator, conjunction, null, null);
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
		if (null == iterator) {
			return null;
		}

		final StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		T item;
		while (iterator.hasNext()) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(conjunction);
			}

			item = iterator.next();
			if (ArrayUtil.isArray(item)) {
				sb.append(ArrayUtil.join(ArrayUtil.wrap(item), conjunction, prefix, suffix));
			} else if (item instanceof Iterable<?>) {
				sb.append(join((Iterable<?>) item, conjunction, prefix, suffix));
			} else if (item instanceof Iterator<?>) {
				sb.append(join((Iterator<?>) item, conjunction, prefix, suffix));
			} else {
				sb.append(StrUtil.wrap(String.valueOf(item), prefix, suffix));
			}
		}
		return sb.toString();
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
		final List<E> list = new ArrayList<>();
		while (iter.hasNext()) {
			list.add(iter.next());
		}
		return list;
	}

	/**
	 * Enumeration转换为Iterator
	 * <p>
	 * Adapt the specified <code>Enumeration</code> to the <code>Iterator</code> interface
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
	 * 获取集合的第一个元素
	 *
	 * @param <T>      集合元素类型
	 * @param iterable {@link Iterable}
	 * @return 第一个元素
	 */
	public static <T> T getFirst(Iterable<T> iterable) {
		if (null == iterable) {
			return null;
		}
		return getFirst(iterable.iterator());
	}

	/**
	 * 获取集合的第一个元素
	 *
	 * @param <T>      集合元素类型
	 * @param iterator {@link Iterator}
	 * @return 第一个元素
	 */
	public static <T> T getFirst(Iterator<T> iterator) {
		if (null != iterator && iterator.hasNext()) {
			return iterator.next();
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
		if (null != iterable) {
			final Iterator<?> iterator = iterable.iterator();
			return getElementType(iterator);
		}
		return null;
	}

	/**
	 * 获得{@link Iterator}对象的元素类型（通过第一个非空元素判断）<br>
	 * 注意，此方法至少会调用多次next方法
	 *
	 * @param iterator {@link Iterator}
	 * @return 元素类型，当列表为空或元素全部为null时，返回null
	 */
	public static Class<?> getElementType(Iterator<?> iterator) {
		final Iterator<?> iter2 = new CopiedIter<>(iterator);
		if (iter2.hasNext()) {
			final Object t = iter2.next();
			if (null != t) {
				return t.getClass();
			}
		}
		return null;
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
	 * @param filter 过滤器接口
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
}
