package cn.hutool.core.map;

import cn.hutool.core.thread.ConcurrencyTester;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import org.junit.Assert;
import org.junit.Test;

public class WeakConcurrentMapTest {

	@Test
	public void putAndGetTest(){
		final WeakConcurrentMap<Object, Object> map = new WeakConcurrentMap<>();
		Object
				key1 = new Object(), value1 = new Object(),
				key2 = new Object(), value2 = new Object(),
				key3 = new Object(), value3 = new Object(),
				key4 = new Object(), value4 = new Object();
		map.put(key1, value1);
		map.put(key2, value2);
		map.put(key3, value3);
		map.put(key4, value4);

		Assert.assertEquals(value1, map.get(key1));
		Assert.assertEquals(value2, map.get(key2));
		Assert.assertEquals(value3, map.get(key3));
		Assert.assertEquals(value4, map.get(key4));

		// 清空引用
		//noinspection UnusedAssignment
		key1 = null;
		//noinspection UnusedAssignment
		key2 = null;

		System.gc();
		ThreadUtil.sleep(200L);

		Assert.assertEquals(2, map.size());
	}

	@Test
	public void getConcurrencyTest(){
		final WeakConcurrentMap<String, String> cache = new WeakConcurrentMap<>();
		final ConcurrencyTester tester = new ConcurrencyTester(9000);
		tester.test(()-> cache.computeIfAbsent("aaa" + RandomUtil.randomInt(2), (key)-> "aaaValue"));

		Assert.assertTrue(tester.getInterval() > 0);
		String value = ObjectUtil.defaultIfNull(cache.get("aaa0"), cache.get("aaa1"));
		Assert.assertEquals("aaaValue", value);
	}
}
