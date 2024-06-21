/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.collection;

import java.util.Collection;

/**
 * 有边界限制的集合，边界集合有最大容量限制
 *
 * @param <E> 元素类型
 * @since 6.0.0
 */
public interface BoundedCollection<E> extends Collection<E> {

	/**
	 * 是否已满，如果集合已满，不允许新增元素
	 *
	 * @return 是否已满
	 */
	boolean isFull();

	/**
	 * 获取集合最大允许容量
	 *
	 * @return 容量
	 */
	int maxSize();

}
