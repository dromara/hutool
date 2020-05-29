package cn.hutool.core.lang.tree;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.parser.DefaultNodeParser;
import cn.hutool.core.lang.tree.parser.NodeParser;
import cn.hutool.core.util.ObjectUtil;

import java.util.ArrayList;
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
		final List<Tree<E>> treeList = CollUtil.newArrayList();
		Tree<E> tree;
		for (T obj : list) {
			tree = new Tree<>(treeNodeConfig);
			nodeParser.parse(obj, tree);
			treeList.add(tree);
		}

		List<Tree<E>> finalTreeList = CollUtil.newArrayList();
		for (Tree<E> node : treeList) {
			if (parentId.equals(node.getParentId())) {
				finalTreeList.add(node);
				innerBuild(treeList, node, 0, treeNodeConfig.getDeep());
			}
		}
		// 内存每层已经排过了 这是最外层排序
		finalTreeList = finalTreeList.stream().sorted().collect(Collectors.toList());
		return finalTreeList;
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

	/**
	 * 获取ID对应的节点，如果有多个ID相同的节点，只返回第一个。<br>
	 * 此方法只查找此节点及子节点，采用广度优先遍历。
	 *
	 * @param <T> ID类型
	 * @param node 节点
	 * @param id ID
	 * @return 节点
	 * @since 5.2.4
	 */
	public static <T> Tree<T> getNode(Tree<T> node, T id) {
		if (ObjectUtil.equal(id, node.getId())) {
			return node;
		}

		final List<Tree<T>> children = node.getChildren();
		if(null == children) {
			return null;
		}
		
		// 查找子节点
		Tree<T> childNode;
		for (Tree<T> child : children) {
			childNode = child.getNode(id);
			if (null != childNode) {
				return childNode;
			}
		}

		// 未找到节点
		return null;
	}

	/**
	 * 获取所有父节点名称列表
	 *
	 * <p>
	 * 比如有个人在研发1部，他上面有研发部，接着上面有技术中心<br>
	 * 返回结果就是：[研发一部, 研发中心, 技术中心]
	 *
	 * @param <T> 节点ID类型
	 * @param node 节点
	 * @param includeCurrentNode 是否包含当前节点的名称
	 * @return 所有父节点名称列表，node为null返回空List
	 * @since 5.2.4
	 */
	public static <T> List<CharSequence> getParentsName(Tree<T> node, boolean includeCurrentNode) {
		final List<CharSequence> result = new ArrayList<>();
		if(null == node){
			return result;
		}

		if (includeCurrentNode) {
			result.add(node.getName());
		}

		Tree<T> parent = node.getParent();
		while (null != parent) {
			result.add(parent.getName());
			parent = parent.getParent();
		}
		return result;
	}

}
