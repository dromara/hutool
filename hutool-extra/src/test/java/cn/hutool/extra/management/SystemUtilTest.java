package cn.hutool.extra.management;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class SystemUtilTest {

	@Test
	@Ignore
	public void dumpTest() {
		ManagementUtil.dumpSystemInfo();
	}

	@Test
	public void getCurrentPidTest() {
		long pid = ManagementUtil.getCurrentPID();
		Assert.assertTrue(pid > 0);
	}

	@Test
	public void getJavaInfoTest() {
		JavaInfo javaInfo = ManagementUtil.getJavaInfo();
		Assert.assertNotNull(javaInfo);
	}

	@Test
	public void getJavaRuntimeInfoTest() {
		JavaRuntimeInfo info = ManagementUtil.getJavaRuntimeInfo();
		Assert.assertNotNull(info);
	}

	@Test
	public void getOsInfoTest() {
		OsInfo osInfo = ManagementUtil.getOsInfo();
		Assert.assertNotNull(osInfo);
	}

	@Test
	public void getHostInfo() {
		HostInfo hostInfo = ManagementUtil.getHostInfo();
		Assert.assertNotNull(hostInfo);
	}

	@Test
	public void getUserInfoTest(){
		// https://gitee.com/dromara/hutool/issues/I3NM39
		final UserInfo userInfo = ManagementUtil.getUserInfo();
		Assert.assertTrue(userInfo.getTempDir().endsWith(File.separator));
	}
}
