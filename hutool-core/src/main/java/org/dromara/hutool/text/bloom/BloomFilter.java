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

package org.dromara.hutool.text.bloom;

import java.io.Serializable;

/**
 * Bloom filter 是由 Howard Bloom 在 1970 年提出的二进制向量数据结构，它具有很好的空间和时间效率，被用来检测一个元素是不是集合中的一个成员。<br>
 * 如果检测结果为是，该元素不一定在集合中；但如果检测结果为否，该元素一定不在集合中。<br>
 * 因此Bloom filter具有100%的召回率。这样每个检测请求返回有“在集合内（可能错误）”和“不在集合内（绝对不在集合内）”两种情况。<br>
 * @author Looly
 *
 */
public interface BloomFilter extends Serializable{

	/**
	 *
	 * @param str 字符串
	 * @return 判断一个字符串是否bitMap中存在
	 */
	boolean contains(String str);

	/**
	 * 在boolean的bitMap中增加一个字符串<br>
	 * 如果存在就返回{@code false} .如果不存在.先增加这个字符串.再返回{@code true}
	 *
	 * @param str 字符串
	 * @return 是否加入成功，如果存在就返回{@code false} .如果不存在返回{@code true}
	 */
	boolean add(String str);
}
