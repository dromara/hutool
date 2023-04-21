package org.dromara.hutool.core.reflect;

import org.dromara.hutool.core.classloader.ClassLoaderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MethodHandleUtilTest {

	@Test
	public void invokeDefaultTest(){
		final Duck duck = (Duck) Proxy.newProxyInstance(
				ClassLoaderUtil.getClassLoader(),
				new Class[] { Duck.class },
				MethodHandleUtil::invoke);

		Assertions.assertEquals("Quack", duck.quack());

		// 测试子类执行default方法
		final Method quackMethod = MethodUtil.getMethod(Duck.class, "quack");
		String quack = MethodHandleUtil.invoke(new BigDuck(), quackMethod);
		Assertions.assertEquals("Quack", quack);

		// 测试反射执行默认方法
		quack = MethodUtil.invoke(new Duck() {}, quackMethod);
		Assertions.assertEquals("Quack", quack);
	}

	@Test
	public void invokeDefaultByReflectTest(){
		final Duck duck = (Duck) Proxy.newProxyInstance(
				ClassLoaderUtil.getClassLoader(),
				new Class[] { Duck.class },
				MethodUtil::invoke);

		Assertions.assertEquals("Quack", duck.quack());
	}

	@Test
	public void invokeStaticByProxyTest(){
		final Duck duck = (Duck) Proxy.newProxyInstance(
				ClassLoaderUtil.getClassLoader(),
				new Class[] { Duck.class },
				MethodUtil::invoke);

		Assertions.assertEquals("Quack", duck.quack());
	}

	@Test
	public void invokeTest(){
		// 测试执行普通方法
		final int size = MethodHandleUtil.invoke(new BigDuck(),
				MethodUtil.getMethod(BigDuck.class, "getSize"));
		Assertions.assertEquals(36, size);
	}

	@Test
	public void invokeStaticTest(){
		// 测试执行普通方法
		final String result = MethodHandleUtil.invoke(null,
				MethodUtil.getMethod(Duck.class, "getDuck", int.class), 78);
		Assertions.assertEquals("Duck 78", result);
	}

	interface Duck {
		default String quack() {
			return "Quack";
		}

		static String getDuck(final int count){
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
