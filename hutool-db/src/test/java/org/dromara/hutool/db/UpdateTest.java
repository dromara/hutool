package org.dromara.hutool.db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class UpdateTest {

	Db db;

	@BeforeEach
	public void init() {
		db = Db.of("test");
	}

	/**
	 * 对更新做单元测试
	 *
	 */
	@Test
	@Disabled
	public void updateTest() {

		// 改
		final int update = db.update(Entity.of("user").set("age", 88), Entity.of().set("name", "unitTestUser"));
		Assertions.assertTrue(update > 0);
		final Entity result2 = db.get("user", "name", "unitTestUser");
		Assertions.assertSame(88, result2.getInt("age"));
	}
}
