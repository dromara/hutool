package cn.hutool.core.lang.tree.lambda;

import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.LambdaUtil;

import java.lang.invoke.MethodType;
import java.util.List;

import static java.lang.invoke.MethodHandles.lookup;

/**
 * MethodHandleUtil
 *
 * @author adoph
 * @version 1.0
 * @since 2022/9/12
 */
public class MethodHandleUtil {

	/**
	 * setChildrenMethodType
	 */
	private static final MethodType setChildrenMethodType = MethodType.methodType(void.class, List.class);

	/**
	 * 根据get lambda方法获取set方法名
	 */
	protected static <T> String requireSetMethod(Func1<T, ?> func) {
		return LambdaUtil.getMethodName(func).replaceFirst("get", "set");
	}

	/**
	 * 通过MethodHandle调用方法
	 *
	 * @param obj        调用对象
	 * @param params     参数
	 * @param methodName 方法名
	 */
	protected static <E> void invokeByMethodHandle(E obj, List<E> params, String methodName) throws MethodHandleRuntimeException {
		try {
			invokeByMethodHandle(obj, params, methodName, setChildrenMethodType);
		} catch (Throwable e) {
			throw new MethodHandleRuntimeException("invoke by method handle exception", e);
		}
	}

	/**
	 * 通过MethodHandle调用方法
	 *
	 * @param obj        调用对象
	 * @param params     参数
	 * @param methodName 方法名
	 * @param methodType MethodType
	 */
	protected static <E> void invokeByMethodHandle(E obj, List<E> params, String methodName, MethodType methodType) throws Throwable {
		lookup().findVirtual(obj.getClass(), methodName, methodType).bindTo(obj).invokeExact(params);
	}

}
