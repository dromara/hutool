package cn.hutool.log.dialect.jdk;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.AbstractLocationAwareLog;

/**
 * <a href="http://java.sun.com/javase/6/docs/technotes/guides/logging/index.html">java.util.logging</a> log.
 * 
 * @author Looly
 *
 */
public class JdkLog extends AbstractLocationAwareLog {
	private static final long serialVersionUID = -6843151523380063975L;
	
	/** 本类的全限定类名 */
	private static final String FQCN_SELF = JdkLog.class.getName();

	private final transient Logger logger;

	// ------------------------------------------------------------------------- Constructor
	public JdkLog(Logger logger) {
		this.logger = logger;
	}

	public JdkLog(Class<?> clazz) {
		this((null == clazz) ? StrUtil.NULL : clazz.getName());
	}

	public JdkLog(String name) {
		this(Logger.getLogger(name));
	}
	
	@Override
	public String getName() {
		return logger.getName();
	}

	// ------------------------------------------------------------------------- Trace
	@Override
	public boolean isTraceEnabled() {
		return logger.isLoggable(Level.FINEST);
	}

	@Override
	public void trace(String format, Object... arguments) {
		logIfEnabled(Level.FINEST, null, format, arguments);
	}

	@Override
	public void trace(Throwable t, String format, Object... arguments) {
		logIfEnabled(Level.FINEST, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Debug
	@Override
	public boolean isDebugEnabled() {
		return logger.isLoggable(Level.FINE);
	}

	@Override
	public void debug(String format, Object... arguments) {
		logIfEnabled(Level.FINE, null, format, arguments);
	}

	@Override
	public void debug(Throwable t, String format, Object... arguments) {
		logIfEnabled(Level.FINE, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Info
	@Override
	public boolean isInfoEnabled() {
		return logger.isLoggable(Level.INFO);
	}

	@Override
	public void info(String format, Object... arguments) {
		logIfEnabled(Level.INFO, null, format, arguments);
	}

	@Override
	public void info(Throwable t, String format, Object... arguments) {
		logIfEnabled(Level.INFO, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return logger.isLoggable(Level.WARNING);
	}

	@Override
	public void warn(String format, Object... arguments) {
		logIfEnabled(Level.WARNING, null, format, arguments);
	}

	@Override
	public void warn(Throwable t, String format, Object... arguments) {
		logIfEnabled(Level.WARNING, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return logger.isLoggable(Level.SEVERE);
	}

	@Override
	public void error(String format, Object... arguments) {
		logIfEnabled(Level.SEVERE, null, format, arguments);
	}

	@Override
	public void error(Throwable t, String format, Object... arguments) {
		logIfEnabled(Level.SEVERE, t, format, arguments);
	}
	
	// ------------------------------------------------------------------------- Log
	@Override
	public void log(cn.hutool.log.level.Level level, String format, Object... arguments) {
		this.log(level, null, format, arguments);
	}
	
	@Override
	public void log(cn.hutool.log.level.Level level, Throwable t, String format, Object... arguments) {
		this.log(FQCN_SELF, level, t, format, arguments);
	}
	
	@Override
	public void log(String fqcn, cn.hutool.log.level.Level level, Throwable t, String format, Object... arguments) {
		Level jdkLevel;
		switch (level) {
			case TRACE:
				jdkLevel = Level.FINEST;
				break;
			case DEBUG:
				jdkLevel = Level.FINE;
				break;
			case INFO:
				jdkLevel = Level.INFO;
				break;
			case WARN:
				jdkLevel = Level.WARNING;
				break;
			case ERROR:
				jdkLevel = Level.SEVERE;
				break;
			default:
				throw new Error(StrUtil.format("Can not identify level: {}", level));
		}
		logIfEnabled(fqcn, jdkLevel, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Private method
	/**
	 * 打印对应等级的日志
	 * 
	 * @param level 等级
	 * @param throwable 异常对象
	 * @param format 消息模板
	 * @param arguments 参数
	 */
	private void logIfEnabled(Level level, Throwable throwable, String format, Object[] arguments){
		this.logIfEnabled(FQCN_SELF, level, throwable, format, arguments);
	}
	
	/**
	 * 打印对应等级的日志
	 * 
	 * @param callerFQCN
	 * @param level 等级
	 * @param throwable 异常对象
	 * @param format 消息模板
	 * @param arguments 参数
	 */
	private void logIfEnabled(String callerFQCN, Level level, Throwable throwable, String format, Object[] arguments){
		if(logger.isLoggable(level)){
			LogRecord record = new LogRecord(level, StrUtil.format(format, arguments));
			record.setLoggerName(getName());
			record.setThrown(throwable);
			fillCallerData(callerFQCN, record);
			logger.log(record);
		}
	}
	
	/**
	 * 传入调用日志类的信息
	 * @param callerFQCN 调用者全限定类名
	 * @param superFQCN 调用者父类全限定名
	 * @param record The record to update
	 */
	private static void fillCallerData(String callerFQCN, LogRecord record) {
		StackTraceElement[] steArray = new Throwable().getStackTrace();

		int found = -1;
		String className;
		for (int i = 0; i < steArray.length; i++) {
			className = steArray[i].getClassName();
			if (className.equals(callerFQCN)) {
				found = i;
				break;
			}
		}

		if (found > -1 && found < steArray.length -1) {
			StackTraceElement ste = steArray[found+1];
			record.setSourceClassName(ste.getClassName());
			record.setSourceMethodName(ste.getMethodName());
		}
	}
}
