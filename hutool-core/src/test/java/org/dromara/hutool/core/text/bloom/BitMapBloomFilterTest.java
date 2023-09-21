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

package org.dromara.hutool.core.text.bloom;

import org.dromara.hutool.core.codec.hash.HashUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BitMapBloomFilterTest {

	@Test
	public void filterTest() {
		final int size = 2 * 1024 * 1024 * 8;

		final CombinedBloomFilter filter = new CombinedBloomFilter(FuncFilter.of(size, HashUtil::rsHash));
		filter.add("123");
		filter.add("abc");
		filter.add("ddd");

		Assertions.assertTrue(filter.contains("abc"));
		Assertions.assertTrue(filter.contains("ddd"));
		Assertions.assertTrue(filter.contains("123"));
	}
}
