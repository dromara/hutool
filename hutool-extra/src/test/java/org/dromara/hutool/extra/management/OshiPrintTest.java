package org.dromara.hutool.extra.management;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.extra.management.oshi.OshiUtil;
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
