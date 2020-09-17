package cn.hutool.core.math;

import org.junit.Assert;
import org.junit.Test;

public class CalculatorTest {

	@Test
	public void conversationTest(){
		final double conversion = Calculator.conversion("(0*1--3)-5/-4-(3*(-2.13))");
		Assert.assertEquals(10.64, conversion, 2);
	}

	@Test
	public void conversationTest2(){
		final double conversion = Calculator.conversion("77 * 12");
		Assert.assertEquals(924.0, conversion, 2);
	}
}
