package cn.hutool.log.dialect.log4j2;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.AbstractLogger;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.AbstractLog;

/**
 * <a href="http://logging.apache.org/log4j/2.x/index.html">Apache Log4J 2</a> log.<br>
 * 
 * @author Looly
 *
 */
public class Log4j2Log extends AbstractLog {
	private static final long serialVersionUID = -6843151523380063975L;
	
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
	public void trace(String fqcn, Throwable t, String format, Object... arguments) {
		logIfEnabled(fqcn, Level.TRACE, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Debug
	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(String fqcn, Throwable t, String format, Object... arguments) {
		logIfEnabled(fqcn, Level.DEBUG, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Info
	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public void info(String fqcn, Throwable t, String format, Object... arguments) {
		logIfEnabled(fqcn, Level.INFO, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public void warn(String fqcn, Throwable t, String format, Object... arguments) {
		logIfEnabled(fqcn, Level.WARN, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public void error(String fqcn, Throwable t, String format, Object... arguments) {
		logIfEnabled(fqcn, Level.ERROR, t, format, arguments);
	}
	
	// ------------------------------------------------------------------------- Log
	@Override
	public void log(String fqcn, cn.hutool.log.level.Level level, Throwable t, String format, Object... arguments) {
		Level log4j2Level;
		switch (level) {
			case TRACE:
				log4j2Level = Level.TRACE;
				break;
			case DEBUG:
				log4j2Level = Level.DEBUG;
				break;
			case INFO:
				log4j2Level = Level.INFO;
				break;
			case WARN:
				log4j2Level = Level.WARN;
				break;
			case ERROR:
				log4j2Level = Level.ERROR;
				break;
			default:
				throw new Error(StrUtil.format("Can not identify level: {}", level));
		}
		logIfEnabled(fqcn, log4j2Level, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Private method
	/**
	 * 打印日志<br>
	 * 此方法用于兼容底层日志实现，通过传入当前包装类名，以解决打印日志中行号错误问题
	 * 
	 * @param fqcn 完全限定类名(Fully Qualified Class Name)，用于纠正定位错误行号
	 * @param level 日志级别，使用org.apache.logging.log4j.Level中的常量
	 * @param t 异常
	 * @param msgTemplate 消息模板
	 * @param arguments 参数
	 */
	private void logIfEnabled(String fqcn, Level level, Throwable t, String msgTemplate, Object... arguments) {
		if(this.logger.isEnabled(level)) {
			if(this.logger instanceof AbstractLogger){
				((AbstractLogger)this.logger).logIfEnabled(fqcn, level, null, StrUtil.format(msgTemplate, arguments), t);
			} else {
				// FQCN无效
				this.logger.log(level, StrUtil.format(msgTemplate, arguments), t);
			}
		}
	}
}
