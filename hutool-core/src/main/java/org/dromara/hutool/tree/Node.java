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

package org.dromara.hutool.tree;

import org.dromara.hutool.comparator.CompareUtil;

import java.io.Serializable;

/**
 * 节点接口，提供节点相关的的方法定义
 *
 * @param <T> ID类型
 * @author looly
 * @since 5.2.4
 */
public interface Node<T> extends Comparable<Node<T>>, Serializable {

	/**
	 * 获取ID
	 *
	 * @return ID
	 */
	T getId();

	/**
	 * 设置ID
	 *
	 * @param id ID
	 * @return this
	 */
	Node<T> setId(T id);

	/**
	 * 获取父节点ID
	 *
	 * @return 父节点ID
	 */
	T getParentId();

	/**
	 * 设置父节点ID
	 *
	 * @param parentId 父节点ID
	 * @return this
	 */
	Node<T> setParentId(T parentId);

	/**
	 * 获取节点标签名称
	 *
	 * @return 节点标签名称
	 */
	CharSequence getName();

	/**
	 * 设置节点标签名称
	 *
	 * @param name 节点标签名称
	 * @return this
	 */
	Node<T> setName(CharSequence name);

	/**
	 * 获取权重
	 *
	 * @return 权重
	 */
	Comparable<?> getWeight();

	/**
	 * 设置权重
	 *
	 * @param weight 权重
	 * @return this
	 */
	Node<T> setWeight(Comparable<?> weight);

	@SuppressWarnings({"unchecked", "rawtypes", "NullableProblems"})
	@Override
	default int compareTo(final Node node) {
		if(null == node){
			return 1;
		}
		final Comparable weight = this.getWeight();
		final Comparable weightOther = node.getWeight();
		return CompareUtil.compare(weight, weightOther);
	}
}
