/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.aop;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.extra.aop.aspects.TimeIntervalAspect;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * AOP模块单元测试
 *
 * @author Looly
 */
public class AopTest {

	@Test
	public void aopTest() {
		final Animal cat = ProxyUtil.proxy(new Cat(), TimeIntervalAspect.class);
		final String result = cat.eat();
		Assertions.assertEquals("猫吃鱼", result);
		cat.seize();
	}

	@Test
	public void aopByAutoCglibTest() {
		final Dog dog = ProxyUtil.proxy(new Dog(), TimeIntervalAspect.class);
		final String result = dog.eat();
		Assertions.assertEquals("狗吃肉", result);

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
		final TagObj target = new TagObj();
		//目标类设置标记
		target.setTag("tag");

		final TagObj proxy = ProxyUtil.proxy(target, TimeIntervalAspect.class);
		//代理类获取标记tag (断言错误)
		Assertions.assertEquals("tag", proxy.getTag());
	}

	@Data
	public static class TagObj{
		private String tag;
	}
}
