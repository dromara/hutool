package com.xiaoleilu.hutool.log.dialect;

import com.xiaoleilu.hutool.lang.Dict;
import com.xiaoleilu.hutool.log.AbstractLog;
import com.xiaoleilu.hutool.log.LogLevel;
import com.xiaoleilu.hutool.util.DateUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 利用System.out.println()打印日志
 * @author Looly
 *
 */
public class ConsoleLog extends AbstractLog {
	private static final long serialVersionUID = -6843151523380063975L;

	private static String logFormat = "[{date}] [{level}] {name}: {msg}";
	private static LogLevel level = LogLevel.DEBUG;
	
	private String name;
	
	//------------------------------------------------------------------------- Constructor
	public ConsoleLog(Class<?> clazz) {
		this.name = clazz.getName();
	}
	
	public ConsoleLog(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	//------------------------------------------------------------------------- Trace
	@Override
	public boolean isTraceEnabled() {
		return level.compareTo(LogLevel.TRACE) <= 0;
	}

	@Override
	public void trace(String format, Object... arguments) {
		log(LogLevel.TRACE, format, arguments);
	}

	@Override
	public void trace(Throwable t, String format, Object... arguments) {
		log(LogLevel.TRACE, t, format, arguments);
	}

	//------------------------------------------------------------------------- Debug
	@Override
	public boolean isDebugEnabled() {
		return level.compareTo(LogLevel.DEBUG) <= 0;
	}

	@Override
	public void debug(String format, Object... arguments) {
		log(LogLevel.DEBUG, format, arguments);
	}

	@Override
	public void debug(Throwable t, String format, Object... arguments) {
		log(LogLevel.DEBUG, t, format, arguments);
	}

	//------------------------------------------------------------------------- Info
	@Override
	public boolean isInfoEnabled() {
		return level.compareTo(LogLevel.INFO) <= 0;
	}

	@Override
	public void info(String format, Object... arguments) {
		log(LogLevel.INFO, format, arguments);
	}

	@Override
	public void info(Throwable t, String format, Object... arguments) {
		log(LogLevel.INFO, t, format, arguments);
	}

	//------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return level.compareTo(LogLevel.WARN) <= 0;
	}

	@Override
	public void warn(String format, Object... arguments) {
		log(LogLevel.WARN, format, arguments);
	}

	@Override
	public void warn(Throwable t, String format, Object... arguments) {
		log(LogLevel.WARN, t, format, arguments);
	}

	//------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return level.compareTo(LogLevel.ERROR) <= 0;
	}

	@Override
	public void error(String format, Object... arguments) {
		log(LogLevel.ERROR, format, arguments);
	}

	@Override
	public void error(Throwable t, String format, Object... arguments) {
		log(LogLevel.ERROR, t, format, arguments);
	}
	
	@Override
	public void log(LogLevel level, String format, Object... arguments) {
		this.log(level, null, format, arguments);
	}

	@Override
	public void log(LogLevel level, Throwable t, String format, Object... arguments) {
		if(false == isEnabled(level)){
			return;
		}
		
		final Dict dict = Dict.create()
				.set("date", DateUtil.now())
				.set("level", level.toString())
				.set("name", this.name)
				.set("msg", StrUtil.format(format, arguments));
		
		String logMsg = StrUtil.format(logFormat, dict);
		
		//WARN以上级别打印至System.err
		if(level.ordinal() >= LogLevel.WARN.ordinal()){
			System.err.println(logMsg);
			if(null != t){
				t.printStackTrace(System.err);
				System.err.flush();
			}
		}else{
			System.out.println(logMsg);
			if(null != t){
				t.printStackTrace();
				System.out.flush();
			}
		}
		
	}
}