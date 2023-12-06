/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.bean;

import lombok.Data;
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
			CopyOptions.of().setFieldMapping(mapping));

		Assertions.assertEquals(subPerson.getSubName(), people.get(0).getName());
	}

	@Data
	static class SubPerson {
		private int age;
		private int openid;
		private String subName;
	}

	@Data
	static class Person {
		private int age;
		private int openid;
		private String name;
	}
}
