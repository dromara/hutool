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
