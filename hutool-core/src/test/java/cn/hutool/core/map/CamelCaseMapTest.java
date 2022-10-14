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
	public void serializableKeyFuncTest() {
		CamelCaseMap<String, String> map = new CamelCaseMap<>();
		map.put("serializable_key", "OK");
		CamelCaseMap<String, String> deSerializableMap = SerializeUtil.deserialize(SerializeUtil.serialize(map));
		Assert.assertEquals("OK", deSerializableMap.get("serializable_key"));
		Assert.assertEquals("OK", deSerializableMap.get("serializableKey"));
		deSerializableMap.put("serializable_func", "OK");
		Assert.assertEquals("OK", deSerializableMap.get("serializable_func"));
		Assert.assertEquals("OK", deSerializableMap.get("serializableFunc"));
	}


}
