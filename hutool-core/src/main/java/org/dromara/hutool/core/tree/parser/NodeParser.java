/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.tree.parser;

import org.dromara.hutool.core.tree.MapTree;

/**
 * 树节点解析器 可以参考{@link DefaultNodeParser}
 *
 * @param <T> 转换的实体 为数据源里的对象类型
 * @param <E> ID类型
 * @author liangbaikai
 */
@FunctionalInterface
public interface NodeParser<T, E> {
	/**
	 * @param object   源数据实体
	 * @param treeNode 树节点实体
	 */
	void parse(T object, MapTree<E> treeNode);
}

