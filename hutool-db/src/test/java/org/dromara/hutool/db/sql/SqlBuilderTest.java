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

package org.dromara.hutool.db.sql;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SqlBuilderTest {

	@Test
	public void queryNullTest() {
		final SqlBuilder builder = SqlBuilder.of().select().from("user").where(new Condition("name", "= null"));
		Assertions.assertEquals("SELECT * FROM user WHERE name IS NULL", builder.build());

		final SqlBuilder builder2 = SqlBuilder.of().select().from("user").where(new Condition("name", "is null"));
		Assertions.assertEquals("SELECT * FROM user WHERE name IS NULL", builder2.build());

		final SqlBuilder builder3 = SqlBuilder.of().select().from("user").where(new Condition("name", "!= null"));
		Assertions.assertEquals("SELECT * FROM user WHERE name IS NOT NULL", builder3.build());

		final SqlBuilder builder4 = SqlBuilder.of().select().from("user").where(new Condition("name", "is not null"));
		Assertions.assertEquals("SELECT * FROM user WHERE name IS NOT NULL", builder4.build());
	}

	@Test
	public void orderByTest() {
		final SqlBuilder builder = SqlBuilder.of().select("id", "username").from("user")
				.join("role", SqlBuilder.Join.INNER)
				.on("user.id = role.user_id")
				.where(new Condition("age", ">=", 18),
						new Condition("username", "abc", Condition.LikeType.Contains)
				).orderBy(new Order("id"));

		Assertions.assertEquals("SELECT id,username FROM user INNER JOIN role ON user.id = role.user_id WHERE age >= ? AND username LIKE ? ORDER BY id", builder.build());
	}

	@Test
	public void likeTest() {
		final Condition conditionEquals = new Condition("user", "123", Condition.LikeType.Contains);
		conditionEquals.setPlaceHolder(false);

		final SqlBuilder sqlBuilder = new SqlBuilder();
		sqlBuilder.select("id");
		sqlBuilder.from("user");
		sqlBuilder.where(conditionEquals);
		final String s1 = sqlBuilder.build();
		Assertions.assertEquals("SELECT id FROM user WHERE user LIKE '%123%'", s1);
	}
}
