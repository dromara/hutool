/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
	public void parseTest() {
		final User user = new User();
		user.setId(1);
		user.setName("test");

		final Entity entity = Entity.of("testTable").parseBean(user);
		Assertions.assertEquals(Integer.valueOf(1), entity.getInt("id"));
		Assertions.assertEquals("test", entity.getStr("name"));
	}

	@Test
	public void parseTest2() {
		final User user = new User();
		user.setId(1);
		user.setName("test");

		final Entity entity = Entity.of().parseBean(user);
		Assertions.assertEquals(Integer.valueOf(1), entity.getInt("id"));
		Assertions.assertEquals("test", entity.getStr("name"));
		Assertions.assertEquals("user", entity.getTableName());
	}

	@Test
	public void parseTest3() {
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
