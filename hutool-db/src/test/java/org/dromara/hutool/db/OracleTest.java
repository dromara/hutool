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
import org.dromara.hutool.db.sql.Query;
import org.dromara.hutool.db.sql.SqlBuilder;
import org.dromara.hutool.db.sql.SqlUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Oracle操作单元测试
 *
 * @author looly
 *
 */
public class OracleTest {

	@Test
	public void oraclePageSqlTest() {
		final Page page = new Page(0, 10);
		final Entity where = Entity.of("PMCPERFORMANCEINFO").set("yearPI", "2017");
		final Query query = new Query(SqlUtil.buildConditions(where), where.getTableName());
		query.setPage(page);

		final SqlBuilder find = SqlBuilder.of().query(query).orderBy(page.getOrders());
		final int[] startEnd = page.getStartEnd();
		final SqlBuilder builder = SqlBuilder.of().append("SELECT * FROM ( SELECT row_.*, rownum rownum_ from ( ")//
				.append(find)//
				.append(" ) row_ where rownum <= ").append(startEnd[1])//
				.append(") table_alias")//
				.append(" where table_alias.rownum_ >= ").append(startEnd[0]);//

		final String ok = "SELECT * FROM "//
				+ "( SELECT row_.*, rownum rownum_ from ( SELECT * FROM PMCPERFORMANCEINFO WHERE yearPI = ? ) row_ "//
				+ "where rownum <= 10) table_alias where table_alias.rownum_ >= 0";//
		Assertions.assertEquals(ok, builder.toString());
	}

	@Test
	@Disabled
	public void insertTest() {
		for (int id = 100; id < 200; id++) {
			Db.of("orcl").insert(Entity.of("T_USER")//
					.set("ID", id)//
					.set("name", "测试用户" + id)//
					.set("TEXT", "描述" + id)//
					.set("TEST1", "t" + id)//
			);
		}
	}

	@Test
	@Disabled
	public void pageTest() {
		final PageResult<Entity> result = Db.of("orcl").page(Entity.of("T_USER"), new Page(2, 10));
		for (final Entity entity : result) {
			Console.log(entity.get("ID"));
		}
	}

}
