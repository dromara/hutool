package cn.hutool.core.lang.tree;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.parser.DefaultNodeParser;
import cn.hutool.core.lang.tree.parser.NodeParser;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 树工具类
 *
 * @author liangbaikai
 */
public class TreeUtil {

	/**
	 * 树构建
	 *
	 * @param list 源数据集合
	 * @return List
	 */
	public static List<Tree<Integer>> build(List<TreeNode<Integer>> list) {
		return build(list, 0);
	}

	/**
	 * 树构建
	 *
	 * @param <E>      ID类型
	 * @param list     源数据集合
	 * @param parentId 最顶层父id值 一般为 0 之类
	 * @return List
	 */
	public static <E> List<Tree<E>> build(List<TreeNode<E>> list, E parentId) {
		return build(list, parentId, TreeNodeConfig.DEFAULT_CONFIG, new DefaultNodeParser<>());
	}

	/**
	 * 树构建
	 *
	 * @param <T>        转换的实体 为数据源里的对象类型
	 * @param <E>        ID类型
	 * @param list       源数据集合
	 * @param parentId   最顶层父id值 一般为 0 之类
	 * @param nodeParser 转换器
	 * @return List
	 */
	public static <T, E> List<Tree<E>> build(List<T> list, E parentId, NodeParser<T, E> nodeParser) {
		return build(list, parentId, TreeNodeConfig.DEFAULT_CONFIG, nodeParser);
	}

	/**
	 * 树构建
	 *
	 * @param <T>            转换的实体 为数据源里的对象类型
	 * @param <E>            ID类型
	 * @param list           源数据集合
	 * @param parentId       最顶层父id值 一般为 0 之类
	 * @param treeNodeConfig 配置
	 * @param nodeParser     转换器
	 * @return List
	 */
	public static <T, E> List<Tree<E>> build(List<T> list, E parentId, TreeNodeConfig treeNodeConfig, NodeParser<T, E> nodeParser) {
		final List<Tree<E>> treeNodes = CollUtil.newArrayList();
		Tree<E> treeNode;
		for (T obj : list) {
			treeNode = new Tree<>(treeNodeConfig);
			nodeParser.parse(obj, treeNode);
			treeNodes.add(treeNode);
		}

		List<Tree<E>> finalTreeNodes = CollUtil.newArrayList();
		for (Tree<E> node : treeNodes) {
			if (parentId.equals(node.getParentId())) {
				finalTreeNodes.add(node);
				innerBuild(treeNodes, node, 0, treeNodeConfig.getDeep());
			}
		}
		// 内存每层已经排过了 这是最外层排序
		finalTreeNodes = finalTreeNodes.stream().sorted().collect(Collectors.toList());
		return finalTreeNodes;
	}

	/**
	 * 递归处理
	 *
	 * @param treeNodes  数据集合
	 * @param parentNode 当前节点
	 * @param deep       已递归深度
	 * @param maxDeep    最大递归深度 可能为null即不限制
	 */
	private static <T> void innerBuild(List<Tree<T>> treeNodes, Tree<T> parentNode, int deep, Integer maxDeep) {

		if (CollUtil.isEmpty(treeNodes)) {
			return;
		}
		//maxDeep 可能为空
		if (maxDeep != null && deep >= maxDeep) {
			return;
		}

		// 每层排序 TreeNodeMap 实现了Comparable接口
		treeNodes = treeNodes.stream().sorted().collect(Collectors.toList());
		for (Tree<T> childNode : treeNodes) {
			if (parentNode.getId().equals(childNode.getParentId())) {
				List<Tree<T>> children = parentNode.getChildren();
				if (children == null) {
					children = CollUtil.newArrayList();
					parentNode.setChildren(children);
				}
				children.add(childNode);
//				childNode.setParentId(parentNode.getId());
				childNode.setParent(parentNode);
				innerBuild(treeNodes, childNode, deep + 1, maxDeep);
			}
		}
	}

}
