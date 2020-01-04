package cn.hutool.core.date;

import cn.hutool.core.date.BetweenFormater.Level;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.Objects;

public class DateBetweenTest {

	@Test
	public void betweenYearTest() {
		Date start = DateUtil.parse("2017-02-01 12:23:46");
		Date end = DateUtil.parse("2018-02-01 12:23:46");
		long betweenYear = new DateBetween(start, end).betweenYear(false);
		Assert.assertEquals(1, betweenYear);

		Date start1 = DateUtil.parse("2017-02-01 12:23:46");
		Date end1 = DateUtil.parse("2018-03-01 12:23:46");
		long betweenYear1 = new DateBetween(start1, end1).betweenYear(false);
		Assert.assertEquals(1, betweenYear1);

		// 不足1年
		Date start2 = DateUtil.parse("2017-02-01 12:23:46");
		Date end2 = DateUtil.parse("2018-02-01 11:23:46");
		long betweenYear2 = new DateBetween(start2, end2).betweenYear(false);
		Assert.assertEquals(0, betweenYear2);
	}

	@Test
	public void betweenYearTest2() {
		Date start = DateUtil.parse("2000-02-29");
		Date end = DateUtil.parse("2018-02-28");
		long betweenYear = new DateBetween(start, end).betweenYear(false);
		Assert.assertEquals(18, betweenYear);
	}

	@Test
	public void betweenMonthTest() {
		Date start = DateUtil.parse("2017-02-01 12:23:46");
		Date end = DateUtil.parse("2018-02-01 12:23:46");
		long betweenMonth = new DateBetween(start, end).betweenMonth(false);
		Assert.assertEquals(12, betweenMonth);

		Date start1 = DateUtil.parse("2017-02-01 12:23:46");
		Date end1 = DateUtil.parse("2018-03-01 12:23:46");
		long betweenMonth1 = new DateBetween(start1, end1).betweenMonth(false);
		Assert.assertEquals(13, betweenMonth1);

		// 不足
		Date start2 = DateUtil.parse("2017-02-01 12:23:46");
		Date end2 = DateUtil.parse("2018-02-01 11:23:46");
		long betweenMonth2 = new DateBetween(start2, end2).betweenMonth(false);
		Assert.assertEquals(11, betweenMonth2);
	}

	@Test
	public void betweenMinuteTest() {
		Date date1 = DateUtil.parse("2017-03-01 20:33:23");
		Date date2 = DateUtil.parse("2017-03-01 23:33:23");
		String formatBetween = DateUtil.formatBetween(date1, date2, Level.SECOND);
		Assert.assertEquals("3小时", formatBetween);
	}

	@Test
	public void isBetween() {
		Date start = DateUtil.parse("2019-12-01 12:00:00");
		Date end = DateUtil.parse("2020-01-04 12:00:00");
		DateBetween dateBetween = new DateBetween(start,end);

		Date targetTrue = DateUtil.parse("2020-01-01 12:00:00");
		Assert.assertTrue(dateBetween.isBetween(Objects.requireNonNull(targetTrue)));

		Date targetEqualsLeft = DateUtil.parse("2019-12-01 12:00:00");
		Assert.assertTrue(dateBetween.isBetween(Objects.requireNonNull(targetEqualsLeft)));

		Date targetEqualsRight = DateUtil.parse("2020-01-04 12:00:00");
		Assert.assertTrue(dateBetween.isBetween(Objects.requireNonNull(targetEqualsRight)));

		Date targetFalse = DateUtil.parse("2020-02-01 12:00:00");
		Assert.assertFalse(dateBetween.isBetween(Objects.requireNonNull(targetFalse)));
	}
	@Test
	public void staticIsBetween() {
		Date start = DateUtil.parse("2019-12-01 12:00:00");
		Date end = DateUtil.parse("2020-01-04 12:00:00");

		Date targetTrue = DateUtil.parse("2020-01-01 12:00:00");
		Assert.assertTrue(DateBetween.isBetween(Objects.requireNonNull(start),Objects.requireNonNull(end), Objects.requireNonNull(targetTrue)));

		Date targetEqualsLeft = DateUtil.parse("2019-12-01 12:00:00");
		Assert.assertTrue(DateBetween.isBetween(Objects.requireNonNull(start),Objects.requireNonNull(end), Objects.requireNonNull(targetEqualsLeft)));

		Date targetEqualsRight = DateUtil.parse("2020-01-04 12:00:00");
		Assert.assertTrue(DateBetween.isBetween(Objects.requireNonNull(start),Objects.requireNonNull(end), Objects.requireNonNull(targetEqualsRight)));

		Date targetFalse = DateUtil.parse("2020-02-01 12:00:00");
		Assert.assertFalse(DateBetween.isBetween(Objects.requireNonNull(start),Objects.requireNonNull(end), Objects.requireNonNull(targetFalse)));
	}
}
