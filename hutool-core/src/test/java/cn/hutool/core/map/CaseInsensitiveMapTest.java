package cn.hutool.core.map;

import cn.hutool.core.lang.Pair;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CaseInsensitiveMapTest {

	@Test
	public void caseInsensitiveMapTest() {
		CaseInsensitiveMap<String, String> map = new CaseInsensitiveMap<>();
		map.put("aAA", "OK");
		assertEquals("OK", map.get("aaa"));
		assertEquals("OK", map.get("AAA"));
	}

	@Test
	public void caseInsensitiveLinkedMapTest() {
		CaseInsensitiveLinkedMap<String, String> map = new CaseInsensitiveLinkedMap<>();
		map.put("aAA", "OK");
		assertEquals("OK", map.get("aaa"));
		assertEquals("OK", map.get("AAA"));
	}

	@Test
	public void mergeTest(){
		//https://github.com/dromara/hutool/issues/2086
		Pair<String, String> b = new Pair<>("a", "value");
		Pair<String, String> a = new Pair<>("A", "value");
		final CaseInsensitiveMap<Object, Object> map = new CaseInsensitiveMap<>();
		map.merge(b.getKey(), b.getValue(), (A, B) -> A);
		map.merge(a.getKey(), a.getValue(), (A, B) -> A);

		assertEquals(1, map.size());
	}

	@Test
	public void issueIA4K4FTest() {
		Map<String, Object> map = new CaseInsensitiveLinkedMap<>();
		map.put("b", 2);
		map.put("a", 1);

		AtomicInteger index = new AtomicInteger();
		map.forEach((k, v) -> {
			if(0 == index.get()){
				assertEquals("b", k);
			} else if(1 == index.get()){
				assertEquals("a", k);
			}

			index.getAndIncrement();
		});
	}

	@Test
	public void issueIA4K4FTest2() {
		Map<String, Object> map = new CaseInsensitiveTreeMap<>();
		map.put("b", 2);
		map.put("a", 1);

		AtomicInteger index = new AtomicInteger();
		map.forEach((k, v) -> {
			if(0 == index.get()){
				assertEquals("a", k);
			} else if(1 == index.get()){
				assertEquals("b", k);
			}

			index.getAndIncrement();
		});
	}
}
