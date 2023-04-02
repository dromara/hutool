package org.dromara.hutool.text.bloom;

import org.dromara.hutool.codec.hash.HashUtil;
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
