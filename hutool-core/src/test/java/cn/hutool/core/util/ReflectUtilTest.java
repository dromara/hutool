package cn.hutool.core.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.date.Week;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.test.bean.ExamInfoDict;
import cn.hutool.core.util.ClassUtilTest.TestSubClass;
import lombok.Data;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 反射工具类单元测试
 *
 * @author Looly
 */
public class ReflectUtilTest {

	@Test
	public void getMethodsTest() {
		Method[] methods = ReflectUtil.getMethods(ExamInfoDict.class);
		assertEquals(20, methods.length);

		//过滤器测试
		methods = ReflectUtil.getMethods(ExamInfoDict.class, t -> Integer.class.equals(t.getReturnType()));

		assertEquals(4, methods.length);
		final Method method = methods[0];
		assertNotNull(method);

		//null过滤器测试
		methods = ReflectUtil.getMethods(ExamInfoDict.class, null);

		assertEquals(20, methods.length);
		final Method method2 = methods[0];
		assertNotNull(method2);
	}

	@Test
	public void getMethodTest() {
		Method method = ReflectUtil.getMethod(ExamInfoDict.class, "getId");
		assertEquals("getId", method.getName());
		assertEquals(0, method.getParameterTypes().length);

		method = ReflectUtil.getMethod(ExamInfoDict.class, "getId", Integer.class);
		assertEquals("getId", method.getName());
		assertEquals(1, method.getParameterTypes().length);
	}

	@Test
	public void getMethodIgnoreCaseTest() {
		Method method = ReflectUtil.getMethodIgnoreCase(ExamInfoDict.class, "getId");
		assertEquals("getId", method.getName());
		assertEquals(0, method.getParameterTypes().length);

		method = ReflectUtil.getMethodIgnoreCase(ExamInfoDict.class, "GetId");
		assertEquals("getId", method.getName());
		assertEquals(0, method.getParameterTypes().length);

		method = ReflectUtil.getMethodIgnoreCase(ExamInfoDict.class, "setanswerIs", Integer.class);
		assertEquals("setAnswerIs", method.getName());
		assertEquals(1, method.getParameterTypes().length);
	}

	@Test
	public void getFieldTest() {
		// 能够获取到父类字段
		final Field privateField = ReflectUtil.getField(TestSubClass.class, "privateField");
		assertNotNull(privateField);
	}

	@Test
	public void getFieldsTest() {
		// 能够获取到父类字段
		final Field[] fields = ReflectUtil.getFields(TestSubClass.class);
		assertEquals(4, fields.length);
	}

	@Test
	public void setFieldTest() {
		final TestClass testClass = new TestClass();
		ReflectUtil.setFieldValue(testClass, "a", "111");
		assertEquals(111, testClass.getA());
	}

	@Test
	public void invokeTest() {
		final TestClass testClass = new TestClass();
		ReflectUtil.invoke(testClass, "setA", 10);
		assertEquals(10, testClass.getA());
	}

	@Test
	public void invokeMethodTest() {
		final TestClass testClass = new TestClass();
		final Method method = ReflectUtil.getMethod(TestClass.class, "setA", int.class);
		ReflectUtil.invoke(testClass, method, 10);
		assertEquals(10, testClass.getA());
	}

	@Test
	public void invokeMethodWithParamConvertTest() {
		final TestClass testClass = new TestClass();
		final Method method = ReflectUtil.getMethod(TestClass.class, "setA", int.class);
		ReflectUtil.invoke(testClass, method, "10");
		assertEquals(10, testClass.getA());
	}

	@Test
	public void invokeMethodWithParamConvertFailedTest() {
		final TestClass testClass = new TestClass();
		final Method method = ReflectUtil.getMethod(TestClass.class, "setA", int.class);
		assertThrows(IllegalArgumentException.class,
				() -> ReflectUtil.invoke(testClass, method, "NaN"));
	}

	@Test
	public void noneStaticInnerClassTest() {
		final NoneStaticClass testAClass = ReflectUtil.newInstanceIfPossible(NoneStaticClass.class);
		assertNotNull(testAClass);
		assertEquals(2, testAClass.getA());
	}

	@Data
	static class TestClass {
		private int a;
	}

	@Data
	@SuppressWarnings("InnerClassMayBeStatic")
	class NoneStaticClass {
		private int a = 2;
	}

	@Test
	@Disabled
	public void getMethodBenchTest() {
		// 预热
		getMethodWithReturnTypeCheck(TestBenchClass.class, false, "getH");

		final TimeInterval timer = DateUtil.timer();
		timer.start();
		for (int i = 0; i < 100000000; i++) {
			ReflectUtil.getMethod(TestBenchClass.class, false, "getH");
		}
		Console.log(timer.interval());

		timer.restart();
		for (int i = 0; i < 100000000; i++) {
			getMethodWithReturnTypeCheck(TestBenchClass.class, false, "getH");
		}
		Console.log(timer.interval());
	}

	@Data
	static class TestBenchClass {
		private int a;
		private String b;
		private String c;
		private String d;
		private String e;
		private String f;
		private String g;
		private String h;
		private String i;
		private String j;
		private String k;
		private String l;
		private String m;
		private String n;
	}

