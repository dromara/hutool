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

package org.dromara.hutool.core.collection.partition;

import java.util.List;
import java.util.RandomAccess;

/**
 * 列表分区或分段（可随机访问列表）<br>
 * 通过传入分区长度，将指定列表分区为不同的块，每块区域的长度相同（最后一块可能小于长度）<br>
 * 分区是在原List的基础上进行的，返回的分区是不可变的抽象列表，原列表元素变更，分区中元素也会变更。
 * 参考：Guava的Lists#RandomAccessPartition
 *
 * @param <T> 元素类型
 * @author looly, guava
 * @since 5.7.10
 */
public class RandomAccessPartition<T> extends Partition<T> implements RandomAccess {

	/**
	 * 构造
	 *
	 * @param list 被分区的列表，必须实现{@link RandomAccess}
	 * @param size 每个分区的长度
	 */
	public RandomAccessPartition(final List<T> list, final int size) {
		super(list, size);
	}
}
