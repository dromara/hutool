package org.dromara.hutool.management;

import org.dromara.hutool.lang.Console;
import org.dromara.hutool.management.oshi.CpuInfo;
import org.dromara.hutool.management.oshi.OshiUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import oshi.software.os.OSProcess;

/**
 * 测试参考：<a href="https://github.com/oshi/oshi/blob/master/oshi-core/src/test/java/oshi/SystemInfoTest.java">https://github.com/oshi/oshi/blob/master/oshi-core/src/test/java/oshi/SystemInfoTest.java</a>
 */
public class OshiTest {

	@Test
	public void getMemoryTest() {
		final long total = OshiUtil.getMemory().getTotal();
		Assertions.assertTrue(total > 0);
	}

	@Test
	public void getCupInfo() {
		final CpuInfo cpuInfo = OshiUtil.getCpuInfo();
		Assertions.assertNotNull(cpuInfo);
	}

	@Test
	public void getCurrentProcessTest() {
		final OSProcess currentProcess = OshiUtil.getCurrentProcess();
		Assertions.assertEquals("java", currentProcess.getName());
	}

	@Test
	@Disabled
	public void getUsedTest() {
		int i = 0;
		while (i < 1000) {
			Console.log(OshiUtil.getCpuInfo().getUsed());
			i++;
		}
	}
}
