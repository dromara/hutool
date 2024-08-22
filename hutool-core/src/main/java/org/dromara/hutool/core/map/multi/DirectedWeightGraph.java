/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.map.multi;

import org.dromara.hutool.core.exception.HutoolException;

import java.util.*;

/**
 * 权重有向图
 * 基于 SPFA 算法实现 可以处理负边 可以进行负权环路检查
 *
 * @param <T> 点对象类型
 * @author NewshiJ
 */
public class DirectedWeightGraph<T> {

	// 全部节点
	private final Set<T> allPoints = new HashSet<>();

	// 邻接边
	private final Map<T, Map<T, Edge<T>>> neighborEdgeMap = new HashMap<>();

	/**
	 * 获取全部点
	 *
	 * @return 全部点
	 */
	public Set<T> getAllPoints() {
		return allPoints;
	}

	/**
	 * 添加边
	 *
	 * @param fromPoint 开始点
	 * @param nextPoint 结束点
	 * @param weight    权重
	 */
	public void putEdge(final T fromPoint, final T nextPoint, final long weight) {
		allPoints.add(fromPoint);
		allPoints.add(nextPoint);
		final Map<T, Edge<T>> nextPointMap = neighborEdgeMap.computeIfAbsent(fromPoint, k -> new HashMap<>());
		nextPointMap.put(nextPoint, new Edge<>(fromPoint, nextPoint, weight));
	}

	/**
	 * 删除边
	 *
	 * @param fromPoint 开始点
	 * @param nextPoint 结束点
	 */
	public void removeEdge(final T fromPoint, final T nextPoint) {
		final Map<T, Edge<T>> nextPointMap = neighborEdgeMap.computeIfAbsent(fromPoint, k -> new HashMap<>());
		nextPointMap.remove(nextPoint);

		// 重新计算 所有点位
		allPoints.clear();
		neighborEdgeMap.forEach((f, m) -> {
			allPoints.add(f);
			m.forEach((t, e) -> {
				allPoints.add(t);
			});
		});
	}

	/**
	 * 删除点
	 *
	 * @param point 点
	 */
	public void removePoint(final T point) {
		allPoints.remove(point);
		neighborEdgeMap.remove(point);
		neighborEdgeMap.forEach((f, m) -> {
			m.remove(point);
		});
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		neighborEdgeMap.forEach((from, edgeMap) -> {
			edgeMap.forEach((to, edge) -> {
				builder.append(edge);
				builder.append("\r\n");
			});
		});
		return builder.toString();
	}


	/**
	 * 计算 从 startPoint 到 所有点 最短路径
	 * 基于 SPFA 算法实现
	 *
	 * @param startPoint 开始节点
	 * @return 最佳路径集合 如果无可触达顶点 返回空 map
	 * @throws NegativeRingException 存在负权环路
	 */
	public Map<T, Path<T>> bestPathMap(final T startPoint) throws NegativeRingException {
		// 全部节点数量
		//final int pointSize = allPoints.size();
		// 待访问队列
		final LinkedList<T> pointQueue = new LinkedList<>();
		// 待访问队列中的节点 加速判断
		final HashSet<T> inQueuePoints = new HashSet<>();
		// 最佳路径集合
		final HashMap<T, Path<T>> bestPathMap = new HashMap<>();


		final Map<T, Edge<T>> map = neighborEdgeMap.get(startPoint);
		// 无可触达路径
		if (map == null || map.isEmpty()) {
			return new HashMap<>();
		}

		map.forEach((to, edge) -> {
			final Path<T> path = new Path<>(edge);
			bestPathMap.put(to, path);
			pointQueue.add(to);
			inQueuePoints.add(to);
		});


		while (!pointQueue.isEmpty()) {
			// 当前节点 开始对 currentPoint 进行扩展
			final T currentPoint = pointQueue.removeFirst();
			// 到当前节点的最短路径
			final Path<T> currentPath = bestPathMap.get(currentPoint);
			// 标记已出队列
			inQueuePoints.remove(currentPoint);

			final Map<T, Edge<T>> edgeMap = neighborEdgeMap.get(currentPoint);
			if (edgeMap == null) {
				continue;
			}

			// 扩展当前点的边
			final Set<Map.Entry<T, Edge<T>>> entrySet = edgeMap.entrySet();
			for (final Map.Entry<T, Edge<T>> entry : entrySet) {
				final T nextPoint = entry.getKey();
				final Edge<T> edge = entry.getValue();

				// 不存在路径 第一次访问 将当前路径放置到 bestPathMap 中
				final Path<T> oldPath = bestPathMap.get(nextPoint);
				if (oldPath == null) {
					final Path<T> nextPath = currentPath.nextPoint(edge);
					bestPathMap.put(nextPoint, nextPath);

					// 不在队列里就入队
					if (!inQueuePoints.contains(nextPoint)) {
						inQueuePoints.add(nextPoint);

						// SLF优化 入队优化
						// 每次出队进行判断扩展出的点与队头元素进行判断，若小于进队头，否则入队尾
						// 尽可能的让 负环路 上的节点 先进入队列头
						if (pointQueue.isEmpty()) {
							pointQueue.addLast(nextPoint);
							continue;
						}
						final T first = pointQueue.getFirst();
						final Path<T> fristPath = bestPathMap.get(first);
						if (nextPath.weight < fristPath.weight) {
							pointQueue.addFirst(nextPoint);
						} else {
							pointQueue.add(nextPoint);
						}
					}
					continue;
				}

				final long newWeight = currentPath.weight + edge.weight;
				// 新路径更糟糕 没有优化的必要
				if (newWeight >= oldPath.weight) {
					continue;
				}

				// 更新最佳路径 如果下一跳没有在队列中 将下一跳放到队列里
				final Path<T> nextPath = currentPath.nextPoint(edge);
				bestPathMap.put(nextPoint, nextPath);
				// 不在队列里就入队
				if (!inQueuePoints.contains(nextPoint)) {
					inQueuePoints.add(nextPoint);

					// SLF优化 入队优化
					// 每次出队进行判断扩展出的点与队头元素进行判断，若小于进队头，否则入队尾
					// 尽可能的让 负环路 上的节点 先进入队列头
					if (pointQueue.isEmpty()) {
						pointQueue.addLast(nextPoint);
						continue;
					}
					final T first = pointQueue.getFirst();
					final Path<T> fristPath = bestPathMap.get(first);
					if (nextPath.weight < fristPath.weight) {
						pointQueue.addFirst(nextPoint);
					} else {
						pointQueue.addLast(nextPoint);
					}
				}
			}
		}
		return bestPathMap;
	}


