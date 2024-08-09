package cn.hutool.core.date;

import cn.hutool.core.util.StrUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class ChineseDateTest {

	@Test
	public void chineseDateTest() {
		ChineseDate date = new ChineseDate(DateUtil.parseDate("2020-01-25"));
		assertEquals("2020-01-25 00:00:00", date.getGregorianDate().toString());
		assertEquals(2020, date.getChineseYear());

		assertEquals(1, date.getMonth());
		assertEquals("一月", date.getChineseMonth());
		assertEquals("正月", date.getChineseMonthName());


		assertEquals(1, date.getDay());
		assertEquals("初一", date.getChineseDay());

		assertEquals("庚子", date.getCyclical());
		assertEquals("鼠", date.getChineseZodiac());
		assertEquals("春节", date.getFestivals());
		assertEquals("庚子鼠年 正月初一", date.toString());

		date = new ChineseDate(DateUtil.parseDate("2020-01-14"));
		assertEquals("己亥猪年 腊月二十", date.toString());
		date = new ChineseDate(DateUtil.parseDate("2020-01-24"));
		assertEquals("己亥猪年 腊月三十", date.toString());

		assertEquals("2019-12-30", date.toStringNormal());
	}

	@Test
	public void toStringNormalTest(){
		ChineseDate date = new ChineseDate(DateUtil.parseDate("2020-03-1"));
		assertEquals("2020-02-08", date.toStringNormal());
	}

	@Test
	public void parseTest(){
		ChineseDate date = new ChineseDate(DateUtil.parseDate("1996-07-14"));
		assertEquals("丙子鼠年 五月廿九", date.toString());

		date = new ChineseDate(DateUtil.parseDate("1996-07-15"));
		assertEquals("丙子鼠年 五月三十", date.toString());
	}

	@Test
	public void getChineseMonthTest(){
		ChineseDate chineseDate = new ChineseDate(2020,6,15);
		assertEquals("2020-08-04 00:00:00", chineseDate.getGregorianDate().toString());
		assertEquals("六月", chineseDate.getChineseMonth());

		chineseDate = new ChineseDate(2020,4,15);
		assertEquals("2020-06-06 00:00:00", chineseDate.getGregorianDate().toString());
		assertEquals("闰四月", chineseDate.getChineseMonth());

		chineseDate = new ChineseDate(2020,5,15);
		assertEquals("2020-07-05 00:00:00", chineseDate.getGregorianDate().toString());
		assertEquals("五月", chineseDate.getChineseMonth());
	}

	@Test
	public void getFestivalsTest(){
		// issue#I1XHSF@Gitee，2023-01-20对应农历腊月29，非除夕
		ChineseDate chineseDate = new ChineseDate(DateUtil.parseDate("2023-01-20"));
		assertTrue(StrUtil.isEmpty(chineseDate.getFestivals()));
	}

	@Test
	public void dateTest(){
		// 修复这两个日期不正确的问题
		// 问题出在计算与1900-01-31相差天数的问题上了，相差天数非整天
		ChineseDate date = new ChineseDate(DateUtil.parseDate("1991-09-14"));
		assertEquals("辛未羊年 八月初七", date.toString());
		date = new ChineseDate(DateUtil.parseDate("1991-09-15"));
		assertEquals("辛未羊年 八月初八", date.toString());
	}

	@Test
	public void dateTest2(){
		//noinspection ConstantConditions
		ChineseDate date = new ChineseDate(DateUtil.parse("2020-10-19"));
		assertEquals("庚子鼠年 九月初三", date.toString());
	}

	@Test
	public void dateTest2_2(){
		//noinspection ConstantConditions
		ChineseDate date = new ChineseDate(DateUtil.parse("2020-07-20"));
		assertEquals("庚子鼠年 五月三十", date.toString());
	}

	@Test
	public void dateTest3(){
		// 初一，offset为0测试
		//noinspection ConstantConditions
		ChineseDate date = new ChineseDate(DateUtil.parse("2099-03-22"));
		assertEquals("己未羊年 闰二月初一", date.toString());
	}

	@Test
	public void leapMonthTest(){
		//noinspection ConstantConditions
		final ChineseDate c1 = new ChineseDate(DateUtil.parse("2028-05-28"));
		//noinspection ConstantConditions
		final ChineseDate c2 = new ChineseDate(DateUtil.parse("2028-06-27"));

		assertEquals("戊申猴年 五月初五", c1.toString());
		assertEquals("戊申猴年 闰五月初五", c2.toString());
	}

	@Test
	public void getChineseMonthTest2(){
		//https://github.com/dromara/hutool/issues/2112
		ChineseDate springFestival = new ChineseDate(DateUtil.parseDate("2022-02-01"));
		final String chineseMonth = springFestival.getChineseMonth();
		assertEquals("一月", chineseMonth);
	}

	@Test
	public void day19700101Test(){
		// https://gitee.com/dromara/hutool/issues/I4UTPK
		Date date = DateUtil.parse("1970-01-01");
		//noinspection ConstantConditions
		ChineseDate chineseDate = new ChineseDate(date);
		assertEquals("己酉鸡年 冬月廿四", chineseDate.toString());

		date = DateUtil.parse("1970-01-02");
		//noinspection ConstantConditions
		chineseDate = new ChineseDate(date);
		assertEquals("己酉鸡年 冬月廿五", chineseDate.toString());

		date = DateUtil.parse("1970-01-03");
		//noinspection ConstantConditions
		chineseDate = new ChineseDate(date);
		assertEquals("己酉鸡年 冬月廿六", chineseDate.toString());
	}

	@Test
	public void day19000101Test(){
		// 1900-01-31之前不支持
		Date date = DateUtil.parse("1900-01-31");
		//noinspection ConstantConditions
		ChineseDate chineseDate = new ChineseDate(date);
		assertEquals("庚子鼠年 正月初一", chineseDate.toString());
	}

	@Test
	public void getGregorianDateTest(){
		// https://gitee.com/dromara/hutool/issues/I4ZSGJ
		ChineseDate chineseDate = new ChineseDate(1998, 5, 1);
		assertEquals("1998-06-24 00:00:00", chineseDate.getGregorianDate().toString());

		chineseDate = new ChineseDate(1998, 5, 1, false);
		assertEquals("1998-05-26 00:00:00", chineseDate.getGregorianDate().toString());
	}
}
