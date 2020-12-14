package cn.hutool.core.map;

import org.junit.Assert;
import org.junit.Test;

public class TableMapTest {

	@Test
	public void getTest(){
		TableMap<String, Integer> tableMap = new TableMap<>(16);
		tableMap.put("aaa", 111);
		tableMap.put("bbb", 222);

		Assert.assertEquals(new Integer(111), tableMap.get("aaa"));
		Assert.assertEquals(new Integer(222), tableMap.get("bbb"));

		Assert.assertEquals("aaa", tableMap.getKey(111));
		Assert.assertEquals("bbb", tableMap.getKey(222));
	}
}
