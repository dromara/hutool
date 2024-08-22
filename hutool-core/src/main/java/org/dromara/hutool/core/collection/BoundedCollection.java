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
