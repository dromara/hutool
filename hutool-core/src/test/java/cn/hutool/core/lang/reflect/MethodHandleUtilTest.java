package cn.hutool.core.lang.reflect;

import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.ReflectUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MethodHandleUtilTest {

	@Test
	public void invokeDefaultTest(){
		Duck duck = (Duck) Proxy.newProxyInstance(
				ClassLoaderUtil.getClassLoader(),
				new Class[] { Duck.class },
				MethodHandleUtil::invokeSpecial);

		Assert.assertEquals("Quack", duck.quack());

		// 测试子类执行default方法
		final Method quackMethod = ReflectUtil.getMethod(Duck.class, "quack");
		String quack = MethodHandleUtil.invokeSpecial(new BigDuck(), quackMethod);
		Assert.assertEquals("Quack", quack);

		// 测试反射执行默认方法
		quack = ReflectUtil.invoke(new Duck() {}, quackMethod);
		Assert.assertEquals("Quack", quack);
	}

	@Test
	public void invokeDefaultByReflectTest(){
		Duck duck = (Duck) Proxy.newProxyInstance(
				ClassLoaderUtil.getClassLoader(),
				new Class[] { Duck.class },
				ReflectUtil::invoke);

		Assert.assertEquals("Quack", duck.quack());
	}

	@Test
	public void invokeStaticByProxyTest(){
		Duck duck = (Duck) Proxy.newProxyInstance(
				ClassLoaderUtil.getClassLoader(),
				new Class[] { Duck.class },
				ReflectUtil::invoke);

		Assert.assertEquals("Quack", duck.quack());
	}

	@Test
	public void invokeTest(){
		// 测试执行普通方法
		final int size = MethodHandleUtil.invokeSpecial(new BigDuck(),
				ReflectUtil.getMethod(BigDuck.class, "getSize"));
		Assert.assertEquals(36, size);
	}

	@Test
	public void invokeStaticTest(){
		// 测试执行普通方法
		final String result = MethodHandleUtil.invoke(null,
				ReflectUtil.getMethod(Duck.class, "getDuck", int.class), 78);
		Assert.assertEquals("Duck 78", result);
	}

	@Test
	public void findMethodTest() throws Throwable {
		MethodHandle handle = MethodHandleUtil.findMethod(Duck.class, "quack",
				MethodType.methodType(String.class));
		Assert.assertNotNull(handle);
		// 对象方法自行需要绑定对象或者传入对象参数
		String invoke = (String) handle.invoke(new BigDuck());
		Assert.assertEquals("Quack", invoke);

		// 对象的方法获取
		handle = MethodHandleUtil.findMethod(BigDuck.class, "getSize",
				MethodType.methodType(int.class));
		Assert.assertNotNull(handle);
		int invokeInt = (int) handle.invoke(new BigDuck());
		Assert.assertEquals(36, invokeInt);
	}

	@Test
	public void findStaticMethodTest() throws Throwable {
		final MethodHandle handle = MethodHandleUtil.findMethod(Duck.class, "getDuck",
				MethodType.methodType(String.class, int.class));
		Assert.assertNotNull(handle);

		// static 方法执行不需要绑定或者传入对象，直接传入参数即可
		final String invoke = (String) handle.invoke(12);
		Assert.assertEquals("Duck 12", invoke);
	}

	@Test
	public void findPrivateMethodTest() throws Throwable {
		final MethodHandle handle = MethodHandleUtil.findMethod(BigDuck.class, "getPrivateValue",
				MethodType.methodType(String.class));
		Assert.assertNotNull(handle);

		final String invoke = (String) handle.invoke(new BigDuck());
		Assert.assertEquals("private value", invoke);
	}

	@Test
	public void findSuperMethodTest() throws Throwable {
		// 查找父类的方法
		final MethodHandle handle = MethodHandleUtil.findMethod(BigDuck.class, "quack",
				MethodType.methodType(String.class));
		Assert.assertNotNull(handle);

		final String invoke = (String) handle.invoke(new BigDuck());
		Assert.assertEquals("Quack", invoke);
	}

	@Test
	public void findPrivateStaticMethodTest() throws Throwable {
		final MethodHandle handle = MethodHandleUtil.findMethod(BigDuck.class, "getPrivateStaticValue",
				MethodType.methodType(String.class));
		Assert.assertNotNull(handle);

		final String invoke = (String) handle.invoke();
		Assert.assertEquals("private static value", invoke);
	}

	interface Duck {
		default String quack() {
			return "Quack";
		}

		static String getDuck(int count){
			return "Duck " + count;
		}
	}

	static class BigDuck implements Duck{
		public int getSize(){
			return 36;
		}

		@SuppressWarnings("unused")
		private String getPrivateValue(){
			return "private value";
		}

		@SuppressWarnings("unused")
		private static String getPrivateStaticValue(){
			return "private static value";
		}
	}
}
