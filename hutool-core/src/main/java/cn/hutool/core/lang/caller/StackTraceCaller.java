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
		final String className = Thread.currentThread().getStackTrace()[OFFSET + 1].getClassName();
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new UtilException(e, "[{}] not found!", className);
		}
	}

	@Override
	public Class<?> getCallerCaller() {
		final String className = Thread.currentThread().getStackTrace()[OFFSET + 2].getClassName();
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new UtilException(e, "[{}] not found!", className);
		}
	}

	@Override
	public Class<?> getCaller(int depth) {
		final String className = Thread.currentThread().getStackTrace()[OFFSET + depth].getClassName();
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new UtilException(e, "[{}] not found!", className);
		}
	}

	@Override
	public boolean isCalledBy(Class<?> clazz) {
		for (final StackTraceElement element : Thread.currentThread().getStackTrace()) {
			if (element.getClassName().equals(clazz.getName())) {
				return true;
			}
		}
		return false;
	}
}
