/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.map;

import org.dromara.hutool.core.map.reference.WeakConcurrentMap;
import org.dromara.hutool.core.thread.ConcurrencyTester;
import org.dromara.hutool.core.thread.ThreadUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WeakConcurrentMapTest {

	@Test
	public void putAndGetTest() {
		final WeakConcurrentMap<Object, Object> map = new WeakConcurrentMap<>();
		Object key1 = new Object();
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
	public void getConcurrencyTest() {
		final WeakConcurrentMap<String, String> cache = new WeakConcurrentMap<>();
		final ConcurrencyTester tester = new ConcurrencyTester(2000);
		tester.test(() -> cache.computeIfAbsent("aaa" + RandomUtil.randomInt(2), (key) -> "aaaValue"));

		Assertions.assertTrue(tester.getInterval() > 0);
		final String value = ObjUtil.defaultIfNull(cache.get("aaa0"), cache.get("aaa1"));
		Assertions.assertEquals("aaaValue", value);
	}
}
