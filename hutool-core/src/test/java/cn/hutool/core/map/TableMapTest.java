package cn.hutool.core.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TableMapTest {

	@Test
	public void getTest(){
		final TableMap<String, Integer> tableMap = new TableMap<>(16);
		tableMap.put("aaa", 111);
		tableMap.put("bbb", 222);

		Assertions.assertEquals(new Integer(111), tableMap.get("aaa"));
		Assertions.assertEquals(new Integer(222), tableMap.get("bbb"));

		Assertions.assertEquals("aaa", tableMap.getKey(111));
		Assertions.assertEquals("bbb", tableMap.getKey(222));
	}

	@SuppressWarnings("OverwrittenKey")
	@Test
	public void removeTest() {
		final TableMap<String, Integer> tableMap = new TableMap<>(16);
		tableMap.put("a", 111);
		tableMap.put("a", 222);
		tableMap.put("a", 222);

		tableMap.remove("a");

		Assertions.assertEquals(0, tableMap.size());
	}

	@SuppressWarnings("OverwrittenKey")
	@Test
	public void removeTest2() {
		final TableMap<String, Integer> tableMap = new TableMap<>(16);
		tableMap.put("a", 111);
		tableMap.put("a", 222);
		tableMap.put("a", 222);

		tableMap.remove("a", 222);

		Assertions.assertEquals(1, tableMap.size());
	}
}
