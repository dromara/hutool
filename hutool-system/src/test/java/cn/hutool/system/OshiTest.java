package cn.hutool.system;

import cn.hutool.core.lang.Console;
import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import oshi.software.os.OSProcess;

/**
 * 测试参考：https://github.com/oshi/oshi/blob/master/oshi-core/src/test/java/oshi/SystemInfoTest.java
 */
public class OshiTest {

	@Test
	public void getMemoryTest() {
		long total = OshiUtil.getMemory().getTotal();
		assertTrue(total > 0);
	}

	@Test
	public void getCupInfo() {
		CpuInfo cpuInfo = OshiUtil.getCpuInfo();
		assertNotNull(cpuInfo);
	}

	@Test
	public void getCurrentProcessTest() {
		final OSProcess currentProcess = OshiUtil.getCurrentProcess();
		assertEquals("java", currentProcess.getName());
	}

	@Test
	@Disabled
	public void getUsedTest(){
		while (true){
			Console.log(OshiUtil.getCpuInfo().getUsed());
		}
	}
}
