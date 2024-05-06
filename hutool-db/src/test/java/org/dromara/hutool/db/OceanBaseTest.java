package org.dromara.hutool.db;

import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Test;

public class OceanBaseTest {
	@Test
	void connectTest() {
		final Db db = Db.of("ob");
		//db.insert(Entity.of("test").set("id", 1).set("name", "测试"));

		final Entity test = db.get(Entity.of("test"));
		Console.log(test);
	}
}
