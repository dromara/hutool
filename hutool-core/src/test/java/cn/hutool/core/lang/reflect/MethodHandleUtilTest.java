package cn.hutool.core.lang.reflect;

import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.ReflectUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MethodHandleUtilTest {

	@Test
	public void invokeDefaultTest(){
		Duck duck = (Duck) Proxy.newProxyInstance(
				ClassLoaderUtil.getClassLoader(),
				new Class[] { Duck.class },
				MethodHandleUtil::invoke);

		Assert.assertEquals("Quack", duck.quack());

		// 测试子类执行default方法
		final Method quackMethod = ReflectUtil.getMethod(Duck.class, "quack");
		String quack = MethodHandleUtil.invoke(new BigDuck(), quackMethod);
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
	public void invokeTest(){
		// 测试执行普通方法
		final int size = MethodHandleUtil.invoke(new BigDuck(),
				ReflectUtil.getMethod(BigDuck.class, "getSize"));
		Assert.assertEquals(36, size);
	}

	interface Duck {
		default String quack() {
			return "Quack";
		}
	}

	static class BigDuck implements Duck{
		public int getSize(){
			return 36;
		}
	}
}
