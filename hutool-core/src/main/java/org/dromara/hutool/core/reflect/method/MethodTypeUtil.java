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

import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;

/**
 * {@link MethodType}相关工具类
 *
 * @author looly
 * @since 6.0.0
 */
public class MethodTypeUtil {

	/**
	 * 获取指定{@link Executable}的{@link MethodType}<br>
	 * 此方法主要是读取方法或构造中的方法列表，主要为：
	 * <ul>
	 *     <li>方法：[返回类型, 参数1类型, 参数2类型, ...]</li>
	 *     <li>构造：[构造对应类类型, 参数1类型, 参数2类型, ...]</li>
	 * </ul>
	 *
	 * @param executable 方法或构造
	 * @return {@link MethodType}
	 */
	public static MethodType methodType(final Executable executable) {
		if (executable instanceof Method) {
			final Method method = (Method) executable;
			return MethodType.methodType(method.getReturnType(), method.getDeclaringClass(), method.getParameterTypes());
		} else {
			final Constructor<?> constructor = (Constructor<?>) executable;
			return MethodType.methodType(constructor.getDeclaringClass(), constructor.getParameterTypes());
		}
	}
}
