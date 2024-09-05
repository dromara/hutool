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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * HSQLDB数据库单元测试
 *
 * @author looly
 *
 */
public class HsqldbTest {

	private static final String DS_GROUP_NAME = "hsqldb";

	@BeforeAll
	public static void init() {
		final Db db = Db.of(DS_GROUP_NAME);
		db.execute("CREATE TABLE test(a INTEGER, b BIGINT)");
		db.insert(Entity.of("test").set("a", 1).set("b", 11));
		db.insert(Entity.of("test").set("a", 2).set("b", 21));
		db.insert(Entity.of("test").set("a", 3).set("b", 31));
		db.insert(Entity.of("test").set("a", 4).set("b", 41));
	}

	@Test
	public void connTest() {
		final List<Entity> query = Db.of(DS_GROUP_NAME).query("select * from test");
		Assertions.assertEquals(4, query.size());
	}

	@Test
	public void findTest() {
		final List<Entity> query = Db.of(DS_GROUP_NAME).find(Entity.of("test"));
		Assertions.assertEquals(4, query.size());
	}
}
