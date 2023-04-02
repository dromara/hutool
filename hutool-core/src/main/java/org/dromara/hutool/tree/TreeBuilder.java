/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.tree;

import org.dromara.hutool.collection.CollUtil;
import org.dromara.hutool.lang.Assert;
import org.dromara.hutool.lang.builder.Builder;
import org.dromara.hutool.map.MapUtil;
import org.dromara.hutool.tree.parser.NodeParser;
import org.dromara.hutool.util.ObjUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 树构建器
 *
 * @param <E> ID类型
 */
public class TreeBuilder<E> implements Builder<MapTree<E>> {
	private static final long serialVersionUID = 1L;

	private final MapTree<E> root;
	private final Map<E, MapTree<E>> idTreeMap;
	private boolean isBuild;

	/**
	 * 创建Tree构建器
	 *
	 * @param rootId 根节点ID
	 * @param <T>    ID类型
	 * @return TreeBuilder
	 */
	public static <T> TreeBuilder<T> of(final T rootId) {
		return of(rootId, null);
	}

	/**
	 * 创建Tree构建器
	 *
	 * @param rootId 根节点ID
	 * @param config 配置
	 * @param <T>    ID类型
	 * @return TreeBuilder
	 */
	public static <T> TreeBuilder<T> of(final T rootId, final TreeNodeConfig config) {
		return new TreeBuilder<>(rootId, config);
	}

	/**
	 * 构造
	 *
	 * @param rootId 根节点ID
	 * @param config 配置
	 */
	public TreeBuilder(final E rootId, final TreeNodeConfig config) {
		root = new MapTree<>(config);
		root.setId(rootId);
		this.idTreeMap = new LinkedHashMap<>();
	}

	/**
	 * 设置ID
	 *
	 * @param id ID
	 * @return this
	 * @since 5.7.14
	 */
	public TreeBuilder<E> setId(final E id) {
		this.root.setId(id);
		return this;
	}

	/**
	 * 设置父节点ID
	 *
	 * @param parentId 父节点ID
	 * @return this
	 * @since 5.7.14
	 */
	public TreeBuilder<E> setParentId(final E parentId) {
		this.root.setParentId(parentId);
		return this;
	}

	/**
	 * 设置节点标签名称
	 *
	 * @param name 节点标签名称
	 * @return this
	 * @since 5.7.14
	 */
	public TreeBuilder<E> setName(final CharSequence name) {
		this.root.setName(name);
		return this;
	}

	/**
	 * 设置权重
	 *
	 * @param weight 权重
	 * @return this
	 * @since 5.7.14
	 */
	public TreeBuilder<E> setWeight(final Comparable<?> weight) {
		this.root.setWeight(weight);
		return this;
	}

	/**
	 * 扩展属性
	 *
	 * @param key   键
	 * @param value 扩展值
	 * @return this
	 * @since 5.7.14
	 */
	public TreeBuilder<E> putExtra(final String key, final Object value) {
		Assert.notEmpty(key, "Key must be not empty !");
		this.root.put(key, value);
		return this;
	}

	/**
	 * 增加节点列表，增加的节点是不带子节点的
	 *
	 * @param map 节点列表
	 * @return this
	 */
	public TreeBuilder<E> append(final Map<E, MapTree<E>> map) {
		checkBuilt();

		this.idTreeMap.putAll(map);
		return this;
	}

	/**
	 * 增加节点列表，增加的节点是不带子节点的
	 *
	 * @param trees 节点列表
	 * @return this
	 */
	public TreeBuilder<E> append(final Iterable<MapTree<E>> trees) {
		checkBuilt();

		for (final MapTree<E> tree : trees) {
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
	public <T> TreeBuilder<E> append(final List<T> list, final NodeParser<T, E> nodeParser) {
		checkBuilt();

		final TreeNodeConfig config = this.root.getConfig();
		final Map<E, MapTree<E>> map = new LinkedHashMap<>(list.size(), 1);
		MapTree<E> node;
		for (final T t : list) {
			node = new MapTree<>(config);
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
	public MapTree<E> build() {
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
	public List<MapTree<E>> buildList() {
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

		final Map<E, MapTree<E>> eTreeMap = MapUtil.sortByValue(this.idTreeMap, false);
		E parentId;
		for (final MapTree<E> node : eTreeMap.values()) {
			if (null == node) {
				continue;
			}
			parentId = node.getParentId();
			if (ObjUtil.equals(this.root.getId(), parentId)) {
				this.root.addChildren(node);
				continue;
			}

			final MapTree<E> parentNode = eTreeMap.get(parentId);
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
	private void cutTree(final MapTree<E> tree, final int currentDepp, final int maxDeep) {
		if (null == tree) {
			return;
		}
		if (currentDepp == maxDeep) {
			// 剪枝
			tree.setChildren(null);
			return;
		}

		final List<MapTree<E>> children = tree.getChildren();
		if (CollUtil.isNotEmpty(children)) {
			for (final MapTree<E> child : children) {
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
