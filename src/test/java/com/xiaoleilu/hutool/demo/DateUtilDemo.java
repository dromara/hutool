package com.xiaoleilu.hutool.demo;

import java.util.Date;

import com.xiaoleilu.hutool.date.DateTime;
import com.xiaoleilu.hutool.date.DateUtil;

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
		
		String dateStr = DateUtil.date().toString();
		System.out.println(dateStr);
		
		String datetimeStr = DateUtil.formatDateTime(new Date());
		System.out.println(datetimeStr);
		
		//计算年龄
		int age = DateUtil.ageOfNow(DateUtil.parse("1988-12-23"));
		System.out.println(age);
	}
}
