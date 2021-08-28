package cn.hutool.core.lang.reflect;

import cn.hutool.core.exceptions.UtilException;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * {@link MethodHandles.Lookup}工厂，用于创建{@link MethodHandles.Lookup}对象<br>
 * jdk8中如果直接调用{@link MethodHandles#lookup()}获取到的{@link MethodHandles.Lookup}在调用findSpecial和unreflectSpecial
 * 时会出现权限不够问题，抛出"no private access for invokespecial"异常，因此针对JDK8及JDK9+分别封装lookup方法。
 *
 * 参考：
 * <ul>
 *     <li>https://blog.csdn.net/u013202238/article/details/108687086</li>
 * </ul>
 *
 * @author looly
 * @since 5.7.7
 */
public class LookupFactory {

	private static final int ALLOWED_MODES = MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED
			| MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC;

	private static Constructor<MethodHandles.Lookup> java8LookupConstructor;
	private static Method privateLookupInMethod;

	static {
		//先查询jdk9 开始提供的java.lang.invoke.MethodHandles.privateLookupIn方法,
		//如果没有说明是jdk8的版本.(不考虑jdk8以下版本)
		try {
			//noinspection JavaReflectionMemberAccess
			privateLookupInMethod = MethodHandles.class.getMethod("privateLookupIn", Class.class, MethodHandles.Lookup.class);
		} catch (NoSuchMethodException ignore) {
			//ignore
		}

		//jdk8
		//这种方式其实也适用于jdk9及以上的版本,但是上面优先,可以避免 jdk9 反射警告
		if (privateLookupInMethod == null) {
			try {
				java8LookupConstructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
				java8LookupConstructor.setAccessible(true);
			} catch (NoSuchMethodException e) {
				//可能是jdk8 以下版本
				throw new IllegalStateException(
						"There is neither 'privateLookupIn(Class, Lookup)' nor 'Lookup(Class, int)' method in java.lang.invoke.MethodHandles.", e);
			}
		}
	}

	/**
	 * jdk8中如果直接调用{@link MethodHandles#lookup()}获取到的{@link MethodHandles.Lookup}在调用findSpecial和unreflectSpecial
	 * 时会出现权限不够问题，抛出"no private access for invokespecial"异常，因此针对JDK8及JDK9+分别封装lookup方法。
	 *
	 * @param callerClass 被调用的类或接口
	 * @return {@link MethodHandles.Lookup}
	 */
	public static MethodHandles.Lookup lookup(Class<?> callerClass) {
		//使用反射,因为当前jdk可能不是java9或以上版本
		if (privateLookupInMethod != null) {
			try {
				return (MethodHandles.Lookup) privateLookupInMethod.invoke(MethodHandles.class, callerClass, MethodHandles.lookup());
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new UtilException(e);
			}
		}
		//jdk 8
		try {
			return java8LookupConstructor.newInstance(callerClass, ALLOWED_MODES);
		} catch (Exception e) {
			throw new IllegalStateException("no 'Lookup(Class, int)' method in java.lang.invoke.MethodHandles.", e);
		}
	}
}
