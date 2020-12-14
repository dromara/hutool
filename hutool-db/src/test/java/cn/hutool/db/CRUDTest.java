package cn.hutool.db;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.pojo.User;
import cn.hutool.db.sql.Condition;
import cn.hutool.db.sql.Condition.LikeType;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

/**
 * 增删改查测试
 *
 * @author looly
 */
public class CRUDTest {

	private static final Db db = Db.use("test");

	@Test
	public void findIsNullTest() throws SQLException {
		List<Entity> results = db.findAll(Entity.create("user").set("age", "is null"));
		Assert.assertEquals(0, results.size());
	}

	@Test
	public void findIsNullTest2() throws SQLException {
		List<Entity> results = db.findAll(Entity.create("user").set("age", "= null"));
		Assert.assertEquals(0, results.size());
	}

	@Test
	public void findIsNullTest3() throws SQLException {
		List<Entity> results = db.findAll(Entity.create("user").set("age", null));
		Assert.assertEquals(0, results.size());
	}

	@Test
	public void findBetweenTest() throws SQLException {
		List<Entity> results = db.findAll(Entity.create("user").set("age", "between '18' and '40'"));
		Assert.assertEquals(1, results.size());
	}

	@Test
	public void findByBigIntegerTest() throws SQLException {
		List<Entity> results = db.findAll(Entity.create("user").set("age", new BigInteger("12")));
		Assert.assertEquals(2, results.size());
	}

	@Test
	public void findByBigDecimalTest() throws SQLException {
		List<Entity> results = db.findAll(Entity.create("user").set("age", new BigDecimal("12")));
		Assert.assertEquals(2, results.size());
	}

	@Test
	public void findLikeTest() throws SQLException {
		List<Entity> results = db.findAll(Entity.create("user").set("name", "like \"%三%\""));
		Assert.assertEquals(2, results.size());
	}

	@Test
	public void findLikeTest2() throws SQLException {
		List<Entity> results = db.findAll(Entity.create("user").set("name", new Condition("name", "三", LikeType.Contains)));
		Assert.assertEquals(2, results.size());
	}

	@Test
	public void findLikeTest3() throws SQLException {
		List<Entity> results = db.findAll(Entity.create("user").set("name", new Condition("name", null, LikeType.Contains)));
		Assert.assertEquals(0, results.size());
	}

	@Test
	public void findInTest() throws SQLException {
		List<Entity> results = db.findAll(Entity.create("user").set("id", "in 1,2,3"));
		Console.log(results);
		Assert.assertEquals(2, results.size());
	}

	@Test
	public void findInTest2() throws SQLException {
		List<Entity> results = db.findAll(Entity.create("user")
				.set("id", new Condition("id", new long[]{1, 2, 3})));
		Assert.assertEquals(2, results.size());
	}

	@Test
	public void findInTest3() throws SQLException {
		List<Entity> results = db.findAll(Entity.create("user")
				.set("id", new long[]{1, 2, 3}));
		Assert.assertEquals(2, results.size());
	}

	@Test
	public void findAllTest() throws SQLException {
		List<Entity> results = db.findAll("user");
		Assert.assertEquals(4, results.size());
	}

	@Test
	public void findTest() throws SQLException {
		List<Entity> find = db.find(CollUtil.newArrayList("name AS name2"), Entity.create("user"), new EntityListHandler());
		Assert.assertFalse(find.isEmpty());
	}

	@Test
	public void findActiveTest() {
		ActiveEntity entity = new ActiveEntity(db, "user");
		entity.setFieldNames("name AS name2").load();
		Assert.assertEquals("user", entity.getTableName());
		Assert.assertFalse(entity.isEmpty());
	}

	/**
	 * 对增删改查做单元测试
	 *
	 * @throws SQLException SQL异常
	 */
	@Test
	@Ignore
	public void crudTest() throws SQLException {

		// 增
		Long id = db.insertForGeneratedKey(Entity.create("user").set("name", "unitTestUser").set("age", 66));
		Assert.assertTrue(id > 0);
		Entity result = db.get("user", "name", "unitTestUser");
		Assert.assertSame(66, result.getInt("age"));

		// 改
		int update = db.update(Entity.create().set("age", 88), Entity.create("user").set("name", "unitTestUser"));
		Assert.assertTrue(update > 0);
		Entity result2 = db.get("user", "name", "unitTestUser");
		Assert.assertSame(88, result2.getInt("age"));

		// 删
		int del = db.del("user", "name", "unitTestUser");
		Assert.assertTrue(del > 0);
		Entity result3 = db.get("user", "name", "unitTestUser");
		Assert.assertNull(result3);
	}

	@Test
	@Ignore
	public void insertBatchTest() throws SQLException {
		User user1 = new User();
		user1.setName("张三");
		user1.setAge(12);
		user1.setBirthday("19900112");
		user1.setGender(true);

		User user2 = new User();
		user2.setName("李四");
		user2.setAge(12);
		user2.setBirthday("19890512");
		user2.setGender(false);

		Entity data1 = Entity.parse(user1);
		data1.put("name", null);
		Entity data2 = Entity.parse(user2);

		Console.log(data1);
		Console.log(data2);

		int[] result = db.insert(CollUtil.newArrayList(data1, data2));
		Console.log(result);
	}

	@Test
	@Ignore
	public void insertBatchOneTest() throws SQLException {
		User user1 = new User();
		user1.setName("张三");
		user1.setAge(12);
		user1.setBirthday("19900112");
		user1.setGender(true);

		Entity data1 = Entity.parse(user1);

		Console.log(data1);

		int[] result = db.insert(CollUtil.newArrayList(data1));
		Console.log(result);
	}
}
