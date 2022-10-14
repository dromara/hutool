package cn.hutool.core.map;

import cn.hutool.core.util.SerializeUtil;
import org.junit.Assert;
import org.junit.Test;

public class CamelCaseMapTest {

	@Test
	public void caseInsensitiveMapTest() {
		CamelCaseMap<String, String> map = new CamelCaseMap<>();
		map.put("customKey", "OK");
		Assert.assertEquals("OK", map.get("customKey"));
		Assert.assertEquals("OK", map.get("custom_key"));
	}

	@Test
	public void caseInsensitiveLinkedMapTest() {
		CamelCaseLinkedMap<String, String> map = new CamelCaseLinkedMap<>();
		map.put("customKey", "OK");
		Assert.assertEquals("OK", map.get("customKey"));
		Assert.assertEquals("OK", map.get("custom_key"));
	}

	@Test
	public void testSerializable() {
		CamelCaseMap<String, String> map = new CamelCaseMap<>();
		map.put("serializable_key", "OK");
		CamelCaseMap<String, String> unSerializableMap = SerializeUtil.deserialize(SerializeUtil.serialize(map));
		Assert.assertEquals("OK", unSerializableMap.get("serializable_key"));
		Assert.assertEquals("OK", unSerializableMap.get("serializableKey"));
		unSerializableMap.put("serializable_func", "OK");
		Assert.assertEquals("OK", unSerializableMap.get("serializable_func"));
		Assert.assertEquals("OK", unSerializableMap.get("serializableFunc"));
	}


}
