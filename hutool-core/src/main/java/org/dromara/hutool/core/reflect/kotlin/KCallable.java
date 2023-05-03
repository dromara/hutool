/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.reflect.kotlin;

import org.dromara.hutool.core.classloader.ClassLoaderUtil;
import org.dromara.hutool.core.reflect.MethodUtil;

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

	static {
		final Class<?> kFunctionClass = ClassLoaderUtil.loadClass("kotlin.reflect.KCallable");
		METHOD_GET_PARAMETERS = MethodUtil.getMethod(kFunctionClass, "getParameters");
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
}
