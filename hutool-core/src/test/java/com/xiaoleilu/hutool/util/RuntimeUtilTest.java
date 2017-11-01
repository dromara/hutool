package com.xiaoleilu.hutool.util;

import org.junit.Ignore;
import org.junit.Test;

import com.xiaoleilu.hutool.lang.Console;

public class RuntimeUtilTest {

	@Test
	@Ignore
	public void execTest() {
		String str = RuntimeUtil.execForStr("ipconfig");
		Console.log(str);
	}
}
