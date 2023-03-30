package cn.hutool.core.map;

import cn.hutool.core.thread.ConcurrencyTester;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WeakConcurrentMapTest {

	@Test
	public void putAndGetTest(){
		final WeakConcurrentMap<Object, Object> map = new WeakConcurrentMap<>();
		Object
				key1 = new Object();
		final Object value1 = new Object();
		Object key2 = new Object();
		final Object value2 = new Object();
		final Object key3 = new Object();
		final Object value3 = new Object();
		final Object key4 = new Object();
		final Object value4 = new Object();
		map.put(key1, value1);
		map.put(key2, value2);
		map.put(key3, value3);
		map.put(key4, value4);

		Assertions.assertEquals(value1, map.get(key1));
		Assertions.assertEquals(value2, map.get(key2));
		Assertions.assertEquals(value3, map.get(key3));
		Assertions.assertEquals(value4, map.get(key4));

		// 清空引用
		//noinspection UnusedAssignment
		key1 = null;
		//noinspection UnusedAssignment
		key2 = null;

		System.gc();
		ThreadUtil.sleep(200L);

		Assertions.assertEquals(2, map.size());
	}

	@Test
	public void getConcurrencyTest(){
		final WeakConcurrentMap<String, String> cache = new WeakConcurrentMap<>();
		final ConcurrencyTester tester = new ConcurrencyTester(2000);
		tester.test(()-> cache.computeIfAbsent("aaa" + RandomUtil.randomInt(2), (key)-> "aaaValue"));

		Assertions.assertTrue(tester.getInterval() > 0);
		final String value = ObjUtil.defaultIfNull(cache.get("aaa0"), cache.get("aaa1"));
		Assertions.assertEquals("aaaValue", value);
	}
}
