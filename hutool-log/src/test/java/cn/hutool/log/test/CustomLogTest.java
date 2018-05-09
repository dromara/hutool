package cn.hutool.log.test;

import org.junit.Test;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.log.dialect.commons.ApacheCommonsLogFactory;
import cn.hutool.log.dialect.console.ConsoleLogFactory;
import cn.hutool.log.dialect.jdk.JdkLogFactory;
import cn.hutool.log.dialect.log4j.Log4jLogFactory;
import cn.hutool.log.dialect.log4j2.Log4j2LogFactory;
import cn.hutool.log.dialect.tinylog.TinyLogFactory;

/**
 * 日志门面单元测试
 * @author Looly
 *
 */
public class CustomLogTest {
	
	private static final String LINE = "----------------------------------------------------------------------";

	@Test
	public void customLogTest(){
		Log log;
		
		//自定义日志实现为Apache Commons Logging
		LogFactory.setCurrentLogFactory(new ApacheCommonsLogFactory());
		log = LogFactory.get();
		// 自动选择日志实现
		log.debug("This is {} log\n{}", "custom apache commons logging", LINE);
		
		//自定义日志实现为TinyLog
		LogFactory.setCurrentLogFactory(new TinyLogFactory());
		log = LogFactory.get();
		// 自动选择日志实现
		log.debug("This is {} log\n{}", "custom tinylog", LINE);
		
		//自定义日志实现为Log4j2
		LogFactory.setCurrentLogFactory(new Log4j2LogFactory());
		log = LogFactory.get();
		// 自动选择日志实现
		log.debug("This is {} log\n{}", "custom log4j2", LINE);
		
		//自定义日志实现为Log4j
		LogFactory.setCurrentLogFactory(new Log4jLogFactory());
		log = LogFactory.get();
		// 自动选择日志实现
		log.debug("This is {} log\n{}", "custom log4j", LINE);
		
		//自定义日志实现为JDK Logging
		LogFactory.setCurrentLogFactory(new JdkLogFactory());
		log = LogFactory.get();
		// 自动选择日志实现
		log.info("This is {} log\n{}", "custom jdk logging", LINE);
		
		//自定义日志实现为Console Logging
		LogFactory.setCurrentLogFactory(new ConsoleLogFactory());
		log = LogFactory.get();
		// 自动选择日志实现
		log.info("This is {} log\n{}", "custom Console", LINE);
	}
	
	@Test
	public void customNullLogTest(){
		Log log;
		
		//自定义日志实现为Apache Commons Logging
		LogFactory.setCurrentLogFactory(new ApacheCommonsLogFactory());
		log = LogFactory.get();
		log.info(null);
		
		//自定义日志实现为TinyLog
		LogFactory.setCurrentLogFactory(new TinyLogFactory());
		log = LogFactory.get();
		log.info(null);
		
		//自定义日志实现为Log4j2
		LogFactory.setCurrentLogFactory(new Log4j2LogFactory());
		log = LogFactory.get();
		log.info(null);
		
		//自定义日志实现为Log4j
		LogFactory.setCurrentLogFactory(new Log4jLogFactory());
		log = LogFactory.get();
		log.info(null);
		
		//自定义日志实现为JDK Logging
		LogFactory.setCurrentLogFactory(new JdkLogFactory());
		log = LogFactory.get();
		log.info(null);
		
		//自定义日志实现为Console Logging
		LogFactory.setCurrentLogFactory(new ConsoleLogFactory());
		log = LogFactory.get();
		log.info(null);
	}
}
