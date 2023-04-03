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

import java.util.function.Function;

/**
 * 基于Hash函数方法的{@link BloomFilter}
 *
 * @author looly
 * @since 5.8.0
 */
public class FuncFilter extends AbstractFilter {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建FuncFilter
	 *
	 * @param size     最大值
	 * @param hashFunc Hash函数
	 * @return FuncFilter
	 */
	public static FuncFilter of(final int size, final Function<String, Number> hashFunc) {
		return new FuncFilter(size, hashFunc);
	}

	private final Function<String, Number> hashFunc;

	/**
	 * @param size     最大值
	 * @param hashFunc Hash函数
	 */
	public FuncFilter(final int size, final Function<String, Number> hashFunc) {
		super(size);
		this.hashFunc = hashFunc;
	}

	@Override
	public int hash(final String str) {
		return hashFunc.apply(str).intValue() % size;
	}
}
