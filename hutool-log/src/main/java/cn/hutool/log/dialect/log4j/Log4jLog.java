package cn.hutool.log.dialect.log4j;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.AbstractLocationAwareLog;

/**
 * <a href="http://logging.apache.org/log4j/1.2/index.html">Apache Log4J</a> log.<br>
 * 
 * @author Looly
 *
 */
public class Log4jLog extends AbstractLocationAwareLog {
	private static final long serialVersionUID = -6843151523380063975L;
	private static final String FQCN = Log4jLog.class.getName();

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
	public void trace(String format, Object... arguments) {
		trace(null, format, arguments);
	}

	@Override
	public void trace(Throwable t, String format, Object... arguments) {
		logIfEnabled(FQCN, cn.hutool.log.level.Level.TRACE, t, format, arguments);
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
		logIfEnabled(FQCN, cn.hutool.log.level.Level.DEBUG, t, format, arguments);
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
		logIfEnabled(FQCN, cn.hutool.log.level.Level.INFO, t, format, arguments);
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
		logIfEnabled(FQCN, cn.hutool.log.level.Level.WARN, t, format, arguments);
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
		logIfEnabled(FQCN, cn.hutool.log.level.Level.ERROR, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Log
	@Override
	public void log(cn.hutool.log.level.Level level, String format, Object... arguments) {
		log(level, null, format, arguments);
	}
	
	@Override
	public void log(cn.hutool.log.level.Level level, Throwable t, String format, Object... arguments) {
		this.log(FQCN, level, t, format, arguments);
	}
	
	@Override
	public void log(String fqcn, cn.hutool.log.level.Level level, Throwable t, String format, Object... arguments) {
		logIfEnabled(fqcn, level, t, format, arguments);
	}
	
	/**
	 * 打印对应等级的日志
	 * @param fqcn FQCN
	 * @param level 等级
	 * @param t 异常
	 * @param format 消息模板
	 * @param arguments 参数
	 * @since 4.5.13
	 */
	private void logIfEnabled(String fqcn, cn.hutool.log.level.Level level, Throwable t, String format, Object... arguments) {
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
	
	// ------------------------------------------------------------------------- Private method
}
