package cn.hutool.log.test;

import cn.hutool.log.LogFactory;
import cn.hutool.log.StaticLog;
import cn.hutool.log.dialect.console.ConsoleColorLogFactory;
import cn.hutool.log.dialect.console.ConsoleLogFactory;
import org.junit.Test;

public class StaticLogTest {
	@Test
	public void test() {
		LogFactory.setCurrentLogFactory(ConsoleLogFactory.class);
		StaticLog.debug("This is static {} log", "debug");
		StaticLog.info("This is static {} log", "info");
	}

	@Test
	public void colorTest(){
		LogFactory.setCurrentLogFactory(ConsoleColorLogFactory.class);
		StaticLog.debug("This is static {} log", "debug");
		StaticLog.info("This is static {} log", "info");
		StaticLog.error("This is static {} log", "error");
		StaticLog.warn("This is static {} log", "warn");
		StaticLog.trace("This is static {} log", "trace");
	}
}
