package com.xiaoleilu.hutool.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

/**
 * {@link ClassUtil} 单元测试
 * 
 * @author Looly
 *
 */
public class ClassUtilTest {

	@Test
	public void getClassNameTest() {
		String className = ClassUtil.getClassName(ClassUtil.class, false);
		Assert.assertEquals("com.xiaoleilu.hutool.util.ClassUtil", className);

		String simpleClassName = ClassUtil.getClassName(ClassUtil.class, true);
		Assert.assertEquals("ClassUtil", simpleClassName);
	}

	@SuppressWarnings("unused")
	class TestClass {
		private String privateField;
		protected String field;

		private void privateMethod() {
		}

		public void publicMethod() {
		}
	}

	@SuppressWarnings("unused")
	class TestSubClass extends TestClass {
		private String subField;

		private void privateSubMethod() {
		}

		public void publicSubMethod() {
		}

	}

	@Test
	public void getPublicMethod() {
		Method superPublicMethod = ClassUtil.getPublicMethod(TestSubClass.class, "publicMethod");
		Assert.assertNotNull(superPublicMethod);
		Method superPrivateMethod = ClassUtil.getPublicMethod(TestSubClass.class, "privateMethod");
		Assert.assertNull(superPrivateMethod);

		Method publicMethod = ClassUtil.getPublicMethod(TestSubClass.class, "publicSubMethod");
		Assert.assertNotNull(publicMethod);
		Method privateMethod = ClassUtil.getPublicMethod(TestSubClass.class, "privateSubMethod");
		Assert.assertNull(privateMethod);
	}

	@Test
	public void getDeclaredMethod() throws Exception {
		Method noMethod = ClassUtil.getDeclaredMethod(TestSubClass.class, "noMethod");
		Assert.assertNull(noMethod);

		Method privateMethod = ClassUtil.getDeclaredMethod(TestSubClass.class, "privateMethod");
		Assert.assertNotNull(privateMethod);
		Method publicMethod = ClassUtil.getDeclaredMethod(TestSubClass.class, "publicMethod");
		Assert.assertNotNull(publicMethod);

		Method publicSubMethod = ClassUtil.getDeclaredMethod(TestSubClass.class, "publicSubMethod");
		Assert.assertNotNull(publicSubMethod);
		Method privateSubMethod = ClassUtil.getDeclaredMethod(TestSubClass.class, "privateSubMethod");
		Assert.assertNotNull(privateSubMethod);

	}

	@Test
	public void getDeclaredField() {
		Field noField = ClassUtil.getDeclaredField(TestSubClass.class, "noField");
		Assert.assertNull(noField);

		// 获取不到父类字段
		Field field = ClassUtil.getDeclaredField(TestSubClass.class, "field");
		Assert.assertNull(field);

		Field subField = ClassUtil.getDeclaredField(TestSubClass.class, "subField");
		Assert.assertNotNull(subField);
	}
	
	@Test
	public void getClassPathTest() {
		String classPath = ClassUtil.getClassPath();
		Assert.assertNotNull(classPath);
	}
}
