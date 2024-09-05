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

package org.dromara.hutool.core.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.hutool.core.bean.copier.CopyOptions;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IssueI8M38TTest {
	@Test
	void copyListByMappingTest() {
		final SubPerson subPerson = new SubPerson();
		subPerson.setAge(14);
		subPerson.setOpenid(112233);
		subPerson.setSubName("sub名字");
		final ArrayList<SubPerson> subPeople = ListUtil.of(subPerson);

		final HashMap<String, String> mapping = MapUtil.of("subName", "name");
		final List<Person> people = BeanUtil.copyToList(subPeople, Person.class,
			// 不覆盖模式下，当第一次subName已经拷贝到值，后续不再覆盖
			CopyOptions.of().setFieldMapping(mapping).setOverride(false));

		Assertions.assertEquals(subPerson.getSubName(), people.get(0).getName());
	}

	@Test
	void copyListByMappingTest2() {
		final SubPerson subPerson = new SubPerson();
		subPerson.setAge(14);
		subPerson.setOpenid(112233);
		subPerson.setSubName("sub名字");
		final ArrayList<SubPerson> subPeople = ListUtil.of(subPerson);

		final HashMap<String, String> mapping = MapUtil.of("subName", "name");
		// subName复制到name后，name字段继续拷贝，覆盖了subName的值
		mapping.put("name", "aaa");
		final List<Person> people = BeanUtil.copyToList(subPeople, Person.class,
			CopyOptions.of().setFieldMapping(mapping));

		Assertions.assertEquals(subPerson.getSubName(), people.get(0).getName());
	}

	@EqualsAndHashCode(callSuper = true)
	@Data
	static class SubPerson extends Person{
		private String subName;
	}

	@Data
	static class Person {
		private int age;
		private int openid;
		private String name;
	}
}
