package cn.hutool.core.map;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 基于多个{@link TreeEntry}构成的、彼此平行的树结构构成的森林集合。
 *
 * @param <K> key类型
 * @param <V> value类型
 * @author huangchengxing
 * @see TreeEntry
 */
public interface ForestMap<K, V> extends Map<K, TreeEntry<K, V>> {

	// ===================== Map接口方法的重定义 =====================

	/**
	 * 添加一个节点，效果等同于 {@code putNode(key, node.getValue())}
	 * <ul>
	 *     <li>若key对应节点不存在，则以传入的键值创建一个新的节点；</li>
	 *     <li>若key对应节点存在，则将该节点的值替换为{@code node}指定的值；</li>
	 * </ul>
	 *
	 * @param key  节点的key值
	 * @param node 节点
	 * @return 节点，若key已有对应节点，则返回具有旧值的节点，否则返回null
	 * @see #putNode(Object, Object)
	 */
	@Override
	default TreeEntry<K, V> put(K key, TreeEntry<K, V> node) {
		return putNode(key, node.getValue());
	}

	/**
	 * 批量添加节点，若节点具有父节点或者子节点，则一并在当前实例中引入该关系
	 *
	 * @param treeEntryMap 节点集合
	 */
	@Override
	default void putAll(Map<? extends K, ? extends TreeEntry<K, V>> treeEntryMap) {
		if (CollUtil.isEmpty(treeEntryMap)) {
			return;
		}
		treeEntryMap.forEach((k, v) -> {
			if (v.hasParent()) {
				final TreeEntry<K, V> parent = v.getDeclaredParent();
				putLinkedNodes(parent.getKey(), parent.getValue(), v.getKey(), v.getValue());
			} else {
				putNode(v.getKey(), v.getValue());
			}
		});
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
	 * @return 删除的节点，若key没有对应节点，则返回null
	 */
	@Override
	TreeEntry<K, V> remove(Object key);

	/**
	 * 将当前集合清空，并清除全部节点间的引用关系
	 */
	@Override
	void clear();

	// ===================== 节点操作 =====================

	/**
	 * 批量添加节点
	 *
	 * @param <C>                集合类型
	 * @param values             要添加的值
	 * @param keyGenerator       从值中获取key的方法
	 * @param parentKeyGenerator 从值中获取父节点key的方法
	 * @param ignoreNullNode     是否获取到的key为null的子节点/父节点
	 */
	default <C extends Collection<V>> void putAllNode(
			C values, Function<V, K> keyGenerator, Function<V, K> parentKeyGenerator, boolean ignoreNullNode) {
		if (CollUtil.isEmpty(values)) {
			return;
		}
		values.forEach(v -> {
			final K key = keyGenerator.apply(v);
			final K parentKey = parentKeyGenerator.apply(v);

			// 不忽略keu为null节点
			final boolean hasKey = ObjectUtil.isNotNull(key);
			final boolean hasParentKey = ObjectUtil.isNotNull(parentKey);
			if (!ignoreNullNode || (hasKey && hasParentKey)) {
				linkNodes(parentKey, key);
				get(key).setValue(v);
				return;
			}

			// 父子节点的key都为null
			if (!hasKey && !hasParentKey) {
				return;
			}

			// 父节点key为null
			if (hasKey) {
				putNode(key, v);
				return;
			}

			// 子节点key为null
			putNode(parentKey, null);
		});
	}

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
	TreeEntry<K, V> putNode(K key, V value);

	/**
	 * 同时添加父子节点：
	 * <ul>
	 *     <li>若{@code parentKey}或{@code childKey}对应的节点不存在，则会根据键值创建一个对应的节点；</li>
	 *     <li>若{@code parentKey}或{@code childKey}对应的节点存在，则会更新对应节点的值；</li>
	 * </ul>
	 * 该操作等同于：
	 * <pre>{@code
	 *     putNode(parentKey, parentValue);
	 *     putNode(childKey, childValue);
	 *     linkNodes(parentKey, childKey);
	 * }</pre>
	 *
	 * @param parentKey   父节点的key
	 * @param parentValue 父节点的value
	 * @param childKey    子节点的key
	 * @param childValue  子节点的值
	 */
	default void putLinkedNodes(K parentKey, V parentValue, K childKey, V childValue) {
		putNode(parentKey, parentValue);
		putNode(childKey, childValue);
		linkNodes(parentKey, childKey);
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
	void putLinkedNodes(K parentKey, K childKey, V childValue);

	/**
	 * 为集合中的指定的节点建立父子关系
	 *
	 * @param parentKey 父节点的key
	 * @param childKey  子节点的key
	 */
	default void linkNodes(K parentKey, K childKey) {
		linkNodes(parentKey, childKey, null);
	}

	/**
	 * 为集合中的指定的节点建立父子关系
	 *
	 * @param parentKey 父节点的key
	 * @param childKey  子节点的key
	 * @param consumer  对父节点和子节点的操作，允许为null
	 */
	void linkNodes(K parentKey, K childKey, BiConsumer<TreeEntry<K, V>, TreeEntry<K, V>> consumer);

	/**
	 * 若{@code parentKey}或{@code childKey}对应节点都存在，则移除指定该父节点与其直接关联的指定子节点间的引用关系
	 *
	 * @param parentKey 父节点的key
	 * @param childKey  子节点
	 */
	void unlinkNode(K parentKey, K childKey);

	// ===================== 父节点相关方法 =====================

	/**
	 * 获取指定节点所在树结构的全部树节点 <br>
	 * 比如：存在 a -&gt; b -&gt; c 的关系，则输入 a/b/c 都将返回 a, b, c
	 *
	 * @param key 指定节点的key
	 * @return 节点
	 */
	default Set<TreeEntry<K, V>> getTreeNodes(K key) {
		final TreeEntry<K, V> target = get(key);
		if (ObjectUtil.isNull(target)) {
			return Collections.emptySet();
		}
		final Set<TreeEntry<K, V>> results = CollUtil.newLinkedHashSet(target.getRoot());
		CollUtil.addAll(results, target.getRoot().getChildren().values());
		return results;
	}

	/**
	 * 获取以指定节点作为叶子节点的树结构，然后获取该树结构的根节点 <br>
	 * 比如：存在 a -&gt; b -&gt; c 的关系，则输入 a/b/c 都将返回 a
	 *
	 * @param key 指定节点的key
	 * @return 节点
	 */
	default TreeEntry<K, V> getRootNode(K key) {
		return Opt.ofNullable(get(key))
				.map(TreeEntry::getRoot)
				.orElse(null);
	}

	/**
	 * 获取指定节点的直接父节点 <br>
	 * 比如：若存在 a -&gt; b -&gt; c 的关系，此时输入 a 将返回 null，输入 b 将返回 a，输入 c 将返回 b
	 *
	 * @param key 指定节点的key
	 * @return 节点
	 */
	default TreeEntry<K, V> getDeclaredParentNode(K key) {
		return Opt.ofNullable(get(key))
				.map(TreeEntry::getDeclaredParent)
				.orElse(null);
	}

	/**
	 * 获取以指定节点作为叶子节点的树结构，然后获取该树结构中指定节点的指定父节点
	 *
	 * @param key       指定节点的key
	 * @param parentKey 指定父节点key
	 * @return 节点
	 */
	default TreeEntry<K, V> getParentNode(K key, K parentKey) {
		return Opt.ofNullable(get(key))
				.map(t -> t.getParent(parentKey))
				.orElse(null);
	}

	/**
	 * 获取以指定节点作为叶子节点的树结构，然后确认该树结构中当前节点是否存在指定父节点
	 *
	 * @param key       指定节点的key
	 * @param parentKey 指定父节点的key
	 * @return 是否
	 */
	default boolean containsParentNode(K key, K parentKey) {
		return Opt.ofNullable(get(key))
				.map(m -> m.containsParent(parentKey))
				.orElse(false);
	}

	/**
	 * 获取指定节点的值
	 *
	 * @param key 节点的key
	 * @return 节点值，若节点不存在，或节点值为null都将返回null
	 */
	default V getNodeValue(K key) {
		return Opt.ofNullable(get(key))
			.map(TreeEntry::getValue)
			.get();
	}

	// ===================== 子节点相关方法 =====================

	/**
	 * 判断以该父节点作为根节点的树结构中是否具有指定子节点
	 *
	 * @param parentKey 父节点
	 * @param childKey  子节点
	 * @return 是否
	 */
	default boolean containsChildNode(K parentKey, K childKey) {
		return Opt.ofNullable(get(parentKey))
				.map(m -> m.containsChild(childKey))
				.orElse(false);
	}

	/**
	 * 获取指定父节点直接关联的子节点 <br>
	 * 比如：若存在 a -&gt; b -&gt; c 的关系，此时输入 b 将返回 c，输入 a 将返回 b
	 *
	 * @param key key
	 * @return 节点
	 */
	default Collection<TreeEntry<K, V>> getDeclaredChildNodes(K key) {
		return Opt.ofNullable(get(key))
				.map(TreeEntry::getDeclaredChildren)
				.map(Map::values)
				.orElseGet(Collections::emptyList);
	}

	/**
	 * 获取指定父节点的全部子节点 <br>
	 * 比如：若存在 a -&gt; b -&gt; c 的关系，此时输入 b 将返回 c，输入 a 将返回 b，c
	 *
	 * @param key key
	 * @return 该节点的全部子节点
	 */
	default Collection<TreeEntry<K, V>> getChildNodes(K key) {
		return Opt.ofNullable(get(key))
				.map(TreeEntry::getChildren)
				.map(Map::values)
				.orElseGet(Collections::emptyList);
	}

}
