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
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class IssueI6YN2ATest {

	@Test
	public void toBeanTest() {
		final String str = "{\"conditions\":{\"user\":\"test\",\"sex\":\"男\"}," +
			"\"headers\":{\"name\":\"姓名\",\"age\":\"年龄\",\"email\":\"邮箱\",\"number\":\"号码\",\"pwd\":\"密码\"}}";
		final JSONObject jsonObject = JSONUtil.parseObj(str);

		final PageQuery<User> bean = jsonObject.toBean(new TypeReference<PageQuery<User>>() {});
		Assertions.assertEquals("{name=姓名, age=年龄, email=邮箱, number=号码, pwd=密码}", bean.headers.toString());
	}

	@Data
	public static class PageQuery<T> {
		private Map<String, String> headers = new LinkedHashMap<>();
		private T conditions;
	}

	@Data
	public static class User {
		private String name;
		private String sex;
	}
}
