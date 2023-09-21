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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * 事务性数据库操作单元测试
 * @author looly
 *
 */
public class SessionTest {

	@Test
	@Disabled
	public void transTest() {
		final Session session = Session.of("test");
		session.beginTransaction();
		session.update(Entity.of().set("age", 76), Entity.of("user").set("name", "unitTestUser"));
		session.commit();
	}

	@Test
	@Disabled
	public void txTest() {
		Session.of("test").tx(session -> session.update(Entity.of().set("age", 78), Entity.of("user").set("name", "unitTestUser")));
	}
}
