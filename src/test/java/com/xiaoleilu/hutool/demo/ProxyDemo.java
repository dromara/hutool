package com.xiaoleilu.hutool.demo;

import java.lang.reflect.Method;

import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.proxy.Aspect;
import com.xiaoleilu.hutool.proxy.ProxyUtil;
import com.xiaoleilu.hutool.proxy.aspects.SimpleAspect;
import com.xiaoleilu.hutool.proxy.aspects.TimeIntervalAspect;

/**
 * 切面样例
 * @author Looly
 *
 */
public class ProxyDemo {
	public static void main(String[] args) {
		B b = new B();
		Console.log("--------------------------------------- 切面代理使用方式1");
		A a = ProxyUtil.proxy(new ExampleAspect(b));
		a.doSomeThing();
		
		Console.log("--------------------------------------- 切面代理使用方式2");
		a = ProxyUtil.proxy(b, ExampleAspect.class);
		a.doSomeThing();
		
		//SimpleAspect是一个简单切面类，无任何操作，可以继承此类实现需要的方法
		Console.log("--------------------------------------- SimpleAspect");
		a = ProxyUtil.proxy(b, SimpleAspect.class);
		a.doSomeThing();
		
		//TimeIntervalAspect用于打印方法执行时间的日志的切面
		Console.log("--------------------------------------- TimeIntervalAspect");
		a = ProxyUtil.proxy(b, TimeIntervalAspect.class);
		a.doSomeThing();
	}
	
	//----------------------------------------------------------
	public static interface A{
		public String doSomeThing();
	}
	
	public static class B implements A{

		@Override
		public String doSomeThing() {
			Console.log("Do Some Things");
			return "OK";
		}
		
	}
	
	public static class ExampleAspect extends Aspect{

		public ExampleAspect(Object target) {
			super(target);
		}
		
		@Override
		public boolean before(Object target, Method method, Object[] args) {
			Console.log("=== I am before target method [{}] invoke.", method.getName());
			return true;
		}
		
		@Override
		public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
			Console.log("=== I am after target throws exception.");
			return true;
		}
		
		@Override
		public boolean after(Object target, Method method, Object[] args) {
			Console.log("=== I am after target [{}] method [{}] invoke.", target.getClass().getName(), method.getName());
			return true;
		}
	}
}
