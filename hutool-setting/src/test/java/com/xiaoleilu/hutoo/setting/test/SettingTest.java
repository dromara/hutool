package com.xiaoleilu.hutoo.setting.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xiaoleilu.hutool.log.LogFactory;
import com.xiaoleilu.hutool.log.dialect.console.ConsoleLogFactory;
import com.xiaoleilu.hutool.setting.Setting;

/**
 * Setting单元测试
 * @author Looly
 *
 */
public class SettingTest {
	
	@Before
	public void init(){
		LogFactory.setCurrentLogFactory(ConsoleLogFactory.class);
	}
	
	@Test
	public void settingTest(){
		Setting setting = new Setting("test.setting", true);
		
		String driver = setting.getByGroup("driver", "demo");
		Assert.assertEquals(driver, "com.mysql.jdbc.Driver");
		
		String user = setting.getByGroup("user", "demo");
		Assert.assertEquals(user, "rootcom.mysql.jdbc.Driver");
	}
}
