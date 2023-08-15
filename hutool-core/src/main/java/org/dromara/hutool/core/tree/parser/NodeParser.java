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

