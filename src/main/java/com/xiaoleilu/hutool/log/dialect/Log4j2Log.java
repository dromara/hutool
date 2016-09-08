package com.xiaoleilu.hutool.log.dialect;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.AbstractLogger;

import com.xiaoleilu.hutool.log.AbstractLog;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * <a href="http://logging.apache.org/log4j/2.x/index.html">Apache Log4J 2</a> log.<br>
 * 
 * @author Looly
 *
 */
public class Log4j2Log extends AbstractLog {
	private static final long serialVersionUID = -6843151523380063975L;
	
	private static final String FQCN = Log4j2Log.class.getName();

	private final transient Logger logger;

	// ------------------------------------------------------------------------- Constructor
	public Log4j2Log(Logger logger) {
		this.logger = logger;
	}

	public Log4j2Log(Class<?> clazz) {
		this(LogManager.getLogger(clazz));
	}

	public Log4j2Log(String name) {
		this(LogManager.getLogger(name));
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
		if(false == logIfEnabled(Level.TRACE, format, arguments, t)){
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
		debug(null, format, arguments);
	}

	@Override
	public void debug(Throwable t, String format, Object... arguments) {
		if(false == logIfEnabled(Level.DEBUG, format, arguments, t)){
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
		info(null, format, arguments);
	}

	@Override
	public void info(Throwable t, String format, Object... arguments) {
		if(false == logIfEnabled(Level.INFO, format, arguments, t)){
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
		warn(null, format, arguments);
	}

	@Override
	public void warn(Throwable t, String format, Object... arguments) {
		if(false == logIfEnabled(Level.WARN, format, arguments, t)){
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
		error(null, format, arguments);
	}

	@Override
	public void error(Throwable t, String format, Object... arguments) {
		if(false == logIfEnabled(Level.ERROR, format, arguments, t)){
			logger.warn(StrUtil.format(format, arguments), t);
		}
	}

	// ------------------------------------------------------------------------- Private method
	/**
	 * 打印日志<br>
	 * 此方法用于兼容底层日志实现，通过传入当前包装类名，以解决打印日志中行号错误问题
	 * @param level 日志级别，使用org.apache.logging.log4j.Level中的常量
	 * @param msgTemplate 消息模板
	 * @param arguments 参数
	 * @param t 异常
	 * @return 是否支持 LocationAwareLogger对象，如果不支持需要日志方法调用被包装类的相应方法
	 */
	private boolean logIfEnabled(Level level, String msgTemplate, Object[] arguments, Throwable t) {
		if(this.logger instanceof AbstractLogger){
			((AbstractLogger)this.logger).logIfEnabled(FQCN, level, null, StrUtil.format(msgTemplate, arguments), t);
			return true;
		}else{
			return false;
		}
	}

}
