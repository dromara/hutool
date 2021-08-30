package cn.hutool.core.math;

import org.junit.Assert;
import org.junit.Test;

public class MoneyTest {

	@Test
	public void yuanToCentTest() {
		final Money money = new Money("1234.56");
		Assert.assertEquals(123456, money.getCent());

		Assert.assertEquals(123456, MathUtil.yuanToCent(1234.56));
	}

	@Test
	public void centToYuanTest() {
		final Money money = new Money(1234, 56);
		Assert.assertEquals(1234.56D, money.getAmount().doubleValue(), 2);

		Assert.assertEquals(1234.56D, MathUtil.centToYuan(123456), 2);
	}
}
