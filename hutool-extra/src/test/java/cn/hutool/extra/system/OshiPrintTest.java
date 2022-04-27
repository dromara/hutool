package cn.hutool.extra.system;

import cn.hutool.core.lang.Console;
import cn.hutool.extra.system.oshi.OshiUtil;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class OshiPrintTest {

	@Test
	@Ignore
	public void printCpuInfo(){
		Console.log(OshiUtil.getCpuInfo());
	}
}
