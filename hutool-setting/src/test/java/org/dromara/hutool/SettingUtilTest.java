package org.dromara.hutool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SettingUtilTest {

	@Test
	public void getTest() {
		final String driver = SettingUtil.get("test").getStrByGroup("driver", "demo");
		Assertions.assertEquals("com.mysql.jdbc.Driver", driver);
	}

	@Test
	public void getTest2() {
		final String driver = SettingUtil.get("example/example").getStrByGroup("key", "demo");
		Assertions.assertEquals("value", driver);
	}

	@Test
	public void getFirstFoundTest() {
		//noinspection ConstantConditions
		final String driver = SettingUtil.getFirstFound("test2", "test")
				.getStrByGroup("driver", "demo");
		Assertions.assertEquals("com.mysql.jdbc.Driver", driver);
	}
}
