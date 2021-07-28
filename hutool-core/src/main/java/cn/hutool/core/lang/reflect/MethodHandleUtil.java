package cn.hutool.core.lang.reflect;

import cn.hutool.core.exceptions.UtilException;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

/**
 * 方法句柄{@link MethodHandle}封装工具类<br>
 * 参考：
 * <ul>
 *     <li>https://stackoverflow.com/questions/22614746/how-do-i-invoke-java-8-default-methods-reflectively</li>
 * </ul>
 *
 * @author looly
 * @since 5.7.7
 */
public class MethodHandleUtil {

	/**
	 * jdk8中如果直接调用{@link MethodHandles#lookup()}获取到的{@link MethodHandles.Lookup}在调用findSpecial和unreflectSpecial
	 * 时会出现权限不够问题，抛出"no private access for invokespecial"异常，因此针对JDK8及JDK9+分别封装lookup方法。
	 *
	 * @param callerClass 被调用的类或接口
	 * @return {@link MethodHandles.Lookup}
	 */
	public static MethodHandles.Lookup lookup(Class<?> callerClass) {
		return LookupFactory.lookup(callerClass);
	}

	/**
	 * 执行Interface中的default方法<br>
	 *
	 * <pre class="code">
	 *     interface Duck {
	 *         default String quack() {
	 *             return "Quack";
	 *         }
	 *     }
	 *
	 *     Duck duck = (Duck) Proxy.newProxyInstance(
	 *         ClassLoaderUtil.getClassLoader(),
	 *         new Class[] { Duck.class },
	 *         MethodHandleUtil::invokeDefault);
	 * </pre>
	 *
	 * @param o      接口的子对象或代理对象
	 * @param method 方法
	 * @param args   参数
	 * @return 结果
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invoke(Object o, Method method, Object... args) {
		final Class<?> declaringClass = method.getDeclaringClass();
		try {
			return (T) lookup(declaringClass)
					.unreflectSpecial(method, declaringClass)
					.bindTo(o)
					.invokeWithArguments(args);
		} catch (Throwable e) {
			throw new UtilException(e);
		}
	}
}
