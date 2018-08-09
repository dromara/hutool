package cn.hutool.bloomfilter;

import org.junit.Assert;
import org.junit.Test;

public class BitMapBloomFilterTest {
	
	@Test
	public void filterTest() {
		BitMapBloomFilter filter = new BitMapBloomFilter(10);
		filter.add("123");
		filter.add("abc");
		filter.add("ddd");
		
		Assert.assertTrue(filter.contains("abc"));
		Assert.assertTrue(filter.contains("ddd"));
		Assert.assertTrue(filter.contains("123"));
	}
}
