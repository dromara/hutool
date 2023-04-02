package org.dromara.hutool.management;

import org.dromara.hutool.util.SystemUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

public class SystemUtilTest {

	@Test
	@Disabled
	public void dumpTest() {
		ManagementUtil.dumpSystemInfo();
	}

	@Test
	public void getCurrentPidTest() {
		final long pid = ManagementUtil.getCurrentPID();
		Assertions.assertTrue(pid > 0);
	}

	@Test
	public void getJavaInfoTest() {
		final JavaInfo javaInfo = ManagementUtil.getJavaInfo();
		Assertions.assertNotNull(javaInfo);
	}

	@Test
	public void getJavaRuntimeInfoTest() {
		final JavaRuntimeInfo info = ManagementUtil.getJavaRuntimeInfo();
		Assertions.assertNotNull(info);
	}

	@Test
	public void getOsInfoTest() {
		final OsInfo osInfo = ManagementUtil.getOsInfo();
		Assertions.assertNotNull(osInfo);
	}

	@Test
	public void getHostInfo() {
		final HostInfo hostInfo = ManagementUtil.getHostInfo();
		Assertions.assertNotNull(hostInfo);
	}

	@Test
	public void getUserInfoTest(){
		// https://gitee.com/dromara/hutool/issues/I3NM39
		final UserInfo userInfo = ManagementUtil.getUserInfo();
		Assertions.assertTrue(userInfo.getTempDir().endsWith(File.separator));
	}

	@Test
	public void getOsVersionTest(){
		String s = SystemUtil.get("os.name");
		Assertions.assertNotNull(s);
		s = SystemUtil.get("os.version");
		Assertions.assertNotNull(s);
	}
}
