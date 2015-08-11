package com.xiaoleilu.hutool.demo;

import com.xiaoleilu.hutool.DateTime;
import com.xiaoleilu.hutool.DateUtil;

/**
 * 日期工具Demo
 * @author Looly
 *
 */
public class DateUtilDemo {
	public static void main(String[] args) {
		//字符串转日期
		DateTime dateTime = DateUtil.parse("2015-03-25 12:04:23.0");
		System.out.println(dateTime);
	}
}
