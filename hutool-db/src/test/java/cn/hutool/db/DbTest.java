package cn.hutool.db;

import cn.hutool.db.sql.Condition;
import cn.hutool.log.StaticLog;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

/**
 * Db对象单元测试
 * @author looly
 *
 */
public class DbTest {

	@Test
	public void queryTest() throws SQLException {
		List<Entity> find = Db.use().query("select * from user where age = ?", 18);
		Assert.assertEquals("王五", find.get(0).get("name"));
	}
	
	@Test
	public void findTest() throws SQLException {
		List<Entity> find = Db.use().find(Entity.create("user").set("age", 18));
		Assert.assertEquals("王五", find.get(0).get("name"));
	}

	@Test
	public void pageTest() throws SQLException {
		// 测试数据库中一共4条数据，第0页有3条，第1页有1条
		List<Entity> page0 = Db.use().page(Entity.create("user"), 0, 3);
		Assert.assertEquals(3, page0.size());
		List<Entity> page1 = Db.use().page(Entity.create("user"), 1, 3);
		Assert.assertEquals(1, page1.size());
	}

	@Test
	public void findLikeTest() throws SQLException {
		// 方式1
		List<Entity> find = Db.use().find(Entity.create("user").set("name", "like 王%"));
		Assert.assertEquals("王五", find.get(0).get("name"));

		// 方式2
		find = Db.use().findLike("user", "name", "王", Condition.LikeType.StartWith);
		Assert.assertEquals("王五", find.get(0).get("name"));

		// 方式3
		find = Db.use().query("select * from user where name like ?", "王%");
		Assert.assertEquals("王五", find.get(0).get("name"));
	}

	@Test
	public void findByTest() throws SQLException {
		List<Entity> find = Db.use().findBy("user",
				Condition.parse("age", "> 18"), 
				Condition.parse("age", "< 100")
		);
		for (Entity entity : find) {
			StaticLog.debug("{}", entity);
		}
		Assert.assertEquals("unitTestUser", find.get(0).get("name"));
	}
	
	@Test
	@Ignore
	public void txTest() throws SQLException {
		Db.use().tx(db -> {
			db.insert(Entity.create("user").set("name", "unitTestUser2"));
			db.update(Entity.create().set("age", 79), Entity.create("user").set("name", "unitTestUser2"));
			db.del("user", "name", "unitTestUser2");
		});
	}
}
