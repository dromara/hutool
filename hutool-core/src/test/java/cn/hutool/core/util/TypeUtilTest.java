package cn.hutool.core.util;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.TypeUtil;

public class TypeUtilTest {
	
	@Test
	public void getEleTypeTest() {
		Method method = ReflectUtil.getMethod(TestClass.class, "getList");
		Type type = TypeUtil.getReturnType(method);
		Assert.assertEquals("java.util.List<java.lang.String>", type.toString());
		
		Type type2 = TypeUtil.getTypeArgument(type);
		Assert.assertEquals(String.class, type2);
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
		public List<String> getList(){
			return new ArrayList<>();
		}
		
		public Integer intTest(Integer integer) {
			return 1;
		}
	}
}
