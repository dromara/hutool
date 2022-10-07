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
 * @param <R> 返回值类型
 */
@FunctionalInterface
public interface SerSupplier<R> extends Supplier<R>, Serializable {

	/**
	 * Gets a result.
	 *
	 * @return a result
	 * @throws Exception wrapped checked exceptions
	 */
	R getting() throws Exception;

	/**
	 * Gets a result.
	 *
	 * @return a result
	 */
	@Override
	default R get() {
		try {
			return getting();
		} catch (final Exception e) {
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
	static <T> SerSupplier<T> last(final SerSupplier<T>... serSups) {
		return Stream.of(serSups).reduce((l, r) -> r).orElseGet(() -> () -> null);
	}

}
