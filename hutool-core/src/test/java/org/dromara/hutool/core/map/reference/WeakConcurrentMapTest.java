package org.dromara.hutool.core.map.reference;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WeakConcurrentMapTest {
	@Test
	public void testWeakConcurrentMap() {
		final WeakConcurrentMap<String, String> map = new WeakConcurrentMap<>();

		// Test if the map is initially empty
		assertTrue(map.isEmpty());

		// Test if the map can store and retrieve values correctly
		map.put("key1", "value1");
		assertEquals("value1", map.get("key1"));

		map.computeIfAbsent("key2", key -> "value2");
		// Test if the map can handle concurrent modifications
		new Thread(() -> {
			map.put("key2", "value2");
		}).start();

		// Test if the map has correctly stored the value from the concurrent modification
		assertEquals("value2", map.get("key2"));

		assertTrue(map.containsKey("key1"));
		assertTrue(map.containsKey("key2"));

		// null值
		String s = map.computeIfAbsent("key3", key -> null);
		assertNull(s);

		// 允许key为null
		s = map.computeIfAbsent(null, key -> null);
		assertNull(s);
	}

	@SuppressWarnings("StringOperationCanBeSimplified")
	@Test
	void computeIfAbsentTest() {
		final WeakConcurrentMap<String, String> map = new WeakConcurrentMap<>();
		final String value = map.computeIfAbsent("key1", key -> new String("value1"));
		assertNotNull(value);
	}
}
