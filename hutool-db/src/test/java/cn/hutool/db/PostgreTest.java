package cn.hutool.db;

import cn.hutool.core.lang.Console;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * PostgreSQL 单元测试
 *
 * @author looly
 */
public class PostgreTest {

	@Test
	@Ignore
	public void insertTest() {
		for (int id = 100; id < 200; id++) {
			Db.of("postgre").insert(Entity.of("user")//
					.set("id", id)//
					.set("name", "测试用户" + id)//
			);
		}
	}

	@Test
	@Ignore
	public void pageTest() {
		final PageResult<Entity> result = Db.of("postgre").page(Entity.of("user"), new Page(2, 10));
		for (final Entity entity : result) {
			Console.log(entity.get("id"));
		}
	}

	@Test
	@Ignore
	public void upsertTest() {
		final Db db = Db.of("postgre");
		db.executeBatch("drop table if exists ctest",
				"create table if not exists \"ctest\" ( \"id\" serial4, \"t1\" varchar(255) COLLATE \"pg_catalog\".\"default\", \"t2\" varchar(255) COLLATE \"pg_catalog\".\"default\", \"t3\" varchar(255) COLLATE \"pg_catalog\".\"default\", CONSTRAINT \"ctest_pkey\" PRIMARY KEY (\"id\") )  ");
		db.insert(Entity.of("ctest").set("id", 1).set("t1", "111").set("t2", "222").set("t3", "333"));
		db.upsert(Entity.of("ctest").set("id", 1).set("t1", "new111").set("t2", "new222").set("t3", "bew333"),"id");
		final Entity et=db.get(Entity.of("ctest").set("id", 1));
		Assert.assertEquals("new111",et.getStr("t1"));
	}
}
