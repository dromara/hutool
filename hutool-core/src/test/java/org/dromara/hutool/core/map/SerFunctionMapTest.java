package org.dromara.hutool.core.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class SerFunctionMapTest {

	@Test
	public void putGetTest(){
		final FuncMap<Object, Object> map = new FuncMap<>(HashMap::new,
				(key)->key.toString().toLowerCase(),
				(value)->value.toString().toUpperCase());

		map.put("aaa", "b");
		map.put("BBB", "c");

		Assertions.assertEquals("B", map.get("aaa"));
		Assertions.assertEquals("C", map.get("bbb"));
	}
}
