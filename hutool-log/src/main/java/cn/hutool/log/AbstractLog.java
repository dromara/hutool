package cn.hutool.log;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.level.Level;

import java.io.Serializable;

/**
 * 抽象日志类<br>
 * 实现了一些通用的接口
 * 
 * @author Looly
 *
 */
public abstract class AbstractLog implements Log, Serializable{
	
	private static final long serialVersionUID = -3211115409504005616L;
	private static final String FQCN = AbstractLog.class.getName();
	
	@Override
	public boolean isEnabled(Level level) {
		switch (level) {
			case TRACE:
				return isTraceEnabled();
			case DEBUG:
				return isDebugEnabled();
			case INFO:
				return isInfoEnabled();
			case WARN:
				return isWarnEnabled();
			case ERROR:
				return isErrorEnabled();
			default:
				throw new Error(StrUtil.format("Can not identify level: {}", level));
		}
	}
	
	@Override
	public void trace(Throwable t) {
		trace(t, ExceptionUtil.getSimpleMessage(t));
	}
	
	@Override
	public void trace(String format, Object... arguments) {
		trace(null, format, arguments);
	}

	@Override
	public void trace(Throwable t, String format, Object... arguments) {
		trace(FQCN, t, format, arguments);
	}
	
	@Override
	public void debug(Throwable t) {
		debug(t, ExceptionUtil.getSimpleMessage(t));
	}
	
	@Override
	public void debug(String format, Object... arguments) {
		if(null != arguments && 1 == arguments.length && arguments[0] instanceof Throwable) {
			// 兼容Slf4j中的xxx(String message, Throwable e)
			debug((Throwable)arguments[0], format);
		} else {
			debug(null, format, arguments);
		}
	}

	@Override
	public void debug(Throwable t, String format, Object... arguments) {
		debug(FQCN, t, format, arguments);
	}
	
	@Override
	public void info(Throwable t) {
		info(t, ExceptionUtil.getSimpleMessage(t));
	}
	
	@Override
	public void info(String format, Object... arguments) {
		if(null != arguments && 1 == arguments.length && arguments[0] instanceof Throwable) {
			// 兼容Slf4j中的xxx(String message, Throwable e)
			info((Throwable)arguments[0], format);
		} else {
			info(null, format, arguments);
		}
	}

	@Override
	public void info(Throwable t, String format, Object... arguments) {
		info(FQCN, t, format, arguments);
	}
	
	@Override
	public void warn(Throwable t) {
		warn(t, ExceptionUtil.getSimpleMessage(t));
	}
	
	@Override
	public void warn(String format, Object... arguments) {
		if(null != arguments && 1 == arguments.length && arguments[0] instanceof Throwable) {
			// 兼容Slf4j中的xxx(String message, Throwable e)
			warn((Throwable)arguments[0], format);
		} else {
			warn(null, format, arguments);
		}
	}

	@Override
	public void warn(Throwable t, String format, Object... arguments) {
		warn(FQCN, t, format, arguments);
	}
	
	@Override
	public void error(Throwable t) {
		this.error(t, ExceptionUtil.getSimpleMessage(t));
	}
	
	@Override
	public void error(String format, Object... arguments) {
		if(null != arguments && 1 == arguments.length && arguments[0] instanceof Throwable) {
			// 兼容Slf4j中的xxx(String message, Throwable e)
			error((Throwable)arguments[0], format);
		} else {
			error(null, format, arguments);
		}
	}

	@Override
	public void error(Throwable t, String format, Object... arguments) {
		error(FQCN, t, format, arguments);
	}
	
	@Override
	public void log(Level level, String format, Object... arguments) {
		if(null != arguments && 1 == arguments.length && arguments[0] instanceof Throwable) {
			// 兼容Slf4j中的xxx(String message, Throwable e)
			log(level, (Throwable)arguments[0], format);
		} else {
			log(level, null, format, arguments);
		}
	}
	
	@Override
	public void log(Level level, Throwable t, String format, Object... arguments) {
		this.log(FQCN, level, t, format, arguments);
	}
}
