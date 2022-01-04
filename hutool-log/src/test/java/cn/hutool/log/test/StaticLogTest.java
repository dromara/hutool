package cn.hutool.log.test;

import org.junit.jupiter.api.Test;

import cn.hutool.log.StaticLog;

public class StaticLogTest {
	@Test
	public void test() {
		StaticLog.debug("This is static {} log", "debug");
		StaticLog.info("This is static {} log", "info");
	}
}
