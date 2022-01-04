package cn.hutool.bloomfilter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import cn.hutool.bloomfilter.bitMap.IntMap;
import cn.hutool.bloomfilter.bitMap.LongMap;

public class BitMapBloomFilterTest {

	@Test
	public void filterTest() {
		BitMapBloomFilter filter = new BitMapBloomFilter(10);
		filter.add("123");
		filter.add("abc");
		filter.add("ddd");

		Assertions.assertTrue(filter.contains("abc"));
		Assertions.assertTrue(filter.contains("ddd"));
		Assertions.assertTrue(filter.contains("123"));
	}

	@Test
	@Disabled
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
	@Disabled
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
