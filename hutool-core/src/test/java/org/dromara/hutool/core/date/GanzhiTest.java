/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.date;

import org.dromara.hutool.core.date.chinese.ChineseDate;
import org.dromara.hutool.core.date.chinese.GanZhi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class GanzhiTest {

	@Test
	public void getGanzhiOfYearTest(){
		Assertions.assertEquals("庚子", GanZhi.getGanzhiOfYear(2020));
	}

	@Test
	public void getCyclicalYMDTest(){
		//通过公历构建
		final ChineseDate chineseDate = new ChineseDate(Objects.requireNonNull(DateUtil.parse("1993-01-06")));
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
		final ChineseDate chineseDate = new ChineseDate(Objects.requireNonNull(DateUtil.parse("2020-08-28")));
		final String cyclicalYMD = chineseDate.getCyclicalYMD();
		Assertions.assertEquals("庚子年甲申月癸卯日",cyclicalYMD);
	}

	@Test
	public void getCyclicalYMDTest4(){
		//通过公历构建
		final ChineseDate chineseDate = new ChineseDate(Objects.requireNonNull(DateUtil.parse("1905-08-28")));
		final String cyclicalYMD = chineseDate.getCyclicalYMD();
		Assertions.assertEquals("乙巳年甲申月己亥日",cyclicalYMD);
	}
}
