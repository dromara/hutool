package cn.hutool.log.dialect.log4j;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.AbstractLog;

/**
 * <a href="http://logging.apache.org/log4j/1.2/index.html">Apache Log4J</a> log.<br>
 * 
 * @author Looly
 *
 */
public class Log4jLog extends AbstractLog {
	private static final long serialVersionUID = -6843151523380063975L;

	private final Logger logger;

	// ------------------------------------------------------------------------- Constructor
	public Log4jLog(Logger logger) {
		this.logger = logger;
	}

	public Log4jLog(Class<?> clazz) {
		this((null == clazz) ? StrUtil.NULL : clazz.getName());
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
	public void trace(String fqcn, Throwable t, String format, Object... arguments) {
		log(fqcn, cn.hutool.log.level.Level.TRACE, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Debug
	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(String fqcn, Throwable t, String format, Object... arguments) {
		log(fqcn, cn.hutool.log.level.Level.DEBUG, t, format, arguments);
	}
	// ------------------------------------------------------------------------- Info
	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public void info(String fqcn, Throwable t, String format, Object... arguments) {
		log(fqcn, cn.hutool.log.level.Level.INFO, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return logger.isEnabledFor(Level.WARN);
	}

	@Override
	public void warn(String fqcn, Throwable t, String format, Object... arguments) {
		log(fqcn, cn.hutool.log.level.Level.WARN, t, format, arguments);
	}
	
	// ------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return logger.isEnabledFor(Level.ERROR);
	}

	@Override
	public void error(String fqcn, Throwable t, String format, Object... arguments) {
		log(fqcn, cn.hutool.log.level.Level.ERROR, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Log
	@Override
	public void log(String fqcn, cn.hutool.log.level.Level level, Throwable t, String format, Object... arguments) {
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
		
		if(logger.isEnabledFor(log4jLevel)) {
			logger.log(fqcn, log4jLevel, StrUtil.format(format, arguments), t);
		}
	}
}
