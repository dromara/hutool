package cn.hutool.core.lang.caller;

/**
 * {@link SecurityManager} 方式获取调用者
 * 
 * @author Looly
 */
public class SecurityManagerCaller extends SecurityManager implements Caller {
	
	private static final int OFFSET = 1;

	@Override
	public Class<?> getCaller() {
		return getClassContext()[OFFSET + 1];
	}

	@Override
	public Class<?> getCallerCaller() {
		return getClassContext()[OFFSET + 2];
	}

	@Override
	public Class<?> getCaller(int depth) {
		final Class<?>[] context = getClassContext();
		if ((OFFSET + depth) < context.length) {
			return context[OFFSET + depth];
		}
		return null;
	}

	@Override
	public boolean isCalledBy(Class<?> clazz) {
		final Class<?>[] classes = getClassContext();
		for (Class<?> contextClass : classes) {
			if (contextClass.equals(clazz)) {
				return true;
			}
		}
		return false;
	}
}
