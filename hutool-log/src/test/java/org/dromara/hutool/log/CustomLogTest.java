package org.dromara.hutool.log;

import org.dromara.hutool.log.engine.LogEngine;
import org.dromara.hutool.log.engine.LogEngineFactory;
import org.dromara.hutool.log.engine.commons.ApacheCommonsLogEngine;
import org.dromara.hutool.log.engine.console.ConsoleLogEngine;
import org.dromara.hutool.log.engine.jboss.JbossLogEngine;
import org.dromara.hutool.log.engine.jdk.JdkLogEngine;
import org.dromara.hutool.log.engine.log4j.Log4jLogEngine;
import org.dromara.hutool.log.engine.log4j2.Log4j2LogEngine;
import org.dromara.hutool.log.engine.slf4j.Slf4jLogEngine;
import org.dromara.hutool.log.engine.tinylog.TinyLog2Engine;
import org.dromara.hutool.log.engine.tinylog.TinyLogEngine;
import org.junit.jupiter.api.Test;

/**
 * 日志门面单元测试
 * @author Looly
 *
 */
public class CustomLogTest {

	private static final String LINE = "----------------------------------------------------------------------";

	@Test
	public void consoleLogTest(){
		final LogEngine engine = new ConsoleLogEngine();
		LogEngineFactory.setDefaultEngine(engine);
		final Log log = Log.get();

		log.info("This is custom '{}' log\n{}", engine.getName(), LINE);
	}

	@Test
	public void consoleLogNullTest(){
		final LogEngine engine = new ConsoleLogEngine();
		LogEngineFactory.setDefaultEngine(engine);
		final Log log = Log.get();

		log.info(null);
		log.info((String)null);
	}

	@Test
	public void commonsLogTest(){
		final LogEngine engine = new ApacheCommonsLogEngine();
		LogEngineFactory.setDefaultEngine(engine);
		final Log log = Log.get();

		log.info(null);
		log.info((String)null);
		log.info("This is custom '{}' log\n{}", engine.getName(), LINE);
	}

	@Test
	public void tinyLogTest(){
		final LogEngine engine = new TinyLogEngine();
		LogEngineFactory.setDefaultEngine(engine);
		final Log log = Log.get();

		log.info(null);
		log.info((String)null);
		log.info("This is custom '{}' log\n{}", engine.getName(), LINE);
	}

	@Test
	public void tinyLog2Test(){
		final LogEngine engine = new TinyLog2Engine();
		LogEngineFactory.setDefaultEngine(engine);
		final Log log = Log.get();

		log.info(null);
		log.info((String)null);
		log.info("This is custom '{}' log\n{}", engine.getName(), LINE);
	}

	@Test
	public void log4j2LogTest(){
		final LogEngine engine = new Log4j2LogEngine();
		LogEngineFactory.setDefaultEngine(engine);
		final Log log = Log.get();

		log.debug(null);
		log.debug("This is custom '{}' log\n{}", engine.getName(), LINE);
		log.info(null);
		log.info((String)null);
		log.info("This is custom '{}' log\n{}", engine.getName(), LINE);
	}

	@Test
	public void log4jLogTest(){
		final LogEngine engine = new Log4jLogEngine();
		LogEngineFactory.setDefaultEngine(engine);
		final Log log = Log.get();

		log.info(null);
		log.info((String)null);
		log.info("This is custom '{}' log\n{}", engine.getName(), LINE);

	}

	@Test
	public void jbossLogTest(){
		final LogEngine engine = new JbossLogEngine();
		LogEngineFactory.setDefaultEngine(engine);
		final Log log = Log.get();

		log.info(null);
		log.info((String)null);
		log.info("This is custom '{}' log\n{}", engine.getName(), LINE);
	}

	@Test
	public void jdkLogTest(){
		final LogEngine engine = new JdkLogEngine();
		LogEngineFactory.setDefaultEngine(engine);
		final Log log = Log.get();

		log.info(null);
		log.info((String)null);
		log.info("This is custom '{}' log\n{}", engine.getName(), LINE);
	}

	@Test
	public void slf4jTest(){
		final LogEngine engine = new Slf4jLogEngine(false);
		LogEngineFactory.setDefaultEngine(engine);
		final Log log = Log.get();

		log.info(null);
		log.info((String)null);
		log.info("This is custom '{}' log\n{}", engine.getName(), LINE);
	}
}
