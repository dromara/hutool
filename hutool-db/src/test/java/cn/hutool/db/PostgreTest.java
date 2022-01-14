package cn.hutool.db;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.lang.Console;

/**
 * PostgreSQL 单元测试
 *
 * @author looly
 */
public class PostgreTest {

	@Test
	@Ignore
	public void insertTest() throws SQLException {
		for (int id = 100; id < 200; id++) {
			Db.use("postgre").insert(Entity.create("user")//
					.set("id", id)//
					.set("name", "测试用户" + id)//
			);
		}
	}

	@Test
	@Ignore
	public void pageTest() throws SQLException {
		PageResult<Entity> result = Db.use("postgre").page(Entity.create("user"), new Page(2, 10));
		for (Entity entity : result) {
			Console.log(entity.get("id"));
		}
	}

	@Test
	@Ignore
	public void upsertTest() throws SQLException {
		Db db = Db.use("postgre");
		db.executeBatch("drop table if exists ctest",
				"create table if not exists \"ctest\" ( \"id\" serial4, \"t1\" varchar(255) COLLATE \"pg_catalog\".\"default\", \"t2\" varchar(255) COLLATE \"pg_catalog\".\"default\", \"t3\" varchar(255) COLLATE \"pg_catalog\".\"default\", CONSTRAINT \"ctest_pkey\" PRIMARY KEY (\"id\") )  ");
		db.insert(Entity.create("ctest").set("id", 1).set("t1", "111").set("t2", "222").set("t3", "333"));
		db.upsert(Entity.create("ctest").set("id", 1).set("t1", "new111").set("t2", "new222").set("t3", "bew333"),"id");
		Entity et=db.get(Entity.create("ctest").set("id", 1));
		Assert.assertEquals("new111",et.getStr("t1"));
	}
}
