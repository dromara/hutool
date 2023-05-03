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

package org.dromara.hutool.core.tree.hierarchy;

import org.dromara.hutool.core.lang.mutable.Mutable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * {@link HierarchyIterator}创建工具类
 *
 * @author huangchengxing
 * @since 6.0.0
 */
public class HierarchyIteratorUtil {
	/**
	 * 创建一个{@code HierarchyIterator}对象, 当{@code finder}返回非空时, 迭代器立刻中断, 返回结果
	 *
	 * @param function 迭代器处理函数
	 * @param finder   查找器
	 * @param <H>      层级结构类型
	 * @param <R>      迭代器结果类型
	 * @return {@code HierarchyIterator}
	 */
	public static <H, R> HierarchyIterator<H, R> find(
		final Function<H, Collection<H>> function, final Function<H, R> finder) {
		Objects.requireNonNull(function);
		Objects.requireNonNull(finder);
		final Mutable<R> mutable = Mutable.of(null);
		final Predicate<H> terminator = h -> {
			final R r = finder.apply(h);
			if (r != null) {
				mutable.set(r);
				return true;
			}
			return false;
		};
		return new HierarchyIteratorImpl<>(mutable::get, terminator, (r, h) -> function.apply(h));
	}

	/**
	 * 创建一个{@code HierarchyIterator}对象, 迭代器结果总是为{@code null}
	 *
	 * @param function   迭代器处理函数
	 * @param terminator 是否终止遍历
	 * @param <H>        层级结构类型
	 * @return {@code HierarchyIterator}
	 */
	public static <H> HierarchyIterator<H, Void> scan(
		final Function<H, Collection<H>> function, final Predicate<H> terminator) {
		Objects.requireNonNull(function);
		return new HierarchyIteratorImpl<>(() -> null, terminator, (r, h) -> function.apply(h));
	}

	/**
	 * 创建一个{@code HierarchyIterator}对象, 迭代器结果总是为{@code null}
	 *
	 * @param function 迭代器处理函数
	 * @param <H>      层级结构类型
	 * @return {@code HierarchyIterator}
	 */
	public static <H> HierarchyIterator<H, Void> scan(final Function<H, Collection<H>> function) {
		return scan(function, h -> false);
	}

	/**
	 * 创建一个{@code HierarchyIterator}对象, 若{@code mapper}返回非空, 则将结果添加到集合中，最终返回集合
	 *
	 * @param function    迭代器处理函数
	 * @param collFactory 集合工厂
	 * @param mapper      迭代器结果映射函数
	 * @param <H>         层级结构类型
	 * @param <R>         迭代器结果类型
	 * @param <C>         集合类型
	 * @return {@code HierarchyIterator}
	 */
	public static <H, R, C extends Collection<R>> HierarchyIterator<H, C> collect(
		final Function<H, Collection<H>> function, final Supplier<C> collFactory, final Function<H, R> mapper) {
		Objects.requireNonNull(function);
		Objects.requireNonNull(collFactory);
		Objects.requireNonNull(mapper);
		final C collection = collFactory.get();
		return new HierarchyIteratorImpl<>(() -> collection, h -> false, (r, h) -> {
			final R apply = mapper.apply(h);
			if (Objects.nonNull(apply)) {
				collection.add(apply);
			}
			return function.apply(h);
		});
	}

	/**
	 * 创建一个{@code HierarchyIterator}对象, 若{@code mapper}返回非空, 则将结果添加到集合中，最终返回集合
	 *
	 * @param function 迭代器处理函数
	 * @param mapper   迭代器结果映射函数
	 * @param <H>      层级结构类型
	 * @param <R>      迭代器结果类型
	 * @return {@code HierarchyIterator}
	 */
	public static <H, R> HierarchyIterator<H, List<R>> collect(
		final Function<H, Collection<H>> function, final Function<H, R> mapper) {
		return collect(function, ArrayList::new, mapper);
	}

	/**
	 * 创建一个{@code HierarchyIterator}对象, 则将非空结果添加到集合中，最终返回集合
	 *
	 * @param function 迭代器处理函数
	 * @param <H>      层级结构类型
	 * @return {@code HierarchyIterator}
	 */
	public static <H> HierarchyIterator<H, List<H>> collect(
		final Function<H, Collection<H>> function) {
		return collect(function, Function.identity());
	}
}
