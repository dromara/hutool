package cn.hutool.core.lang.caller;

import java.io.Serializable;

import cn.hutool.core.util.ArrayUtil;

/**
 * {@link SecurityManager} 方式获取调用者
 *
 * @author Looly
 */
public class SecurityManagerCaller extends SecurityManager implements Caller, Serializable {
	private static final long serialVersionUID = 1L;

	private static final int OFFSET = 1;

	@Override
	public Class<?> getCaller() {
		final Class<?>[] context = getClassContext();
		if (null != context && (OFFSET + 1) < context.length) {
			return context[OFFSET + 1];
		}
		return null;
	}

	@Override
	public Class<?> getCallerCaller() {
		final Class<?>[] context = getClassContext();
		if (null != context && (OFFSET + 2) < context.length) {
			return context[OFFSET + 2];
		}
		return null;
	}

	@Override
	public Class<?> getCaller(int depth) {
		final Class<?>[] context = getClassContext();
		if (null != context && (OFFSET + depth) < context.length) {
			return context[OFFSET + depth];
		}
		return null;
	}

	@Override
	public boolean isCalledBy(Class<?> clazz) {
		final Class<?>[] classes = getClassContext();
		if(ArrayUtil.isNotEmpty(classes)) {
			for (Class<?> contextClass : classes) {
				if (contextClass.equals(clazz)) {
					return true;
				}
			}
		}
		return false;
	}
}
