package cn.hutool.core.date;

import cn.hutool.core.date.BetweenFormatter.Level;
import org.junit.Assert;
import org.junit.Test;

import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateBetweenTest {

	@Test
	public void betweenYearTest() {
		final Date start = DateUtil.parse("2017-02-01 12:23:46");
		final Date end = DateUtil.parse("2018-02-01 12:23:46");
		final long betweenYear = new DateBetween(start, end).betweenYear(false);
		Assert.assertEquals(1, betweenYear);

		final Date start1 = DateUtil.parse("2017-02-01 12:23:46");
		final Date end1 = DateUtil.parse("2018-03-01 12:23:46");
		final long betweenYear1 = new DateBetween(start1, end1).betweenYear(false);
		Assert.assertEquals(1, betweenYear1);

		// 不足1年
		final Date start2 = DateUtil.parse("2017-02-01 12:23:46");
		final Date end2 = DateUtil.parse("2018-02-01 11:23:46");
		final long betweenYear2 = new DateBetween(start2, end2).betweenYear(false);
		Assert.assertEquals(0, betweenYear2);
	}

	@Test
	public void betweenYearTest2() {
		final Date start = DateUtil.parse("2000-02-29");
		final Date end = DateUtil.parse("2018-02-28");
		final long betweenYear = new DateBetween(start, end).betweenYear(false);
		Assert.assertEquals(18, betweenYear);
	}

	@Test
	public void betweenMonthTest() {
		final Date start = DateUtil.parse("2017-02-01 12:23:46");
		final Date end = DateUtil.parse("2018-02-01 12:23:46");
		final long betweenMonth = new DateBetween(start, end).betweenMonth(false);
		Assert.assertEquals(12, betweenMonth);

		final Date start1 = DateUtil.parse("2017-02-01 12:23:46");
		final Date end1 = DateUtil.parse("2018-03-01 12:23:46");
		final long betweenMonth1 = new DateBetween(start1, end1).betweenMonth(false);
		Assert.assertEquals(13, betweenMonth1);

		// 不足
		final Date start2 = DateUtil.parse("2017-02-01 12:23:46");
		final Date end2 = DateUtil.parse("2018-02-01 11:23:46");
		final long betweenMonth2 = new DateBetween(start2, end2).betweenMonth(false);
		Assert.assertEquals(11, betweenMonth2);
	}

	@Test
	public void betweenMinuteTest() {
		final Date date1 = DateUtil.parse("2017-03-01 20:33:23");
		final Date date2 = DateUtil.parse("2017-03-01 23:33:23");
		final String formatBetween = DateUtil.formatBetween(date1, date2, Level.SECOND);
		Assert.assertEquals("3小时", formatBetween);
	}

	@Test
	public void betweenWeeksTest(){
		final long betweenWeek = DateUtil.betweenWeek(
				DateUtil.parse("2020-11-21"),
				DateUtil.parse("2020-11-23"), false);

		final long betweenWeek2 = LocalDateTimeUtil.between(
				LocalDateTimeUtil.parse("2020-11-21", "yyy-MM-dd"),
				LocalDateTimeUtil.parse("2020-11-23", "yyy-MM-dd"),
				ChronoUnit.WEEKS);
		Assert.assertEquals(betweenWeek, betweenWeek2);
	}
}
