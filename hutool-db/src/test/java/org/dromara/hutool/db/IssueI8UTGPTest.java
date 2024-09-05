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
import org.dromara.hutool.db.config.DbConfig;
import org.dromara.hutool.db.config.SettingConfigParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class IssueI8UTGPTest {

	@Test
	void customSqlLogTest() {
		final AtomicBoolean isFilterValid = new AtomicBoolean(false);

		final DbConfig dbConfig = DbConfig.of()
			.setUrl("jdbc:sqlite:test.db")
			.addSqlFilter((conn, boundSql, returnGeneratedKey) -> {
				isFilterValid.set(true);
				Console.log("Custom log: {}", boundSql.getSql());
			});

		final Db db = Db.of(dbConfig);

		final List<Entity> find = db.query("select * from user where age = ?", 18);
		Assertions.assertEquals("王五", find.get(0).get("name"));

		Assertions.assertTrue(isFilterValid.get());
	}

	@Test
	void customSqlLogTest2() {
		final AtomicBoolean isFilterValid = new AtomicBoolean(false);

		final DbConfig dbConfig = SettingConfigParser.of().parse("test");
		dbConfig.addSqlFilter((conn, boundSql, returnGeneratedKey) -> {
				isFilterValid.set(true);
				Console.log("Custom log: {}", boundSql.getSql());
			});

		final Db db = Db.of(dbConfig);

		final List<Entity> find = db.query("select * from user where age = ?", 18);
		Assertions.assertEquals("王五", find.get(0).get("name"));

		Assertions.assertTrue(isFilterValid.get());
	}
}
