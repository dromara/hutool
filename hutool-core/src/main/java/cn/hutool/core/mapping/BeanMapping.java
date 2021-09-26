package cn.hutool.core.mapping;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * JavaBean映射
 * @author XueRi
 * @since 1.0
 */
public class BeanMapping extends Mapping {

	/**
	 * 映射一个新的实例
	 * @param source 数据源
	 * @param target 目标
	 * @param <T>    数据源类型
	 * @param <R>    目标类型
	 * @return 返回 R 类型的实例
	 */
	public static <T, R> R toBean(T source, Class<R> target) {
		return toBean(source, target, null);
	}

	/**
	 * 映射一个新的实例
	 * @param source     数据源
	 * @param target     目标
	 * @param biConsumer 数据源字段与目标字段不同,可通过手动映射
	 * @param <T>        数据源类型
	 * @param <R>        目标类型
	 * @return 返回 R 类型的实例
	 */
	public static <T, R> R toBean(T source, Class<R> target, BiConsumer<T, R> biConsumer) {
		R instance = createInstance(source, target);
		if (!Objects.isNull(instance) && !Objects.isNull(biConsumer)) {
			biConsumer.accept(source, instance);
		}
		return instance;
	}

	/**
	 * 映射一个新的List
	 * @param source 数据源
	 * @param target 目标
	 * @param <T>    数据源类型
	 * @param <R>    目标类型
	 * @return 返回 R 类型的实例List集合
	 */
	public static <T, R> List<R> toList(Collection<T> source, Class<R> target) {
		return toList(source, target, null);
	}

	/**
	 * 映射一个新的List
	 * @param source     数据源
	 * @param target     目标
	 * @param biConsumer biConsumer 数据源字段与目标字段不同,可通过手动映射
	 * @param <T>        数据源类型
	 * @param <R>        目标类型
	 * @return 返回 R 类型的实例List集合
	 */
	public static <T, R> List<R> toList(Collection<T> source, Class<R> target, BiConsumer<T, R> biConsumer) {
		List<R> targetCollection = new ArrayList<>();
		getInstanceCollection(targetCollection, source, target, biConsumer);
		return targetCollection;
	}

	/**
	 * 映射一个新的范围List
	 * @param source 数据源
	 * @param target 目标
	 * @param skip   跳过
	 * @param <T>    数据源类型
	 * @param <R>    目标类型
	 * @return 返回 R 类型范围的实例List集合
	 */
	public static <T, R> List<R> toListRange(List<T> source, Class<R> target, int skip) {
		return toListRange(source, target, skip, source.size());
	}

	/**
	 * 映射一个新的范围List
	 * @param source     数据源
	 * @param target     目标
	 * @param skip       跳过
	 * @param biConsumer biConsumer 数据源字段与目标字段不同,可通过手动映射
	 * @param <T>        数据源类型
	 * @param <R>        目标类型
	 * @return 返回 R 类型范围的实例List集合
	 */
	public static <T, R> List<R> toListRange(List<T> source, Class<R> target, int skip, BiConsumer<T, R> biConsumer) {
		return toListRange(source, target, skip, source.size(), biConsumer);
	}

	/**
	 * 映射一个新的范围List
	 * @param source 数据源
	 * @param target 目标
	 * @param skip   跳过
	 * @param limit  截止
	 * @param <T>    数据源类型
	 * @param <R>    目标类型
	 * @return 返回 R 类型范围的实例List集合
	 */
	public static <T, R> List<R> toListRange(List<T> source, Class<R> target, int skip, int limit) {
		return toListRange(source, target, skip, limit, null);
	}

	/**
	 * 映射一个新的范围List
	 * @param source     数据源
	 * @param target     目标
	 * @param skip       跳过
	 * @param limit      截止(包含)
	 * @param biConsumer biConsumer 数据源字段与目标字段不同,可通过手动映射
	 * @param <T>        数据源类型
	 * @param <R>        目标类型
	 * @return 返回 R 类型范围的实例List集合
	 */
	public static <T, R> List<R> toListRange(List<T> source, Class<R> target, int skip, int limit, BiConsumer<T, R> biConsumer) {
		if (skip > source.size() || limit < skip || limit < 0 || skip == limit) {
			return Collections.emptyList();
		}
		List<R> targetCollection = new ArrayList<>();
		int end = Math.min(limit, source.size());
		getInstanceCollection(targetCollection, source.subList(Math.max(skip, 0), end), target, biConsumer);
		toList(source, target, null);
		return targetCollection;
	}

	/**
	 * 映射一个新的Set
	 * @param source 数据源
	 * @param target 目标
	 * @param <T>    数据源类型
	 * @param <R>    目标类型
	 * @return 返回 R 类型的实例Set集合
	 */
	public static <T, R> Set<R> toSet(Collection<T> source, Class<R> target) {
		return toSet(source, target, null);
	}

	/**
	 * 映射一个新的Set
	 * @param source     数据源
	 * @param target     目标
	 * @param biConsumer biConsumer 数据源字段与目标字段不同,可通过手动映射
	 * @param <T>        数据源类型
	 * @param <R>        目标类型
	 * @return 返回 R 类型的实例Set集合
	 */
	public static <T, R> Set<R> toSet(Collection<T> source, Class<R> target, BiConsumer<T, R> biConsumer) {
		Set<R> targetCollection = new HashSet<>();
		getInstanceCollection(targetCollection, source, target, biConsumer);
		return targetCollection;
	}
}
