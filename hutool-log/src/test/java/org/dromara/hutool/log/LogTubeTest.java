package org.dromara.hutool.log;

import org.dromara.hutool.log.engine.LogEngine;
import org.dromara.hutool.log.engine.LogEngineFactory;
import org.dromara.hutool.log.engine.logtube.LogTubeLogEngine;
import org.junit.jupiter.api.Test;

public class LogTubeTest {

	@Test
	public void logTest(){
		final LogEngine engine = new LogTubeLogEngine();
		LogEngineFactory.setDefaultEngine(engine);
		final Log log = Log.get();
		log.debug("LogTube debug test.");
	}
}
