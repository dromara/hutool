/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
