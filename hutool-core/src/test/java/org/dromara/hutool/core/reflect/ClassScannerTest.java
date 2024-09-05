/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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
