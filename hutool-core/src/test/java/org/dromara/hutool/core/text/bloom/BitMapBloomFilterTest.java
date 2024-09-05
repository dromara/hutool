/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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
