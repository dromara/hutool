package cn.hutool.core.reflect;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.test.bean.ExamInfoDict;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.SystemUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Method;

public class MethodUtilTest {
	private static final String JAVA_VERSION = SystemUtil.get("java.version", false);
	private static final boolean isGteJdk15 = getJavaVersion() >= 15;
	/**
	 * jdk版本：是否>= jdk15
	 * jdk15: 删除了 object registerNatives
	 * @return 反馈jdk版本，如：7、8、11、15、17
	 * @author dazer
	 */
	private static int getJavaVersion() {
		if (JAVA_VERSION.startsWith("1.")) {
			return Integer.parseInt(JAVA_VERSION.split("\\.")[1]);
		}
		return Integer.parseInt(JAVA_VERSION.split("\\.")[0]);
	}

	@Test
	public void getMethodsTest() {
		Method[] methods = MethodUtil.getMethods(ExamInfoDict.class);
		Assert.assertEquals(isGteJdk15 ? 19 : 20, methods.length);

		//过滤器测试
		methods = MethodUtil.getMethods(ExamInfoDict.class, t -> Integer.class.equals(t.getReturnType()));

		Assert.assertEquals(4, methods.length);
		final Method method = methods[0];
		Assert.assertNotNull(method);

		//null过滤器测试
		methods = MethodUtil.getMethods(ExamInfoDict.class, null);

		Assert.assertEquals(isGteJdk15 ? 19 : 20, methods.length);
		final Method method2 = methods[0];
		Assert.assertNotNull(method2);
	}

	@Test
	public void getMethodTest() {
		Method method = MethodUtil.getMethod(ExamInfoDict.class, "getId");
		Assert.assertEquals("getId", method.getName());
		Assert.assertEquals(0, method.getParameterTypes().length);

		method = MethodUtil.getMethod(ExamInfoDict.class, "getId", Integer.class);
		Assert.assertEquals("getId", method.getName());
		Assert.assertEquals(1, method.getParameterTypes().length);
	}

	@Test
	public void getMethodIgnoreCaseTest() {
		Method method = MethodUtil.getMethodIgnoreCase(ExamInfoDict.class, "getId");
		Assert.assertEquals("getId", method.getName());
		Assert.assertEquals(0, method.getParameterTypes().length);

		method = MethodUtil.getMethodIgnoreCase(ExamInfoDict.class, "GetId");
		Assert.assertEquals("getId", method.getName());
		Assert.assertEquals(0, method.getParameterTypes().length);

		method = MethodUtil.getMethodIgnoreCase(ExamInfoDict.class, "setanswerIs", Integer.class);
		Assert.assertEquals("setAnswerIs", method.getName());
		Assert.assertEquals(1, method.getParameterTypes().length);
	}

	@Test
	public void invokeTest() {
		final ReflectUtilTest.AClass testClass = new ReflectUtilTest.AClass();
		MethodUtil.invoke(testClass, "setA", 10);
		Assert.assertEquals(10, testClass.getA());
	}

	@Test
	@Ignore
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
		Assert.assertEquals(isGteJdk15 ? 14 : 15, methods.length);

		// 排除Object中的方法
		// 3个方法包括类
		methods = MethodUtil.getMethodsDirectly(ReflectUtilTest.C2.class, true, false);
		Assert.assertEquals(3, methods.length);

		// getA属于本类
		Assert.assertEquals("public void cn.hutool.core.reflect.ReflectUtilTest$C2.getA()", methods[0].toString());
		// getB属于父类
		Assert.assertEquals("public void cn.hutool.core.reflect.ReflectUtilTest$C1.getB()", methods[1].toString());
		// getC属于接口中的默认方法
		Assert.assertEquals("public default void cn.hutool.core.reflect.ReflectUtilTest$TestInterface1.getC()", methods[2].toString());
	}

	@Test
	public void getMethodsFromInterfaceTest() {
		// 对于接口，直接调用Class.getMethods方法获取所有方法，因为接口都是public方法
		// 因此此处得到包括TestInterface1、TestInterface2、TestInterface3中一共4个方法
		final Method[] methods = MethodUtil.getMethods(ReflectUtilTest.TestInterface3.class);
		Assert.assertEquals(4, methods.length);

		// 接口里，调用getMethods和getPublicMethods效果相同
		final Method[] publicMethods = MethodUtil.getPublicMethods(ReflectUtilTest.TestInterface3.class);
		Assert.assertArrayEquals(methods, publicMethods);
	}

	@Test
	public void getPublicMethod() {
		final Method superPublicMethod = MethodUtil.getPublicMethod(ReflectUtilTest.TestSubClass.class, "publicMethod");
		Assert.assertNotNull(superPublicMethod);
		final Method superPrivateMethod = MethodUtil.getPublicMethod(ReflectUtilTest.TestSubClass.class, "privateMethod");
		Assert.assertNull(superPrivateMethod);

		final Method publicMethod = MethodUtil.getPublicMethod(ReflectUtilTest.TestSubClass.class, "publicSubMethod");
		Assert.assertNotNull(publicMethod);
		final Method privateMethod = MethodUtil.getPublicMethod(ReflectUtilTest.TestSubClass.class, "privateSubMethod");
		Assert.assertNull(privateMethod);
	}

	@Test
	public void getDeclaredMethod() {
		final Method noMethod = MethodUtil.getMethod(ReflectUtilTest.TestSubClass.class, "noMethod");
		Assert.assertNull(noMethod);

		final Method privateMethod = MethodUtil.getMethod(ReflectUtilTest.TestSubClass.class, "privateMethod");
		Assert.assertNotNull(privateMethod);
		final Method publicMethod = MethodUtil.getMethod(ReflectUtilTest.TestSubClass.class, "publicMethod");
		Assert.assertNotNull(publicMethod);

		final Method publicSubMethod = MethodUtil.getMethod(ReflectUtilTest.TestSubClass.class, "publicSubMethod");
		Assert.assertNotNull(publicSubMethod);
		final Method privateSubMethod = MethodUtil.getMethod(ReflectUtilTest.TestSubClass.class, "privateSubMethod");
		Assert.assertNotNull(privateSubMethod);

	}
}
