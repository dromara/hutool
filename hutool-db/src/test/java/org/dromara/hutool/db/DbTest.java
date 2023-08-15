package org.dromara.hutool.db;

import org.dromara.hutool.core.util.RandomUtil;
import org.dromara.hutool.db.handler.EntityListHandler;
import org.dromara.hutool.db.sql.Condition;
import org.dromara.hutool.log.LogUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Db对象单元测试
 *
 * @author looly
 */
public class DbTest {

	@Test
	public void queryTest() {
		final List<Entity> find = Db.of().query("select * from user where age = ?", 18);
		Assertions.assertEquals("王五", find.get(0).get("name"));
	}

	@Test
	public void findTest() {
		final List<Entity> find = Db.of().find(Entity.of("user").set("age", 18));
		Assertions.assertEquals("王五", find.get(0).get("name"));
	}

	@Test
	public void pageTest() {
		// 测试数据库中一共4条数据，第0页有3条，第1页有1条
		final List<Entity> page0 = Db.of().page(Entity.of("user"),
			Page.of(0, 3));
		Assertions.assertEquals(3, page0.size());

		final List<Entity> page1 = Db.of().page(Entity.of("user"), Page.of(1, 3));
		Assertions.assertEquals(1, page1.size());
	}

	@Test
	public void pageBySqlTest() {
		final String sql = "select * from user order by name";
		// 测试数据库中一共4条数据，第0页有3条，第1页有1条
		final List<Entity> page0 = Db.of().page(
			sql, Page.of(0, 3));
		Assertions.assertEquals(3, page0.size());

		final List<Entity> page1 = Db.of().page(
			sql, Page.of(1, 3));
		Assertions.assertEquals(1, page1.size());
	}

	@Test
	public void pageBySqlWithInTest() {
		// in和其他条件混用
		final String sql = "select * from user where age > :age and name in (:names) order by name";
		// 测试数据库中一共4条数据，第0页有3条，第1页有1条
		final List<Entity> page0 = Db.of().page(
			sql, Page.of(0, 3),
			Entity.of().set("age", 12)
				.set("names", new String[]{"张三", "王五"})
		);
		Assertions.assertEquals(1, page0.size());
	}

	@Test
	public void pageWithParamsTest() {
		final String sql = "select * from user where name = ?";
		final PageResult<Entity> result = Db.of().page(
			sql, Page.of(0, 3), "张三");

		Assertions.assertEquals(2, result.getTotal());
		Assertions.assertEquals(1, result.getTotalPage());
		Assertions.assertEquals(2, result.size());
	}

	@Test
	public void countTest() {
		final long count = Db.of().count("select * from user");
		Assertions.assertEquals(4, count);
	}

	@Test
	public void countByQueryTest() {
		final long count = Db.of().count(Entity.of("user"));
		Assertions.assertEquals(4, count);
	}

	@Test
	public void countTest2() {
		final long count = Db.of().count("select * from user order by name DESC");
		Assertions.assertEquals(4, count);
	}

	@Test
	public void findLikeTest() {
		// 方式1
		List<Entity> find = Db.of().find(Entity.of("user").set("name", "like 王%"));
		Assertions.assertEquals("王五", find.get(0).get("name"));

		// 方式2
		find = Db.of().findLike("user", "name", "王", Condition.LikeType.StartWith);
		Assertions.assertEquals("王五", find.get(0).get("name"));

		// 方式3
		find = Db.of().query("select * from user where name like ?", "王%");
		Assertions.assertEquals("王五", find.get(0).get("name"));
	}

	@Test
	public void findByTest() {
		final List<Entity> find = Db.of().findBy("user",
			Condition.parse("age", "> 18"),
			Condition.parse("age", "< 100")
		);
		for (final Entity entity : find) {
			LogUtil.debug("{}", entity);
		}
		Assertions.assertEquals("unitTestUser", find.get(0).get("name"));
	}

	@Test
	@Disabled
	public void txTest() throws SQLException {
		Db.of().tx(db -> {
			db.insert(Entity.of("user").set("name", "unitTestuser2"));
			db.update(Entity.of().set("age", 79), Entity.of("user").set("name", "unitTestuser2"));
			db.del("user", "name", "unitTestuser2");
		});
	}

	@Test
	@Disabled
	public void batchInsertTest() throws SQLException {
		final List<Entity> es = new ArrayList<>();
		final String tableName = "act_ge_property";
		final Entity e = buildEntity();
		es.add(e);
		Db.of().tx(db -> {
			db.insert(es);
			final List<Entity> ens = Db.of().find(e);
			Assertions.assertEquals(1, ens.size());
			db.del(tableName, "NAME_", "!= null");
		});
	}

	@Test
	@Disabled
	public void batchInsertBatch() throws SQLException {
		final List<Entity> es = new ArrayList<>();
		final String tableName = "act_ge_property";
		final Entity e = buildEntity();
		es.add(e);
		es.add(buildEntity());
		es.add(buildEntity());
		es.add(buildEntity());
		Db.of().tx(db -> {
			db.insert(es);
			final List<Entity> ens = Db.of().find(e);
			Assertions.assertEquals(1, ens.size());
			db.del(tableName, "NAME_", "!= null");
		});


	}

	private Entity buildEntity() {
		final Entity entity = Entity.of("act_ge_property");
		entity.put("NAME_", RandomUtil.randomString(15));
		entity.put("VALUE_", RandomUtil.randomString(50));
		entity.put("REV_", RandomUtil.randomInt());
		return entity;
	}

	@Test
	@Disabled
	public void queryFetchTest() {
		// https://gitee.com/dromara/hutool/issues/I4JXWN
		Db.of().query((conn -> {
			final PreparedStatement ps = conn.prepareStatement("select * from table",
				ResultSet.TYPE_FORWARD_ONLY,
				ResultSet.CONCUR_READ_ONLY);
			ps.setFetchSize(Integer.MIN_VALUE);
			ps.setFetchDirection(ResultSet.FETCH_FORWARD);
			return ps;
		}), EntityListHandler.of());
	}

	@Test
	@Disabled
	public void findWithDotTest() {
		// 当字段带有点时，导致被拆分分别wrap了，因此此时关闭自动包装即可
		Db.of().disableWrapper().find(Entity.of("user").set("a.b", "1"));
	}
}
