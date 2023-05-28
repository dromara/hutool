package org.dromara.hutool.core.reflect.method;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * <p>方法匹配器，本身可作为{@link Predicate}校验方法是否匹配指定条件。
 * 当作为{@link MethodMetadataLookup}使用时，
 * 若方法符合条件，则返回{@link Boolean#TRUE}，否则返回{@code null}。
 *
 * @author huangchengxing
 * @see MethodMatcherUtil
 * @see MethodMetadataLookup
 * @since 6.0.0
 */
@FunctionalInterface
public interface MethodMatcher extends MethodMetadataLookup<Boolean>, Predicate<Method> {

	/**
	 * 返回一个组合的条件，当且仅当所有条件都符合时，才返回{@code true}，
	 *
	 * @param other 其他条件
	 * @return 条件
	 * @throws NullPointerException 当other为null时抛出
	 */
	@Override
	default MethodMatcher and(final Predicate<? super Method> other) {
		Objects.requireNonNull(other);
		return t -> test(t) && other.test(t);
	}

	/**
	 * 返回一个与此条件相反的条件
	 *
	 * @return 条件
	 */
	@Override
	default MethodMatcher negate() {
		return t -> !test(t);
	}

	/**
	 * 返回一个组合的条件，当且仅当任一条件符合时，才返回{@code true}，
	 *
	 * @param other 其他条件
	 * @return 条件
	 * @throws NullPointerException 当other为null时抛出
	 */
	@Override
	default MethodMatcher or(final Predicate<? super Method> other) {
		Objects.requireNonNull(other);
		return t -> test(t) || other.test(t);
	}

	/**
	 * 检查方法，若结果不为{@code null}则认为方法与其匹配
	 *
	 * @param method 要检查的方法
	 * @return 结果
	 */
	@Override
	default Boolean inspect(final Method method) {
		return test(method) ? Boolean.TRUE : null;
	}
}
