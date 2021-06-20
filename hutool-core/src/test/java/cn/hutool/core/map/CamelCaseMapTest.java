package cn.hutool.core.map;

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
}
