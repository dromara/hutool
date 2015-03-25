package com.xiaoleilu.hutool.demo;

import java.util.Set;

import com.xiaoleilu.hutool.ClassUtil;
import com.xiaoleilu.hutool.DateTime;
import com.xiaoleilu.hutool.StrUtil;

/**
 * ClassUtil Demo
 * @author Looly
 *
 */
public class ClassUtilDemo {
	public static void main(String[] args) throws ClassNotFoundException {
		//获得指定包下所有类
		Set<Class<?>> packageSet = ClassUtil.scanPackage("com.xiaoleilu");
		for (Class<?> clazz : packageSet) {
			System.out.println(clazz);
		}
		
		//转换包装类型为基本类型
		Class<?> primitive = ClassUtil.castToPrimitive(Integer.class);
		System.out.println(primitive);
		
		//获得当前的ClassLoader
		ClassLoader classLoader = ClassUtil.getClassLoader();
		System.out.println(classLoader);
		
		//获得ClassPath
		Set<String> resources = ClassUtil.getClassPathResources();
		for (String resource : resources) {
			System.out.println(resource);
		}
		
		//克隆对象（当这个对象没有实现Cloneable接口时）
		DateTime dateTime1 = new DateTime();
		DateTime dateTime2 = ClassUtil.cloneObj(dateTime1);
		System.out.println(StrUtil.format("Is dateTime1 equals dateTime2: {}", dateTime1 == dateTime2));
	}
}
