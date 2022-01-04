package cn.hutool.core.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class BiMapTest {

	@Test
	public void getTest(){
		BiMap<String, Integer> biMap = new BiMap<>(new HashMap<>());
		biMap.put("aaa", 111);
		biMap.put("bbb", 222);

		Assertions.assertEquals(new Integer(111), biMap.get("aaa"));
		Assertions.assertEquals(new Integer(222), biMap.get("bbb"));

		Assertions.assertEquals("aaa", biMap.getKey(111));
		Assertions.assertEquals("bbb", biMap.getKey(222));
	}
}
