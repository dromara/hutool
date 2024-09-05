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

package org.dromara.hutool.core.collection;

import org.dromara.hutool.core.map.MapProxy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MapProxyTest {

	@Test
	public void mapProxyTest() {
		final Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		map.put("b", "2");

		final MapProxy mapProxy = new MapProxy(map);
		final Integer b = mapProxy.getInt("b");
		Assertions.assertEquals(new Integer(2), b);

		final Set<Object> keys = mapProxy.keySet();
		Assertions.assertFalse(keys.isEmpty());

		final Set<Entry<Object,Object>> entrys = mapProxy.entrySet();
		//noinspection ConstantConditions
		Assertions.assertFalse(entrys.isEmpty());
	}

	private interface Student {
		Student setName(String name);
		Student setAge(int age);

		String getName();
		int getAge();
	}

	@Test
	public void classProxyTest() {
		final Student student = MapProxy.of(new HashMap<>()).toProxyBean(Student.class);
		student.setName("小明").setAge(18);
		Assertions.assertEquals(student.getAge(), 18);
		Assertions.assertEquals(student.getName(), "小明");
	}
}
