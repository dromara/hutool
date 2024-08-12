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

package org.dromara.hutool.core.reflect.lookup;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;

/**
 * jdk8中如果直接调用{@link MethodHandles#lookup()}获取到的{@link MethodHandles.Lookup}<br>
 * 在调用findSpecial和unreflectSpecial时会出现权限不够问题，抛出"no private access for invokespecial"异常<br>
 * 所以通过反射创建MethodHandles.Lookup解决该问题。
 * <p>
 * 参考：https://blog.csdn.net/u013202238/article/details/108687086
 *
 * @author looly
 * @since 6.0.0
 */
public class ConstructorLookupFactory implements LookupFactory {

	private static final int ALLOWED_MODES = MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED
		| MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC;

	private final Constructor<MethodHandles.Lookup> lookupConstructor;

	/**
	 * 构造
	 */
	public ConstructorLookupFactory() {
		this.lookupConstructor = createLookupConstructor();
	}

	@Override
	public MethodHandles.Lookup lookup(final Class<?> callerClass) {
		try {
			return lookupConstructor.newInstance(callerClass, ALLOWED_MODES);
		} catch (final Exception e) {
			throw new IllegalStateException("no 'Lookup(Class, int)' method in java.lang.invoke.MethodHandles.", e);
		}
	}

	private static Constructor<MethodHandles.Lookup> createLookupConstructor() {
		final Constructor<MethodHandles.Lookup> constructor;
		try {
			constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
		} catch (final NoSuchMethodException e) {
			//可能是jdk8 以下版本
			throw new IllegalStateException(
				"There is no 'Lookup(Class, int)' constructor in java.lang.invoke.MethodHandles.", e);
		}
		constructor.setAccessible(true);
		return constructor;
	}
}
