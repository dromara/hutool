package cn.hutool.core.exceptions;

import cn.hutool.core.lang.func.*;

import java.util.Objects;

/**
 * 方便的执行会抛出受检查类型异常的方法调用或者代码段
 * <p>
 * 该工具通过函数式的方式将那些需要抛出受检查异常的表达式或者代码段转化成一个 cn.hutool.core.lang.func.Func* 对象
 * </p>
 * <p>
 * {@code
 * <pre>
 *      //代码中如果遇到一个方法调用声明了受检查异常那么我们的代码就必须这样写
 *         Map<String, String> describedObject = null;
 *         try {
 *             describe = BeanUtils.describe(new Object());
 *         } catch (IllegalAccessException e) {
 *             throw new RuntimeException(e);
 *         } catch (InvocationTargetException e) {
 *             throw new RuntimeException(e);
 *         } catch (NoSuchMethodException e) {
 *             throw new RuntimeException(e);
 *         }
 *         // use describedObject ...
 *
 *       //上面的代码增加了异常块使得代码不那么流畅，现在可以这样写：
 *       Map<String, String> describedObject = CheckedUtil.uncheck(BeanUtils::describe).call(new Object());
 *       // use describedObject ...
 *
 *       CheckedUtil.uncheck 方法接受任意可以转化成 cn.hutool.core.lang.func.Func* 函数式接口的 Lambda 表达式。返回对应的函数式对象。
 *       上述代码可以理解为：
 *        Func0<Object, Map<String, String>> aFunc = CheckedUtil.uncheck(BeanUtils::describe);
 *        Map<String, String> describedObject = aFunc.call(传入参数);
 *        该aFunc对象代表的就是BeanUtils::describe这个表达式，且在内部转化了检查类型异常，不需要代码里面显示处理。
 *
 *
 * </pre>
 * }
 *
 * @author conder
 * @since 5.7.19
 */
public class CheckedUtil {

	/**
	 * 接收一个可以转化成 cn.hutool.core.lang.func.Func 的Lambda表达式，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression Lambda表达式
	 * @param <P>        运行时传入的参数类型
	 * @param <R>        最终返回的数据类型
	 * @return {@link FuncRt}
	 */
	public static <P, R> FuncRt<P, R> uncheck(Func<P, R> expression) {
		return uncheck(expression, new RuntimeException());
	}

	/**
	 * 接收一个可以转化成 cn.hutool.core.lang.func.Func0 的Lambda表达式，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression 运行时传入的参数类型
	 * @param <R>        最终返回的数据类型
	 * @return {@link Func0Rt}
	 */
	public static <R> Func0Rt<R> uncheck(Func0<R> expression) {
		return uncheck(expression, new RuntimeException());
	}

	/**
	 * 接收一个可以转化成 cn.hutool.core.lang.func.Func1 的Lambda表达式，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression 运行时传入的参数类型
	 * @param <P>        运行时传入的参数类型
	 * @param <R>        最终返回的数据类型
	 * @return {@link Func1Rt}
	 */
	public static <P, R> Func1Rt<P, R> uncheck(Func1<P, R> expression) {
		return uncheck(expression, new RuntimeException());
	}


	/**
	 * 接收一个可以转化成 cn.hutool.core.lang.func.VoidFunc 的Lambda表达式，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression 运行时传入的参数类型
	 * @param <P>        运行时传入的参数类型
	 * @return {@link VoidFuncRt}
	 */
	public static <P> VoidFuncRt<P> uncheck(VoidFunc<P> expression) {
		return uncheck(expression, new RuntimeException());
	}

	/**
	 * 接收一个可以转化成 cn.hutool.core.lang.func.VoidFunc0 的Lambda表达式，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression 运行时传入的参数类型
	 * @return {@link VoidFunc0Rt}
	 */
	public static VoidFunc0Rt uncheck(VoidFunc0 expression) {
		return uncheck(expression, new RuntimeException());
	}

	/**
	 * 接收一个可以转化成 cn.hutool.core.lang.func.VoidFunc1 的Lambda表达式，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression 运行时传入的参数类型
	 * @param <P>        运行时传入的参数类型
	 * @return {@link VoidFunc1Rt}
	 */
	public static <P> VoidFunc1Rt<P> uncheck(VoidFunc1<P> expression) {
		return uncheck(expression, new RuntimeException());
	}


	/**
	 * 接收一个可以转化成 cn.hutool.core.lang.func.Func的Lambda表达式，和一个RuntimeException，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression Lambda表达式
	 * @param rte        期望抛出的运行时异常
	 * @param <P>        运行时传入的参数类型
	 * @param <R>        最终返回的数据类型
	 * @return {@link FuncRt}
	 */
	public static <P, R> FuncRt<P, R> uncheck(Func<P, R> expression, RuntimeException rte) {
		Objects.requireNonNull(expression, "expression can not be null");
		return t -> {
			try {
				return expression.call(t);
			} catch (Exception e) {
				if (rte == null) {
					throw new RuntimeException(e);
				} else {
					rte.initCause(e);
					throw rte;
				}
			}
		};
	}

