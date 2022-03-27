package cn.hutool.cron.pattern;

import cn.hutool.core.date.DateUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class CronPatternNextMatchTest {

	@Test
	public void nextMatchAfterTest(){
		CronPattern pattern = new CronPattern("23 12 * 12 * * *");

		// 时间正常递增
		//noinspection ConstantConditions
		Calendar calendar = pattern.nextMatchAfter(
				DateUtil.parse("2022-04-12 09:12:12").toCalendar());

		Assert.assertTrue(pattern.match(calendar, true));
		Assert.assertEquals("2022-04-12 09:12:23", DateUtil.date(calendar).toString());

		// 秒超出规定值的最大值，小时+1
		//noinspection ConstantConditions
		calendar = pattern.nextMatchAfter(
				DateUtil.parse("2022-04-12 09:12:24").toCalendar());
		Assert.assertTrue(pattern.match(calendar, true));
		Assert.assertEquals("2022-04-12 10:12:23", DateUtil.date(calendar).toString());

		// 天超出规定值的最大值，月+1
		//noinspection ConstantConditions
		calendar = pattern.nextMatchAfter(
				DateUtil.parse("2022-04-13 09:12:24").toCalendar());
		Assert.assertTrue(pattern.match(calendar, true));
		Assert.assertEquals("2022-05-12 00:12:23", DateUtil.date(calendar).toString());
	}
}
