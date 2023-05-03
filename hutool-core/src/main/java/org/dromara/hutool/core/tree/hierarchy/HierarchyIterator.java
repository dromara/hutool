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

import java.util.Collection;

/**
 * 用于遍历层级结构的迭代器，提供了遍历过程中的回调方法：
 * <ul>
 *     <li>{@link #isBreak}: 是否中断遍历，其在{@link #nextHierarchies}之前调用；</li>
 *     <li>{@link #nextHierarchies}: 获得下一需要遍历的层级，当为空时结束；</li>
 *     <li>{@link #getResult}: 当遍历完成后，获取迭代器结果；</li>
 * </ul>
 *
 * <p>默认提供了三类{@code HierarchyIterator}实现：
 * <ul>
 *     <li>scan: 遍历所有的层级结构；</li>
 *     <li>collector: 收集层级结构中所有符合条件的结果，并在结束遍历后返回所有结果；</li>
 *     <li>find: 找到层级结构中符合条件的结果后立刻中断遍历，并返回该结果；</li>
 * </ul>
 * 可以实现自定义的{@code HierarchyIterator}来支持更多的遍历模式。
 *
 * @param <H> 层级类型
 * @param <R> 结果类型
 * @author huangchengxing
 * @since 6.0.0
 */
@FunctionalInterface
public interface HierarchyIterator<H, R> {

	/**
	 * 获取下一层级
	 *
	 * @param result    结果
	 * @param hierarchy 当前层级
	 * @return 向容器中添加元素的方法
	 */
	Collection<H> nextHierarchies(R result, H hierarchy);

	/**
	 * 是否中断遍历
	 *
	 * @param hierarchy 当前层级
	 * @return 是否中断遍历
	 */
	default boolean isBreak(final H hierarchy) {
		return false;
	}

	/**
	 * 获取结果
	 *
	 * @return 结果
	 */
	default R getResult() {
		return null;
	}
}
