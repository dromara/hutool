package cn.hutool.db;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

/**
 * Derby数据库单元测试
 *
 * @author looly
 *
 */
public class DerbyTest {

	private static final String DS_GROUP_NAME = "derby";

	@BeforeClass
	public static void init() throws SQLException {
		final Db db = Db.of(DS_GROUP_NAME);
		db.execute("CREATE TABLE test(a INTEGER, b BIGINT)");

		db.insert(Entity.of("test").set("a", 1).set("b", 11));
		db.insert(Entity.of("test").set("a", 2).set("b", 21));
		db.insert(Entity.of("test").set("a", 3).set("b", 31));
		db.insert(Entity.of("test").set("a", 4).set("b", 41));
	}

	@Test
	@Ignore
	public void queryTest() throws SQLException {
		final List<Entity> query = Db.of(DS_GROUP_NAME).query("select * from test");
		Assert.assertEquals(4, query.size());
	}

	@Test
	@Ignore
	public void findTest() throws SQLException {
		final List<Entity> query = Db.of(DS_GROUP_NAME).find(Entity.of("test"));
		Assert.assertEquals(4, query.size());
	}
}
