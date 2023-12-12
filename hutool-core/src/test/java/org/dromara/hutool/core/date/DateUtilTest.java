/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.date;

import org.dromara.hutool.core.date.format.FastDateFormat;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 时间工具单元测试<br>
 * 此单元测试依赖时区为中国+08:00
 *
 * <pre>
 * export TZ=Asia/Shanghai
 * </pre>
 *
 * @author Looly
 */
public class DateUtilTest {

	@Test
	public void nowTest() {
		// 当前时间
		final Date date = DateUtil.now();
		Assertions.assertNotNull(date);
		// 当前时间
		final Date date2 = DateUtil.date(Calendar.getInstance());
		Assertions.assertNotNull(date2);
		// 当前时间
		final Date date3 = DateUtil.date(System.currentTimeMillis());
		Assertions.assertNotNull(date3);

		// 当前日期字符串，格式：yyyy-MM-dd HH:mm:ss
		final String now = DateUtil.formatNow();
		Assertions.assertNotNull(now);
		// 当前日期字符串，格式：yyyy-MM-dd
		final String today = DateUtil.formatToday();
		Assertions.assertNotNull(today);
	}

	@Test
	public void todayTest() {
		final String s = DateUtil.today().toString();
		Assertions.assertTrue(s.endsWith("00:00:00"));
	}

	@Test
	public void formatAndParseTest() {
		final String dateStr = "2017-03-01";
		final Date date = DateUtil.parse(dateStr);

		final String format = DateUtil.format(date, "yyyy/MM/dd");
		Assertions.assertEquals("2017/03/01", format);

		// 常用格式的格式化
		final String formatDate = DateUtil.formatDate(date);
		Assertions.assertEquals("2017-03-01", formatDate);
		final String formatDateTime = DateUtil.formatDateTime(date);
		Assertions.assertEquals("2017-03-01 00:00:00", formatDateTime);
		final String formatTime = DateUtil.formatTime(date);
		Assertions.assertEquals("00:00:00", formatTime);
	}

	@Test
	public void formatAndParseCustomTest() {
		final String dateStr = "2017-03-01";
		final Date date = DateUtil.parse(dateStr);

		final String format = DateUtil.format(date, "#sss");
		Assertions.assertEquals("1488297600", format);

		final DateTime parse = DateUtil.parse(format, "#sss");
		Assertions.assertEquals(date, parse);
	}

	@Test
	public void formatAndParseCustomTest2() {
		final String dateStr = "2017-03-01";
		final Date date = DateUtil.parse(dateStr);

		final String format = DateUtil.format(date, "#SSS");
		Assertions.assertEquals("1488297600000", format);

		final DateTime parse = DateUtil.parse(format, "#SSS");
		Assertions.assertEquals(date, parse);
	}

	@Test
	public void beginAndEndTest() {
		final String dateStr = "2017-03-01 00:33:23";
		final Date date = DateUtil.parse(dateStr);

		// 一天的开始
		final Date beginOfDay = DateUtil.beginOfDay(date);
		Assertions.assertEquals("2017-03-01 00:00:00", beginOfDay.toString());
		// 一天的结束
		final Date endOfDay = DateUtil.endOfDay(date);
		Assertions.assertEquals("2017-03-01 23:59:59", endOfDay.toString());
	}

	@Test
	public void endOfDayTest() {
		final DateTime parse = DateUtil.parse("2020-05-31 00:00:00");
		Assertions.assertEquals("2020-05-31 23:59:59", DateUtil.endOfDay(parse).toString());
	}

	@Test
	public void truncateTest() {
		final String dateStr2 = "2020-02-29 12:59:34";
		final Date date2 = DateUtil.parse(dateStr2);
		final DateTime dateTime = DateUtil.truncate(date2, DateField.MINUTE);
		Assertions.assertEquals("2020-02-29 12:59:00", dateTime.toString());
	}

