package cn.hutool.system;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class SystemUtilTest {
	
	@Test
	@Ignore
	public void dumpTest() {
		SystemUtil.dumpSystemInfo();
	}
	
	@Test
	public void getCurrentPidTest() {
		long pid = SystemUtil.getCurrentPID();
		Assert.assertTrue(pid > 0);
	}
}
