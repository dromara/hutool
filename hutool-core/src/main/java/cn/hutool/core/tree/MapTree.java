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

package cn.hutool.core.tree;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ObjUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 通过转换器将你的实体转化为TreeNodeMap节点实体 属性都存在此处,属性有序，可支持排序
 *
 * @param <T> ID类型
 * @author liangbaikai
 * @since 5.2.1
 */
public class MapTree<T> extends LinkedHashMap<String, Object> implements Node<T> {
	private static final long serialVersionUID = 1L;

	private final TreeNodeConfig treeNodeConfig;
	private MapTree<T> parent;

	/**
	 * 构造
	 */
	public MapTree() {
		this(null);
	}

	/**
	 * 构造
	 *
	 * @param treeNodeConfig TreeNode配置
	 */
	public MapTree(final TreeNodeConfig treeNodeConfig) {
		this.treeNodeConfig = ObjUtil.defaultIfNull(
				treeNodeConfig, TreeNodeConfig.DEFAULT_CONFIG);
	}

	/**
	 * 获取节点配置
	 *
	 * @return 节点配置
	 * @since 5.7.2
	 */
	public TreeNodeConfig getConfig() {
		return this.treeNodeConfig;
	}

	/**
	 * 获取父节点
	 *
	 * @return 父节点
	 * @since 5.2.4
	 */
	public MapTree<T> getParent() {
		return parent;
	}

	/**
	 * 获取ID对应的节点，如果有多个ID相同的节点，只返回第一个。<br>
	 * 此方法只查找此节点及子节点，采用广度优先遍历。
	 *
	 * @param id ID
	 * @return 节点
	 * @since 5.2.4
	 */
	public MapTree<T> getNode(final T id) {
		return TreeUtil.getNode(this, id);
	}

	/**
	 * 获取所有父节点名称列表
	 *
	 * <p>
	 * 比如有个人在研发1部，他上面有研发部，接着上面有技术中心<br>
	 * 返回结果就是：[研发一部, 研发中心, 技术中心]
	 *
	 * @param id                 节点ID
	 * @param includeCurrentNode 是否包含当前节点的名称
	 * @return 所有父节点名称列表
	 * @since 5.2.4
	 */
	public List<CharSequence> getParentsName(final T id, final boolean includeCurrentNode) {
		return TreeUtil.getParentsName(getNode(id), includeCurrentNode);
	}

	/**
	 * 获取所有父节点名称列表
	 *
	 * <p>
	 * 比如有个人在研发1部，他上面有研发部，接着上面有技术中心<br>
	 * 返回结果就是：[研发一部, 研发中心, 技术中心]
	 *
	 * @param includeCurrentNode 是否包含当前节点的名称
	 * @return 所有父节点名称列表
	 * @since 5.2.4
	 */
	public List<CharSequence> getParentsName(final boolean includeCurrentNode) {
		return TreeUtil.getParentsName(this, includeCurrentNode);
	}

