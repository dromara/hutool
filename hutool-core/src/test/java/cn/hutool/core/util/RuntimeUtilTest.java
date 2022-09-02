package cn.hutool.core.util;

import cn.hutool.core.lang.Console;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 命令行单元测试
 * @author looly
 *
 */
public class RuntimeUtilTest {

	@Test
	@Ignore
	public void execTest() {
		final String str = RuntimeUtil.execForStr("ipconfig");
		Console.log(str);
	}

	@Test
	@Ignore
	public void execCmdTest() {
		final String str = RuntimeUtil.execForStr("cmd /c dir");
		Console.log(str);
	}

	@Test
	@Ignore
	public void execCmdTest2() {
		final String str = RuntimeUtil.execForStr("cmd /c", "cd \"C:\\Program Files (x86)\"", "chdir");
		Console.log(str);
	}

	@Test
	public void getUsableMemoryTest(){
		Assert.assertTrue(RuntimeUtil.getUsableMemory() > 0);
	}

	@Test
	public void getPidTest(){
		final int pid = RuntimeUtil.getPid();
		Assert.assertTrue(pid > 0);
	}

	@Test
	public void getProcessorCountTest(){
		int cpu = RuntimeUtil.getProcessorCount();
		Console.log("cpu个数：{}", cpu);
		Assert.assertTrue(cpu > 0);
	}
}
