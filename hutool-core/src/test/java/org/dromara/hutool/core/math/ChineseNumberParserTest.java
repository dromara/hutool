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

package org.dromara.hutool.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class ChineseNumberParserTest {
	@Test
	public void chineseToNumberTest(){
		Assertions.assertEquals(0, parseFromChinese("零"));
		Assertions.assertEquals(102, parseFromChinese("一百零二"));
		Assertions.assertEquals(112, parseFromChinese("一百一十二"));
		Assertions.assertEquals(1012, parseFromChinese("一千零一十二"));
		Assertions.assertEquals(1000000, parseFromChinese("一百万"));
		Assertions.assertEquals(2000100112, parseFromChinese("二十亿零一十万零一百一十二"));
	}

	@Test
	public void chineseToNumberTest2(){
		Assertions.assertEquals(120, parseFromChinese("一百二"));
		Assertions.assertEquals(1200, parseFromChinese("一千二"));
		Assertions.assertEquals(22000, parseFromChinese("两万二"));
		Assertions.assertEquals(22003, parseFromChinese("两万二零三"));
		Assertions.assertEquals(22010, parseFromChinese("两万二零一十"));
	}

	@Test
	public void chineseToNumberTest3(){
		// issue#1726，对于单位开头的数组，默认赋予1
		// 十二 -> 一十二
		// 百二 -> 一百二
		Assertions.assertEquals(12, parseFromChinese("十二"));
		Assertions.assertEquals(120, parseFromChinese("百二"));
		Assertions.assertEquals(1300, parseFromChinese("千三"));
	}

	@Test
	public void badNumberTest(){
		Assertions.assertThrows(IllegalArgumentException.class, ()->{
			// 连续数字检查
			ChineseNumberParser.parseFromChinese("一百一二三");
		});
	}

	@Test
	public void badNumberTest2(){
		Assertions.assertThrows(IllegalArgumentException.class, ()->{
			// 非法字符
			ChineseNumberParser.parseFromChinese("一百你三");
		});
	}

	@Test
	public void testChineseMoneyToNumber(){
		/*
		 * s=陆万柒仟伍佰伍拾陆圆, n=67556
		 * s=陆万柒仟伍佰伍拾陆元, n=67556
		 * s=叁角, n=0.3
		 * s=贰分, n=0.02
		 * s=陆万柒仟伍佰伍拾陆元叁角, n=67556.3
		 * s=陆万柒仟伍佰伍拾陆元贰分, n=67556.02
		 * s=叁角贰分, n=0.32
		 * s=陆万柒仟伍佰伍拾陆元叁角贰分, n=67556.32
		 */
		Assertions.assertEquals(67556, parseFromChinese("陆万柒仟伍佰伍拾陆圆"));
		Assertions.assertEquals(67556, parseFromChinese("陆万柒仟伍佰伍拾陆元"));
		Assertions.assertEquals(0.3D, parseFromChinese("叁角"), 0);
		Assertions.assertEquals(0.02, parseFromChinese("贰分"), 0);
		Assertions.assertEquals(67556.3, parseFromChinese("陆万柒仟伍佰伍拾陆元叁角"), 0);
		Assertions.assertEquals(67556.02, parseFromChinese("陆万柒仟伍佰伍拾陆元贰分"), 0);
		Assertions.assertEquals(0.32, parseFromChinese("叁角贰分"), 0);
		Assertions.assertEquals(67556.32, parseFromChinese("陆万柒仟伍佰伍拾陆元叁角贰分"), 0);
	}

	@Test
	void parseFromChineseNumberTest() {
		BigDecimal i = ChineseNumberParser.parseFromChineseNumber("十二点二三");
		Assertions.assertEquals(NumberUtil.toBigDecimal(12.23D), i);

		i = ChineseNumberParser.parseFromChineseNumber("三点一四一五九二六五四");
		Assertions.assertEquals(NumberUtil.toBigDecimal(3.141592654D), i);
	}

	private double parseFromChinese(final String str){
		return ChineseNumberParser.parseFromChinese(str).doubleValue();
	}
}
