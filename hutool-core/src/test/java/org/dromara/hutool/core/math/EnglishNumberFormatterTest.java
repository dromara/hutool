package org.dromara.hutool.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EnglishNumberFormatterTest {
	@Test
	public void testFormatNull() {
		// 测试传入null值的情况
		final String result = EnglishNumberFormatter.format(null);
		Assertions.assertEquals("", result);
	}

	@Test
	public void testFormatInteger() {
		// 测试传入整数的情况
		String result = EnglishNumberFormatter.format(1234);
		Assertions.assertEquals("ONE THOUSAND TWO HUNDRED AND THIRTY FOUR ONLY", result);

		result = EnglishNumberFormatter.format(1204);
		Assertions.assertEquals("ONE THOUSAND TWO HUNDRED AND FOUR ONLY", result);

		result = EnglishNumberFormatter.format(1004);
		Assertions.assertEquals("ONE THOUSAND FOUR ONLY", result);
	}

	@Test
	public void testFormatDecimal() {
		// 测试传入小数的情况
		final String result = EnglishNumberFormatter.format(1234.56);
		Assertions.assertEquals("ONE THOUSAND TWO HUNDRED AND THIRTY FOUR AND CENTS FIFTY SIX ONLY", result);
	}

	@Test
	public void testFormatLargeNumber() {
		// 测试传入大数字的情况
		final String result = EnglishNumberFormatter.format(1234567890123L);
		Assertions.assertEquals("ONE TRILLION TWO HUNDRED AND THIRTY FOUR BILLION FIVE HUNDRED AND SIXTY SEVEN MILLION EIGHT HUNDRED AND NINETY THOUSAND ONE HUNDRED AND TWENTY THREE ONLY", result);
	}

	@Test
	public void testFormatNonNumeric() {
		Assertions.assertThrows(NumberFormatException.class, ()->{
			// 测试传入非数字字符串的情况
			EnglishNumberFormatter.format("non-numeric");
		});
	}

	@Test
	public void issue3579Test() {
		Assertions.assertEquals("ZERO AND CENTS TEN ONLY", EnglishNumberFormatter.format(0.1));
		Assertions.assertEquals("ZERO AND CENTS ONE ONLY", EnglishNumberFormatter.format(0.01));
	}
}
