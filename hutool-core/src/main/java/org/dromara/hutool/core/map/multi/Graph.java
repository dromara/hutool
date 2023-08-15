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

package org.dromara.hutool.core.map.multi;

import org.dromara.hutool.core.collection.CollUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

/**
 * 支持处理无向图结构的{@link Map}，本质上是基于{@link SetValueMap}实现的邻接表
 *
 * @param <T> 节点类型
 * @author huangchengxing
 * @since 6.0.0
 */
public class Graph<T> extends SetValueMap<T, T> {
	private static final long serialVersionUID = 1L;

	/**
	 * 添加边
	 *
	 * @param target1 节点
	 * @param target2 节点
	 */
	public void putEdge(final T target1, final T target2) {
		this.putValue(target1, target2);
		this.putValue(target2, target1);
	}

	/**
	 * 是否存在边
	 *
	 * @param target1 节点
	 * @param target2 节点
	 * @return 是否
	 */
	public boolean containsEdge(final T target1, final T target2) {
		return this.getValues(target1).contains(target2)
			&& this.getValues(target2).contains(target1);
	}

	/**
	 * 移除边
	 *
	 * @param target1 节点
	 * @param target2 节点
	 */
	public void removeEdge(final T target1, final T target2) {
		this.removeValue(target1, target2);
		this.removeValue(target2, target1);
	}

	/**
	 * 移除节点，并删除该节点与其他节点之间连成的边
	 *
	 * @param target 目标对象
	 */
	public void removePoint(final T target) {
		final Collection<T> associatedPoints = this.remove(target);
		if (CollUtil.isNotEmpty(associatedPoints)) {
			associatedPoints.forEach(p -> this.removeValue(p, target));
		}
	}

	/**
	 * 两节点是否存在直接或间接的关联
	 *
	 * @param target1 节点
	 * @param target2 节点
	 * @return 两节点是否存在关联
	 */
	public boolean containsAssociation(final T target1, final T target2) {
		if (!this.containsKey(target1) || !this.containsKey(target2)) {
			return false;
		}
		final AtomicBoolean flag = new AtomicBoolean(false);
		visitAssociatedPoints(target1, t -> {
			if (Objects.equals(t, target2)) {
				flag.set(true);
				return true;
			}
			return false;
		});
		return flag.get();
	}

	/**
	 * 按广度优先，获得节点的所有直接或间接关联的节点，节点默认按添加顺序排序
	 *
	 * @param target        节点
	 * @param includeTarget 是否包含查询节点
	 * @return 节点的所有关联节点
	 */
	public Collection<T> getAssociatedPoints(final T target, final boolean includeTarget) {
		final Set<T> points = visitAssociatedPoints(target, t -> false);
		if (!includeTarget) {
			points.remove(target);
		}
		return points;
	}

	/**
	 * 获取节点的邻接节点
	 *
	 * @param target 节点
	 * @return 邻接节点
	 */
	public Collection<T> getAdjacentPoints(final T target) {
		return this.getValues(target);
	}

	/**
	 * 按广度优先，访问节点的所有关联节点
	 */
	private Set<T> visitAssociatedPoints(final T key, final Predicate<T> breaker) {
		if (!this.containsKey(key)) {
			return Collections.emptySet();
		}
		final Set<T> accessed = new HashSet<>();
		final Deque<T> deque = new LinkedList<>();
		deque.add(key);
		while (!deque.isEmpty()) {
			// 访问节点
			final T t = deque.removeFirst();
			if (accessed.contains(t)) {
				continue;
			}
			accessed.add(t);
			// 若符合条件则中断循环
			if (breaker.test(t)) {
				break;
			}
			// 获取邻接节点
			final Collection<T> neighbours = this.getValues(t);
			if (!neighbours.isEmpty()) {
				deque.addAll(neighbours);
			}
		}
		return accessed;
	}

}
