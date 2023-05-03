package org.dromara.hutool.core.lang.hierarchy;

import org.dromara.hutool.core.collection.CollUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * <p>通用层级结构扫描器工具类。<br>
 *
 * <p>用于根据广度优先或深度优先或搜索遍历如树或图等特定数据结构的层级，如：文件夹层级、类层级、方法或注解层级等。
 * 使用时指定遍历的起始节点，以及遍历算法，并配合{@link HierarchyIterator}即可完成遍历中的业务逻辑处理。
 *
 * <p>比如，我们可以使用其来访问树结构：
 * <pre>{@code
 * // 构建一个树形结构
 * Node root = Tree.build();
 * // 从树结构中通过深度优先查找某个节点
 * Node target = HierarchyUtil.traverseByDepthFirst(
 * 	root, HierarchyUtil.finder(Node::getChildren, node -> Objects.equals(node.getId(), "target") ? node : null)
 * );
 * // 从树结构中通过广度优先获取其所有的子节点
 * List<Node> nodes = HierarchyUtil.traverseByBreadthFirst(
 * 	root, HierarchyUtil.collector(Node::getChildren)
 * );
 * }</pre>
 *
 * @author huangchengxing
 * @see HierarchyIterator
 * @since 6.0.0
 */
public class HierarchyUtil {

	// ================ breadth first ================

	/**
	 * 按广度优先遍历指定包括{@code hierarchy}本身在内层级结构
	 *
	 * @param hierarchy 层级结构
	 * @param filter 过滤器，校验不通过的层级结构不会被查找
	 * @param hierarchyIterator 遍历过程中的迭代器, 若迭代器返回空, 则不会继续遍历下一层级
	 * @param <H> 层级结构类型
	 * @param <R> 迭代器结果类型
	 * @return 迭代器结果
	 */
	public static <H, R> R traverseByBreadthFirst(
		final H hierarchy, final Predicate<? super H> filter, final HierarchyIterator<H, R> hierarchyIterator) {
		return scanHierarchies(hierarchy, filter, hierarchyIterator, Collection::addAll);
	}

	/**
	 * 按广度优先遍历指定包括{@code hierarchy}本身在内层级结构
	 *
	 * @param hierarchy 层级结构
	 * @param hierarchyIterator 遍历过程中的迭代器, 若迭代器返回空, 则不会继续遍历下一层级
	 * @param <H> 层级结构类型
	 * @param <R> 迭代器结果类型
	 * @return 迭代器结果
	 */
	public static <H, R> R traverseByBreadthFirst(final H hierarchy, final HierarchyIterator<H, R> hierarchyIterator) {
		return traverseByBreadthFirst(hierarchy, h -> true, hierarchyIterator);
	}

	// ================ depth first ================

	/**
	 * 按深度优先遍历指定包括{@code hierarchy}本身在内层级结构
	 *
	 * @param hierarchy 层级结构
	 * @param filter 过滤器，校验不通过的层级结构不会被查找
	 * @param hierarchyIterator 遍历过程中的迭代器, 若迭代器返回空, 则不会继续遍历下一层级
	 * @param <H> 层级结构类型
	 * @param <R> 迭代器结果类型
	 * @return 迭代器结果
	 */
	public static <H, R> R traverseByDepthFirst(
		final H hierarchy, final Predicate<? super H> filter, final HierarchyIterator<H, R> hierarchyIterator) {
		return scanHierarchies(hierarchy, filter, hierarchyIterator, (queue, hierarchies) -> {
			// 跳跃式添加，保证顺序
			int index = 0;
			for (final H h : hierarchies) {
				queue.add(index++, h);
			}
		});
	}

	/**
	 * 按深度优先遍历指定包括{@code hierarchy}本身在内层级结构
	 *
	 * @param hierarchy 层级结构
	 * @param hierarchyIterator 遍历过程中的迭代器, 若迭代器返回空, 则不会继续遍历下一层级
	 * @param <H> 层级结构类型
	 * @param <R> 迭代器结果类型
	 * @return 迭代器结果
	 */
	public static <H, R> R traverseByDepthFirst(final H hierarchy, final HierarchyIterator<H, R> hierarchyIterator) {
		return traverseByDepthFirst(hierarchy, h -> true, hierarchyIterator);
	}

	private static <H, R> R scanHierarchies(
		final H hierarchy, final Predicate<? super H> filter, final HierarchyIterator<H, R> hierarchyIterator,
		final BiConsumer<List<H>, Collection<? extends H>> appender) {
		Objects.requireNonNull(hierarchy);
		Objects.requireNonNull(filter);
		Objects.requireNonNull(hierarchyIterator);

		// 遍历层级结构
		final Predicate<? super H> hierarchyFilter = filter.negate();
		final LinkedList<H> queue = new LinkedList<>();
		final Set<H> accessed = new HashSet<>();
		queue.add(hierarchy);
		while (!queue.isEmpty()) {
			// 跳过已经访问过或者被过滤的层级结构
			final H curr = queue.removeFirst();
			if (accessed.contains(curr) || hierarchyFilter.test(curr)) {
				continue;
			}
			accessed.add(curr);

			// 若迭代器返回true，则结束遍历
			if (hierarchyIterator.isBreak(curr)) {
				return hierarchyIterator.getResult();
			}

			// 获取下一需要遍历的层级
			final Collection<? extends H> nextHierarchies = hierarchyIterator.nextHierarchies(hierarchyIterator.getResult(), curr);
			if (CollUtil.isEmpty(nextHierarchies)) {
				continue;
			}
			appender.accept(queue, nextHierarchies);
		}
		return hierarchyIterator.getResult();
	}
}
