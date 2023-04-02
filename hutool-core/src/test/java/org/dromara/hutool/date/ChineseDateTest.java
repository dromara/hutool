package org.dromara.hutool.date;

import org.dromara.hutool.date.chinese.ChineseDate;
import org.dromara.hutool.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Objects;

public class ChineseDateTest {

	@Test
	public void chineseDateTest() {
		ChineseDate date = new ChineseDate(Objects.requireNonNull(DateUtil.parse("2020-01-25")));
		Assertions.assertEquals("2020-01-25 00:00:00", date.getGregorianDate().toString());
		Assertions.assertEquals(2020, date.getChineseYear());

		Assertions.assertEquals(1, date.getMonth());
		Assertions.assertEquals("一月", date.getChineseMonth());
		Assertions.assertEquals("正月", date.getChineseMonthName());


		Assertions.assertEquals(1, date.getDay());
		Assertions.assertEquals("初一", date.getChineseDay());

		Assertions.assertEquals("庚子", date.getCyclical());
		Assertions.assertEquals("鼠", date.getChineseZodiac());
		Assertions.assertEquals("春节", date.getFestivals());
		Assertions.assertEquals("庚子鼠年 正月初一", date.toString());

		date = new ChineseDate(Objects.requireNonNull(DateUtil.parse("2020-01-14")));
		Assertions.assertEquals("己亥猪年 腊月二十", date.toString());
		date = new ChineseDate(Objects.requireNonNull(DateUtil.parse("2020-01-24")));
		Assertions.assertEquals("己亥猪年 腊月三十", date.toString());

		Assertions.assertEquals("2019-12-30", date.toStringNormal());
	}

	@Test
	public void toStringNormalTest(){
		final ChineseDate date = new ChineseDate(Objects.requireNonNull(DateUtil.parse("2020-03-1")));
		Assertions.assertEquals("2020-02-08", date.toStringNormal());
	}

	@Test
	public void parseTest(){
		ChineseDate date = new ChineseDate(Objects.requireNonNull(DateUtil.parse("1996-07-14")));
		Assertions.assertEquals("丙子鼠年 五月廿九", date.toString());

		date = new ChineseDate(Objects.requireNonNull(DateUtil.parse("1996-07-15")));
		Assertions.assertEquals("丙子鼠年 五月三十", date.toString());
	}

	@Test
	public void getChineseMonthTest(){
		ChineseDate chineseDate = new ChineseDate(2020,6,15);
		Assertions.assertEquals("2020-08-04 00:00:00", chineseDate.getGregorianDate().toString());
		Assertions.assertEquals("六月", chineseDate.getChineseMonth());

		chineseDate = new ChineseDate(2020,4,15);
		Assertions.assertEquals("2020-06-06 00:00:00", chineseDate.getGregorianDate().toString());
		Assertions.assertEquals("闰四月", chineseDate.getChineseMonth());

		chineseDate = new ChineseDate(2020,5,15);
		Assertions.assertEquals("2020-07-05 00:00:00", chineseDate.getGregorianDate().toString());
		Assertions.assertEquals("五月", chineseDate.getChineseMonth());
	}

	@Test
	public void getFestivalsTest(){
		// issue#I1XHSF@Gitee，2023-01-20对应农历腊月29，非除夕
		final ChineseDate chineseDate = new ChineseDate(Objects.requireNonNull(DateUtil.parse("2023-01-20")));
		Assertions.assertTrue(StrUtil.isEmpty(chineseDate.getFestivals()));
	}

	@Test
	public void dateTest(){
		// 修复这两个日期不正确的问题
		// 问题出在计算与1900-01-31相差天数的问题上了，相差天数非整天
		ChineseDate date = new ChineseDate(Objects.requireNonNull(DateUtil.parse("1991-09-14")));
		Assertions.assertEquals("辛未羊年 八月初七", date.toString());
		date = new ChineseDate(Objects.requireNonNull(DateUtil.parse("1991-09-15")));
		Assertions.assertEquals("辛未羊年 八月初八", date.toString());
	}

	@Test
	public void dateTest2(){
		//noinspection ConstantConditions
		final ChineseDate date = new ChineseDate(DateUtil.parse("2020-10-19"));
		Assertions.assertEquals("庚子鼠年 九月初三", date.toString());
	}

	@Test
	public void dateTest2_2(){
		//noinspection ConstantConditions
		final ChineseDate date = new ChineseDate(DateUtil.parse("2020-07-20"));
		Assertions.assertEquals("庚子鼠年 五月三十", date.toString());
	}

	@Test
	public void dateTest3(){
		// 初一，offset为0测试
		//noinspection ConstantConditions
		final ChineseDate date = new ChineseDate(DateUtil.parse("2099-03-22"));
		Assertions.assertEquals("己未羊年 闰二月初一", date.toString());
	}

	@Test
	public void leapMonthTest(){
		//noinspection ConstantConditions
		final ChineseDate c1 = new ChineseDate(DateUtil.parse("2028-05-28"));
		//noinspection ConstantConditions
		final ChineseDate c2 = new ChineseDate(DateUtil.parse("2028-06-27"));

		Assertions.assertEquals("戊申猴年 五月初五", c1.toString());
		Assertions.assertEquals("戊申猴年 闰五月初五", c2.toString());
	}

	@Test
	public void getChineseMonthTest2(){
		//https://github.com/dromara/hutool/issues/2112
		final ChineseDate springFestival = new ChineseDate(Objects.requireNonNull(DateUtil.parse("2022-02-01")));
		final String chineseMonth = springFestival.getChineseMonth();
		Assertions.assertEquals("一月", chineseMonth);
	}

	@Test
	public void day19700101Test(){
		// https://gitee.com/dromara/hutool/issues/I4UTPK
		Date date = DateUtil.parse("1970-01-01");
		//noinspection ConstantConditions
		ChineseDate chineseDate = new ChineseDate(date);
		Assertions.assertEquals("己酉鸡年 冬月廿四", chineseDate.toString());

		date = DateUtil.parse("1970-01-02");
		//noinspection ConstantConditions
		chineseDate = new ChineseDate(date);
		Assertions.assertEquals("己酉鸡年 冬月廿五", chineseDate.toString());

		date = DateUtil.parse("1970-01-03");
		//noinspection ConstantConditions
		chineseDate = new ChineseDate(date);
		Assertions.assertEquals("己酉鸡年 冬月廿六", chineseDate.toString());
	}

	@Test
	public void day19000101Test(){
		// 1900-01-31之前不支持
		final Date date = DateUtil.parse("1900-01-31");
		//noinspection ConstantConditions
		final ChineseDate chineseDate = new ChineseDate(date);
		Assertions.assertEquals("庚子鼠年 正月初一", chineseDate.toString());
	}

	@Test
	public void getGregorianDateTest(){
		// https://gitee.com/dromara/hutool/issues/I4ZSGJ
		ChineseDate chineseDate = new ChineseDate(1998, 5, 1);
		Assertions.assertEquals("1998-06-24 00:00:00", chineseDate.getGregorianDate().toString());

		chineseDate = new ChineseDate(1998, 5, 1, false);
		Assertions.assertEquals("1998-05-26 00:00:00", chineseDate.getGregorianDate().toString());
	}
}
