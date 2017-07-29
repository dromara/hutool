package com.xiaoleilu.hutool.log;

import org.junit.Test;

public class StaticLogTest {
	@Test
	public void test() {
		StaticLog.debug("This is static {} log", "debug");
		StaticLog.info("This is static {} log", "info");
	}
}
