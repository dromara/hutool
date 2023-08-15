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

package org.dromara.hutool.core.tree.hierarchy;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * {@code HierarchyIterator}的基本实现。
 *
 * @param <H> 层级类型
 * @param <R> 结果类型
 * @author huangchengxing
 */
public class HierarchyIteratorImpl<H, R> implements HierarchyIterator<H, R> {

	private final Supplier<? extends R> resultFactory;
	private final Predicate<? super H> hierarchyFilter;
	private final BiFunction<? super R, ? super H, ? extends Collection<H>> hierarchyFinder;

	/**
	 * 构造
	 *
	 * @param resultFactory   结果创建类
	 * @param hierarchyFilter 层级过滤器
	 * @param hierarchyFinder 层级查找器
	 */
	public HierarchyIteratorImpl(
		final Supplier<? extends R> resultFactory, final Predicate<? super H> hierarchyFilter,
		final BiFunction<? super R, ? super H, ? extends Collection<H>> hierarchyFinder) {
		this.resultFactory = resultFactory;
		this.hierarchyFilter = hierarchyFilter;
		this.hierarchyFinder = hierarchyFinder;
	}

	/**
	 * 获取下一层级
	 *
	 * @param result    结果
	 * @param hierarchy 当前层级
	 * @return 下一需要遍历的层级
	 */
	@Override
	public Collection<H> nextHierarchies(final R result, final H hierarchy) {
		return hierarchyFinder.apply(result, hierarchy);
	}

	/**
	 * 是否中断遍历
	 *
	 * @param hierarchy 当前层级
	 * @return 是否中断遍历
	 */
	@Override
	public boolean isBreak(final H hierarchy) {
		return hierarchyFilter.test(hierarchy);
	}

	/**
	 * 获取结果
	 *
	 * @return 结果
	 */
	@Override
	public R getResult() {
		return resultFactory.get();
	}
}
