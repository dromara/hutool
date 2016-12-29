package com.xiaoleilu.hutool.demo;

import java.util.Date;

import com.xiaoleilu.hutool.date.DateTime;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.lang.Console;

/**
 * 日期工具Demo
 * @author Looly
 *
 */
public class DateUtilDemo {
	public static void main(String[] args) {
		//字符串转日期
		DateTime dateTime = DateUtil.parse("2015-03-25 12:04:23.0");
		Console.log(dateTime);
		
		DateTime date = DateUtil.date(1420774989);
		Console.log(date);
		
		String dateStr = DateUtil.date().toString();
		Console.log(dateStr);
		
		String datetimeStr = DateUtil.formatDateTime(new Date());
		Console.log(datetimeStr);
		
		//计算年龄
		int age = DateUtil.ageOfNow(DateUtil.parse("1988-12-23"));
		Console.log("Age of me: {}", age);
		
		//DateTime对象
		DateTime now = DateTime.now();
		int year = now.year();
		int month = now.month();
		int dayOfMonth = now.dayOfMonth();
		boolean leapYear = now.isLeapYear();
		Console.log("year: {}, month: {}, dayOfMonth: {}, isLeepYear: {}", year, month, dayOfMonth, leapYear);
	}
}
