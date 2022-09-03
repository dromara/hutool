package cn.hutool.core.date;

import cn.hutool.core.lang.Console;
import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;

public class LocalDateTimeUtilTest {

	@Test
	public void nowTest() {
		Assert.assertNotNull(LocalDateTimeUtil.now());
	}

	@Test
	public void ofTest() {
		String dateStr = "2020-01-23T12:23:56";
		final DateTime dt = DateUtil.parse(dateStr);

		LocalDateTime of = LocalDateTimeUtil.of(dt);
		Assert.assertNotNull(of);
		Assert.assertEquals(dateStr, of.toString());

		of = LocalDateTimeUtil.ofUTC(dt.getTime());
		Assert.assertEquals(dateStr, of.toString());
	}

	@Test
	public void parseOffsetTest() {
		final LocalDateTime localDateTime = LocalDateTimeUtil.parse("2021-07-30T16:27:27+08:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		Assert.assertEquals("2021-07-30T16:27:27", localDateTime.toString());
	}

	@Test
	public void parseTest() {
		final LocalDateTime localDateTime = LocalDateTimeUtil.parse("2020-01-23T12:23:56", DateTimeFormatter.ISO_DATE_TIME);
		Assert.assertEquals("2020-01-23T12:23:56", localDateTime.toString());
	}

	@Test
	public void parseTest2() {
		final LocalDateTime localDateTime = LocalDateTimeUtil.parse("2020-01-23", DatePattern.NORM_DATE_PATTERN);
		Assert.assertEquals("2020-01-23T00:00", localDateTime.toString());
	}

	@Test
	public void parseTest3() {
		final LocalDateTime localDateTime = LocalDateTimeUtil.parse("12:23:56", DatePattern.NORM_TIME_PATTERN);
		Assert.assertEquals("12:23:56", localDateTime.toLocalTime().toString());
	}

	@Test
	public void parseTest4() {
		final LocalDateTime localDateTime = LocalDateTimeUtil.parse("2020-01-23T12:23:56");
		Assert.assertEquals("2020-01-23T12:23:56", localDateTime.toString());
	}

	@Test
	public void parseTest5() {
		LocalDateTime localDateTime = LocalDateTimeUtil.parse("19940121183604", "yyyyMMddHHmmss");
		Assert.assertEquals("1994-01-21T18:36:04", localDateTime.toString());
	}

	@Test
	public void parseTest6() {
		LocalDateTime localDateTime = LocalDateTimeUtil.parse("19940121183604682", "yyyyMMddHHmmssSSS");
		Assert.assertEquals("1994-01-21T18:36:04.682", localDateTime.toString());

		localDateTime = LocalDateTimeUtil.parse("1994012118360468", "yyyyMMddHHmmssSS");
		Assert.assertEquals("1994-01-21T18:36:04.680", localDateTime.toString());

		localDateTime = LocalDateTimeUtil.parse("199401211836046", "yyyyMMddHHmmssS");
		Assert.assertEquals("1994-01-21T18:36:04.600", localDateTime.toString());
	}

	@Test
	public void parseDateTest() {
		LocalDate localDate = LocalDateTimeUtil.parseDate("2020-01-23");
		Assert.assertEquals("2020-01-23", localDate.toString());

		localDate = LocalDateTimeUtil.parseDate("2020-01-23T12:23:56", DateTimeFormatter.ISO_DATE_TIME);
		Assert.assertEquals("2020-01-23", localDate.toString());
	}

	@Test
	public void parseSingleMonthAndDayTest() {
		LocalDate localDate = LocalDateTimeUtil.parseDate("2020-1-1", "yyyy-M-d");
		Assert.assertEquals("2020-01-01", localDate.toString());
	}

	@Test
	public void formatTest() {
		final LocalDateTime localDateTime = LocalDateTimeUtil.parse("2020-01-23T12:23:56");
		String format = LocalDateTimeUtil.format(localDateTime, DatePattern.NORM_DATETIME_PATTERN);
		Assert.assertEquals("2020-01-23 12:23:56", format);

		format = LocalDateTimeUtil.formatNormal(localDateTime);
		Assert.assertEquals("2020-01-23 12:23:56", format);

		format = LocalDateTimeUtil.format(localDateTime, DatePattern.NORM_DATE_PATTERN);
		Assert.assertEquals("2020-01-23", format);
	}

	@Test
	public void formatLocalDateTest() {
		final LocalDate date = LocalDate.parse("2020-01-23");
		String format = LocalDateTimeUtil.format(date, DatePattern.NORM_DATE_PATTERN);
		Assert.assertEquals("2020-01-23", format);

		format = LocalDateTimeUtil.formatNormal(date);
		Assert.assertEquals("2020-01-23", format);
	}

	@Test
	public void offset() {
		final LocalDateTime localDateTime = LocalDateTimeUtil.parse("2020-01-23T12:23:56");
		LocalDateTime offset = LocalDateTimeUtil.offset(localDateTime, 1, ChronoUnit.DAYS);
		// 非同一对象
		Assert.assertNotSame(localDateTime, offset);

		Assert.assertEquals("2020-01-24T12:23:56", offset.toString());

		offset = LocalDateTimeUtil.offset(localDateTime, -1, ChronoUnit.DAYS);
		Assert.assertEquals("2020-01-22T12:23:56", offset.toString());
	}

	@Test
	public void between() {
		final Duration between = LocalDateTimeUtil.between(
				LocalDateTimeUtil.parse("2019-02-02T00:00:00"),
				LocalDateTimeUtil.parse("2020-02-02T00:00:00"));
		Assert.assertEquals(365, between.toDays());
	}

	@Test
	public void isBetween() {
		// 时间范围 8点-9点
		LocalDateTime start = LocalDateTime.parse("2019-02-02T08:00:00");
		LocalDateTime end = LocalDateTime.parse("2019-02-02T09:00:00");

		// 不在时间范围内 用例
		Assert.assertFalse(LocalDateTimeUtil.isBetween(LocalDateTime.parse("2019-02-02T06:00:00"), start, end));
		Assert.assertFalse(LocalDateTimeUtil.isBetween(LocalDateTime.parse("2019-02-02T13:00:00"), start, end));
		Assert.assertFalse(LocalDateTimeUtil.isBetween(LocalDateTime.parse("2019-02-01T08:00:00"), start, end));
		Assert.assertFalse(LocalDateTimeUtil.isBetween(LocalDateTime.parse("2019-02-03T09:00:00"), start, end));

		// 在时间范围内 用例
		Assert.assertTrue(LocalDateTimeUtil.isBetween(LocalDateTime.parse("2019-02-02T08:00:00"), start, end));
		Assert.assertTrue(LocalDateTimeUtil.isBetween(LocalDateTime.parse("2019-02-02T08:00:01"), start, end));
		Assert.assertTrue(LocalDateTimeUtil.isBetween(LocalDateTime.parse("2019-02-02T08:11:00"), start, end));
		Assert.assertTrue(LocalDateTimeUtil.isBetween(LocalDateTime.parse("2019-02-02T08:22:00"), start, end));
		Assert.assertTrue(LocalDateTimeUtil.isBetween(LocalDateTime.parse("2019-02-02T08:59:59"), start, end));
		Assert.assertTrue(LocalDateTimeUtil.isBetween(LocalDateTime.parse("2019-02-02T09:00:00"), start, end));

		// 测试边界条件
		Assert.assertTrue(LocalDateTimeUtil.isBetween(start, start, end, true, false));
		Assert.assertFalse(LocalDateTimeUtil.isBetween(start, start, end, false, false));
		Assert.assertTrue(LocalDateTimeUtil.isBetween(end, start, end, false, true));
		Assert.assertFalse(LocalDateTimeUtil.isBetween(end, start, end, false, false));

		// 异常入参
		Assert.assertThrows(IllegalArgumentException.class, ()->LocalDateTimeUtil.isBetween(null, start, end, false, false));
		Assert.assertThrows(IllegalArgumentException.class, ()->LocalDateTimeUtil.isBetween(start, null, end, false, false));
		Assert.assertThrows(IllegalArgumentException.class, ()->LocalDateTimeUtil.isBetween(start, start, null, false, false));
		Assert.assertThrows(IllegalArgumentException.class, ()->LocalDateTimeUtil.isBetween(start, end, start, false, false));
	}

	@Test
	public void beginOfDayTest() {
		final LocalDateTime localDateTime = LocalDateTimeUtil.parse("2020-01-23T12:23:56");
		final LocalDateTime beginOfDay = LocalDateTimeUtil.beginOfDay(localDateTime);
		Assert.assertEquals("2020-01-23T00:00", beginOfDay.toString());
	}

	@Test
	public void endOfDayTest() {
		final LocalDateTime localDateTime = LocalDateTimeUtil.parse("2020-01-23T12:23:56");

		LocalDateTime endOfDay = LocalDateTimeUtil.endOfDay(localDateTime);
		Assert.assertEquals("2020-01-23T23:59:59.999999999", endOfDay.toString());

		endOfDay = LocalDateTimeUtil.endOfDay(localDateTime, true);
		Assert.assertEquals("2020-01-23T23:59:59", endOfDay.toString());
	}

	@Test
	public void dayOfWeekTest() {
		final Week one = LocalDateTimeUtil.dayOfWeek(LocalDate.of(2021, 9, 20));
		Assert.assertEquals(Week.MONDAY, one);

		final Week two = LocalDateTimeUtil.dayOfWeek(LocalDate.of(2021, 9, 21));
		Assert.assertEquals(Week.TUESDAY, two);

		final Week three = LocalDateTimeUtil.dayOfWeek(LocalDate.of(2021, 9, 22));
		Assert.assertEquals(Week.WEDNESDAY, three);

		final Week four = LocalDateTimeUtil.dayOfWeek(LocalDate.of(2021, 9, 23));
		Assert.assertEquals(Week.THURSDAY, four);

		final Week five = LocalDateTimeUtil.dayOfWeek(LocalDate.of(2021, 9, 24));
		Assert.assertEquals(Week.FRIDAY, five);

		final Week six = LocalDateTimeUtil.dayOfWeek(LocalDate.of(2021, 9, 25));
		Assert.assertEquals(Week.SATURDAY, six);

		final Week seven = LocalDateTimeUtil.dayOfWeek(LocalDate.of(2021, 9, 26));
		Assert.assertEquals(Week.SUNDAY, seven);
	}

	@Test
	public void isOverlapTest(){
		LocalDateTime oneStartTime = LocalDateTime.of(2022, 1, 1, 10, 10, 10);
		LocalDateTime oneEndTime = LocalDateTime.of(2022, 1, 1, 11, 10, 10);

		LocalDateTime oneStartTime2 = LocalDateTime.of(2022, 1, 1, 11, 20, 10);
		LocalDateTime oneEndTime2 = LocalDateTime.of(2022, 1, 1, 11, 30, 10);

		LocalDateTime oneStartTime3 = LocalDateTime.of(2022, 1, 1, 11, 40, 10);
		LocalDateTime oneEndTime3 = LocalDateTime.of(2022, 1, 1, 11, 50, 10);

		//真实请假数据
		LocalDateTime realStartTime = LocalDateTime.of(2022, 1, 1, 11, 49, 10);
		LocalDateTime realEndTime = LocalDateTime.of(2022, 1, 1, 12, 0, 10);

		LocalDateTime realStartTime1 = DateUtil.parseLocalDateTime("2022-03-01 08:00:00");
		LocalDateTime realEndTime1   = DateUtil.parseLocalDateTime("2022-03-01 10:00:00");

		LocalDateTime startTime  = DateUtil.parseLocalDateTime("2022-03-23 05:00:00");
		LocalDateTime endTime    = DateUtil.parseLocalDateTime("2022-03-23 13:00:00");

		Assert.assertFalse(LocalDateTimeUtil.isOverlap(oneStartTime,oneEndTime,realStartTime,realEndTime));
		Assert.assertFalse(LocalDateTimeUtil.isOverlap(oneStartTime2,oneEndTime2,realStartTime,realEndTime));
		Assert.assertTrue(LocalDateTimeUtil.isOverlap(oneStartTime3,oneEndTime3,realStartTime,realEndTime));

		Assert.assertFalse(LocalDateTimeUtil.isOverlap(realStartTime1,realEndTime1,startTime,endTime));
		Assert.assertFalse(LocalDateTimeUtil.isOverlap(startTime,endTime,realStartTime1,realEndTime1));
	}

	@Test
	public void weekOfYearTest(){
		LocalDate date1 = LocalDate.of(2021, 12, 31);
		final int weekOfYear1 = LocalDateTimeUtil.weekOfYear(date1);
		Assert.assertEquals(52, weekOfYear1);

		final int weekOfYear2 = LocalDateTimeUtil.weekOfYear(date1.atStartOfDay());
		Assert.assertEquals(52, weekOfYear2);
	}

	@Test
	public void weekOfYearTest2(){
		LocalDate date1 = LocalDate.of(2022, 1, 31);
		final int weekOfYear1 = LocalDateTimeUtil.weekOfYear(date1);
		Assert.assertEquals(5, weekOfYear1);

		final int weekOfYear2 = LocalDateTimeUtil.weekOfYear(date1.atStartOfDay());
		Assert.assertEquals(5, weekOfYear2);
	}

	@Test
	public void ofTest2(){
		final Instant instant = DateUtil.parse("2022-02-22").toInstant();
		final LocalDateTime of = LocalDateTimeUtil.of((TemporalAccessor) instant);
		Console.log(of);
	}

	@Test
	public void parseBlankTest(){
		final LocalDateTime parse = LocalDateTimeUtil.parse("");
		Assert.assertNull(parse);
	}
}
