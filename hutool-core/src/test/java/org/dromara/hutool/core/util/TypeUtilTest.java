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

import org.dromara.hutool.core.reflect.FieldUtil;
import org.dromara.hutool.core.reflect.method.MethodUtil;
import org.dromara.hutool.core.reflect.TypeUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TypeUtilTest {

	@Test
	public void getEleTypeTest() {
		final Method method = MethodUtil.getMethod(TestClass.class, "getList");
		final Type type = TypeUtil.getReturnType(method);
		Assertions.assertEquals("java.util.List<java.lang.String>", type.toString());

		final Type type2 = TypeUtil.getTypeArgument(type);
		Assertions.assertEquals(String.class, type2);
	}

	@Test
	public void getParamTypeTest() {
		final Method method = MethodUtil.getMethod(TestClass.class, "intTest", Integer.class);
		final Type type = TypeUtil.getParamType(method, 0);
		Assertions.assertEquals(Integer.class, type);

		final Type returnType = TypeUtil.getReturnType(method);
		Assertions.assertEquals(Integer.class, returnType);
	}

	public static class TestClass {
		public List<String> getList(){
			return new ArrayList<>();
		}

		public Integer intTest(final Integer integer) {
			return 1;
		}
	}

	@Test
	public void getTypeArgumentTest(){
		// 测试不继承父类，而是实现泛型接口时是否可以获取成功。
		final Type typeArgument = TypeUtil.getTypeArgument(IPService.class);
		Assertions.assertEquals(String.class, typeArgument);
	}

	public interface OperateService<T> {
		void service(T t);
	}

	public static class IPService implements OperateService<String> {
		@Override
		public void service(final String string) {
		}
	}

	@Test
	public void getActualTypesTest(){
		// 测试多层级泛型参数是否能获取成功
		final Type idType = TypeUtil.getActualType(Level3.class,
				FieldUtil.getField(Level3.class, "id"));

		Assertions.assertEquals(Long.class, idType);
	}

	@Test
	public void getClasses() {
		Method method = MethodUtil.getMethod(Parent.class, "getLevel");
		Type returnType = TypeUtil.getReturnType(method);
		Class<?> clazz = TypeUtil.getClass(returnType);
		Assertions.assertEquals(Level1.class, clazz);

		method = MethodUtil.getMethod(Level1.class, "getId");
		returnType = TypeUtil.getReturnType(method);
		clazz = TypeUtil.getClass(returnType);
		Assertions.assertEquals(Object.class, clazz);
	}

	public static class Level3 extends Level2<Level3>{

	}

	public static class Level2<E> extends Level1<Long>{

	}

	@Data
	public static class Level1<T>{
		private T id;
	}

	@Data
	public static class Parent<T extends Level1<B>, B extends Long> {
		private T level;
	}

}