	/**
	 * 接收一个可以转化成 cn.hutool.core.lang.func.Func0的Lambda表达式，和一个RuntimeException，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression Lambda表达式
	 * @param rte        期望抛出的运行时异常
	 * @param <R>        最终返回的数据类型
	 * @return {@link Func0Rt}
	 */
	public static <R> Func0Rt<R> uncheck(Func0<R> expression, RuntimeException rte) {
		Objects.requireNonNull(expression, "expression can not be null");
		return () -> {
			try {
				return expression.call();
			} catch (Exception e) {
				if (rte == null) {
					throw new RuntimeException(e);
				} else {
					rte.initCause(e);
					throw rte;
				}
			}
		};
	}

	/**
	 * 接收一个可以转化成 cn.hutool.core.lang.func.Func1的Lambda表达式，和一个RuntimeException，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression Lambda表达式
	 * @param rte        期望抛出的运行时异常
	 * @param <P>        运行时传入的参数类型
	 * @param <R>        最终返回的数据类型
	 * @return {@link Func1Rt}
	 */
	public static <P, R> Func1Rt<P, R> uncheck(Func1<P, R> expression, RuntimeException rte) {
		Objects.requireNonNull(expression, "expression can not be null");
		return t -> {
			try {
				return expression.call(t);
			} catch (Exception e) {
				if (rte == null) {
					throw new RuntimeException(e);
				} else {
					rte.initCause(e);
					throw rte;
				}
			}
		};
	}

	/**
	 * 接收一个可以转化成 cn.hutool.core.lang.func.VoidFunc的Lambda表达式，和一个RuntimeException，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression Lambda表达式
	 * @param rte        期望抛出的运行时异常
	 * @param <P>        运行时传入的参数类型
	 * @return {@link VoidFuncRt}
	 */
	public static <P> VoidFuncRt<P> uncheck(VoidFunc<P> expression, RuntimeException rte) {
		Objects.requireNonNull(expression, "expression can not be null");
		return t -> {
			try {
				expression.call(t);
			} catch (Exception e) {
				if (rte == null) {
					throw new RuntimeException(e);
				} else {
					rte.initCause(e);
					throw rte;
				}
			}
		};
	}


	/**
	 * 接收一个可以转化成 cn.hutool.core.lang.func.VoidFunc0的Lambda表达式，和一个RuntimeException，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression Lambda表达式
	 * @param rte        期望抛出的运行时异常
	 * @return {@link VoidFunc0Rt}
	 */
	public static VoidFunc0Rt uncheck(VoidFunc0 expression, RuntimeException rte) {
		Objects.requireNonNull(expression, "expression can not be null");
		return () -> {
			try {
				expression.call();
			} catch (Exception e) {
				if (rte == null) {
					throw new RuntimeException(e);
				} else {
					rte.initCause(e);
					throw rte;
				}
			}
		};
	}


	/**
	 * 接收一个可以转化成 cn.hutool.core.lang.func.VoidFunc1的Lambda表达式，和一个RuntimeException，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression Lambda表达式
	 * @param rte        期望抛出的运行时异常
	 * @param <P>        运行时传入的参数类型
	 * @return {@link VoidFunc1Rt}
	 */
	public static <P> VoidFunc1Rt<P> uncheck(VoidFunc1<P> expression, RuntimeException rte) {
		Objects.requireNonNull(expression, "expression can not be null");
		return t -> {
			try {
				expression.call(t);
			} catch (Exception e) {
				if (rte == null) {
					throw new RuntimeException(e);
				} else {
					rte.initCause(e);
					throw rte;
				}
			}
		};
	}

	public interface FuncRt<P, R> extends Func<P, R> {
		@SuppressWarnings("unchecked")
		R call(P... parameters) throws RuntimeException;
	}

	public interface Func0Rt<R> extends Func0<R> {
		R call() throws RuntimeException;
	}

	public interface Func1Rt<P, R> extends Func1<P, R> {
		R call(P parameter) throws RuntimeException;
	}

	public interface VoidFuncRt<P> extends VoidFunc<P> {
		@SuppressWarnings("unchecked")
		void call(P... parameters) throws RuntimeException;
	}

	public interface VoidFunc0Rt extends VoidFunc0 {
		void call() throws RuntimeException;
	}

	public interface VoidFunc1Rt<P> extends VoidFunc1<P> {
		void call(P parameter) throws RuntimeException;
	}


}
