package cn.hutool.setting.test;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.setting.SettingUtil;

public class SettingUtilTest {
	
	@Test
	public void getTest() {
		String driver = SettingUtil.get("test").get("demo", "driver");
		Assert.assertEquals("com.mysql.jdbc.Driver", driver);
	}

	@Test
	public void getTest2() {
		String driver = SettingUtil.get("example/example").get("demo", "key");
		Assert.assertEquals("value", driver);
	}

	@Test
	public void getFirstFoundTest() {
		//noinspection ConstantConditions
		String driver = SettingUtil.getFirstFound("test2", "test")
				.get("demo", "driver");
		Assert.assertEquals("com.mysql.jdbc.Driver", driver);
	}
}
