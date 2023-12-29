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

import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TimeUtilTest {

	@Test
	public void nowTest() {
		Assertions.assertNotNull(TimeUtil.now());
	}

	@Test
	public void ofTest() {
		final String dateStr = "2020-01-23T12:23:56";
		final DateTime dt = DateUtil.parse(dateStr);

		final LocalDateTime of = TimeUtil.of(dt);
		Assertions.assertNotNull(of);
		Assertions.assertEquals(dateStr, of.toString());
	}

	@SuppressWarnings("DataFlowIssue")
	@Test
	public void ofUTCTest() {
		final String dateStr = "2020-01-23T12:23:56Z";
		final DateTime dt = DateUtil.parse(dateStr);

		final LocalDateTime of = TimeUtil.of(dt);
		final LocalDateTime of2 = TimeUtil.ofUTC(dt.getTime());
		Assertions.assertNotNull(of);
		Assertions.assertNotNull(of2);
		Assertions.assertEquals(of, of2);
	}

	@Test
	public void parseOffsetTest() {
		final LocalDateTime localDateTime = TimeUtil.parse("2021-07-30T16:27:27+08:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		Assertions.assertEquals("2021-07-30T16:27:27", Objects.requireNonNull(localDateTime).toString());
	}

	@Test
	public void parseTest() {
		final LocalDateTime localDateTime = TimeUtil.parse("2020-01-23T12:23:56", DateTimeFormatter.ISO_DATE_TIME);
		Assertions.assertEquals("2020-01-23T12:23:56", Objects.requireNonNull(localDateTime).toString());
	}

	@Test
	public void parseTest2() {
		final LocalDateTime localDateTime = TimeUtil.parse("2020-01-23", DatePattern.NORM_DATE_PATTERN);
		Assertions.assertEquals("2020-01-23T00:00", Objects.requireNonNull(localDateTime).toString());
	}

	@Test
	public void parseTest3() {
		final LocalDateTime localDateTime = TimeUtil.parse("12:23:56", DatePattern.NORM_TIME_PATTERN);
		Assertions.assertEquals("12:23:56", Objects.requireNonNull(localDateTime).toLocalTime().toString());
	}

	@Test
	public void parseTest4() {
		final LocalDateTime localDateTime = TimeUtil.parseByISO("2020-01-23T12:23:56");
		Assertions.assertEquals("2020-01-23T12:23:56", localDateTime.toString());
	}

	@Test
	public void parseTest5() {
		final LocalDateTime localDateTime = TimeUtil.parse("19940121183604", "yyyyMMddHHmmss");
		Assertions.assertEquals("1994-01-21T18:36:04", Objects.requireNonNull(localDateTime).toString());
	}

	@Test
	public void parseTest6() {
		LocalDateTime localDateTime = TimeUtil.parse("19940121183604682", "yyyyMMddHHmmssSSS");
		Assertions.assertEquals("1994-01-21T18:36:04.682", Objects.requireNonNull(localDateTime).toString());

		localDateTime = TimeUtil.parse("1994012118360468", "yyyyMMddHHmmssSS");
		Assertions.assertEquals("1994-01-21T18:36:04.680", Objects.requireNonNull(localDateTime).toString());

		localDateTime = TimeUtil.parse("199401211836046", "yyyyMMddHHmmssS");
		Assertions.assertEquals("1994-01-21T18:36:04.600", Objects.requireNonNull(localDateTime).toString());
	}

	@Test
	public void parseDateTest() {
		LocalDate localDate = TimeUtil.parseDateByISO("2020-01-23");
		Assertions.assertEquals("2020-01-23", localDate.toString());

		localDate = TimeUtil.parseDate("2020-01-23T12:23:56", DateTimeFormatter.ISO_DATE_TIME);
		Assertions.assertEquals("2020-01-23", Objects.requireNonNull(localDate).toString());
	}

	@Test
	public void parseSingleMonthAndDayTest() {
		final LocalDate localDate = TimeUtil.parseDate("2020-1-1", "yyyy-M-d");
		Assertions.assertEquals("2020-01-01", Objects.requireNonNull(localDate).toString());
	}

	@Test
	public void formatTest() {
		final LocalDateTime localDateTime = TimeUtil.parseByISO("2020-01-23T12:23:56");
		String format = TimeUtil.format(localDateTime, DatePattern.NORM_DATETIME_PATTERN);
		Assertions.assertEquals("2020-01-23 12:23:56", format);

		format = TimeUtil.formatNormal(localDateTime);
		Assertions.assertEquals("2020-01-23 12:23:56", format);

		format = TimeUtil.format(localDateTime, DatePattern.NORM_DATE_PATTERN);
		Assertions.assertEquals("2020-01-23", format);
	}

	@Test
	public void formatLocalDateTest() {
		final LocalDate date = LocalDate.parse("2020-01-23");
		String format = TimeUtil.format(date, DatePattern.NORM_DATE_PATTERN);
		Assertions.assertEquals("2020-01-23", format);

		format = TimeUtil.formatNormal(date);
		Assertions.assertEquals("2020-01-23", format);
	}

	@Test
	public void offset() {
		final LocalDateTime localDateTime = TimeUtil.parseByISO("2020-01-23T12:23:56");
		LocalDateTime offset = TimeUtil.offset(localDateTime, 1, ChronoUnit.DAYS);
		// 非同一对象
		Assertions.assertNotSame(localDateTime, offset);

		Assertions.assertEquals("2020-01-24T12:23:56", offset.toString());

		offset = TimeUtil.offset(localDateTime, -1, ChronoUnit.DAYS);
		Assertions.assertEquals("2020-01-22T12:23:56", offset.toString());
	}

	@Test
	public void between() {
		final Duration between = TimeUtil.between(
				TimeUtil.parseByISO("2019-02-02T00:00:00"),
				TimeUtil.parseByISO("2020-02-02T00:00:00"));
		Assertions.assertEquals(365, between.toDays());
	}

	@Test
	public void beginOfDayTest() {
		final LocalDateTime localDateTime = TimeUtil.parseByISO("2020-01-23T12:23:56");
		final LocalDateTime beginOfDay = TimeUtil.beginOfDay(localDateTime);
		Assertions.assertEquals("2020-01-23T00:00", beginOfDay.toString());
	}

	@Test
	public void endOfDayTest() {
		final LocalDateTime localDateTime = TimeUtil.parseByISO("2020-01-23T12:23:56");

		LocalDateTime endOfDay = TimeUtil.endOfDay(localDateTime, false);
		Assertions.assertEquals("2020-01-23T23:59:59.999999999", endOfDay.toString());

		endOfDay = TimeUtil.endOfDay(localDateTime, true);
		Assertions.assertEquals("2020-01-23T23:59:59", endOfDay.toString());
	}

	@Test
	public void beginOfMonthTest() {
		final LocalDateTime localDateTime = TimeUtil.parseByISO("2020-01-23T12:23:56");
		final LocalDateTime begin = TimeUtil.beginOfMonth(localDateTime);
		Assertions.assertEquals("2020-01-01T00:00", begin.toString());
	}

	@Test
	public void endOfMonthTest() {
		final LocalDateTime localDateTime = TimeUtil.parseByISO("2020-01-23T12:23:56");

		LocalDateTime end = TimeUtil.endOfMonth(localDateTime, false);
		Assertions.assertEquals("2020-01-31T23:59:59.999999999", end.toString());

		end = TimeUtil.endOfMonth(localDateTime, true);
		Assertions.assertEquals("2020-01-31T23:59:59", end.toString());
	}

	@Test
	public void beginOfYearTest() {
		final LocalDateTime localDateTime = TimeUtil.parseByISO("2020-01-23T12:23:56");
		final LocalDateTime begin = TimeUtil.beginOfMonth(localDateTime);
		Assertions.assertEquals("2020-01-01T00:00", begin.toString());
	}

	@Test
	public void endOfYearTest() {
		final LocalDateTime localDateTime = TimeUtil.parseByISO("2020-01-23T12:23:56");

		LocalDateTime end = TimeUtil.endOfYear(localDateTime, false);
		Assertions.assertEquals("2020-12-31T23:59:59.999999999", end.toString());

		end = TimeUtil.endOfYear(localDateTime, true);
		Assertions.assertEquals("2020-12-31T23:59:59", end.toString());
	}

	@Test
	public void dayOfWeekTest() {
		final Week one = TimeUtil.dayOfWeek(LocalDate.of(2021, 9, 20));
		Assertions.assertEquals(Week.MONDAY, one);

		final Week two = TimeUtil.dayOfWeek(LocalDate.of(2021, 9, 21));
		Assertions.assertEquals(Week.TUESDAY, two);

		final Week three = TimeUtil.dayOfWeek(LocalDate.of(2021, 9, 22));
		Assertions.assertEquals(Week.WEDNESDAY, three);

		final Week four = TimeUtil.dayOfWeek(LocalDate.of(2021, 9, 23));
		Assertions.assertEquals(Week.THURSDAY, four);

		final Week five = TimeUtil.dayOfWeek(LocalDate.of(2021, 9, 24));
		Assertions.assertEquals(Week.FRIDAY, five);

		final Week six = TimeUtil.dayOfWeek(LocalDate.of(2021, 9, 25));
		Assertions.assertEquals(Week.SATURDAY, six);

		final Week seven = TimeUtil.dayOfWeek(LocalDate.of(2021, 9, 26));
		Assertions.assertEquals(Week.SUNDAY, seven);
	}

	@Test
	public void isOverlapTest(){
		final LocalDateTime oneStartTime = LocalDateTime.of(2022, 1, 1, 10, 10, 10);
		final LocalDateTime oneEndTime = LocalDateTime.of(2022, 1, 1, 11, 10, 10);

		final LocalDateTime oneStartTime2 = LocalDateTime.of(2022, 1, 1, 11, 20, 10);
		final LocalDateTime oneEndTime2 = LocalDateTime.of(2022, 1, 1, 11, 30, 10);

		final LocalDateTime oneStartTime3 = LocalDateTime.of(2022, 1, 1, 11, 40, 10);
		final LocalDateTime oneEndTime3 = LocalDateTime.of(2022, 1, 1, 11, 50, 10);

		//真实请假数据
		final LocalDateTime realStartTime = LocalDateTime.of(2022, 1, 1, 11, 49, 10);
		final LocalDateTime realEndTime = LocalDateTime.of(2022, 1, 1, 12, 0, 10);

		final LocalDateTime realStartTime1 = TimeUtil.parseByISO("2022-03-01 08:00:00");
		final LocalDateTime realEndTime1   = TimeUtil.parseByISO("2022-03-01 10:00:00");

		final LocalDateTime startTime  = TimeUtil.parseByISO("2022-03-23 05:00:00");
		final LocalDateTime endTime    = TimeUtil.parseByISO("2022-03-23 13:00:00");

		Assertions.assertFalse(TimeUtil.isOverlap(oneStartTime,oneEndTime,realStartTime,realEndTime));
		Assertions.assertFalse(TimeUtil.isOverlap(oneStartTime2,oneEndTime2,realStartTime,realEndTime));
		Assertions.assertTrue(TimeUtil.isOverlap(oneStartTime3,oneEndTime3,realStartTime,realEndTime));

		Assertions.assertFalse(TimeUtil.isOverlap(realStartTime1,realEndTime1,startTime,endTime));
		Assertions.assertFalse(TimeUtil.isOverlap(startTime,endTime,realStartTime1,realEndTime1));

		Assertions.assertTrue(TimeUtil.isOverlap(startTime,startTime,startTime,startTime));
		Assertions.assertTrue(TimeUtil.isOverlap(startTime,startTime,startTime,endTime));
		Assertions.assertFalse(TimeUtil.isOverlap(startTime,startTime,endTime,endTime));
		Assertions.assertTrue(TimeUtil.isOverlap(startTime,endTime,endTime,endTime));
	}

	@Test
	public void weekOfYearTest(){
		final LocalDate date1 = LocalDate.of(2021, 12, 31);
		final int weekOfYear1 = TimeUtil.weekOfYear(date1);
		Assertions.assertEquals(52, weekOfYear1);

		final int weekOfYear2 = TimeUtil.weekOfYear(date1.atStartOfDay());
		Assertions.assertEquals(52, weekOfYear2);
	}

	@Test
	public void weekOfYearTest2(){
		final LocalDate date1 = LocalDate.of(2022, 1, 31);
		final int weekOfYear1 = TimeUtil.weekOfYear(date1);
		Assertions.assertEquals(5, weekOfYear1);

		final int weekOfYear2 = TimeUtil.weekOfYear(date1.atStartOfDay());
		Assertions.assertEquals(5, weekOfYear2);
	}

	@Test
	public void ofTest2(){
		final Instant instant = Objects.requireNonNull(DateUtil.parse("2022-02-22")).toInstant();
		final LocalDateTime of = TimeUtil.of(instant);
		Console.log(of);
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void isInTest() {
		// 时间范围 8点-9点
		final LocalDateTime begin = LocalDateTime.parse("2019-02-02T08:00:00");
		final LocalDateTime end = LocalDateTime.parse("2019-02-02T09:00:00");

		// 不在时间范围内 用例
		Assertions.assertFalse(TimeUtil.isIn(LocalDateTime.parse("2019-02-02T06:00:00"), begin, end));
		Assertions.assertFalse(TimeUtil.isIn(LocalDateTime.parse("2019-02-02T13:00:00"), begin, end));
		Assertions.assertFalse(TimeUtil.isIn(LocalDateTime.parse("2019-02-01T08:00:00"), begin, end));
		Assertions.assertFalse(TimeUtil.isIn(LocalDateTime.parse("2019-02-03T09:00:00"), begin, end));

		// 在时间范围内 用例
		Assertions.assertTrue(TimeUtil.isIn(LocalDateTime.parse("2019-02-02T08:00:00"), begin, end));
		Assertions.assertTrue(TimeUtil.isIn(LocalDateTime.parse("2019-02-02T08:00:01"), begin, end));
		Assertions.assertTrue(TimeUtil.isIn(LocalDateTime.parse("2019-02-02T08:11:00"), begin, end));
		Assertions.assertTrue(TimeUtil.isIn(LocalDateTime.parse("2019-02-02T08:22:00"), begin, end));
		Assertions.assertTrue(TimeUtil.isIn(LocalDateTime.parse("2019-02-02T08:59:59"), begin, end));
		Assertions.assertTrue(TimeUtil.isIn(LocalDateTime.parse("2019-02-02T09:00:00"), begin, end));

		// 测试边界条件
		Assertions.assertTrue(TimeUtil.isIn(begin, begin, end, true, false));
		Assertions.assertFalse(TimeUtil.isIn(begin, begin, end, false, false));
		Assertions.assertTrue(TimeUtil.isIn(end, begin, end, false, true));
		Assertions.assertFalse(TimeUtil.isIn(end, begin, end, false, false));

		// begin、end互换
		Assertions.assertTrue(TimeUtil.isIn(begin, end, begin, true, true));

		// 比较当前时间范围
		final LocalDateTime now = LocalDateTime.now();
		Assertions.assertTrue(TimeUtil.isIn(now, now.minusHours(1L), now.plusHours(1L)));
		Assertions.assertFalse(TimeUtil.isIn(now, now.minusHours(1L), now.minusHours(2L)));
		Assertions.assertFalse(TimeUtil.isIn(now, now.plusHours(1L), now.plusHours(2L)));

		// 异常入参
		Assertions.assertThrows(IllegalArgumentException.class, () -> TimeUtil.isIn(null, begin, end, false, false));
		Assertions.assertThrows(IllegalArgumentException.class, () -> TimeUtil.isIn(begin, null, end, false, false));
		Assertions.assertThrows(IllegalArgumentException.class, () -> TimeUtil.isIn(begin, begin, null, false, false));
	}


	@Test
	public void formatDateFunctionTest() {
		final List<String> dateStrList = Stream.of("2023-03-01", "2023-03-02")
				.map(LocalDate::parse)
				.map(TimeUtil.formatFunc(DatePattern.CHINESE_DATE_FORMATTER))
				.collect(Collectors.toList());
		Assertions.assertEquals("2023年03月01日", dateStrList.get(0));
		Assertions.assertEquals("2023年03月02日", dateStrList.get(1));
	}

	@Test
	public void formatTimeFunctionTest() {
		final List<String> dateStrList = Stream.of("2023-03-01T12:23:56", "2023-03-02T12:23:56")
				.map(LocalDateTime::parse)
				.map(TimeUtil.formatFunc(DatePattern.CHINESE_DATE_FORMATTER))
				.collect(Collectors.toList());
		Assertions.assertEquals("2023年03月01日", dateStrList.get(0));
		Assertions.assertEquals("2023年03月02日", dateStrList.get(1));
	}
}
