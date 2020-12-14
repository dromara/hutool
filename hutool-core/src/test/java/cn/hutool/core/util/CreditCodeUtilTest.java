package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

public class CreditCodeUtilTest {

	@Test
	public void isCreditCodeBySimple() {
		String testCreditCode = "91310115591693856A";
		Assert.assertTrue(CreditCodeUtil.isCreditCodeSimple(testCreditCode));
	}

	@Test
	public void isCreditCode() {
		String testCreditCode = "91310110666007217T";
		Assert.assertTrue(CreditCodeUtil.isCreditCode(testCreditCode));
	}

	@Test
	public void randomCreditCode() {
		final String s = CreditCodeUtil.randomCreditCode();
		Assert.assertTrue(CreditCodeUtil.isCreditCode(s));
	}
}