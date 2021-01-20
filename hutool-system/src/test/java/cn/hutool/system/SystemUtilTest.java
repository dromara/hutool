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
	
	@Test
	public void getJavaInfoTest() {
		JavaInfo javaInfo = SystemUtil.getJavaInfo();
		Assert.assertNotNull(javaInfo);
	}
	
	@Test
	public void getOsInfoTest() {
		OsInfo osInfo = SystemUtil.getOsInfo();
		Assert.assertNotNull(osInfo);
	}

	@Test
	public void getHostInfo() {
		HostInfo hostInfo = SystemUtil.getHostInfo();
		Assert.assertNotNull(hostInfo);
	}
	
}
