package cn.hutool.core.map;

import org.junit.Assert;
import org.junit.Test;

public class CaseInsensitiveMapTest {
	
	@Test
	public void caseInsensitiveMapTest() {
		CaseInsensitiveMap<String, String> map = new CaseInsensitiveMap<>();
		map.put("aAA", "OK");
		Assert.assertEquals("OK", map.get("aaa"));
		Assert.assertEquals("OK", map.get("AAA"));
	}
	
	@Test
	public void caseInsensitiveLinkedMapTest() {
		CaseInsensitiveLinkedMap<String, String> map = new CaseInsensitiveLinkedMap<>();
		map.put("aAA", "OK");
		Assert.assertEquals("OK", map.get("aaa"));
		Assert.assertEquals("OK", map.get("AAA"));
	}
}
