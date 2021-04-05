package cn.hutool.core.date.chinese;

import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DateUtil;
import org.junit.Assert;
import org.junit.Test;

public class SolarTermsTest {

	@Test
	public void getTermTest1(){
		final int term = SolarTerms.getTerm(1987, 3);
		Assert.assertEquals(4, term);
	}

	@Test
	public void getTermTest() {

		Assert.assertEquals("小寒", SolarTerms.getTerm(2021, 1, 5));

		Assert.assertEquals("大寒", SolarTerms.getTerm(2021, 1, 20));

		Assert.assertEquals("立春", SolarTerms.getTerm(2021, 2, 3));

		Assert.assertEquals("雨水", SolarTerms.getTerm(2021, 2, 18));

		Assert.assertEquals("惊蛰", SolarTerms.getTerm(2021, 3, 5));

		Assert.assertEquals("春分", SolarTerms.getTerm(2021, 3, 20));

		Assert.assertEquals("清明", SolarTerms.getTerm(2021, 4, 4));

		Assert.assertEquals("谷雨", SolarTerms.getTerm(2021, 4, 20));

		Assert.assertEquals("立夏", SolarTerms.getTerm(2021, 5, 5));

		Assert.assertEquals("小满", SolarTerms.getTerm(2021, 5, 21));

		Assert.assertEquals("芒种", SolarTerms.getTerm(2021, 6, 5));

		Assert.assertEquals("夏至", SolarTerms.getTerm(2021, 6, 21));

		Assert.assertEquals("小暑", SolarTerms.getTerm(2021, 7, 7));

		Assert.assertEquals("大暑", SolarTerms.getTerm(2021, 7, 22));

		Assert.assertEquals("立秋", SolarTerms.getTerm(2021, 8, 7));

		Assert.assertEquals("处暑", SolarTerms.getTerm(2021, 8, 23));

		Assert.assertEquals("白露", SolarTerms.getTerm(2021, 9, 7));

		Assert.assertEquals("秋分", SolarTerms.getTerm(2021, 9, 23));

		Assert.assertEquals("寒露", SolarTerms.getTerm(2021, 10, 8));

		Assert.assertEquals("霜降", SolarTerms.getTerm(2021, 10, 23));

		Assert.assertEquals("立冬", SolarTerms.getTerm(2021, 11, 7));

		Assert.assertEquals("小雪", SolarTerms.getTerm(2021, 11, 22));

		Assert.assertEquals("大雪", SolarTerms.getTerm(2021, 12, 7));

		Assert.assertEquals("冬至", SolarTerms.getTerm(2021, 12, 21));
	}


	@Test
	public void getTermByDateTest() {
		Assert.assertEquals("春分", SolarTerms.getTerm(DateUtil.parseDate("2021-03-20")));
		Assert.assertEquals("处暑", SolarTerms.getTerm(DateUtil.parseDate("2022-08-23")));
	}


	@Test
	public void getTermByChineseDateTest() {
		Assert.assertEquals("清明", SolarTerms.getTerm(new ChineseDate(2021, 2, 23)));
		Assert.assertEquals("秋分", SolarTerms.getTerm(new ChineseDate(2022, 8, 28)));
	}
}
