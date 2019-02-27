package cn.hutool.log.dialect.console;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.AbstractLog;
import cn.hutool.log.level.Level;

/**
 * 利用System.out.println()打印日志
 * @author Looly
 *
 */
public class ConsoleLog extends AbstractLog {
	private static final long serialVersionUID = -6843151523380063975L;

	private static String logFormat = "[{date}] [{level}] {name}: {msg}";
	private static Level level = Level.DEBUG;
	
	private String name;
	
	//------------------------------------------------------------------------- Constructor
	/**
	 * 构造
	 * 
	 * @param clazz 类
	 */
	public ConsoleLog(Class<?> clazz) {
		this.name = clazz.getName();
	}
	
	/**
	 * 构造
	 * 
	 * @param name 类名
	 */
	public ConsoleLog(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	/**
	 * 设置自定义的日志显示级别
	 * @param customLevel 自定义级别
	 * @since 4.1.10
	 */
	public static void setLevel(Level customLevel) {
		Assert.notNull(customLevel);
		level = customLevel;
	}

	//------------------------------------------------------------------------- Trace
	@Override
	public boolean isTraceEnabled() {
		return level.compareTo(Level.TRACE) <= 0;
	}

	@Override
	public void trace(String format, Object... arguments) {
		log(Level.TRACE, format, arguments);
	}

	@Override
	public void trace(Throwable t, String format, Object... arguments) {
		log(Level.TRACE, t, format, arguments);
	}

	//------------------------------------------------------------------------- Debug
	@Override
	public boolean isDebugEnabled() {
		return level.compareTo(Level.DEBUG) <= 0;
	}

	@Override
	public void debug(String format, Object... arguments) {
		log(Level.DEBUG, format, arguments);
	}

	@Override
	public void debug(Throwable t, String format, Object... arguments) {
		log(Level.DEBUG, t, format, arguments);
	}

	//------------------------------------------------------------------------- Info
	@Override
	public boolean isInfoEnabled() {
		return level.compareTo(Level.INFO) <= 0;
	}

	@Override
	public void info(String format, Object... arguments) {
		log(Level.INFO, format, arguments);
	}

	@Override
	public void info(Throwable t, String format, Object... arguments) {
		log(Level.INFO, t, format, arguments);
	}

	//------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return level.compareTo(Level.WARN) <= 0;
	}

	@Override
	public void warn(String format, Object... arguments) {
		log(Level.WARN, format, arguments);
	}

	@Override
	public void warn(Throwable t, String format, Object... arguments) {
		log(Level.WARN, t, format, arguments);
	}

	//------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return level.compareTo(Level.ERROR) <= 0;
	}

	@Override
	public void error(String format, Object... arguments) {
		log(Level.ERROR, format, arguments);
	}

	@Override
	public void error(Throwable t, String format, Object... arguments) {
		log(Level.ERROR, t, format, arguments);
	}
	
	//------------------------------------------------------------------------- Log
	@Override
	public void log(Level level, String format, Object... arguments) {
		this.log(level, null, format, arguments);
	}

	@Override
	public void log(Level level, Throwable t, String format, Object... arguments) {
		if(false == isEnabled(level)){
			return;
		}
		
		final Dict dict = Dict.create()
				.set("date", DateUtil.now())
				.set("level", level.toString())
				.set("name", this.name)
				.set("msg", StrUtil.format(format, arguments));
		
		final String logMsg = StrUtil.format(logFormat, dict);
		
		//WARN以上级别打印至System.err
		if(level.ordinal() >= Level.WARN.ordinal()){
			Console.error(t, logMsg);
		}else{
			Console.log(t, logMsg);
		}
		
	}
}