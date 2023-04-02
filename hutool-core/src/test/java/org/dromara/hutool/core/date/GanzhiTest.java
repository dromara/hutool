package org.dromara.hutool.core.date;

import org.dromara.hutool.core.date.chinese.ChineseDate;
import org.dromara.hutool.core.date.chinese.GanZhi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GanzhiTest {

	@Test
	public void getGanzhiOfYearTest(){
		Assertions.assertEquals("庚子", GanZhi.getGanzhiOfYear(2020));
	}

	@Test
	public void getCyclicalYMDTest(){
		//通过公历构建
		final ChineseDate chineseDate = new ChineseDate(DateUtil.parse("1993-01-06"));
		final String cyclicalYMD = chineseDate.getCyclicalYMD();
		Assertions.assertEquals("壬申年癸丑月丁亥日",cyclicalYMD);
	}

	@Test
	public void getCyclicalYMDTest2(){
		//通过农历构建
		final ChineseDate chineseDate = new ChineseDate(1992,12,14);
		final String cyclicalYMD = chineseDate.getCyclicalYMD();
		Assertions.assertEquals("壬申年癸丑月丁亥日",cyclicalYMD);
	}

	@Test
	public void getCyclicalYMDTest3(){
		//通过公历构建
		final ChineseDate chineseDate = new ChineseDate(DateUtil.parse("2020-08-28"));
		final String cyclicalYMD = chineseDate.getCyclicalYMD();
		Assertions.assertEquals("庚子年甲申月癸卯日",cyclicalYMD);
	}

	@Test
	public void getCyclicalYMDTest4(){
		//通过公历构建
		final ChineseDate chineseDate = new ChineseDate(DateUtil.parse("1905-08-28"));
		final String cyclicalYMD = chineseDate.getCyclicalYMD();
		Assertions.assertEquals("乙巳年甲申月己亥日",cyclicalYMD);
	}
}
