package cn.hutool.core.date;

import org.junit.Assert;
import org.junit.Test;

public class ChineseDateTest {

	@Test
	public void chineseDateTest() {
		ChineseDate date = new ChineseDate(DateUtil.parseDate("2020-01-25"));
		Assert.assertEquals(2020, date.getChineseYear());

		Assert.assertEquals(1, date.getMonth());
		Assert.assertEquals("一月", date.getChineseMonth());
		Assert.assertEquals("正月", date.getChineseMonthName());


		Assert.assertEquals(1, date.getDay());
		Assert.assertEquals("初一", date.getChineseDay());

		Assert.assertEquals("庚子", date.getCyclical());
		Assert.assertEquals("鼠", date.getChineseZodiac());
		Assert.assertEquals("春节", date.getFestivals());
		Assert.assertEquals("庚子鼠年 正月初一", date.toString());

		date = new ChineseDate(DateUtil.parseDate("2020-01-14"));
		Assert.assertEquals("己亥猪年 腊月二十", date.toString());
		date = new ChineseDate(DateUtil.parseDate("2020-01-24"));
		Assert.assertEquals("己亥猪年 腊月三十", date.toString());

		Assert.assertEquals("2019-12-30", date.toStringNormal());
	}

	@Test
	public void toStringNormalTest(){
		ChineseDate date = new ChineseDate(DateUtil.parseDate("2020-03-1"));
		Assert.assertEquals("2020-02-08", date.toStringNormal());
	}
}
