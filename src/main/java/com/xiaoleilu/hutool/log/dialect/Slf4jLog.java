package com.xiaoleilu.hutool.log.dialect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

import com.xiaoleilu.hutool.log.AbstractLog;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * <a href="http://www.slf4j.org/">SLF4J</a> log.<br>
 * 同样无缝支持 <a href="http://logback.qos.ch/">LogBack</a>
 * 
 * @author Looly
 *
 */
public class Slf4jLog extends AbstractLog {
	private static final long serialVersionUID = -6843151523380063975L;
	private static final String FQCN = Slf4jLog.class.getName();

	private final transient Logger logger;

	// ------------------------------------------------------------------------- Constructor
	public Slf4jLog(Logger logger) {
		this.logger = logger;
	}

	public Slf4jLog(Class<?> clazz) {
		this(LoggerFactory.getLogger(clazz));
	}

	public Slf4jLog(String name) {
		this(LoggerFactory.getLogger(name));
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
		if(false == locationAwareLog(LocationAwareLogger.TRACE_INT, format, arguments)){
			logger.trace(format, arguments);
		}
	}

	@Override
	public void trace(Throwable t, String format, Object... arguments) {
		if(false == locationAwareLog(LocationAwareLogger.TRACE_INT, format, arguments, t)){
			logger.trace(StrUtil.format(format, arguments), t);
		}
	}

	// ------------------------------------------------------------------------- Debug
	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(String format, Object... arguments) {
		if(false == locationAwareLog(LocationAwareLogger.DEBUG_INT, format, arguments)){
			logger.debug(format, arguments);
		}
	}

	@Override
	public void debug(Throwable t, String format, Object... arguments) {
		if(false == locationAwareLog(LocationAwareLogger.DEBUG_INT, format, arguments, t)){
			logger.debug(StrUtil.format(format, arguments), t);
		}
	}

	// ------------------------------------------------------------------------- Info
	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public void info(String format, Object... arguments) {
		if(false == locationAwareLog(LocationAwareLogger.INFO_INT, format, arguments)){
			logger.info(format, arguments);
		}
	}

	@Override
	public void info(Throwable t, String format, Object... arguments) {
		if(false == locationAwareLog(LocationAwareLogger.INFO_INT, format, arguments, t)){
			logger.info(StrUtil.format(format, arguments), t);
		}
	}

	// ------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public void warn(String format, Object... arguments) {
		if(false == locationAwareLog(LocationAwareLogger.WARN_INT, format, arguments)){
			logger.warn(format, arguments);
		}
	}

	@Override
	public void warn(Throwable t, String format, Object... arguments) {
		if(false == locationAwareLog(LocationAwareLogger.WARN_INT, format, arguments, t)){
			logger.warn(StrUtil.format(format, arguments), t);
		}
	}

	// ------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public void error(String format, Object... arguments) {
		if(false == locationAwareLog(LocationAwareLogger.ERROR_INT, format, arguments)){
			logger.error(format, arguments);
		}
	}

	@Override
	public void error(Throwable t, String format, Object... arguments) {
		if(false == locationAwareLog(LocationAwareLogger.ERROR_INT, format, arguments, t)){
			logger.error(StrUtil.format(format, arguments), t);
		}
	}
	
	//-------------------------------------------------------------------------------------------------- Private method
	/**
	 * 打印日志<br>
	 * 此方法用于兼容底层日志实现，通过传入当前包装类名，以解决打印日志中行号错误问题
	 * @param level_int 日志级别，使用LocationAwareLogger中的常量
	 * @param msgTemplate 消息模板
	 * @param arguments 参数
	 * @return 是否支持 LocationAwareLogger对象，如果不支持需要日志方法调用被包装类的相应方法
	 */
	private boolean locationAwareLog(int level_int, String msgTemplate, Object[] arguments) {
		return locationAwareLog(level_int, msgTemplate, arguments, null);
	}

	/**
	 * 打印日志<br>
	 * 此方法用于兼容底层日志实现，通过传入当前包装类名，以解决打印日志中行号错误问题
	 * @param level_int 日志级别，使用LocationAwareLogger中的常量
	 * @param msgTemplate 消息模板
	 * @param arguments 参数
	 * @param t 异常
	 * @return 是否支持 LocationAwareLogger对象，如果不支持需要日志方法调用被包装类的相应方法
	 */
	private boolean locationAwareLog(int level_int, String msgTemplate, Object[] arguments, Throwable t) {
		if(this.logger instanceof LocationAwareLogger){
			((LocationAwareLogger)this.logger).log(null, FQCN, level_int, msgTemplate, arguments, t);
			return true;
		}else{
			return false;
		}
	}
}
