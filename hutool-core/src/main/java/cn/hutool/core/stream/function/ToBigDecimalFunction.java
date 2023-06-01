package cn.hutool.core.stream.function;

import java.math.BigDecimal;
import java.util.function.Function;

/**
 * Represents a function that produces a BigDecimal-valued result.  This is the
 * {@code BigDecimal}-producing primitive specialization for {@link Function}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #applyAsBigDecimal(Object)}.
 *
 * @param <T> the type of the input to the function
 * @author bin.li
 * @since 5.8.20
 */
@FunctionalInterface
public interface ToBigDecimalFunction<T> {

	/**
	 * Applies this function to the given argument.
	 *
	 * @param value the function argument
	 * @return the function result
	 */
	BigDecimal applyAsBigDecimal(T value);
}
