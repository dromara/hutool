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

package org.dromara.hutool.db;

import org.dromara.hutool.db.pojo.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Entity测试
 *
 * @author looly
 *
 */
public class EntityTest {

	@Test
	public void ofTest() {
		final User user = new User();
		user.setId(1);
		user.setName("test");

		final Entity entity = Entity.of("testTable").parseBean(user);
		Assertions.assertEquals(Integer.valueOf(1), entity.getInt("id"));
		Assertions.assertEquals("test", entity.getStr("name"));
	}

	@Test
	public void ofTest2() {
		final User user = new User();
		user.setId(1);
		user.setName("test");

		final Entity entity = Entity.of().parseBean(user);
		Assertions.assertEquals(Integer.valueOf(1), entity.getInt("id"));
		Assertions.assertEquals("test", entity.getStr("name"));
		Assertions.assertEquals("user", entity.getTableName());
	}

	@Test
	public void ofTest3() {
		final User user = new User();
		user.setName("test");

		final Entity entity = Entity.of().parseBean(user, false, true);

		Assertions.assertFalse(entity.containsKey("id"));
		Assertions.assertEquals("test", entity.getStr("name"));
		Assertions.assertEquals("user", entity.getTableName());
	}

	@Test
	public void entityToBeanIgnoreCaseTest() {
		final Entity entity = Entity.of().set("ID", 2).set("NAME", "testName");
		final User user = entity.toBeanIgnoreCase(User.class);

		Assertions.assertEquals(Integer.valueOf(2), user.getId());
		Assertions.assertEquals("testName", user.getName());
	}
}
