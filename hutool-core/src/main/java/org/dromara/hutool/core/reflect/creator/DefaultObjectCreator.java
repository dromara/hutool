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
