package cn.hutool.core.lang.func;

import cn.hutool.core.exceptions.UtilException;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * SerBiConsumer
 *
 * @author VampireAchao
 */
@FunctionalInterface
public interface SerBiConsumer<T, U> extends BiConsumer<T, U>, Serializable {
	/**
	 * multi
	 *
	 * @param consumers lambda
	 * @param <T>       type
	 * @param <U>       return type
	 * @return lambda
	 */
	@SafeVarargs
	static <T, U> SerBiConsumer<T, U> multi(final SerBiConsumer<T, U>... consumers) {
		return Stream.of(consumers).reduce(SerBiConsumer::andThen).orElseGet(() -> (o, q) -> {});
	}

	/**
	 * Performs this operation on the given arguments.
	 *
	 * @param t the first input argument
	 * @param u the second input argument
	 * @throws Exception wrapped checked exceptions for easy using
	 */
	void accepting(T t, U u) throws Exception;

	/**
	 * Performs this operation on the given arguments.
	 *
	 * @param t the first input argument
	 * @param u the second input argument
	 */
	@Override
	default void accept(final T t, final U u) {
		try {
			accepting(t, u);
		} catch (final Exception e) {
			throw new UtilException(e);
		}
	}

	/**
	 * Returns a composed {@code SerBiCons} that performs, in sequence, this
	 * operation followed by the {@code after} operation. If performing either
	 * operation throws an exception, it is relayed to the caller of the
	 * composed operation.  If performing this operation throws an exception,
	 * the {@code after} operation will not be performed.
	 *
	 * @param after the operation to perform after this operation
	 * @return a composed {@code SerBiCons} that performs in sequence this
	 * operation followed by the {@code after} operation
	 * @throws NullPointerException if {@code after} is null
	 */
	default SerBiConsumer<T, U> andThen(final SerBiConsumer<? super T, ? super U> after) {
		Objects.requireNonNull(after);
		return (l, r) -> {
			accepting(l, r);
			after.accepting(l, r);
		};
	}

	/**
	 * 什么也不做，用于一些需要传入lambda的方法占位使用
	 *
	 * @param <T> 参数1类型
	 * @param <U> 参数2类型
	 * @return 什么也不做
	 */
	static <T, U> SerBiConsumer<T, U> nothing() {
		return (l, r) -> {};
	}
}
