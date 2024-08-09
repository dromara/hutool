package cn.hutool.core.util;

import cn.hutool.core.lang.Console;
import static org.junit.jupiter.api.Assertions.*;
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
		String str = RuntimeUtil.execForStr("ipconfig");
		Console.log(str);
	}

	@Test
	@Disabled
	public void execCmdTest() {
		String str = RuntimeUtil.execForStr("cmd /c dir");
		Console.log(str);
	}

	@Test
	@Disabled
	public void execCmdTest2() {
		String str = RuntimeUtil.execForStr("cmd /c", "cd \"C:\\Program Files (x86)\"", "chdir");
		Console.log(str);
	}

	@Test
	public void getUsableMemoryTest(){
		assertTrue(RuntimeUtil.getUsableMemory() > 0);
	}

	@Test
	public void getPidTest(){
		int pid = RuntimeUtil.getPid();
		assertTrue(pid > 0);
	}

	@Test
	public void getProcessorCountTest(){
		int cpu = RuntimeUtil.getProcessorCount();
		Console.log("cpu个数：{}", cpu);
		assertTrue(cpu > 0);
	}

	@Test
	@Disabled
	public void issueIAB5LWTest() {
		final String s = RuntimeUtil.execForStr("cmd /c netstat -aon | findstr 8080");
		Console.log(s);
	}
}