	/**
	 * 设置父节点
	 *
	 * @param parent 父节点
	 * @return this
	 * @since 5.2.4
	 */
	public MapTree<T> setParent(final MapTree<T> parent) {
		this.parent = parent;
		if (null != parent) {
			this.setParentId(parent.getId());
		}
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getId() {
		return (T) this.get(treeNodeConfig.getIdKey());
	}

	@Override
	public MapTree<T> setId(final T id) {
		this.put(treeNodeConfig.getIdKey(), id);
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getParentId() {
		return (T) this.get(treeNodeConfig.getParentIdKey());
	}

	@Override
	public MapTree<T> setParentId(final T parentId) {
		this.put(treeNodeConfig.getParentIdKey(), parentId);
		return this;
	}

	@Override
	public CharSequence getName() {
		return (CharSequence) this.get(treeNodeConfig.getNameKey());
	}

	@Override
	public MapTree<T> setName(final CharSequence name) {
		this.put(treeNodeConfig.getNameKey(), name);
		return this;
	}

	@Override
	public Comparable<?> getWeight() {
		return (Comparable<?>) this.get(treeNodeConfig.getWeightKey());
	}

	@Override
	public MapTree<T> setWeight(final Comparable<?> weight) {
		this.put(treeNodeConfig.getWeightKey(), weight);
		return this;
	}

	/**
	 * 获取所有子节点
	 *
	 * @return 所有子节点
	 */
	@SuppressWarnings("unchecked")
	public List<MapTree<T>> getChildren() {
		return (List<MapTree<T>>) this.get(treeNodeConfig.getChildrenKey());
	}

	/**
	 * 是否有子节点，无子节点则此为叶子节点
	 *
	 * @return 是否有子节点
	 * @since 5.7.17
	 */
	public boolean hasChild() {
		return CollUtil.isNotEmpty(getChildren());
	}

	/**
	 * 递归树并处理子树下的节点，采用深度优先遍历方式。
	 *
	 * @param consumer 节点处理器
	 * @since 5.7.16
	 */
	public void walk(final Consumer<MapTree<T>> consumer) {
		walk(consumer, false);
	}

	/**
	 * 递归树并处理子树下的节点
	 *
	 * @param consumer 节点处理器
	 * @param broadFirst 是否广度优先遍历
	 * @since 6.0.0
	 */
	public void walk(final Consumer<MapTree<T>> consumer, final boolean broadFirst) {
		if (broadFirst) { // 广度优先遍历
			// 加入FIFO队列
			final Queue<MapTree<T>> queue = new LinkedList<>();
			queue.offer(this);
			while (false == queue.isEmpty()) {
				final MapTree<T> node = queue.poll();
				consumer.accept(node);
				final List<MapTree<T>> children = node.getChildren();
				if (CollUtil.isNotEmpty(children)) {
					children.forEach(queue::offer);
				}
			}
		} else { // 深度优先遍历
			// 入栈,FILO
			final Stack<MapTree<T>> stack = new Stack<>();
			stack.add(this);
			while (false == stack.isEmpty()) {
				final MapTree<T> node = stack.pop();
				consumer.accept(node);
				final List<MapTree<T>> children = node.getChildren();
				if (CollUtil.isNotEmpty(children)) {
					children.forEach(stack::push);
				}
			}
		}
	}

	/**
	 * 递归过滤并生成新的树<br>
	 * 通过{@link Predicate}指定的过滤规则，本节点或子节点满足过滤条件，则保留当前节点，否则抛弃节点及其子节点
	 *
	 * @param predicate 节点过滤规则函数，只需处理本级节点本身即可，{@link Predicate#test(Object)}为{@code true}保留，null表示全部保留
	 * @return 过滤后的节点，{@code null} 表示不满足过滤要求，丢弃之
	 * @see #filter(Predicate)
	 * @since 5.7.17
	 */
	public MapTree<T> filterNew(final Predicate<MapTree<T>> predicate) {
		return cloneTree().filter(predicate);
	}

	/**
	 * 递归过滤当前树，注意此方法会修改当前树<br>
	 * 通过{@link Predicate}指定的过滤规则，本节点或子节点满足过滤条件，则保留当前节点及其所有子节点，否则抛弃节点及其子节点
	 *
	 * @param predicate 节点过滤规则函数，只需处理本级节点本身即可，{@link Predicate#test(Object)}为{@code true}保留，null表示保留全部
	 * @return 过滤后的节点，{@code null} 表示不满足过滤要求，丢弃之
	 * @see #filterNew(Predicate)
	 * @since 5.7.17
	 */
	public MapTree<T> filter(final Predicate<MapTree<T>> predicate) {
		if (null == predicate || predicate.test(this)) {
			// 本节点满足，则包括所有子节点都保留
			return this;
		}

		final List<MapTree<T>> children = getChildren();
		if (CollUtil.isNotEmpty(children)) {
			// 递归过滤子节点
			final List<MapTree<T>> filteredChildren = new ArrayList<>(children.size());
			MapTree<T> filteredChild;
			for (final MapTree<T> child : children) {
				filteredChild = child.filter(predicate);
				if (null != filteredChild) {
					filteredChildren.add(filteredChild);
				}
			}
			if (CollUtil.isNotEmpty(filteredChildren)) {
				// 子节点有符合过滤条件的节点，则本节点保留
				return this.setChildren(filteredChildren);
			} else {
				this.setChildren(null);
			}
		}

		// 子节点都不符合过滤条件，检查本节点
		return null;
	}

	/**
	 * 设置子节点，设置后会覆盖所有原有子节点
	 *
	 * @param children 子节点列表，如果为{@code null}表示移除子节点
	 * @return this
	 */
	public MapTree<T> setChildren(final List<MapTree<T>> children) {
		if (null == children) {
			this.remove(treeNodeConfig.getChildrenKey());
		}
		this.put(treeNodeConfig.getChildrenKey(), children);
		return this;
	}

	/**
	 * 增加子节点，同时关联子节点的父节点为当前节点
	 *
	 * @param children 子节点列表
	 * @return this
	 * @since 5.6.7
	 */
	@SafeVarargs
	public final MapTree<T> addChildren(final MapTree<T>... children) {
		if (ArrayUtil.isNotEmpty(children)) {
			List<MapTree<T>> childrenList = this.getChildren();
			if (null == childrenList) {
				childrenList = new ArrayList<>();
				setChildren(childrenList);
			}
			for (final MapTree<T> child : children) {
				child.setParent(this);
				childrenList.add(child);
			}
		}
		return this;
	}

	/**
	 * 扩展属性
	 *
	 * @param key   键
	 * @param value 扩展值
	 */
	public void putExtra(final String key, final Object value) {
		Assert.notEmpty(key, "Key must be not empty !");
		this.put(key, value);
	}

	@Override
	public String toString() {
		final StringWriter stringWriter = new StringWriter();
		printTree(this, new PrintWriter(stringWriter), 0);
		return stringWriter.toString();
	}

	/**
	 * 递归克隆当前节点（即克隆整个树，保留字段值）<br>
	 * 注意，此方法只会克隆节点，节点属性如果是引用类型，不会克隆
	 *
	 * @return 新的节点
	 * @since 5.7.17
	 */
	public MapTree<T> cloneTree() {
		final MapTree<T> result = ObjUtil.clone(this);
		result.setChildren(cloneChildren());
		return result;
	}

	/**
	 * 递归复制子节点
	 *
	 * @return 新的子节点列表
	 */
	private List<MapTree<T>> cloneChildren() {
		final List<MapTree<T>> children = getChildren();
		if (null == children) {
			return null;
		}
		final List<MapTree<T>> newChildren = new ArrayList<>(children.size());
		children.forEach((t) -> newChildren.add(t.cloneTree()));
		return newChildren;
	}

	/**
	 * 打印
	 *
	 * @param tree   树
	 * @param writer Writer
	 * @param intent 缩进量
	 */
	private static void printTree(final MapTree<?> tree, final PrintWriter writer, final int intent) {
		writer.println(StrUtil.format("{}{}[{}]", StrUtil.repeat(CharUtil.SPACE, intent), tree.getName(), tree.getId()));
		writer.flush();

		final List<? extends MapTree<?>> children = tree.getChildren();
		if (CollUtil.isNotEmpty(children)) {
			for (final MapTree<?> child : children) {
				printTree(child, writer, intent + 2);
			}
		}
	}
}
