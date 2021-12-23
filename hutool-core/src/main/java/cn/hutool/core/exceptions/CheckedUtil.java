package cn.hutool.core.exceptions;

import java.util.Objects;
import java.util.function.*;

/**
 * 方便的执行会抛出受检查类型异常的方法调用或者代码段
 * <p>
 * 该工具通过函数式的方式将那些需要抛出受检查异常的表达式或者代码段转化成一个标准的java8 functional 对象
 * </p>
 *
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
 *       Map<String, String> describedObject = CheckedUtil.uncheck(BeanUtils::describe).apply(new Object());
 *       // use describedObject ...
 *
 *       CheckedUtil.uncheck 方法接受任意可以转化成标准java8 函数式接口的 Lambda 表达式。返回对应的函数式对象。
 *       上述代码可以理解为：
 *        Function<Object, Map<String, String>> aFunc = CheckedUtil.uncheck(BeanUtils::describe);
 *        Map<String, String> describedObject = aFunc.apply(传入参数);
 *        该aFunc对象代表的就是BeanUtils::describe这个表达式，且在内部转化了检查类型异常，不需要代码里面显示处理。
 *
 *
 * </pre>
 *
 * @author conder
 */
public class CheckedUtil {

	/**
	 * 接收一个可以转化成 java.util.function.Function的Lambda表达式，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression Lambda表达式
	 * @param <T>        运行时传入的参数类型
	 * @param <R>        最终返回的数据类型
	 * @return java.util.function.Function
	 */
	public static <T, R> Function<T, R> uncheck(MorFunction<T, R> expression) {
		return uncheck(expression, new RuntimeException());
	}

	/**
	 * 接收一个可以转化成 java.util.function.BiFunction的Lambda表达式当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression Lambda表达式
	 * @param <T1>       运行时传入的参数类型一
	 * @param <T2>       运行时传入的参数类型二
	 * @param <R>        最终返回的数据类型
	 * @return java.util.function.BiFunction
	 */
	public static <T1, T2, R> BiFunction<T1, T2, R> uncheck(BiFunction<T1, T2, R> expression) {
		return uncheck(expression, new RuntimeException());
	}

	/**
	 * 接收一个可以转化成 java.util.function.Consumer的Lambda表达式，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression Lambda表达式
	 * @param <T>        运行时传入的参数类型
	 * @return java.util.function.Consumer
	 */
	public static <T> Consumer<T> uncheck(MorConsumer<T> expression) {
		return uncheck(expression, new RuntimeException());
	}

	/**
	 * 接收一个可以转化成 java.util.function.BiConsumer的Lambda表达式，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression Lambda表达式
	 * @param <T>        运行时传入的参数类型一
	 * @param <U>        运行时传入的参数类型二
	 * @return java.util.function.BiConsumer
	 */
	public static <T, U> BiConsumer<T, U> uncheck(MorBiConsumer<T, U> expression) {
		return uncheck(expression, new RuntimeException());
	}

	/**
	 * 接收一个可以转化成 java.util.function.Runnable的Lambda表达式，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression Lambda表达式
	 * @return java.util.function.Runnable
	 */
	public static Runnable uncheck(MorRunnable expression) {
		return uncheck(expression, new RuntimeException());
	}

	/**
	 * 接收一个可以转化成 java.util.function.Supplier的Lambda表达式，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression Lambda表达式
	 * @param <R>        运行时传入的参数类型
	 * @return java.util.function.Supplier
	 */
	public static <R> Supplier<R> uncheck(MorSupplier<R> expression) {
		return uncheck(expression, new RuntimeException());
	}


	/**
	 * 接收一个可以转化成 java.util.function.Function的Lambda表达式，和一个RuntimeException，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression Lambda表达式
	 * @param rte        期望抛出的运行时异常
	 * @param <T>        运行时传入的参数类型
	 * @param <R>        最终返回的数据类型
	 * @return java.util.function.Function
	 */
	public static <T, R> Function<T, R> uncheck(MorFunction<T, R> expression, RuntimeException rte) {
		Objects.requireNonNull(expression, "expression can not be null");
		return t -> {
			try {
				return expression.apply(t);
			} catch (Throwable throwable) {
				if (rte == null) {
					throw new RuntimeException(throwable);
				} else {
					rte.initCause(throwable);
					throw rte;
				}
			}
		};
	}

	/**
	 * 接收一个可以转化成 java.util.function.BiFunction的Lambda表达式，和一个RuntimeException，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression Lambda表达式
	 * @param rte        期望抛出的运行时异常
	 * @param <T1>       运行时传入的参数类型一
	 * @param <T2>       运行时传入的参数类型二
	 * @param <R>        最终返回的数据类型
	 * @return java.util.function.BiFunction
	 */
	public static <T1, T2, R> BiFunction<T1, T2, R> uncheck(BiFunction<T1, T2, R> expression, RuntimeException rte) {
		Objects.requireNonNull(expression, "expression can not be null");
		return (t1, t2) -> {
			try {
				return expression.apply(t1, t2);
			} catch (Throwable throwable) {
				if (rte == null) {
					throw new RuntimeException(throwable);
				} else {
					rte.initCause(throwable);
					throw rte;
				}
			}
		};
	}

