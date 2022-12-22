package cn.hutool.bloomfilter;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.bloomfilter.bitMap.IntMap;
import cn.hutool.bloomfilter.bitMap.LongMap;

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

	@Test
	@Ignore
	public void testIntMap(){
		IntMap intMap = new IntMap();

		for (int i = 0 ; i < 32; i++) {
			intMap.add(i);
		}
		intMap.remove(30);


		for (int i = 0; i < 32; i++) {
			System.out.println(i + "是否存在-->" + intMap.contains(i));
		}
	}

	@Test
	@Ignore
	public void testLongMap(){
		LongMap longMap = new LongMap();

		for (int i = 0 ; i < 64; i++) {
			longMap.add(i);
		}
		longMap.remove(30);


		for (int i = 0; i < 64; i++) {
			System.out.println(i + "是否存在-->" + longMap.contains(i));
		}
	}
}
