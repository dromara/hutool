package com.xiaoleilu.hutool.demo;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.xiaoleilu.hutool.log.dialect.ApacheCommonsLogFactory;
import com.xiaoleilu.hutool.log.dialect.ConsoleLogFactory;
import com.xiaoleilu.hutool.log.dialect.JdkLogFactory;
import com.xiaoleilu.hutool.log.level.Level;

public class LogDemo {
	public static void main(String[] args) {
		Log log = LogFactory.get();
		
		System.out.println("----------------------------自动选择日志------------------------------");
		// 自动选择日志实现
		log.debug("This is {} log", Level.DEBUG);
		log.info("This is {} log", Level.INFO);
		log.warn("This is {} log", Level.WARN);
		log.error("This is {} log", Level.ERROR);

		System.out.println("----------------------------自定义为Common Log日志------------------------------");
		// 自定义日志实现
		LogFactory.setCurrentLogFactory(new ApacheCommonsLogFactory());
		log = LogFactory.get();
		log.debug("This is {} log", Level.DEBUG);
		log.info("This is {} log", Level.INFO);
		log.warn("This is {} log", Level.WARN);
		log.error("This is {} log", Level.ERROR);
		
		System.out.println("----------------------------自定义为JDK Log日志------------------------------");
		// 自定义日志实现
		LogFactory.setCurrentLogFactory(new JdkLogFactory());
		log = LogFactory.get();
		log.debug("This is {} log", Level.DEBUG);
		log.info("This is {} log", Level.INFO);
		log.warn("This is {} log", Level.WARN);
		log.error("This is {} log", Level.ERROR);

		System.out.println("----------------------------自定义为Console Log日志------------------------------");
		// 自定义日志实现
		LogFactory.setCurrentLogFactory(new ConsoleLogFactory());
		log = LogFactory.get();
		log.debug("This is {} log", Level.DEBUG);
		log.info("This is {} log", Level.INFO);
		log.warn("This is {} log", Level.WARN);
		log.error("This is {} log", Level.ERROR);
	}
}
