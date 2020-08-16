package cn.hutool.core.convert;

import org.junit.Assert;
import org.junit.Test;

public class ConvertToBooleanTest {

	@Test
	public void intToBooleanTest(){
		int a = 100;
		final Boolean aBoolean = Convert.toBool(a);
		Assert.assertTrue(aBoolean);

		int b = 0;
		final Boolean bBoolean = Convert.toBool(b);
		Assert.assertFalse(bBoolean);
	}
}
