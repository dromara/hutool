package cn.hutool.core.map;

import cn.hutool.core.util.SerializeUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CamelCaseMapTest {

	@Test
	public void caseInsensitiveMapTest() {
		CamelCaseMap<String, String> map = new CamelCaseMap<>();
		map.put("customKey", "OK");
		assertEquals("OK", map.get("customKey"));
		assertEquals("OK", map.get("custom_key"));
	}

	@Test
	public void caseInsensitiveLinkedMapTest() {
		CamelCaseLinkedMap<String, String> map = new CamelCaseLinkedMap<>();
		map.put("customKey", "OK");
		assertEquals("OK", map.get("customKey"));
		assertEquals("OK", map.get("custom_key"));
	}

	@Test
	public void serializableKeyFuncTest() {
		CamelCaseMap<String, String> map = new CamelCaseMap<>();
		map.put("serializable_key", "OK");
		CamelCaseMap<String, String> deSerializableMap = SerializeUtil.deserialize(SerializeUtil.serialize(map));
		assertEquals("OK", deSerializableMap.get("serializable_key"));
		assertEquals("OK", deSerializableMap.get("serializableKey"));
		deSerializableMap.put("serializable_func", "OK");
		assertEquals("OK", deSerializableMap.get("serializable_func"));
		assertEquals("OK", deSerializableMap.get("serializableFunc"));
	}


}
