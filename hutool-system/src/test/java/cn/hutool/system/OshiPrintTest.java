package cn.hutool.system;

import cn.hutool.core.lang.Console;
import cn.hutool.system.oshi.OshiUtil;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class OshiPrintTest {

	@Test
	public void printCpuInfo(){
		Console.log(OshiUtil.getCpuInfo());
	}
}
