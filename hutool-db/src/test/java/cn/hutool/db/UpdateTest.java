package cn.hutool.db;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class UpdateTest {

	Db db;

	@Before
	public void init() {
		db = Db.of("test");
	}

	/**
	 * 对更新做单元测试
	 *
	 */
	@Test
	@Ignore
	public void updateTest() {

		// 改
		final int update = db.update(Entity.of("user").set("age", 88), Entity.of().set("name", "unitTestUser"));
		Assert.assertTrue(update > 0);
		final Entity result2 = db.get("user", "name", "unitTestUser");
		Assert.assertSame(88, result2.getInt("age"));
	}
}
