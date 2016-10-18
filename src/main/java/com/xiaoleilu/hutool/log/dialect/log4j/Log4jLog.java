package com.xiaoleilu.hutool.log.dialect.log4j;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.xiaoleilu.hutool.log.AbstractLog;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * <a href="http://logging.apache.org/log4j/1.2/index.html">Apache Log4J</a> log.<br>
 * 
 * @author Looly
 *
 */
public class Log4jLog extends AbstractLog {
	private static final long serialVersionUID = -6843151523380063975L;
	private static final String FQCN = Log4jLog.class.getName();

	private final transient Logger logger;

	// ------------------------------------------------------------------------- Constructor
	public Log4jLog(Logger logger) {
		this.logger = logger;
	}

	public Log4jLog(Class<?> clazz) {
		this(Logger.getLogger(clazz));
	}

	public Log4jLog(String name) {
		this(Logger.getLogger(name));
	}

	@Override
	public String getName() {
		return logger.getName();
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
		logger.log(FQCN, Level.TRACE, StrUtil.format(format, arguments), t);
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
		logger.log(FQCN, Level.DEBUG, StrUtil.format(format, arguments), t);
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
		logger.log(FQCN, Level.INFO, StrUtil.format(format, arguments), t);
	}

	// ------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return logger.isEnabledFor(Level.WARN);
	}

	@Override
	public void warn(String format, Object... arguments) {
		warn(null, format, arguments);
	}

	@Override
	public void warn(Throwable t, String format, Object... arguments) {
		logger.log(FQCN, Level.WARN, StrUtil.format(format, arguments), t);
	}

	// ------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return logger.isEnabledFor(Level.ERROR);
	}

	@Override
	public void error(String format, Object... arguments) {
		error(null, format, arguments);
	}

	@Override
	public void error(Throwable t, String format, Object... arguments) {
		logger.log(FQCN, Level.ERROR, StrUtil.format(format, arguments), t);
	}
	
	// ------------------------------------------------------------------------- Private method
}
