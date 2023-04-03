package org.dromara.hutool.log;

import org.dromara.hutool.log.dialect.logtube.LogTubeLogFactory;
import org.junit.jupiter.api.Test;

public class LogTubeTest {

	@Test
	public void logTest(){
		final LogFactory factory = new LogTubeLogFactory();
		LogFactory.setCurrentLogFactory(factory);
		final Log log = LogFactory.get();
		log.debug("LogTube debug test.");
	}
}
