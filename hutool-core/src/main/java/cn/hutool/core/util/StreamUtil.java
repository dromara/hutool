package cn.hutool.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * java Stream工具类
 * <pre>
 *  在实际项目中，我们经常需要处理集合对象, 提取集合元素的某个字段集合。
 *  此工具类可以简化一些代码, 如
 *  原来需要提取User对象的id集合：
 *      1 - 写一个for循环
 *          {@code
 *          List<Long> userIds = new ArrayList<>();
 *          for(User user : userList) {
 *              userIds.add(user.getId());
 *          }
 *          }
 *      2 - 使用java8 引入的stream方式
 *          {@code List<Long> userIds = userList.stream().map(User::getId).collect(Collectors.toList());}
 *
 *      3 - 此工具类提供了更简洁的stream写法(无需每次都写{@code collect(Collectors.toList())})
 *          {@code List<Long> userId = StreamUtil.list(userList, User::getId);}
 * </pre>
 *
 * @author yiming
 */
public class StreamUtil {

    /**
     * 将指定List元素的某个field提取成新的List(过滤null元素)
     *
     * @param sourceList 原list
     * @param mapperFunction 映射方法
     * @return 转化后的list
     */
    public static <T, R> List<R> list(List<T> sourceList, Function<T, R> mapperFunction) {
        if (CollUtil.isEmpty(sourceList)) {
            return Collections.emptyList();
        }
        Assert.notNull(mapperFunction);
        return sourceList.stream().map(mapperFunction).filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    /**
     * 获取对象列表的某个字段list
     *
     * @param sourceList 源数据
     * @param mapperFunction 映射方法
     * @param filter 过滤器
     * @param <T> 源数据类型
     * @param <R> 结果数据类型
     * @return 转化后的列表数据
     */
    public static <T, R> List<R> list(List<T> sourceList, Function<T, R> mapperFunction, Predicate<? super T> filter) {
        if (CollUtil.isEmpty(sourceList)) {
            return Collections.emptyList();
        }
        Assert.notNull(mapperFunction);
        Assert.notNull(filter);
        return sourceList.stream().filter(filter).map(mapperFunction).distinct().collect(Collectors.toList());
    }

    /**
     * 将指定List元素的某个field提取成新的Set(过滤null元素)
     *
     * @param sourceList 原list
     * @param mapperFunction 映射方法
     * @return 转化后的set
     */
    public static <T, R> Set<R> toSet(List<T> sourceList, Function<T, R> mapperFunction) {
        return new HashSet<>(list(sourceList, mapperFunction));
    }

    /**
     * 将指定List元素的某个field提取成新的Set(过滤null元素)
     *
     * @param sourceList 原list
     * @param mapperFunction 映射方法
     * @param filter 过滤器
     * @return 转化后的set
     */
    public static <T, R> Set<R> toSet(List<T> sourceList, Function<T, R> mapperFunction, Predicate<T> filter) {
        return new HashSet<>(list(sourceList, mapperFunction, filter));
    }

    /**
     * list 转 map
     *
     * @param sourceList 源数据
     * @param keyMapper key映射
     * @param valueMapper value映射
     * @param mergeFunction value合并策略
     * @param <T> 对象集合类型
     * @param <K> map键类型
     * @param <R> map值类型
     * @return 由list转化而的map
     */
    public static <T, K, R> Map<K, R> toMap(List<T> sourceList, Function<T, K> keyMapper, Function<T, R> valueMapper,
        BinaryOperator<R> mergeFunction) {
        if (CollUtil.isEmpty(sourceList) || keyMapper == null || valueMapper == null) {
            return Collections.emptyMap();
        }
        return sourceList.stream().collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction));
    }

    /**
     * 提取list指定字段转成map
     *
     * @param sourceList 原list
     * @param keyMapper key映射方法
     * @param valueMapper value映射方法
     * @return 指定map
     */
    public static <T, K, R> Map<K, R> toMap(List<T> sourceList, Function<T, K> keyMapper, Function<T, R> valueMapper) {
        if (CollUtil.isEmpty(sourceList) || keyMapper == null || valueMapper == null) {
            return Collections.emptyMap();
        }
        return sourceList.stream().collect(Collectors.toMap(keyMapper, valueMapper, (v1, v2) -> v1));
    }

    /**
     * 提取list指定字段转成map
     *
     * @param sourceList 原list
     * @param keyMapper key映射方法
     * @param valueMapper value映射方法
     * @param filter 过滤器
     * @return 指定map
     */
    public static <T, K, R> Map<K, R> toMap(List<T> sourceList, Function<T, K> keyMapper, Function<T, R> valueMapper,
        Predicate<? super T> filter) {
        if (CollUtil.isEmpty(sourceList) || keyMapper == null || valueMapper == null) {
            return Collections.emptyMap();
        }
        return sourceList.stream().filter(filter).collect(Collectors.toMap(keyMapper, valueMapper, (v1, v2) -> v1));
    }

    /**
     * list -> map (map值类型同list元素类型)
     *
     * @param sourceList 源数据
     * @param keyMapper key映射
     * @param <T> 对象集合类型
     * @param <K> map键类型
     * @return 由list转化而来的map
     */
    public static <T, K> Map<K, T> toMap(List<T> sourceList, Function<T, K> keyMapper) {
        return toMap(sourceList, keyMapper, Function.identity(), (v1, v2) -> v1);
    }

    /**
     * 按指定字段分组
     *
     * @param sourceList 原list
     * @param keyMapper 字段映射
     * @return 分组
     */
    public static <T, K> Map<K, List<T>> groupingBy(List<T> sourceList, Function<T, K> keyMapper) {
        if (CollUtil.isEmpty(sourceList) || keyMapper == null) {
            return Collections.emptyMap();
        }
        return sourceList.stream().collect(Collectors.groupingBy(keyMapper));
    }
}
