package cn.hutool.core.lang.caller;

/**
 * 通过sun.reflect.Reflection 获取调用者
 * 
 * @author Looly
 *
 */
@SuppressWarnings({ "deprecation", "restriction" })
public class ReflectionCaller extends SecurityManagerCaller {
	private static final int OFFSET = 2;

	@Override
	public Class<?> getCaller() {
		return sun.reflect.Reflection.getCallerClass(OFFSET + 1);
	}

	@Override
	public Class<?> getCallerCaller() {
		return sun.reflect.Reflection.getCallerClass(OFFSET + 2);
	}

	@Override
	public Class<?> getCaller(int depth) {
		return sun.reflect.Reflection.getCallerClass(OFFSET + depth);
	}
}
