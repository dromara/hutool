package org.dromara.hutool.db;

import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

/**
 * MySQL操作单元测试
 *
 * @author looly
 */
public class MySQLTest {
	//@BeforeAll
	public static void createTable() {
		final Db db = Db.of("mysql");
		db.executeBatch("drop table if exists testuser", "CREATE TABLE if not exists `testuser` ( `id` int(11) NOT NULL, `account` varchar(255) DEFAULT NULL, `pass` varchar(255) DEFAULT NULL, PRIMARY KEY (`id`) ) ENGINE=InnoDB DEFAULT CHARSET=utf8");
	}

	@Test
	@Disabled
	public void insertTest() {
		for (int id = 100; id < 200; id++) {
			Db.of("mysql").insert(Entity.of("user")//
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
	@Test
	@Disabled
	public void txTest() throws SQLException {
		Assertions.assertThrows(SQLException.class, ()->{
			Db.of("mysql").tx(db -> {
				final int update = db.update(Entity.of("user").set("text", "描述100"), Entity.of().set("id", 100));
				db.update(Entity.of("user").set("text", "描述101"), Entity.of().set("id", 101));
				if (1 == update) {
					// 手动指定异常，然后测试回滚触发
					throw new RuntimeException("Error");
				}
				db.update(Entity.of("user").set("text", "描述102"), Entity.of().set("id", 102));
			});
		});
	}

	@Test
	@Disabled
	public void pageTest() {
		final PageResult<Entity> result = Db.of("mysql").page(Entity.of("user"), new Page(2, 10));
		for (final Entity entity : result) {
			Console.log(entity.get("id"));
		}
	}

	@Test
	@Disabled
	public void getTimeStampTest() {
		final List<Entity> all = Db.of("mysql").findAll("test");
		Console.log(all);
	}

	@Test
	@Disabled
	public void upsertTest() {
		final Db db = Db.of("mysql");
		db.insert(Entity.of("testuser").set("id", 1).set("account", "ice").set("pass", "123456"));
		db.upsert(Entity.of("testuser").set("id", 1).set("account", "icefairy").set("pass", "a123456"));
		final Entity user = db.get(Entity.of("testuser").set("id", 1));
		System.out.println("user======="+user.getStr("account")+"___"+user.getStr("pass"));
		Assertions.assertEquals(user.getStr("account"), "icefairy");
	}
}
