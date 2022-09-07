package cn.hutool.core.lang.func;

import cn.hutool.core.exceptions.UtilException;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.BinaryOperator;

/**
 * SerBinaryOperator
 *
 * @author VampireAchao
 * @since 2022/6/8
 */
@FunctionalInterface
public interface SerBinaryOperator<T> extends BinaryOperator<T>, Serializable {

	/**
	 * Applies this function to the given arguments.
	 *
	 * @param t the first function argument
	 * @param u the second function argument
	 * @return the function result
	 * @throws Exception wrappered checked exceptions
	 */
	@SuppressWarnings("all")
	T applying(T t, T u) throws Exception;

	/**
	 * Applies this function to the given arguments.
	 *
	 * @param t the first function argument
	 * @param u the second function argument
	 * @return the function result
	 */
	@Override
	default T apply(T t, T u) {
		try {
			return this.applying(t, u);
		} catch (Exception e) {
			throw new UtilException(e);
		}
	}

	/**
	 * Returns a {@link SerBinaryOperator} which returns the lesser of two elements
	 * according to the specified {@code Comparator}.
	 *
	 * @param <T>        the type of the input arguments of the comparator
	 * @param comparator a {@code Comparator} for comparing the two values
	 * @return a {@code SerBiUnOp} which returns the lesser of its operands,
	 * according to the supplied {@code Comparator}
	 * @throws NullPointerException if the argument is null
	 */
	static <T> SerBinaryOperator<T> minBy(Comparator<? super T> comparator) {
		Objects.requireNonNull(comparator);
		return (a, b) -> comparator.compare(a, b) <= 0 ? a : b;
	}

	/**
	 * Returns a {@link SerBinaryOperator} which returns the greater of two elements
	 * according to the specified {@code Comparator}.
	 *
	 * @param <T>        the type of the input arguments of the comparator
	 * @param comparator a {@code Comparator} for comparing the two values
	 * @return a {@code SerBiUnOp} which returns the greater of its operands,
	 * according to the supplied {@code Comparator}
	 * @throws NullPointerException if the argument is null
	 */
	static <T> SerBinaryOperator<T> maxBy(Comparator<? super T> comparator) {
		Objects.requireNonNull(comparator);
		return (a, b) -> comparator.compare(a, b) >= 0 ? a : b;
	}

	/**
	 * just before
	 *
	 * @param <T> type
	 * @return before
	 */
	static <T> SerBinaryOperator<T> justBefore() {
		return (l, r) -> l;
	}

	/**
	 * just after
	 *
	 * @param <T> type
	 * @return after
	 */
	static <T> SerBinaryOperator<T> justAfter() {
		return (l, r) -> r;
	}
}

