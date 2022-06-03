package cn.hutool.db;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * H2数据库单元测试
 *
 * @author looly
 *
 */
public class H2Test {

	private static final String DS_GROUP_NAME = "h2";

	@BeforeClass
	public static void init() {
		final Db db = Db.of(DS_GROUP_NAME);
		db.execute("CREATE TABLE test(a INTEGER, b BIGINT)");

		db.insert(Entity.of("test").set("a", 1).set("b", 11));
		db.insert(Entity.of("test").set("a", 2).set("b", 21));
		db.insert(Entity.of("test").set("a", 3).set("b", 31));
		db.insert(Entity.of("test").set("a", 4).set("b", 41));
	}

	@Test
	public void queryTest() {
		final List<Entity> query = Db.of(DS_GROUP_NAME).query("select * from test");
		Assert.assertEquals(4, query.size());
	}

	@Test
	public void findTest() {
		final List<Entity> query = Db.of(DS_GROUP_NAME).find(Entity.of("test"));
		Assert.assertEquals(4, query.size());
	}

	@Test
	public void upsertTest() {
		final Db db=Db.of(DS_GROUP_NAME);
		db.upsert(Entity.of("test").set("a",1).set("b",111),"a");
		final Entity a1=db.get("test","a",1);
		Assert.assertEquals(Long.valueOf(111),a1.getLong("b"));
	}
}
