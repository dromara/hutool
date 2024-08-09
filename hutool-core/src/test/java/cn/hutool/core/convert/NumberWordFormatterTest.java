package cn.hutool.core.convert;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NumberWordFormatterTest {

	@Test
	public void testFormatNull() {
		// 测试传入null值的情况
		String result = NumberWordFormatter.format(null);
		assertEquals("", result);
	}

	@Test
	public void testFormatInteger() {
		// 测试传入整数的情况
		String result = NumberWordFormatter.format(1234);
		assertEquals("ONE THOUSAND TWO HUNDRED AND THIRTY FOUR ONLY", result);

		result = NumberWordFormatter.format(1204);
		assertEquals("ONE THOUSAND TWO HUNDRED AND FOUR ONLY", result);

		result = NumberWordFormatter.format(1004);
		assertEquals("ONE THOUSAND FOUR ONLY", result);
	}

	@Test
	public void testFormatDecimal() {
		// 测试传入小数的情况
		String result = NumberWordFormatter.format(1234.56);
		assertEquals("ONE THOUSAND TWO HUNDRED AND THIRTY FOUR AND CENTS FIFTY SIX ONLY", result);
	}

	@Test
	public void testFormatLargeNumber() {
		// 测试传入大数字的情况
		String result = NumberWordFormatter.format(1234567890123L);
		assertEquals("ONE TRILLION TWO HUNDRED AND THIRTY FOUR BILLION FIVE HUNDRED AND SIXTY SEVEN MILLION EIGHT HUNDRED AND NINETY THOUSAND ONE HUNDRED AND TWENTY THREE ONLY", result);
	}

	@Test
	public void testFormatNonNumeric() {
		assertThrows(NumberFormatException.class, () -> {
			// 测试传入非数字字符串的情况
			NumberWordFormatter.format("non-numeric");
		});
	}

	@Test
	public void issue3579Test() {
		assertEquals("ZERO AND CENTS TEN ONLY", NumberWordFormatter.format(0.1));
		assertEquals("ZERO AND CENTS ONE ONLY", NumberWordFormatter.format(0.01));
	}
}

