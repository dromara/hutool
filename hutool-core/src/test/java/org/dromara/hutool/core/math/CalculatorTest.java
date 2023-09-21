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

package org.dromara.hutool.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalculatorTest {

	@Test
	public void conversationTest(){
		final double conversion = Calculator.conversion("(0*1--3)-5/-4-(3*(-2.13))");
		Assertions.assertEquals(10.64, conversion, 0);
	}

	@Test
	public void conversationTest2(){
		final double conversion = Calculator.conversion("77 * 12");
		Assertions.assertEquals(924.0, conversion, 0);
	}

	@Test
	public void conversationTest3(){
		final double conversion = Calculator.conversion("1");
		Assertions.assertEquals(1, conversion, 0);
	}

	@Test
	public void conversationTest4(){
		final double conversion = Calculator.conversion("(88*66/23)%26+45%9");
		Assertions.assertEquals((88D * 66 / 23) % 26, conversion, 0.000000001);
	}

	@Test
	public void conversationTest5(){
		// https://github.com/dromara/hutool/issues/1984
		final double conversion = Calculator.conversion("((1/1) / (1/1) -1) * 100");
		Assertions.assertEquals(0, conversion, 0);
	}

	@Test
	public void conversationTest6() {
		final double conversion = Calculator.conversion("-((2.12-2) * 100)");
		Assertions.assertEquals(-1D * (2.12D - 2D) * 100D, conversion, 0.00001);
	}

	@Test
	public void conversationTest7() {
		//https://gitee.com/dromara/hutool/issues/I4KONB
		final double conversion = Calculator.conversion("((-2395+0) * 0.3+140.24+35+90)/30");
		Assertions.assertEquals(-15.11D, conversion, 0.01);
	}

	@Test
	public void issue2964Test() {
		// 忽略数字之间的运算符，按照乘法对待。
		// https://github.com/dromara/hutool/issues/2964
		final double calcValue = Calculator.conversion("(11+2)12");
		Assertions.assertEquals(156D, calcValue, 0.001);
	}
}
