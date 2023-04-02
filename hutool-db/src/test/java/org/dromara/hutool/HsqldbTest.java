package org.dromara.hutool;

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
