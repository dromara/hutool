package org.dromara.hutool.convert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConvertToBooleanTest {

	@Test
	public void intToBooleanTest() {
		final int a = 100;
		final Boolean aBoolean = Convert.toBoolean(a);
		Assertions.assertTrue(aBoolean);

		final int b = 0;
		final Boolean bBoolean = Convert.toBoolean(b);
		Assertions.assertFalse(bBoolean);
	}

	@Test
	public void issueI65P8ATest() {
		final Boolean bool = Convert.toBoolean("", Boolean.TRUE);
		Assertions.assertFalse(bool);
	}

}
