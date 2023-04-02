package org.dromara.hutool.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class BiMapTest {

	@Test
	public void getTest(){
		final BiMap<String, Integer> biMap = new BiMap<>(new HashMap<>());
		biMap.put("aaa", 111);
		biMap.put("bbb", 222);

		Assertions.assertEquals(new Integer(111), biMap.get("aaa"));
		Assertions.assertEquals(new Integer(222), biMap.get("bbb"));

		Assertions.assertEquals("aaa", biMap.getKey(111));
		Assertions.assertEquals("bbb", biMap.getKey(222));
	}

	@Test
	public void computeIfAbsentTest(){
		final BiMap<String, Integer> biMap = new BiMap<>(new HashMap<>());
		biMap.put("aaa", 111);
		biMap.put("bbb", 222);

		biMap.computeIfAbsent("ccc", s -> 333);
		Assertions.assertEquals(new Integer(333), biMap.get("ccc"));
		Assertions.assertEquals("ccc", biMap.getKey(333));
	}

	@Test
	public void putIfAbsentTest(){
		final BiMap<String, Integer> biMap = new BiMap<>(new HashMap<>());
		biMap.put("aaa", 111);
		biMap.put("bbb", 222);

		biMap.putIfAbsent("ccc", 333);
		Assertions.assertEquals(new Integer(333), biMap.get("ccc"));
		Assertions.assertEquals("ccc", biMap.getKey(333));
	}
}
