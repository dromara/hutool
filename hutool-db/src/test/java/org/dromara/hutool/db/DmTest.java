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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * 达梦数据库单元测试
 *
 * @author wb04307201
 */
public class DmTest {

	private static final String DS_GROUP_NAME = "dm";

	//@BeforeAll
	//@Disabled
	public static void init() {
		final Db db = Db.of(DS_GROUP_NAME);
		db.execute("CREATE TABLE test(a INTEGER, b INTEGER)");

		db.insert(Entity.of("test").set("a", 1).set("b", 11));
		db.insert(Entity.of("test").set("a", 2).set("b", 21));
		db.insert(Entity.of("test").set("a", 3).set("b", 31));
		db.insert(Entity.of("test").set("a", 4).set("b", 41));
	}

	@Test
	@Disabled
	public void upsertTest() {
		final Db db = Db.of(DS_GROUP_NAME);
		db.upsert(Entity.of("test").set("a", 1).set("b", 111), "a");
		final Entity a1 = db.get("test", "a", 1);
		Assertions.assertEquals(Long.valueOf(111), a1.getLong("b"));
	}
}