	/**
	 * 接收一个可以转化成 java.util.function.Consumer的Lambda表达式，和一个RuntimeException，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression Lambda表达式
	 * @param rte        期望抛出的运行时异常
	 * @param <T>        运行时传入的参数类型
	 * @return java.util.function.Consumer
	 */
	public static <T> Consumer<T> uncheck(MorConsumer<T> expression, RuntimeException rte) {
		Objects.requireNonNull(expression, "expression can not be null");
		return t -> {
			try {
				expression.apply(t);
			} catch (Throwable throwable) {
				if (rte == null) {
					throw new RuntimeException(throwable);
				} else {
					rte.initCause(throwable);
					throw rte;
				}
			}
		};
	}

	/**
	 * 接收一个可以转化成 java.util.function.BiConsumer的Lambda表达式，和一个RuntimeException，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression Lambda表达式
	 * @param rte        期望抛出的运行时异常
	 * @param <T>        运行时传入的参数类型一
	 * @param <U>        运行时传入的参数类型二
	 * @return java.util.function.BiConsumer
	 */
	public static <T, U> BiConsumer<T, U> uncheck(MorBiConsumer<T, U> expression, RuntimeException rte) {
		Objects.requireNonNull(expression, "expression can not be null");
		return (t, u) -> {
			try {
				expression.apply(t, u);
			} catch (Throwable throwable) {
				if (rte == null) {
					throw new RuntimeException(throwable);
				} else {
					rte.initCause(throwable);
					throw rte;
				}
			}
		};
	}

	/**
	 * 接收一个可以转化成 java.util.function.Runnable的Lambda表达式，和一个RuntimeException，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression Lambda表达式
	 * @param rte        期望抛出的运行时异常
	 * @return java.util.function.Runnable
	 */
	public static Runnable uncheck(MorRunnable expression, RuntimeException rte) {
		Objects.requireNonNull(expression, "expression can not be null");
		return () -> {
			try {
				expression.apply();
			} catch (Throwable throwable) {
				if (rte == null) {
					throw new RuntimeException(throwable);
				} else {
					rte.initCause(throwable);
					throw rte;
				}
			}
		};
	}

	/**
	 * 接收一个可以转化成 java.util.function.Supplier的Lambda表达式，和一个RuntimeException，当执行表达式抛出任何异常的时候，都会转化成运行时异常
	 * 如此一来，代码中就不用显示的try-catch转化成运行时异常
	 *
	 * @param expression Lambda表达式
	 * @param rte        期望抛出的运行时异常
	 * @param <R>        运行时传入的参数类型
	 * @return java.util.function.Supplier
	 */
	public static <R> Supplier<R> uncheck(MorSupplier<R> expression, RuntimeException rte) {
		Objects.requireNonNull(expression, "expression can not be null");
		return () -> {
			try {
				return expression.apply();
			} catch (Throwable throwable) {
				if (rte == null) {
					throw new RuntimeException(throwable);
				} else {
					rte.initCause(throwable);
					throw rte;
				}
			}
		};
	}


	/**
	 * 对应java8 java.util.function.Function ,能够接受受检查异常的转化
	 *
	 * @param <T>
	 * @param <R>
	 */
	public interface MorFunction<T, R> {
		R apply(T t) throws Throwable;
	}

	/**
	 * 对应java8 java.util.function.BiFunction ,能够接受受检查异常的转化
	 *
	 * @param <T1>
	 * @param <T2>
	 * @param <R>
	 */
	public interface MorBiFunction<T1, T2, R> {
		R apply(T1 t1, T2 t2) throws Throwable;
	}

	/**
	 * 对应java8 java.util.function.Consumer ,能够接受受检查异常的转化
	 *
	 * @param <T>
	 */
	public interface MorConsumer<T> {
		void apply(T t) throws Throwable;
	}

	/**
	 * 对应java8 java.util.function.BiConsumer ,能够接受受检查异常的转化
	 *
	 * @param <T>
	 * @param <U>
	 */
	public interface MorBiConsumer<T, U> {
		void apply(T t, U u) throws Throwable;
	}

	/**
	 * 对应java8 java.util.function.Runnable ,能够接受受检查异常的转化
	 */
	public interface MorRunnable {
		void apply() throws Throwable;
	}

	/**
	 * 对应java8 java.util.function.Supplier ,能够接受受检查异常的转化
	 *
	 * @param <R>
	 */
	public interface MorSupplier<R> {
		R apply() throws Throwable;
	}
}
