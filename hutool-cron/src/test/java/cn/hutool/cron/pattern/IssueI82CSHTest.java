package cn.hutool.cron.pattern;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class IssueI82CSHTest {

	@Test
	public void test() {
		final DateTime begin = DateUtil.parse("2023-09-20");
		final DateTime end = DateUtil.parse("2025-09-20");
		final List<Date> dates = CronPatternUtil.matchedDates("0 0 1 3-3,9 *", begin, end, 20, false);
		//dates.forEach(Console::log);
		Assert.assertEquals(4,  dates.size());
	}
}
