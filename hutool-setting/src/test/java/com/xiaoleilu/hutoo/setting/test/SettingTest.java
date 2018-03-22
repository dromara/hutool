package com.xiaoleilu.hutoo.setting.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
	
	@Test
	@Ignore
	public void settingTestForAbsPath(){
		Setting setting = new Setting("d:/test.setting", true);
		
		String driver = setting.getByGroup("driver", "demo");
		Assert.assertEquals(driver, "com.mysql.jdbc.Driver");
		
		String user = setting.getByGroup("user", "demo");
		Assert.assertEquals(user, "rootcom.mysql.jdbc.Driver");
	}
	
	@Test
	public void settingTestForCustom() {
		Setting setting = new Setting();
		setting.put("group1.user", "root");
		setting.put("group2.user", "root2");
		setting.set("user", "group3", "root3");
		setting.set("user", "root4");
		
		Assert.assertEquals("root", setting.getByGroup("user", "group1"));
		Assert.assertEquals("root2", setting.getByGroup("user", "group2"));
		Assert.assertEquals("root3", setting.getByGroup("user", "group3"));
		Assert.assertEquals("root4", setting.get("user"));
	}
}
