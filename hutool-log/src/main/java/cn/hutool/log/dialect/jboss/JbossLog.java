package cn.hutool.log.dialect.jboss;

import org.jboss.logging.Logger;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.AbstractLog;
import cn.hutool.log.level.Level;

/**
 * <a href="https://github.com/jboss-logging">Jboss-Logging</a> log.
 * 
 * @author Looly
 *
 */
public class JbossLog extends AbstractLog {
	private static final long serialVersionUID = -6843151523380063975L;

	private final transient Logger logger;

	// ------------------------------------------------------------------------- Constructor
	/**
	 * 构造
	 * 
	 * @param logger {@link Logger}
	 */
	public JbossLog(Logger logger) {
		this.logger = logger;
	}

	/**
	 * 构造
	 * 
	 * @param clazz 日志打印所在类
	 */
	public JbossLog(Class<?> clazz) {
		this((null == clazz) ? StrUtil.NULL : clazz.getName());
	}

	/**
	 * 构造
	 * 
	 * @param name 日志打印所在类名
	 */
	public JbossLog(String name) {
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
		if (isTraceEnabled()) {
			logger.trace(fqcn, StrUtil.format(format, arguments), t);
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
			logger.debug(fqcn, StrUtil.format(format, arguments), t);
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
			logger.info(fqcn, StrUtil.format(format, arguments), t);
		}
	}

	// ------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return logger.isEnabled(Logger.Level.WARN);
	}

	@Override
	public void warn(String fqcn, Throwable t, String format, Object... arguments) {
		if (isWarnEnabled()) {
			logger.warn(fqcn, StrUtil.format(format, arguments), t);
		}
	}

	// ------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return logger.isEnabled(Logger.Level.ERROR);
	}

	@Override
	public void error(String fqcn, Throwable t, String format, Object... arguments) {
		if (isErrorEnabled()) {
			logger.error(fqcn, StrUtil.format(format, arguments), t);
		}
	}

	// ------------------------------------------------------------------------- Log
	@Override
	public void log(String fqcn, Level level, Throwable t, String format, Object... arguments) {
		switch (level) {
		case TRACE:
			trace(fqcn, t, format, arguments);
			break;
		case DEBUG:
			debug(fqcn, t, format, arguments);
			break;
		case INFO:
			info(fqcn, t, format, arguments);
			break;
		case WARN:
			warn(fqcn, t, format, arguments);
			break;
		case ERROR:
			error(fqcn, t, format, arguments);
			break;
		default:
			throw new Error(StrUtil.format("Can not identify level: {}", level));
		}
	}
}
