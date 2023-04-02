package org.dromara.hutool.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MoneyTest {

	@Test
	public void yuanToCentTest() {
		final Money money = new Money("1234.56");
		Assertions.assertEquals(123456, money.getCent());

		Assertions.assertEquals(123456, MathUtil.yuanToCent(1234.56));
	}

	@Test
	public void centToYuanTest() {
		final Money money = new Money(1234, 56);
		Assertions.assertEquals(1234.56D, money.getAmount().doubleValue(), 0);

		Assertions.assertEquals(1234.56D, MathUtil.centToYuan(123456), 0);
	}
}
