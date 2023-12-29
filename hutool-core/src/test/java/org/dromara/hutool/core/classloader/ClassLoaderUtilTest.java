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

package org.dromara.hutool.core.classloader;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.map.Dict;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.core.reflect.FieldUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class ClassLoaderUtilTest {

	@Test
	void getCallerClassLoaderTest() {
		final ClassLoader callerClassLoader = ClassLoaderUtil.getCallerClassLoader();
		Assertions.assertEquals(ClassLoaderUtilTest.class.getClassLoader(), callerClassLoader);
	}

	@Test
	public void isPresentTest() {
		final boolean present = ClassLoaderUtil.isPresent("org.dromara.hutool.core.classloader.ClassLoaderUtil");
		Assertions.assertTrue(present);
	}

	@Test
	public void loadClassTest() {
		String name = ClassLoaderUtil.loadClass("java.lang.Thread.State").getName();
		Assertions.assertEquals("java.lang.Thread$State", name);

		name = ClassLoaderUtil.loadClass("java.lang.Thread$State").getName();
		Assertions.assertEquals("java.lang.Thread$State", name);
	}

	@Test
	public void loadArrayClassTest(){
		final String s = Dict[].class.getName();

		final Class<Object> objectClass = ClassLoaderUtil.loadClass(s);
		Assertions.assertEquals(Dict[].class, objectClass);
	}

	@Test
	public void loadInnerClassTest() {
		String name = ClassLoaderUtil.loadClass("org.dromara.hutool.core.classloader.ClassLoaderUtilTest.A").getName();
		Assertions.assertEquals("org.dromara.hutool.core.classloader.ClassLoaderUtilTest$A", name);
		name = ClassLoaderUtil.loadClass("org.dromara.hutool.core.classloader.ClassLoaderUtilTest.A.B").getName();
		Assertions.assertEquals("org.dromara.hutool.core.classloader.ClassLoaderUtilTest$A$B", name);
	}

	@SuppressWarnings("unused")
	private static class A{
		private static class B{

		}
	}

	@Test
	@Disabled
	void loadClassFromJarTest() {
		final JarClassLoader classLoader = ClassLoaderUtil.getJarClassLoader(
			FileUtil.file("D:\\m2_repo\\com\\sap\\cloud\\db\\jdbc\\ngdbc\\2.18.13\\ngdbc-2.18.13.jar"));

		final Class<?> aClass = ClassUtil.forName("com.sap.db.jdbc.Driver", true, classLoader);
		final Field instance = FieldUtil.getField(aClass, "INSTANCE");
		Console.log(FieldUtil.getFieldValue(null, instance));

		final Field version = FieldUtil.getField(aClass, "JAVA_VERSION");
		Console.log(FieldUtil.getFieldValue(null, version));
	}
}
