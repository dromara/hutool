package cn.hutool.core.convert;


import org.junit.Assert;
import org.junit.Test;

public class NumberWordFormatterTest {

	@Test
	public void testFormatNull() {
		// 测试传入null值的情况
		String result = NumberWordFormatter.format(null);
		Assert.assertEquals("", result);
	}

	@Test
	public void testFormatInteger() {
		// 测试传入整数的情况
		String result = NumberWordFormatter.format(1234);
		Assert.assertEquals("ONE THOUSAND TWO HUNDRED AND THIRTY FOUR ONLY", result);

		result = NumberWordFormatter.format(1204);
		Assert.assertEquals("ONE THOUSAND TWO HUNDRED AND FOUR ONLY", result);

		result = NumberWordFormatter.format(1004);
		Assert.assertEquals("ONE THOUSAND FOUR ONLY", result);
	}

	@Test
	public void testFormatDecimal() {
		// 测试传入小数的情况
		String result = NumberWordFormatter.format(1234.56);
		Assert.assertEquals("ONE THOUSAND TWO HUNDRED AND THIRTY FOUR AND CENTS FIFTY SIX ONLY", result);
	}

	@Test
	public void testFormatLargeNumber() {
		// 测试传入大数字的情况
		String result = NumberWordFormatter.format(1234567890123L);
		Assert.assertEquals("ONE TRILLION TWO HUNDRED AND THIRTY FOUR BILLION FIVE HUNDRED AND SIXTY SEVEN MILLION EIGHT HUNDRED AND NINETY THOUSAND ONE HUNDRED AND TWENTY THREE ONLY", result);
	}

	@Test(expected = NumberFormatException.class)
	public void testFormatNonNumeric() {
		// 测试传入非数字字符串的情况
		NumberWordFormatter.format("non-numeric");
	}

	@Test
	public void issue3579Test() {
		Assert.assertEquals("ZERO AND CENTS TEN ONLY", NumberWordFormatter.format(0.1));
		Assert.assertEquals("ZERO AND CENTS ONE ONLY", NumberWordFormatter.format(0.01));
	}
}

