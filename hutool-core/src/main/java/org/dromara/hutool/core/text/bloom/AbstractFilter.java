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

package org.dromara.hutool.core.text.bloom;

import java.util.BitSet;

/**
 * 抽象Bloom过滤器
 *
 * @author looly
 */
public abstract class AbstractFilter implements BloomFilter {
	private static final long serialVersionUID = 1L;

	private final BitSet bitSet;
	/**
	 * 容量
	 */
	protected int size;


	/**
	 * 构造
	 *
	 * @param size 容量
	 */
	public AbstractFilter(final int size) {
		this.size = size;
		this.bitSet = new BitSet(size);
	}

	@Override
	public boolean contains(final String str) {
		return bitSet.get(Math.abs(hash(str)));
	}

	@Override
	public boolean add(final String str) {
		final int hash = Math.abs(hash(str));
		if (bitSet.get(hash)) {
			return false;
		}

		bitSet.set(hash);
		return true;
	}

	/**
	 * 自定义Hash方法
	 *
	 * @param str 字符串
	 * @return HashCode
	 */
	public abstract int hash(String str);
}
