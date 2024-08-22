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
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.reflect.method.MethodUtil;

import java.lang.reflect.Method;
import java.util.List;

/**
 * kotlin.reflect.jvm.internal.KClassImpl包装
 *
 * @author VampireAchao, Looly
 */
public class KClassImpl {
	private static final Class<?> KCLASS_IMPL_CLASS;
	private static final Method METHOD_GET_CONSTRUCTORS;

	static {
		KCLASS_IMPL_CLASS = ClassLoaderUtil.loadClass("kotlin.reflect.jvm.internal.KClassImpl");
		METHOD_GET_CONSTRUCTORS = MethodUtil.getMethod(KCLASS_IMPL_CLASS, "getConstructors");
	}

	/**
	 * 获取Kotlin类的所有构造方法
	 *
	 * @param targetType kotlin类
	 * @return 构造列表
	 */
	public static List<?> getConstructors(final Class<?> targetType) {
		final Object kClassImpl = ConstructorUtil.newInstance(KCLASS_IMPL_CLASS, targetType);
		return MethodUtil.invoke(kClassImpl, METHOD_GET_CONSTRUCTORS);
	}
}