	public static Method getMethodWithReturnTypeCheck(final Class<?> clazz, final boolean ignoreCase, final String methodName, final Class<?>... paramTypes) throws SecurityException {
		if (null == clazz || StrUtil.isBlank(methodName)) {
			return null;
		}

		Method res = null;
		final Method[] methods = ReflectUtil.getMethods(clazz);
		if (ArrayUtil.isNotEmpty(methods)) {
			for (final Method method : methods) {
				if (StrUtil.equals(methodName, method.getName(), ignoreCase)
						&& ClassUtil.isAllAssignableFrom(method.getParameterTypes(), paramTypes)
						&& (res == null
						|| res.getReturnType().isAssignableFrom(method.getReturnType()))) {
					res = method;
				}
			}
		}
		return res;
	}

	@Test
	public void getMethodsFromClassExtends() {
		// 继承情况下，需解决方法去重问题
		Method[] methods = ReflectUtil.getMethods(C2.class);
		assertEquals(15, methods.length);

		// 排除Object中的方法
		// 3个方法包括类
		methods = ReflectUtil.getMethodsDirectly(C2.class, true, false);
		assertEquals(3, methods.length);

		// getA属于本类
		assertEquals("public void cn.hutool.core.util.ReflectUtilTest$C2.getA()", methods[0].toString());
		// getB属于父类
		assertEquals("public void cn.hutool.core.util.ReflectUtilTest$C1.getB()", methods[1].toString());
		// getC属于接口中的默认方法
		assertEquals("public default void cn.hutool.core.util.ReflectUtilTest$TestInterface1.getC()", methods[2].toString());
	}

	@Test
	public void getMethodsFromInterfaceTest() {
		// 对于接口，直接调用Class.getMethods方法获取所有方法，因为接口都是public方法
		// 因此此处得到包括TestInterface1、TestInterface2、TestInterface3中一共4个方法
		final Method[] methods = ReflectUtil.getMethods(TestInterface3.class);
		assertEquals(4, methods.length);

		Arrays.sort(methods, Comparator.comparing(Method::toString));
		// 接口里，调用getMethods和getPublicMethods效果相同
		final Method[] publicMethods = ReflectUtil.getPublicMethods(TestInterface3.class);
		Arrays.sort(publicMethods, Comparator.comparing(Method::toString));
		assertArrayEquals(methods, publicMethods);
	}

	interface TestInterface1 {
		@SuppressWarnings("unused")
		void getA();

		@SuppressWarnings("unused")
		void getB();

		@SuppressWarnings("unused")
		default void getC() {

		}
	}

	interface TestInterface2 extends TestInterface1 {
		@Override
		void getB();
	}

	interface TestInterface3 extends TestInterface2 {
		void get3();
	}

	@SuppressWarnings("InnerClassMayBeStatic")
	class C1 implements TestInterface2 {

		@Override
		public void getA() {

		}

		@Override
		public void getB() {

		}
	}

	class C2 extends C1 {
		@Override
		public void getA() {

		}
	}

	@Test
	public void newInstanceIfPossibleTest(){
		//noinspection ConstantConditions
		final int intValue = ReflectUtil.newInstanceIfPossible(int.class);
		assertEquals(0, intValue);

		final Integer integer = ReflectUtil.newInstanceIfPossible(Integer.class);
		assertEquals(new Integer(0), integer);

		final Map<?, ?> map = ReflectUtil.newInstanceIfPossible(Map.class);
		assertNotNull(map);

		final Collection<?> collection = ReflectUtil.newInstanceIfPossible(Collection.class);
		assertNotNull(collection);

		final Week week = ReflectUtil.newInstanceIfPossible(Week.class);
		assertEquals(Week.SUNDAY, week);

		final int[] intArray = ReflectUtil.newInstanceIfPossible(int[].class);
		assertArrayEquals(new int[0], intArray);
	}

	public static class JdbcDialects {
		private static final List<Number> DIALECTS =
				Arrays.asList(1L, 2L, 3L);
	}

	@Test
	public void setFieldValueWithFinalTest() {
		final String fieldName = "DIALECTS";
		final List<Number> dialects =
				Arrays.asList(
						1,
						2,
						3,
						99
				);
		final Field field = ReflectUtil.getField(JdbcDialects.class, fieldName);
		ReflectUtil.removeFinalModify(field);
		ReflectUtil.setFieldValue(JdbcDialects.class, fieldName, dialects);

		assertEquals(dialects, ReflectUtil.getFieldValue(JdbcDialects.class, fieldName));
	}

	@Test
	public void issue2625Test(){
		// 内部类继承的情况下父类方法会被定义为桥接方法，因此按照pr#1965@Github判断返回值的继承关系来代替判断桥接。
		final Method getThis = ReflectUtil.getMethod(A.C.class, "getThis");
		assertTrue(getThis.isBridge());
	}

	@SuppressWarnings("InnerClassMayBeStatic")
	public class A{

		public class C extends B{

		}

		protected class B{
			public B getThis(){
				return this;
			}
		}
	}
}
