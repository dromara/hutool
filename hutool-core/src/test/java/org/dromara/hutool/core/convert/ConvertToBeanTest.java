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

import org.dromara.hutool.core.bean.BeanUtilTest.SubPerson;
import org.dromara.hutool.core.map.CaseInsensitiveMap;
import org.dromara.hutool.core.reflect.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 类型转换工具单元测试<br>
 * 转换为数组
 *
 * @author Looly
 *
 */
public class ConvertToBeanTest {

	@Test
	public void beanToMapTest() {
		final SubPerson person = new SubPerson();
		person.setAge(14);
		person.setOpenid("11213232");
		person.setName("测试A11");
		person.setSubName("sub名字");

		final Map<?, ?> map = ConvertUtil.convert(Map.class, person);
		assertEquals(map.get("name"), "测试A11");
		assertEquals(map.get("age"), 14);
		assertEquals("11213232", map.get("openid"));
	}

	@Test
	public void beanToMapTest2() {
		final SubPerson person = new SubPerson();
		person.setAge(14);
		person.setOpenid("11213232");
		person.setName("测试A11");
		person.setSubName("sub名字");

		final Map<String, String> map = ConvertUtil.toMap(String.class, String.class, person);
		assertEquals("测试A11", map.get("name"));
		assertEquals("14", map.get("age"));
		assertEquals("11213232", map.get("openid"));

		final LinkedHashMap<String, String> map2 = ConvertUtil.convert(
				new TypeReference<LinkedHashMap<String, String>>() {}, person);
		assertEquals("测试A11", map2.get("name"));
		assertEquals("14", map2.get("age"));
		assertEquals("11213232", map2.get("openid"));
	}

	@Test
	public void mapToMapTest() {
		final LinkedHashMap<String, Integer> map1 = new LinkedHashMap<>();
		map1.put("key1", 1);
		map1.put("key2", 2);
		map1.put("key3", 3);
		map1.put("key4", 4);

		final Map<String, String> map2 = ConvertUtil.toMap(String.class, String.class, map1);

		assertEquals("1", map2.get("key1"));
		assertEquals("2", map2.get("key2"));
		assertEquals("3", map2.get("key3"));
		assertEquals("4", map2.get("key4"));
	}

	@Test
	public void mapToBeanTest() {
		final HashMap<String, Object> map = new HashMap<>();
		map.put("id", "88dc4b28-91b1-4a1a-bab5-444b795c7ecd");
		map.put("age", 14);
		map.put("openid", "11213232");
		map.put("name", "测试A11");
		map.put("subName", "sub名字");

		final SubPerson subPerson = ConvertUtil.convert(SubPerson.class, map);
		assertEquals("88dc4b28-91b1-4a1a-bab5-444b795c7ecd", subPerson.getId().toString());
		assertEquals(14, subPerson.getAge());
		assertEquals("11213232", subPerson.getOpenid());
		assertEquals("测试A11", subPerson.getName());
		assertEquals("11213232", subPerson.getOpenid());
	}

	@Test
	public void nullStrToBeanTest(){
		final String nullStr = "null";
		final SubPerson subPerson = ConvertUtil.convertQuietly(SubPerson.class, nullStr);
		Assertions.assertNull(subPerson);
	}

	@Test
	public void mapToMapWithSelfTypeTest() {
		final CaseInsensitiveMap<String, Integer> caseInsensitiveMap = new CaseInsensitiveMap<>();
		caseInsensitiveMap.put("jerry", 1);
		caseInsensitiveMap.put("Jerry", 2);
		caseInsensitiveMap.put("tom", 3);

		final Map<String, String> map = ConvertUtil.toMap(String.class, String.class, caseInsensitiveMap);
		assertEquals("2", map.get("jerry"));
		assertEquals("2", map.get("Jerry"));
		assertEquals("3", map.get("tom"));
	}
	@Test
	public void beanToSpecifyMapTest() {
		final SubPerson person = new SubPerson();
		person.setAge(14);
		person.setOpenid("11213232");
		person.setName("测试A11");
		person.setSubName("sub名字");

		final Map<String, String> map = ConvertUtil.toMap(LinkedHashMap.class, String.class, String.class, person);
		assertEquals("测试A11", map.get("name"));
		assertEquals("14", map.get("age"));
		assertEquals("11213232", map.get("openid"));
	}
}
