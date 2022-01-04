package cn.hutool.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class CalculatorTest {

	@Test
	public void conversationTest(){
		final double conversion = Calculator.conversion("(0*1--3)-5/-4-(3*(-2.13))");
		Assertions.assertEquals(10.64, conversion, 2);
	}

	@Test
	public void conversationTest2(){
		final double conversion = Calculator.conversion("77 * 12");
		Assertions.assertEquals(924.0, conversion, 2);
	}

	@Test
	public void conversationTest3(){
		final double conversion = Calculator.conversion("1");
		Assertions.assertEquals(1, conversion, 2);
	}

	@Test
	public void conversationTest4(){
		final double conversion = Calculator.conversion("(88*66/23)%26+45%9");
		Assertions.assertEquals((88D * 66 / 23) % 26, conversion, 2);
	}

	@Test
	@Disabled
	public void conversationTest5(){
		// https://github.com/dromara/hutool/issues/1984
		final double conversion = Calculator.conversion("((1/1) / (1/1) -1) * 100");
		Assertions.assertEquals((88D * 66 / 23) % 26, conversion, 2);
	}
}
