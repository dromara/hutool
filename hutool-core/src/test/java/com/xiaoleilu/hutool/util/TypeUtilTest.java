package com.xiaoleilu.hutool.util;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.util.ReflectUtil;
import com.xiaoleilu.hutool.util.TypeUtil;

public class TypeUtilTest {
	
	@Test
	public void getArgumentTypeTest(){
		List<String> list = new ArrayList<String>();
		list.add("aaaa");
		ParameterizedType type = (ParameterizedType)list.getClass().getGenericSuperclass();
		Type[] arguments = type.getActualTypeArguments();
		Console.log(arguments[0]);
		
		Type typeArgument = TypeUtil.getTypeArgument(list.getClass());
		Console.log(typeArgument);
	}
	
	@Test
	public void getParamTypeTest() {
		Method method = ReflectUtil.getMethod(TestClass.class, "intTest", Integer.class);
		Type type = TypeUtil.getParamType(method, 0);
		Assert.assertEquals(Integer.class, type);
		
		Type returnType = TypeUtil.getReturnType(method);
		Assert.assertEquals(Integer.class, returnType);
	}
	
	public static class TestClass {
		public Integer intTest(Integer integer) {
			return 1;
		}
	}
}
