package com.xiaoleilu.hutool.log.dialect.commons;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.Level;

import com.xiaoleilu.hutool.log.AbstractLocationAwareLog;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * Apache Commons Logging for Log4j
 * @author Looly
 *
 */
public class ApacheCommonsLog4JLog extends AbstractLocationAwareLog {
	private static final long serialVersionUID = -6843151523380063975L;
	
	private static final String FQCN = ApacheCommonsLog4JLog.class.getName();
	
	private final transient Log4JLogger logger;
	private final String name;

	// ------------------------------------------------------------------------- Constructor
	public ApacheCommonsLog4JLog(Log logger, String name) {
		this.logger = (Log4JLogger) logger;
		this.name = name;
	}

	public ApacheCommonsLog4JLog(Class<?> clazz) {
		this(LogFactory.getLog(clazz), clazz.getName());
	}

	public ApacheCommonsLog4JLog(String name) {
		this(LogFactory.getLog(name), name);
	}

	@Override
	public String getName() {
		return this.name;
	}

	// ------------------------------------------------------------------------- Trace
	@Override
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	@Override
	public void trace(String format, Object... arguments) {
		trace(null, format, arguments);
	}

	@Override
	public void trace(Throwable t, String format, Object... arguments) {
		logger.getLogger().log(FQCN, Level.TRACE, StrUtil.format(format, arguments), t);
	}

	// ------------------------------------------------------------------------- Debug
	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(String format, Object... arguments) {
		debug(null, format, arguments);
	}

	@Override
	public void debug(Throwable t, String format, Object... arguments) {
		logger.getLogger().log(FQCN, Level.DEBUG, StrUtil.format(format, arguments), t);
	}

	// ------------------------------------------------------------------------- Info
	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public void info(String format, Object... arguments) {
		info(null, format, arguments);
	}

	@Override
	public void info(Throwable t, String format, Object... arguments) {
		logger.getLogger().log(FQCN, Level.INFO, StrUtil.format(format, arguments), t);
	}

	// ------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public void warn(String format, Object... arguments) {
		warn(null, format, arguments);
	}

	@Override
	public void warn(Throwable t, String format, Object... arguments) {
		logger.getLogger().log(FQCN, Level.WARN, StrUtil.format(format, arguments), t);
	}

	// ------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public void error(String format, Object... arguments) {
		error(null, format, arguments);
	}

	@Override
	public void error(Throwable t, String format, Object... arguments) {
		logger.getLogger().log(FQCN, Level.ERROR, StrUtil.format(format, arguments), t);
	}

	// ------------------------------------------------------------------------- Log
	@Override
	public void log(com.xiaoleilu.hutool.log.level.Level level, String format, Object... arguments) {
		this.log(level, null, format, arguments);
	}
	
	@Override
	public void log(com.xiaoleilu.hutool.log.level.Level level, Throwable t, String format, Object... arguments) {
		this.log(FQCN, level, t, format, arguments);
	}
	
	@Override
	public void log(String fqcn, com.xiaoleilu.hutool.log.level.Level level, Throwable t, String format, Object... arguments) {
		Level log4jLevel;
		switch (level) {
			case TRACE:
				log4jLevel = Level.TRACE;
				break;
			case DEBUG:
				log4jLevel = Level.DEBUG;
				break;
			case INFO:
				log4jLevel = Level.INFO;
				break;
			case WARN:
				log4jLevel = Level.WARN;
				break;
			case ERROR:
				log4jLevel = Level.ERROR;
				break;
			default:
				throw new Error(StrUtil.format("Can not identify level: {}", level));
		}
		logger.getLogger().log(FQCN, log4jLevel, StrUtil.format(format, arguments), t);
	}

}
