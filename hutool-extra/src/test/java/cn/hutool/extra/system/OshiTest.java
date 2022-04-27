package cn.hutool.extra.system;

import cn.hutool.core.lang.Console;
import cn.hutool.extra.system.oshi.CpuInfo;
import cn.hutool.extra.system.oshi.OshiUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import oshi.software.os.OSProcess;

/**
 * 测试参考：<a href="https://github.com/oshi/oshi/blob/master/oshi-core/src/test/java/oshi/SystemInfoTest.java">https://github.com/oshi/oshi/blob/master/oshi-core/src/test/java/oshi/SystemInfoTest.java</a>
 */
public class OshiTest {

	@Test
	public void getMemoryTest() {
		long total = OshiUtil.getMemory().getTotal();
		Assert.assertTrue(total > 0);
	}

	@Test
	public void getCupInfo() {
		CpuInfo cpuInfo = OshiUtil.getCpuInfo();
		Assert.assertNotNull(cpuInfo);
	}

	@Test
	public void getCurrentProcessTest() {
		final OSProcess currentProcess = OshiUtil.getCurrentProcess();
		Assert.assertEquals("java", currentProcess.getName());
	}

	@Test
	@Ignore
	public void getUsedTest() {
		while (true) {
			Console.log(OshiUtil.getCpuInfo().getUsed());
		}
	}
}
