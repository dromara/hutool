package cn.hutool.core.text.bloom;

import cn.hutool.core.lang.hash.HashUtil;
import org.junit.Assert;
import org.junit.Test;

public class BitMapBloomFilterTest {

	@Test
	public void filterTest() {
		int size = 2 * 1024 * 1024 * 8;

		CombinedBloomFilter filter = new CombinedBloomFilter(FuncFilter.of(size, HashUtil::rsHash));
		filter.add("123");
		filter.add("abc");
		filter.add("ddd");

		Assert.assertTrue(filter.contains("abc"));
		Assert.assertTrue(filter.contains("ddd"));
		Assert.assertTrue(filter.contains("123"));
	}
}
