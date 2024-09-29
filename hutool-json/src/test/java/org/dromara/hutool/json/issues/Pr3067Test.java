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

package org.dromara.hutool.json.issues;

import lombok.Data;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.json.JSONArray;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Pr3067Test {

	final String jsonStr = "[{\"username\":\"a\",\"password\":\"a-password\"}, {\"username\":\"b\",\"password\":\"b-password\"}]";

	@Test
	void toListTest() {
		final JSONArray array = JSONUtil.parseArray(jsonStr);
		final List<TestUser> resultList = array.toList(TestUser.class);
		Assertions.assertNotNull(resultList);
		Assertions.assertEquals(2, resultList.size());
		Assertions.assertEquals("a", resultList.get(0).getUsername());
		Assertions.assertEquals("a-password", resultList.get(0).getPassword());
		Assertions.assertEquals("b", resultList.get(1).getUsername());
		Assertions.assertEquals("b-password", resultList.get(1).getPassword());
	}

	@Test
	void toTypeReferenceTest() {
		final JSONArray array = JSONUtil.parseArray(jsonStr);
		final List<TestUser> resultList = array.toBean(new TypeReference<List<TestUser>>() {});

		Assertions.assertNotNull(resultList);
		Assertions.assertEquals(2, resultList.size());
		Assertions.assertEquals("a", resultList.get(0).getUsername());
		Assertions.assertEquals("a-password", resultList.get(0).getPassword());
		Assertions.assertEquals("b", resultList.get(1).getUsername());
		Assertions.assertEquals("b-password", resultList.get(1).getPassword());
	}

	@Test
	public void getListByPathTest1() {
		final JSONObject json = JSONUtil.parseObj(ResourceUtil.readUtf8Str("test_json_path_001.json"));

		final List<TestUser> resultList = json.getByPath("testUserList[1].testArray",
			new TypeReference<List<TestUser>>() {});

		Assertions.assertNotNull(resultList);
		Assertions.assertEquals(2, resultList.size());
		Assertions.assertEquals("a", resultList.get(0).getUsername());
		Assertions.assertEquals("a-password", resultList.get(0).getPassword());
		Assertions.assertEquals("b", resultList.get(1).getUsername());
		Assertions.assertEquals("b-password", resultList.get(1).getPassword());
	}

	@Data
	public static class TestUser {
		private String username;
		private String password;
	}
}
