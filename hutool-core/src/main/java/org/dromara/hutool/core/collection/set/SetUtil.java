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

package org.dromara.hutool.core.collection.set;

import org.dromara.hutool.core.array.ArrayUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 集合中的{@link java.util.Set}相关方法封装
 *
 * @author looly
 */
public class SetUtil {

	/**
	 * 新建一个List<br>
	 * 如果提供的初始化数组为空，新建默认初始长度的List
	 *
	 * @param <T>      集合元素类型
	 * @param isLinked 是否为链表
	 * @return List对象
	 */
	public static <T> HashSet<T> of(final boolean isLinked) {
		return _of(isLinked, null);
	}

	/**
	 * 新建一个HashSet
	 *
	 * @param <T> 集合元素类型
	 * @param ts  元素数组
	 * @return HashSet对象
	 */
	@SafeVarargs
	public static <T> HashSet<T> of(final T... ts) {
		return _of(false, ts);
	}

	/**
	 * 新建一个LinkedHashSet
	 *
	 * @param <T> 集合元素类型
	 * @param ts  元素数组
	 * @return HashSet对象
	 * @since 4.1.10
	 */
	@SafeVarargs
	public static <T> LinkedHashSet<T> ofLinked(final T... ts) {
		return (LinkedHashSet<T>) _of(true, ts);
	}

	/**
	 * 新建一个HashSet
	 *
	 * @param <T>      集合元素类型
	 * @param iterable 集合
	 * @return HashSet对象
	 */
	public static <T> HashSet<T> of(final Iterable<T> iterable) {
		return of(false, iterable);
	}

	/**
	 * 新建一个HashSet<br>
	 * 提供的参数为null时返回空{@link HashSet}
	 *
	 * @param <T>      集合元素类型
	 * @param isLinked 是否新建LinkedList
	 * @param iterable {@link Iterable}
	 * @return HashSet对象
	 */
	public static <T> HashSet<T> of(final boolean isLinked, final Iterable<T> iterable) {
		if (null == iterable) {
			return of(isLinked);
		}
		if (iterable instanceof Collection) {
			final Collection<T> collection = (Collection<T>) iterable;
			return isLinked ? new LinkedHashSet<>(collection) : new HashSet<>(collection);
		}
		return of(isLinked, iterable.iterator());
	}

	/**
	 * 新建一个HashSet
	 *
	 * @param <T>      集合元素类型
	 * @param isSorted 是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
	 * @param iter     {@link Iterator}
	 * @return HashSet对象
	 * @since 3.0.8
	 */
	public static <T> HashSet<T> of(final boolean isSorted, final Iterator<T> iter) {
		if (null == iter) {
			return _of(isSorted, null);
		}
		final HashSet<T> set = isSorted ? new LinkedHashSet<>() : new HashSet<>();
		while (iter.hasNext()) {
			set.add(iter.next());
		}
		return set;
	}

	/**
	 * 新建一个HashSet
	 *
	 * @param <T>         集合元素类型
	 * @param isLinked    是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
	 * @param enumeration {@link Enumeration}
	 * @return HashSet对象
	 * @since 3.0.8
	 */
	public static <T> HashSet<T> of(final boolean isLinked, final Enumeration<T> enumeration) {
		if (null == enumeration) {
			return _of(isLinked, null);
		}
		final HashSet<T> set = isLinked ? new LinkedHashSet<>() : new HashSet<>();
		while (enumeration.hasMoreElements()) {
			set.add(enumeration.nextElement());
		}
		return set;
	}

	/**
	 * 新建一个SetFromMap
	 *
	 * @param <T> 集合元素类型
	 * @param map Map
	 * @return SetFromMap对象
	 */
	public static <T> SetFromMap<T> of(final Map<T, Boolean> map) {
		return new SetFromMap<>(map);
	}

	/**
	 * 数组转为一个不可变List<br>
	 * 类似于Java9中的List.of
	 *
	 * @param ts  对象
	 * @param <T> 对象类型
	 * @return 不可修改List
	 */
	@SafeVarargs
	public static <T> Set<T> view(final T... ts) {
		return view(of(ts));
	}

	/**
	 * 转为一个不可变Set
	 *
	 * @param ts  对象
	 * @param <T> 对象类型
	 * @return 不可修改List，如果提供List为{@code null}或者空，返回{@link Collections#emptySet()}
	 */
	public static <T> Set<T> view(final Set<T> ts) {
		if (ArrayUtil.isEmpty(ts)) {
			return empty();
		}
		return Collections.unmodifiableSet(ts);
	}

	/**
	 * 获取一个空Set，这个空Set不可变
	 *
	 * @param <T> 元素类型
	 * @return 空的List
	 * @see Collections#emptySet()
	 */
	public static <T> Set<T> empty() {
		return Collections.emptySet();
	}

	/**
	 * 获取一个初始大小为0的Set，这个空Set可变
	 *
	 * @param <T> 元素类型
	 * @return 空的List
	 * @since 6.0.0
	 */
	public static <T> Set<T> zero() {
		return new HashSet<>(0, 1);
	}

	/**
	 * 获取一个初始大小为0的LinkedHashSet，这个空Set可变
	 *
	 * @param <T> 元素类型
	 * @return 空的List
	 * @since 6.0.0
	 */
	public static <T> Set<T> zeroLinked() {
		return new LinkedHashSet<>(0, 1);
	}

	/**
	 * 转为只读Set
	 *
	 * @param <T> 元素类型
	 * @param c   集合
	 * @return 只读集合
	 * @see Collections#unmodifiableSet(Set)
	 * @since 6.0.0
	 */
	public static <T> Set<T> unmodifiable(final Set<? extends T> c) {
		if (null == c) {
			return null;
		}
		return Collections.unmodifiableSet(c);
	}

	/**
	 * 新建一个HashSet
	 *
	 * @param <T>      集合元素类型
	 * @param isLinked 是否有序，有序返回 {@link LinkedHashSet}，否则返回 {@link HashSet}
	 * @param ts       元素数组
	 * @return HashSet对象
	 */
	private static <T> HashSet<T> _of(final boolean isLinked, final T[] ts) {
		if (ArrayUtil.isEmpty(ts)) {
			return isLinked ? new LinkedHashSet<>() : new HashSet<>();
		}
		final int initialCapacity = Math.max((int) (ts.length / .75f) + 1, 16);
		final HashSet<T> set = isLinked ? new LinkedHashSet<>(initialCapacity) : new HashSet<>(initialCapacity);
		Collections.addAll(set, ts);
		return set;
	}
}
