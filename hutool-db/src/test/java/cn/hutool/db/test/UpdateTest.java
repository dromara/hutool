package cn.hutool.db.test;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;

public class UpdateTest {
	
	Db db;

	@Before
	public void init() {
		db = Db.use("test");
	}
	
	/**
	 * 对更新做单元测试
	 * 
	 * @throws SQLException
	 */
	@Test
	@Ignore
	public void updateTest() throws SQLException {

		// 改
		int update = db.update(Entity.create("user").set("age", 88), Entity.create().set("name", "unitTestUser"));
		Assert.assertTrue(update > 0);
		Entity result2 = db.get("user", "name", "unitTestUser");
		Assert.assertSame(88, (int) result2.getInt("age"));
	}
}
