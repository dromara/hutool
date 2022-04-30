package cn.hutool.core.map;

import cn.hutool.core.lang.Pair;
import org.junit.Assert;
import org.junit.Test;

public class CaseInsensitiveMapTest {

	@Test
	public void caseInsensitiveMapTest() {
		final CaseInsensitiveMap<String, String> map = new CaseInsensitiveMap<>();
		map.put("aAA", "OK");
		Assert.assertEquals("OK", map.get("aaa"));
		Assert.assertEquals("OK", map.get("AAA"));
	}

	@Test
	public void caseInsensitiveLinkedMapTest() {
		final CaseInsensitiveLinkedMap<String, String> map = new CaseInsensitiveLinkedMap<>();
		map.put("aAA", "OK");
		Assert.assertEquals("OK", map.get("aaa"));
		Assert.assertEquals("OK", map.get("AAA"));
	}

	@Test
	public void mergeTest(){
		//https://github.com/dromara/hutool/issues/2086
		final Pair<String, String> b = new Pair<>("a", "value");
		final Pair<String, String> a = new Pair<>("A", "value");
		final CaseInsensitiveMap<Object, Object> map = new CaseInsensitiveMap<>();
		map.merge(b.getKey(), b.getValue(), (A, B) -> A);
		map.merge(a.getKey(), a.getValue(), (A, B) -> A);

		Assert.assertEquals(1, map.size());
	}
}
