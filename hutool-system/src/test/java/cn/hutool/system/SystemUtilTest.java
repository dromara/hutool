package cn.hutool.system;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

public class SystemUtilTest {

	@Test
	@Disabled
	public void dumpTest() {
		SystemUtil.dumpSystemInfo();
	}

	@Test
	public void getCurrentPidTest() {
		long pid = SystemUtil.getCurrentPID();
		Assertions.assertTrue(pid > 0);
	}

	@Test
	public void getJavaInfoTest() {
		JavaInfo javaInfo = SystemUtil.getJavaInfo();
		Assertions.assertNotNull(javaInfo);
	}

	@Test
	public void getJavaRuntimeInfoTest() {
		JavaRuntimeInfo info = SystemUtil.getJavaRuntimeInfo();
		Assertions.assertNotNull(info);
	}

	@Test
	public void getOsInfoTest() {
		OsInfo osInfo = SystemUtil.getOsInfo();
		Assertions.assertNotNull(osInfo);
	}

	@Test
	public void getHostInfo() {
		HostInfo hostInfo = SystemUtil.getHostInfo();
		Assertions.assertNotNull(hostInfo);
	}

	@Test
	public void getUserInfoTest(){
		// https://gitee.com/dromara/hutool/issues/I3NM39
		final UserInfo userInfo = SystemUtil.getUserInfo();
		Assertions.assertTrue(userInfo.getTempDir().endsWith(File.separator));
	}
}
