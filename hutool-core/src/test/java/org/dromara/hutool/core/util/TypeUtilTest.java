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
