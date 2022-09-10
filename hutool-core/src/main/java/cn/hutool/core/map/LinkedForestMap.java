package cn.hutool.core.map;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * {@link ForestMap}的基本实现。
 *
 * <p>该集合可以被视为以{@link TreeEntryNode#getKey()}作为key，{@link TreeEntryNode}实例作为value的{@link LinkedHashMap}。<br>
 * 使用时，将每一对键与值对视为一个{@link TreeEntryNode}节点，节点的id即为{@link TreeEntryNode#getKey()}，
 * 任何情况下使用相同的key都将会访问到同一个节点。<br>
 *
 * <p>节点通过key形成父子关系，并最终构成多叉树结构，多组平行的多叉树将在当前集合中构成森林。
 * 使用者可以通过{@link ForestMap}本身的方法来对森林进行操作或访问，
 * 也可以在获取到{@link TreeEntry}后，使用节点本身的方法对数进行操作或访问。
 *
 * @param <K> key类型
 * @author huangchengxing
 */
public class LinkedForestMap<K, V> implements ForestMap<K, V> {

	/**
	 * 节点集合
	 */
	private final Map<K, TreeEntryNode<K, V>> nodes;

	/**
	 * 当指定节点已经与其他节点构成了父子关系，是否允许将该节点的父节点强制替换为指定节点
	 */
	private final boolean allowOverrideParent;

	/**
	 * 构建{@link LinkedForestMap}
	 *
	 * @param allowOverrideParent 当指定节点已经与其他节点构成了父子关系，是否允许将该节点的父节点强制替换为指定节点
	 */
	public LinkedForestMap(boolean allowOverrideParent) {
		this.allowOverrideParent = allowOverrideParent;
		this.nodes = new LinkedHashMap<>();
	}

	// ====================== Map接口实现 ======================

	/**
	 * 获取当前实例中的节点个数
	 *
	 * @return 节点个数
	 */
	@Override
	public int size() {
		return nodes.size();
	}

	/**
	 * 当前实例是否为空
	 *
	 * @return 是否
	 */
	@Override
	public boolean isEmpty() {
		return nodes.isEmpty();
	}

	/**
	 * 当前实例中是否存在key对应的节点
	 *
	 * @param key key
	 * @return 是否
	 */
	@Override
	public boolean containsKey(Object key) {
		return nodes.containsKey(key);
	}

	/**
	 * 当前实例中是否存在对应的{@link TreeEntry}实例
	 *
	 * @param value {@link TreeEntry}实例
	 * @return 是否
	 */
	@Override
	public boolean containsValue(Object value) {
		return nodes.containsValue(value);
	}

	/**
	 * 获取key对应的节点
	 *
	 * @param key key
	 * @return 节点
	 */
	@Override
	public TreeEntry<K, V> get(Object key) {
		return nodes.get(key);
	}

	/**
	 * 将指定节点从当前{@link Map}中删除
	 * <ul>
	 *     <li>若存在父节点或子节点，则将其断开其与父节点或子节点的引用关系；</li>
	 *     <li>
	 *         若同时存在父节点或子节点，则会在删除后将让子节点直接成为父节点的子节点，比如：<br>
	 *         现有引用关系 a -&gt; b -&gt; c，删除 b 后，将有 a -&gt; c
	 *     </li>
	 * </ul>
	 *
	 * @param key 节点的key
	 * @return 删除的且引用关系已经改变的节点，若key没有对应节点，则返回null
	 */
	@Override
	public TreeEntry<K, V> remove(Object key) {
		final TreeEntryNode<K, V> target = nodes.remove(key);
		if (ObjectUtil.isNull(target)) {
			return null;
		}
		// 若存在父节点：
		// 1.将该目标从父节点的子节点中移除
		// 2.将目标的子节点直接将目标的父节点作为父节点
		if (target.hasParent()) {
			final TreeEntryNode<K, V> parent = target.getDeclaredParent();
			final Map<K, TreeEntry<K, V>> targetChildren = target.getChildren();
			parent.removeDeclaredChild(target.getKey());
			target.clear();
			targetChildren.forEach((k, c) -> parent.addChild((TreeEntryNode<K, V>) c));
		}
		return target;
	}

	/**
	 * 将当前集合清空，并清除全部节点间的引用关系
	 */
	@Override
	public void clear() {
		nodes.values().forEach(TreeEntryNode::clear);
		nodes.clear();
	}

	/**
	 * 返回当前实例中全部的key组成的{@link Set}集合
	 *
	 * @return 集合
	 */
	@Override
	public Set<K> keySet() {
		return nodes.keySet();
	}

	/**
	 * 返回当前实例中全部{@link TreeEntry}组成的{@link Collection}集合
	 *
	 * @return 集合
	 */
	@Override
	public Collection<TreeEntry<K, V>> values() {
		return new ArrayList<>(nodes.values());
	}

	/**
	 * 由key与{@link TreeEntry}组成的键值对实体的{@link Set}集合。
	 * 注意，返回集合中{@link Map.Entry#setValue(Object)}不支持调用。
	 *
	 * @return 集合
	 */
	@Override
	public Set<Map.Entry<K, TreeEntry<K, V>>> entrySet() {
		return nodes.entrySet().stream()
				.map(this::wrap)
				.collect(Collectors.toSet());
	}

	/**
	 * 将{@link TreeEntryNode}包装为{@link EntryNodeWrapper}
	 */
	private Map.Entry<K, TreeEntry<K, V>> wrap(Map.Entry<K, TreeEntryNode<K, V>> nodeEntry) {
		return new EntryNodeWrapper<>(nodeEntry.getValue());
	}

	// ====================== ForestMap接口实现 ======================

	/**
	 * 添加一个节点
	 * <ul>
	 *     <li>若key对应节点不存在，则以传入的键值创建一个新的节点；</li>
	 *     <li>若key对应节点存在，则将该节点的值替换为{@code node}指定的值；</li>
	 * </ul>
	 *
	 * @param key   节点的key
	 * @param value 节点的value
	 * @return 节点，若key已有对应节点，则返回具有旧值的节点，否则返回null
	 */
	@Override
	public TreeEntryNode<K, V> putNode(K key, V value) {
		TreeEntryNode<K, V> target = nodes.get(key);
		if (ObjectUtil.isNotNull(target)) {
			final V oldVal = target.getValue();
			target.setValue(value);
			return target.copy(oldVal);
		}
		target = new TreeEntryNode<>(null, key, value);
		nodes.put(key, target);
		return null;
	}

	/**
	 * 同时添加父子节点：
	 * <ul>
	 *     <li>若{@code parentKey}或{@code childKey}对应的节点不存在，则会根据键值创建一个对应的节点；</li>
	 *     <li>若{@code parentKey}或{@code childKey}对应的节点存在，则会更新对应节点的值；</li>
	 * </ul>
	 * 该操作等同于：
	 * <pre>
	 *     TreeEntry&lt;K, V&gt;  parent = putNode(parentKey, parentValue);
	 *     TreeEntry&lt;K, V&gt;  child = putNode(childKey, childValue);
	 *     linkNodes(parentKey, childKey);
	 * </pre>
	 *
	 * @param parentKey   父节点的key
	 * @param parentValue 父节点的value
	 * @param childKey    子节点的key
	 * @param childValue  子节点的值
	 */
	@Override
	public void putLinkedNodes(K parentKey, V parentValue, K childKey, V childValue) {
		linkNodes(parentKey, childKey, (parent, child) -> {
			parent.setValue(parentValue);
			child.setValue(childValue);
		});
	}

	/**
	 * 添加子节点，并为子节点指定父节点：
	 * <ul>
	 *     <li>若{@code parentKey}或{@code childKey}对应的节点不存在，则会根据键值创建一个对应的节点；</li>
	 *     <li>若{@code parentKey}或{@code childKey}对应的节点存在，则会更新对应节点的值；</li>
	 * </ul>
	 *
	 * @param parentKey  父节点的key
	 * @param childKey   子节点的key
	 * @param childValue 子节点的值
	 */
	@Override
	public void putLinkedNodes(K parentKey, K childKey, V childValue) {
		linkNodes(parentKey, childKey, (parent, child) -> child.setValue(childValue));
	}

	/**
	 * 为指定的节点建立父子关系，若{@code parentKey}或{@code childKey}对应节点不存在，则会创建一个对应的值为null的空节点
	 *
	 * @param parentKey 父节点的key
	 * @param childKey  子节点的key
	 * @param consumer  对父节点和子节点的操作，允许为null
	 */
	@Override
	public void linkNodes(K parentKey, K childKey, BiConsumer<TreeEntry<K, V>, TreeEntry<K, V>> consumer) {
		consumer = ObjectUtil.defaultIfNull(consumer, (parent, child) -> {
		});
		final TreeEntryNode<K, V> parentNode = nodes.computeIfAbsent(parentKey, t -> new TreeEntryNode<>(null, t));
		TreeEntryNode<K, V> childNode = nodes.get(childKey);

		// 1.子节点不存在
		if (ObjectUtil.isNull(childNode)) {
			childNode = new TreeEntryNode<>(parentNode, childKey);
			consumer.accept(parentNode, childNode);
			nodes.put(childKey, childNode);
			return;
		}

		// 2.子节点存在，且已经是该父节点的子节点了
		if (ObjectUtil.equals(parentNode, childNode.getDeclaredParent())) {
			consumer.accept(parentNode, childNode);
			return;
		}

		// 3.子节点存在，但是未与其他节点构成父子关系
		if (false == childNode.hasParent()) {
			parentNode.addChild(childNode);
		}
		// 4.子节点存在，且已经与其他节点构成父子关系，但是允许子节点直接修改其父节点
		else if (allowOverrideParent) {
			childNode.getDeclaredParent().removeDeclaredChild(childNode.getKey());
			parentNode.addChild(childNode);
		}
		// 5.子节点存在，且已经与其他节点构成父子关系，但是不允许子节点直接修改其父节点
		else {
			throw new IllegalArgumentException(StrUtil.format(
					"[{}] has been used as child of [{}], can not be overwrite as child of [{}]",
					childNode.getKey(), childNode.getDeclaredParent().getKey(), parentKey
			));
		}
		consumer.accept(parentNode, childNode);
	}

	/**
	 * 移除指定父节点与其直接关联的子节点间的引用关系，但是不会将该节点从集合中删除
	 *
	 * @param parentKey 父节点的key
	 * @param childKey  子节点
	 */
	@Override
	public void unlinkNode(K parentKey, K childKey) {
		final TreeEntryNode<K, V> childNode = nodes.get(childKey);
		if (ObjectUtil.isNull(childNode)) {
			return;
		}
		if (childNode.hasParent()) {
			childNode.getDeclaredParent().removeDeclaredChild(childNode.getKey());
		}
	}

	/**
	 * 树节点
	 *
	 * @param <K> key类型
	 * @author huangchengxing
	 */
	public static class TreeEntryNode<K, V> implements TreeEntry<K, V> {

		/**
		 * 根节点
		 */
		private TreeEntryNode<K, V> root;

		/**
		 * 父节点
		 */
		private TreeEntryNode<K, V> parent;

		/**
		 * 权重，表示到根节点的距离
		 */
		private int weight;

		/**
		 * 子节点
		 */
		private final Map<K, TreeEntryNode<K, V>> children;

		/**
		 * key
		 */
		private final K key;

		/**
		 * 值
		 */
		private V value;

		/**
		 * 创建一个节点
		 *
		 * @param parent 节点的父节点
		 * @param key    节点的key
		 */
		public TreeEntryNode(TreeEntryNode<K, V> parent, K key) {
			this(parent, key, null);
		}

		/**
		 * 创建一个节点
		 *
		 * @param parent 节点的父节点
		 * @param key    节点的key
		 * @param value  节点的value
		 */
		public TreeEntryNode(TreeEntryNode<K, V> parent, K key, V value) {
			this.parent = parent;
			this.key = key;
			this.value = value;
			this.children = new LinkedHashMap<>();
			if (ObjectUtil.isNull(parent)) {
				this.root = this;
				this.weight = 0;
			} else {
				parent.addChild(this);
				this.weight = parent.weight + 1;
				this.root = parent.root;
			}
		}

		/**
		 * 获取当前节点的key
		 *
		 * @return 节点的key
		 */
		@Override
		public K getKey() {
			return key;
		}

		/**
		 * 获取当前节点与根节点的距离
		 *
		 * @return 当前节点与根节点的距离
		 */
		@Override
		public int getWeight() {
			return weight;
		}

		/**
		 * 获取节点的value
		 *
		 * @return 节点的value
		 */
		@Override
		public V getValue() {
			return value;
		}

		/**
		 * 设置节点的value
		 *
		 * @param value 节点的value
		 * @return 节点的旧value
		 */
		@Override
		public V setValue(V value) {
			final V oldVal = getValue();
			this.value = value;
			return oldVal;
		}

		// ================== 父节点的操作 ==================

		/**
		 * 从当前节点开始，向上递归当前节点的父节点
		 *
		 * @param includeCurrent 是否处理当前节点
		 * @param consumer       对节点的操作
		 * @param breakTraverse  是否终止遍历
		 * @return 遍历到的最后一个节点
		 */
		TreeEntryNode<K, V> traverseParentNodes(
				boolean includeCurrent, Consumer<TreeEntryNode<K, V>> consumer, Predicate<TreeEntryNode<K, V>> breakTraverse) {
			breakTraverse = ObjectUtil.defaultIfNull(breakTraverse, a -> n -> false);
			TreeEntryNode<K, V> curr = includeCurrent ? this : this.parent;
			while (ObjectUtil.isNotNull(curr)) {
				consumer.accept(curr);
				if (breakTraverse.test(curr)) {
					break;
				}
				curr = curr.parent;
			}
			return curr;
		}

		/**
		 * 当前节点是否为根节点
		 *
		 * @return 当前节点是否为根节点
		 */
		public boolean isRoot() {
			return getRoot() == this;
		}

		/**
		 * 获取以当前节点作为叶子节点的树结构，然后获取该树结构的根节点
		 *
		 * @return 根节点
		 */
		@Override
		public TreeEntryNode<K, V> getRoot() {
			if (ObjectUtil.isNotNull(this.root)) {
				return this.root;
			} else {
				this.root = traverseParentNodes(true, p -> {
				}, p -> !p.hasParent());
			}
			return this.root;
		}

		/**
		 * 获取当前节点直接关联的父节点
		 *
		 * @return 父节点，当节点不存在对应父节点时返回null
		 */
		@Override
		public TreeEntryNode<K, V> getDeclaredParent() {
			return parent;
		}

		/**
		 * 获取以当前节点作为叶子节点的树结构，然后获取该树结构中当前节点的指定父节点
		 *
		 * @param key 指定父节点的key
		 * @return 指定父节点，当不存在时返回null
		 */
		@Override
		public TreeEntryNode<K, V> getParent(K key) {
			return traverseParentNodes(false, p -> {
			}, p -> p.equalsKey(key));
		}

		/**
		 * 获取以当前节点作为根节点的树结构，然后遍历所有节点
		 *
		 * @param includeSelf  是否处理当前节点
		 * @param nodeConsumer 对节点的处理
		 */
		@Override
		public void forEachChild(boolean includeSelf, Consumer<TreeEntry<K, V>> nodeConsumer) {
			traverseChildNodes(includeSelf, (index, child) -> nodeConsumer.accept(child), null);
		}

		/**
		 * 指定key与当前节点的key是否相等
		 *
		 * @param key 要比较的key
		 * @return 是否key一致
		 */
		public boolean equalsKey(K key) {
			return ObjectUtil.equal(getKey(), key);
		}

		// ================== 子节点的操作 ==================

		/**
		 * 从当前节点开始，按广度优先向下遍历当前节点的所有子节点
		 *
		 * @param includeCurrent 是否包含当前节点
		 * @param consumer       对节点与节点和当前节点的距离的操作，当{code includeCurrent}为false时下标从1开始，否则从0开始
		 * @param breakTraverse  是否终止遍历，为null时默认总是返回{@code true}
		 * @return 遍历到的最后一个节点
		 */
		TreeEntryNode<K, V> traverseChildNodes(
				boolean includeCurrent, BiConsumer<Integer, TreeEntryNode<K, V>> consumer, BiPredicate<Integer, TreeEntryNode<K, V>> breakTraverse) {
			breakTraverse = ObjectUtil.defaultIfNull(breakTraverse, (i, n) -> false);
			final Deque<List<TreeEntryNode<K, V>>> keyNodeDeque = CollUtil.newLinkedList(CollUtil.newArrayList(this));
			boolean needProcess = includeCurrent;
			int index = includeCurrent ? 0 : 1;
			TreeEntryNode<K, V> lastNode = null;
			while (!keyNodeDeque.isEmpty()) {
				final List<TreeEntryNode<K, V>> curr = keyNodeDeque.removeFirst();
				final List<TreeEntryNode<K, V>> next = new ArrayList<>();
				for (final TreeEntryNode<K, V> node : curr) {
					if (needProcess) {
						consumer.accept(index, node);
						if (breakTraverse.test(index, node)) {
							return node;
						}
					} else {
						needProcess = true;
					}
					CollUtil.addAll(next, node.children.values());
				}
				if (!next.isEmpty()) {
					keyNodeDeque.addLast(next);
				}
				lastNode = CollUtil.getLast(next);
				index++;
			}
			return lastNode;
		}


		/**
		 * 添加子节点
		 *
		 * @param child 子节点
		 * @throws IllegalArgumentException 当要添加的子节点已经是其自身父节点时抛出
		 */
		void addChild(TreeEntryNode<K, V> child) {
			if (containsChild(child.key)) {
				return;
			}

			// 检查循环引用
			traverseParentNodes(true, s -> Assert.notEquals(
					s.key, child.key,
					"circular reference between [{}] and [{}]!",
					s.key, this.key
			), null);

			// 调整该节点的信息
			child.parent = this;
			child.traverseChildNodes(true, (i, c) -> {
				c.root = getRoot();
				c.weight = i + getWeight() + 1;
			}, null);

			// 将该节点添加为当前节点的子节点
			children.put(child.key, child);
		}

		/**
		 * 移除子节点
		 *
		 * @param key 子节点
		 */
		void removeDeclaredChild(K key) {
			final TreeEntryNode<K, V> child = children.get(key);
			if (ObjectUtil.isNull(child)) {
				return;
			}

			// 断开该节点与其父节点的关系
			this.children.remove(key);

			// 重置子节点及其下属节点的相关属性
			child.parent = null;
			child.traverseChildNodes(true, (i, c) -> {
				c.root = child;
				c.weight = i;
			}, null);
		}

		/**
		 * 获取以当前节点作为根节点的树结构，然后获取该树结构中的当前节点的指定子节点
		 *
		 * @param key 指定子节点的key
		 * @return 节点
		 */
		@Override
		public TreeEntryNode<K, V> getChild(K key) {
			return traverseChildNodes(false, (i, c) -> {
			}, (i, c) -> c.equalsKey(key));
		}

		/**
		 * 获取当前节点直接关联的子节点
		 *
		 * @return 节点
		 */
		@Override
		public Map<K, TreeEntry<K, V>> getDeclaredChildren() {
			return new LinkedHashMap<>(this.children);
		}

		/**
		 * 获取以当前节点作为根节点的树结构，然后按广度优先获取该树结构中的当前节点的全部子节点
		 *
		 * @return 节点
		 */
		@Override
		public Map<K, TreeEntry<K, V>> getChildren() {
			final Map<K, TreeEntry<K, V>> childrenMap = new LinkedHashMap<>();
			traverseChildNodes(false, (i, c) -> childrenMap.put(c.getKey(), c), null);
			return childrenMap;
		}

		/**
		 * 移除对子节点、父节点与根节点的全部引用
		 */
		void clear() {
			this.root = null;
			this.children.clear();
			this.parent = null;
		}

		/**
		 * 比较目标对象与当前{@link TreeEntry}是否相等。<br>
		 * 默认只要{@link TreeEntry#getKey()}的返回值相同，即认为两者相等
		 *
		 * @param o 目标对象
		 * @return 是否
		 */
		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || this.getClass().equals(o.getClass()) || ClassUtil.isAssignable(this.getClass(), o.getClass())) {
				return false;
			}
			final TreeEntry<?, ?> treeEntry = (TreeEntry<?, ?>) o;
			return ObjectUtil.equals(this.getKey(), treeEntry.getKey());
		}

		/**
		 * 返回当前{@link TreeEntry}的哈希值。<br>
		 * 默认总是返回{@link TreeEntry#getKey()}的哈希值
		 *
		 * @return 哈希值
		 */
		@Override
		public int hashCode() {
			return Objects.hash(getKey());
		}

		/**
		 * 复制一个当前节点
		 *
		 * @param value 复制的节点的值
		 * @return 节点
		 */
		TreeEntryNode<K, V> copy(V value) {
			TreeEntryNode<K, V> copiedNode = new TreeEntryNode<>(this.parent, this.key, ObjectUtil.defaultIfNull(value, this.value));
			copiedNode.children.putAll(children);
			return copiedNode;
		}

	}

	/**
	 * {@link java.util.Map.Entry}包装类
	 *
	 * @param <K> key类型
	 * @param <V> value类型
	 * @param <N> 包装的{@link TreeEntry}类型
	 * @see #entrySet()
	 * @see #values()
	 */
	public static class EntryNodeWrapper<K, V, N extends TreeEntry<K, V>> implements Map.Entry<K, TreeEntry<K, V>> {
		private final N entryNode;

		EntryNodeWrapper(N entryNode) {
			this.entryNode = entryNode;
		}

		@Override
		public K getKey() {
			return entryNode.getKey();
		}

		@Override
		public TreeEntry<K, V> getValue() {
			return entryNode;
		}

		@Override
		public TreeEntry<K, V> setValue(TreeEntry<K, V> value) {
			throw new UnsupportedOperationException();
		}
	}

}
