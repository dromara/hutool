package org.dromara.hutool.log;

import org.dromara.hutool.log.engine.LogEngineFactory;
import org.dromara.hutool.log.engine.console.ConsoleColorLogEngine;
import org.dromara.hutool.log.engine.console.ConsoleLogEngine;
import org.dromara.hutool.log.engine.log4j2.Log4j2LogEngine;
import org.junit.jupiter.api.Test;

public class StaticLogTest {

	@Test
	public void staticLog4j2Test() {
		LogEngineFactory.setDefaultEngine(Log4j2LogEngine.class);
		StaticLog.debug("This is static {} log", "debug");
		StaticLog.info("This is static {} log", "info");
	}

	@Test
	public void test() {
		LogEngineFactory.setDefaultEngine(ConsoleLogEngine.class);
		StaticLog.debug("This is static {} log", "debug");
		StaticLog.info("This is static {} log", "info");
	}

	@Test
	public void colorTest(){
		LogEngineFactory.setDefaultEngine(ConsoleColorLogEngine.class);
		StaticLog.debug("This is static {} log", "debug");
		StaticLog.info("This is static {} log", "info");
		StaticLog.error("This is static {} log", "error");
		StaticLog.warn("This is static {} log", "warn");
		StaticLog.trace("This is static {} log", "trace");
	}
}
