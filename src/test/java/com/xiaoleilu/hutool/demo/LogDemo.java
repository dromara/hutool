package com.xiaoleilu.hutool.demo;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.xiaoleilu.hutool.log.dialect.ApacheCommonsLogFactory;
import com.xiaoleilu.hutool.log.dialect.ConsoleLogFactory;
import com.xiaoleilu.hutool.log.level.Level;

public class LogDemo {
	public static void main(String[] args) {
		// 自动选择日志实现
		Log log = LogFactory.get();
		log.debug("This is {} log", Level.DEBUG);
		log.info("This is {} log", Level.INFO);
		log.warn("This is {} log", Level.WARN);
		log.error("This is {} log", Level.ERROR);

		// 自定义日志实现
		LogFactory.setCurrentLogFactory(new ApacheCommonsLogFactory());
		log = LogFactory.get();
		log.debug("This is {} log", Level.DEBUG);
		log.info("This is {} log", Level.INFO);
		log.warn("This is {} log", Level.WARN);
		log.error("This is {} log", Level.ERROR);

		// 自定义日志实现
		LogFactory.setCurrentLogFactory(new ConsoleLogFactory());
		log = LogFactory.get();
		log.debug("This is {} log", Level.DEBUG);
		log.info("This is {} log", Level.INFO);
		log.warn("This is {} log", Level.WARN);
		log.error("This is {} log", Level.ERROR);
	}
}
