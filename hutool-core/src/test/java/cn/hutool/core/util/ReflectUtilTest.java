package cn.hutool.core.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.date.Week;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.test.bean.ExamInfoDict;
import cn.hutool.core.util.ClassUtilTest.TestSubClass;
import lombok.Data;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
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
		Assert.assertEquals(20, methods.length);

		//过滤器测试
		methods = ReflectUtil.getMethods(ExamInfoDict.class, t -> Integer.class.equals(t.getReturnType()));

		Assert.assertEquals(4, methods.length);
		final Method method = methods[0];
		Assert.assertNotNull(method);

		//null过滤器测试
		methods = ReflectUtil.getMethods(ExamInfoDict.class, null);

		Assert.assertEquals(20, methods.length);
		final Method method2 = methods[0];
		Assert.assertNotNull(method2);
	}

	@Test
	public void getMethodTest() {
		Method method = ReflectUtil.getMethod(ExamInfoDict.class, "getId");
		Assert.assertEquals("getId", method.getName());
		Assert.assertEquals(0, method.getParameterTypes().length);

		method = ReflectUtil.getMethod(ExamInfoDict.class, "getId", Integer.class);
		Assert.assertEquals("getId", method.getName());
		Assert.assertEquals(1, method.getParameterTypes().length);
	}

	@Test
	public void getMethodIgnoreCaseTest() {
		Method method = ReflectUtil.getMethodIgnoreCase(ExamInfoDict.class, "getId");
		Assert.assertEquals("getId", method.getName());
		Assert.assertEquals(0, method.getParameterTypes().length);

		method = ReflectUtil.getMethodIgnoreCase(ExamInfoDict.class, "GetId");
		Assert.assertEquals("getId", method.getName());
		Assert.assertEquals(0, method.getParameterTypes().length);

		method = ReflectUtil.getMethodIgnoreCase(ExamInfoDict.class, "setanswerIs", Integer.class);
		Assert.assertEquals("setAnswerIs", method.getName());
		Assert.assertEquals(1, method.getParameterTypes().length);
	}

	@Test
	public void getFieldTest() {
		// 能够获取到父类字段
		Field privateField = ReflectUtil.getField(TestSubClass.class, "privateField");
		Assert.assertNotNull(privateField);
	}

	@Test
	public void getFieldsTest() {
		// 能够获取到父类字段
		final Field[] fields = ReflectUtil.getFields(TestSubClass.class);
		Assert.assertEquals(4, fields.length);
	}

	@Test
	public void setFieldTest() {
		TestClass testClass = new TestClass();
		ReflectUtil.setFieldValue(testClass, "a", "111");
		Assert.assertEquals(111, testClass.getA());
	}

	@Test
	public void invokeTest() {
		TestClass testClass = new TestClass();
		ReflectUtil.invoke(testClass, "setA", 10);
		Assert.assertEquals(10, testClass.getA());
	}

	@Test
	public void noneStaticInnerClassTest() {
		final NoneStaticClass testAClass = ReflectUtil.newInstanceIfPossible(NoneStaticClass.class);
		Assert.assertNotNull(testAClass);
		Assert.assertEquals(2, testAClass.getA());
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
	@Ignore
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

	public static Method getMethodWithReturnTypeCheck(Class<?> clazz, boolean ignoreCase, String methodName, Class<?>... paramTypes) throws SecurityException {
		if (null == clazz || StrUtil.isBlank(methodName)) {
			return null;
		}

		Method res = null;
		final Method[] methods = ReflectUtil.getMethods(clazz);
		if (ArrayUtil.isNotEmpty(methods)) {
			for (Method method : methods) {
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
		Assert.assertEquals(15, methods.length);

		// 排除Object中的方法
		// 3个方法包括类
		methods = ReflectUtil.getMethodsDirectly(C2.class, true, false);
		Assert.assertEquals(3, methods.length);

		// getA属于本类
		Assert.assertEquals("public void cn.hutool.core.util.ReflectUtilTest$C2.getA()", methods[0].toString());
		// getB属于父类
		Assert.assertEquals("public void cn.hutool.core.util.ReflectUtilTest$C1.getB()", methods[1].toString());
		// getC属于接口中的默认方法
		Assert.assertEquals("public default void cn.hutool.core.util.ReflectUtilTest$TestInterface1.getC()", methods[2].toString());
	}

	@Test
	public void getMethodsFromInterfaceTest() {
		// 对于接口，直接调用Class.getMethods方法获取所有方法，因为接口都是public方法
		// 因此此处得到包括TestInterface1、TestInterface2、TestInterface3中一共4个方法
		final Method[] methods = ReflectUtil.getMethods(TestInterface3.class);
		Assert.assertEquals(4, methods.length);

		// 接口里，调用getMethods和getPublicMethods效果相同
		final Method[] publicMethods = ReflectUtil.getPublicMethods(TestInterface3.class);
		Assert.assertArrayEquals(methods, publicMethods);
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
		int intValue = ReflectUtil.newInstanceIfPossible(int.class);
		Assert.assertEquals(0, intValue);

		Integer integer = ReflectUtil.newInstanceIfPossible(Integer.class);
		Assert.assertEquals(new Integer(0), integer);

		Map<?, ?> map = ReflectUtil.newInstanceIfPossible(Map.class);
		Assert.assertNotNull(map);

		Collection<?> collection = ReflectUtil.newInstanceIfPossible(Collection.class);
		Assert.assertNotNull(collection);

		Week week = ReflectUtil.newInstanceIfPossible(Week.class);
		Assert.assertEquals(Week.SUNDAY, week);

		int[] intArray = ReflectUtil.newInstanceIfPossible(int[].class);
		Assert.assertArrayEquals(new int[0], intArray);
	}
}
