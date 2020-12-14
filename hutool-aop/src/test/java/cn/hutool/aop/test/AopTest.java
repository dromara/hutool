package cn.hutool.aop.test;

import cn.hutool.aop.ProxyUtil;
import cn.hutool.aop.aspects.TimeIntervalAspect;
import cn.hutool.core.lang.Console;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

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
	public void aopByAutoCglibTest() {
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

	@Test
	public void testCGLIBProxy() {
		TagObj target = new TagObj();
		//目标类设置标记
		target.setTag("tag");

		TagObj proxy = ProxyUtil.proxy(target, TimeIntervalAspect.class);
		//代理类获取标记tag (断言错误)
		Assert.assertEquals("tag", proxy.getTag());
	}

	@Data
	public static class TagObj{
		private String tag;
	}
}
