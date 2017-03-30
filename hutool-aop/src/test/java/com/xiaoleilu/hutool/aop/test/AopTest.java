package com.xiaoleilu.hutool.aop.test;

import org.junit.Test;

import com.xiaoleilu.hutool.aop.ProxyUtil;
import com.xiaoleilu.hutool.aop.aspects.TimeIntervalAspect;
import com.xiaoleilu.hutool.lang.Console;

/**
 * AOP模块单元测试
 * @author Looly
 *
 */
public class AopTest {
	
	@Test
	public void aopTest(){
		Animal cat = ProxyUtil.proxy(new Cat(), TimeIntervalAspect.class);
		cat.eat();
	}
	
	static interface Animal{
		void eat();
	}
	
	static class Cat implements Animal{

		@Override
		public void eat() {
			Console.log("猫吃鱼");
		}
		
	}
}
