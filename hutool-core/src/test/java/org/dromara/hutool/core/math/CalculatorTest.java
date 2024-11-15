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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {

	@Test
	public void conversationTest(){
		final double conversion = Calculator.conversion("(0*1--3)-5/-4-(3*(-2.13))");
		assertEquals(10.64, conversion, 0);
	}

	@Test
	public void conversationTest2(){
		final double conversion = Calculator.conversion("77 * 12");
		assertEquals(924.0, conversion, 0);
	}

	@Test
	public void conversationTest3(){
		final double conversion = Calculator.conversion("1");
		assertEquals(1, conversion, 0);
	}

	@Test
	public void conversationTest4(){
		final double conversion = Calculator.conversion("(88*66/23)%26+45%9");
		assertEquals((88D * 66 / 23) % 26, conversion, 0.000000001);
	}

	@Test
	public void conversationTest5(){
		// https://github.com/dromara/hutool/issues/1984
		final double conversion = Calculator.conversion("((1/1) / (1/1) -1) * 100");
		assertEquals(0, conversion, 0);
	}

	@Test
	public void conversationTest6() {
		final double conversion = Calculator.conversion("-((2.12-2) * 100)");
		assertEquals(-1D * (2.12D - 2D) * 100D, conversion, 0.00001);
	}

	@Test
	public void conversationTest7() {
		//https://gitee.com/dromara/hutool/issues/I4KONB
		final double conversion = Calculator.conversion("((-2395+0) * 0.3+140.24+35+90)/30");
		assertEquals(-15.11D, conversion, 0.01);
	}

	@Test
	public void issue2964Test() {
		// 忽略数字之间的运算符，按照乘法对待。
		// https://github.com/dromara/hutool/issues/2964
		final double calcValue = Calculator.conversion("(11+2)12");
		assertEquals(156D, calcValue, 0.001);
	}

	@Test
	void issue3787Test() {
		final Calculator calculator1 = new Calculator();
		double result = calculator1.calculate("0+50/100x(1/0.5)");
		assertEquals(1D, result);

		result = calculator1.calculate("0+50/100X(1/0.5)");
		assertEquals(1D, result);
	}
}
