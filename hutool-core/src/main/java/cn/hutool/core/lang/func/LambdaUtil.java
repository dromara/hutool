package cn.hutool.core.lang.func;

import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.util.ReflectUtil;

import java.beans.Introspector;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;

/**
 * Lambda相关工具类
 *
 * @author looly, Scen, zak
 * @since 5.6.3
 */
public class LambdaUtil {

	private static final SimpleCache<String, SerializedLambda> cache = new SimpleCache<>();

	/**
	 * 解析lambda表达式,加了缓存。
	 * 该缓存可能会在任意不定的时间被清除
	 *
	 * @param <T> Lambda类型
	 * @param func 需要解析的 lambda 对象（无参方法）
	 * @return 返回解析后的结果
	 */
	public static <T> SerializedLambda resolve(Func1<T, ?> func) {
		return _resolve(func);
	}

	/**
	 * 获取lambda表达式函数（方法）名称
	 *
	 * @param <T> Lambda类型
	 * @param func 函数（无参方法）
	 * @return 函数名称
	 */
	public static <T> String getMethodName(Func1<T, ?> func) {
		return resolve(func).getImplMethodName();
	}

	/**
	 * 当lambda表达式函数（方法）是一个get方法时，获取字段名称
	 *
	 * @param <T> Lambda类型
	 * @param func 函数（无参方法）
	 * @return 字段名称
	 */
	public static <T> String getFieldName(Func1<T, ?> func) {
		String methodName = getMethodName(func);
		if (methodName.startsWith("get")) {
			return Introspector.decapitalize(methodName.substring(3));
		}
		if (methodName.startsWith("is")) {
			return Introspector.decapitalize(methodName.substring(2));
		}
		throw new IllegalArgumentException("lambda表达式方法不是一个get方法");
	}

	/**
	 * 解析lambda表达式,加了缓存。
	 * 该缓存可能会在任意不定的时间被清除
	 *
	 * @param func 需要解析的 lambda 对象
	 * @return 返回解析后的结果
	 */
	private static <T> SerializedLambda _resolve(Serializable func) {
		return cache.get(func.getClass().getName(), () -> ReflectUtil.invoke(func, "writeReplace"));
	}


	/**
	 * 将受检异常通过泛型绕过编译检测
	 */
	public static <R, E extends Throwable> R raise(Throwable e) throws E {
		throw (E) e;
	}


	/**
	 * 将受检异常通过泛型绕过编译检测
	 *
	 * @param raiseWrapper 异常代码包装
	 * @param <T> 返回值类型
	 * @return 返回值
	 */
	public static <T> T raise(RaiseWrapper<T> raiseWrapper) {
		try {
			return raiseWrapper.get();
		} catch (Throwable throwable) {
			return raise(throwable);
		}
	}

	/**
	 * 将受检异常通过泛型绕过编译检测
	 *
	 * @param raiseWrapper 异常代码包装
	 */
	public static void raise(RaiseWrapperNoReturn raiseWrapper) {
		try {
			raiseWrapper.get();
		} catch (Throwable throwable) {
			raise(throwable);
		}
	}



	/**
	 * 异常代码包装，有返回值
	 */
	@FunctionalInterface
	public interface RaiseWrapper<T> {
		T get() throws Throwable;
	}

	/**
	 * 异常代码包装，没有返回值
	 */
	@FunctionalInterface
	public interface RaiseWrapperNoReturn {
		void get() throws Throwable;
	}
}
