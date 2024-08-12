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

package org.dromara.hutool.core.text.bloom;

/**
 * 组合BloomFilter 实现 <br>
 * 1.构建hash算法 <br>
 * 2.散列hash映射到数组的bit位置 <br>
 * 3.验证<br>
 * 此实现方式可以指定Hash算法
 *
 * @author Ansj
 */
public class CombinedBloomFilter implements BloomFilter {
	private static final long serialVersionUID = 1L;

	private final BloomFilter[] filters;

	/**
	 * 使用自定的多个过滤器建立BloomFilter
	 *
	 * @param filters Bloom过滤器列表
	 */
	public CombinedBloomFilter(final BloomFilter... filters) {
		this.filters = filters;
	}

	/**
	 * 增加字符串到Filter映射中
	 *
	 * @param str 字符串
	 */
	@Override
	public boolean add(final String str) {
		boolean flag = false;
		for (final BloomFilter filter : filters) {
			flag |= filter.add(str);
		}
		return flag;
	}

	/**
	 * 是否可能包含此字符串，此处存在误判
	 *
	 * @param str 字符串
	 * @return 是否存在
	 */
	@Override
	public boolean contains(final String str) {
		for (final BloomFilter filter : filters) {
			if (filter.contains(str) == false) {
				return false;
			}
		}
		return true;
	}
}
