package cn.hutool.system;

import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import org.junit.Assert;
import org.junit.Test;
import oshi.software.os.OSProcess;

/**
 * 测试参考：https://github.com/oshi/oshi/blob/master/oshi-core/src/test/java/oshi/SystemInfoTest.java
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
}
