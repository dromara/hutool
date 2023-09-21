/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.reflect.method;

import java.lang.reflect.Method;

/**
 * 方法的元数据查找器，参照 spring 的 {@code MethodIntrospector.MetadataLookup}，用于从方法上获得特定的元数据。
 *
 * @param <T> 返回类型
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
