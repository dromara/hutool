package cn.hutool.log.dialect.jdk;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.AbstractLog;

/**
 * <a href="http://java.sun.com/javase/6/docs/technotes/guides/logging/index.html">java.util.logging</a> log.
 * 
 * @author Looly
 *
 */
public class JdkLog extends AbstractLog {
	private static final long serialVersionUID = -6843151523380063975L;
	
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
	public void trace(String fqcn, Throwable t, String format, Object... arguments) {
		logIfEnabled(fqcn, Level.FINEST, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Debug
	@Override
	public boolean isDebugEnabled() {
		return logger.isLoggable(Level.FINE);
	}

	@Override
	public void debug(String fqcn, Throwable t, String format, Object... arguments) {
		logIfEnabled(fqcn, Level.FINE, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Info
	@Override
	public boolean isInfoEnabled() {
		return logger.isLoggable(Level.INFO);
	}

	@Override
	public void info(String fqcn, Throwable t, String format, Object... arguments) {
		logIfEnabled(fqcn, Level.INFO, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Warn
	@Override
	public boolean isWarnEnabled() {
		return logger.isLoggable(Level.WARNING);
	}

	@Override
	public void warn(String fqcn, Throwable t, String format, Object... arguments) {
		logIfEnabled(fqcn, Level.WARNING, t, format, arguments);
	}

	// ------------------------------------------------------------------------- Error
	@Override
	public boolean isErrorEnabled() {
		return logger.isLoggable(Level.SEVERE);
	}

	@Override
	public void error(String fqcn, Throwable t, String format, Object... arguments) {
		logIfEnabled(fqcn, Level.SEVERE, t, format, arguments);
	}
	
	// ------------------------------------------------------------------------- Log
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
	 * @param callerFQCN 调用者的完全限定类名(Fully Qualified Class Name)
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
	 * @param record The record to update
	 */
	private static void fillCallerData(String callerFQCN, LogRecord record) {
		StackTraceElement[] steArray = Thread.currentThread().getStackTrace();

		int found = -1;
		String className;
		for (int i = steArray.length -2; i > -1; i--) {
			// 此处初始值为length-2，表示从倒数第二个堆栈开始检查，如果是倒数第一个，那调用者就获取不到
			className = steArray[i].getClassName();
			if (callerFQCN.equals(className)) {
				found = i;
				break;
			}
		}
		
		if (found > -1) {
			StackTraceElement ste = steArray[found+1];
			record.setSourceClassName(ste.getClassName());
			record.setSourceMethodName(ste.getMethodName());
		}
	}
}
