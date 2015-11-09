package com.xiaoleilu.hutool.log;

import java.util.Arrays;
import java.util.List;

import com.xiaoleilu.hutool.ClassUtil;
import com.xiaoleilu.hutool.StrUtil;
import com.xiaoleilu.hutool.log.dialect.ApacheCommonsLog;
import com.xiaoleilu.hutool.log.dialect.JdkLog;
import com.xiaoleilu.hutool.log.dialect.Log4jLog;
import com.xiaoleilu.hutool.log.dialect.Slf4jLog;

/**
 * 静态日志类，用于在不引入日志对象的情况下打印日志
 * @author Looly
 *
 */
public class StaticLog {
	
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
		trace(innerGet(), format, arguments);
	}

	/**
	 * Trace等级日志，小于Debug
	 * 
	 * @param log 日志对象
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void trace(Log log, String format, Object... arguments) {
		log.trace(format, arguments);
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
		debug(innerGet(), format, arguments);
	}

	/**
	 * Debug等级日志，小于Info
	 * 
	 * @param log 日志对象
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void debug(Log log, String format, Object... arguments) {
		log.debug(format, arguments);
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
		info(innerGet(), format, arguments);
	}

	/**
	 * Info等级日志，小于Warn
	 * 
	 * @param log 日志对象
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void info(Log log, String format, Object... arguments) {
		log.info(format, arguments);
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
		warn(innerGet(), format, arguments);
	}

	/**
	 * Warn等级日志，小于Error
	 * 
	 * @param log 日志对象
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void warn(Log log, String format, Object... arguments) {
		log.warn(format, arguments);
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
		warn(innerGet(), e, StrUtil.format(format, arguments));
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
		log.warn(StrUtil.format(format, arguments), e);
	}

	// ------------------------ error
	/**
	 * Error等级日志<br>
	 * 由于动态获取Log，效率较低，建议在非频繁调用的情况下使用！！
	 * 
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void error(String format, Object... arguments) {
		error(innerGet(), format, arguments);
	}

	/**
	 * Error等级日志<br>
	 * 
	 * @param log 日志对象
	 * @param format 格式文本，{} 代表变量
	 * @param arguments 变量对应的参数
	 */
	public static void error(Log log, String format, Object... arguments) {
		log.error(format, arguments);
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
		error(innerGet(), e, StrUtil.format(format, arguments));
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
		log.error(StrUtil.format(format, arguments), e);
	}

	/**
	 * Error等级日志<br>
	 * 
	 * @param log 日志对象
	 * @param e 需在日志中堆栈打印的异常
	 */
	public static void error(Log log, Throwable e) {
		log.error(e.getMessage(), e);
	}

	/**
	 * Error等级日志<br>
	 * 由于动态获取Log，效率较低，建议在非频繁调用的情况下使用！！
	 * 
	 * @param e 需在日志中堆栈打印的异常
	 */
	public static void error(Throwable e) {
		innerGet().error(e.getMessage(), e);
	}
	// ----------------------------------------------------------- Log method end
	
	private static Log initialLog;
	
	/**
	 * 获得Log
	 * @param clazz 日志发出的类
	 * @return Log
	 */
	public static Log get(Class<?> clazz) {
		return innerGet(initialLog, clazz);
	}

	/**
	 * 获得Log
	 * @param name 自定义的日志发出者名称
	 * @return Log
	 */
	public static Log get(String name) {
		return innerGet(initialLog, name);
	}
	
	/**
	 * @return 获得日志，自动判定日志发出者
	 */
	public static Log get() {
		final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		return get(stackTrace[2].getClassName());
	}
	
	//----------------------------------------------------------- Private method start
	/**
	 * 根据已有的Log类型创建Log<br>
	 * 此方法存在的原因是可以减少Log类型的判断，提高性能
	 * @param log 已经创建的日志对象
	 * @param name 日志名
	 * @return Log
	 */
	private static Log innerGet(Log log, String name){
		if(null == log){
			return innerGet(name);
		}
		
		if(log instanceof Slf4jLog){
			return new Slf4jLog(name);
		}
		if(log instanceof Log4jLog){
			return new Log4jLog(name);
		}
		if(log instanceof ApacheCommonsLog){
			return new ApacheCommonsLog(name);
		}
		return new JdkLog(name);
	}
	
	/**
	 * 根据已有的Log类型创建Log<br>
	 * 此方法存在的原因是可以减少Log类型的判断，提高性能
	 * @param log 已经创建的日志对象
	 * @param name 打印日志的类
	 * @return Log
	 */
	private static Log innerGet(Log log, Class<?> clazz){
		if(null == log){
			return innerGet(clazz);
		}
		
		if(log instanceof Slf4jLog){
			return new Slf4jLog(clazz);
		}
		if(log instanceof Log4jLog){
			return new Log4jLog(clazz);
		}
		if(log instanceof ApacheCommonsLog){
			return new ApacheCommonsLog(clazz);
		}
		return new JdkLog(clazz);
	}
	
	/**
	 * 获得Log
	 * @param param 参数，String或者Class类型
	 * @return Log
	 */
	private static Log innerGet(Object param){
		List<Class<? extends AbstractLog>> logClassList = Arrays.asList(
				Slf4jLog.class,
				Log4jLog.class, 
				ApacheCommonsLog.class, 
				JdkLog.class
		);
		
		Log log = null;
		for (Class<? extends AbstractLog> logClass : logClassList) {
			try {
				log = ClassUtil.newInstance(logClass, param);
				System.out.println("Use :" + log.getClass().getName());
			} catch (Exception e) {
				continue;
			}
			break;
		}
		
		return log;
	}
	
	/**
	 * @return 获得日志，自动判定日志发出者
	 */
	private static Log innerGet() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		return get(stackTrace[3].getClassName());
	}
	//----------------------------------------------------------- Private method end
}
