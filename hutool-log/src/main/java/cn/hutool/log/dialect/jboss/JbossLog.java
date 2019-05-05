package cn.hutool.log.dialect.jboss;

import org.jboss.logging.Logger;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.AbstractLocationAwareLog;
import cn.hutool.log.level.Level;

/**
 * <a href="https://github.com/jboss-logging">Jboss-Logging</a> log.
 * 
 * @author Looly
 *
 */
public class JbossLog extends AbstractLocationAwareLog {
	private static final long serialVersionUID = -6843151523380063975L;
	private static final String FQCN = JbossLog.class.getName();

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
	public void trace(String format, Object... arguments) {
		trace(null, format, arguments);
	}

	@Override
	public void trace(Throwable t, String format, Object... arguments) {
		logger.trace(FQCN, StrUtil.format(format, arguments), t);
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
		logger.debug(FQCN, StrUtil.format(format, arguments), t);
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
		logger.info(FQCN, StrUtil.format(format, arguments), t);
	}

	// ------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return logger.isEnabled(Logger.Level.WARN);
	}

	@Override
	public void warn(String format, Object... arguments) {
		warn(null, format, arguments);
	}

	@Override
	public void warn(Throwable t, String format, Object... arguments) {
		logger.warn(FQCN, StrUtil.format(format, arguments), t);
	}

	// ------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return logger.isEnabled(Logger.Level.ERROR);
	}

	@Override
	public void error(String format, Object... arguments) {
		error(null, format, arguments);
	}

	@Override
	public void error(Throwable t, String format, Object... arguments) {
		logger.error(FQCN, StrUtil.format(format, arguments), t);
	}
	
	// ------------------------------------------------------------------------- Log
	@Override
	public void log(Level level, String format, Object... arguments) {
		this.log(level, null, format, arguments);
	}
	
	@Override
	public void log(Level level, Throwable t, String format, Object... arguments) {
		this.log(FQCN, level, t, format, arguments);
	}
	
	@Override
	public void log(String fqcn, Level level, Throwable t, String format, Object... arguments) {
		switch (level) {
			case TRACE:
				logger.trace(fqcn, StrUtil.format(format, arguments), t);
				break;
			case DEBUG:
				logger.debug(fqcn, StrUtil.format(format, arguments), t);
				break;
			case INFO:
				logger.info(fqcn, StrUtil.format(format, arguments), t);
				break;
			case WARN:
				logger.warn(fqcn, StrUtil.format(format, arguments), t);
				break;
			case ERROR:
				logger.error(fqcn, StrUtil.format(format, arguments), t);
				break;
			default:
				throw new Error(StrUtil.format("Can not identify level: {}", level));
		}
	}
}
