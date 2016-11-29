package com.xiaoleilu.hutool.test;

import com.xiaoleilu.hutool.convert.Convert;

/**
 * 仅用于临时测试
 * 
 * @author Looly
 *
 */
public class Test {
	public static void main(String[] args) {
		Object value = 1;
		Double double1 = Convert.convert(double.class, value, 0D);
		System.out.println(double1);
	}
}
