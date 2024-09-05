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

package org.dromara.hutool.core.convert;

import org.dromara.hutool.core.map.MapBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Map转换单元测试
 *
 * @author looly
 *
 */
public class MapConvertTest {

	@Test
	public void beanToMapTest() {
		final User user = new User();
		user.setName("AAA");
		user.setAge(45);

		final HashMap<?, ?> map = ConvertUtil.convert(HashMap.class, user);
		Assertions.assertEquals("AAA", map.get("name"));
		Assertions.assertEquals(45, map.get("age"));
	}

	@Test
	public void mapToMapTest() {
		final Map<String, Object> srcMap = MapBuilder
				.of(new HashMap<String, Object>())
				.put("name", "AAA")
				.put("age", 45).map();

		final LinkedHashMap<?, ?> map = ConvertUtil.convert(LinkedHashMap.class, srcMap);
		Assertions.assertEquals("AAA", map.get("name"));
		Assertions.assertEquals(45, map.get("age"));
	}

	public static class User {
		private String name;
		private int age;

		public String getName() {
			return name;
		}

		public void setName(final String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(final int age) {
			this.age = age;
		}
	}
}
