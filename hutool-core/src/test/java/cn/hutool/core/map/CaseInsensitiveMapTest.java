package cn.hutool.core.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CaseInsensitiveMapTest {

	@Test
	public void caseInsensitiveMapTest() {
		CaseInsensitiveMap<String, String> map = new CaseInsensitiveMap<>();
		map.put("aAA", "OK");
		Assertions.assertEquals("OK", map.get("aaa"));
		Assertions.assertEquals("OK", map.get("AAA"));
	}

	@Test
	public void caseInsensitiveLinkedMapTest() {
		CaseInsensitiveLinkedMap<String, String> map = new CaseInsensitiveLinkedMap<>();
		map.put("aAA", "OK");
		Assertions.assertEquals("OK", map.get("aaa"));
		Assertions.assertEquals("OK", map.get("AAA"));
	}
}
