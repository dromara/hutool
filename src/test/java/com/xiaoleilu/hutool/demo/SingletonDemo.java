package com.xiaoleilu.hutool.demo;

import com.xiaoleilu.hutool.Singleton;

/**
 * 单例样例
 * @author loolly
 *
 */
public class SingletonDemo {
	
	/**
	 * 动物接口
	 * @author loolly
	 *
	 */
	public static interface Animal{
		public void say();
	}
	
	/**
	 * 狗实现
	 * @author loolly
	 *
	 */
	public static class Dog implements Animal{
		@Override
		public void say() {
			System.out.println("汪汪");
		}
	}
	
	/**
	 * 猫实现
	 * @author loolly
	 *
	 */
	public static class Cat implements Animal{
		@Override
		public void say() {
			System.out.println("喵喵");
		}
	}
	
	public static void main(String[] args) {
		Animal dog = Singleton.get(Dog.class);
		Animal cat = Singleton.get(Cat.class);
		
		//单例对象每次取出为同一个对象，除非调用Singleton.destroy()或者remove方法
		System.out.println(dog == Singleton.get(Dog.class));		//True
		System.out.println(cat == Singleton.get(Cat.class));			//True
		
		dog.say();		//汪汪
		cat.say();		//喵喵
	}
}
