package cn.hutool.cron.pattern;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import org.junit.Test;

import java.util.Calendar;

public class CronPatternNextMatchTest {

	@Test
	public void nextMatchAfterTest(){
		CronPattern pattern = new CronPattern("23 12 * 12 * * *");
		final Calendar calendar = pattern.nextMatchAfter(
				DateUtil.parse("2022-04-12 09:12:23").toCalendar());
		Console.log(DateUtil.date(calendar));
	}
}
