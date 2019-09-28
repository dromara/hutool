package cn.hutool.aop.test;

import cn.hutool.core.lang.Console;
import org.junit.Assert;
import org.junit.Test;

import cn.hutool.aop.ProxyUtil;
import cn.hutool.aop.aspects.TimeIntervalAspect;

/**
 * AOP模块单元测试
 *
 * @author Looly
 */
public class AopTest {

	@Test
	public void aopTest() {
		Animal cat = ProxyUtil.proxy(new Cat(), TimeIntervalAspect.class);
		String result = cat.eat();
		Assert.assertEquals("猫吃鱼", result);
		cat.seize();
	}

	@Test
	public void aopByCglibTest() {
		Dog dog = ProxyUtil.proxy(new Dog(), TimeIntervalAspect.class);
		String result = dog.eat();
		Assert.assertEquals("狗吃肉", result);
		dog.seize();
	}

	interface Animal {
		String eat();

		void seize();
	}

	/**
	 * 有接口
	 *
	 * @author looly
	 */
	static class Cat implements Animal {

		@Override
		public String eat() {
			return "猫吃鱼";
		}

		@Override
		public void seize() {
			Console.log("抓了条鱼");
		}
	}

	/**
	 * 无接口
	 *
	 * @author looly
	 */
	static class Dog {
		public String eat() {
			return "狗吃肉";
		}

		public void seize() {
            Console.log("抓了只鸡");
		}
	}
}
