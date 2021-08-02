package cn.hutool.core.lang.reflect;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
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
	 * 查找指定方法的方法句柄<br>
	 * 此方法只会查找：
	 * <ul>
	 *     <li>当前类的方法（包括构造方法和private方法）</li>
	 *     <li>父类的方法（包括构造方法和private方法）</li>
	 *     <li>当前类的static方法</li>
	 * </ul>
	 *
	 * @param callerClass 方法所在类或接口
	 * @param name        方法名称，{@code null}或者空则查找构造方法
	 * @param type        返回类型和参数类型
	 * @return 方法句柄 {@link MethodHandle}，{@code null}表示未找到方法
	 */
	public static MethodHandle findMethod(Class<?> callerClass, String name, MethodType type) {
		if (StrUtil.isBlank(name)) {
			return findConstructor(callerClass, type);
		}

		MethodHandle handle = null;
		final MethodHandles.Lookup lookup = lookup(callerClass);
		try {
			handle = lookup.findVirtual(callerClass, name, type);
		} catch (IllegalAccessException | NoSuchMethodException ignore) {
			//ignore
		}

		// static方法
		if (null == handle) {
			try {
				handle = lookup.findStatic(callerClass, name, type);
			} catch (IllegalAccessException | NoSuchMethodException ignore) {
				//ignore
			}
		}

		// 特殊方法，包括构造方法、私有方法等
		if (null == handle) {
			try {
				handle = lookup.findSpecial(callerClass, name, type, callerClass);
			} catch (NoSuchMethodException ignore) {
				//ignore
			} catch (IllegalAccessException e) {
				throw new UtilException(e);
			}
		}

		return handle;
	}

	/**
	 * 查找指定的构造方法
	 *
	 * @param callerClass 类
	 * @param args        参数
	 * @return 构造方法句柄
	 */
	public static MethodHandle findConstructor(Class<?> callerClass, Class<?>... args) {
		return findConstructor(callerClass, MethodType.methodType(void.class, args));
	}

	/**
	 * 查找指定的构造方法
	 *
	 * @param callerClass 类
	 * @param type        参数类型，此处返回类型应为void.class
	 * @return 构造方法句柄
	 */
	public static MethodHandle findConstructor(Class<?> callerClass, MethodType type) {
		final MethodHandles.Lookup lookup = lookup(callerClass);
		try {
			return lookup.findConstructor(callerClass, type);
		} catch (NoSuchMethodException e) {
			return null;
		} catch (IllegalAccessException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 执行接口或对象中的方法<br>
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
	 * @param <T>        返回结果类型
	 * @param obj        接口的子对象或代理对象
	 * @param methodName 方法名称
	 * @param args       参数
	 * @return 结果
	 */
	public static <T> T invokeSpecial(Object obj, String methodName, Object... args) {
		Assert.notNull(obj, "Object to get method must be not null!");
		Assert.notBlank(methodName, "Method name must be not blank!");

		final Method method = ReflectUtil.getMethodOfObj(obj, methodName, args);
		if (null == method) {
			throw new UtilException("No such method: [{}] from [{}]", methodName, obj.getClass());
		}
		return invokeSpecial(obj, method, args);
	}

	/**
	 * 执行接口或对象中的方法
	 *
	 * @param <T>    返回结果类型
	 * @param obj    接口的子对象或代理对象
	 * @param method 方法
	 * @param args   参数
	 * @return 结果
	 */
	public static <T> T invoke(Object obj, Method method, Object... args) {
		return invoke(false, obj, method, args);
	}

	/**
	 * 执行接口或对象中的方法<br>
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
	 *         MethodHandleUtil::invoke);
	 * </pre>
	 *
	 * @param <T>    返回结果类型
	 * @param obj    接口的子对象或代理对象
	 * @param method 方法
	 * @param args   参数
	 * @return 结果
	 */
	public static <T> T invokeSpecial(Object obj, Method method, Object... args) {
		return invoke(true, obj, method, args);
	}

	/**
	 * 执行接口或对象中的方法<br>
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
	 *         MethodHandleUtil::invoke);
	 * </pre>
	 *
	 * @param <T>       返回结果类型
	 * @param isSpecial 是否为特殊方法（private、static等）
	 * @param obj       接口的子对象或代理对象
	 * @param method    方法
	 * @param args      参数
	 * @return 结果
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invoke(boolean isSpecial, Object obj, Method method, Object... args) {
		Assert.notNull(method, "Method must be not null!");
		final Class<?> declaringClass = method.getDeclaringClass();
		final MethodHandles.Lookup lookup = lookup(declaringClass);
		try {
			MethodHandle handle = isSpecial ? lookup.unreflectSpecial(method, declaringClass)
					: lookup.unreflect(method);
			if (null != obj) {
				handle = handle.bindTo(obj);
			}
			return (T) handle.invokeWithArguments(args);
		} catch (Throwable e) {
			throw new UtilException(e);
		}
	}
}
