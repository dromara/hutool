package cn.hutool.core.lang.tree.lambda;

import java.util.List;

/**
 * Lambda树形转换工具
 *
 * @author adoph
 * @version 1.0
 * @since 2022/8/30
 */
public class LambdaTreeUtil {

	/**
	 * 树形构造器
	 *
	 * @param treeNodes 原节点数据集
	 * @param <E>       节点类型
	 * @param <R>       节点标识类型
	 * @return {@link TreeBuilder}
	 */
	public static <E, R> TreeBuilder<E, R> builder(List<E> treeNodes) {
		return new TreeBuilder<>(treeNodes);
	}

	/**
	 * 树形构造器（指定根节点标识）
	 *
	 * @param treeNodes 原节点数据集
	 * @param rootPid   根据点标识
	 * @param <E>       节点类型
	 * @param <R>       节点标识类型
	 * @return {@link TreeBuilder}
	 */
	public static <E, R> TreeBuilder<E, R> builder(List<E> treeNodes, R rootPid) {
		return new TreeBuilder<>(treeNodes, rootPid);
	}

}
