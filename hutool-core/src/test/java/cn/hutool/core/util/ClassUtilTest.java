package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
		String className = ClassUtil.getClassName(ClassUtil.class, false);
		Assert.assertEquals("cn.hutool.core.util.ClassUtil", className);

		String simpleClassName = ClassUtil.getClassName(ClassUtil.class, true);
		Assert.assertEquals("ClassUtil", simpleClassName);
	}

	@SuppressWarnings("unused")
	static class TestClass {
		private String privateField;
		protected String field;

		private void privateMethod() {
		}

		public void publicMethod() {
		}
	}

	@SuppressWarnings({"unused", "InnerClassMayBeStatic"})
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
	public void getDeclaredMethod() {
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
	
	@Test
	public void getShortClassNameTest() {
		String className = "cn.hutool.core.util.StrUtil";
		String result = ClassUtil.getShortClassName(className);
		Assert.assertEquals("c.h.c.u.StrUtil", result);
	}

	@Test
	public void getLocationPathTest(){
		final String classDir = ClassUtil.getLocationPath(ClassUtilTest.class);
		Assert.assertTrue(Objects.requireNonNull(classDir).endsWith("/hutool-core/target/test-classes/"));
	}
}
