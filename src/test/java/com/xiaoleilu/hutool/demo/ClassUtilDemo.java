package com.xiaoleilu.hutool.demo;

import java.util.Set;

import com.xiaoleilu.hutool.ClassUtil;

/**
 * ClassUtil Demo
 * @author Looly
 *
 */
public class ClassUtilDemo {
	public static void main(String[] args) throws ClassNotFoundException {
		//获得指定包下所有类
//		Set<Class<?>> packageSet = ClassUtil.scanPackage("");
//		for (Class<?> clazz : packageSet) {
//			System.out.println(clazz);
//		}
		
		Class<?> primitive = ClassUtil.castToPrimitive(Integer.class);
		System.out.println(primitive);
	}
}
