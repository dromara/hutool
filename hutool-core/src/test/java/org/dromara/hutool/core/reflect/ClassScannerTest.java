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

package org.dromara.hutool.core.reflect;

import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class ClassScannerTest {

	@Test
	@Disabled
	public void scanTest() {
		final ClassScanner scaner = new ClassScanner("org.dromara.hutool.core.util", null);
		final Set<Class<?>> set = scaner.scan();
		for (final Class<?> clazz : set) {
			Console.log(clazz.getName());
		}
	}

	@Test
	@Disabled
	public void scanPackageBySuperTest() {
		// 扫描包，如果在classpath下找到，就不扫描JDK的jar了
		final Set<Class<?>> classes = ClassScanner.scanPackageBySuper(null, Iterable.class);
		Console.log(classes.size());
	}

	@Test
	@Disabled
	public void scanAllPackageBySuperTest() {
		// 扫描包，如果在classpath下找到，就不扫描JDK的jar了
		final Set<Class<?>> classes = ClassScanner.scanAllPackageBySuper(null, Iterable.class);
		Console.log(classes.size());
	}

	@Test
	@Disabled
	public void scanAllPackageIgnoreLoadErrorTest() {
		final ClassScanner classScanner = new ClassScanner(null, null);
		classScanner.setIgnoreLoadError(true);
		final Set<Class<?>> classes = classScanner.scan(false);
		Console.log(classes.size());
		Console.log(classScanner.getClassesOfLoadError());
	}
}
