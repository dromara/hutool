package cn.hutool.core.lang.tree;

import cn.hutool.core.builder.Builder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.tree.parser.NodeParser;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 树构建器
 *
 * @param <E> ID类型
 */
public class TreeBuilder<E> implements Builder<Tree<E>> {
	private static final long serialVersionUID = 1L;

	private final Tree<E> root;
	private final Map<E, Tree<E>> idTreeMap;
	private boolean isBuild;

	/**
	 * 创建Tree构建器
	 *
	 * @param rootId 根节点ID
	 * @param <T>    ID类型
	 * @return {@link TreeBuilder}
	 */
	public static <T> TreeBuilder<T> of(T rootId) {
		return of(rootId, null);
	}

	/**
	 * 创建Tree构建器
	 *
	 * @param rootId 根节点ID
	 * @param config 配置
	 * @param <T>    ID类型
	 * @return {@link TreeBuilder}
	 */
	public static <T> TreeBuilder<T> of(T rootId, TreeNodeConfig config) {
		return new TreeBuilder<>(rootId, config);
	}

	/**
	 * 构造
	 *
	 * @param rootId 根节点ID
	 * @param config 配置
	 */
	public TreeBuilder(E rootId, TreeNodeConfig config) {
		root = new Tree<>(config);
		root.setId(rootId);
		this.idTreeMap = new HashMap<>();
	}

	/**
	 * 增加节点列表，增加的节点是不带子节点的
	 *
	 * @param map 节点列表
	 * @return this
	 */
	public TreeBuilder<E> append(Map<E, Tree<E>> map) {
		checkBuilt();

		Assert.isFalse(isBuild, "Current tree has been built.");
		this.idTreeMap.putAll(map);
		return this;
	}

	/**
	 * 增加节点列表，增加的节点是不带子节点的
	 *
	 * @param trees 节点列表
	 * @return this
	 */
	public TreeBuilder<E> append(Iterable<Tree<E>> trees) {
		checkBuilt();

		for (Tree<E> tree : trees) {
			this.idTreeMap.put(tree.getId(), tree);
		}
		return this;
	}

	/**
	 * 增加节点列表，增加的节点是不带子节点的
	 *
	 * @param list       Bean列表
	 * @param <T>        Bean类型
	 * @param nodeParser 节点转换器，用于定义一个Bean如何转换为Tree节点
	 * @return this
	 */
	public <T> TreeBuilder<E> append(List<T> list, NodeParser<T, E> nodeParser) {
		checkBuilt();

		final TreeNodeConfig config = this.root.getConfig();
		final Map<E, Tree<E>> map = new LinkedHashMap<>(list.size(), 1);
		Tree<E> node;
		for (T t : list) {
			node = new Tree<>(config);
			nodeParser.parse(t, node);
			map.put(node.getId(), node);
		}
		return append(map);
	}


	/**
	 * 重置Builder，实现复用
	 *
	 * @return this
	 */
	public TreeBuilder<E> reset() {
		this.idTreeMap.clear();
		this.root.setChildren(null);
		this.isBuild = false;
		return this;
	}

	@Override
	public Tree<E> build() {
		checkBuilt();

		buildFromMap();
		cutTree();

		this.isBuild = true;
		this.idTreeMap.clear();

		return root;
	}

	/**
	 * 构建树列表，没有顶层节点，例如：
	 *
	 * <pre>
	 * -用户管理
	 *  -用户管理
	 *    +用户添加
	 * - 部门管理
	 *  -部门管理
	 *    +部门添加
	 * </pre>
	 *
	 * @return 树列表
	 */
	public List<Tree<E>> buildList() {
		if (isBuild) {
			// 已经构建过了
			return this.root.getChildren();
		}
		return build().getChildren();
	}

	/**
	 * 开始构建
	 */
	private void buildFromMap() {
		if (MapUtil.isEmpty(this.idTreeMap)) {
			return;
		}

		final Map<E, Tree<E>> eTreeMap = MapUtil.sortByValue(this.idTreeMap, false);
		List<Tree<E>> rootTreeList = CollUtil.newArrayList();
		E parentId;
		for (Tree<E> node : eTreeMap.values()) {
			if (null == node) {
				continue;
			}
			parentId = node.getParentId();
			if (ObjectUtil.equals(this.root.getId(), parentId)) {
				this.root.addChildren(node);
				rootTreeList.add(node);
				continue;
			}

			final Tree<E> parentNode = eTreeMap.get(parentId);
			if (null != parentNode) {
				parentNode.addChildren(node);
			}
		}
	}

	/**
	 * 树剪枝
	 */
	private void cutTree() {
		final TreeNodeConfig config = this.root.getConfig();
		final Integer deep = config.getDeep();
		if (null == deep || deep < 0) {
			return;
		}
		cutTree(this.root, 0, deep);
	}

	/**
	 * 树剪枝叶
	 *
	 * @param tree        节点
	 * @param currentDepp 当前层级
	 * @param maxDeep     最大层级
	 */
	private void cutTree(Tree<E> tree, int currentDepp, int maxDeep) {
		if (null == tree) {
			return;
		}
		if (currentDepp == maxDeep) {
			// 剪枝
			tree.setChildren(null);
			return;
		}

		final List<Tree<E>> children = tree.getChildren();
		if (CollUtil.isNotEmpty(children)) {
			for (Tree<E> child : children) {
				cutTree(child, currentDepp + 1, maxDeep);
			}
		}
	}

	/**
	 * 检查是否已经构建
	 */
	private void checkBuilt() {
		Assert.isFalse(isBuild, "Current tree has been built.");
	}
}
