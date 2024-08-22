/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.reflect.kotlin;

import org.dromara.hutool.core.classloader.ClassLoaderUtil;
import org.dromara.hutool.core.reflect.method.MethodUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * kotlin.reflect.KCallable方法包装调用类
 *
 * @author VampireAchao, Looly
 */
public class KCallable {

	private static final Method METHOD_GET_PARAMETERS;
	private static final Method METHOD_CALL;

	static {
		final Class<?> kFunctionClass = ClassLoaderUtil.loadClass("kotlin.reflect.KCallable");
		METHOD_GET_PARAMETERS = MethodUtil.getMethod(kFunctionClass, "getParameters");
		METHOD_CALL = MethodUtil.getMethodByName(kFunctionClass, "call");
	}

	/**
	 * 获取参数列表
	 *
	 * @param kCallable kotlin的类、方法或构造
	 * @return 参数列表
	 */
	public static List<KParameter> getParameters(final Object kCallable) {
		final List<?> parameters = MethodUtil.invoke(kCallable, METHOD_GET_PARAMETERS);
		final List<KParameter> result = new ArrayList<>(parameters.size());
		for (final Object parameter : parameters) {
			result.add(new KParameter(parameter));
		}
		return result;
	}

	/**
	 * 实例化对象，本质上调用KCallable.call方法
	 *
	 * @param kCallable kotlin的类、方法或构造
	 * @param args      参数列表
	 * @return 参数列表
	 */
	public static Object call(final Object kCallable, final Object... args) {
		return MethodUtil.invoke(kCallable, METHOD_CALL, new Object[]{args});
	}
}
