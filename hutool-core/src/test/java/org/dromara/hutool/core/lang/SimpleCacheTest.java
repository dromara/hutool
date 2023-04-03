package org.dromara.hutool.core.lang;

import org.dromara.hutool.core.cache.SimpleCache;
import org.dromara.hutool.core.thread.ConcurrencyTester;
import org.dromara.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SimpleCacheTest {

	@BeforeEach
	public void putTest(){
		final SimpleCache<String, String> cache = new SimpleCache<>();
		ThreadUtil.execute(()->cache.put("key1", "value1"));
		ThreadUtil.execute(()->cache.get("key1"));
		ThreadUtil.execute(()->cache.put("key2", "value2"));
		ThreadUtil.execute(()->cache.get("key2"));
		ThreadUtil.execute(()->cache.put("key3", "value3"));
		ThreadUtil.execute(()->cache.get("key3"));
		ThreadUtil.execute(()->cache.put("key4", "value4"));
		ThreadUtil.execute(()->cache.get("key4"));
		ThreadUtil.execute(()->cache.get("key5", ()->"value5"));

		cache.get("key5", ()->"value5");
	}

	@Test
	public void getTest(){
		final SimpleCache<String, String> cache = new SimpleCache<>();
		cache.put("key1", "value1");
		cache.get("key1");
		cache.put("key2", "value2");
		cache.get("key2");
		cache.put("key3", "value3");
		cache.get("key3");
		cache.put("key4", "value4");
		cache.get("key4");
		cache.get("key5", ()->"value5");

		Assertions.assertEquals("value1", cache.get("key1"));
		Assertions.assertEquals("value2", cache.get("key2"));
		Assertions.assertEquals("value3", cache.get("key3"));
		Assertions.assertEquals("value4", cache.get("key4"));
		Assertions.assertEquals("value5", cache.get("key5"));
		Assertions.assertEquals("value6", cache.get("key6", ()-> "value6"));
	}

	@Test
	public void getWithPredicateTest(){
		// 检查predicate空指针
		final SimpleCache<String, String> cache = new SimpleCache<>();
		final String value = cache.get("abc", (v)-> v.equals("1"), () -> "123");
		Assertions.assertEquals("123", value);
	}

	@SuppressWarnings("resource")
	@Test
	public void getConcurrencyTest(){
		final SimpleCache<String, String> cache = new SimpleCache<>();
		final ConcurrencyTester tester = new ConcurrencyTester(2000);
		tester.test(()-> cache.get("aaa", ()-> {
			ThreadUtil.sleep(200);
			return "aaaValue";
		}));

		Assertions.assertTrue(tester.getInterval() > 0);
		Assertions.assertEquals("aaaValue", cache.get("aaa"));
	}
}
