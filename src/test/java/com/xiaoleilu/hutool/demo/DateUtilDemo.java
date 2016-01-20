package com.xiaoleilu.hutool.demo;

import com.xiaoleilu.hutool.lang.DateTime;
import com.xiaoleilu.hutool.util.DateUtil;

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
		
		DateTime date = DateUtil.date(1420774989);
		System.out.println(date.toString());
	}
}
