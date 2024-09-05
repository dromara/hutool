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
import org.dromara.hutool.core.bean.copier.CopyOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Issue3452Test {

	@Test
	void fillBeanWithMapTest() {
		final Map<String, Object> properties = new HashMap<>();
		properties.put("name", "JohnDoe");
		properties.put("user_age", 25);
		final User user = BeanUtil.fillBeanWithMap(
			properties, new User(), CopyOptions.of());
		Assertions.assertEquals("JohnDoe", user.getName());
		Assertions.assertEquals(25, user.getUserAge());
	}

	@Data
	static class User {
		private String name;
		private int userAge;
	}
}
