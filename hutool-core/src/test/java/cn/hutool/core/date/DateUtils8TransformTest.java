package cn.hutool.core.date;

import org.junit.Assert;
import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static cn.hutool.core.date.DatePattern.*;

/**
 * DateUtils8Transform LocalDate和Date的互换 测试用例
 *
 * @author dazer
 * @date 2021/3/16 22:23
 */
public class DateUtils8TransformTest {

	/**
	 * 01. java.util.Date --> java.time.LocalDateTime
	 */
	@Test
	public void date2Java8DateTimeTest() {
		String str = "2021-03-16 23:59:59";
		DateTime nowDateTime = DateTime.of(str, NORM_DATETIME_PATTERN);
		LocalDateTime now = DateUtils8Transform.date2Java8DateTime(nowDateTime);
		Assert.assertEquals(LocalDateTimeUtil.format(now, DatePattern.NORM_DATETIME_FORMATTER), str);
	}

	/**
	 * 02. java.util.Date --> java.time.LocalDate
	 */
	@Test
	public void date2Java8DateTest() {
		String str = "2021-03-16";
		DateTime nowDateTime = DateTime.of(str, NORM_DATE_PATTERN);
		LocalDate now = DateUtils8Transform.date2Java8Date(nowDateTime);
		Assert.assertEquals(LocalDateTimeUtil.format(now, DatePattern.NORM_DATE_PATTERN), str);
	}

	/**
	 * 03. java.util.Date --> java.time.LocalTime
	 */
	@Test
	public void date2Java8TimeTest() {
		String str = "23:59:59";
		DateTime nowDateTime = DateTime.of(str, NORM_TIME_PATTERN);
		LocalTime now = DateUtils8Transform.date2Java8Time(nowDateTime);
		now.format(DateTimeFormatter.ofPattern(NORM_TIME_PATTERN));
		Assert.assertEquals(now.format(DateTimeFormatter.ofPattern(NORM_TIME_PATTERN)), str);
	}


	/**
	 * 04. java.time.LocalDateTime --> java.util.Date
	 */
	@Test
	public void java8Date2DateTest1() {
		String str = "2021-03-16 23:59:59";
		LocalDateTime now = LocalDateTimeUtil.parse(str, NORM_DATETIME_FORMATTER);
		Date nowDate = DateUtils8Transform.java8Date2Date(now);
		String temp1 = DateUtil.format(nowDate, NORM_DATE_FORMAT);
		Assert.assertEquals(LocalDateTimeUtil.of(nowDate), now);
	}


	/**
	 * 05. java.time.LocalDate --> java.util.Date
	 */
	@Test
	public void java8Date2DateTest2() {
		String str = "2021-03-16";
		LocalDate now = LocalDateTimeUtil.parseDate(str);
		Date nowDate = DateUtils8Transform.java8Date2Date(now);
		String temp1 = DateUtil.format(nowDate, NORM_DATE_FORMAT);
		Assert.assertEquals(temp1, str);
	}

	/**
	 * 06. java.time.LocalTime --> java.util.Date
	 */
	@Test
	public void java8Date2DateTest() {
		String str = "23:59:59";
		LocalTime now = LocalDateTimeUtil.parse(str, DateTimeFormatter.ofPattern(NORM_TIME_PATTERN)).toLocalTime();
		Date nowDate = DateUtils8Transform.java8Date2Date(now);
		String temp1 = DateUtil.format(nowDate, NORM_TIME_PATTERN);
		Assert.assertEquals(temp1, str);
	}

	/**
	 * java.sql.Date与java.util.Date的转化
	 *
	 * @return
	 */
	@Test
	public void sqlDate2DateTest() {
		String str = "2021-03-16";
		LocalDate now = LocalDateTimeUtil.parseDate(str);
		Date date = DateUtils8Transform.sqlDate2Date(java.sql.Date.valueOf(now));
		Assert.assertEquals(new DateTime(date).toDateStr(), str);
	}

	/**
	 * 08. java.util.Date与java.sql.Date的转化
	 */
	@Test
	public void date2SqlDateTest() {
		String str = "2021-03-16";
		java.sql.Date date = DateUtils8Transform.date2SqlDate(DateTime.of(str, NORM_DATE_PATTERN));
		Assert.assertEquals(new DateTime(date).toDateStr(), str);
	}
}
