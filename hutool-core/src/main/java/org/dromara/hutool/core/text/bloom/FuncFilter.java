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
