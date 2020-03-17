package cn.hutool.core.lang.tree;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 通过转换器将你的实体转化为TreeNodeMap节点实体 属性都存在此处,属性有序，可支持排序
 *
 * @param <T> ID类型
 * @author liangbaikai
 * @since 5.2.1
 */
public class Tree<T> extends LinkedHashMap<String, Object> implements Comparable<Tree<T>> {
	private static final long serialVersionUID = 1L;

	private TreeNodeConfig treeNodeConfig;
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
		super();
		this.treeNodeConfig = ObjectUtil.defaultIfNull(
				treeNodeConfig, TreeNodeConfig.DEFAULT_CONFIG);
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
	 * 设置父节点
	 *
	 * @param parent 父节点
	 * @since 5.2.4
	 */
	public Tree<T> setParent(Tree<T> parent) {
		this.parent = parent;
		if(null != parent){
			this.setParentId(parent.getId());
		}
		return this;
	}

	/**
	 * 获取节点ID
	 *
	 * @return 节点ID
	 */
	@SuppressWarnings("unchecked")
	public T getId() {
		return (T) this.get(treeNodeConfig.getIdKey());
	}

	/**
	 * 设置节点ID
	 *
	 * @param id 节点ID
	 * @return this
	 */
	public Tree<T> setId(T id) {
		this.put(treeNodeConfig.getIdKey(), id);
		return this;
	}

	/**
	 * 获取父节点ID
	 *
	 * @return 父节点ID
	 */
	@SuppressWarnings("unchecked")
	public T getParentId() {
		return (T) this.get(treeNodeConfig.getParentIdKey());
	}

	public Tree<T> setParentId(T parentId) {
		this.put(treeNodeConfig.getParentIdKey(), parentId);
		return this;
	}

	@SuppressWarnings("unchecked")
	public T getName() {
		return (T) this.get(treeNodeConfig.getNameKey());
	}

	public Tree<T> setName(Object name) {
		this.put(treeNodeConfig.getNameKey(), name);
		return this;
	}

	public Comparable<?> getWeight() {
		return (Comparable<?>) this.get(treeNodeConfig.getWeightKey());
	}

	public Tree<T> setWeight(Comparable<?> weight) {
		this.put(treeNodeConfig.getWeightKey(), weight);
		return this;
	}

	@SuppressWarnings("unchecked")
	public List<Tree<T>> getChildren() {
		return (List<Tree<T>>) this.get(treeNodeConfig.getChildrenKey());
	}

	public void setChildren(List<Tree<T>> children) {
		this.put(treeNodeConfig.getChildrenKey(), children);
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

	@SuppressWarnings({"rawtypes", "unchecked", "NullableProblems"})
	@Override
	public int compareTo(Tree<T> tree) {
		final Comparable weight = this.getWeight();
		if (null != weight) {
			final Comparable weightOther = tree.getWeight();
			return weight.compareTo(weightOther);
		}
		return 0;
	}
}