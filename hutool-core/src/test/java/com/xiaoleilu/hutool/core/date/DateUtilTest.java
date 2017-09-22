package com.xiaoleilu.hutool.core.date;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.date.BetweenFormater.Level;
import com.xiaoleilu.hutool.date.DateField;
import com.xiaoleilu.hutool.date.DateTime;
import com.xiaoleilu.hutool.date.DateUnit;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.date.TimeInterval;
import com.xiaoleilu.hutool.lang.Console;

/**
 * 时间工具单元测试
 * 
 * @author Looly
 *
 */
public class DateUtilTest {
	
	@Test
	public void dateTest(){
		long current = DateUtil.current(false);
		Console.log(current);
		DateTime date = DateUtil.date(current);
		Console.log(date);
	}

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
	public void offsetDateTest() {
		String dateStr = "2017-03-01 22:33:23";
		Date date = DateUtil.parse(dateStr);
		
		Date newDate = DateUtil.offset(date, DateField.DAY_OF_MONTH, 2);
		Assert.assertEquals("2017-03-03 22:33:23", newDate.toString());
		
		//常用偏移
		DateTime newDate2 = DateUtil.offsetDay(date, 3);
		Assert.assertEquals("2017-03-04 22:33:23", newDate2.toString());
		//常用偏移
		DateTime newDate3 = DateUtil.offsetHour(date, -3);
		Assert.assertEquals("2017-03-01 19:33:23", newDate3.toString());
	}
	
	@Test
	public void betweenTest() {
		String dateStr1 = "2017-03-01 22:34:23";
		Date date1 = DateUtil.parse(dateStr1);
		
		String dateStr2 = "2017-04-01 23:56:14";
		Date date2 = DateUtil.parse(dateStr2);
		
		//相差月
		long betweenMonth = DateUtil.betweenMonth(date1, date2, false);
		Assert.assertEquals(1, betweenMonth);//相差一个月
		
		//相差天
		long betweenDay = DateUtil.between(date1, date2, DateUnit.DAY);
		Assert.assertEquals(31, betweenDay);//相差一个月，31天
		
		//相差毫秒
		long between = DateUtil.between(date1, date2, DateUnit.MS);
		String formatBetween = DateUtil.formatBetween(between, Level.MINUTE);
		Assert.assertEquals("31天1小时21分", formatBetween);
	}
	
	@Test
	public void timerTest(){
		TimeInterval timer = DateUtil.timer();
		
		//---------------------------------
		//-------这是执行过程
		//---------------------------------
		
		timer.interval();//花费毫秒数
		timer.intervalRestart();//返回花费时间，并重置开始时间
		timer.intervalMinute();//花费分钟数
	}
	
	@Test
	public void currentTest(){
		long current = DateUtil.current(false);
		String currentStr = String.valueOf(current);
		Assert.assertEquals(13, currentStr.length());
		
		long currentNano = DateUtil.current(true);
		String currentNanoStr = String.valueOf(currentNano);
		Assert.assertNotNull(currentNanoStr);
	}
	
	@Test
	public void weekOfYearTest() {
		//第一周周日
		int weekOfYear1 = DateUtil.weekOfYear(DateUtil.parse("2016-01-03"));
		Assert.assertEquals(1, weekOfYear1);
		
		//第二周周四
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
	public void secondToTime() {
		String time = DateUtil.secondToTime(3600);
		Assert.assertEquals("01:00:00", time);
		time = DateUtil.secondToTime(3800);
		Assert.assertEquals("01:03:20", time);
		time = DateUtil.secondToTime(0);
		Assert.assertEquals("00:00:00", time);
		time = DateUtil.secondToTime(30);
		Assert.assertEquals("00:00:30", time);
	}
}
