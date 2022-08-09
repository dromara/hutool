package cn.hutool.core.tree;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.tree.parser.DefaultNodeParser;
import cn.hutool.core.tree.parser.NodeParser;
import cn.hutool.core.util.ObjUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 树工具类
 *
 * @author liangbaikai
 */
public class TreeUtil {

	/**
	 * 构建单root节点树
	 *
	 * @param list 源数据集合
	 * @return {@link Tree}
	 * @since 5.7.2
	 */
	public static Tree<Integer> buildSingle(final List<TreeNode<Integer>> list) {
		return buildSingle(list, 0);
	}

	/**
	 * 树构建
	 *
	 * @param list 源数据集合
	 * @return List
	 */
	public static List<Tree<Integer>> build(final List<TreeNode<Integer>> list) {
		return build(list, 0);
	}

	/**
	 * 构建单root节点树<br>
	 * 它会生成一个以指定ID为ID的空的节点，然后逐级增加子节点。
	 *
	 * @param <E>      ID类型
	 * @param list     源数据集合
	 * @param parentId 最顶层父id值 一般为 0 之类
	 * @return {@link Tree}
	 * @since 5.7.2
	 */
	public static <E> Tree<E> buildSingle(final List<TreeNode<E>> list, final E parentId) {
		return buildSingle(list, parentId, TreeNodeConfig.DEFAULT_CONFIG, new DefaultNodeParser<>());
	}

	/**
	 * 树构建
	 *
	 * @param <E>      ID类型
	 * @param list     源数据集合
	 * @param parentId 最顶层父id值 一般为 0 之类
	 * @return List
	 */
	public static <E> List<Tree<E>> build(final List<TreeNode<E>> list, final E parentId) {
		return build(list, parentId, TreeNodeConfig.DEFAULT_CONFIG, new DefaultNodeParser<>());
	}

