package cn.hutool.db.dialect;

import org.junit.Assert;
import org.junit.Test;

public class DriverUtilTest {

	@Test
	public void identifyDriverTest(){
		String url = "jdbc:h2:file:./db/test;AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL";
		String driver = DriverUtil.identifyDriver(url); // driver 返回 mysql 的 driver
		Assert.assertEquals("org.h2.Driver", driver);
	}
}
