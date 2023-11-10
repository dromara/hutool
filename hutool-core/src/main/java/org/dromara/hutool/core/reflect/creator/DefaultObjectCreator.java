/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.reflect.creator;

import org.dromara.hutool.core.classloader.ClassLoaderUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.core.reflect.lookup.LookupUtil;
import org.dromara.hutool.core.reflect.method.MethodHandleUtil;

import java.lang.invoke.MethodHandle;

/**
 * 默认对象实例化器<br>
 * 通过传入对象类型和构造函数的参数，调用对应的构造方法创建对象。
 *
 * @param <T> 对象类型
 */
public class DefaultObjectCreator<T> implements ObjectCreator<T> {

	/**
	 * 创建默认的对象实例化器
	 *
	 * @param fullClassName  类名全程
	 * @param <T>    对象类型
	 * @return DefaultObjectCreator
	 */
	public static <T> DefaultObjectCreator<T> of(final String fullClassName) {
		return of(ClassLoaderUtil.loadClass(fullClassName));
	}

	/**
	 * 创建默认的对象实例化器
	 *
	 * @param clazz  实例化的类
	 * @param params 构造参数，无参数空
	 * @param <T>    对象类型
	 * @return DefaultObjectCreator
	 */
	public static <T> DefaultObjectCreator<T> of(final Class<T> clazz, final Object... params) {
		return new DefaultObjectCreator<>(clazz, params);
	}

	final MethodHandle constructor;
	final Object[] params;

	/**
	 * 构造
	 *
	 * @param clazz  实例化的类
	 * @param params 构造参数，无参数空
	 */
	public DefaultObjectCreator(final Class<T> clazz, final Object... params) {
		final Class<?>[] paramTypes = ClassUtil.getClasses(params);
		this.constructor = LookupUtil.findConstructor(clazz, paramTypes);
		Assert.notNull(this.constructor, "Constructor not found!");
		this.params = params;
	}

	@Override
	public T create() {
		return MethodHandleUtil.invokeHandle(constructor, params);
	}
}
