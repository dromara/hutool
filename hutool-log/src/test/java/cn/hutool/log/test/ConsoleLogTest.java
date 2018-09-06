package cn.hutool.log.test;

import org.junit.Test;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.log.dialect.console.ConsoleLog;
import cn.hutool.log.dialect.console.ConsoleLogFactory;
import cn.hutool.log.level.Level;

/**
 * 命令行日志单元测试
 * @author Looly
 *
 */
public class ConsoleLogTest {
	
	private static final String LINE = "----------------------------------------------------------------------";

	@Test
	public void customLogTest(){
		Log log;
		
		//手动设定级别
		ConsoleLog.setLevel(Level.INFO);
		//自定义日志实现为Console Logging
		LogFactory.setCurrentLogFactory(new ConsoleLogFactory());
		log = LogFactory.get();
		// 自动选择日志实现
		log.info("This is {} log\n{}", "custom Console", LINE);
	}
}
