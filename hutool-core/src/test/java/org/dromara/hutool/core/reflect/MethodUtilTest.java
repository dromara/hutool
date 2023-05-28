package org.dromara.hutool.core.reflect;

import lombok.Data;
import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.date.StopWatch;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.lang.test.bean.ExamInfoDict;
import org.dromara.hutool.core.reflect.method.MethodUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.JdkUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class MethodUtilTest {

	private static final boolean isGteJdk15 = getJavaVersion() >= 15;
	/**
	 * jdk版本：是否>= jdk15
	 * jdk15: 删除了 object registerNatives
	 * @return 反馈jdk版本，如：7、8、11、15、17
	 * @author dazer
	 */
	private static int getJavaVersion() {
		return JdkUtil.JVM_VERSION;
	}

	@Test
	public void getMethodsTest() {
		Method[] methods = MethodUtil.getMethods(ExamInfoDict.class);
		Assertions.assertEquals(isGteJdk15 ? 19 : 20, methods.length);

		//过滤器测试
		methods = MethodUtil.getMethods(ExamInfoDict.class, t -> Integer.class.equals(t.getReturnType()));

		Assertions.assertEquals(4, methods.length);
		final Method method = methods[0];
		Assertions.assertNotNull(method);

		//null过滤器测试
		methods = MethodUtil.getMethods(ExamInfoDict.class, null);

		Assertions.assertEquals(isGteJdk15 ? 19 : 20, methods.length);
		final Method method2 = methods[0];
		Assertions.assertNotNull(method2);
	}

	@Test
	public void getMethodTest() {
		Method method = MethodUtil.getMethod(ExamInfoDict.class, "getId");
		Assertions.assertEquals("getId", method.getName());
		Assertions.assertEquals(0, method.getParameterTypes().length);

		method = MethodUtil.getMethod(ExamInfoDict.class, "getId", Integer.class);
		Assertions.assertEquals("getId", method.getName());
		Assertions.assertEquals(1, method.getParameterTypes().length);
	}

	@Test
	public void getMethodIgnoreCaseTest() {
		Method method = MethodUtil.getMethodIgnoreCase(ExamInfoDict.class, "getId");
		Assertions.assertEquals("getId", method.getName());
		Assertions.assertEquals(0, method.getParameterTypes().length);

		method = MethodUtil.getMethodIgnoreCase(ExamInfoDict.class, "GetId");
		Assertions.assertEquals("getId", method.getName());
		Assertions.assertEquals(0, method.getParameterTypes().length);

		method = MethodUtil.getMethodIgnoreCase(ExamInfoDict.class, "setanswerIs", Integer.class);
		Assertions.assertEquals("setAnswerIs", method.getName());
		Assertions.assertEquals(1, method.getParameterTypes().length);
	}

	@Test
	public void invokeTest() {
		final ReflectUtilTest.AClass testClass = new ReflectUtilTest.AClass();
		MethodUtil.invoke(testClass, "setA", 10);
		Assertions.assertEquals(10, testClass.getA());
	}

	@Test
	public void getDeclaredMethodsTest() {
		Class<?> type = ReflectUtilTest.TestBenchClass.class;
		Method[] methods = type.getDeclaredMethods();
		Assertions.assertArrayEquals(methods, MethodUtil.getDeclaredMethods(type));
		Assertions.assertSame(MethodUtil.getDeclaredMethods(type), MethodUtil.getDeclaredMethods(type));

		type = Object.class;
		methods = type.getDeclaredMethods();
		Assertions.assertArrayEquals(methods, MethodUtil.getDeclaredMethods(type));
	}

	@Test
	@Disabled
	public void getMethodBenchTest() {
		// 预热
		getMethodWithReturnTypeCheck(ReflectUtilTest.TestBenchClass.class, false, "getH");

		final StopWatch timer = new StopWatch();
		timer.start();
		for (int i = 0; i < 100000000; i++) {
			MethodUtil.getMethod(ReflectUtilTest.TestBenchClass.class, false, "getH");
		}
		timer.stop();
		Console.log(timer.getLastTaskTimeMillis());

		timer.start();
		for (int i = 0; i < 100000000; i++) {
			getMethodWithReturnTypeCheck(ReflectUtilTest.TestBenchClass.class, false, "getH");
		}
		timer.stop();
		Console.log(timer.getLastTaskTimeMillis());
	}

	@SuppressWarnings("UnusedReturnValue")
	public static Method getMethodWithReturnTypeCheck(final Class<?> clazz, final boolean ignoreCase, final String methodName, final Class<?>... paramTypes) throws SecurityException {
		if (null == clazz || StrUtil.isBlank(methodName)) {
			return null;
		}

		Method res = null;
		final Method[] methods = MethodUtil.getMethods(clazz);
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
		Method[] methods = MethodUtil.getMethods(ReflectUtilTest.C2.class);
		Assertions.assertEquals(isGteJdk15 ? 14 : 15, methods.length);

		// 排除Object中的方法
		// 3个方法包括类
		methods = MethodUtil.getMethodsDirectly(ReflectUtilTest.C2.class, true, false);
		Assertions.assertEquals(3, methods.length);

		// getA属于本类
		Assertions.assertEquals("public void org.dromara.hutool.core.reflect.ReflectUtilTest$C2.getA()", methods[0].toString());
		// getB属于父类
		Assertions.assertEquals("public void org.dromara.hutool.core.reflect.ReflectUtilTest$C1.getB()", methods[1].toString());
		// getC属于接口中的默认方法
		Assertions.assertEquals("public default void org.dromara.hutool.core.reflect.ReflectUtilTest$TestInterface1.getC()", methods[2].toString());
	}

	@Test
	public void getMethodsFromInterfaceTest() {
		// 对于接口，直接调用Class.getMethods方法获取所有方法，因为接口都是public方法
		// 因此此处得到包括TestInterface1、TestInterface2、TestInterface3中一共4个方法
		final Method[] methods = MethodUtil.getMethods(ReflectUtilTest.TestInterface3.class);
		Assertions.assertEquals(4, methods.length);

		// 接口里，调用getMethods和getPublicMethods效果相同
		final Method[] publicMethods = MethodUtil.getPublicMethods(ReflectUtilTest.TestInterface3.class);
		Assertions.assertArrayEquals(methods, publicMethods);
	}

	@Test
	public void getPublicMethod() {
		final Method superPublicMethod = MethodUtil.getPublicMethod(ReflectUtilTest.TestSubClass.class, "publicMethod");
		Assertions.assertNotNull(superPublicMethod);
		final Method superPrivateMethod = MethodUtil.getPublicMethod(ReflectUtilTest.TestSubClass.class, "privateMethod");
		Assertions.assertNull(superPrivateMethod);

		final Method publicMethod = MethodUtil.getPublicMethod(ReflectUtilTest.TestSubClass.class, "publicSubMethod");
		Assertions.assertNotNull(publicMethod);
		final Method privateMethod = MethodUtil.getPublicMethod(ReflectUtilTest.TestSubClass.class, "privateSubMethod");
		Assertions.assertNull(privateMethod);
	}

	@Test
	public void getDeclaredMethod() {
		final Method noMethod = MethodUtil.getMethod(ReflectUtilTest.TestSubClass.class, "noMethod");
		Assertions.assertNull(noMethod);

		final Method privateMethod = MethodUtil.getMethod(ReflectUtilTest.TestSubClass.class, "privateMethod");
		Assertions.assertNotNull(privateMethod);
		final Method publicMethod = MethodUtil.getMethod(ReflectUtilTest.TestSubClass.class, "publicMethod");
		Assertions.assertNotNull(publicMethod);

		final Method publicSubMethod = MethodUtil.getMethod(ReflectUtilTest.TestSubClass.class, "publicSubMethod");
		Assertions.assertNotNull(publicSubMethod);
		final Method privateSubMethod = MethodUtil.getMethod(ReflectUtilTest.TestSubClass.class, "privateSubMethod");
		Assertions.assertNotNull(privateSubMethod);
	}

	@Test
	public void issue2625Test(){
		// 内部类继承的情况下父类方法会被定义为桥接方法，因此按照pr#1965@Github判断返回值的继承关系来代替判断桥接。
		final Method getThis = MethodUtil.getMethod(A.C.class, "getThis");
		Assertions.assertTrue(getThis.isBridge());
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

	@Data
	static class TestClass {
		private int a;
	}

	@Test
	public void invokeMethodTest() {
		final TestClass testClass = new TestClass();
		final Method method = MethodUtil.getMethod(TestClass.class, "setA", int.class);
		MethodUtil.invoke(testClass, method, 10);
		Assertions.assertEquals(10, testClass.getA());
	}

	@Test
	public void invokeMethodWithParamConvertTest() {
		final TestClass testClass = new TestClass();
		final Method method = MethodUtil.getMethod(TestClass.class, "setA", int.class);
		MethodUtil.invoke(testClass, method, "10");
		Assertions.assertEquals(10, testClass.getA());
	}

	@Test
	public void invokeMethodWithParamConvertFailedTest() {
		final TestClass testClass = new TestClass();
		final Method method = MethodUtil.getMethod(TestClass.class, "setA", int.class);
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> MethodUtil.invoke(testClass, method, "aaa"));
	}
}
