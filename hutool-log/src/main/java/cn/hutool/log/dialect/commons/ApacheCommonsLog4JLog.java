package cn.hutool.log.dialect.commons;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.Log4JLogger;

import cn.hutool.log.dialect.log4j.Log4jLog;

/**
 * Apache Commons Logging for Log4j
 * @author Looly
 *
 */
public class ApacheCommonsLog4JLog extends Log4jLog {
	private static final long serialVersionUID = -6843151523380063975L;
	
	// ------------------------------------------------------------------------- Constructor
	public ApacheCommonsLog4JLog(Log logger) {
		super(((Log4JLogger) logger).getLogger());
	}

	public ApacheCommonsLog4JLog(Class<?> clazz) {
		super(clazz);
	}

	public ApacheCommonsLog4JLog(String name) {
		super(name);
	}
}
