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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * SQL Server操作单元测试
 *
 * @author looly
 *
 */
public class SqlServerTest {

	@Test
	@Disabled
	public void createTableTest() {
		Db.of("sqlserver").execute("create table T_USER(ID bigint, name varchar(255))");
	}

	@Test
	@Disabled
	public void insertTest() {
		for (int id = 100; id < 200; id++) {
			Db.of("sqlserver").insert(Entity.of("T_USER")//
					.set("ID", id)//
					.set("name", "测试用户" + id)//
			);
		}
	}

	@Test
	@Disabled
	public void pageTest() {
		final PageResult<Entity> result = Db.of("sqlserver").page(Entity.of("T_USER"), new Page(2, 10));
		for (final Entity entity : result) {
			Console.log(entity.get("ID"));
		}
	}

}
