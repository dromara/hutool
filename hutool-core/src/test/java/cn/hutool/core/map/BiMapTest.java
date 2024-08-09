package cn.hutool.core.map;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class BiMapTest {

	@Test
	public void getTest(){
		final BiMap<String, Integer> biMap = new BiMap<>(new HashMap<>());
		biMap.put("aaa", 111);
		biMap.put("bbb", 222);

		assertEquals(new Integer(111), biMap.get("aaa"));
		assertEquals(new Integer(222), biMap.get("bbb"));

		assertEquals("aaa", biMap.getKey(111));
		assertEquals("bbb", biMap.getKey(222));
	}

	@Test
	public void computeIfAbsentTest(){
		final BiMap<String, Integer> biMap = new BiMap<>(new HashMap<>());
		biMap.put("aaa", 111);
		biMap.put("bbb", 222);

		biMap.computeIfAbsent("ccc", s -> 333);
		assertEquals(new Integer(333), biMap.get("ccc"));
		assertEquals("ccc", biMap.getKey(333));
	}

	@Test
	public void putIfAbsentTest(){
		final BiMap<String, Integer> biMap = new BiMap<>(new HashMap<>());
		biMap.put("aaa", 111);
		biMap.put("bbb", 222);

		biMap.putIfAbsent("ccc", 333);
		assertEquals(new Integer(333), biMap.get("ccc"));
		assertEquals("ccc", biMap.getKey(333));
	}
}
