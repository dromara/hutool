/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.tree;

import org.dromara.hutool.core.comparator.CompareUtil;

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
