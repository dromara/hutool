package cn.hutool.db.test;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.db.Entity;
import cn.hutool.db.SqlRunner;

public class UpdateTest {
	
	SqlRunner runner;

	@Before
	public void init() {
		runner = SqlRunner.create("test");
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
		int update = runner.update(Entity.create("user").set("age", 88), Entity.create().set("name", "unitTestUser"));
		Assert.assertTrue(update > 0);
		Entity result2 = runner.get("user", "name", "unitTestUser");
		Assert.assertSame(88, (int) result2.getInt("age"));
	}
}
