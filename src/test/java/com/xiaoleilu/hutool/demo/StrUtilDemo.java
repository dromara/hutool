package com.xiaoleilu.hutool.demo;

import java.util.List;

import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.util.StrUtil;

public class StrUtilDemo {
	public static void main(String[] args) {
		
		//驼峰转下划线模式
		Console.log("-----------------------驼峰转下划线模式--------------------------------");
		String underlineCase = StrUtil.toUnderlineCase("HelloWorldCCDn");
		Console.log(underlineCase);
		Console.log();
		
		//切分字符串
		Console.log("-----------------------切分字符串--------------------------------");
		List<String> split = StrUtil.split("aaa-bbb ccc", '-', 3);
		for (String str : split) {
			Console.log(str);
		}
		Console.log();
		
		//格式化字符串
		Console.log("-----------------------格式化字符串--------------------------------");
		String param1 = "param1";
		int[] param2 = {1,2,3,4,5};
		String format = StrUtil.format("变量占位符：\\{}，这是第一个参数值：{}， 第二个参数值：{}", param1, param2);
		Console.log(format);
		Console.log();
		
		//字符串切分
		Console.log("-----------------------字符串切分--------------------------------");
		String str = "0123456789";
		Console.log(StrUtil.sub(str, 0, 100));
		Console.log(StrUtil.sub(str, 2, 3));
		Console.log(StrUtil.sub(str, -4, 0));
		Console.log();
	}
}
