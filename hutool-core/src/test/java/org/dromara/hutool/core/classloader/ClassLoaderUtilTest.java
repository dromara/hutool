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
