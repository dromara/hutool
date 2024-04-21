package cn.hutool.cron.pattern;

import cn.hutool.core.date.DateUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class IssueI9FQUATest {
	@Test
	public void nextDateAfterTest() {
		final String cron = "0/5 * * * * ?";
		final Calendar calendar = CronPattern.of(cron).nextMatchAfter(
			DateUtil.parse("2024-01-01 00:00:00").toCalendar());

		//Console.log(DateUtil.date(calendar));
		Assert.assertEquals("2024-01-01 00:00:05", DateUtil.date(calendar).toString());
	}
}
