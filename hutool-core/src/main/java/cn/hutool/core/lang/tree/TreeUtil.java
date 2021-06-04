package cn.hutool.core.lang.tree;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.parser.DefaultNodeParser;
import cn.hutool.core.lang.tree.parser.NodeParser;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
	 * @param rootId       最顶层父id值 一般为 0 之类
	 * @param treeNodeConfig 配置
	 * @param nodeParser     转换器
	 * @return List
	 */
	public static <T, E> List<Tree<E>> build(List<T> list, E rootId, TreeNodeConfig treeNodeConfig, NodeParser<T, E> nodeParser) {
		final Map<E, Tree<E>> map = new LinkedHashMap<>(list.size(), 1);
		Tree<E> node;
		for (T t : list) {
			node = new Tree<>(treeNodeConfig);
			nodeParser.parse(t, node);
			map.put(node.getId(), node);
		}

		return build(map, rootId);
	}

	/**
	 * 树构建，按照权重排序
	 *
	 * @param <E>            ID类型
	 * @param map           源数据Map
	 * @param rootId       最顶层父id值 一般为 0 之类
	 * @return List
	 * @since 5.6.7
	 */
	public static <E> List<Tree<E>> build(Map<E, Tree<E>> map, E rootId) {
		final Map<E, Tree<E>> eTreeMap = MapUtil.sortByValue(map, false);
		List<Tree<E>> rootTreeList = CollUtil.newArrayList();
		E parentId;
		for (Tree<E> node : eTreeMap.values()) {
			parentId = node.getParentId();
			if(ObjectUtil.equals(rootId, parentId)){
				rootTreeList.add(node);
				continue;
			}

			final Tree<E> parentNode = map.get(parentId);
			if(null != parentNode){
				parentNode.addChildren(node);
			}
		}
		return rootTreeList;
	}

	/**
	 * 获取ID对应的节点，如果有多个ID相同的节点，只返回第一个。<br>
	 * 此方法只查找此节点及子节点，采用递归深度优先遍历。
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
