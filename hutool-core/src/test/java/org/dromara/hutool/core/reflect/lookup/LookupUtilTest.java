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

package org.dromara.hutool.core.reflect.lookup;

import org.dromara.hutool.core.reflect.method.MethodUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

class LookupUtilTest {

	@Test
	void lookupTest() {
		final MethodHandles.Lookup lookup = LookupUtil.lookup();
		Assertions.assertNotNull(lookup);
	}

	@Test
	void findMethodTest() throws Throwable {
		MethodHandle handle = LookupUtil.findMethod(Duck.class, "quack",
			MethodType.methodType(String.class));
		Assertions.assertNotNull(handle);
		// 对象方法自行需要绑定对象或者传入对象参数
		final String invoke = (String) handle.invoke(new BigDuck());
		Assertions.assertEquals("Quack", invoke);

		// 对象的方法获取
		handle = LookupUtil.findMethod(BigDuck.class, "getSize",
			MethodType.methodType(int.class));
		Assertions.assertNotNull(handle);
		final int invokeInt = (int) handle.invoke(new BigDuck());
		Assertions.assertEquals(36, invokeInt);
	}

	@Test
	void findStaticMethodTest() throws Throwable {
		final MethodHandle handle = LookupUtil.findMethod(Duck.class, "getDuck",
			MethodType.methodType(String.class, int.class));
		Assertions.assertNotNull(handle);

		// static 方法执行不需要绑定或者传入对象，直接传入参数即可
		final String invoke = (String) handle.invoke(12);
		Assertions.assertEquals("Duck 12", invoke);
	}

	@Test
	void findPrivateMethodTest() throws Throwable {
		final MethodHandle handle = LookupUtil.findMethod(BigDuck.class, "getPrivateValue",
			MethodType.methodType(String.class));
		Assertions.assertNotNull(handle);

		final String invoke = (String) handle.invoke(new BigDuck());
		Assertions.assertEquals("private value", invoke);
	}

	@Test
	void findSuperMethodTest() throws Throwable {
		// 查找父类的方法
		final MethodHandle handle = LookupUtil.findMethod(BigDuck.class, "quack",
			MethodType.methodType(String.class));
		Assertions.assertNotNull(handle);

		final String invoke = (String) handle.invoke(new BigDuck());
		Assertions.assertEquals("Quack", invoke);
	}

	@Test
	void findPrivateStaticMethodTest() throws Throwable {
		final MethodHandle handle = LookupUtil.findMethod(BigDuck.class, "getPrivateStaticValue",
			MethodType.methodType(String.class));
		Assertions.assertNotNull(handle);

		final String invoke = (String) handle.invoke();
		Assertions.assertEquals("private static value", invoke);
	}

	@Test
	void unreflectTest() throws Throwable {
		final MethodHandle handle = LookupUtil.unreflect(
			MethodUtil.getMethodByName(BigDuck.class, "getSize"));

		final int invoke = (int) handle.invoke(new BigDuck());
		Assertions.assertEquals(36, invoke);
	}

	@Test
	void unreflectPrivateTest() throws Throwable {
		final MethodHandle handle = LookupUtil.unreflect(
			MethodUtil.getMethodByName(BigDuck.class, "getPrivateValue"));

		final String invoke = (String) handle.invoke(new BigDuck());
		Assertions.assertEquals("private value", invoke);
	}

	@Test
	void unreflectPrivateStaticTest() throws Throwable {
		final MethodHandle handle = LookupUtil.unreflect(
			MethodUtil.getMethodByName(BigDuck.class, "getPrivateStaticValue"));

		final String invoke = (String) handle.invoke();
		Assertions.assertEquals("private static value", invoke);
	}

	@Test
	void unreflectDefaultTest() throws Throwable {
		final MethodHandle handle = LookupUtil.unreflect(
			MethodUtil.getMethodByName(BigDuck.class, "quack"));

		final String invoke = (String) handle.invoke(new BigDuck());
		Assertions.assertEquals("Quack", invoke);
	}

	@Test
	void unreflectStaticInInterfaceTest() throws Throwable {
		final MethodHandle handle = LookupUtil.unreflect(
			MethodUtil.getMethodByName(BigDuck.class, "getDuck"));

		final String invoke = (String) handle.invoke(1);
		Assertions.assertEquals("Duck 1", invoke);
	}

	interface Duck {
		default String quack() {
			return "Quack";
		}

		static String getDuck(final int count){
			return "Duck " + count;
		}
	}

	static class BigDuck implements Duck {
		public int getSize(){
			return 36;
		}

		@SuppressWarnings("unused")
		private String getPrivateValue(){
			return "private value";
		}

		@SuppressWarnings("unused")
		private static String getPrivateStaticValue(){
			return "private static value";
		}
	}
}
