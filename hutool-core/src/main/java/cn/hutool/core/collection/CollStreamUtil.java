package cn.hutool.core.collection;


import cn.hutool.core.map.MapUtil;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 集合的stream操作封装
 *
 * @author 528910437@QQ.COM
 * @since 5.5.2
 */
public class CollStreamUtil {

	/**
	 * 将collection转化为类型不变的map<br>
	 * <B>{@code Collection<E>  ---->  Map<T,E>}</B>
	 *
	 * @param collection 需要转化的集合
	 * @param key        E类型转化为T类型的lambda方法
	 * @param <V>        collection中的泛型
	 * @param <K>        map中的key类型
	 * @return 转化后的map
	 */
	public static <V, K> Map<K, V> toIdentityMap(Collection<V> collection, Function<V, K> key) {
		if (CollUtil.isEmpty(collection)) {
			return Collections.emptyMap();
		}
		return collection.stream().collect(Collectors.toMap(key, Function.identity()));
	}

	/**
	 * 将Collection转化为map(value类型与collection的泛型不同)<br>
	 * <B>{@code Collection<E> -----> Map<T,U>  }</B>
	 *
	 * @param collection 需要转化的集合
	 * @param key        E类型转化为T类型的lambda方法
	 * @param value      E类型转化为U类型的lambda方法
	 * @param <E>        collection中的泛型
	 * @param <K>        map中的key类型
	 * @param <V>        map中的value类型
	 * @return 转化后的map
	 */
	public static <E, K, V> Map<K, V> toMap(Collection<E> collection, Function<E, K> key, Function<E, V> value) {
		if (CollUtil.isEmpty(collection)) {
			return Collections.emptyMap();
		}
		return collection.stream().collect(Collectors.toMap(key, value));
	}

	/**
	 * 将collection按照规则(比如有相同的班级id)分类成map<br>
	 * <B>{@code Collection<E> -------> Map<T,List<E>> } </B>
	 *
	 * @param collection 需要分类的集合
	 * @param key        分类的规则
	 * @param <E>        collection中的泛型
	 * @param <K>        map中的key类型
	 * @return 分类后的map
	 */
	public static <E, K> Map<K, List<E>> groupByKey(Collection<E> collection, Function<E, K> key) {
		if (CollUtil.isEmpty(collection)) {
			return Collections.emptyMap();
		}
		return collection
				.stream()
				.collect(Collectors.groupingBy(key, Collectors.toList()));
	}

	/**
	 * 将collection按照两个规则(比如有相同的年级id,班级id)分类成双层map<br>
	 * <B>{@code Collection<E>  --->  Map<T,Map<U,List<E>>> } </B>
	 *
	 * @param collection 需要分类的集合
	 * @param key1       第一个分类的规则
	 * @param key2       第二个分类的规则
	 * @param <E>        集合元素类型
	 * @param <K>        第一个map中的key类型
	 * @param <U>        第二个map中的key类型
	 * @return 分类后的map
	 */
	public static <E, K, U> Map<K, Map<U, List<E>>> groupBy2Key(Collection<E> collection, Function<E, K> key1, Function<E, U> key2) {
		if (CollUtil.isEmpty(collection)) {
			return Collections.emptyMap();
		}
		return collection
				.stream()
				.collect(Collectors.groupingBy(key1, Collectors.groupingBy(key2, Collectors.toList())));
	}

	/**
	 * 将collection按照两个规则(比如有相同的年级id,班级id)分类成双层map<br>
	 * <B>{@code Collection<E>  --->  Map<T,Map<U,E>> } </B>
	 *
	 * @param collection 需要分类的集合
	 * @param key1       第一个分类的规则
	 * @param key2       第二个分类的规则
	 * @param <T>        第一个map中的key类型
	 * @param <U>        第二个map中的key类型
	 * @param <E>        collection中的泛型
	 * @return 分类后的map
	 */
	public static <E, T, U> Map<T, Map<U, E>> group2Map(Collection<E> collection, Function<E, T> key1, Function<E, U> key2) {
		if (CollUtil.isEmpty(collection) || key1 == null || key2 == null) {
			return Collections.emptyMap();
		}
		return collection
				.stream()
				.collect(Collectors.groupingBy(key1, Collectors.toMap(key2, Function.identity())));
	}

	/**
	 * 将collection转化为List集合，但是两者的泛型不同<br>
	 * <B>{@code Collection<E>  ------>  List<T> } </B>
	 *
	 * @param collection 需要转化的集合
	 * @param function   collection中的泛型转化为list泛型的lambda表达式
	 * @param <E>        collection中的泛型
	 * @param <T>        List中的泛型
	 * @return 转化后的list
	 */
	public static <E, T> List<T> toList(Collection<E> collection, Function<E, T> function) {
		if (CollUtil.isEmpty(collection)) {
			return Collections.emptyList();
		}
		return collection
				.stream()
				.map(function)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * 将collection转化为Set集合，但是两者的泛型不同<br>
	 * <B>{@code Collection<E>  ------>  Set<T> } </B>
	 *
	 * @param collection 需要转化的集合
	 * @param function   collection中的泛型转化为set泛型的lambda表达式
	 * @param <E>        collection中的泛型
	 * @param <T>        Set中的泛型
	 * @return 转化后的Set
	 */
	public static <E, T> Set<T> toSet(Collection<E> collection, Function<E, T> function) {
		if (CollUtil.isEmpty(collection) || function == null) {
			return Collections.emptySet();
		}
		return collection
				.stream()
				.map(function)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}


	/**
	 * 合并两个相同key类型的map
	 *
	 * @param map1  第一个需要合并的 map
	 * @param map2  第二个需要合并的 map
	 * @param merge 合并的lambda，将key  value1 value2合并成最终的类型,注意value可能为空的情况
	 * @param <K>   map中的key类型
	 * @param <X>   第一个 map的value类型
	 * @param <Y>   第二个 map的value类型
	 * @param <V>   最终map的value类型
	 * @return 合并后的map
	 */
	public static <K, X, Y, V> Map<K, V> merge(Map<K, X> map1, Map<K, Y> map2, BiFunction<X, Y, V> merge) {
		if (MapUtil.isEmpty(map1) && MapUtil.isEmpty(map2)) {
			return Collections.emptyMap();
		} else if (MapUtil.isEmpty(map1)) {
			map1 = Collections.emptyMap();
		} else if (MapUtil.isEmpty(map2)) {
			map2 = Collections.emptyMap();
		}
		Set<K> key = new HashSet<>();
		key.addAll(map1.keySet());
		key.addAll(map2.keySet());
		Map<K, V> map = new HashMap<>();
		for (K t : key) {
			X x = map1.get(t);
			Y y = map2.get(t);
			V z = merge.apply(x, y);
			if (z != null) {
				map.put(t, z);
			}
		}
		return map;
	}
}