	@Test
	public void ceilingMinuteTest() {
		final String dateStr2 = "2020-02-29 12:59:34";
		final Date date2 = DateUtil.parse(dateStr2);


		DateTime dateTime = DateUtil.ceiling(date2, DateField.MINUTE);
		Assertions.assertEquals("2020-02-29 12:59:59.999", dateTime.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		dateTime = DateUtil.ceiling(date2, DateField.MINUTE, true);
		Assertions.assertEquals("2020-02-29 12:59:59.000", dateTime.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
	}

	@Test
	public void ceilingDayTest() {
		final String dateStr2 = "2020-02-29 12:59:34";
		final Date date2 = DateUtil.parse(dateStr2);


		DateTime dateTime = DateUtil.ceiling(date2, DateField.DAY_OF_MONTH);
		Assertions.assertEquals("2020-02-29 23:59:59.999", dateTime.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		dateTime = DateUtil.ceiling(date2, DateField.DAY_OF_MONTH, true);
		Assertions.assertEquals("2020-02-29 23:59:59.000", dateTime.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
	}

	@Test
	public void beginOfWeekTest() {
		final String dateStr = "2017-03-01 22:33:23";
		final DateTime date = DateUtil.parse(dateStr);
		Objects.requireNonNull(date).setFirstDayOfWeek(Week.MONDAY);

		// 一周的开始
		final Date beginOfWeek = DateUtil.beginOfWeek(date);
		Assertions.assertEquals("2017-02-27 00:00:00", beginOfWeek.toString());
		// 一周的结束
		final Date endOfWeek = DateUtil.endOfWeek(date);
		Assertions.assertEquals("2017-03-05 23:59:59", endOfWeek.toString());

		final Calendar calendar = DateUtil.calendar(date);
		// 一周的开始
		final Calendar begin = DateUtil.beginOfWeek(calendar);
		Assertions.assertEquals("2017-02-27 00:00:00", DateUtil.date(begin).toString());
		// 一周的结束
		final Calendar end = DateUtil.endOfWeek(calendar);
		Assertions.assertEquals("2017-03-05 23:59:59", DateUtil.date(end).toString());
	}

	@Test
	public void beginOfWeekTest2() {
		final String beginStr = "2020-03-11";
		final DateTime date = DateUtil.parse(beginStr);
		final Calendar calendar = Objects.requireNonNull(date).toCalendar();
		final Calendar begin = DateUtil.beginOfWeek(calendar, false);
		Assertions.assertEquals("2020-03-08 00:00:00", DateUtil.date(begin).toString());

		final Calendar calendar2 = date.toCalendar();
		final Calendar end = DateUtil.endOfWeek(calendar2, false);
		Assertions.assertEquals("2020-03-14 23:59:59", DateUtil.date(end).toString());
	}

	@Test
	public void offsetDateTest() {
		final String dateStr = "2017-03-01 22:33:23";
		final Date date = DateUtil.parse(dateStr);

		final Date newDate = DateUtil.offset(date, DateField.DAY_OF_MONTH, 2);
		Assertions.assertEquals("2017-03-03 22:33:23", newDate.toString());

		// 偏移天
		final DateTime newDate2 = DateUtil.offsetDay(date, 3);
		Assertions.assertEquals("2017-03-04 22:33:23", newDate2.toString());

		// 偏移小时
		final DateTime newDate3 = DateUtil.offsetHour(date, -3);
		Assertions.assertEquals("2017-03-01 19:33:23", newDate3.toString());

		// 偏移月
		final DateTime offsetMonth = DateUtil.offsetMonth(date, -1);
		Assertions.assertEquals("2017-02-01 22:33:23", offsetMonth.toString());
	}

	@Test
	public void offsetMonthTest() {
		final DateTime st = DateUtil.parse("2018-05-31");
		final List<DateTime> list = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			list.add(DateUtil.offsetMonth(st, i));
		}
		Assertions.assertEquals("2018-05-31 00:00:00", list.get(0).toString());
		Assertions.assertEquals("2018-06-30 00:00:00", list.get(1).toString());
		Assertions.assertEquals("2018-07-31 00:00:00", list.get(2).toString());
		Assertions.assertEquals("2018-08-31 00:00:00", list.get(3).toString());
	}

	@Test
	public void betweenTest() {
		final String dateStr1 = "2017-03-01 22:34:23";
		final Date date1 = DateUtil.parse(dateStr1);

		final String dateStr2 = "2017-04-01 23:56:14";
		final Date date2 = DateUtil.parse(dateStr2);

		// 相差月
		long betweenMonth = DateUtil.betweenMonth(date1, date2, false);
		Assertions.assertEquals(1, betweenMonth);// 相差一个月
		// 反向
		betweenMonth = DateUtil.betweenMonth(date2, date1, false);
		Assertions.assertEquals(1, betweenMonth);// 相差一个月

		// 相差天
		long betweenDay = DateUtil.between(date1, date2, DateUnit.DAY);
		Assertions.assertEquals(31, betweenDay);// 相差一个月，31天
		// 反向
		betweenDay = DateUtil.between(date2, date1, DateUnit.DAY);
		Assertions.assertEquals(31, betweenDay);// 相差一个月，31天

		// 相差小时
		long betweenHour = DateUtil.between(date1, date2, DateUnit.HOUR);
		Assertions.assertEquals(745, betweenHour);
		// 反向
		betweenHour = DateUtil.between(date2, date1, DateUnit.HOUR);
		Assertions.assertEquals(745, betweenHour);

		// 相差分
		long betweenMinute = DateUtil.between(date1, date2, DateUnit.MINUTE);
		Assertions.assertEquals(44721, betweenMinute);
		// 反向
		betweenMinute = DateUtil.between(date2, date1, DateUnit.MINUTE);
		Assertions.assertEquals(44721, betweenMinute);

		// 相差秒
		long betweenSecond = DateUtil.between(date1, date2, DateUnit.SECOND);
		Assertions.assertEquals(2683311, betweenSecond);
		// 反向
		betweenSecond = DateUtil.between(date2, date1, DateUnit.SECOND);
		Assertions.assertEquals(2683311, betweenSecond);

		// 相差秒
		long betweenMS = DateUtil.between(date1, date2, DateUnit.MS);
		Assertions.assertEquals(2683311000L, betweenMS);
		// 反向
		betweenMS = DateUtil.between(date2, date1, DateUnit.MS);
		Assertions.assertEquals(2683311000L, betweenMS);
	}

	@Test
	public void betweenTest2() {
		final long between = DateUtil.between(DateUtil.parse("2019-05-06 02:15:00"), DateUtil.parse("2019-05-06 02:20:00"), DateUnit.HOUR);
		Assertions.assertEquals(0, between);
	}

	@Test
	public void betweenTest3() {
		final long between = DateUtil.between(DateUtil.parse("2020-03-31 23:59:59"), DateUtil.parse("2020-04-01 00:00:00"), DateUnit.SECOND);
		Assertions.assertEquals(1, between);
	}

	@Test
	public void formatChineseDateTest() {
		String formatChineseDate = DateUtil.formatChineseDate(DateUtil.parse("2018-02-24"), true, false);
		Assertions.assertEquals("二〇一八年二月二十四日", formatChineseDate);

		formatChineseDate = DateUtil.formatChineseDate(DateUtil.parse("2018-02-14"), true, false);
		Assertions.assertEquals("二〇一八年二月十四日", formatChineseDate);
	}

	@Test
	public void formatChineseDateTimeTest() {
		String formatChineseDateTime = DateUtil.formatChineseDate(DateUtil.parse("2018-02-24 12:13:14"), true, true);
		Assertions.assertEquals("二〇一八年二月二十四日十二时十三分十四秒", formatChineseDateTime);

		formatChineseDateTime = DateUtil.formatChineseDate(DateUtil.parse("2022-01-18 12:00:00"), true, true);
		Assertions.assertEquals("二〇二二年一月十八日十二时零分零秒", formatChineseDateTime);
	}

	@Test
	public void formatBetweenTest() {
		final String dateStr1 = "2017-03-01 22:34:23";
		final Date date1 = DateUtil.parse(dateStr1);

		final String dateStr2 = "2017-04-01 23:56:14";
		final Date date2 = DateUtil.parse(dateStr2);

		final long between = DateUtil.between(date1, date2, DateUnit.MS);
		final String formatBetween = DateUtil.formatBetween(between, BetweenFormatter.Level.MINUTE);
		Assertions.assertEquals("31天1小时21分", formatBetween);
	}

	@Test
	public void currentTest() {
		final long current = DateUtil.current();
		final String currentStr = String.valueOf(current);
		Assertions.assertEquals(13, currentStr.length());

		final long currentNano = DateUtil.current();
		final String currentNanoStr = String.valueOf(currentNano);
		Assertions.assertNotNull(currentNanoStr);
	}

	@Test
	public void weekOfYearTest() {
		// 第一周周日
		final int weekOfYear1 = DateUtil.weekOfYear(DateUtil.parse("2016-01-03"));
		Assertions.assertEquals(1, weekOfYear1);

		// 第二周周四
		final int weekOfYear2 = DateUtil.weekOfYear(DateUtil.parse("2016-01-07"));
		Assertions.assertEquals(2, weekOfYear2);
	}

	@Test
	public void timeToSecondTest() {
		int second = DateUtil.timeToSecond("00:01:40");
		Assertions.assertEquals(100, second);
		second = DateUtil.timeToSecond("00:00:40");
		Assertions.assertEquals(40, second);
		second = DateUtil.timeToSecond("01:00:00");
		Assertions.assertEquals(3600, second);
		second = DateUtil.timeToSecond("00:00:00");
		Assertions.assertEquals(0, second);
	}

	@Test
	public void secondToTimeTest() {
		String time = DateUtil.secondToTime(3600);
		Assertions.assertEquals("01:00:00", time);
		time = DateUtil.secondToTime(3800);
		Assertions.assertEquals("01:03:20", time);
		time = DateUtil.secondToTime(0);
		Assertions.assertEquals("00:00:00", time);
		time = DateUtil.secondToTime(30);
		Assertions.assertEquals("00:00:30", time);
	}

	@Test
	public void secondToTimeTest2() {
		final String s1 = "55:02:18";
		final String s2 = "55:00:50";
		final int i = DateUtil.timeToSecond(s1) + DateUtil.timeToSecond(s2);
		final String s = DateUtil.secondToTime(i);
		Assertions.assertEquals("110:03:08", s);
	}

	@Test
	public void parseTest2() {
		// 转换时间与SimpleDateFormat结果保持一致即可
		final String birthday = "700403";
		final Date birthDate = DateUtil.parse(birthday, "yyMMdd");
		// 获取出生年(完全表现形式,如：2010)
		final int sYear = DateUtil.year(birthDate);
		Assertions.assertEquals(1970, sYear);
	}

	@Test
	public void parseTest3() {
		final String dateStr = "2018-10-10 12:11:11";
		final Date date = DateUtil.parse(dateStr);
		final String format = DateUtil.format(date, DatePattern.NORM_DATETIME_PATTERN);
		Assertions.assertEquals(dateStr, format);
	}

	@Test
	public void parseTest4() {
		final String ymd = DateUtil.parse("2019-3-21 12:20:15", "yyyy-MM-dd").toString(DatePattern.PURE_DATE_PATTERN);
		Assertions.assertEquals("20190321", ymd);
	}

	@Test
	public void parseTest5() {
		// 测试时间解析
		//noinspection ConstantConditions
		String time = DateUtil.parse("22:12:12").toString(DatePattern.NORM_TIME_FORMAT);
		Assertions.assertEquals("22:12:12", time);
		//noinspection ConstantConditions
		time = DateUtil.parse("2:12:12").toString(DatePattern.NORM_TIME_FORMAT);
		Assertions.assertEquals("02:12:12", time);
		//noinspection ConstantConditions
		time = DateUtil.parse("2:2:12").toString(DatePattern.NORM_TIME_FORMAT);
		Assertions.assertEquals("02:02:12", time);
		//noinspection ConstantConditions
		time = DateUtil.parse("2:2:1").toString(DatePattern.NORM_TIME_FORMAT);
		Assertions.assertEquals("02:02:01", time);
		//noinspection ConstantConditions
		time = DateUtil.parse("22:2:1").toString(DatePattern.NORM_TIME_FORMAT);
		Assertions.assertEquals("22:02:01", time);
		//noinspection ConstantConditions
		time = DateUtil.parse("2:22:1").toString(DatePattern.NORM_TIME_FORMAT);
		Assertions.assertEquals("02:22:01", time);

		// 测试两位时间解析
		//noinspection ConstantConditions
		time = DateUtil.parse("2:22").toString(DatePattern.NORM_TIME_FORMAT);
		Assertions.assertEquals("02:22:00", time);
		//noinspection ConstantConditions
		time = DateUtil.parse("12:22").toString(DatePattern.NORM_TIME_FORMAT);
		Assertions.assertEquals("12:22:00", time);
		//noinspection ConstantConditions
		time = DateUtil.parse("12:2").toString(DatePattern.NORM_TIME_FORMAT);
		Assertions.assertEquals("12:02:00", time);

	}

	@Test
	public void parseTest6() {
		final String str = "Tue Jun 4 16:25:15 +0800 2019";
		final DateTime dateTime = DateUtil.parse(str);
		assert dateTime != null;
		Assertions.assertEquals("2019-06-04 16:25:15", dateTime.toString());
	}

	@Test
	public void parseTest7() {
		String str = "2019-06-01T19:45:43.000 +0800";
		DateTime dateTime = DateUtil.parse(str);
		assert dateTime != null;
		Assertions.assertEquals("2019-06-01 19:45:43", dateTime.toString());

		str = "2019-06-01T19:45:43 +08:00";
		dateTime = DateUtil.parse(str);
		assert dateTime != null;
		Assertions.assertEquals("2019-06-01 19:45:43", dateTime.toString());
	}

	@Test
	public void parseTest8() {
		final String str = "2020-06-28T02:14:13.000Z";
		final DateTime dateTime = DateUtil.parse(str);
		assert dateTime != null;
		Assertions.assertEquals("2020-06-28 02:14:13", dateTime.toString());
	}

	/**
	 * 测试支持：yyyy-MM-dd HH:mm:ss.SSSSSS 格式
	 */
	@Test
	public void parseNormFullTest() {
		String str = "2020-02-06 01:58:00.000020";
		DateTime dateTime = DateUtil.parse(str);
		Assertions.assertNotNull(dateTime);
		Assertions.assertEquals("2020-02-06 01:58:00.000", dateTime.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		str = "2020-02-06 01:58:00.00002";
		dateTime = DateUtil.parse(str);
		Assertions.assertNotNull(dateTime);
		Assertions.assertEquals("2020-02-06 01:58:00.000", dateTime.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		str = "2020-02-06 01:58:00.111000";
		dateTime = DateUtil.parse(str);
		Assertions.assertNotNull(dateTime);
		Assertions.assertEquals("2020-02-06 01:58:00.111", dateTime.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		str = "2020-02-06 01:58:00.111";
		dateTime = DateUtil.parse(str);
		Assertions.assertNotNull(dateTime);
		Assertions.assertEquals("2020-02-06 01:58:00.111", dateTime.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
	}

	/**
	 * 测试字符串是空，返回null, 而不是直接报错；
	 */
	@Test
	public void parseEmptyTest() {
		final String str = " ";
		final DateTime dateTime = DateUtil.parse(str);
		Assertions.assertNull(dateTime);
	}

	@Test
	public void parseUTCOffsetTest() {
		// issue#I437AP@Gitee
		String str = "2019-06-01T19:45:43+08:00";
		DateTime dateTime = DateUtil.parse(str);
		assert dateTime != null;
		Assertions.assertEquals("2019-06-01 19:45:43", dateTime.toString());

		str = "2019-06-01T19:45:43 +08:00";
		dateTime = DateUtil.parse(str);
		assert dateTime != null;
		Assertions.assertEquals("2019-06-01 19:45:43", dateTime.toString());
	}

	@Test
	public void parseAndOffsetTest() {
		// 检查UTC时间偏移是否准确
		final String str = "2019-09-17T13:26:17.948Z";
		final DateTime dateTime = DateUtil.parse(str);
		assert dateTime != null;
		Assertions.assertEquals("2019-09-17 13:26:17", dateTime.toString());

		final DateTime offset = DateUtil.offsetHour(dateTime, 8);
		Assertions.assertEquals("2019-09-17 21:26:17", offset.toString());
	}

	@Test
	public void parseDateTest() {
		final String dateStr = "2018-4-10";
		final Date date = DateUtil.parse(dateStr);
		final String format = DateUtil.format(date, DatePattern.NORM_DATE_PATTERN);
		Assertions.assertEquals("2018-04-10", format);
	}

	@Test
	public void parseToDateTimeTest1() {
		final String dateStr1 = "2017-02-01";
		final String dateStr2 = "2017/02/01";
		final String dateStr3 = "2017.02.01";
		final String dateStr4 = "2017年02月01日";

		final DateTime dt1 = DateUtil.parse(dateStr1);
		final DateTime dt2 = DateUtil.parse(dateStr2);
		final DateTime dt3 = DateUtil.parse(dateStr3);
		final DateTime dt4 = DateUtil.parse(dateStr4);
		Assertions.assertEquals(dt1, dt2);
		Assertions.assertEquals(dt2, dt3);
		Assertions.assertEquals(dt3, dt4);
	}

	@Test
	public void parseToDateTimeTest2() {
		final String dateStr1 = "2017-02-01 12:23";
		final String dateStr2 = "2017/02/01 12:23";
		final String dateStr3 = "2017.02.01 12:23";
		final String dateStr4 = "2017年02月01日 12:23";

		final DateTime dt1 = DateUtil.parse(dateStr1);
		final DateTime dt2 = DateUtil.parse(dateStr2);
		final DateTime dt3 = DateUtil.parse(dateStr3);
		final DateTime dt4 = DateUtil.parse(dateStr4);
		Assertions.assertEquals(dt1, dt2);
		Assertions.assertEquals(dt2, dt3);
		Assertions.assertEquals(dt3, dt4);
	}

	@Test
	public void parseToDateTimeTest3() {
		final String dateStr1 = "2017-02-01 12:23:45";
		final String dateStr2 = "2017/02/01 12:23:45";
		final String dateStr3 = "2017.02.01 12:23:45";
		final String dateStr4 = "2017年02月01日 12时23分45秒";

		final DateTime dt1 = DateUtil.parse(dateStr1);
		final DateTime dt2 = DateUtil.parse(dateStr2);
		final DateTime dt3 = DateUtil.parse(dateStr3);
		final DateTime dt4 = DateUtil.parse(dateStr4);
		Assertions.assertEquals(dt1, dt2);
		Assertions.assertEquals(dt2, dt3);
		Assertions.assertEquals(dt3, dt4);
	}

	@Test
	public void parseToDateTimeTest4() {
		final String dateStr1 = "2017-02-01 12:23:45";
		final String dateStr2 = "20170201122345";

		final DateTime dt1 = DateUtil.parse(dateStr1);
		final DateTime dt2 = DateUtil.parse(dateStr2);
		Assertions.assertEquals(dt1, dt2);
	}

	@Test
	public void parseToDateTimeTest5() {
		final String dateStr1 = "2017-02-01";
		final String dateStr2 = "20170201";

		final DateTime dt1 = DateUtil.parse(dateStr1);
		final DateTime dt2 = DateUtil.parse(dateStr2);
		Assertions.assertEquals(dt1, dt2);
	}

	@Test
	public void parseUTCTest() {
		String dateStr1 = "2018-09-13T05:34:31Z";
		DateTime dt = DateUtil.parse(dateStr1);

		// parse方法支持UTC格式测试
		final DateTime dt2 = DateUtil.parse(dateStr1);
		Assertions.assertEquals(dt, dt2);

		// 默认使用Pattern对应的时区，即UTC时区
		String dateStr = Objects.requireNonNull(dt).toString();
		Assertions.assertEquals("2018-09-13 05:34:31", dateStr);

		// 使用当前（上海）时区
		dateStr = dt.toString(TimeZone.getTimeZone("GMT+8:00"));
		Assertions.assertEquals("2018-09-13 13:34:31", dateStr);

		dateStr1 = "2018-09-13T13:34:32+0800";
		dt = DateUtil.parse(dateStr1);
		dateStr = Objects.requireNonNull(dt).toString(TimeZone.getTimeZone("GMT+8:00"));
		Assertions.assertEquals("2018-09-13 13:34:32", dateStr);

		dateStr1 = "2018-09-13T13:34:33+08:00";
		dt = DateUtil.parse(dateStr1);
		dateStr = Objects.requireNonNull(dt).toString(TimeZone.getTimeZone("GMT+8:00"));
		Assertions.assertEquals("2018-09-13 13:34:33", dateStr);

		dateStr1 = "2018-09-13T13:34:34+0800";
		dt = DateUtil.parse(dateStr1);
		assert dt != null;
		dateStr = dt.toString(TimeZone.getTimeZone("GMT+8:00"));
		Assertions.assertEquals("2018-09-13 13:34:34", dateStr);

		dateStr1 = "2018-09-13T13:34:35+08:00";
		dt = DateUtil.parse(dateStr1);
		assert dt != null;
		dateStr = dt.toString(TimeZone.getTimeZone("GMT+8:00"));
		Assertions.assertEquals("2018-09-13 13:34:35", dateStr);

		dateStr1 = "2018-09-13T13:34:36.999+0800";
		dt = DateUtil.parse(dateStr1);
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DatePattern.NORM_DATETIME_MS_PATTERN);
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		dateStr = Objects.requireNonNull(dt).toString(simpleDateFormat);
		Assertions.assertEquals("2018-09-13 13:34:36.999", dateStr);

		dateStr1 = "2018-09-13T13:34:37.999+08:00";
		dt = DateUtil.parse(dateStr1);
		dateStr = Objects.requireNonNull(dt).toString(simpleDateFormat);
		Assertions.assertEquals("2018-09-13 13:34:37.999", dateStr);

		dateStr1 = "2018-09-13T13:34:38.999+0800";
		dt = DateUtil.parse(dateStr1);
		assert dt != null;
		dateStr = dt.toString(simpleDateFormat);
		Assertions.assertEquals("2018-09-13 13:34:38.999", dateStr);

		dateStr1 = "2018-09-13T13:34:39.999+08:00";
		dt = DateUtil.parse(dateStr1);
		assert dt != null;
		dateStr = dt.toString(simpleDateFormat);
		Assertions.assertEquals("2018-09-13 13:34:39.999", dateStr);

		// 使用UTC时区
		dateStr1 = "2018-09-13T13:34:39.99";
		dt = DateUtil.parse(dateStr1);
		assert dt != null;
		dateStr = dt.toString();
		Assertions.assertEquals("2018-09-13 13:34:39", dateStr);
	}

	@Test
	public void parseUTCTest2() {
		// issue1503@Github
		// 检查不同毫秒长度都可以正常匹配
		String utcTime = "2021-03-30T12:56:51.3Z";
		DateTime parse = DateUtil.parse(utcTime);
		Assertions.assertEquals("2021-03-30 12:56:51", Objects.requireNonNull(parse).toString());

		utcTime = "2021-03-30T12:56:51.34Z";
		parse = DateUtil.parse(utcTime);
		Assertions.assertEquals("2021-03-30 12:56:51", Objects.requireNonNull(parse).toString());

		utcTime = "2021-03-30T12:56:51.345Z";
		parse = DateUtil.parse(utcTime);
		Assertions.assertEquals("2021-03-30 12:56:51", Objects.requireNonNull(parse).toString());
	}

	@Test
	public void parseUTCTest3() {
		// issue#I5M6DP
		final String dateStr = "2022-08-13T09:30";
		final DateTime dateTime = DateUtil.parse(dateStr);
		Assertions.assertNotNull(dateTime);
		Assertions.assertEquals("2022-08-13 09:30:00", dateTime.toString());
	}

	@Test
	public void parseCSTTest() {
		final String dateStr = "Wed Sep 16 11:26:23 CST 2009";

		final SimpleDateFormat sdf = new SimpleDateFormat(DatePattern.JDK_DATETIME_PATTERN, Locale.US);
		// Asia/Shanghai是以地区命名的地区标准时，在中国叫CST，因此如果解析CST时不使用"Asia/Shanghai"而使用"GMT+08:00"，会导致相差一个小时
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		final DateTime parse = DateUtil.parse(dateStr, sdf);

		DateTime dateTime = DateUtil.parse(dateStr);
		Assertions.assertEquals(parse, dateTime);

		dateTime = DateUtil.parse(dateStr);
		Assertions.assertEquals(parse, dateTime);
	}

	@Test
	public void parseCSTTest2() {
		final String dateStr = "Wed Sep 16 11:26:23 CST 2009";

		final SimpleDateFormat sdf = new SimpleDateFormat(DatePattern.JDK_DATETIME_PATTERN, Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
		final DateTime parse = DateUtil.parse(dateStr, sdf);

		final FastDateFormat fdf = FastDateFormat.getInstance(DatePattern.JDK_DATETIME_PATTERN, TimeZone.getTimeZone("America/Chicago"), Locale.US);
		final DateTime parse2 = DateUtil.parse(dateStr, fdf);

		Assertions.assertEquals(parse, parse2);
	}

	@Test
	public void parseJDkTest() {
		final String dateStr = "Thu May 16 17:57:18 GMT+08:00 2019";
		final DateTime time = DateUtil.parse(dateStr);
		Assertions.assertEquals("2019-05-16 17:57:18", Objects.requireNonNull(time).toString());
	}

	@Test
	public void parseISOTest() {
		final String dateStr = "2020-04-23T02:31:00.000Z";
		final DateTime time = DateUtil.parse(dateStr);
		Assertions.assertEquals("2020-04-23 02:31:00", Objects.requireNonNull(time).toString());
	}

	@Test
	public void endOfYearTest() {
		final DateTime date = DateUtil.now();
		date.setField(DateField.YEAR, 2019);
		final DateTime endOfYear = DateUtil.endOfYear(date);
		Assertions.assertEquals("2019-12-31 23:59:59", endOfYear.toString());
	}

	@Test
	public void endOfQuarterTest() {
		final Date date = DateUtil.endOfQuarter(
				DateUtil.parse("2020-05-31 00:00:00"));

		Assertions.assertEquals("2020-06-30 23:59:59", DateUtil.format(date, "yyyy-MM-dd HH:mm:ss"));
	}

	@Test
	public void endOfWeekTest() {
		// 周日
		final DateTime now = DateUtil.parse("2019-09-15 13:00");

		final DateTime startOfWeek = DateUtil.beginOfWeek(now);
		Assertions.assertEquals("2019-09-09 00:00:00", startOfWeek.toString());
		final DateTime endOfWeek = DateUtil.endOfWeek(now);
		Assertions.assertEquals("2019-09-15 23:59:59", endOfWeek.toString());

		final long between = DateUtil.between(endOfWeek, startOfWeek, DateUnit.DAY);
		// 周一和周日相距6天
		Assertions.assertEquals(6, between);
	}

	@Test
	public void dayOfWeekTest() {
		final int dayOfWeek = DateUtil.dayOfWeek(DateUtil.parse("2018-03-07"));
		Assertions.assertEquals(Calendar.WEDNESDAY, dayOfWeek);
		final Week week = DateUtil.dayOfWeekEnum(DateUtil.parse("2018-03-07"));
		Assertions.assertEquals(Week.WEDNESDAY, week);
	}

	@Test
	public void compareTest() {
		final Date date1 = DateUtil.parse("2021-04-13 23:59:59.999");
		final Date date2 = DateUtil.parse("2021-04-13 23:59:10");

		Assertions.assertEquals(1, DateUtil.compare(date1, date2));
		Assertions.assertEquals(1, DateUtil.compare(date1, date2, DatePattern.NORM_DATETIME_PATTERN));
		Assertions.assertEquals(0, DateUtil.compare(date1, date2, DatePattern.NORM_DATE_PATTERN));
		Assertions.assertEquals(0, DateUtil.compare(date1, date2, DatePattern.NORM_DATETIME_MINUTE_PATTERN));


		final Date date11 = DateUtil.parse("2021-04-13 23:59:59.999");
		final Date date22 = DateUtil.parse("2021-04-11 23:10:10");
		Assertions.assertEquals(0, DateUtil.compare(date11, date22, DatePattern.NORM_MONTH_PATTERN));
	}

	@Test
	public void formatHttpDateTest() {
		final String formatHttpDate = DateUtil.formatHttpDate(DateUtil.parse("2019-01-02 22:32:01"));
		Assertions.assertEquals("Wed, 02 Jan 2019 14:32:01 GMT", formatHttpDate);
	}

	@Test
	public void toInstantTest() {
		final LocalDateTime localDateTime = LocalDateTime.parse("2017-05-06T08:30:00", DateTimeFormatter.ISO_DATE_TIME);
		Instant instant = DateUtil.toInstant(localDateTime);
		Assertions.assertEquals("2017-05-06T00:30:00Z", instant.toString());

		final LocalDate localDate = localDateTime.toLocalDate();
		instant = DateUtil.toInstant(localDate);
		Assertions.assertNotNull(instant);

		final LocalTime localTime = localDateTime.toLocalTime();
		instant = DateUtil.toInstant(localTime);
		Assertions.assertNotNull(instant);
	}

	@Test
	public void dateTest() {
		//LocalDateTime ==> date
		final LocalDateTime localDateTime = LocalDateTime.parse("2017-05-06T08:30:00", DateTimeFormatter.ISO_DATE_TIME);
		final DateTime date = DateUtil.date(localDateTime);
		Assertions.assertEquals("2017-05-06 08:30:00", date.toString());

		//LocalDate ==> date
		final LocalDate localDate = localDateTime.toLocalDate();
		final DateTime date2 = DateUtil.date(localDate);
		Assertions.assertEquals("2017-05-06",
				DateUtil.format(date2, DatePattern.NORM_DATE_PATTERN));
	}

	@Test
	public void dateTest2() {
		// 测试负数日期
		final long dateLong = -1497600000;
		final DateTime date = DateUtil.date(dateLong);
		Assertions.assertEquals("1969-12-15 00:00:00", date.toString());
	}

	@Test
	public void ageTest() {
		final String d1 = "2000-02-29";
		final String d2 = "2018-02-28";
		final int age = DateUtil.age(DateUtil.parse(d1), DateUtil.parse(d2));
		// issue#I6E6ZG，法定生日当天不算年龄，从第二天开始计算
		Assertions.assertEquals(17, age);
	}

	@Test
	public void ageTest2() {
		Assertions.assertThrows(IllegalArgumentException.class, ()->{
			final String d1 = "2019-02-29";
			final String d2 = "2018-02-28";
			DateUtil.age(DateUtil.parse(d1), DateUtil.parse(d2));
		});
	}

	@Test
	public void ageTest3() {
		// 按照《最高人民法院关于审理未成年人刑事案件具体应用法律若干问题的解释》第二条规定刑法第十七条规定的“周岁”，按照公历的年、月、日计算，从周岁生日的第二天起算。
		// 那我们认为就算当年是闰年，29日也算周岁生日的第二天，可以算作一岁
		final String d1 = "1998-02-28";
		final String d2 = "2000-02-29";
		final int age = DateUtil.age(DateUtil.parse(d1), DateUtil.parse(d2));
		// issue#I6E6ZG，法定生日当天不算年龄，从第二天开始计算
		Assertions.assertEquals(2, age);
	}

	@Test
	public void ageTest4() {
		// 按照《最高人民法院关于审理未成年人刑事案件具体应用法律若干问题的解释》第二条规定刑法第十七条规定的“周岁”，按照公历的年、月、日计算，从周岁生日的第二天起算。
		// 那我们认为就算当年是闰年，29日也算周岁生日的第二天，可以算作一岁
		final String d1 = "1999-02-28";
		final String d2 = "2000-02-29";
		final int age = DateUtil.age(DateUtil.parse(d1), DateUtil.parse(d2));
		// issue#I6E6ZG，法定生日当天不算年龄，从第二天开始计算
		Assertions.assertEquals(1, age);
	}

	@Test
	public void localDateTimeTest() {
		// 测试字符串与LocalDateTime的互相转换
		final String strDate = "2019-12-01 17:02:30";
		LocalDateTime ldt = TimeUtil.parseByISO(strDate);
		String strDate1 = DateUtil.formatLocalDateTime(ldt);
		Assertions.assertEquals(strDate, strDate1);

		final String strDate2 = "2019-12-01 17:02:30.111";
		ldt = TimeUtil.parse(strDate2, DatePattern.NORM_DATETIME_MS_PATTERN);
		strDate1 = DateUtil.format(ldt, DatePattern.NORM_DATETIME_PATTERN);
		Assertions.assertEquals(strDate, strDate1);
	}

	@Test
	public void localDateTimeTest2() {
		// 测试字符串与LocalDateTime的互相转换
		final String strDate = "2019-12-01";
		final LocalDateTime localDateTime = TimeUtil.parse(strDate, "yyyy-MM-dd");
		Assertions.assertEquals(strDate, DateUtil.format(localDateTime, DatePattern.NORM_DATE_PATTERN));
	}

	@Test
	public void betweenWeekTest() {
		final DateTime start = DateUtil.parse("2019-03-05");
		final DateTime end = DateUtil.parse("2019-10-05");

		final long weekCount = DateUtil.betweenWeek(start, end, true);
		Assertions.assertEquals(30L, weekCount);
	}

	@Test
	public void betweenDayTest() {
		for (int i = 0; i < 1000; i++) {
			final String datr = RandomUtil.randomInt(1900, 2099) + "-01-20";
			final long betweenDay = DateUtil.betweenDay(
					DateUtil.parse("1970-01-01"),
					DateUtil.parse(datr), false);
			Assertions.assertEquals(Math.abs(LocalDate.parse(datr).toEpochDay()), betweenDay);
		}
	}

	@Test
	public void dayOfYearTest() {
		final int dayOfYear = DateUtil.dayOfYear(DateUtil.parse("2020-01-01"));
		Assertions.assertEquals(1, dayOfYear);
		final int lengthOfYear = DateUtil.lengthOfYear(2020);
		Assertions.assertEquals(366, lengthOfYear);
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void parseSingleNumberTest() {
		DateTime dateTime = DateUtil.parse("2020-5-08");
		Assertions.assertEquals("2020-05-08 00:00:00", dateTime.toString());
		dateTime = DateUtil.parse("2020-5-8");
		Assertions.assertEquals("2020-05-08 00:00:00", dateTime.toString());
		dateTime = DateUtil.parse("2020-05-8");
		Assertions.assertEquals("2020-05-08 00:00:00", dateTime.toString());

		//datetime
		dateTime = DateUtil.parse("2020-5-8 3:12:3");
		Assertions.assertEquals("2020-05-08 03:12:03", dateTime.toString());
		dateTime = DateUtil.parse("2020-5-8 3:2:3");
		Assertions.assertEquals("2020-05-08 03:02:03", dateTime.toString());
		dateTime = DateUtil.parse("2020-5-8 3:12:13");
		Assertions.assertEquals("2020-05-08 03:12:13", dateTime.toString());

		dateTime = DateUtil.parse("2020-5-8 4:12:26.223");
		Assertions.assertEquals("2020-05-08 04:12:26", dateTime.toString());
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void parseISO8601Test() {
		final String dt = "2020-06-03 12:32:12,333";
		final DateTime parse = DateUtil.parse(dt);
		Assertions.assertEquals("2020-06-03 12:32:12", parse.toString());
	}

	@Test
	public void parseNotFitTest() {
		Assertions.assertThrows(DateException.class, ()->{
			//https://github.com/dromara/hutool/issues/1332
			// 在日期格式不匹配的时候，测试是否正常报错
			DateUtil.parse("2020-12-23", DatePattern.PURE_DATE_PATTERN);
		});
	}

	@Test
	public void formatTest() {
		final Calendar calendar = new GregorianCalendar();
		calendar.set(2021, Calendar.JULY, 14, 23, 59, 59);
		final Date date = new DateTime(calendar);

		Assertions.assertEquals("2021-07-14 23:59:59", DateUtil.format(date, DatePattern.NORM_DATETIME_FORMATTER));
		Assertions.assertEquals("2021-07-14 23:59:59", DateUtil.format(date, DatePattern.NORM_DATETIME_FORMAT));
		Assertions.assertEquals("2021-07-14 23:59:59", DateUtil.format(date, DatePattern.NORM_DATETIME_PATTERN));
	}

	@Test
	public void formatNormDateTimeFormatterTest() {
		String format = DateUtil.format(DateUtil.parse("2021-07-14 10:05:38"), DatePattern.NORM_DATETIME_FORMATTER);
		Assertions.assertEquals("2021-07-14 10:05:38", format);

		format = DateUtil.format(TimeUtil.parseByISO("2021-07-14T10:05:38"),
				"yyyy-MM-dd HH:mm:ss");
		Assertions.assertEquals("2021-07-14 10:05:38", format);
	}

	@Test
	public void isWeekendTest() {
		DateTime parse = DateUtil.parse("2021-07-28");
		Assertions.assertFalse(DateUtil.isWeekend(parse));

		parse = DateUtil.parse("2021-07-25");
		Assertions.assertTrue(DateUtil.isWeekend(parse));
		parse = DateUtil.parse("2021-07-24");
		Assertions.assertTrue(DateUtil.isWeekend(parse));
	}

	@Test
	public void parseSingleMonthAndDayTest() {
		DateTime parse = DateUtil.parse("2021-1-1");
		Assertions.assertNotNull(parse);
		Assertions.assertEquals("2021-01-01 00:00:00", parse.toString());

		parse = DateUtil.parse("2021-1-22 00:00:00");
		Assertions.assertNotNull(parse);
		Assertions.assertEquals("2021-01-22 00:00:00", parse.toString());
	}

	@Test
	public void parseByDateTimeFormatterTest() {
		final DateTime parse = DateUtil.parse("2021-12-01", DatePattern.NORM_DATE_FORMATTER);
		Assertions.assertEquals("2021-12-01 00:00:00", parse.toString());
	}

	@Test
	public void isSameWeekTest() {
		// 周六与周日比较
		final boolean isSameWeek = DateUtil.isSameWeek(DateTime.of("2022-01-01", "yyyy-MM-dd"), DateTime.of("2022-01-02", "yyyy-MM-dd"), true);
		Assertions.assertTrue(isSameWeek);
		// 周日与周一比较
		final boolean isSameWeek1 = DateUtil.isSameWeek(DateTime.of("2022-01-02", "yyyy-MM-dd"), DateTime.of("2022-01-03", "yyyy-MM-dd"), false);
		Assertions.assertTrue(isSameWeek1);
		// 跨月比较
		final boolean isSameWeek2 = DateUtil.isSameWeek(DateTime.of("2021-12-29", "yyyy-MM-dd"), DateTime.of("2022-01-01", "yyyy-MM-dd"), true);
		Assertions.assertTrue(isSameWeek2);
	}

	@Test
	public void parseTimeTest(){
		final DateTime dateTime = DateUtil.parse("12:23:34");
		Console.log(dateTime);
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void isOverlapTest() {
		final DateTime oneStartTime = DateUtil.parse("2022-01-01 10:10:10");
		final DateTime oneEndTime = DateUtil.parse("2022-01-01 11:10:10");

		final DateTime oneStartTime2 = DateUtil.parse("2022-01-01 11:20:10");
		final DateTime oneEndTime2 = DateUtil.parse("2022-01-01 11:30:10");

		final DateTime oneStartTime3 = DateUtil.parse("2022-01-01 11:40:10");
		final DateTime oneEndTime3 = DateUtil.parse("2022-01-01 11:50:10");

		//真实请假数据
		final DateTime realStartTime = DateUtil.parse("2022-01-01 11:49:10");
		final DateTime realEndTime = DateUtil.parse("2022-01-01 12:00:10");

		final DateTime realStartTime1 = DateUtil.parse("2022-03-01 08:00:00");
		final DateTime realEndTime1   = DateUtil.parse("2022-03-01 10:00:00");

		final DateTime startTime  = DateUtil.parse("2022-03-23 05:00:00");
		final DateTime endTime    = DateUtil.parse("2022-03-23 13:00:00");

		Assertions.assertFalse(DateUtil.isOverlap(oneStartTime, oneEndTime, realStartTime, realEndTime));
		Assertions.assertFalse(DateUtil.isOverlap(oneStartTime2, oneEndTime2, realStartTime, realEndTime));
		Assertions.assertTrue(DateUtil.isOverlap(oneStartTime3, oneEndTime3, realStartTime, realEndTime));

		Assertions.assertFalse(DateUtil.isOverlap(realStartTime1,realEndTime1,startTime,endTime));
		Assertions.assertFalse(DateUtil.isOverlap(startTime,endTime,realStartTime1,realEndTime1));

		Assertions.assertTrue(DateUtil.isOverlap(startTime,startTime,startTime,startTime));
		Assertions.assertTrue(DateUtil.isOverlap(startTime,startTime,startTime,endTime));
		Assertions.assertFalse(DateUtil.isOverlap(startTime,startTime,endTime,endTime));
		Assertions.assertTrue(DateUtil.isOverlap(startTime,endTime,endTime,endTime));
	}

	@Test
	public void isInTest(){
		final String sourceStr = "2022-04-19 00:00:00";
		final String startTimeStr = "2022-04-19 00:00:00";
		final String endTimeStr = "2022-04-19 23:59:59";
		final boolean between = DateUtil.isIn(DateUtil.parse(startTimeStr),
				DateUtil.parse(endTimeStr),
				DateUtil.parse(sourceStr));
		Assertions.assertTrue(between);
	}

	@Test
	public void isLastDayTest(){
		final DateTime dateTime = DateUtil.parse("2022-09-30");
		final int dayOfMonth  = DateUtil.getLastDayOfMonth(dateTime);
		Assertions.assertEquals(dayOfMonth, Objects.requireNonNull(dateTime).dayOfMonth());
		Assertions.assertTrue(DateUtil.isLastDayOfMonth(dateTime), "not is last day of this month !!");
	}

	/**
	 * issue#2887 由于UTC时间的毫秒部分超出3位导致的秒数增加的问题
	 */
	@Test
	public void parseUTCTest4() {
		final String dateStr = "2023-02-07T00:02:16.12345+08:00";
		final DateTime dateTime = DateUtil.parse(dateStr);
		Assertions.assertNotNull(dateTime);
		Assertions.assertEquals("2023-02-07 00:02:16", dateTime.toString());

		final String dateStr2 = "2023-02-07T00:02:16.12345-08:00";
		final DateTime dateTime2 = DateUtil.parse(dateStr2);
		Assertions.assertNotNull(dateTime2);
		Assertions.assertEquals("2023-02-07 00:02:16", dateTime2.toString());

		final String dateStr3 = "2021-03-17T06:31:33.9999";
		final DateTime dateTime3 = DateUtil.parse(dateStr3);
		Assertions.assertNotNull(dateTime3);
		Assertions.assertEquals("2021-03-17 06:31:33", dateTime3.toString());
	}

	/**
	 * issue#I6E6ZG 法定年龄/周岁/实岁计算
	 */
	@Test
	public void issueI6E6ZGTest() {
		// issue#I6E6ZG，法定生日当天不算年龄，从第二天开始计算
		Assertions.assertEquals(70, DateUtil.age(DateUtil.parse("1952-02-14"), DateUtil.parse("2023-02-14")));
		Assertions.assertEquals(71, DateUtil.age(DateUtil.parse("1952-02-13"), DateUtil.parse("2023-02-14")));
		Assertions.assertEquals(0, DateUtil.age(DateUtil.parse("2023-02-14"), DateUtil.parse("2023-02-14")));
	}

	@Test
	void issueI7H34NTest() {
		final DateTime parse = DateUtil.parse("2019-10-22T09:56:03.000123Z");
		Assertions.assertNotNull(parse);
		Assertions.assertEquals("2019-10-22 09:56:03", parse.toString());
	}

	@Test
	public void issueI8NMP7Test() {
		final String str = "1702262524444";
		final DateTime parse = DateUtil.parse(str);
		Assertions.assertEquals("2023-12-11 10:42:04", Objects.requireNonNull(parse).toString());
	}
}
