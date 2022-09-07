package cn.hutool.core.lang.func;

import cn.hutool.core.exceptions.UtilException;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * 可序列化的UnaryOperator
 *
 * @author VampireAchao
 * @see UnaryOperator
 */
@FunctionalInterface
public interface SerUnaryOperator<T> extends UnaryOperator<T>, Serializable {

	/**
	 * Applies this function to the given argument.
	 *
	 * @param t the function argument
	 * @return the function result
	 * @throws Exception wrappered checked exceptions
	 */
	@SuppressWarnings("all")
	T applying(T t) throws Exception;

	/**
	 * Applies this function to the given argument.
	 *
	 * @param t the function argument
	 * @return the function result
	 */
	@Override
	default T apply(T t) {
		try {
			return applying(t);
		} catch (Exception e) {
			throw new UtilException(e);
		}
	}

	/**
	 * Returns a unary operator that always returns its input argument.
	 *
	 * @param <T> the type of the input and output of the operator
	 * @return a unary operator that always returns its input argument
	 */
	static <T> SerUnaryOperator<T> identity() {
		return t -> t;
	}


	/**
	 * casting identity
	 *
	 * @param function source function
	 * @param <T>      param type
	 * @param <R>      result type
	 * @param <F>      lambda type
	 * @return identity after casting
	 */
	@SuppressWarnings("unchecked")
	static <T, R, F extends Function<T, R>> SerUnaryOperator<T> casting(F function) {
		return t -> (T) function.apply(t);
	}

}
