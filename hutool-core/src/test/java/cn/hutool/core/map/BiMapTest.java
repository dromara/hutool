package cn.hutool.core.map;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class BiMapTest {

	@Test
	public void getTest(){
		BiMap<String, Integer> biMap = new BiMap<>(new HashMap<>());
		biMap.put("aaa", 111);
		biMap.put("bbb", 222);

		Assert.assertEquals(new Integer(111), biMap.get("aaa"));
		Assert.assertEquals(new Integer(222), biMap.get("bbb"));

		Assert.assertEquals("aaa", biMap.getKey(111));
		Assert.assertEquals("bbb", biMap.getKey(222));
	}
}
