package com.xiaoleilu.hutool.log.dialect;

import com.xiaoleilu.hutool.StrUtil;
import com.xiaoleilu.hutool.log.AbstractLog;

/**
 * <a href="http://www.slf4j.org/">SLF4J</a> log.<br>
 * 同样无缝支持 <a href="http://logback.qos.ch/">LogBack</a>
 * @author Looly
 *
 */
public class Slf4jLog extends AbstractLog {
	private static final long serialVersionUID = -6843151523380063975L;

	private final transient org.slf4j.Logger logger;

	//------------------------------------------------------------------------- Constructor
	public Slf4jLog(org.slf4j.Logger logger) {
		this.logger = logger;
	}
	
	public Slf4jLog(Class<?> clazz) {
		this.logger = org.slf4j.LoggerFactory.getLogger(clazz);
	}
	
	public Slf4jLog(String name) {
		this.logger = org.slf4j.LoggerFactory.getLogger(name);
	}
	
	@Override
	public String getName() {
		return logger.getName();
	}

	//------------------------------------------------------------------------- Trace
	@Override
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	@Override
	public void trace(String format, Object... arguments) {
		logger.trace(format, arguments);
	}

	@Override
	public void trace(Throwable t, String format, Object... arguments) {
		logger.trace(StrUtil.format(format, arguments), t);
	}

	//------------------------------------------------------------------------- Debug
	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(String format, Object... arguments) {
		logger.debug(format, arguments);
	}

	@Override
	public void debug(Throwable t, String format, Object... arguments) {
		logger.debug(StrUtil.format(format, arguments), t);
	}

	//------------------------------------------------------------------------- Info
	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public void info(String format, Object... arguments) {
		logger.info(format, arguments);
	}

	@Override
	public void info(Throwable t, String format, Object... arguments) {
		logger.info(StrUtil.format(format, arguments), t);
	}

	//------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public void warn(String format, Object... arguments) {
		logger.warn(format, arguments);
	}

	@Override
	public void warn(Throwable t, String format, Object... arguments) {
		logger.warn(StrUtil.format(format, arguments), t);
	}

	//------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public void error(String format, Object... arguments) {
		logger.error(format, arguments);
	}

	@Override
	public void error(Throwable t, String format, Object... arguments) {
		logger.error(StrUtil.format(format, arguments), t);
	}

}
