package cn.hutool.setting;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SettingUtilTest {

	@Test
	public void getTest() {
		String driver = SettingUtil.get("test").get("demo", "driver");
		Assertions.assertEquals("com.mysql.jdbc.Driver", driver);
	}

	@Test
	public void getTest2() {
		String driver = SettingUtil.get("example/example").get("demo", "key");
		Assertions.assertEquals("value", driver);
	}

	@Test
	public void getFirstFoundTest() {
		//noinspection ConstantConditions
		String driver = SettingUtil.getFirstFound("test2", "test")
				.get("demo", "driver");
		Assertions.assertEquals("com.mysql.jdbc.Driver", driver);
	}
}
