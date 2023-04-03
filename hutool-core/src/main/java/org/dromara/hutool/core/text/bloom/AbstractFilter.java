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
