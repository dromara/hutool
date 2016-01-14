package com.xiaoleilu.hutool.log.dialect;

import com.xiaoleilu.hutool.log.AbstractLog;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * <a href="http://logging.apache.org/log4j/1.2/index.html">Apache Log4J</a> log.<br>
 * 
 * @author Looly
 *
 */
public class ApacheCommonsLog extends AbstractLog {
	private static final long serialVersionUID = -6843151523380063975L;

	private final transient org.apache.commons.logging.Log logger;
	private final String name;

	// ------------------------------------------------------------------------- Constructor
	public ApacheCommonsLog(org.apache.commons.logging.Log logger, String name) {
		this.logger = logger;
		this.name = name;
	}

	public ApacheCommonsLog(Class<?> clazz) {
		this.logger = org.apache.commons.logging.LogFactory.getLog(clazz);
		this.name = clazz.getName();
	}

	public ApacheCommonsLog(String name) {
		this.logger = org.apache.commons.logging.LogFactory.getLog(name);
		this.name = name;
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
		if (isTraceEnabled()) {
			logger.trace(StrUtil.format(format, arguments));
		}
	}

	@Override
	public void trace(Throwable t, String format, Object... arguments) {
		if(isTraceEnabled()){
			logger.trace(StrUtil.format(format, arguments), t);
		}
	}

	// ------------------------------------------------------------------------- Debug
	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(String format, Object... arguments) {
		if(isDebugEnabled()){
			logger.debug(StrUtil.format(format, arguments));
		}
	}

	@Override
	public void debug(Throwable t, String format, Object... arguments) {
		if(isDebugEnabled()){
			logger.debug(StrUtil.format(format, arguments), t);
		}
	}

	// ------------------------------------------------------------------------- Info
	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public void info(String format, Object... arguments) {
		if(isInfoEnabled()){
			logger.info(StrUtil.format(format, arguments));
		}
	}

	@Override
	public void info(Throwable t, String format, Object... arguments) {
		if(isInfoEnabled()){
			logger.info(StrUtil.format(format, arguments), t);
		}
	}

	// ------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public void warn(String format, Object... arguments) {
		if(isWarnEnabled()){
			logger.warn(StrUtil.format(format, arguments));
		}
	}

	@Override
	public void warn(Throwable t, String format, Object... arguments) {
		if(isWarnEnabled()){
			logger.warn(StrUtil.format(format, arguments), t);
		}
	}

	// ------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public void error(String format, Object... arguments) {
		if(isErrorEnabled()){
			logger.error(StrUtil.format(format, arguments));
		}
	}

	@Override
	public void error(Throwable t, String format, Object... arguments) {
		if(isErrorEnabled()){
			logger.warn(StrUtil.format(format, arguments), t);
		}
	}

	// ------------------------------------------------------------------------- Private method

}
