package com.xiaoleilu.hutool.demo;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.xiaoleilu.hutool.log.LogLevel;
import com.xiaoleilu.hutool.log.dialect.ApacheCommonsLogFactory;

public class LogDemo {
	public static void main(String[] args) {
		//自动选择日志实现
		Log log = LogFactory.get();
		log.debug("This is {} log", LogLevel.DEBUG);
		log.info("This is {} log", LogLevel.INFO);
		log.warn("This is {} log", LogLevel.WARN);
		log.error("This is {} log", LogLevel.ERROR);
		
		//自定义日志实现
		LogFactory.setCurrentLogFactory(new ApacheCommonsLogFactory());
		log = LogFactory.get();
		log.debug("This is {} log", LogLevel.DEBUG);
		log.info("This is {} log", LogLevel.INFO);
		log.warn("This is {} log", LogLevel.WARN);
		log.error("This is {} log", LogLevel.ERROR);
	}
}
