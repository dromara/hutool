package cn.hutool.log.dialect.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.AbstractLog;
import cn.hutool.log.level.Level;

/**
 * <a href="http://www.slf4j.org/">SLF4J</a> log.<br>
 * 同样无缝支持 <a href="http://logback.qos.ch/">LogBack</a>
 * 
 * @author Looly
 *
 */
public class Slf4jLog extends AbstractLog {
	private static final long serialVersionUID = -6843151523380063975L;

	private final transient Logger logger;

	// ------------------------------------------------------------------------- Constructor
	public Slf4jLog(Logger logger) {
		this.logger = logger;
	}

	public Slf4jLog(Class<?> clazz) {
		this(getSlf4jLogger(clazz));
	}

	public Slf4jLog(String name) {
		this(LoggerFactory.getLogger(name));
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
		if (isTraceEnabled()) {
			if (false == locationAwareLog(fqcn, LocationAwareLogger.TRACE_INT, t, format, arguments)) {
				logger.trace(StrUtil.format(format, arguments), t);
			}
		}
	}

	// ------------------------------------------------------------------------- Debug
	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(String fqcn, Throwable t, String format, Object... arguments) {
		if (isDebugEnabled()) {
			if (false == locationAwareLog(fqcn, LocationAwareLogger.DEBUG_INT, t, format, arguments)) {
				logger.debug(StrUtil.format(format, arguments), t);
			}
		}
	}

	// ------------------------------------------------------------------------- Info
	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public void info(String fqcn, Throwable t, String format, Object... arguments) {
		if (isInfoEnabled()) {
			if (false == locationAwareLog(fqcn, LocationAwareLogger.INFO_INT, t, format, arguments)) {
				logger.info(StrUtil.format(format, arguments), t);
			}
		}
	}

	// ------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public void warn(String fqcn, Throwable t, String format, Object... arguments) {
		if (isWarnEnabled()) {
			if (false == locationAwareLog(fqcn, LocationAwareLogger.WARN_INT, t, format, arguments)) {
				logger.warn(StrUtil.format(format, arguments), t);
			}
		}
	}

	// ------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public void error(String fqcn, Throwable t, String format, Object... arguments) {
		if (isErrorEnabled()) {
			if (false == locationAwareLog(fqcn, LocationAwareLogger.ERROR_INT, t, format, arguments)) {
				logger.error(StrUtil.format(format, arguments), t);
			}
		}
	}

	// ------------------------------------------------------------------------- Log
	@Override
	public void log(String fqcn, Level level, Throwable t, String format, Object... arguments) {
		int level_int;
		switch (level) {
		case TRACE:
			level_int = LocationAwareLogger.TRACE_INT;
			break;
		case DEBUG:
			level_int = LocationAwareLogger.DEBUG_INT;
			break;
		case INFO:
			level_int = LocationAwareLogger.INFO_INT;
			break;
		case WARN:
			level_int = LocationAwareLogger.WARN_INT;
			break;
		case ERROR:
			level_int = LocationAwareLogger.ERROR_INT;
			break;
		default:
			throw new Error(StrUtil.format("Can not identify level: {}", level));
		}
		this.locationAwareLog(fqcn, level_int, t, format, arguments);
	}

	// -------------------------------------------------------------------------------------------------- Private method
	/**
	 * 打印日志<br>
	 * 此方法用于兼容底层日志实现，通过传入当前包装类名，以解决打印日志中行号错误问题
	 * 
	 * @param fqcn 完全限定类名(Fully Qualified Class Name)，用于纠正定位错误行号
	 * @param level_int 日志级别，使用LocationAwareLogger中的常量
	 * @param t 异常
	 * @param msgTemplate 消息模板
	 * @param arguments 参数
	 * @return 是否支持 LocationAwareLogger对象，如果不支持需要日志方法调用被包装类的相应方法
	 */
	private boolean locationAwareLog(String fqcn, int level_int, Throwable t, String msgTemplate, Object[] arguments) {
		if (this.logger instanceof LocationAwareLogger) {
			// ((LocationAwareLogger)this.logger).log(null, fqcn, level_int, msgTemplate, arguments, t);
			// 由于slf4j-log4j12中此方法的实现存在bug，故在此拼接参数
			((LocationAwareLogger) this.logger).log(null, fqcn, level_int, StrUtil.format(msgTemplate, arguments), null, t);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取Slf4j Logger对象
	 * 
	 * @param clazz 打印日志所在类，当为{@code null}时使用“null”表示
	 * @return {@link Logger}
	 */
	private static Logger getSlf4jLogger(Class<?> clazz) {
		return (null == clazz) ? LoggerFactory.getLogger(StrUtil.EMPTY) : LoggerFactory.getLogger(clazz);
	}
}
