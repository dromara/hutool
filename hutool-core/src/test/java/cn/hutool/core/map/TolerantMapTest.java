package cn.hutool.core.map;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class TolerantMapTest {

	private static final TolerantMap<String, String> map = TolerantMap.of(new HashMap<>(), "default");

	@BeforeAll
	public static void init() {
		map.put("monday", "星期一");
		map.put("tuesday", "星期二");
	}

	@Test
	public void testSerialize() {
		byte[] bytes = ObjectUtil.serialize(map);
		TolerantMap<String, String> serializedMap = ObjectUtil.deserialize(bytes);
		Assertions.assertNotSame(serializedMap, map);
		Assertions.assertEquals(map, serializedMap);
	}

	@Test
	public void testClone() {
		TolerantMap<String, String> clonedMap = ObjectUtil.clone(map);
		Assertions.assertNotSame(clonedMap, map);
		Assertions.assertEquals(map, clonedMap);
	}

	@Test
	public void testGet() {
		Assertions.assertEquals("星期二", map.get("tuesday"));
		Assertions.assertEquals("default", map.get(RandomUtil.randomString(6)));
	}
}
