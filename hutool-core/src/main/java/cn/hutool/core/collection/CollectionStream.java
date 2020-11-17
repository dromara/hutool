package cn.hutool.core.collection;


import cn.hutool.core.map.MapUtil;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 常用stream操作集合<br></br>
 * 做这个集合的封装的原因在于list.stream后续的写法虽然简便，但是别人看不易不懂，甚至自己写了两周后都看不懂,维护成本高<br></br>
 * 于是笔者采用见名知意的方法封装，降低阅读难度，减轻了维护成本，使用者只需要提供一些简单的lambda表达式<br></br>
 * 当然本人才疏学浅，名字未必能够做到信雅达，欢迎各位大佬指教<br></br>
 * 另外，CollectionStream封装的都是一些常用的stream方法，当然考虑到一些方法不常用(比如map->list)，并未提交，如果有一些好的意见，欢迎联系本人528910437@QQ.COM
 *
 * @version 1.0
 */
public class CollectionStream {
    /**
     * 将collection转化为类型不变的map
     * <br></br>
     * <B>{@code Collection<E>  ---->  Map<T,E>}</B>
     *
     * @param collection 需要转化的集合
     * @param key        E类型转化为T类型的lambda方法
     * @param <E>        collection中的泛型
     * @param <T>        map中的key类型
     * @return 转化后的map
     */
    public static <E, T> Map<T, E> toIdentityMap(Collection<E> collection, Function<E, T> key) {
        if (CollUtil.isEmpty(collection)) {
            return Collections.EMPTY_MAP;
        }
        return collection.stream().collect(Collectors.toMap(key, Function.identity()));
    }

    /**
     * E
     * 将collection转化为map(value类型与collection的泛型不同)
     * <br></br>
     * <B>{@code Collection<E> -----> Map<T,U>  }</B>
     *
     * @param collection 需要转化的集合
     * @param key        E类型转化为T类型的lambda方法
     * @param value      E类型转化为U类型的lambda方法
     * @param <E>        collection中的泛型
     * @param <T>        map中的key类型
     * @param <U>        map中的value类型
     * @return 转化后的map
     */
    public static <E, T, U> Map<T, U> toMap(Collection<E> collection, Function<E, T> key, Function<E, U> value) {
        if (CollUtil.isEmpty(collection)) {
            return Collections.EMPTY_MAP;
        }
        return collection.stream().collect(Collectors.toMap(key, value));
    }

    /**
     * 将collection按照规则(比如有相同的班级id)分类成map
     * <br></br>
     * <B>{@code Collection<E> -------> Map<T,List<E>> } </B>
     *
     * @param collection 需要分类的集合
     * @param key        分类的规则
     * @param <E>        collection中的泛型
     * @param <T>        map中的key类型
     * @return 分类后的map
     */
    public static <E, T> Map<T, List<E>> groupByKey(Collection<E> collection, Function<E, T> key) {
        if (CollUtil.isEmpty(collection)) {
            return Collections.EMPTY_MAP;
        }
        return collection
                .stream()
                .collect(Collectors.groupingBy(key, Collectors.toList()));
    }

    /**
     * 将collection按照两个规则(比如有相同的年级id,班级id)分类成双层map
     * <br></br>
     * <B>{@code Collection<E>  --->  Map<T,Map<U,List<E>>> } </B>
     *
     * @param collection 需要分类的集合
     * @param key1       第一个分类的规则
     * @param key2       第二个分类的规则
     * @param <E>        第一个map中的key类型
     * @param <T>        第二个map中的key类型
     * @return 分类后的map
     */
    public static <E, T, U> Map<T, Map<U, List<E>>> groupBy2Key(Collection<E> collection, Function<E, T> key1, Function<E, U> key2) {
        if (CollUtil.isEmpty(collection)) {
            return Collections.EMPTY_MAP;
        }
        return collection
                .stream()
                .collect(Collectors.groupingBy(key1, Collectors.groupingBy(key2, Collectors.toList())));
    }

    /**
     * 将collection按照两个规则(比如有相同的年级id,班级id)分类成双层map
     * <br></br>
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
            return Collections.EMPTY_MAP;
        }
        return collection
                .stream()
                .collect(Collectors.groupingBy(key1, Collectors.toMap(key2, Function.identity())));
    }

    /**
     * 将collection转化为List集合，但是两者的泛型不同
     * <br></br>
     * <B>{@code Collection<E>  ------>  List<T> } </B>
     *
     * @param collection 需要转化的集合
     * @param function   collection中的泛型转化为list泛型的lambda表达式
     * @param <E>        collection中的泛型
     * @param <T>        List中的泛型
     * @return 转化后的list
     */
    public static <E, T> List<T> translate2List(Collection<E> collection, Function<E, T> function) {
        if (CollUtil.isEmpty(collection)) {
            return Collections.EMPTY_LIST;
        }
        return collection
                .stream()
                .map(function)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 将collection转化为Set集合，但是两者的泛型不同
     * <br></br>
     * <B>{@code Collection<E>  ------>  Set<T> } </B>
     *
     * @param collection 需要转化的集合
     * @param function   collection中的泛型转化为set泛型的lambda表达式
     * @param <E>        collection中的泛型
     * @param <T>        Set中的泛型
     * @return 转化后的Set
     */
    public static <E, T> Set<T> translate2Set(Collection<E> collection, Function<E, T> function) {
        if (CollUtil.isEmpty(collection) || function == null) {
            return Collections.EMPTY_SET;
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
     * @param <T>   map中的key类型
     * @param <X>   第一个 map的value类型
     * @param <Y>   第二个 map的value类型
     * @param <Z>   最终map的value类型
     * @return 合并后的map
     */
    public static <T, X, Y, Z> Map<T, Z> merge(Map<T, X> map1, Map<T, Y> map2, BiFunction<X, Y, Z> merge) {
        if (MapUtil.isEmpty(map1) && MapUtil.isEmpty(map2)) {
            return Collections.EMPTY_MAP;
        } else if (MapUtil.isEmpty(map1)) {
            map1 = Collections.EMPTY_MAP;
        } else if (MapUtil.isEmpty(map2)) {
            map2 = Collections.EMPTY_MAP;
        }
        Set<T> key = new HashSet<>();
        key.addAll(map1.keySet());
        key.addAll(map2.keySet());
        Map<T, Z> map = new HashMap<>();
        for (T t : key) {
            X x = map1.get(t);
            Y y = map2.get(t);
            Z z = merge.apply(x, y);
            if (z != null) {
                map.put(t, z);
            }
        }
        return map;
    }
}
