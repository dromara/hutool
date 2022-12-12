package cn.hutool.core.convert;

import org.junit.Assert;
import org.junit.Test;

public class ConvertToBooleanTest {

	@Test
	public void intToBooleanTest() {
		final int a = 100;
		final Boolean aBoolean = Convert.toBool(a);
		Assert.assertTrue(aBoolean);

		final int b = 0;
		final Boolean bBoolean = Convert.toBool(b);
		Assert.assertFalse(bBoolean);
	}

	@Test
	public void issueI65P8ATest() {
		final Boolean bool = Convert.toBool("", Boolean.TRUE);
		Assert.assertFalse(bool);
	}

}
