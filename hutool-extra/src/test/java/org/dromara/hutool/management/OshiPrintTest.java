package org.dromara.hutool.management;

import org.dromara.hutool.lang.Console;
import org.dromara.hutool.management.oshi.OshiUtil;
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
