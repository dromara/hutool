package com.xiaoleilu.hutool.demo;

import java.util.List;

import com.xiaoleilu.hutool.util.ObjectUtil;
import com.xiaoleilu.hutool.util.StrUtil;

public class StrUtilDemo {
	public static void main(String[] args) {
		
		//驼峰转下划线模式
		String underlineCase = StrUtil.toUnderlineCase("HelloWorldCCDn");
		System.out.println(underlineCase);
		
		//切分字符串
		List<String> split = StrUtil.split("aaa-bbb ccc", '-', 3);
		for (String str : split) {
			System.out.println(str);
		}
		
		//
		String param1 = "param1";
		int[] param2 = {1,2,3,4,5};
		String format = StrUtil.format("这是第一个参数值：{}， 第二个参数值：{}", param1, param2);
		System.out.println(format);
		
		int b = 0;
		System.out.println(ObjectUtil.isBasicType(b));
	}
}
