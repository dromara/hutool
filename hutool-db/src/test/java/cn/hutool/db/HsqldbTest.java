package cn.hutool.db;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

/**
 * HSQLDB数据库单元测试
 * 
 * @author looly
 *
 */
public class HsqldbTest {
	
	private static final String DS_GROUP_NAME = "hsqldb";
	
	@BeforeClass
	public static void init() throws SQLException {
		Db db = Db.use(DS_GROUP_NAME);
		db.execute("CREATE TABLE test(a INTEGER, b BIGINT)");
		db.insert(Entity.create("test").set("a", 1).set("b", 11));
		db.insert(Entity.create("test").set("a", 2).set("b", 21));
		db.insert(Entity.create("test").set("a", 3).set("b", 31));
		db.insert(Entity.create("test").set("a", 4).set("b", 41));
	}
	
	@Test
	public void connTest() throws SQLException {
		List<Entity> query = Db.use(DS_GROUP_NAME).query("select * from test");
		Assert.assertEquals(4, query.size());
	}

	@Test
	public void findTest() throws SQLException {
		List<Entity> query = Db.use(DS_GROUP_NAME).find(Entity.create("test"));
		Assert.assertEquals(4, query.size());
	}
}
