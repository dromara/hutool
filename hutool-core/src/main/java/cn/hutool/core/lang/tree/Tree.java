package cn.hutool.core.lang.tree;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * 通过转换器将你的实体转化为TreeNodeMap节点实体 属性都存在此处,属性有序，可支持排序
 *
 * @param <T> ID类型
 * @author liangbaikai
 * @since 5.2.1
 */
public class Tree<T> extends LinkedHashMap<String, Object> implements Node<T> {
	private static final long serialVersionUID = 1L;

	private final TreeNodeConfig treeNodeConfig;
	private Tree<T> parent;

	public Tree() {
		this(null);
	}

	/**
	 * 构造
	 *
	 * @param treeNodeConfig TreeNode配置
	 */
	public Tree(TreeNodeConfig treeNodeConfig) {
		this.treeNodeConfig = ObjectUtil.defaultIfNull(
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
	public Tree<T> getParent() {
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
	public Tree<T> getNode(T id) {
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
	public List<CharSequence> getParentsName(T id, boolean includeCurrentNode) {
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
	public List<CharSequence> getParentsName(boolean includeCurrentNode) {
		return TreeUtil.getParentsName(this, includeCurrentNode);
	}

	/**
	 * 设置父节点
	 *
	 * @param parent 父节点
	 * @return this
	 * @since 5.2.4
	 */
	public Tree<T> setParent(Tree<T> parent) {
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
	public Tree<T> setId(T id) {
		this.put(treeNodeConfig.getIdKey(), id);
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getParentId() {
		return (T) this.get(treeNodeConfig.getParentIdKey());
	}

	@Override
	public Tree<T> setParentId(T parentId) {
		this.put(treeNodeConfig.getParentIdKey(), parentId);
		return this;
	}

	@Override
	public CharSequence getName() {
		return (CharSequence) this.get(treeNodeConfig.getNameKey());
	}

	@Override
	public Tree<T> setName(CharSequence name) {
		this.put(treeNodeConfig.getNameKey(), name);
		return this;
	}

	@Override
	public Comparable<?> getWeight() {
		return (Comparable<?>) this.get(treeNodeConfig.getWeightKey());
	}

	@Override
	public Tree<T> setWeight(Comparable<?> weight) {
		this.put(treeNodeConfig.getWeightKey(), weight);
		return this;
	}

	/**
	 * 获取所有子节点
	 *
	 * @return 所有子节点
	 */
	@SuppressWarnings("unchecked")
	public List<Tree<T>> getChildren() {
		return (List<Tree<T>>) this.get(treeNodeConfig.getChildrenKey());
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
	 * 递归树并处理子树下的节点：
	 *
	 * @param consumer 节点处理器
	 * @since 5.7.16
	 */
	public void walk(Consumer<Tree<T>> consumer) {
		consumer.accept(this);
		final List<Tree<T>> children = getChildren();
		if (CollUtil.isNotEmpty(children)) {
			children.forEach((tree) -> tree.walk(consumer));
		}
	}

	/**
	 * 递归过滤并生成新的树<br>
	 * 通过{@link Filter}指定的过滤规则，本节点或子节点满足过滤条件，则保留当前节点，否则抛弃节点及其子节点
	 *
	 * @param filter 节点过滤规则函数，只需处理本级节点本身即可
	 * @return 过滤后的节点，{@code null} 表示不满足过滤要求，丢弃之
	 * @see #filter(Filter)
	 * @since 5.7.17
	 */
	public Tree<T> filterNew(Filter<Tree<T>> filter) {
		return cloneTree().filter(filter);
	}

	/**
	 * 递归过滤当前树，注意此方法会修改当前树<br>
	 * 通过{@link Filter}指定的过滤规则，本节点或子节点满足过滤条件，则保留当前节点及其所有子节点，否则抛弃节点及其子节点
	 *
	 * @param filter 节点过滤规则函数，只需处理本级节点本身即可
	 * @return 过滤后的节点，{@code null} 表示不满足过滤要求，丢弃之
	 * @see #filterNew(Filter)
	 * @since 5.7.17
	 */
	public Tree<T> filter(Filter<Tree<T>> filter) {
		if(filter.accept(this)){
			// 本节点满足，则包括所有子节点都保留
			return this;
		}

		final List<Tree<T>> children = getChildren();
		if (CollUtil.isNotEmpty(children)) {
			// 递归过滤子节点
			final List<Tree<T>> filteredChildren = new ArrayList<>(children.size());
			Tree<T> filteredChild;
			for (Tree<T> child : children) {
				filteredChild = child.filter(filter);
				if (null != filteredChild) {
					filteredChildren.add(filteredChild);
				}
			}
			if(CollUtil.isNotEmpty(filteredChildren)){
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
	public Tree<T> setChildren(List<Tree<T>> children) {
		if(null == children){
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
	public final Tree<T> addChildren(Tree<T>... children) {
		if (ArrayUtil.isNotEmpty(children)) {
			List<Tree<T>> childrenList = this.getChildren();
			if (null == childrenList) {
				childrenList = new ArrayList<>();
				setChildren(childrenList);
			}
			for (Tree<T> child : children) {
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
	public void putExtra(String key, Object value) {
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
	public Tree<T> cloneTree() {
		final Tree<T> result = ObjectUtil.clone(this);
		result.setChildren(cloneChildren());
		return result;
	}

	/**
	 * 递归复制子节点
	 *
	 * @return 新的子节点列表
	 */
	private List<Tree<T>> cloneChildren() {
		final List<Tree<T>> children = getChildren();
		if (null == children) {
			return null;
		}
		final List<Tree<T>> newChildren = new ArrayList<>(children.size());
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
	private static void printTree(Tree<?> tree, PrintWriter writer, int intent) {
		writer.println(StrUtil.format("{}{}[{}]", StrUtil.repeat(CharUtil.SPACE, intent), tree.getName(), tree.getId()));
		writer.flush();

		final List<? extends Tree<?>> children = tree.getChildren();
		if (CollUtil.isNotEmpty(children)) {
			for (Tree<?> child : children) {
				printTree(child, writer, intent + 2);
			}
		}
	}
}
