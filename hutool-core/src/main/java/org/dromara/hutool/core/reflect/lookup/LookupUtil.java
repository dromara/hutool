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

package org.dromara.hutool.core.reflect.lookup;

import org.dromara.hutool.core.lang.caller.CallerUtil;
import org.dromara.hutool.core.util.JdkUtil;

import java.lang.invoke.MethodHandles;

/**
 * {@link MethodHandles.Lookup}工厂工具，用于创建{@link MethodHandles.Lookup}对象<br>
 * {@link MethodHandles.Lookup}是一个方法句柄查找对象，用于在指定类中查找符合给定方法名称、方法类型的方法句柄。
 *
 * <p>
 * jdk8中如果直接调用{@link MethodHandles#lookup()}获取到的{@link MethodHandles.Lookup}在调用findSpecial和unreflectSpecial
 * 时会出现权限不够问题，抛出"no private access for invokespecial"异常，因此针对JDK8及JDK9+分别封装lookup方法。
 * </p>
 *
 * <p>
 * 参考：https://blog.csdn.net/u013202238/article/details/108687086
 *
 * @author looly
 * @since 6.0.0
 */
public class LookupUtil {

	private static final LookupFactory factory;

	static {
		if (JdkUtil.IS_JDK8) {
			factory = new ConstructorLookupFactory();
		} else {
			factory = new MethodLookupFactory();
		}
	}

	/**
	 * jdk8中如果直接调用{@link MethodHandles#lookup()}获取到的{@link MethodHandles.Lookup}在调用findSpecial和unreflectSpecial
	 * 时会出现权限不够问题，抛出"no private access for invokespecial"异常，因此针对JDK8及JDK9+分别封装lookup方法。
	 *
	 * @return {@link MethodHandles.Lookup}
	 */
	public static MethodHandles.Lookup lookup() {
		return lookup(CallerUtil.getCaller());
	}

	/**
	 * jdk8中如果直接调用{@link MethodHandles#lookup()}获取到的{@link MethodHandles.Lookup}在调用findSpecial和unreflectSpecial
	 * 时会出现权限不够问题，抛出"no private access for invokespecial"异常，因此针对JDK8及JDK9+分别封装lookup方法。
	 *
	 * @param callerClass 被调用的类或接口
	 * @return {@link MethodHandles.Lookup}
	 */
	public static MethodHandles.Lookup lookup(final Class<?> callerClass) {
		return factory.lookup(callerClass);
	}
}
