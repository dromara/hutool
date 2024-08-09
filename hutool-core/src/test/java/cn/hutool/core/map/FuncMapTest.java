package cn.hutool.core.map;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class FuncMapTest {

	@Test
	public void putGetTest(){
		final FuncMap<Object, Object> map = new FuncMap<>(HashMap::new,
				(key)->key.toString().toLowerCase(),
				(value)->value.toString().toUpperCase());

		map.put("aaa", "b");
		map.put("BBB", "c");

		assertEquals("B", map.get("aaa"));
		assertEquals("C", map.get("bbb"));
	}
}
