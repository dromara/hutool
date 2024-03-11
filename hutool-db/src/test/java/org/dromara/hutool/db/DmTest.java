package org.dromara.hutool.db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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