	/**
	 * 边
	 *
	 * @param <T> 点类型
	 */
	public static class Edge<T> {
		// 起始点
		protected T fromPoint;
		// 目标点
		protected T toPoint;
		// 权重
		protected long weight;

		/**
		 * 构造
		 */
		public Edge() {
		}

		/**
		 * 构造
		 *
		 * @param fromPoint 起始点
		 * @param toPoint   目标点
		 * @param weight    权重
		 */
		public Edge(final T fromPoint, final T toPoint, final long weight) {
			this.fromPoint = fromPoint;
			this.toPoint = toPoint;
			this.weight = weight;
		}

		@Override
		public String toString() {
			return fromPoint + "->" + toPoint + "(" + weight + ")";
		}
	}

	/**
	 * 路径
	 *
	 * @param <T> 点类型
	 */
	public static class Path<T> extends Edge<T> {

		/**
		 * 道路 即依次按照顺序经过的边
		 */
		private final LinkedList<Edge<T>> way = new LinkedList<>();
		/**
		 * 已经经过的点 如果 有一个点已经多次经过了 可以判定已经成环
		 * 当源图是一个非联通图时 或者 开始节点处于图路径中下游时 或者 成环路经过的节点数量较少时
		 * 使用判断节点经过次数与全部节点数量进行比较会有冗余判断
		 * 用成环判断 可以加速这种情况 是针对一些特殊的图结构优化了最差情况
		 */
		private final Set<T> passedPoints = new HashSet<>();

		/**
		 * 构造
		 */
		public Path() {
			super();
		}

		/**
		 * 构造
		 *
		 * @param edge 边
		 */
		public Path(final Edge<T> edge) {
			this.fromPoint = edge.fromPoint;
			this.toPoint = edge.toPoint;
			way.add(edge);
			weight = edge.weight;
			passedPoints.add(edge.fromPoint);
			passedPoints.add(edge.toPoint);
		}

		/**
		 * 生成下一跳
		 *
		 * @param edge 边
		 * @return 下一跳
		 * @throws NegativeRingException 负环路
		 */
		public Path<T> nextPoint(final Edge<T> edge) throws NegativeRingException {
			final Path<T> nextPath = new Path<>();
			nextPath.fromPoint = fromPoint;
			nextPath.toPoint = edge.toPoint;
			nextPath.way.addAll(way);
			nextPath.way.add(edge);
			nextPath.weight = weight + edge.weight;
			nextPath.passedPoints.addAll(passedPoints);

			// 负环检查
			if (nextPath.passedPoints.contains(edge.toPoint)) {
				throw new NegativeRingException("路径:" + nextPath + "存在负环路");
			}

			nextPath.passedPoints.add(edge.toPoint);
			return nextPath;
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			builder.append(String.format("[%s->%s(%d)]  ", fromPoint, toPoint, weight));
			for (final Edge<T> edge : way) {
				builder.append(edge);
				builder.append(" ");
			}
			return builder.toString();
		}
	}

	/**
	 * 负环异常
	 */
	public static class NegativeRingException extends HutoolException {
		private static final long serialVersionUID = 1L;

		/**
		 * 构造
		 *
		 * @param msg 消息
		 */
		public NegativeRingException(final String msg) {
			super(msg);
		}
	}
}



