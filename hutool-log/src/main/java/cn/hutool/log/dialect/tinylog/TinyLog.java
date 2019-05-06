package cn.hutool.log.dialect.tinylog;

import org.pmw.tinylog.Level;
import org.pmw.tinylog.LogEntryForwarder;
import org.pmw.tinylog.Logger;

import cn.hutool.core.util.ArrayUtil;
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
	
	/** 堆栈增加层数，因为封装因此多了两层，此值用于正确获取当前类名 */
	private static final int DEPTH = 2;

	private int level;
	private String name;

	// ------------------------------------------------------------------------- Constructor
	public TinyLog(Class<?> clazz) {
		this(null == clazz ? StrUtil.NULL : clazz.getName());
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
		log(cn.hutool.log.level.Level.TRACE, format, arguments);
	}

	@Override
	public void trace(Throwable t, String format, Object... arguments) {
		log(cn.hutool.log.level.Level.TRACE, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Debug
	@Override
	public boolean isDebugEnabled() {
		return this.level <= org.pmw.tinylog.Level.DEBUG.ordinal();
	}

	@Override
	public void debug(String format, Object... arguments) {
		log(cn.hutool.log.level.Level.DEBUG, format, arguments);
	}

	@Override
	public void debug(Throwable t, String format, Object... arguments) {
		log(cn.hutool.log.level.Level.DEBUG, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Info
	@Override
	public boolean isInfoEnabled() {
		return this.level <= org.pmw.tinylog.Level.INFO.ordinal();
	}

	@Override
	public void info(String format, Object... arguments) {
		log(cn.hutool.log.level.Level.INFO, format, arguments);
	}

	@Override
	public void info(Throwable t, String format, Object... arguments) {
		log(cn.hutool.log.level.Level.INFO, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return this.level <= org.pmw.tinylog.Level.WARNING.ordinal();
	}

	@Override
	public void warn(String format, Object... arguments) {
		log(cn.hutool.log.level.Level.WARN, format, arguments);
	}

	@Override
	public void warn(Throwable t, String format, Object... arguments) {
		log(cn.hutool.log.level.Level.WARN, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return this.level <= org.pmw.tinylog.Level.ERROR.ordinal();
	}

	@Override
	public void error(String format, Object... arguments) {
		log(cn.hutool.log.level.Level.ERROR, format, arguments);
	}

	@Override
	public void error(Throwable t, String format, Object... arguments) {
		log(cn.hutool.log.level.Level.ERROR, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Log
	@Override
	public void log(cn.hutool.log.level.Level level, String format, Object... arguments) {
		LogEntryForwarder.forward(DEPTH, toTinyLevel(level), getLastArgumentIfThrowable(level, arguments), format, arguments);
	}

	@Override
	public void log(cn.hutool.log.level.Level level, Throwable t, String format, Object... arguments) {
		LogEntryForwarder.forward(DEPTH, toTinyLevel(level), t, format, arguments);
	}

	/**
	 * 将Hutool的Level等级转换为Tinylog的Level等级
	 * 
	 * @param level Hutool的Level等级
	 * @return Tinylog的Level
	 * @since 4.0.3
	 */
	private Level toTinyLevel(cn.hutool.log.level.Level level) {
		Level tinyLevel;
		switch (level) {
		case TRACE:
			tinyLevel = Level.TRACE;
			break;
		case DEBUG:
			tinyLevel = Level.DEBUG;
			break;
		case INFO:
			tinyLevel = Level.INFO;
			break;
		case WARN:
			tinyLevel = Level.WARNING;
			break;
		case ERROR:
			tinyLevel = Level.ERROR;
			break;
		case OFF:
			tinyLevel = Level.OFF;
			break;
		default:
			throw new Error(StrUtil.format("Can not identify level: {}", level));
		}
		return tinyLevel;
	}

	/**
	 * 如果最后一个参数为异常参数，则获取之，否则返回null
	 * 
	 * @param arguments 参数
	 * @return 最后一个异常参数
	 * @since 4.0.3
	 */
	private static Throwable getLastArgumentIfThrowable(Object... arguments) {
		if (ArrayUtil.isNotEmpty(arguments) && arguments[arguments.length - 1] instanceof Throwable) {
			return (Throwable) arguments[arguments.length - 1];
		} else {
			return null;
		}
	}
}
