package cn.hutool.core.collection;

import cn.hutool.core.util.ArrayUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Set工具
 *
 * @author daidai21
 */
public class SetUtil {
	/**
	 * 数组转为一个Set
	 *
	 * @param ts
	 * @param <T>
	 * @return
	 */
	public static <T> Set<T> of(T... ts) {
		if (ArrayUtil.isEmpty(ts)) {
			return Collections.emptySet();
		}
		return new HashSet<>(Arrays.asList(ts));
	}

	/**
	 * 计算两个集合的并集，不修改原集合
	 *
	 * @param list1
	 * @param list2
	 * @param <T>
	 * @return
	 * @since 5.6.3
	 */
	public static <T> Set<T> concat(Set<T> list1, Set<T> list2) {
		return Stream.concat(list1.stream(), list2.stream()).collect(Collectors.toSet());
	}

	/**
	 * 计算两个集合的交集，不修改原集合
	 *
	 * @param list1
	 * @param list2
	 * @param <T>
	 * @return
	 * @since 5.6.3
	 */
	public static <T> Set<T> intersect(Set<T> list1, Set<T> list2) {
		return list1.stream().filter(list2::contains).collect(Collectors.toSet());
	}
}
