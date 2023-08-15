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
import java.util.Objects;

/**
 * kotlin.reflect.KParameter实例表示类<br>
 * 通过反射获取Kotlin中KParameter相关属性值
 *
 * @author VampireAchao, Looly
 */
public class KParameter {

	private static final Method METHOD_GET_NAME;
	private static final Method METHOD_GET_TYPE;
	private static final Method METHOD_GET_JAVA_TYPE;

	static {
		final Class<?> kParameterClass = ClassLoaderUtil.loadClass("kotlin.reflect.KParameter");
		METHOD_GET_NAME = MethodUtil.getMethod(kParameterClass, "getName");
		METHOD_GET_TYPE = MethodUtil.getMethod(kParameterClass, "getType");

		final Class<?> kTypeClass = ClassLoaderUtil.loadClass("kotlin.reflect.jvm.internal.KTypeImpl");
		METHOD_GET_JAVA_TYPE = MethodUtil.getMethod(kTypeClass, "getJavaType");
	}

	private final String name;
	private final Class<?> type;

	/**
	 * 构造
	 *
	 * @param kParameterInstance kotlin.reflect.KParameter实例对象
	 */
	public KParameter(final Object kParameterInstance) {
		this.name = MethodUtil.invoke(kParameterInstance, METHOD_GET_NAME);
		final Object kType = MethodUtil.invoke(kParameterInstance, METHOD_GET_TYPE);
		this.type = MethodUtil.invoke(kType, METHOD_GET_JAVA_TYPE);
	}

	/**
	 * 获取参数名
	 *
	 * @return 参数名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取参数类型
	 *
	 * @return 参数类型
	 */
	public Class<?> getType() {
		return type;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final KParameter that = (KParameter) o;
		return Objects.equals(name, that.name) && Objects.equals(type, that.type);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, type);
	}

	@Override
	public String toString() {
		return "KParameter{" +
			"name='" + name + '\'' +
			", type=" + type +
			'}';
	}
}
