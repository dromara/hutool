package org.dromara.hutool.core.util;

import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * 命令行单元测试
 * @author looly
 *
 */
public class RuntimeUtilTest {

	@Test
	@Disabled
	public void execTest() {
		final String str = RuntimeUtil.execForStr("ipconfig");
		Console.log(str);
	}

	@Test
	@Disabled
	public void execCmdTest() {
		final String str = RuntimeUtil.execForStr("cmd /c dir");
		Console.log(str);
	}

	@Test
	@Disabled
	public void execCmdTest2() {
		final String str = RuntimeUtil.execForStr("cmd /c", "cd \"C:\\Program Files (x86)\"", "chdir");
		Console.log(str);
	}

	@Test
	public void getUsableMemoryTest(){
		Assertions.assertTrue(RuntimeUtil.getUsableMemory() > 0);
	}

	@Test
	public void getPidTest(){
		final int pid = RuntimeUtil.getPid();
		Assertions.assertTrue(pid > 0);
	}

	@Test
	public void getProcessorCountTest(){
		final int cpu = RuntimeUtil.getProcessorCount();
		Assertions.assertTrue(cpu > 0);
	}

	@Test
	@Disabled
	void pingTest() {
		final String s = RuntimeUtil.execForStr("cmd /c ping " + "hutool.cn");
		Console.log(s);
	}
}
