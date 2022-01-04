package cn.hutool.core.convert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConvertToBooleanTest {

	@Test
	public void intToBooleanTest(){
		int a = 100;
		final Boolean aBoolean = Convert.toBool(a);
		Assertions.assertTrue(aBoolean);

		int b = 0;
		final Boolean bBoolean = Convert.toBool(b);
		Assertions.assertFalse(bBoolean);
	}
}
