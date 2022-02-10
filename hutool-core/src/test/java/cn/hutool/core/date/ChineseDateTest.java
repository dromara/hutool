package cn.hutool.core.date;

import cn.hutool.core.util.StrUtil;
import org.junit.Assert;
import org.junit.Test;

public class ChineseDateTest {

	@Test
	public void chineseDateTest() {
		ChineseDate date = new ChineseDate(DateUtil.parseDate("2020-01-25"));
		Assert.assertEquals("2020-01-25 00:00:00", date.getGregorianDate().toString());
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

	@Test
	public void parseTest(){
		ChineseDate date = new ChineseDate(DateUtil.parseDate("1996-07-14"));
		Assert.assertEquals("丙子鼠年 五月廿九", date.toString());

		date = new ChineseDate(DateUtil.parseDate("1996-07-15"));
		Assert.assertEquals("丙子鼠年 五月三十", date.toString());
	}

	@Test
	public void getChineseMonthTest(){
		ChineseDate chineseDate = new ChineseDate(2020,6,15);
		Assert.assertEquals("2020-08-04 00:00:00", chineseDate.getGregorianDate().toString());
		Assert.assertEquals("六月", chineseDate.getChineseMonth());

		chineseDate = new ChineseDate(2020,4,15);
		Assert.assertEquals("四月", chineseDate.getChineseMonth());

		chineseDate = new ChineseDate(2020,5,15);
		Assert.assertEquals("闰四月", chineseDate.getChineseMonth());
	}

	@Test
	public void getFestivalsTest(){
		// issue#I1XHSF@Gitee，2023-01-20对应农历腊月29，非除夕
		ChineseDate chineseDate = new ChineseDate(DateUtil.parseDate("2023-01-20"));
		Assert.assertTrue(StrUtil.isEmpty(chineseDate.getFestivals()));
	}

	@Test
	public void dateTest(){
		// 修复这两个日期不正确的问题
		// 问题出在计算与1900-01-31相差天数的问题上了，相差天数非整天
		ChineseDate date = new ChineseDate(DateUtil.parseDate("1991-09-14"));
		Assert.assertEquals("辛未羊年 八月初七", date.toString());
		date = new ChineseDate(DateUtil.parseDate("1991-09-15"));
		Assert.assertEquals("辛未羊年 八月初八", date.toString());
	}

	@Test
	public void dateTest2(){
		ChineseDate date = new ChineseDate(DateUtil.parse("2020-10-19"));
		Assert.assertEquals("庚子鼠年 九月初三", date.toString());
	}

	@Test
	public void dateTest2_2(){
		ChineseDate date = new ChineseDate(DateUtil.parse("2020-07-20"));
		Assert.assertEquals("庚子鼠年 五月三十", date.toString());
	}

	@Test
	public void dateTest3(){
		// 初一，offset为0测试
		ChineseDate date = new ChineseDate(DateUtil.parse("2099-03-22"));
		Assert.assertEquals("己未羊年 闰二月初一", date.toString());
	}

	@Test
	public void leapMonthTest(){
		final ChineseDate c1 = new ChineseDate(DateUtil.parse("2028-05-28"));
		final ChineseDate c2 = new ChineseDate(DateUtil.parse("2028-06-27"));

		Assert.assertEquals("戊申猴年 五月初五", c1.toString());
		Assert.assertEquals("戊申猴年 闰五月初五", c2.toString());
	}

	@Test
	public void getChineseMonthTest2(){
		//https://github.com/dromara/hutool/issues/2112
		ChineseDate springFestival = new ChineseDate(DateUtil.parseDate("2022-02-01"));
		final String chineseMonth = springFestival.getChineseMonth();
		Assert.assertEquals("一月", chineseMonth);
	}
}
