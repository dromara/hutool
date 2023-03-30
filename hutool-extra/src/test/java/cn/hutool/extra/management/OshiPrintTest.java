package cn.hutool.extra.management;

import cn.hutool.core.lang.Console;
import cn.hutool.extra.management.oshi.OshiUtil;
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
