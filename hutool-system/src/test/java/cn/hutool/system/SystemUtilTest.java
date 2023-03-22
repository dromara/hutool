package cn.hutool.system;

import cn.hutool.core.lang.Console;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class SystemUtilTest {

	@Test
	@Ignore
	public void dumpTest() {
		SystemUtil.dumpSystemInfo();
	}

	@Test
	public void getCurrentPidTest() {
		final long pid = SystemUtil.getCurrentPID();
		Assert.assertTrue(pid > 0);
	}

	@Test
	public void getJavaInfoTest() {
		final JavaInfo javaInfo = SystemUtil.getJavaInfo();
		Assert.assertNotNull(javaInfo);
	}

	@Test
	public void getJavaRuntimeInfoTest() {
		final JavaRuntimeInfo info = SystemUtil.getJavaRuntimeInfo();
		Assert.assertNotNull(info);
	}

	@Test
	public void getOsInfoTest() {
		final OsInfo osInfo = SystemUtil.getOsInfo();
		Assert.assertNotNull(osInfo);

		Console.log(osInfo.getName());
	}

	@Test
	public void getHostInfo() {
		final HostInfo hostInfo = SystemUtil.getHostInfo();
		Assert.assertNotNull(hostInfo);
	}

	@Test
	public void getUserInfoTest(){
		// https://gitee.com/dromara/hutool/issues/I3NM39
		final UserInfo userInfo = SystemUtil.getUserInfo();
		Assert.assertTrue(userInfo.getTempDir().endsWith(File.separator));
	}
}
