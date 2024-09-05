/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.date;

import org.dromara.hutool.core.date.chinese.ChineseDate;
import org.dromara.hutool.core.date.chinese.ChineseDateFormat;
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
		final String cyclicalYMD = chineseDate.toString(ChineseDateFormat.GGG);
		Assertions.assertEquals("农历壬申年癸丑月丁亥日",cyclicalYMD);
	}

	@Test
	public void getCyclicalYMDTest2(){
		//通过农历构建
		final ChineseDate chineseDate = new ChineseDate(1992,12,14);
		final String cyclicalYMD = chineseDate.toString(ChineseDateFormat.GGG);
		Assertions.assertEquals("农历壬申年癸丑月丁亥日",cyclicalYMD);
	}

	@Test
	public void getCyclicalYMDTest3(){
		//通过公历构建
		final ChineseDate chineseDate = new ChineseDate(Objects.requireNonNull(DateUtil.parse("2020-08-28")));
		final String cyclicalYMD = chineseDate.toString(ChineseDateFormat.GGG);
		Assertions.assertEquals("农历庚子年甲申月癸卯日",cyclicalYMD);
	}

	@Test
	public void getCyclicalYMDTest4(){
		//通过公历构建
		final ChineseDate chineseDate = new ChineseDate(Objects.requireNonNull(DateUtil.parse("1905-08-28")));
		final String cyclicalYMD = chineseDate.toString(ChineseDateFormat.GGG);
		Assertions.assertEquals("农历乙巳年甲申月己亥日",cyclicalYMD);
	}
}
