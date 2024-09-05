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

package org.dromara.hutool.core.util;

import org.dromara.hutool.core.reflect.ClassUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * {@link ClassUtil} 单元测试
 *
 * @author Looly
 *
 */
public class ClassUtilTest {

	@Test
	public void getClassNameTest() {
		final String className = ClassUtil.getClassName(ClassUtil.class, false);
		Assertions.assertEquals("org.dromara.hutool.core.reflect.ClassUtil", className);

		final String simpleClassName = ClassUtil.getClassName(ClassUtil.class, true);
		Assertions.assertEquals("ClassUtil", simpleClassName);
	}

	@Test
	public void getClassPathTest() {
		final String classPath = ClassUtil.getClassPath();
		Assertions.assertNotNull(classPath);
	}

	@Test
	public void getShortClassNameTest() {
		final String className = "org.dromara.hutool.core.text.StrUtil";
		final String result = ClassUtil.getShortClassName(className);
		Assertions.assertEquals("o.d.h.c.t.StrUtil", result);
	}

	@Test
	public void getLocationPathTest(){
		final String classDir = ClassUtil.getLocationPath(ClassUtilTest.class);
		Assertions.assertTrue(Objects.requireNonNull(classDir).endsWith("/hutool-core/target/test-classes/"));
	}

	@Test
	public void isAssignableTest(){
		Assertions.assertTrue(ClassUtil.isAssignable(int.class, int.class));
		Assertions.assertTrue(ClassUtil.isAssignable(int.class, Integer.class));
		Assertions.assertFalse(ClassUtil.isAssignable(int.class, String.class));
	}
}
