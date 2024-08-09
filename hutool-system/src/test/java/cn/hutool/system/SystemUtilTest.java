package cn.hutool.system;

import cn.hutool.core.lang.Console;
import static org.junit.jupiter.api.Assertions.*;
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
		final long pid = SystemUtil.getCurrentPID();
		assertTrue(pid > 0);
	}

	@Test
	public void getJavaInfoTest() {
		final JavaInfo javaInfo = SystemUtil.getJavaInfo();
		assertNotNull(javaInfo);
	}

	@Test
	public void getJavaRuntimeInfoTest() {
		final JavaRuntimeInfo info = SystemUtil.getJavaRuntimeInfo();
		assertNotNull(info);
	}

	@Test
	public void getOsInfoTest() {
		final OsInfo osInfo = SystemUtil.getOsInfo();
		assertNotNull(osInfo);

		Console.log(osInfo.getName());
	}

	@Test
	public void getHostInfo() {
		final HostInfo hostInfo = SystemUtil.getHostInfo();
		assertNotNull(hostInfo);
	}

	@Test
	public void getUserInfoTest(){
		// https://gitee.com/dromara/hutool/issues/I3NM39
		final UserInfo userInfo = SystemUtil.getUserInfo();
		assertTrue(userInfo.getTempDir().endsWith(File.separator));
	}
}
