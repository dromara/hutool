/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.codec.hash;

import java.io.Serializable;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一致性Hash算法
 * 算法详解：<a href="http://blog.csdn.net/sparkliang/article/details/5279393">http://blog.csdn.net/sparkliang/article/details/5279393</a>
 * 算法实现：<a href="https://weblogs.java.net/blog/2007/11/27/consistent-hashing">https://weblogs.java.net/blog/2007/11/27/consistent-hashing</a>
 *
 * @param <T> 节点类型
 * @author Looly
 */
public class ConsistentHash<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Hash计算对象，用于自定义hash算法
	 */
	Hash32<Object> hashFunc;
	/**
	 * 复制的节点个数
	 */
	private final int numberOfReplicas;
	/**
	 * 一致性Hash环
	 */
	private final SortedMap<Integer, T> circle = new TreeMap<>();

	/**
	 * 构造，使用Java默认的Hash算法
	 *
	 * @param numberOfReplicas 复制的节点个数，增加每个节点的复制节点有利于负载均衡
	 * @param nodes            节点对象
	 */
	public ConsistentHash(final int numberOfReplicas, final Collection<T> nodes) {
		this.numberOfReplicas = numberOfReplicas;
		this.hashFunc = key -> {
			//默认使用FNV1hash算法
			return HashUtil.fnvHash(key.toString());
		};
		//初始化节点
		for (final T node : nodes) {
			add(node);
		}
	}

	/**
	 * 构造
	 *
	 * @param hashFunc         hash算法对象
	 * @param numberOfReplicas 复制的节点个数，增加每个节点的复制节点有利于负载均衡
	 * @param nodes            节点对象
	 */
	public ConsistentHash(final Hash32<Object> hashFunc, final int numberOfReplicas, final Collection<T> nodes) {
		this.numberOfReplicas = numberOfReplicas;
		this.hashFunc = hashFunc;
		//初始化节点
		for (final T node : nodes) {
			add(node);
		}
	}

	/**
	 * 增加节点<br>
	 * 每增加一个节点，就会在闭环上增加给定复制节点数<br>
	 * 例如复制节点数是2，则每调用此方法一次，增加两个虚拟节点，这两个节点指向同一Node
	 * 由于hash算法会调用node的toString方法，故按照toString去重
	 *
	 * @param node 节点对象
	 */
	public void add(final T node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.put(hashFunc.hash32(node.toString() + i), node);
		}
	}

	/**
	 * 移除节点的同时移除相应的虚拟节点
	 *
	 * @param node 节点对象
	 */
	public void remove(final T node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.remove(hashFunc.hash32(node.toString() + i));
		}
	}

	/**
	 * 获得一个最近的顺时针节点
	 *
	 * @param key 为给定键取Hash，取得顺时针方向上最近的一个虚拟节点对应的实际节点
	 * @return 节点对象
	 */
	public T get(final Object key) {
		if (circle.isEmpty()) {
			return null;
		}
		int hash = hashFunc.hash32(key);
		if (!circle.containsKey(hash)) {
			final SortedMap<Integer, T> tailMap = circle.tailMap(hash);    //返回此映射的部分视图，其键大于等于 hash
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		//正好命中
		return circle.get(hash);
	}
}
