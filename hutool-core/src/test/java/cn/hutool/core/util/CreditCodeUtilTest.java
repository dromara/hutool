package cn.hutool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CreditCodeUtilTest {

	@Test
	public void isCreditCodeBySimple() {
		String testCreditCode = "91310115591693856A";
		Assertions.assertTrue(CreditCodeUtil.isCreditCodeSimple(testCreditCode));
	}

	@Test
	public void isCreditCode() {
		String testCreditCode = "91310110666007217T";
		Assertions.assertTrue(CreditCodeUtil.isCreditCode(testCreditCode));
	}

	@Test
	public void randomCreditCode() {
		final String s = CreditCodeUtil.randomCreditCode();
		Assertions.assertTrue(CreditCodeUtil.isCreditCode(s));
	}
}
