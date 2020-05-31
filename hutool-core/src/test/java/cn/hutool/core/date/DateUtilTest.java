package cn.hutool.core.date;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.BetweenFormater.Level;
import cn.hutool.core.date.format.FastDateFormat;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.TimeZone;

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
		Date date = DateUtil.date();
		Assert.assertNotNull(date);
		// 当前时间
		Date date2 = DateUtil.date(Calendar.getInstance());
		Assert.assertNotNull(date2);
		// 当前时间
		Date date3 = DateUtil.date(System.currentTimeMillis());
		Assert.assertNotNull(date3);

		// 当前日期字符串，格式：yyyy-MM-dd HH:mm:ss
		String now = DateUtil.now();
		Assert.assertNotNull(now);
		// 当前日期字符串，格式：yyyy-MM-dd
		String today = DateUtil.today();
		Assert.assertNotNull(today);
	}

	@Test
	public void formatAndParseTest() {
		String dateStr = "2017-03-01";
		Date date = DateUtil.parse(dateStr);

		String format = DateUtil.format(date, "yyyy/MM/dd");
		Assert.assertEquals("2017/03/01", format);

		// 常用格式的格式化
		String formatDate = DateUtil.formatDate(date);
		Assert.assertEquals("2017-03-01", formatDate);
		String formatDateTime = DateUtil.formatDateTime(date);
		Assert.assertEquals("2017-03-01 00:00:00", formatDateTime);
		String formatTime = DateUtil.formatTime(date);
		Assert.assertEquals("00:00:00", formatTime);
	}

	@Test
	public void beginAndEndTest() {
		String dateStr = "2017-03-01 22:33:23";
		Date date = DateUtil.parse(dateStr);

		// 一天的开始
		Date beginOfDay = DateUtil.beginOfDay(date);
		Assert.assertEquals("2017-03-01 00:00:00", beginOfDay.toString());
		// 一天的结束
		Date endOfDay = DateUtil.endOfDay(date);
		Assert.assertEquals("2017-03-01 23:59:59", endOfDay.toString());
	}

	@Test
	public void truncateTest(){
		String dateStr2 = "2020-02-29 12:59:34";
		Date date2 = DateUtil.parse(dateStr2);
		final DateTime dateTime = DateUtil.truncate(date2, DateField.MINUTE);
		Assert.assertEquals("2020-02-29 12:59:00", dateTime.toString());
	}

	@Test
	public void beginAndWeedTest() {
		String dateStr = "2017-03-01 22:33:23";
		DateTime date = DateUtil.parse(dateStr);
		Objects.requireNonNull(date).setFirstDayOfWeek(Week.MONDAY);

		// 一周的开始
		Date beginOfWeek = DateUtil.beginOfWeek(date);
		Assert.assertEquals("2017-02-27 00:00:00", beginOfWeek.toString());
		// 一周的结束
		Date endOfWeek = DateUtil.endOfWeek(date);
		Assert.assertEquals("2017-03-05 23:59:59", endOfWeek.toString());

		Calendar calendar = DateUtil.calendar(date);
		// 一周的开始
		Calendar begin = DateUtil.beginOfWeek(calendar);
		Assert.assertEquals("2017-02-27 00:00:00", DateUtil.date(begin).toString());
		// 一周的结束
		Calendar end = DateUtil.endOfWeek(calendar);
		Assert.assertEquals("2017-03-05 23:59:59", DateUtil.date(end).toString());
	}

	@Test
	public void beginAndWeedTest2() {
		String beginStr = "2020-03-11";
		DateTime date = DateUtil.parseDate(beginStr);
		Calendar calendar = date.toCalendar();
		final Calendar begin = DateUtil.beginOfWeek(calendar, false);
		Assert.assertEquals("2020-03-08 00:00:00", DateUtil.date(begin).toString());

		Calendar calendar2 = date.toCalendar();
		final Calendar end = DateUtil.endOfWeek(calendar2, false);
		Assert.assertEquals("2020-03-14 23:59:59", DateUtil.date(end).toString());
	}

	@Test
	public void offsetDateTest() {
		String dateStr = "2017-03-01 22:33:23";
		Date date = DateUtil.parse(dateStr);

		Date newDate = DateUtil.offset(date, DateField.DAY_OF_MONTH, 2);
		Assert.assertEquals("2017-03-03 22:33:23", newDate.toString());

		// 偏移天
		DateTime newDate2 = DateUtil.offsetDay(date, 3);
		Assert.assertEquals("2017-03-04 22:33:23", newDate2.toString());

		// 偏移小时
		DateTime newDate3 = DateUtil.offsetHour(date, -3);
		Assert.assertEquals("2017-03-01 19:33:23", newDate3.toString());

		// 偏移月
		DateTime offsetMonth = DateUtil.offsetMonth(date, -1);
		Assert.assertEquals("2017-02-01 22:33:23", offsetMonth.toString());
	}

	@Test
	public void offsetMonthTest() {
		DateTime st = DateUtil.parseDate("2018-05-31");
		List<DateTime> list = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			list.add(DateUtil.offsetMonth(st, i));
		}
		Assert.assertEquals("2018-05-31 00:00:00", list.get(0).toString());
		Assert.assertEquals("2018-06-30 00:00:00", list.get(1).toString());
		Assert.assertEquals("2018-07-31 00:00:00", list.get(2).toString());
		Assert.assertEquals("2018-08-31 00:00:00", list.get(3).toString());
	}

	@Test
	public void betweenTest() {
		String dateStr1 = "2017-03-01 22:34:23";
		Date date1 = DateUtil.parse(dateStr1);

		String dateStr2 = "2017-04-01 23:56:14";
		Date date2 = DateUtil.parse(dateStr2);

		// 相差月
		long betweenMonth = DateUtil.betweenMonth(date1, date2, false);
		Assert.assertEquals(1, betweenMonth);// 相差一个月
		// 反向
		betweenMonth = DateUtil.betweenMonth(date2, date1, false);
		Assert.assertEquals(1, betweenMonth);// 相差一个月

		// 相差天
		long betweenDay = DateUtil.between(date1, date2, DateUnit.DAY);
		Assert.assertEquals(31, betweenDay);// 相差一个月，31天
		// 反向
		betweenDay = DateUtil.between(date2, date1, DateUnit.DAY);
		Assert.assertEquals(31, betweenDay);// 相差一个月，31天

		// 相差小时
		long betweenHour = DateUtil.between(date1, date2, DateUnit.HOUR);
		Assert.assertEquals(745, betweenHour);
		// 反向
		betweenHour = DateUtil.between(date2, date1, DateUnit.HOUR);
		Assert.assertEquals(745, betweenHour);

		// 相差分
		long betweenMinute = DateUtil.between(date1, date2, DateUnit.MINUTE);
		Assert.assertEquals(44721, betweenMinute);
		// 反向
		betweenMinute = DateUtil.between(date2, date1, DateUnit.MINUTE);
		Assert.assertEquals(44721, betweenMinute);

		// 相差秒
		long betweenSecond = DateUtil.between(date1, date2, DateUnit.SECOND);
		Assert.assertEquals(2683311, betweenSecond);
		// 反向
		betweenSecond = DateUtil.between(date2, date1, DateUnit.SECOND);
		Assert.assertEquals(2683311, betweenSecond);

		// 相差秒
		long betweenMS = DateUtil.between(date1, date2, DateUnit.MS);
		Assert.assertEquals(2683311000L, betweenMS);
		// 反向
		betweenMS = DateUtil.between(date2, date1, DateUnit.MS);
		Assert.assertEquals(2683311000L, betweenMS);
	}

	@Test
	public void betweenTest2() {
		long between = DateUtil.between(DateUtil.parse("2019-05-06 02:15:00"), DateUtil.parse("2019-05-06 02:20:00"), DateUnit.HOUR);
		Assert.assertEquals(0, between);
	}

	@Test
	public void formatChineseDateTest() {
		String formatChineseDate = DateUtil.formatChineseDate(DateUtil.parse("2018-02-24"), true);
		Assert.assertEquals("二〇一八年二月二十四日", formatChineseDate);
	}

	@Test
	public void formatBetweenTest() {
		String dateStr1 = "2017-03-01 22:34:23";
		Date date1 = DateUtil.parse(dateStr1);

		String dateStr2 = "2017-04-01 23:56:14";
		Date date2 = DateUtil.parse(dateStr2);

		long between = DateUtil.between(date1, date2, DateUnit.MS);
		String formatBetween = DateUtil.formatBetween(between, Level.MINUTE);
		Assert.assertEquals("31天1小时21分", formatBetween);
	}

	@Test
	public void timerTest() {
		TimeInterval timer = DateUtil.timer();

		// ---------------------------------
		// -------这是执行过程
		// ---------------------------------

		timer.interval();// 花费毫秒数
		timer.intervalRestart();// 返回花费时间，并重置开始时间
		timer.intervalMinute();// 花费分钟数
	}

	@Test
	public void currentTest() {
		long current = DateUtil.current(false);
		String currentStr = String.valueOf(current);
		Assert.assertEquals(13, currentStr.length());

		long currentNano = DateUtil.current(true);
		String currentNanoStr = String.valueOf(currentNano);
		Assert.assertNotNull(currentNanoStr);
	}

	@Test
	public void weekOfYearTest() {
		// 第一周周日
		int weekOfYear1 = DateUtil.weekOfYear(DateUtil.parse("2016-01-03"));
		Assert.assertEquals(1, weekOfYear1);

		// 第二周周四
		int weekOfYear2 = DateUtil.weekOfYear(DateUtil.parse("2016-01-07"));
		Assert.assertEquals(2, weekOfYear2);
	}

	@Test
	public void timeToSecondTest() {
		int second = DateUtil.timeToSecond("00:01:40");
		Assert.assertEquals(100, second);
		second = DateUtil.timeToSecond("00:00:40");
		Assert.assertEquals(40, second);
		second = DateUtil.timeToSecond("01:00:00");
		Assert.assertEquals(3600, second);
		second = DateUtil.timeToSecond("00:00:00");
		Assert.assertEquals(0, second);
	}

	@Test
	public void secondToTimeTest() {
		String time = DateUtil.secondToTime(3600);
		Assert.assertEquals("01:00:00", time);
		time = DateUtil.secondToTime(3800);
		Assert.assertEquals("01:03:20", time);
		time = DateUtil.secondToTime(0);
		Assert.assertEquals("00:00:00", time);
		time = DateUtil.secondToTime(30);
		Assert.assertEquals("00:00:30", time);
	}

	@Test
	public void secondToTimeTest2() {
		String s1 = "55:02:18";
		String s2 = "55:00:50";
		int i = DateUtil.timeToSecond(s1) + DateUtil.timeToSecond(s2);
		String s = DateUtil.secondToTime(i);
		Assert.assertEquals("110:03:08", s);
	}

	@Test
	public void parseTest2() {
		// 转换时间与SimpleDateFormat结果保持一致即可
		String birthday = "700403";
		Date birthDate = DateUtil.parse(birthday, "yyMMdd");
		// 获取出生年(完全表现形式,如：2010)
		int sYear = DateUtil.year(birthDate);
		Assert.assertEquals(1970, sYear);
	}

	@Test
	public void parseTest3() {
		String dateStr = "2018-10-10 12:11:11";
		Date date = DateUtil.parse(dateStr);
		String format = DateUtil.format(date, DatePattern.NORM_DATETIME_PATTERN);
		Assert.assertEquals(dateStr, format);
	}

	@Test
	public void parseTest4() {
		String ymd = DateUtil.parse("2019-3-21 12:20:15", "yyyy-MM-dd").toString(DatePattern.PURE_DATE_PATTERN);
		Assert.assertEquals("20190321", ymd);
	}

	@Test
	public void parseTest5() {
		// 测试时间解析
		//noinspection ConstantConditions
		String time = DateUtil.parse("22:12:12").toString(DatePattern.NORM_TIME_FORMAT);
		Assert.assertEquals("22:12:12", time);
		//noinspection ConstantConditions
		time = DateUtil.parse("2:12:12").toString(DatePattern.NORM_TIME_FORMAT);
		Assert.assertEquals("02:12:12", time);
		//noinspection ConstantConditions
		time = DateUtil.parse("2:2:12").toString(DatePattern.NORM_TIME_FORMAT);
		Assert.assertEquals("02:02:12", time);
		//noinspection ConstantConditions
		time = DateUtil.parse("2:2:1").toString(DatePattern.NORM_TIME_FORMAT);
		Assert.assertEquals("02:02:01", time);
		//noinspection ConstantConditions
		time = DateUtil.parse("22:2:1").toString(DatePattern.NORM_TIME_FORMAT);
		Assert.assertEquals("22:02:01", time);
		//noinspection ConstantConditions
		time = DateUtil.parse("2:22:1").toString(DatePattern.NORM_TIME_FORMAT);
		Assert.assertEquals("02:22:01", time);

		// 测试两位时间解析
		//noinspection ConstantConditions
		time = DateUtil.parse("2:22").toString(DatePattern.NORM_TIME_FORMAT);
		Assert.assertEquals("02:22:00", time);
		//noinspection ConstantConditions
		time = DateUtil.parse("12:22").toString(DatePattern.NORM_TIME_FORMAT);
		Assert.assertEquals("12:22:00", time);
		//noinspection ConstantConditions
		time = DateUtil.parse("12:2").toString(DatePattern.NORM_TIME_FORMAT);
		Assert.assertEquals("12:02:00", time);

	}

	@Test
	public void parseTest6() {
		String str = "Tue Jun 4 16:25:15 +0800 2019";
		DateTime dateTime = DateUtil.parse(str);
		assert dateTime != null;
		Assert.assertEquals("2019-06-04 16:25:15", dateTime.toString());
	}

	@Test
	public void parseTest7() {
		String str = "2019-06-01T19:45:43.000 +0800";
		DateTime dateTime = DateUtil.parse(str, "yyyy-MM-dd'T'HH:mm:ss.SSS Z");
		Assert.assertEquals("2019-06-01 19:45:43", dateTime.toString());
	}

	@Test
	public void parseAndOffsetTest() {
		// 检查UTC时间偏移是否准确
		String str = "2019-09-17T13:26:17.948Z";
		DateTime dateTime = DateUtil.parse(str);
		assert dateTime != null;
		Assert.assertEquals("2019-09-17 13:26:17", dateTime.toString());

		DateTime offset = DateUtil.offsetHour(dateTime, 8);
		Assert.assertEquals("2019-09-17 21:26:17", offset.toString());
	}

	@Test
	public void parseDateTest() {
		String dateStr = "2018-4-10";
		Date date = DateUtil.parseDate(dateStr);
		String format = DateUtil.format(date, DatePattern.NORM_DATE_PATTERN);
		Assert.assertEquals("2018-04-10", format);
	}

	@Test
	public void parseToDateTimeTest1() {
		String dateStr1 = "2017-02-01";
		String dateStr2 = "2017/02/01";
		String dateStr3 = "2017.02.01";
		String dateStr4 = "2017年02月01日";

		DateTime dt1 = DateUtil.parse(dateStr1);
		DateTime dt2 = DateUtil.parse(dateStr2);
		DateTime dt3 = DateUtil.parse(dateStr3);
		DateTime dt4 = DateUtil.parse(dateStr4);
		Assert.assertEquals(dt1, dt2);
		Assert.assertEquals(dt2, dt3);
		Assert.assertEquals(dt3, dt4);
	}

	@Test
	public void parseToDateTimeTest2() {
		String dateStr1 = "2017-02-01 12:23";
		String dateStr2 = "2017/02/01 12:23";
		String dateStr3 = "2017.02.01 12:23";
		String dateStr4 = "2017年02月01日 12:23";

		DateTime dt1 = DateUtil.parse(dateStr1);
		DateTime dt2 = DateUtil.parse(dateStr2);
		DateTime dt3 = DateUtil.parse(dateStr3);
		DateTime dt4 = DateUtil.parse(dateStr4);
		Assert.assertEquals(dt1, dt2);
		Assert.assertEquals(dt2, dt3);
		Assert.assertEquals(dt3, dt4);
	}

	@Test
	public void parseToDateTimeTest3() {
		String dateStr1 = "2017-02-01 12:23:45";
		String dateStr2 = "2017/02/01 12:23:45";
		String dateStr3 = "2017.02.01 12:23:45";
		String dateStr4 = "2017年02月01日 12时23分45秒";

		DateTime dt1 = DateUtil.parse(dateStr1);
		DateTime dt2 = DateUtil.parse(dateStr2);
		DateTime dt3 = DateUtil.parse(dateStr3);
		DateTime dt4 = DateUtil.parse(dateStr4);
		Assert.assertEquals(dt1, dt2);
		Assert.assertEquals(dt2, dt3);
		Assert.assertEquals(dt3, dt4);
	}

	@Test
	public void parseToDateTimeTest4() {
		String dateStr1 = "2017-02-01 12:23:45";
		String dateStr2 = "20170201122345";

		DateTime dt1 = DateUtil.parse(dateStr1);
		DateTime dt2 = DateUtil.parse(dateStr2);
		Assert.assertEquals(dt1, dt2);
	}

	@Test
	public void parseToDateTimeTest5() {
		String dateStr1 = "2017-02-01";
		String dateStr2 = "20170201";

		DateTime dt1 = DateUtil.parse(dateStr1);
		DateTime dt2 = DateUtil.parse(dateStr2);
		Assert.assertEquals(dt1, dt2);
	}

	@Test
	public void parseUTCTest() {
		String dateStr1 = "2018-09-13T05:34:31Z";
		DateTime dt = DateUtil.parseUTC(dateStr1);

		// parse方法支持UTC格式测试
		DateTime dt2 = DateUtil.parse(dateStr1);
		Assert.assertEquals(dt, dt2);

		// 默认使用Pattern对应的时区，即UTC时区
		String dateStr = dt.toString();
		Assert.assertEquals("2018-09-13 05:34:31", dateStr);

		// 使用当前（上海）时区
		dateStr = dt.toString(TimeZone.getTimeZone("GMT+8:00"));
		Assert.assertEquals("2018-09-13 13:34:31", dateStr);

		dateStr1 = "2018-09-13T13:34:32+0800";
		dt = DateUtil.parseUTC(dateStr1);
		dateStr = dt.toString(TimeZone.getTimeZone("GMT+8:00"));
		Assert.assertEquals("2018-09-13 13:34:32", dateStr);

		dateStr1 = "2018-09-13T13:34:33+08:00";
		dt = DateUtil.parseUTC(dateStr1);
		dateStr = dt.toString(TimeZone.getTimeZone("GMT+8:00"));
		Assert.assertEquals("2018-09-13 13:34:33", dateStr);

		dateStr1 = "2018-09-13T13:34:34+0800";
		dt = DateUtil.parse(dateStr1);
		assert dt != null;
		dateStr = dt.toString(TimeZone.getTimeZone("GMT+8:00"));
		Assert.assertEquals("2018-09-13 13:34:34", dateStr);

		dateStr1 = "2018-09-13T13:34:35+08:00";
		dt = DateUtil.parse(dateStr1);
		assert dt != null;
		dateStr = dt.toString(TimeZone.getTimeZone("GMT+8:00"));
		Assert.assertEquals("2018-09-13 13:34:35", dateStr);

		dateStr1 = "2018-09-13T13:34:36.999+0800";
		dt = DateUtil.parseUTC(dateStr1);
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DatePattern.NORM_DATETIME_MS_PATTERN);
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		dateStr = dt.toString(simpleDateFormat);
		Assert.assertEquals("2018-09-13 13:34:36.999", dateStr);

		dateStr1 = "2018-09-13T13:34:37.999+08:00";
		dt = DateUtil.parseUTC(dateStr1);
		dateStr = dt.toString(simpleDateFormat);
		Assert.assertEquals("2018-09-13 13:34:37.999", dateStr);

		dateStr1 = "2018-09-13T13:34:38.999+0800";
		dt = DateUtil.parse(dateStr1);
		assert dt != null;
		dateStr = dt.toString(simpleDateFormat);
		Assert.assertEquals("2018-09-13 13:34:38.999", dateStr);

		dateStr1 = "2018-09-13T13:34:39.999+08:00";
		dt = DateUtil.parse(dateStr1);
		assert dt != null;
		dateStr = dt.toString(simpleDateFormat);
		Assert.assertEquals("2018-09-13 13:34:39.999", dateStr);
	}

	@Test
	public void parseCSTTest(){
		String dateStr = "Wed Sep 16 11:26:23 CST 2009";

		SimpleDateFormat sdf = new SimpleDateFormat(DatePattern.JDK_DATETIME_PATTERN, Locale.US);
		final DateTime parse = DateUtil.parse(dateStr, sdf);

		DateTime dateTime = DateUtil.parseCST(dateStr);
		Assert.assertEquals(parse, dateTime);

		dateTime = DateUtil.parse(dateStr);
		Assert.assertEquals(parse, dateTime);
	}

	@Test
	public void parseCSTTest2(){
		String dateStr = "Wed Sep 16 11:26:23 CST 2009";

		SimpleDateFormat sdf = new SimpleDateFormat(DatePattern.JDK_DATETIME_PATTERN, Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
		final DateTime parse = DateUtil.parse(dateStr, sdf);

		FastDateFormat fdf = FastDateFormat.getInstance(DatePattern.JDK_DATETIME_PATTERN, TimeZone.getTimeZone("America/Chicago"), Locale.US);
		final DateTime parse2 = DateUtil.parse(dateStr, fdf);

		Assert.assertEquals(parse, parse2);
	}

	@Test
	public void parseJDkTest() {
		String dateStr = "Thu May 16 17:57:18 GMT+08:00 2019";
		DateTime time = DateUtil.parse(dateStr);
		Assert.assertEquals("2019-05-16 17:57:18", Objects.requireNonNull(time).toString());
	}

	@Test
	public void parseISOTest() {
		String dateStr = "2020-04-23T02:31:00.000Z";
		DateTime time = DateUtil.parse(dateStr);
		Assert.assertEquals("2020-04-23 02:31:00", Objects.requireNonNull(time).toString());
	}

	@Test
	public void endOfYearTest() {
		DateTime date = DateUtil.date();
		date.setField(DateField.YEAR, 2019);
		DateTime endOfYear = DateUtil.endOfYear(date);
		Assert.assertEquals("2019-12-31 23:59:59", endOfYear.toString());
	}

	@Test
	public void endOfWeekTest() {
		// 周日
		DateTime now = DateUtil.parse("2019-09-15 13:00");

		DateTime startOfWeek = DateUtil.beginOfWeek(now);
		Assert.assertEquals("2019-09-09 00:00:00", startOfWeek.toString());
		DateTime endOfWeek = DateUtil.endOfWeek(now);
		Assert.assertEquals("2019-09-15 23:59:59", endOfWeek.toString());

		long between = DateUtil.between(endOfWeek, startOfWeek, DateUnit.DAY);
		// 周一和周日相距6天
		Assert.assertEquals(6, between);
	}

	@Test
	public void dayOfWeekTest() {
		int dayOfWeek = DateUtil.dayOfWeek(DateUtil.parse("2018-03-07"));
		Assert.assertEquals(Calendar.WEDNESDAY, dayOfWeek);
		Week week = DateUtil.dayOfWeekEnum(DateUtil.parse("2018-03-07"));
		Assert.assertEquals(Week.WEDNESDAY, week);
	}

	@Test
	public void rangeTest() {
		DateTime start = DateUtil.parse("2017-01-01");
		DateTime end = DateUtil.parse("2017-01-03");

		// 测试包含开始和结束情况下步进为1的情况
		DateRange range = DateUtil.range(start, end, DateField.DAY_OF_YEAR);
		Assert.assertEquals(range.next(), DateUtil.parse("2017-01-01"));
		Assert.assertEquals(range.next(), DateUtil.parse("2017-01-02"));
		Assert.assertEquals(range.next(), DateUtil.parse("2017-01-03"));
		try {
			range.next();
			Assert.fail("已超过边界，下一个元素不应该存在！");
		} catch (NoSuchElementException ignored) {
		}

		// 测试多步进的情况
		range = new DateRange(start, end, DateField.DAY_OF_YEAR, 2);
		Assert.assertEquals(range.next(), DateUtil.parse("2017-01-01"));
		Assert.assertEquals(range.next(), DateUtil.parse("2017-01-03"));

		// 测试不包含开始结束时间的情况
		range = new DateRange(start, end, DateField.DAY_OF_YEAR, 1, false, false);
		Assert.assertEquals(range.next(), DateUtil.parse("2017-01-02"));
		try {
			range.next();
			Assert.fail("不包含结束时间情况下，下一个元素不应该存在！");
		} catch (NoSuchElementException ignored) {
		}
	}

	@Test
	public void rangeToListTest() {
		DateTime start = DateUtil.parse("2017-01-01");
		DateTime end = DateUtil.parse("2017-01-31");

		List<DateTime> rangeToList = DateUtil.rangeToList(start, end, DateField.DAY_OF_YEAR);
		Assert.assertEquals(rangeToList.get(0), DateUtil.parse("2017-01-01"));
		Assert.assertEquals(rangeToList.get(1), DateUtil.parse("2017-01-02"));
	}

	@Test
	public void yearAndQTest() {
		String yearAndQuarter = DateUtil.yearAndQuarter(DateUtil.parse("2018-12-01"));
		Assert.assertEquals("20184", yearAndQuarter);

		LinkedHashSet<String> yearAndQuarters = DateUtil.yearAndQuarter(DateUtil.parse("2018-09-10"), DateUtil.parse("2018-12-20"));
		List<String> list = CollUtil.list(false, yearAndQuarters);
		Assert.assertEquals(2, list.size());
		Assert.assertEquals("20183", list.get(0));
		Assert.assertEquals("20184", list.get(1));

		LinkedHashSet<String> yearAndQuarters2 = DateUtil.yearAndQuarter(DateUtil.parse("2018-10-10"), DateUtil.parse("2018-12-10"));
		List<String> list2 = CollUtil.list(false, yearAndQuarters2);
		Assert.assertEquals(1, list2.size());
		Assert.assertEquals("20184", list2.get(0));
	}

	@Test
	public void formatHttpDateTest() {
		String formatHttpDate = DateUtil.formatHttpDate(DateUtil.parse("2019-01-02 22:32:01"));
		Assert.assertEquals("Wed, 02 Jan 2019 14:32:01 GMT", formatHttpDate);
	}

	@Test
	public void toInstantTest(){
		LocalDateTime localDateTime = LocalDateTime.parse("2017-05-06T08:30:00", DateTimeFormatter.ISO_DATE_TIME);
		Instant instant = DateUtil.toInstant(localDateTime);
		Assert.assertEquals("2017-05-06T00:30:00Z", instant.toString());

		LocalDate localDate = localDateTime.toLocalDate();
		instant = DateUtil.toInstant(localDate);
		Assert.assertNotNull(instant);

		LocalTime localTime = localDateTime.toLocalTime();
		instant = DateUtil.toInstant(localTime);
		Assert.assertNotNull(instant);
	}

	@Test
	public void dateTest(){
		LocalDateTime localDateTime = LocalDateTime.parse("2017-05-06T08:30:00", DateTimeFormatter.ISO_DATE_TIME);
		DateTime date = DateUtil.date(localDateTime);
		Assert.assertEquals("2017-05-06 08:30:00", date.toString());
	}

	@Test
	public void dateTest2(){
		// 测试负数日期
		long dateLong = -1497600000;
		final DateTime date = DateUtil.date(dateLong);
		Assert.assertEquals("1969-12-15 00:00:00", date.toString());
	}

	@Test
	public void ageTest(){
		String d1 = "2000-02-29";
		String d2 = "2018-02-28";
		final int age = DateUtil.age(DateUtil.parseDate(d1), DateUtil.parseDate(d2));
		Assert.assertEquals(18, age);
	}

	@Test(expected = IllegalArgumentException.class)
	public void ageTest2(){
		String d1 = "2019-02-29";
		String d2 = "2018-02-28";
		DateUtil.age(DateUtil.parseDate(d1), DateUtil.parseDate(d2));
	}

	@Test
	public void isExpiredTest(){
		DateTime startDate = DateUtil.parse("2019-12-01 17:02:30");
		DateTime endDate = DateUtil.parse("2019-12-02 17:02:30");
		int length = 3;
		//noinspection deprecation
		boolean expired = DateUtil.isExpired(startDate, DateField.DAY_OF_YEAR, length, endDate);
		Assert.assertTrue(expired);
	}
	
	@Test
	public void localDateTimeTest() {
		// 测试字符串与LocalDateTime的互相转换
		String strDate = "2019-12-01 17:02:30";
		LocalDateTime ldt = DateUtil.parseLocalDateTime(strDate);
		String strDate1 = DateUtil.formatLocalDateTime(ldt);
		Assert.assertEquals(strDate, strDate1);
		
		String strDate2 = "2019年12月01日 17:02:30.111";
		ldt = DateUtil.parseLocalDateTime(strDate2, DatePattern.NORM_DATETIME_MS_PATTERN);
		strDate1 = DateUtil.format(ldt, DatePattern.NORM_DATETIME_PATTERN);
		Assert.assertEquals(strDate, strDate1);
	}

	@Test
	public void localDateTimeTest2() {
		// 测试字符串与LocalDateTime的互相转换
		String strDate = "2019-12-01";
		final LocalDateTime localDateTime = DateUtil.parseLocalDateTime(strDate, "yyyy-MM-dd");
		Assert.assertEquals(strDate, DateUtil.format(localDateTime, DatePattern.NORM_DATE_PATTERN));
	}

	@Test
	public void betweenWeekTest() {
		final DateTime start = DateUtil.parse("2019-03-05");
		final DateTime end = DateUtil.parse("2019-10-05");

		final long weekCount = DateUtil.betweenWeek(start, end, true);
		Assert.assertEquals(30L, weekCount);
	}

	@Test
	public void dayOfYearTest() {
		int dayOfYear = DateUtil.dayOfYear(DateUtil.parse("2020-01-01"));
		Assert.assertEquals(1, dayOfYear);
		int lengthOfYear = DateUtil.lengthOfYear(2020);
		Assert.assertEquals(366, lengthOfYear);
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void parseSingleNumberTest(){
		DateTime dateTime = DateUtil.parse("2020-5-08");
		Assert.assertEquals("2020-05-08 00:00:00", dateTime.toString());
		dateTime = DateUtil.parse("2020-5-8");
		Assert.assertEquals("2020-05-08 00:00:00", dateTime.toString());
		dateTime = DateUtil.parse("2020-05-8");
		Assert.assertEquals("2020-05-08 00:00:00", dateTime.toString());

		//datetime
		dateTime = DateUtil.parse("2020-5-8 3:12:3");
		Assert.assertEquals("2020-05-08 03:12:03", dateTime.toString());
		dateTime = DateUtil.parse("2020-5-8 3:2:3");
		Assert.assertEquals("2020-05-08 03:02:03", dateTime.toString());
	}
}
