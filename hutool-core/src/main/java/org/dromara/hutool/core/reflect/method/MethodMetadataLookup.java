package org.dromara.hutool.core.reflect.method;

import java.lang.reflect.Method;

/**
 * 方法的元数据查找器，参照 spring 的 {@code MethodIntrospector.MetadataLookup}，用于从方法上获得特定的元数据。
 *
 * @author huangchengxing
 * @see MethodMatcher
 * @see MethodMatcherUtil
 * @since 6.0.0
 */
@FunctionalInterface
public interface MethodMetadataLookup<T> {

	/**
	 * 检查并返回方法上的特定元数据，若结果不为{@code null}则认为方法与其匹配
	 *
	 * @param method 要检查的方法
	 * @return 结果
	 */
	T inspect(Method method);
}
