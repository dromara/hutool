package cn.hutool.core.lang.func;

import cn.hutool.core.exceptions.UtilException;

import java.io.Serializable;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 可序列化的Supplier
 *
 * @author VampireAchao
 * @see Supplier
 */
@FunctionalInterface
public interface SerSupplier<T> extends Supplier<T>, Serializable {

	/**
	 * Gets a result.
	 *
	 * @return a result
	 * @throws Exception wrappered checked exceptions
	 */
	@SuppressWarnings("all")
	T getting() throws Exception;

	/**
	 * Gets a result.
	 *
	 * @return a result
	 */
	@Override
	default T get() {
		try {
			return getting();
		} catch (Exception e) {
			throw new UtilException(e);
		}
	}

	/**
	 * last
	 *
	 * @param serSups lambda
	 * @param <T>     type
	 * @return lambda
	 */
	@SafeVarargs
	static <T> SerSupplier<T> last(SerSupplier<T>... serSups) {
		return Stream.of(serSups).reduce((l, r) -> r).orElseGet(() -> () -> null);
	}

}
