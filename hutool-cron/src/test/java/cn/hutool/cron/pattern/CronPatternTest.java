package cn.hutool.cron.pattern;

import cn.hutool.core.date.DateUtil;
import cn.hutool.cron.CronException;
import org.junit.Assert;
import org.junit.Test;

/**
 * 定时任务单元测试类
 *
 * @author Looly
 */
public class CronPatternTest {

	@Test
	public void matchAllTest() {
		CronPattern pattern;
		// 任何时间匹配
		pattern = CronPattern.of("* * * * * *");
		assertMatch(pattern, DateUtil.now());
	}

	@Test
	public void matchAllTest2() {
		// 在5位表达式中，秒部分并不是任意匹配，而是一个固定值0
		// 因此此处匹配就不能匹配秒
		CronPattern pattern;
		// 任何时间匹配
		// 分 时 天 月 周
		pattern = CronPattern.of("* * * * *");

		// 测试时，秒归零，则任意时间匹配
		assertMatch(pattern, DateUtil.beginOfMinute(DateUtil.date()).toString());
	}

	@Test
	public void cronPatternTest() {
		CronPattern pattern;

		// 12:11匹配
		pattern = CronPattern.of("39 11 12 * * *");
		assertMatch(pattern, "12:11:39");

		// 每5分钟匹配，匹配分钟为：[0,5,10,15,20,25,30,35,40,45,50,55]
		pattern = CronPattern.of("39 */5 * * * *");
		assertMatch(pattern, "12:00:39");
		assertMatch(pattern, "12:05:39");
		assertMatch(pattern, "12:10:39");
		assertMatch(pattern, "12:15:39");
		assertMatch(pattern, "12:20:39");
		assertMatch(pattern, "12:25:39");
		assertMatch(pattern, "12:30:39");
		assertMatch(pattern, "12:35:39");
		assertMatch(pattern, "12:40:39");
		assertMatch(pattern, "12:45:39");
		assertMatch(pattern, "12:50:39");
		assertMatch(pattern, "12:55:39");

		// 2:01,3:01,4:01
		pattern = CronPattern.of("39 1 2-4 * * *");
		assertMatch(pattern, "02:01:39");
		assertMatch(pattern, "03:01:39");
		assertMatch(pattern, "04:01:39");

		// 2:01,3:01,4:01
		pattern = CronPattern.of("39 1 2,3,4 * * *");
		assertMatch(pattern, "02:01:39");
		assertMatch(pattern, "03:01:39");
		assertMatch(pattern, "04:01:39");

		// 08-07, 08-06
		pattern = CronPattern.of("39 0 0 6,7 8 *");
		assertMatch(pattern, "2016-08-07 00:00:39");
		assertMatch(pattern, "2016-08-06 00:00:39");

		// 别名忽略大小写
		pattern = CronPattern.of("39 0 0 6,7 Aug *");
		assertMatch(pattern, "2016-08-06 00:00:39");
		assertMatch(pattern, "2016-08-07 00:00:39");

		pattern = CronPattern.of("39 0 0 7 aug *");
		assertMatch(pattern, "2016-08-07 00:00:39");
	}

	@Test
	public void matchDayOfWeekTest() {
		// 星期四
		CronPattern pattern = CronPattern.of("39 0 0 * * Thu");
		assertMatch(pattern, "2017-02-09 00:00:39");

		// 星期日的三种形式
		pattern = CronPattern.of("39 0 0 * * Sun");
		assertMatch(pattern, "2022-03-27 00:00:39");

		pattern = CronPattern.of("39 0 0 * * 0");
		assertMatch(pattern, "2022-03-27 00:00:39");

		pattern = CronPattern.of("39 0 0 * * 7");
		assertMatch(pattern, "2022-03-27 00:00:39");
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void CronPatternTest2() {
		CronPattern pattern = CronPattern.of("0/30 * * * *");
		Assert.assertTrue(pattern.match(DateUtil.parse("2018-10-09 12:00:00").getTime(), false));
		Assert.assertTrue(pattern.match(DateUtil.parse("2018-10-09 12:30:00").getTime(), false));

		pattern = CronPattern.of("32 * * * *");
		Assert.assertTrue(pattern.match(DateUtil.parse("2018-10-09 12:32:00").getTime(), false));
	}

	@Test
	public void patternTest() {
		CronPattern pattern = CronPattern.of("* 0 4 * * ?");
		assertMatch(pattern, "2017-02-09 04:00:00");
		assertMatch(pattern, "2017-02-19 04:00:33");

		// 6位Quartz风格表达式
		pattern = CronPattern.of("* 0 4 * * ?");
		assertMatch(pattern, "2017-02-09 04:00:00");
		assertMatch(pattern, "2017-02-19 04:00:33");
	}

	@Test
	public void patternNegativeTest() {
		// -4表示倒数的数字，此处在小时上，-4表示 23 - 4，为19
		CronPattern pattern = CronPattern.of("* 0 -4 * * ?");
		assertMatch(pattern, "2017-02-09 19:00:00");
		assertMatch(pattern, "2017-02-19 19:00:33");
	}

	@Test
	public void rangePatternTest() {
		CronPattern pattern = CronPattern.of("* 20/2 * * * ?");
		assertMatch(pattern, "2017-02-09 04:20:00");
		assertMatch(pattern, "2017-02-09 05:20:00");
		assertMatch(pattern, "2017-02-19 04:22:33");

		pattern = CronPattern.of("* 2-20/2 * * * ?");
		assertMatch(pattern, "2017-02-09 04:02:00");
		assertMatch(pattern, "2017-02-09 05:04:00");
		assertMatch(pattern, "2017-02-19 04:20:33");
	}

	@Test
	public void lastTest() {
		// 每月最后一天的任意时间
		CronPattern pattern = CronPattern.of("* * * L * ?");
		assertMatch(pattern, "2017-07-31 04:20:00");
		assertMatch(pattern, "2017-02-28 04:20:00");

		// 最后一个月的任意时间
		pattern = CronPattern.of("* * * * L ?");
		assertMatch(pattern, "2017-12-02 04:20:00");

		// 任意天的最后时间
		pattern = CronPattern.of("L L L * * ?");
		assertMatch(pattern, "2017-12-02 23:59:59");
	}

	@Test(expected = CronException.class)
	public void rangeYearTest() {
		// year的范围是1970~2099年，超出报错
		CronPattern.of("0/1 * * * 1/1 ? 2020-2120");
	}

	/**
	 * 表达式是否匹配日期
	 *
	 * @param pattern 表达式
	 * @param date    日期，标准日期时间字符串
	 */
	@SuppressWarnings("ConstantConditions")
	private void assertMatch(CronPattern pattern, String date) {
		Assert.assertTrue(pattern.match(DateUtil.parse(date).toCalendar(), false));
		Assert.assertTrue(pattern.match(DateUtil.parse(date).toCalendar(), true));
	}
}
