package cn.hutool.system;

import cn.hutool.core.lang.Console;
import cn.hutool.system.oshi.OshiUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class OshiPrintTest {

	@Test
	@Disabled
	public void printCpuInfo(){
		Console.log(OshiUtil.getCpuInfo());
	}
}
