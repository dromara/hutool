package cn.hutool.core.date;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.date.DateBetween;
import cn.hutool.core.date.DateUtil;

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
		
		//不足1年
		Date start2 = DateUtil.parse("2017-02-01 12:23:46");
		Date end2 = DateUtil.parse("2018-02-01 11:23:46");
		long betweenYear2 = new DateBetween(start2, end2).betweenYear(false);
		Assert.assertEquals(0, betweenYear2);
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
		
		//不足
		Date start2 = DateUtil.parse("2017-02-01 12:23:46");
		Date end2 = DateUtil.parse("2018-02-01 11:23:46");
		long betweenMonth2 = new DateBetween(start2, end2).betweenMonth(false);
		Assert.assertEquals(11, betweenMonth2);
	}
}
