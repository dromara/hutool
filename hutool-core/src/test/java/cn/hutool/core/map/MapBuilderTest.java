package cn.hutool.core.map;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class MapBuilderTest {

	@Test
	public void conditionPutTest() {
		Map<String, String> map = MapBuilder.<String, String>create()
				.put(true, "a", "1")
				.put(false, "b", "2")
				.put(true, "c", () -> getValue(3))
				.put(false, "d", () -> getValue(4))
				.build();

		assertEquals(map.get("a"), "1");
		assertFalse(map.containsKey("b"));
		assertEquals(map.get("c"), "3");
		assertFalse(map.containsKey("d"));
	}

	public String getValue(int value) {
		return String.valueOf(value);
	}
}
