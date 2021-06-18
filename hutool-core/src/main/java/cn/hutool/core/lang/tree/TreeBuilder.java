package cn.hutool.core.lang.tree;

import cn.hutool.core.builder.Builder;
import cn.hutool.core.collection.CollUtil;
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

	@Override
	public Tree<E> build() {
		buildFromMap();
		return root;
	}

	/**
	 * 构建树列表，例如：
	 *
	 * <pre>
	 * -用户管理
	 *  --用户添加
	 *  --用户管理
	 * - 部门管理
	 *  --部门添加
	 *  --部门管理
	 * </pre>
	 *
	 * @return 树列表
	 */
	public List<Tree<E>> buildList() {
		return this.root.getChildren();
	}

	/**
	 * 开始构建
	 */
	private void buildFromMap() {
		final Map<E, Tree<E>> eTreeMap = MapUtil.sortByValue(this.idTreeMap, false);
		List<Tree<E>> rootTreeList = CollUtil.newArrayList();
		E parentId;
		for (Tree<E> node : eTreeMap.values()) {
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
}
