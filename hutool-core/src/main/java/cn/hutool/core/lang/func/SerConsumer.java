package cn.hutool.core.lang.func;

import cn.hutool.core.exceptions.UtilException;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 可序列化的Consumer
 *
 * @param <T> 参数类型
 * @author VampireAchao
 * @see Consumer
 */
@FunctionalInterface
public interface SerConsumer<T> extends Consumer<T>, Serializable {

	/**
	 * Performs this operation on the given argument.
	 *
	 * @param t the input argument
	 * @throws Exception wrapped checked exceptions
	 */
	void accepting(T t) throws Exception;

	/**
	 * Performs this operation on the given argument.
	 *
	 * @param t the input argument
	 */
	@Override
	default void accept(final T t) {
		try {
			accepting(t);
		} catch (final Exception e) {
			throw new UtilException(e);
		}
	}

	/**
	 * multi
	 *
	 * @param consumers lambda
	 * @param <T>       type
	 * @return lambda
	 */
	@SafeVarargs
	static <T> SerConsumer<T> multi(final SerConsumer<T>... consumers) {
		return Stream.of(consumers).reduce(SerConsumer::andThen).orElseGet(() -> o -> {
		});
	}

	/**
	 * Returns a composed {@code Consumer} that performs, in sequence, this
	 * operation followed by the {@code after} operation. If performing either
	 * operation throws an exception, it is relayed to the caller of the
	 * composed operation.  If performing this operation throws an exception,
	 * the {@code after} operation will not be performed.
	 *
	 * @param after the operation to perform after this operation
	 * @return a composed {@code Consumer} that performs in sequence this
	 * operation followed by the {@code after} operation
	 * @throws NullPointerException if {@code after} is null
	 */
	default SerConsumer<T> andThen(final SerConsumer<? super T> after) {
		Objects.requireNonNull(after);
		return (T t) -> {
			accept(t);
			after.accept(t);
		};
	}

	/**
	 * nothing
	 *
	 * @param <T> type
	 * @return nothing
	 */
	static <T> SerConsumer<T> nothing() {
		return t -> {
		};
	}
}
