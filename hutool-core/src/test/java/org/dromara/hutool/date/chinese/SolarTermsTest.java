package org.dromara.hutool.date.chinese;

import org.dromara.hutool.date.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SolarTermsTest {

	@Test
	public void getTermTest1(){
		final int term = SolarTerms.getTerm(1987, 3);
		Assertions.assertEquals(4, term);
	}

	@Test
	public void getTermTest() {

		Assertions.assertEquals("小寒", SolarTerms.getTerm(2021, 1, 5));

		Assertions.assertEquals("大寒", SolarTerms.getTerm(2021, 1, 20));

		Assertions.assertEquals("立春", SolarTerms.getTerm(2021, 2, 3));

		Assertions.assertEquals("雨水", SolarTerms.getTerm(2021, 2, 18));

		Assertions.assertEquals("惊蛰", SolarTerms.getTerm(2021, 3, 5));

		Assertions.assertEquals("春分", SolarTerms.getTerm(2021, 3, 20));

		Assertions.assertEquals("清明", SolarTerms.getTerm(2021, 4, 4));

		Assertions.assertEquals("谷雨", SolarTerms.getTerm(2021, 4, 20));

		Assertions.assertEquals("立夏", SolarTerms.getTerm(2021, 5, 5));

		Assertions.assertEquals("小满", SolarTerms.getTerm(2021, 5, 21));

		Assertions.assertEquals("芒种", SolarTerms.getTerm(2021, 6, 5));

		Assertions.assertEquals("夏至", SolarTerms.getTerm(2021, 6, 21));

		Assertions.assertEquals("小暑", SolarTerms.getTerm(2021, 7, 7));

		Assertions.assertEquals("大暑", SolarTerms.getTerm(2021, 7, 22));

		Assertions.assertEquals("立秋", SolarTerms.getTerm(2021, 8, 7));

		Assertions.assertEquals("处暑", SolarTerms.getTerm(2021, 8, 23));

		Assertions.assertEquals("白露", SolarTerms.getTerm(2021, 9, 7));

		Assertions.assertEquals("秋分", SolarTerms.getTerm(2021, 9, 23));

		Assertions.assertEquals("寒露", SolarTerms.getTerm(2021, 10, 8));

		Assertions.assertEquals("霜降", SolarTerms.getTerm(2021, 10, 23));

		Assertions.assertEquals("立冬", SolarTerms.getTerm(2021, 11, 7));

		Assertions.assertEquals("小雪", SolarTerms.getTerm(2021, 11, 22));

		Assertions.assertEquals("大雪", SolarTerms.getTerm(2021, 12, 7));

		Assertions.assertEquals("冬至", SolarTerms.getTerm(2021, 12, 21));
	}


	@Test
	public void getTermByDateTest() {
		Assertions.assertEquals("春分", SolarTerms.getTerm(DateUtil.parse("2021-03-20")));
		Assertions.assertEquals("处暑", SolarTerms.getTerm(DateUtil.parse("2022-08-23")));
	}


	@Test
	public void getTermByChineseDateTest() {
		Assertions.assertEquals("清明", SolarTerms.getTerm(new ChineseDate(2021, 2, 23)));
		Assertions.assertEquals("秋分", SolarTerms.getTerm(new ChineseDate(2022, 8, 28)));
	}
}
