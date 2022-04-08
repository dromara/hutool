package cn.hutool.db;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ArrayUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

/**
 * MySQL操作单元测试
 *
 * @author looly
 */
public class MySQLTest {
	@BeforeClass
	@Ignore
	public static void createTable() throws SQLException {
		Db db = Db.use("mysql");
		db.executeBatch("drop table if exists testuser", "CREATE TABLE if not exists `testuser` ( `id` int(11) NOT NULL, `account` varchar(255) DEFAULT NULL, `pass` varchar(255) DEFAULT NULL, PRIMARY KEY (`id`) ) ENGINE=InnoDB DEFAULT CHARSET=utf8");
	}

	@Test
	@Ignore
	public void insertTest() throws SQLException {
		for (int id = 100; id < 200; id++) {
			Db.use("mysql").insert(Entity.create("user")//
					.set("id", id)//
					.set("name", "测试用户" + id)//
					.set("text", "描述" + id)//
					.set("test1", "t" + id)//
			);
		}
	}

	/**
	 * 事务测试<br>
	 * 更新三条信息，低2条后抛出异常，正常情况下三条都应该不变
	 *
	 * @throws SQLException SQL异常
	 */
	@Test(expected = SQLException.class)
	@Ignore
	public void txTest() throws SQLException {
		Db.use("mysql").tx(db -> {
			int update = db.update(Entity.create("user").set("text", "描述100"), Entity.create().set("id", 100));
			db.update(Entity.create("user").set("text", "描述101"), Entity.create().set("id", 101));
			if (1 == update) {
				// 手动指定异常，然后测试回滚触发
				throw new RuntimeException("Error");
			}
			db.update(Entity.create("user").set("text", "描述102"), Entity.create().set("id", 102));
		});
	}

	@Test
	@Ignore
	public void pageTest() throws SQLException {
		PageResult<Entity> result = Db.use("mysql").page(Entity.create("user"), new Page(2, 10));
		for (Entity entity : result) {
			Console.log(entity.get("id"));
		}
	}

	@Test
	@Ignore
	public void getTimeStampTest() throws SQLException {
		final List<Entity> all = Db.use("mysql").findAll("test");
		Console.log(all);
	}

	@Test
	@Ignore
	public void upsertTest() throws SQLException {
		Db db = Db.use("mysql");
		db.insert(Entity.create("testuser").set("id", 1).set("account", "ice").set("pass", "123456"));
		db.upsert(Entity.create("testuser").set("id", 1).set("account", "icefairy").set("pass", "a123456"));
		Entity user = db.get(Entity.create("testuser").set("id", 1));
		System.out.println("user======="+user.getStr("account")+"___"+user.getStr("pass"));
		Assert.assertEquals(user.getStr("account"), new String("icefairy"));
	}
}
