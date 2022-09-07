package cn.hutool.core.lang.func;

import cn.hutool.core.exceptions.UtilException;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 可序列化的Function
 *
 * @author VampireAchao
 * @see Function
 */
@FunctionalInterface
public interface SerFunction<T, R> extends Function<T, R>, Serializable {

	/**
	 * Applies this function to the given argument.
	 *
	 * @param t the function argument
	 * @return the function result
	 * @throws Exception wrappered checked exceptions
	 */
	@SuppressWarnings("all")
	R applying(T t) throws Exception;

	/**
	 * Applies this function to the given argument.
	 *
	 * @param t the function argument
	 * @return the function result
	 */
	@Override
	default R apply(T t) {
		try {
			return applying(t);
		} catch (Exception e) {
			throw new UtilException(e);
		}
	}

	/**
	 * Returns a function that always returns its input argument.
	 *
	 * @param <T> the type of the input and output objects to the function
	 * @return a function that always returns its input argument
	 */
	static <T> SerFunction<T, T> identity() {
		return t -> t;
	}

	/**
	 * casting identity
	 *
	 * @param <T> param type
	 * @param <R> result type
	 * @return identity after casting
	 */
	@SuppressWarnings("unchecked")
	static <T, R> Function<T, R> castingIdentity() {
		return t -> (R) t;
	}
}