	/**
	 * 构建单root节点树<br>
	 * 它会生成一个以指定ID为ID的空的节点，然后逐级增加子节点。
	 *
	 * @param <T>        转换的实体 为数据源里的对象类型
	 * @param <E>        ID类型
	 * @param list       源数据集合
	 * @param parentId   最顶层父id值 一般为 0 之类
	 * @param nodeParser 转换器
	 * @return {@link Tree}
	 * @since 5.7.2
	 */
	public static <T, E> Tree<E> buildSingle(final List<T> list, final E parentId, final NodeParser<T, E> nodeParser) {
		return buildSingle(list, parentId, TreeNodeConfig.DEFAULT_CONFIG, nodeParser);
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
	public static <T, E> List<Tree<E>> build(final List<T> list, final E parentId, final NodeParser<T, E> nodeParser) {
		return build(list, parentId, TreeNodeConfig.DEFAULT_CONFIG, nodeParser);
	}

	/**
	 * 树构建
	 *
	 * @param <T>            转换的实体 为数据源里的对象类型
	 * @param <E>            ID类型
	 * @param list           源数据集合
	 * @param rootId         最顶层父id值 一般为 0 之类
	 * @param treeNodeConfig 配置
	 * @param nodeParser     转换器
	 * @return List
	 */
	public static <T, E> List<Tree<E>> build(final List<T> list, final E rootId, final TreeNodeConfig treeNodeConfig, final NodeParser<T, E> nodeParser) {
		return buildSingle(list, rootId, treeNodeConfig, nodeParser).getChildren();
	}

	/**
	 * 构建单root节点树<br>
	 * 它会生成一个以指定ID为ID的空的节点，然后逐级增加子节点。
	 *
	 * @param <T>            转换的实体 为数据源里的对象类型
	 * @param <E>            ID类型
	 * @param list           源数据集合
	 * @param rootId         最顶层父id值 一般为 0 之类
	 * @param treeNodeConfig 配置
	 * @param nodeParser     转换器
	 * @return {@link Tree}
	 * @since 5.7.2
	 */
	public static <T, E> Tree<E> buildSingle(final List<T> list, final E rootId, final TreeNodeConfig treeNodeConfig, final NodeParser<T, E> nodeParser) {
		return TreeBuilder.of(rootId, treeNodeConfig)
				.append(list, nodeParser).build();
	}

	/**
	 * 树构建，按照权重排序
	 *
	 * @param <E>    ID类型
	 * @param map    源数据Map
	 * @param rootId 最顶层父id值 一般为 0 之类
	 * @return List
	 * @since 5.6.7
	 */
	public static <E> List<Tree<E>> build(final Map<E, Tree<E>> map, final E rootId) {
		return buildSingle(map, rootId).getChildren();
	}

	/**
	 * 单点树构建，按照权重排序<br>
	 * 它会生成一个以指定ID为ID的空的节点，然后逐级增加子节点。
	 *
	 * @param <E>    ID类型
	 * @param map    源数据Map
	 * @param rootId 根节点id值 一般为 0 之类
	 * @return {@link Tree}
	 * @since 5.7.2
	 */
	public static <E> Tree<E> buildSingle(final Map<E, Tree<E>> map, final E rootId) {
		final Tree<E> tree = CollUtil.getFirstNoneNull(map.values());
		if (null != tree) {
			final TreeNodeConfig config = tree.getConfig();
			return TreeBuilder.of(rootId, config)
					.append(map)
					.build();
		}

		return createEmptyNode(rootId);
	}

	/**
	 * 获取ID对应的节点，如果有多个ID相同的节点，只返回第一个。<br>
	 * 此方法只查找此节点及子节点，采用递归深度优先遍历。
	 *
	 * @param <T>  ID类型
	 * @param node 节点
	 * @param id   ID
	 * @return 节点
	 * @since 5.2.4
	 */
	public static <T> Tree<T> getNode(final Tree<T> node, final T id) {
		if (ObjUtil.equals(id, node.getId())) {
			return node;
		}

		final List<Tree<T>> children = node.getChildren();
		if (null == children) {
			return null;
		}

		// 查找子节点
		Tree<T> childNode;
		for (final Tree<T> child : children) {
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
	 * @param <T>                节点ID类型
	 * @param node               节点
	 * @param includeCurrentNode 是否包含当前节点的名称
	 * @return 所有父节点名称列表，node为null返回空List
	 * @since 5.2.4
	 */
	public static <T> List<CharSequence> getParentsName(final Tree<T> node, final boolean includeCurrentNode) {
		final List<CharSequence> result = new ArrayList<>();
		if (null == node) {
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

	/**
	 * 创建空Tree的节点
	 *
	 * @param id  节点ID
	 * @param <E> 节点ID类型
	 * @return {@link Tree}
	 * @since 5.7.2
	 */
	public static <E> Tree<E> createEmptyNode(final E id) {
		return new Tree<E>().setId(id);
	}

	/**
	 * 深度优先,遍历森林,将森林转换为数组
	 *
	 * @param forest 森林
	 * @param <E>    节点ID类型
	 * @return 森林所有节点列表
	 */
	public static <E> List<Tree<E>> deepFirstForestConvertToList(List<Tree<E>> forest) {
		if (CollectionUtil.isEmpty(forest)) {
			return null;
		}
		List<Tree<E>> list = new ArrayList<>();
		forest.forEach(root -> list.addAll(Objects.requireNonNull(deepFirstTreeConvertToList(root))));
		return list;
	}

	/**
	 * 广度优先,遍历森林,将森林转换为数组
	 *
	 * @param forest 森林
	 * @param <E>    节点ID类型
	 * @return 森林所有节点列表
	 */
	public static <E> List<Tree<E>> broadFirstForestConvertToList(List<Tree<E>> forest) {
		if (CollectionUtil.isEmpty(forest)) {
			return null;
		}
		List<Tree<E>> list = new ArrayList<>();
		forest.forEach(root -> list.addAll(Objects.requireNonNull(broadFirstTreeConvertToList(root))));
		return list;
	}


	/**
	 * 深度优先,遍历树,将树换为数组
	 *
	 * @param root 树的根节点
	 * @param <E>  节点ID类型
	 * @return 树所有节点列表
	 */
	public static <E> List<Tree<E>> deepFirstTreeConvertToList(Tree<E> root) {
		if (Objects.isNull(root)) {
			return null;
		}

		// 入栈,FILO
		List<Tree<E>> list = new ArrayList<>();
		Stack<Tree<E>> stack = new Stack<>();
		stack.add(root);
		while (!stack.isEmpty()) {
			Tree<E> node = stack.pop();
			list.add(node);
			if (node.hasChild()) {
				node.getChildren().forEach(stack::push);
			}
		}
		return list;
	}

	/**
	 * 深度优先,遍历树,将树转换为数组
	 *
	 * @param root 树的根节点
	 * @param <E>  节点ID类型
	 * @return 树所有节点列表
	 */
	public static <E> List<Tree<E>> broadFirstTreeConvertToList(Tree<E> root) {
		if (Objects.isNull(root)) {
			return null;
		}

		// 加入FIFO队列
		List<Tree<E>> list = new ArrayList<>();
		Queue<Tree<E>> queue = new LinkedList<>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			Tree<E> node = queue.poll();
            list.add(node);
			if (node.hasChild()) {
				node.getChildren().forEach(queue::offer);
			}
		}
		return list;
	}
}
