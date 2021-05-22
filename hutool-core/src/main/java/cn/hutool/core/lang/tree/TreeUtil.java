package cn.hutool.core.lang.tree;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.parser.DefaultNodeParser;
import cn.hutool.core.lang.tree.parser.NodeParser;
import cn.hutool.core.util.ObjectUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 树工具类
 *
 * @author liangbaikai
 * @author Pluto-Whong
 */
public class TreeUtil {

	/**
	 * 树构建
	 *
	 * @param list 源数据集合
	 * @return List
	 */
	public static List<Tree<Integer>> build(List<TreeNode<Integer>> list) {
		return build(list, 0);
	}

	/**
	 * 树构建
	 *
	 * @param <E>      ID类型
	 * @param list     源数据集合
	 * @param parentId 最顶层父id值 一般为 0 之类
	 * @return List
	 */
	public static <E> List<Tree<E>> build(List<TreeNode<E>> list, E parentId) {
		return build(list, parentId, TreeNodeConfig.DEFAULT_CONFIG, new DefaultNodeParser<>());
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
	public static <T, E> List<Tree<E>> build(List<T> list, E parentId, NodeParser<T, E> nodeParser) {
		return build(list, parentId, TreeNodeConfig.DEFAULT_CONFIG, nodeParser);
	}

	/**
	 * 树构建
	 *
	 * @param <T>            转换的实体 为数据源里的对象类型
	 * @param <E>            ID类型
	 * @param list           源数据集合
	 * @param parentId       最顶层父id值 一般为 0 之类
	 * @param treeNodeConfig 配置
	 * @param nodeParser     转换器
	 * @return List
	 */
	public static <T, E> List<Tree<E>> build(List<T> list, E parentId, TreeNodeConfig treeNodeConfig,
			NodeParser<T, E> nodeParser) {
		final List<Tree<E>> treeList = CollUtil.newArrayList();
		Tree<E> tree;
		for (T obj : list) {
			tree = new Tree<>(treeNodeConfig);
			nodeParser.parse(obj, tree);
			treeList.add(tree);
		}

		return build(treeList,
				// 获取ID、父ID，增加孩子节点
				Tree::getId, Tree::getParentId, Tree::addChildren,
				// 排序器
				generateSortConsumer(Tree::getChildren),
				// 判断是否为根节点
				node -> Objects.equals(node.getParentId(), parentId),
				// 最大深度、设置父节点
				Objects.isNull(treeNodeConfig.getDeep()) ? 0 : treeNodeConfig.getDeep(), Tree::setParent);
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
	public static <T> Tree<T> getNode(Tree<T> node, T id) {
		if (ObjectUtil.equal(id, node.getId())) {
			return node;
		}

		final List<Tree<T>> children = node.getChildren();
		if (null == children) {
			return null;
		}

		// 查找子节点
		Tree<T> childNode;
		for (Tree<T> child : children) {
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
	public static <T> List<CharSequence> getParentsName(Tree<T> node, boolean includeCurrentNode) {
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

	// =================入参模型与反参模型一致的树构造器=======================

	private static class Pair<T> {
		final T element;
		boolean mark;
		int deep;

		private Pair(T element, boolean mark, int deep) {
			this.element = element;
			this.mark = mark;
			this.deep = deep;
		}

		static <T> Pair<T> of(T element, boolean mark, int deep) {
			return new Pair<>(element, mark, deep);
		}
	}

	/**
	 * 生成常规的树同层比较器
	 *
	 * @param <T>           模型
	 * @param comparator    比较器
	 * @param childFunction 获取孩子链表
	 * @return 比较器
	 */
	public static <T> Consumer<List<T>> generateSortConsumer(Comparator<T> comparator,
			Function<T, List<T>> childFunction) {
		return new Consumer<List<T>>() {
			@Override
			public void accept(List<T> list) {
				Collections.sort(list, comparator);
				for (T model : list) {
					List<T> childList = childFunction.apply(model);
					if (Objects.nonNull(childList) && !childList.isEmpty()) {
						this.accept(childFunction.apply(model));
					}
				}
			}
		};
	}

	/**
	 * 生成常规的树同层比较器
	 *
	 * @param <T>           extends Comparable<? super T>
	 * @param childFunction 获取孩子链表
	 * @return 比较器
	 */
	public static <T extends Comparable<? super T>> Consumer<List<T>> generateSortConsumer(
			Function<T, List<T>> childFunction) {
		return new Consumer<List<T>>() {
			@Override
			public void accept(List<T> list) {
				Collections.sort(list);
				for (T model : list) {
					List<T> childList = childFunction.apply(model);
					if (Objects.nonNull(childList) && !childList.isEmpty()) {
						this.accept(childFunction.apply(model));
					}
				}
			}
		};
	}

	/**
	 * 将集合内数据组装成树形结构，并按入参模型进行返回<br>
	 * 注意不得有重复数据，若有则存在丢失的可能<br>
	 * 无排序
	 *
	 * @param <T>               模型
	 * @param <K>               key类型
	 * @param elements          数据
	 * @param keyFunction       获取key的方法
	 * @param parentKeyFunction 获取父key的方法
	 * @param addChildConsumer  向模型添加孩子的方法
	 * @return {@link ArrayList}
	 */
	public static <T, K> List<T> build(Collection<T> elements, Function<T, K> keyFunction,
			Function<T, K> parentKeyFunction, BiConsumer<T, T> addChildConsumer) {
		return build(elements, keyFunction, parentKeyFunction, addChildConsumer, null);
	}

	/**
	 * 将集合内数据组装成树形结构，并按入参模型进行返回<br>
	 * 注意不得有重复数据，若有则存在丢失的可能<br>
	 * 排序
	 *
	 * @param <T>               模型
	 * @param <K>               key类型
	 * @param elements          数据
	 * @param keyFunction       获取key的方法
	 * @param parentKeyFunction 获取父key的方法
	 * @param addChildConsumer  向模型添加孩子的方法
	 * @param sortConsumer      排序方法，可通过 {@link #generateSortConsumer(Function)} 或
	 *                          {@link #generateSortConsumer(Comparator, Function)}
	 *                          方式快速成功，但是孩子节点必须要用list保存
	 * @return {@link ArrayList}
	 */
	public static <T, K> List<T> build(Collection<T> elements, Function<T, K> keyFunction,
			Function<T, K> parentKeyFunction, BiConsumer<T, T> addChildConsumer, Consumer<List<T>> sortConsumer) {
		return build(elements, keyFunction, parentKeyFunction, addChildConsumer, sortConsumer, null);
	}

	/**
	 * 将集合内数据组装成树形结构，并按入参模型进行返回<br>
	 * 注意不得有重复数据，若有则存在丢失的可能<br>
	 * 指定根节点判断依据
	 *
	 * @param <T>               模型
	 * @param <K>               key类型
	 * @param elements          数据
	 * @param keyFunction       获取key的方法
	 * @param parentKeyFunction 获取父key的方法
	 * @param addChildConsumer  向模型添加孩子的方法
	 * @param sortConsumer      排序方法，可通过 {@link #generateSortConsumer(Function)} 或
	 *                          {@link #generateSortConsumer(Comparator, Function)}
	 *                          方式快速成功，但是孩子节点必须要用list保存
	 * @param isRootFunction    判断是否是根节点方法，若为null，则表示直到找不到上层便为根节点
	 * @return {@link ArrayList}
	 */
	public static <T, K> List<T> build(Collection<T> elements, Function<T, K> keyFunction,
			Function<T, K> parentKeyFunction, BiConsumer<T, T> addChildConsumer, Consumer<List<T>> sortConsumer,
			Function<T, Boolean> isRootFunction) {
		return build(elements, keyFunction, parentKeyFunction, addChildConsumer, sortConsumer, isRootFunction, 0);
	}

	/**
	 * 将集合内数据组装成树形结构，并按入参模型进行返回<br>
	 * 注意不得有重复数据，若有则存在丢失的可能<br>
	 * 指定深度
	 *
	 * @param <T>               模型
	 * @param <K>               key类型
	 * @param elements          数据
	 * @param keyFunction       获取key的方法
	 * @param parentKeyFunction 获取父key的方法
	 * @param addChildConsumer  向模型添加孩子的方法
	 * @param sortConsumer      排序方法，可通过 {@link #generateSortConsumer(Function)} 或
	 *                          {@link #generateSortConsumer(Comparator, Function)}
	 *                          方式快速成功，但是孩子节点必须要用list保存
	 * @param deep              最大深度，若 <=0 则表示无限深（不进行截取）
	 * @return {@link ArrayList}
	 */
	public static <T, K> List<T> build(Collection<T> elements, Function<T, K> keyFunction,
			Function<T, K> parentKeyFunction, BiConsumer<T, T> addChildConsumer, Consumer<List<T>> sortConsumer,
			int deep) {
		return build(elements, keyFunction, parentKeyFunction, addChildConsumer, sortConsumer, null, deep);
	}

	/**
	 * 将集合内数据组装成树形结构，并按入参模型进行返回<br>
	 * 注意不得有重复数据，若有则存在丢失的可能
	 *
	 * @param <T>               模型
	 * @param <K>               key类型
	 * @param elements          数据
	 * @param keyFunction       获取key的方法
	 * @param parentKeyFunction 获取父key的方法
	 * @param addChildConsumer  向模型添加孩子的方法
	 * @param sortConsumer      排序方法，可通过 {@link #generateSortConsumer(Function)} 或
	 *                          {@link #generateSortConsumer(Comparator, Function)}
	 *                          方式快速成功，但是孩子节点必须要用list保存
	 * @param isRootFunction    判断是否是根节点方法，若为null，则表示直到找不到上层便为根节点
	 * @param deep              最大深度，若 <=0 则表示无限深（不进行截取）
	 * @return {@link ArrayList}
	 */
	public static <T, K> List<T> build(Collection<T> elements, Function<T, K> keyFunction,
			Function<T, K> parentKeyFunction, BiConsumer<T, T> addChildConsumer, Consumer<List<T>> sortConsumer,
			Function<T, Boolean> isRootFunction, int deep) {
		return build(elements, keyFunction, parentKeyFunction, addChildConsumer, sortConsumer, null, deep, null);
	}

	/**
	 * 将集合内数据组装成树形结构，并按入参模型进行返回<br>
	 * 注意不得有重复数据，若有则存在丢失的可能<br>
	 * 对子节点设置父节点
	 *
	 * @param <T>               模型
	 * @param <K>               key类型
	 * @param elements          数据
	 * @param keyFunction       获取key的方法
	 * @param parentKeyFunction 获取父key的方法
	 * @param addChildConsumer  向模型添加孩子的方法
	 * @param sortConsumer      排序方法，可通过 {@link #generateSortConsumer(Function)} 或
	 *                          {@link #generateSortConsumer(Comparator, Function)}
	 *                          方式快速成功，但是孩子节点必须要用list保存
	 * @param isRootFunction    判断是否是根节点方法，若为null，则表示直到找不到上层便为根节点
	 * @param deep              最大深度，若 <=0 则表示无限深（不进行截取）
	 * @param setParentConsumer 对子节点设置父节点，注意做序列化处理时会发生循环引用，谨慎使用
	 * @return {@link ArrayList}
	 */
	public static <T, K> List<T> build(Collection<T> elements, Function<T, K> keyFunction,
			Function<T, K> parentKeyFunction, BiConsumer<T, T> addChildConsumer, Consumer<List<T>> sortConsumer,
			Function<T, Boolean> isRootFunction, int deep, BiConsumer<T, T> setParentConsumer) {
		if (Objects.isNull(elements) || elements.isEmpty()) {
			return Collections.emptyList();
		}

		// 元素map，将所有元素转换为hashMap，这样可以将查找复杂度近似变为O(1)
		final HashMap<K, Pair<T>> elementMap = new HashMap<>(elements.size());

		for (T element : elements) {
			K key = keyFunction.apply(element);
			elementMap.put(key, Pair.of(element, false, 0));
		}

		// 根元素，在最终组装完结后，将最后的元素加入到子列表中
		final List<T> rootList = new ArrayList<>();

		// 检查元素回环的方法，如果本次“倒桩”组成时，发现已经存在过则说明发生了回环
		HashSet<K> alreadySet = new HashSet<>();
		for (Map.Entry<K, Pair<T>> entry : elementMap.entrySet()) {
			final K key = entry.getKey();
			final Pair<T> pair = entry.getValue();

			// 如果用过了，就不再查找了
			if (pair.mark) {
				continue;
			}

			// 每次都要清空，因为每次“倒桩”组成树都是一次独立的枝条
			alreadySet.clear();
			alreadySet.add(key);

			Deque<Pair<T>> stack = new LinkedList<>();
			stack.push(pair);

			// 在寻找的路径中，某个元素被使用过，说明前面已经寻找过
			for (Pair<T> currentPair = pair; !currentPair.mark;) {
				currentPair.mark = true;

				if (Objects.nonNull(isRootFunction) && isRootFunction.apply(currentPair.element)) {
					rootList.add(currentPair.element);
					break;
				}

				// 获取父节点
				final K parentKey = parentKeyFunction.apply(currentPair.element);
				final Pair<T> parentPair = elementMap.get(parentKey);

				// 如果不存在父节点，则认为是根节点
				if (Objects.isNull(parentPair)) {
					// 如果有了根节点判断依据，则不加入根节点列表
					if (Objects.isNull(isRootFunction)) {
						rootList.add(currentPair.element);
					}
					break;
				}

				// 如果已经被加入过，则说明存在回环，存在 1->2->1 的问题，因为parent可能被使用过
				if (alreadySet.contains(parentKey)) {
//					throw new IllegalStateException("has loopback！loopback key is [" + parentKey + "]");
					// 直接break，这样不会有从根节点跟踪下来的路径，等价于直接抛弃，与原 TreeUtil 效果一致
					break;
				}
				alreadySet.add(parentKey);

				stack.push(parentPair);

				// 继续寻找上层元素
				currentPair = parentPair;
			}

			// 从上而下加入子节点
			{
				Pair<T> parentPair = stack.pop();
				while (!stack.isEmpty()) {
					Pair<T> currentPair = stack.pop();

					currentPair.deep = parentPair.deep + 1;
					if (deep > 0 && currentPair.deep >= deep) {
						// 后面的不管怎么加，也已经从当前截断了，从而可以做剪枝
						break;
					}
					addChildConsumer.accept(parentPair.element, currentPair.element);

					if (Objects.nonNull(setParentConsumer)) {
						setParentConsumer.accept(currentPair.element, parentPair.element);
					}

					parentPair = currentPair;
				}
			}

		}

		if (Objects.nonNull(sortConsumer)) {
			sortConsumer.accept(rootList);
		}

		return rootList;
	}

}
