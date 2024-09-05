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

import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * PostgreSQL 单元测试
 *
 * @author looly
 */
public class PostgreTest {

	@Test
	@Disabled
	public void insertTest() {
		for (int id = 100; id < 200; id++) {
			Db.of("postgre").insert(Entity.of("user")//
					.set("id", id)//
					.set("name", "测试用户" + id)//
			);
		}
	}

	@Test
	@Disabled
	public void pageTest() {
		final PageResult<Entity> result = Db.of("postgre").page(Entity.of("user"), new Page(2, 10));
		for (final Entity entity : result) {
			Console.log(entity.get("id"));
		}
	}

	@Test
	@Disabled
	public void upsertTest() {
		final Db db = Db.of("postgre");
		db.executeBatch("drop table if exists ctest",
				"create table if not exists \"ctest\" ( \"id\" serial4, \"t1\" varchar(255) COLLATE \"pg_catalog\".\"default\", \"t2\" varchar(255) COLLATE \"pg_catalog\".\"default\", \"t3\" varchar(255) COLLATE \"pg_catalog\".\"default\", CONSTRAINT \"ctest_pkey\" PRIMARY KEY (\"id\") )  ");
		db.insert(Entity.of("ctest").set("id", 1).set("t1", "111").set("t2", "222").set("t3", "333"));
		db.upsert(Entity.of("ctest").set("id", 1).set("t1", "new111").set("t2", "new222").set("t3", "bew333"),"id");
		final Entity et=db.get(Entity.of("ctest").set("id", 1));
		Assertions.assertEquals("new111",et.getStr("t1"));
	}
}
