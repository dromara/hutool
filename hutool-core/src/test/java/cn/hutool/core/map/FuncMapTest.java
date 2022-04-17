package cn.hutool.core.map;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class FuncMapTest {

	@Test
	public void putGetTest(){
		final FuncMap<Object, Object> map = new FuncMap<>(HashMap::new,
				(key)->key.toString().toLowerCase(),
				(value)->value.toString().toUpperCase());

		map.put("aaa", "b");
		map.put("BBB", "c");

		Assert.assertEquals("B", map.get("aaa"));
		Assert.assertEquals("C", map.get("bbb"));
	}
}
