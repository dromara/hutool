package cn.hutool.log.dialect.console;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.AbstractLog;
import cn.hutool.log.level.Level;

import java.util.Date;

/**
 * @author hongda.li 2022-04-27 09:55
 */
public class ConsoleColorLog extends AbstractLog {

	//-----------------------------------可供定制的不同级别的日志颜色代码---------------------------

	private static int ERROR = 31;
	private static int INFO = 32;
	private static int DEBUG = 32;
	private static int WARN = 33;
	private static int TRACE = 35;

	public static void setErrorColor(int errorColor) {
		ConsoleColorLog.ERROR = errorColor;
	}

	public static void setInfoColor(int infoColor) {
		ConsoleColorLog.INFO = infoColor;
	}

	public static void setDebugColor(int debugColor) {
		ConsoleColorLog.DEBUG = debugColor;
	}

	public static void setWarnColor(int warnColor) {
		ConsoleColorLog.WARN = warnColor;
	}

	public static void setTraceColor(int traceColor) {
		ConsoleColorLog.TRACE = traceColor;
	}

	/**
	 * 控制台打印类名的颜色代码
	 */
	private static final int CLASSNAME = 36;

	/**
	 * 控制台打印时间的颜色代码
	 */
	private static final int TIME = 37;

	/**
	 * 控制台打印正常信息的颜色代码
	 */
	private static final int NONE = 38;

	/**
	 * 系统换行符
	 */
	private static final String LINE_SEPARATOR = System.lineSeparator();

	/**
	 * 日志名称
	 */
	private final String name;

	/**
	 * 日志级别
	 */
	private static Level currentLevel = Level.DEBUG;

	/**
	 * 构造
	 *
	 * @param name 类名
	 */
	public ConsoleColorLog(String name) {
		this.name = name;
	}

	/**
	 * 构造
	 *
	 * @param clazz 类
	 */
	public ConsoleColorLog(Class<?> clazz) {
		this.name = (null == clazz) ? StrUtil.NULL : clazz.getName();
	}

	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * 设置自定义的日志显示级别
	 *
	 * @param customLevel 自定义级别
	 */
	public static void setLevel(Level customLevel) {
		Assert.notNull(customLevel);
		currentLevel = customLevel;
	}

	@Override
	public synchronized void log(String fqcn, Level level, Throwable t, String format, Object... arguments) {
		if (!isEnabled(level)) {
			return;
		}
		System.out.format("\33[%d;2m%s", TIME, DatePattern.NORM_DATETIME_MS_FORMAT.format(new Date()));
		switch (level) {
			case DEBUG:
				System.out.format("\33[%d;2m%-8s", DEBUG, "  DEBUG");
				System.out.format("\33[%d;2m%s", DEBUG, " --- ");
				break;
			case WARN:
				System.out.format("\33[%d;2m%-8s", WARN, "  WARN");
				System.out.format("\33[%d;2m%s", WARN, " --- ");
				break;
			case ERROR:
				System.out.format("\33[%d;2m%-8s", ERROR, "  ERROR");
				System.out.format("\33[%d;2m%s", ERROR, " --- ");
				break;
			case INFO:
				System.out.format("\33[%d;2m%-8s", INFO, "  INFO");
				System.out.format("\33[%d;2m%s", INFO, " --- ");
				break;
			case TRACE:
				System.out.format("\33[%d;2m%-8s", TRACE, "  TRACE");
				System.out.format("\33[%d;2m%s", TRACE, " --- ");
				break;
			default:
		}
		System.out.format("\33[%d;2m%-35s", CLASSNAME, "[" + ClassUtil.getShortClassName(name) + "]");
		System.out.format("\33[%d;2m%s", NONE, " : " + StrUtil.format(format, arguments));
		System.out.format("%s", LINE_SEPARATOR);
		System.out.format("\33[%d;2m%s", NONE, "");
	}

	@Override
	public boolean isDebugEnabled() {
		return isEnabled(Level.DEBUG);
	}

	@Override
	public void debug(String fqcn, Throwable t, String format, Object... arguments) {
		log(fqcn, Level.DEBUG, t, format, arguments);
	}

	@Override
	public boolean isErrorEnabled() {
		return isEnabled(Level.ERROR);
	}

	@Override
	public void error(String fqcn, Throwable t, String format, Object... arguments) {
		log(fqcn, Level.ERROR, t, format, arguments);
	}

	@Override
	public boolean isInfoEnabled() {
		return isEnabled(Level.INFO);
	}

	@Override
	public void info(String fqcn, Throwable t, String format, Object... arguments) {
		log(fqcn, Level.INFO, t, format, arguments);
	}

	@Override
	public boolean isTraceEnabled() {
		return isEnabled(Level.TRACE);
	}

	@Override
	public void trace(String fqcn, Throwable t, String format, Object... arguments) {
		log(fqcn, Level.TRACE, t, format, arguments);
	}

	@Override
	public boolean isWarnEnabled() {
		return isEnabled(Level.WARN);
	}

	@Override
	public void warn(String fqcn, Throwable t, String format, Object... arguments) {
		log(fqcn, Level.WARN, t, format, arguments);
	}

	@Override
	public boolean isEnabled(Level level) {
		return currentLevel.compareTo(level) <= 0;
	}
}
