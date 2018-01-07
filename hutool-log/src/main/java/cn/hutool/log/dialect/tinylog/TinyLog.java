package cn.hutool.log.dialect.tinylog;

import org.pmw.tinylog.Logger;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.AbstractLog;

/**
 * <a href="http://www.tinylog.org/">tinylog</a> log.<br>
 * 
 * @author Looly
 *
 */
public class TinyLog extends AbstractLog {
	private static final long serialVersionUID = -4848042277045993735L;

	private int level;
	private String name;

	// ------------------------------------------------------------------------- Constructor
	public TinyLog(Class<?> clazz) {
		this.name = clazz.getName();
		this.level = Logger.getLevel(name).ordinal();
	}

	public TinyLog(String name) {
		this.name = name;
		this.level = Logger.getLevel(name).ordinal();
	}

	@Override
	public String getName() {
		return this.name;
	}

	// ------------------------------------------------------------------------- Trace
	@Override
	public boolean isTraceEnabled() {
		return this.level <= org.pmw.tinylog.Level.TRACE.ordinal();
	}

	@Override
	public void trace(String format, Object... arguments) {
		Logger.trace(format, arguments);
	}

	@Override
	public void trace(Throwable t, String format, Object... arguments) {
		Logger.trace(t, format, arguments);
	}

	// ------------------------------------------------------------------------- Debug
	@Override
	public boolean isDebugEnabled() {
		return this.level <= org.pmw.tinylog.Level.DEBUG.ordinal();
	}

	@Override
	public void debug(String format, Object... arguments) {
		Logger.debug(format, arguments);
	}

	@Override
	public void debug(Throwable t, String format, Object... arguments) {
		Logger.debug(t, format, arguments);
	}

	// ------------------------------------------------------------------------- Info
	@Override
	public boolean isInfoEnabled() {
		return this.level <= org.pmw.tinylog.Level.INFO.ordinal();
	}

	@Override
	public void info(String format, Object... arguments) {
		Logger.info(format, arguments);
	}

	@Override
	public void info(Throwable t, String format, Object... arguments) {
		Logger.info(t, format, arguments);
	}

	// ------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return this.level <= org.pmw.tinylog.Level.WARNING.ordinal();
	}

	@Override
	public void warn(String format, Object... arguments) {
		Logger.warn(format, arguments);
	}

	@Override
	public void warn(Throwable t, String format, Object... arguments) {
		Logger.warn(t, format, arguments);
	}

	// ------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return this.level <= org.pmw.tinylog.Level.ERROR.ordinal();
	}

	@Override
	public void error(String format, Object... arguments) {
		Logger.error(format, arguments);
	}

	@Override
	public void error(Throwable t, String format, Object... arguments) {
		Logger.error(t, format, arguments);
	}

	// ------------------------------------------------------------------------- Log
	@Override
	public void log(cn.hutool.log.level.Level level, String format, Object... arguments) {
		this.log(level, null, format, arguments);
	}

	@Override
	public void log(cn.hutool.log.level.Level level, Throwable t, String format, Object... arguments) {
		switch (level) {
			case TRACE:
				trace(t, format, arguments);
				break;
			case DEBUG:
				debug(t, format, arguments);
				break;
			case INFO:
				info(t, format, arguments);
				break;
			case WARN:
				warn(t, format, arguments);
				break;
			case ERROR:
				error(t, format, arguments);
				break;
			default:
				throw new Error(StrUtil.format("Can not identify level: {}", level));
		}
	}

}
