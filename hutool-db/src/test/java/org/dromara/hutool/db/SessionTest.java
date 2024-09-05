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
