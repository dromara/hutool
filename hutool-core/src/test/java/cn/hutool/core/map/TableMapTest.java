package cn.hutool.core.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TableMapTest {

	@Test
	public void getTest(){
		TableMap<String, Integer> tableMap = new TableMap<>(16);
		tableMap.put("aaa", 111);
		tableMap.put("bbb", 222);

		Assertions.assertEquals(new Integer(111), tableMap.get("aaa"));
		Assertions.assertEquals(new Integer(222), tableMap.get("bbb"));

		Assertions.assertEquals("aaa", tableMap.getKey(111));
		Assertions.assertEquals("bbb", tableMap.getKey(222));
	}
}
