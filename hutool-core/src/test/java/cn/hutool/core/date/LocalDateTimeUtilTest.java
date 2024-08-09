package cn.hutool.core.date;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class LocalDateTimeUtilTest {

	@Test
	public void nowTest() {
		assertNotNull(LocalDateTimeUtil.now());
	}

	@Test
	public void ofTest() {
		final String dateStr = "2020-01-23T12:23:56";
		final DateTime dt = DateUtil.parse(dateStr);

		LocalDateTime of = LocalDateTimeUtil.of(dt);
		assertNotNull(of);
		assertEquals(dateStr, of.toString());

		// 不加Z是标准当地时间，与UTC时间不同
		of = LocalDateTimeUtil.ofUTC(dt.getTime());
		assertNotEquals(dateStr, of.toString());
	}

	@Test
	public void parseOffsetTest() {
		final LocalDateTime localDateTime = LocalDateTimeUtil.parse("2021-07-30T16:27:27+08:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		assertEquals("2021-07-30T16:27:27", Objects.requireNonNull(localDateTime).toString());
	}

	@Test
	public void parseTest() {
		final LocalDateTime localDateTime = LocalDateTimeUtil.parse("2020-01-23T12:23:56", DateTimeFormatter.ISO_DATE_TIME);
		assertEquals("2020-01-23T12:23:56", Objects.requireNonNull(localDateTime).toString());
	}

	@Test
	public void parseTest2() {
		final LocalDateTime localDateTime = LocalDateTimeUtil.parse("2020-01-23", DatePattern.NORM_DATE_PATTERN);
		assertEquals("2020-01-23T00:00", Objects.requireNonNull(localDateTime).toString());
	}

	@Test
	public void parseTest3() {
		final LocalDateTime localDateTime = LocalDateTimeUtil.parse("12:23:56", DatePattern.NORM_TIME_PATTERN);
		assertEquals("12:23:56", Objects.requireNonNull(localDateTime).toLocalTime().toString());
	}

	@Test
	public void parseTest4() {
		final LocalDateTime localDateTime = LocalDateTimeUtil.parse("2020-01-23T12:23:56");
		assertEquals("2020-01-23T12:23:56", localDateTime.toString());
	}

	@Test
	public void parseTest5() {
		final LocalDateTime localDateTime = LocalDateTimeUtil.parse("19940121183604", "yyyyMMddHHmmss");
		assertEquals("1994-01-21T18:36:04", Objects.requireNonNull(localDateTime).toString());
	}

	@Test
	public void parseTest6() {
		LocalDateTime localDateTime = LocalDateTimeUtil.parse("19940121183604682", "yyyyMMddHHmmssSSS");
		assertEquals("1994-01-21T18:36:04.682", Objects.requireNonNull(localDateTime).toString());

		localDateTime = LocalDateTimeUtil.parse("1994012118360468", "yyyyMMddHHmmssSS");
		assertEquals("1994-01-21T18:36:04.680", Objects.requireNonNull(localDateTime).toString());

		localDateTime = LocalDateTimeUtil.parse("199401211836046", "yyyyMMddHHmmssS");
		assertEquals("1994-01-21T18:36:04.600", Objects.requireNonNull(localDateTime).toString());
	}

	@Test
	public void parseDateTest() {
		LocalDate localDate = LocalDateTimeUtil.parseDate("2020-01-23");
		assertEquals("2020-01-23", localDate.toString());

		localDate = LocalDateTimeUtil.parseDate("2020-01-23T12:23:56", DateTimeFormatter.ISO_DATE_TIME);
		assertNotNull(localDate);
		assertEquals("2020-01-23", localDate.toString());
	}

	@Test
	public void parseSingleMonthAndDayTest() {
		final LocalDate localDate = LocalDateTimeUtil.parseDate("2020-1-1", "yyyy-M-d");
		assertEquals("2020-01-01", localDate.toString());
	}

	@Test
	public void formatTest() {
		final LocalDateTime localDateTime = LocalDateTimeUtil.parse("2020-01-23T12:23:56");
		String format = LocalDateTimeUtil.format(localDateTime, DatePattern.NORM_DATETIME_PATTERN);
		assertEquals("2020-01-23 12:23:56", format);

		format = LocalDateTimeUtil.formatNormal(localDateTime);
		assertEquals("2020-01-23 12:23:56", format);

		format = LocalDateTimeUtil.format(localDateTime, DatePattern.NORM_DATE_PATTERN);
		assertEquals("2020-01-23", format);
	}

	@Test
	public void formatLocalDateTest() {
		final LocalDate date = LocalDate.parse("2020-01-23");
		String format = LocalDateTimeUtil.format(date, DatePattern.NORM_DATE_PATTERN);
		assertEquals("2020-01-23", format);

		format = LocalDateTimeUtil.formatNormal(date);
		assertEquals("2020-01-23", format);
	}

	@Test
	public void offset() {
		final LocalDateTime localDateTime = LocalDateTimeUtil.parse("2020-01-23T12:23:56");
		LocalDateTime offset = LocalDateTimeUtil.offset(localDateTime, 1, ChronoUnit.DAYS);
		// 非同一对象
		assertNotSame(localDateTime, offset);

		assertEquals("2020-01-24T12:23:56", offset.toString());

		offset = LocalDateTimeUtil.offset(localDateTime, -1, ChronoUnit.DAYS);
		assertEquals("2020-01-22T12:23:56", offset.toString());
	}

	@Test
	public void ofTest2(){
		final Instant instant = Objects.requireNonNull(DateUtil.parse("2022-02-22")).toInstant();
		final LocalDateTime of = LocalDateTimeUtil.of((TemporalAccessor) instant);
		assertEquals("2022-02-22T00:00", of.toString());
	}

	@Test
	public void between() {
		final Duration between = LocalDateTimeUtil.between(
				LocalDateTimeUtil.parse("2019-02-02T00:00:00"),
				LocalDateTimeUtil.parse("2020-02-02T00:00:00"));
		assertEquals(365, between.toDays());
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void isIn() {
		// 时间范围 8点-9点
		final LocalDateTime begin = LocalDateTime.parse("2019-02-02T08:00:00");
		final LocalDateTime end = LocalDateTime.parse("2019-02-02T09:00:00");

		// 不在时间范围内 用例
		assertFalse(LocalDateTimeUtil.isIn(LocalDateTime.parse("2019-02-02T06:00:00"), begin, end));
		assertFalse(LocalDateTimeUtil.isIn(LocalDateTime.parse("2019-02-02T13:00:00"), begin, end));
		assertFalse(LocalDateTimeUtil.isIn(LocalDateTime.parse("2019-02-01T08:00:00"), begin, end));
		assertFalse(LocalDateTimeUtil.isIn(LocalDateTime.parse("2019-02-03T09:00:00"), begin, end));

		// 在时间范围内 用例
		assertTrue(LocalDateTimeUtil.isIn(LocalDateTime.parse("2019-02-02T08:00:00"), begin, end));
		assertTrue(LocalDateTimeUtil.isIn(LocalDateTime.parse("2019-02-02T08:00:01"), begin, end));
		assertTrue(LocalDateTimeUtil.isIn(LocalDateTime.parse("2019-02-02T08:11:00"), begin, end));
		assertTrue(LocalDateTimeUtil.isIn(LocalDateTime.parse("2019-02-02T08:22:00"), begin, end));
		assertTrue(LocalDateTimeUtil.isIn(LocalDateTime.parse("2019-02-02T08:59:59"), begin, end));
		assertTrue(LocalDateTimeUtil.isIn(LocalDateTime.parse("2019-02-02T09:00:00"), begin, end));

		// 测试边界条件
		assertTrue(LocalDateTimeUtil.isIn(begin, begin, end, true, false));
		assertFalse(LocalDateTimeUtil.isIn(begin, begin, end, false, false));
		assertTrue(LocalDateTimeUtil.isIn(end, begin, end, false, true));
		assertFalse(LocalDateTimeUtil.isIn(end, begin, end, false, false));

		// begin、end互换
		assertTrue(LocalDateTimeUtil.isIn(begin, end, begin, true, true));

		// 比较当前时间范围
		final LocalDateTime now = LocalDateTime.now();
		assertTrue(LocalDateTimeUtil.isIn(now, now.minusHours(1L), now.plusHours(1L)));
		assertFalse(LocalDateTimeUtil.isIn(now, now.minusHours(1L), now.minusHours(2L)));
		assertFalse(LocalDateTimeUtil.isIn(now, now.plusHours(1L), now.plusHours(2L)));

		// 异常入参
		assertThrows(IllegalArgumentException.class, () -> LocalDateTimeUtil.isIn(null, begin, end, false, false));
		assertThrows(IllegalArgumentException.class, () -> LocalDateTimeUtil.isIn(begin, null, end, false, false));
		assertThrows(IllegalArgumentException.class, () -> LocalDateTimeUtil.isIn(begin, begin, null, false, false));
	}

	@Test
	public void beginOfDayTest() {
		final LocalDateTime localDateTime = LocalDateTimeUtil.parse("2020-01-23T12:23:56");
		final LocalDateTime beginOfDay = LocalDateTimeUtil.beginOfDay(localDateTime);
		assertEquals("2020-01-23T00:00", beginOfDay.toString());
	}

	@Test
	public void endOfDayTest() {
		final LocalDateTime localDateTime = LocalDateTimeUtil.parse("2020-01-23T12:23:56");

		LocalDateTime endOfDay = LocalDateTimeUtil.endOfDay(localDateTime);
		assertEquals("2020-01-23T23:59:59.999999999", endOfDay.toString());

		endOfDay = LocalDateTimeUtil.endOfDay(localDateTime, true);
		assertEquals("2020-01-23T23:59:59", endOfDay.toString());
	}

	@Test
	public void dayOfWeekTest() {
		final Week one = LocalDateTimeUtil.dayOfWeek(LocalDate.of(2021, 9, 20));
		assertEquals(Week.MONDAY, one);

		final Week two = LocalDateTimeUtil.dayOfWeek(LocalDate.of(2021, 9, 21));
		assertEquals(Week.TUESDAY, two);

		final Week three = LocalDateTimeUtil.dayOfWeek(LocalDate.of(2021, 9, 22));
		assertEquals(Week.WEDNESDAY, three);

		final Week four = LocalDateTimeUtil.dayOfWeek(LocalDate.of(2021, 9, 23));
		assertEquals(Week.THURSDAY, four);

		final Week five = LocalDateTimeUtil.dayOfWeek(LocalDate.of(2021, 9, 24));
		assertEquals(Week.FRIDAY, five);

		final Week six = LocalDateTimeUtil.dayOfWeek(LocalDate.of(2021, 9, 25));
		assertEquals(Week.SATURDAY, six);

		final Week seven = LocalDateTimeUtil.dayOfWeek(LocalDate.of(2021, 9, 26));
		assertEquals(Week.SUNDAY, seven);
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

		final LocalDateTime realStartTime1 = DateUtil.parseLocalDateTime("2022-03-01 08:00:00");
		final LocalDateTime realEndTime1   = DateUtil.parseLocalDateTime("2022-03-01 10:00:00");

		final LocalDateTime startTime  = DateUtil.parseLocalDateTime("2022-03-23 05:00:00");
		final LocalDateTime endTime    = DateUtil.parseLocalDateTime("2022-03-23 13:00:00");



		assertFalse(LocalDateTimeUtil.isOverlap(oneStartTime,oneEndTime,realStartTime,realEndTime));
		assertFalse(LocalDateTimeUtil.isOverlap(oneStartTime2,oneEndTime2,realStartTime,realEndTime));
		assertTrue(LocalDateTimeUtil.isOverlap(oneStartTime3,oneEndTime3,realStartTime,realEndTime));

		assertFalse(LocalDateTimeUtil.isOverlap(realStartTime1,realEndTime1,startTime,endTime));
		assertFalse(LocalDateTimeUtil.isOverlap(startTime,endTime,realStartTime1,realEndTime1));

		assertTrue(LocalDateTimeUtil.isOverlap(startTime,startTime,startTime,startTime));
		assertTrue(LocalDateTimeUtil.isOverlap(startTime,startTime,startTime,endTime));
		assertFalse(LocalDateTimeUtil.isOverlap(startTime,startTime,endTime,endTime));
		assertTrue(LocalDateTimeUtil.isOverlap(startTime,endTime,endTime,endTime));
	}

	@Test
	public void weekOfYearTest(){
		final LocalDate date1 = LocalDate.of(2021, 12, 31);
		final int weekOfYear1 = LocalDateTimeUtil.weekOfYear(date1);
		assertEquals(52, weekOfYear1);

		final int weekOfYear2 = LocalDateTimeUtil.weekOfYear(date1.atStartOfDay());
		assertEquals(52, weekOfYear2);
	}

	@Test
	public void weekOfYearTest2(){
		final LocalDate date1 = LocalDate.of(2022, 1, 31);
		final int weekOfYear1 = LocalDateTimeUtil.weekOfYear(date1);
		assertEquals(5, weekOfYear1);

		final int weekOfYear2 = LocalDateTimeUtil.weekOfYear(date1.atStartOfDay());
		assertEquals(5, weekOfYear2);
	}

	@Test
	public void parseBlankTest(){
		final LocalDateTime parse = LocalDateTimeUtil.parse("");
		assertNull(parse);
	}
}
