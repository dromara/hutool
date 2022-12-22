package cn.hutool.db;

import cn.hutool.core.map.CaseInsensitiveMap;
import cn.hutool.core.map.MapUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * H2数据库单元测试
 *
 * @author looly
 *
 */
public class H2Test {

	private static final String DS_GROUP_NAME = "h2";

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
	public void queryTest() throws SQLException {
		List<Entity> query = Db.use(DS_GROUP_NAME).query("select * from test");
		Assert.assertEquals(4, query.size());
	}

	@Test
	public void pageTest() throws SQLException {
		String sql = "select * from test where a = @a and b = :b";
		Map<String, Object> paramMap = MapUtil.builder(new CaseInsensitiveMap<String, Object>())
				.put("A", 3)
				.put("b", 31)
				.build();
		List<Entity> query = Db.use(DS_GROUP_NAME).page(sql, Page.of(0, 3), paramMap);
		Assert.assertEquals(1, query.size());
	}

	@Test
	public void findTest() throws SQLException {
		List<Entity> query = Db.use(DS_GROUP_NAME).find(Entity.create("test"));
		Assert.assertEquals(4, query.size());
	}

	@Test
	public void upsertTest() throws SQLException {
		Db db=Db.use(DS_GROUP_NAME);
		db.upsert(Entity.create("test").set("a",1).set("b",111),"a");
		Entity a1=db.get("test","a",1);
		Assert.assertEquals(Long.valueOf(111),a1.getLong("b"));
	}
}
