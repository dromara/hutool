package cn.hutool.core.map;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

public class IssueI88R5MTest {
	@Test
	public void biMapTest() {
		final BiMap<String, Integer> biMap = new BiMap<>(new LinkedHashMap<>());
		biMap.put("aaa", 111);
		biMap.getKey(111);
		biMap.put("aaa", 222);
		assertNull(biMap.getKey(111));
	}
}
