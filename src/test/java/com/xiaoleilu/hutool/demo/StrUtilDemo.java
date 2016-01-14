package com.xiaoleilu.hutool.demo;

import java.util.List;

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
	}
}
