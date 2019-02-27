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
}
