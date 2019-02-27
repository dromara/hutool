package cn.hutool.core.lang.caller;

import cn.hutool.core.exceptions.UtilException;

/**
 * 通过StackTrace方式获取调用者。此方式效率最低，不推荐使用
 * 
 * @author Looly
 */
public class StackTraceCaller implements Caller {
	private static final int OFFSET = 2;

	@Override
	public Class<?> getCaller() {
		final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		if (null == stackTrace || (OFFSET + 1) >= stackTrace.length) {
			return null;
		}
		final String className = stackTrace[OFFSET + 1].getClassName();
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new UtilException(e, "[{}] not found!", className);
		}
	}

	@Override
	public Class<?> getCallerCaller() {
		final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		if (null == stackTrace || (OFFSET + 2) >= stackTrace.length) {
			return null;
		}
		final String className = stackTrace[OFFSET + 2].getClassName();
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new UtilException(e, "[{}] not found!", className);
		}
	}

	@Override
	public Class<?> getCaller(int depth) {
		final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		if (null == stackTrace || (OFFSET + depth) >= stackTrace.length) {
			return null;
		}
		final String className = stackTrace[OFFSET + depth].getClassName();
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new UtilException(e, "[{}] not found!", className);
		}
	}

	@Override
	public boolean isCalledBy(Class<?> clazz) {
		final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		if(null != stackTrace) {
			for (final StackTraceElement element : stackTrace) {
				if (element.getClassName().equals(clazz.getName())) {
					return true;
				}
			}
		}
		return false;
	}
}
