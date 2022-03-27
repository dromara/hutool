package cn.hutool.cron.pattern;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Week;
import cn.hutool.core.lang.Console;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Calendar;

public class CronPatternNextMatchTest {

	@Test
	public void nextMatchAfterTest(){
		CronPattern pattern = new CronPattern("23 12 * 12 * * *");
		//noinspection ConstantConditions
		final Calendar calendar = pattern.nextMatchAfter(
				DateUtil.parse("2022-04-12 09:12:24").toCalendar());

		Console.log(DateUtil.date(calendar));
		Assert.assertTrue(pattern.match(calendar, true));
	}

	@Test
	@Ignore
	public void calendarTest(){
		final Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_WEEK, Week.SATURDAY.getValue());

		Console.log(DateUtil.date(ca));
	}
}
