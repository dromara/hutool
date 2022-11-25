package cn.hutool.core.lang.tree.lambda;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.func.Func1;

import java.util.*;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

/**
 * 树构造器
 *
 * @param <E> 树节点类型
 * @param <R> 树节点标识类型
 * @author adoph
 */
public class TreeBuilder<E, R> {

	/**
	 * 未指定根节点标识构造器
	 *
	 * @param treeNodes 原树节点集
	 */
	public TreeBuilder(List<E> treeNodes) {
		this.treeNodes = treeNodes;
	}

	/**
	 * 指定根节点标识构造器
	 *
	 * @param treeNodes 原树节点集
	 * @param rootPid   根节点标识
	 */
	public TreeBuilder(List<E> treeNodes, R rootPid) {
		this.treeNodes = treeNodes;
		this.rootPid = rootPid;
	}

	/**
	 * 原树节点集
	 */
	private final List<E> treeNodes;

	/**
	 * 根节点父标识
	 */
	private R rootPid;

	/**
	 * 获取节点标识表示func
	 */
	private Function<E, R> id;

	/**
	 * 获取父节点标识func
	 */
	private Function<E, R> pid;

	/**
	 * 获取子节点集func
	 */
	private Func1<E, List<E>> children;

	/**
	 * setChildrenMethodName
	 */
	private String setChildrenMethodName;

	/**
	 * 排序字段func，仅支持整数类型（Byte、Short、Integer、Long）
	 */
	private ToLongFunction<E> sort;

	/**
	 * 设置节点标识func
	 *
	 * @param id 获取节点标识func
	 * @return this
	 */
	public TreeBuilder<E, R> id(Function<E, R> id) {
		this.id = id;
		return this;
	}

	/**
	 * 设置节点父标识func
	 *
	 * @param pid 获取节点父标识func
	 * @return this
	 */
	public TreeBuilder<E, R> pid(Function<E, R> pid) {
		this.pid = pid;
		return this;
	}

	/**
	 * 设置子节点集func
	 *
	 * @param children 获取子节点集func
	 * @return this
	 */
	public TreeBuilder<E, R> children(Func1<E, List<E>> children) {
		this.children = children;
		return this;
	}

	/**
	 * 设置排序func
	 *
	 * @param sort 获取排序func
	 * @return this
	 */
	public TreeBuilder<E, R> sort(ToLongFunction<E> sort) {
		this.sort = sort;
		return this;
	}

	/**
	 * 开始构建树
	 *
	 * @return this
	 */
	public List<E> build() {
		Assert.notNull(treeNodes, "treeNodes is null!");
		Assert.notNull(id, "getId function is null!");
		Assert.notNull(pid, "getPid function is null!");
		Assert.notNull(children, "getChildren function is null!");

		if (treeNodes.isEmpty()) {
			return treeNodes;
		}

		// 获取设置子节点集方法名称
		setChildrenMethodName = MethodHandleUtil.requireSetMethod(children);

		if (Objects.isNull(rootPid)) {
			// 未指定根节点标识
			return doBuild();
		}

		// 指定根节点标识
		return doBuild(rootPid);
	}

	/**
	 * 指定根节点标识构建
	 *
	 * @param rootPid 根节点标识
	 * @return 树形结构集
	 */
	public List<E> doBuild(R rootPid) {
		List<E> tmpList = new ArrayList<>(treeNodes);

		// 按父节点分组
		Map<R, List<E>> treeMap = tmpList.stream().collect(Collectors.groupingBy(node -> pid.apply(node)));

		// 排序
		if (Objects.nonNull(sort)) {
			treeMap.forEach((k, v) -> v.sort(Comparator.comparingLong(sort)));
		}

		// 根节点集
		List<E> rootNodes = treeMap.get(rootPid);

		rootNodes.forEach(node -> buildChild(node, treeMap));

		return rootNodes;
	}

	/**
	 * 未指定根节点标识构建
	 *
	 * @return 树形结构集
	 */
	public List<E> doBuild() {
		List<E> tmpList = new ArrayList<>(treeNodes);
		for (E node1 : treeNodes) {
			for (E node2 : treeNodes) {
				if (!Objects.equals(id.apply(node1), id.apply(node2))) {
					if (Objects.equals(id.apply(node1), pid.apply(node2))) {
						List<E> subNodes = null;
						try {
							subNodes = children.call(node1);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (subNodes == null) {
							subNodes = new ArrayList<>();
							setChildrenByMethodHandle(node1, subNodes);
						}
						subNodes.add(node2);
						// 排序子节点
						if (Objects.nonNull(sort)) {
							subNodes.sort(Comparator.comparingLong(sort));
						}
						tmpList.remove(node2);
					}
				}
			}
		}

		// 排序根节点
		if (Objects.nonNull(sort) && tmpList.size() > 1) {
			tmpList.sort(Comparator.comparingLong(sort));
		}

		return tmpList;
	}

	/**
	 * 构建子节点集
	 *
	 * @param pNode   父节点
	 * @param treeMap 分组节点集
	 */
	private void buildChild(E pNode, Map<R, List<E>> treeMap) {
		R tmpPid = id.apply(pNode);
		List<E> tmpChildren = treeMap.get(tmpPid);
		if (Objects.nonNull(tmpChildren)) {
			setChildrenByMethodHandle(pNode, tmpChildren);
			tmpChildren.forEach(node -> buildChild(node, treeMap));
		}
	}

	/**
	 * 通过MethodHandle设置子节点集
	 *
	 * @param pNode    父节点
	 * @param children 子节点集
	 */
	private void setChildrenByMethodHandle(E pNode, List<E> children) {
		MethodHandleUtil.invokeByMethodHandle(pNode, children, setChildrenMethodName);
	}

}
