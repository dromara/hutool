package cn.hutool.core.lang;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

import cn.hutool.core.util.HashUtil;

/**
 * 一致性Hash算法
 * 算法详解：http://blog.csdn.net/sparkliang/article/details/5279393
 * 算法实现：https://weblogs.java.net/blog/2007/11/27/consistent-hashing
 * @author xiaoleilu
 *
 * @param <T>	节点类型
 */
public class ConsistentHash<T> {
	/** Hash计算对象，用于自定义hash算法 */
	HashFunc hashFunc;
	/** 复制的节点个数 */
	private final int numberOfReplicas;
	/** 一致性Hash环 */
	private final SortedMap<Integer, T> circle = new TreeMap<Integer, T>();
	
	/**
	 * 构造，使用Java默认的Hash算法
	 * @param numberOfReplicas 复制的节点个数，增加每个节点的复制节点有利于负载均衡
	 * @param nodes 节点对象
	 */
	public ConsistentHash(int numberOfReplicas, Collection<T> nodes) {
		this.numberOfReplicas = numberOfReplicas;
		this.hashFunc = new HashFunc() {
			
			@Override
			public Integer hash(Object key) {
				//默认使用FNV1hash算法
				return HashUtil.fnvHash(key.toString());
			}
		};
		//初始化节点
		for (T node : nodes) {
			add(node);
		}
	}

	/**
	 * 构造
	 * @param hashFunc hash算法对象
	 * @param numberOfReplicas 复制的节点个数，增加每个节点的复制节点有利于负载均衡
	 * @param nodes 节点对象
	 */
	public ConsistentHash(HashFunc hashFunc, int numberOfReplicas, Collection<T> nodes) {
		this.numberOfReplicas = numberOfReplicas;
		this.hashFunc = hashFunc;
		//初始化节点
		for (T node : nodes) {
			add(node);
		}
	}

	/**
	 * 增加节点<br>
	 * 每增加一个节点，就会在闭环上增加给定复制节点数<br>
	 * 例如复制节点数是2，则每调用此方法一次，增加两个虚拟节点，这两个节点指向同一Node
	 * 由于hash算法会调用node的toString方法，故按照toString去重
	 * @param node 节点对象
	 */
	public void add(T node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.put(hashFunc.hash(node.toString() + i), node);
		}
	}

	/**
	 * 移除节点的同时移除相应的虚拟节点
	 * @param node 节点对象
	 */
	public void remove(T node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.remove(hashFunc.hash(node.toString() + i));
		}
	}

	/**
	 * 获得一个最近的顺时针节点
	 * @param key 为给定键取Hash，取得顺时针方向上最近的一个虚拟节点对应的实际节点
	 * @return 节点对象
	 */
	public T get(Object key) {
		if (circle.isEmpty()) {
			return null;
		}
		int hash = hashFunc.hash(key);
		if (!circle.containsKey(hash)) {
			SortedMap<Integer, T> tailMap = circle.tailMap(hash);	//返回此映射的部分视图，其键大于等于 hash
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		//正好命中
		return circle.get(hash);
	}

	/**
	 * Hash算法对象，用于自定义hash算法
	 * @author xiaoleilu
	 *
	 */
	public interface HashFunc {
		public Integer hash(Object key);
	}
}