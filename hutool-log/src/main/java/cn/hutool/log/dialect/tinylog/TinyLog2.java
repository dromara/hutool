package cn.hutool.log.dialect.tinylog;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.AbstractLog;
import org.tinylog.Level;
import org.tinylog.configuration.Configuration;
import org.tinylog.format.AdvancedMessageFormatter;
import org.tinylog.format.MessageFormatter;
import org.tinylog.provider.LoggingProvider;
import org.tinylog.provider.ProviderRegistry;

/**
 * <a href="http://www.tinylog.org/">tinylog</a> log.<br>
 * 
 * @author Looly
 *
 */
public class TinyLog2 extends AbstractLog {
	private static final long serialVersionUID = 1L;

	/** 堆栈增加层数，因为封装因此多了两层，此值用于正确获取当前类名 */
	private static final int DEPTH = 5;

	private final int level;
	private final String name;
	private static final LoggingProvider provider = ProviderRegistry.getLoggingProvider();
	private static final MessageFormatter formatter = new AdvancedMessageFormatter(
			Configuration.getLocale(),
			Configuration.isEscapingEnabled()
	);

	// ------------------------------------------------------------------------- Constructor
	public TinyLog2(Class<?> clazz) {
		this(null == clazz ? StrUtil.NULL : clazz.getName());
	}

	public TinyLog2(String name) {
		this.name = name;
		this.level = provider.getMinimumLevel().ordinal();
	}

	@Override
	public String getName() {
		return this.name;
	}

	// ------------------------------------------------------------------------- Trace
	@Override
	public boolean isTraceEnabled() {
		return this.level <= Level.TRACE.ordinal();
	}

	@Override
	public void trace(String fqcn, Throwable t, String format, Object... arguments) {
		logIfEnabled(fqcn, Level.TRACE, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Debug
	@Override
	public boolean isDebugEnabled() {
		return this.level <= Level.DEBUG.ordinal();
	}

	@Override
	public void debug(String fqcn, Throwable t, String format, Object... arguments) {
		logIfEnabled(fqcn, Level.DEBUG, t, format, arguments);
	}
	// ------------------------------------------------------------------------- Info
	@Override
	public boolean isInfoEnabled() {
		return this.level <= Level.INFO.ordinal();
	}

	@Override
	public void info(String fqcn, Throwable t, String format, Object... arguments) {
		logIfEnabled(fqcn, Level.INFO, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return this.level <= Level.WARN.ordinal();
	}

	@Override
	public void warn(String fqcn, Throwable t, String format, Object... arguments) {
		logIfEnabled(fqcn, Level.WARN, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return this.level <= Level.ERROR.ordinal();
	}

	@Override
	public void error(String fqcn, Throwable t, String format, Object... arguments) {
		logIfEnabled(fqcn, Level.ERROR, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Log
	@Override
	public void log(String fqcn, cn.hutool.log.level.Level level, Throwable t, String format, Object... arguments) {
		logIfEnabled(fqcn, toTinyLevel(level), t, format, arguments);
	}
	
	@Override
	public boolean isEnabled(cn.hutool.log.level.Level level) {
		return this.level <= toTinyLevel(level).ordinal();
	}
	
	/**
	 * 在对应日志级别打开情况下打印日志
	 * @param fqcn 完全限定类名(Fully Qualified Class Name)，用于定位日志位置
	 * @param level 日志级别
	 * @param t 异常，null则检查最后一个参数是否为Throwable类型，是则取之，否则不打印堆栈
	 * @param format 日志消息模板
	 * @param arguments 日志消息参数
	 */
	private void logIfEnabled(String fqcn, Level level, Throwable t, String format, Object... arguments) {
		// fqcn 无效
		if(null == t){
			t = getLastArgumentIfThrowable(arguments);
		}
		provider.log(DEPTH, null, level, t, formatter, StrUtil.toString(format), arguments);
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
			tinyLevel = Level.WARN;
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
