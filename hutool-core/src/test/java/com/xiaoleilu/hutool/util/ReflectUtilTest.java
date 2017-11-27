package com.xiaoleilu.hutool.util;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.lang.test.bean.ExamInfoDict;

/**
 * 反射工具类单元测试
 * @author Looly
 *
 */
public class ReflectUtilTest {
	
	@Test
	public void getMethodsTest(){
		Method[] methods = ReflectUtil.getMethods(ExamInfoDict.class);
		Assert.assertTrue(ArrayUtil.isNotEmpty(methods));
	}
	
	@Test
	public void getMethodTest(){
		Method method = ReflectUtil.getMethod(ExamInfoDict.class, "getId");
		Assert.assertEquals("getId", method.getName());
	}
	
	@Test
	public void invokeTest() {
		TestClass testClass = new TestClass();
		ReflectUtil.invoke(testClass, "setA", 10);
		Assert.assertEquals(10, testClass.getA());
	}
	
	static class TestClass{
		private int a;

		public int getA() {
			return a;
		}

		public void setA(int a) {
			this.a = a;
		}
	}
}
