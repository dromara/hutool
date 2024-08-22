/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.tree;


import java.util.Map;
import java.util.Objects;

/**
 * 树节点 每个属性都可以在{@link TreeNodeConfig}中被重命名<br>
 * 在你的项目里它可以是部门实体、地区实体等任意类树节点实体
 * 类树节点实体: 包含key，父Key.不限于这些属性的可以构造成一颗树的实体对象
 *
 * @param <T> ID类型
 * @author liangbaikai
 */
public class TreeNode<T> implements Node<T> {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private T id;

	/**
	 * 父节点ID
	 */
	private T parentId;

	/**
	 * 名称
	 */
	private CharSequence name;

	/**
	 * 顺序 越小优先级越高 默认0
	 */
	private Comparable<?> weight = 0;

	/**
	 * 扩展字段
	 */
	private Map<String, Object> extra;


	/**
	 * 空构造
	 */
	public TreeNode() {
	}

	/**
	 * 构造
	 *
	 * @param id       ID
	 * @param parentId 父节点ID
	 * @param name     名称
	 * @param weight   权重
	 */
	public TreeNode(final T id, final T parentId, final String name, final Comparable<?> weight) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		if (weight != null) {
			this.weight = weight;
		}

	}

	@Override
	public T getId() {
		return id;
	}

	@Override
	public TreeNode<T> setId(final T id) {
		this.id = id;
		return this;
	}

	@Override
	public T getParentId() {
		return this.parentId;
	}

	@Override
	public TreeNode<T> setParentId(final T parentId) {
		this.parentId = parentId;
		return this;
	}

	@Override
	public CharSequence getName() {
		return name;
	}

	@Override
	public TreeNode<T> setName(final CharSequence name) {
		this.name = name;
		return this;
	}

	@Override
	public Comparable<?> getWeight() {
		return weight;
	}

	@Override
	public TreeNode<T> setWeight(final Comparable<?> weight) {
		this.weight = weight;
		return this;
	}

	/**
	 * 获取扩展字段
	 *
	 * @return 扩展字段Map
	 * @since 5.2.5
	 */
	public Map<String, Object> getExtra() {
		return extra;
	}

	/**
	 * 设置扩展字段
	 *
	 * @param extra 扩展字段
	 * @return this
	 * @since 5.2.5
	 */
	public TreeNode<T> setExtra(final Map<String, Object> extra) {
		this.extra = extra;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final TreeNode<?> treeNode = (TreeNode<?>) o;
		return Objects.equals(id, treeNode.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
