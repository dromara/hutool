package org.dromara.hutool.map;

import org.dromara.hutool.io.SerializeUtil;
import org.dromara.hutool.util.ObjUtil;
import org.dromara.hutool.util.RandomUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class TolerantMapTest {

	@Test
	public void testSerialize() {
		TolerantMap<String, String> map = TolerantMap.of(new HashMap<>(), "default");
		map.put("monday", "星期一");
		map.put("tuesday", "星期二");

		final byte[] bytes = SerializeUtil.serialize(map);
		final TolerantMap<String, String> serializedMap = SerializeUtil.deserialize(bytes);
		assert serializedMap != map;
		assert map.equals(serializedMap);
	}

	@Test
	public void testClone() {
		TolerantMap<String, String> map = TolerantMap.of(new HashMap<>(), "default");
		map.put("monday", "星期一");
		map.put("tuesday", "星期二");

		final TolerantMap<String, String> clonedMap = ObjUtil.clone(map);
		assert clonedMap != map;
		assert map.equals(clonedMap);
	}

	@Test
	public void testGet() {
		TolerantMap<String, String> map = TolerantMap.of(new HashMap<>(), "default");
		map.put("monday", "星期一");
		map.put("tuesday", "星期二");

		assert "星期二".equals(map.get("tuesday"));
		assert "default".equals(map.get(RandomUtil.randomString(6)));
	}
}
