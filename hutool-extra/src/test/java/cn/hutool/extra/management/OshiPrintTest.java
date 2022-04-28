package cn.hutool.extra.management;

import cn.hutool.core.lang.Console;
import cn.hutool.extra.management.oshi.OshiUtil;
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
