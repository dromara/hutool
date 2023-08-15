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
