package cn.hutool.cron.pattern;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.date.DateUtil;

public class CronPatternUtilTest {
	
	@Test
	public void matchedDatesTest() {
		//测试每30秒执行
		List<Date> matchedDates = CronPatternUtil.matchedDates("0/30 * 8-18 * * ?", DateUtil.parse("2018-10-15 14:33:22"), 5, true);
		Assert.assertEquals(5, matchedDates.size());
		Assert.assertEquals("2018-10-15 14:33:30", matchedDates.get(0).toString());
		Assert.assertEquals("2018-10-15 14:34:00", matchedDates.get(1).toString());
		Assert.assertEquals("2018-10-15 14:34:30", matchedDates.get(2).toString());
		Assert.assertEquals("2018-10-15 14:35:00", matchedDates.get(3).toString());
		Assert.assertEquals("2018-10-15 14:35:30", matchedDates.get(4).toString());
	}
	
	@Test
	public void matchedDatesTest2() {
		//测试每小时执行
		List<Date> matchedDates = CronPatternUtil.matchedDates("0 0 */1 * * *", DateUtil.parse("2018-10-15 14:33:22"), 5, true);
		Assert.assertEquals(5, matchedDates.size());
		Assert.assertEquals("2018-10-15 15:00:00", matchedDates.get(0).toString());
		Assert.assertEquals("2018-10-15 16:00:00", matchedDates.get(1).toString());
		Assert.assertEquals("2018-10-15 17:00:00", matchedDates.get(2).toString());
		Assert.assertEquals("2018-10-15 18:00:00", matchedDates.get(3).toString());
		Assert.assertEquals("2018-10-15 19:00:00", matchedDates.get(4).toString());
	}
	
	@Test
	public void matchedDatesTest3() {
		//测试最后一天
		List<Date> matchedDates = CronPatternUtil.matchedDates("0 0 */1 L * *", DateUtil.parse("2018-10-30 23:33:22"), 5, true);
		Assert.assertEquals(5, matchedDates.size());
		Assert.assertEquals("2018-10-31 00:00:00", matchedDates.get(0).toString());
		Assert.assertEquals("2018-10-31 01:00:00", matchedDates.get(1).toString());
		Assert.assertEquals("2018-10-31 02:00:00", matchedDates.get(2).toString());
		Assert.assertEquals("2018-10-31 03:00:00", matchedDates.get(3).toString());
		Assert.assertEquals("2018-10-31 04:00:00", matchedDates.get(4).toString());
	}
}
