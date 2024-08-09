package cn.hutool.core.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

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
		assertEquals("cn.hutool.core.util.ClassUtil", className);

		String simpleClassName = ClassUtil.getClassName(ClassUtil.class, true);
		assertEquals("ClassUtil", simpleClassName);
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
		assertNotNull(superPublicMethod);
		Method superPrivateMethod = ClassUtil.getPublicMethod(TestSubClass.class, "privateMethod");
		assertNull(superPrivateMethod);

		Method publicMethod = ClassUtil.getPublicMethod(TestSubClass.class, "publicSubMethod");
		assertNotNull(publicMethod);
		Method privateMethod = ClassUtil.getPublicMethod(TestSubClass.class, "privateSubMethod");
		assertNull(privateMethod);
	}

	@Test
	public void getDeclaredMethod() {
		Method noMethod = ClassUtil.getDeclaredMethod(TestSubClass.class, "noMethod");
		assertNull(noMethod);

		Method privateMethod = ClassUtil.getDeclaredMethod(TestSubClass.class, "privateMethod");
		assertNotNull(privateMethod);
		Method publicMethod = ClassUtil.getDeclaredMethod(TestSubClass.class, "publicMethod");
		assertNotNull(publicMethod);

		Method publicSubMethod = ClassUtil.getDeclaredMethod(TestSubClass.class, "publicSubMethod");
		assertNotNull(publicSubMethod);
		Method privateSubMethod = ClassUtil.getDeclaredMethod(TestSubClass.class, "privateSubMethod");
		assertNotNull(privateSubMethod);

	}

	@Test
	public void getDeclaredField() {
		Field noField = ClassUtil.getDeclaredField(TestSubClass.class, "noField");
		assertNull(noField);

		// 获取不到父类字段
		Field field = ClassUtil.getDeclaredField(TestSubClass.class, "field");
		assertNull(field);

		Field subField = ClassUtil.getDeclaredField(TestSubClass.class, "subField");
		assertNotNull(subField);
	}

	@Test
	public void getClassPathTest() {
		String classPath = ClassUtil.getClassPath();
		assertNotNull(classPath);
	}

	@Test
	public void getShortClassNameTest() {
		String className = "cn.hutool.core.util.StrUtil";
		String result = ClassUtil.getShortClassName(className);
		assertEquals("c.h.c.u.StrUtil", result);
	}

	@Test
	public void getLocationPathTest(){
		final String classDir = ClassUtil.getLocationPath(ClassUtilTest.class);
		assertTrue(Objects.requireNonNull(classDir).endsWith("/hutool-core/target/test-classes/"));
	}
}
