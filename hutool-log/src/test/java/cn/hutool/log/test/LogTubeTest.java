package cn.hutool.log.test;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.log.dialect.logtube.LogTubeLogFactory;
import org.junit.Test;

public class LogTubeTest {

	@Test
	public void logTest(){
		LogFactory factory = new LogTubeLogFactory();
		LogFactory.setCurrentLogFactory(factory);
		Log log = LogFactory.get();
		log.debug("LogTube debug test.");
	}
}
