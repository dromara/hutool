package cn.hutool.log;

import cn.hutool.core.lang.caller.CallerUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.level.Level;

/**
 * 静态日志类，用于在不引入日志对象的情况下打印日志
 * 
 * @author Looly
 *
 */
public final class StaticLog {
	private static final String FQCN = StaticLog.class.getName();

	private StaticLog() {
	}

	// ----------------------------------------------------------- Log method start
	// ------------------------ Trace
	/**
	 * Trace等级日志，小于debug<br>
	 * 由于动态获取Log，效率较低，建议在非频繁调用的情况下使用！！
	 * 
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void trace(String format, Object... arguments) {
		trace(LogFactory.get(CallerUtil.getCallerCaller()), format, arguments);
	}

	/**
	 * Trace等级日志，小于Debug
	 * 
	 * @param log 日志对象
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void trace(Log log, String format, Object... arguments) {
		log.trace(FQCN, null, format, arguments);
	}

	// ------------------------ debug
	/**
	 * Debug等级日志，小于Info<br>
	 * 由于动态获取Log，效率较低，建议在非频繁调用的情况下使用！！
	 * 
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void debug(String format, Object... arguments) {
		debug(LogFactory.get(CallerUtil.getCallerCaller()), format, arguments);
	}

	/**
	 * Debug等级日志，小于Info
	 * 
	 * @param log 日志对象
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void debug(Log log, String format, Object... arguments) {
		log.debug(FQCN, null, format, arguments);
	}

	// ------------------------ info
	/**
	 * Info等级日志，小于Warn<br>
	 * 由于动态获取Log，效率较低，建议在非频繁调用的情况下使用！！
	 * 
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void info(String format, Object... arguments) {
		info(LogFactory.get(CallerUtil.getCallerCaller()), format, arguments);
	}

	/**
	 * Info等级日志，小于Warn
	 * 
	 * @param log 日志对象
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void info(Log log, String format, Object... arguments) {
		log.info(FQCN, null, format, arguments);
	}

	// ------------------------ warn
	/**
	 * Warn等级日志，小于Error<br>
	 * 由于动态获取Log，效率较低，建议在非频繁调用的情况下使用！！
	 * 
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void warn(String format, Object... arguments) {
		warn(LogFactory.get(CallerUtil.getCallerCaller()), format, arguments);
	}

	/**
	 * Warn等级日志，小于Error<br>
	 * 由于动态获取Log，效率较低，建议在非频繁调用的情况下使用！！
	 * 
	 * @param e 需在日志中堆栈打印的异常
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void warn(Throwable e, String format, Object... arguments) {
		warn(LogFactory.get(CallerUtil.getCallerCaller()), e, StrUtil.format(format, arguments));
	}

	/**
	 * Warn等级日志，小于Error
	 * 
	 * @param log 日志对象
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void warn(Log log, String format, Object... arguments) {
		warn(log, null, format, arguments);
	}

	/**
	 * Warn等级日志，小于Error
	 * 
	 * @param log 日志对象
	 * @param e 需在日志中堆栈打印的异常
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void warn(Log log, Throwable e, String format, Object... arguments) {
		log.warn(FQCN, e, format, arguments);
	}

	// ------------------------ error
	/**
	 * Error等级日志<br>
	 * 由于动态获取Log，效率较低，建议在非频繁调用的情况下使用！！
	 * 
	 * @param e 需在日志中堆栈打印的异常
	 */
	public static void error(Throwable e) {
		error(LogFactory.get(CallerUtil.getCallerCaller()), e);
	}

	/**
	 * Error等级日志<br>
	 * 由于动态获取Log，效率较低，建议在非频繁调用的情况下使用！！
	 * 
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void error(String format, Object... arguments) {
		error(LogFactory.get(CallerUtil.getCallerCaller()), format, arguments);
	}

	/**
	 * Error等级日志<br>
	 * 由于动态获取Log，效率较低，建议在非频繁调用的情况下使用！！
	 * 
	 * @param e 需在日志中堆栈打印的异常
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void error(Throwable e, String format, Object... arguments) {
		error(LogFactory.get(CallerUtil.getCallerCaller()), e, format, arguments);
	}

	/**
	 * Error等级日志<br>
	 * 
	 * @param log 日志对象
	 * @param e 需在日志中堆栈打印的异常
	 */
	public static void error(Log log, Throwable e) {
		error(log, e, e.getMessage());
	}

	/**
	 * Error等级日志<br>
	 * 
	 * @param log 日志对象
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void error(Log log, String format, Object... arguments) {
		error(log, null, format, arguments);
	}

	/**
	 * Error等级日志<br>
	 * 
	 * @param log 日志对象
	 * @param e 需在日志中堆栈打印的异常
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void error(Log log, Throwable e, String format, Object... arguments) {
		log.error(FQCN, e, format, arguments);
	}

	// ------------------------ Log
	/**
	 * 打印日志<br>
	 * 
	 * @param level 日志级别
	 * @param t 需在日志中堆栈打印的异常
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void log(Level level, Throwable t, String format, Object... arguments) {
		LogFactory.get(CallerUtil.getCallerCaller()).log(FQCN, level, t, format, arguments);
	}

	// ----------------------------------------------------------- Log method end

	/**
	 * 获得Log
	 * 
	 * @param clazz 日志发出的类
	 * @return Log
	 * @deprecated 请使用 {@link Log#get(Class)}
	 */
	@Deprecated
	public static Log get(Class<?> clazz) {
		return LogFactory.get(clazz);
	}

	/**
	 * 获得Log
	 * 
	 * @param name 自定义的日志发出者名称
	 * @return Log
	 * @deprecated 请使用 {@link Log#get(String)}
	 */
	@Deprecated
	public static Log get(String name) {
		return LogFactory.get(name);
	}

	/**
	 * @return 获得日志，自动判定日志发出者
	 * @deprecated 请使用 {@link Log#get()}
	 */
	@Deprecated
	public static Log get() {
		return LogFactory.get(CallerUtil.getCallerCaller());
	}
}
