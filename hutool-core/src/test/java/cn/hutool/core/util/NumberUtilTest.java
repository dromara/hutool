package cn.hutool.core.util;

import cn.hutool.core.lang.Console;
import cn.hutool.core.math.NumberUtil;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * {@link NumberUtil} 单元测试类
 *
 * @author Looly
 */
public class NumberUtilTest {

	@Test
	public void addTest() {
		final Float a = 3.15f;
		final Double b = 4.22;
		final double result = NumberUtil.add(a, b).doubleValue();
		Assert.assertEquals(7.37, result, 0);
	}

	@Test
	public void addTest2() {
		final double a = 3.15f;// 转换丢失精度
		final double b = 4.22;
		final double result = NumberUtil.add(a, b).doubleValue();
		Assert.assertEquals(7.37, result, 0.0001);
	}

	@Test
	public void addTest3() {
		final float a = 3.15f;
		final double b = 4.22;
		final double result = NumberUtil.add(a, b, a, b).doubleValue();
		Assert.assertEquals(14.74, result, 0);
	}

	@Test
	public void addTest4() {
		BigDecimal result = NumberUtil.add(new BigDecimal("133"), new BigDecimal("331"));
		Assert.assertEquals(new BigDecimal("464"), result);

		result = NumberUtil.add(new BigDecimal[]{new BigDecimal("133"), new BigDecimal("331")});
		Assert.assertEquals(new BigDecimal("464"), result);
	}

	@Test
	public void addBlankTest() {
		final BigDecimal result = NumberUtil.add("123", " ");
		Assert.assertEquals(new BigDecimal("123"), result);
	}

	@Test
	public void subTest() {
		BigDecimal result = NumberUtil.sub(new BigDecimal("133"), new BigDecimal("331"));
		Assert.assertEquals(new BigDecimal("-198"), result);

		result = NumberUtil.sub(new BigDecimal[]{new BigDecimal("133"), new BigDecimal("331")});
		Assert.assertEquals(new BigDecimal("-198"), result);
	}

	@Test
	public void mulTest() {
		BigDecimal result = NumberUtil.mul(new BigDecimal("133"), new BigDecimal("331"));
		Assert.assertEquals(new BigDecimal("44023"), result);

		result = NumberUtil.mul(new BigDecimal[]{new BigDecimal("133"), new BigDecimal("331")});
		Assert.assertEquals(new BigDecimal("44023"), result);
	}

	@Test
	public void mulNullTest() {
		final BigDecimal mul = NumberUtil.mul(new BigDecimal("10"), null);
		Assert.assertEquals(BigDecimal.ZERO, mul);
	}

	@Test
	public void isIntegerTest() {
		String[] validNumArr = {"0", "-0", "+0", "12", "+12", "1234567890", "2147483647", "-2147483648",
				"0x012345678", "0X012345678", "0xabcdef", "-0xabcdef", "0x12abcdef", "0x7FFFFFFF", "-0x80000000",
				"01234567", "017777777777", "-020000000000"
		};
		privateIsIntegerTest(validNumArr, true);

		String[] invalidNumArr = {null, "", "  ", "+", "+1.", ".1", "99L", "99D", "99F", "12.3", "123e1", "-12.3", "1.2.3",
				"2147483648", "0x80000000", "020000000000", "-2147483649", "-0x80000001", "-020000000001", "-020000000001",
				"a", "+a", "123abc", "09"
		};
		privateIsIntegerTest(invalidNumArr, false);
	}

	private void privateIsIntegerTest(final String[] numStrArr, final boolean expected) {
		for (String numStr : numStrArr) {
			Assert.assertEquals("未通过的数字为: " + numStr, expected, NumberUtil.isInteger(numStr));
		}
	}

	@Test
	public void isLongTest() {
		String[] validNumArr = {
				"0", "0L", "-0L", "+0L", "12", "+12", "1234567890123456789", "99L",
				"2147483648", "0x80000000", "020000000000", "-2147483649", "-0x80000001", "-020000000001", "-020000000001",
				"9223372036854775807", "-9223372036854775808",
				"0x0123456789", "0X0123456789", "0x123456789abcdef", "0xabcdef123456789", "0x7FFFFFFF", "-0x80000000",
				"0x7fffffffffffffff", "-0x8000000000000000",
				"01234567", "0777777777777777777777", "-01000000000000000000000"
		};
		privateIsLongTest(validNumArr, true);

		String[] invalidNumArr = {null, "", "  ", "+", "+1.", ".1", "99D", "99F", "12.3", "123e1", "-12.3", "1.2.3",
				"a", "+a", "123abc", "09",
				"9223372036854775808", "-9223372036854775809",
				"0x8000000000000000", "-0x8000000000000001",
				"01000000000000000000000", "-01000000000000000000001"
		};
		privateIsLongTest(invalidNumArr, false);
	}

	private void privateIsLongTest(final String[] numStrArr, final boolean expected) {
		for (String numStr : numStrArr) {
			Assert.assertEquals("未通过的数字为: " + numStr, expected, NumberUtil.isLong(numStr));
		}
	}

	@Test
	public void isNumberTest() {
		privateIsNumberTest("28.55", true);
		privateIsNumberTest("12345", true);
		privateIsNumberTest("1234.5", true);
		privateIsNumberTest(".12345", true);
		privateIsNumberTest("1234E5", true);
		privateIsNumberTest("1234E+5", true);
		privateIsNumberTest("1234E-5", true);
		privateIsNumberTest("123.4E5", true);
		privateIsNumberTest("-1234", true);
		privateIsNumberTest("-1234.5", true);
		privateIsNumberTest("-.12345", true);
		privateIsNumberTest("-1234E5", true);
		privateIsNumberTest("0", true);
		privateIsNumberTest("0.1", true); // LANG-1216
		privateIsNumberTest("-0", true);
		privateIsNumberTest("01234", true);
		privateIsNumberTest("-01234", true);
		privateIsNumberTest("-0xABC123", true);
		privateIsNumberTest("-0x0", true);
		privateIsNumberTest("123.4E21D", true);
		privateIsNumberTest("-221.23F", true);
		privateIsNumberTest("22338L", true);

		privateIsNumberTest(null, false);
		privateIsNumberTest("", false);
		privateIsNumberTest(" ", false);
		privateIsNumberTest("\r\n\t", false);
		privateIsNumberTest("--2.3", false);
		privateIsNumberTest(".12.3", false);
		privateIsNumberTest("-123E", false);
		privateIsNumberTest("-123E+-212", false);
		privateIsNumberTest("-123E2.12", false);
		privateIsNumberTest("0xGF", false);
		privateIsNumberTest("0xFAE-1", false);
		privateIsNumberTest(".", false);
		privateIsNumberTest("-0ABC123", false);
		privateIsNumberTest("123.4E-D", false);
		privateIsNumberTest("123.4ED", false);
		privateIsNumberTest("1234E5l", false);
		privateIsNumberTest("11a", false);
		privateIsNumberTest("1a", false);
		privateIsNumberTest("a", false);
		privateIsNumberTest("11g", false);
		privateIsNumberTest("11z", false);
		privateIsNumberTest("11def", false);
		privateIsNumberTest("11d11", false);
		privateIsNumberTest("11 11", false);
		privateIsNumberTest(" 1111", false);
		privateIsNumberTest("1111 ", false);

		privateIsNumberTest("2.", true); // LANG-521
		privateIsNumberTest("1.1L", false); // LANG-664
		privateIsNumberTest("+0xF", true); // LANG-1645
		privateIsNumberTest("+0xFFFFFFFF", true); // LANG-1645
		privateIsNumberTest("+0xFFFFFFFFFFFFFFFF", true); // LANG-1645
		privateIsNumberTest(".0", true); // LANG-1646
		privateIsNumberTest("0.", true); // LANG-1646
		privateIsNumberTest("0.D", true); // LANG-1646
		privateIsNumberTest("0e1", true); // LANG-1646
		privateIsNumberTest("0e1D", true); // LANG-1646
		privateIsNumberTest(".D", false); // LANG-1646
		privateIsNumberTest(".e10", false); // LANG-1646
		privateIsNumberTest(".e10D", false); // LANG-1646
	}

	private void privateIsNumberTest(final String numStr, final boolean expected) {
		Assert.assertEquals(expected, NumberUtil.isNumber(numStr));
	}

	@Test
	public void divTest() {
		final double result = NumberUtil.div(0, 1).doubleValue();
		Assert.assertEquals(0.0, result, 0);
	}

	@Test
	public void divBigDecimalTest() {
		final BigDecimal result = NumberUtil.div(BigDecimal.ZERO, BigDecimal.ONE);
		Assert.assertEquals(BigDecimal.ZERO, result.stripTrailingZeros());
	}

	@Test
	public void roundTest() {

		// 四舍
		final String round1 = NumberUtil.roundStr(2.674, 2);
		final String round2 = NumberUtil.roundStr("2.674", 2);
		Assert.assertEquals("2.67", round1);
		Assert.assertEquals("2.67", round2);

		// 五入
		final String round3 = NumberUtil.roundStr(2.675, 2);
		final String round4 = NumberUtil.roundStr("2.675", 2);
		Assert.assertEquals("2.68", round3);
		Assert.assertEquals("2.68", round4);

		// 四舍六入五成双
		final String round31 = NumberUtil.roundStr(4.245, 2, RoundingMode.HALF_EVEN);
		final String round41 = NumberUtil.roundStr("4.2451", 2, RoundingMode.HALF_EVEN);
		Assert.assertEquals("4.24", round31);
		Assert.assertEquals("4.25", round41);

		// 补0
		final String round5 = NumberUtil.roundStr(2.6005, 2);
		final String round6 = NumberUtil.roundStr("2.6005", 2);
		Assert.assertEquals("2.60", round5);
		Assert.assertEquals("2.60", round6);

		// 补0
		final String round7 = NumberUtil.roundStr(2.600, 2);
		final String round8 = NumberUtil.roundStr("2.600", 2);
		Assert.assertEquals("2.60", round7);
		Assert.assertEquals("2.60", round8);
	}

	@Test
	public void roundStrTest() {
		final String roundStr = NumberUtil.roundStr(2.647, 2);
		Assert.assertEquals(roundStr, "2.65");

		final String roundStr1 = NumberUtil.roundStr(0, 10);
		Assert.assertEquals(roundStr1, "0.0000000000");
	}

	@Test
	public void roundHalfEvenTest() {
		String roundStr = NumberUtil.roundHalfEven(4.245, 2).toString();
		Assert.assertEquals(roundStr, "4.24");
		roundStr = NumberUtil.roundHalfEven(4.2450, 2).toString();
		Assert.assertEquals(roundStr, "4.24");
		roundStr = NumberUtil.roundHalfEven(4.2451, 2).toString();
		Assert.assertEquals(roundStr, "4.25");
		roundStr = NumberUtil.roundHalfEven(4.2250, 2).toString();
		Assert.assertEquals(roundStr, "4.22");

		roundStr = NumberUtil.roundHalfEven(1.2050, 2).toString();
		Assert.assertEquals(roundStr, "1.20");
		roundStr = NumberUtil.roundHalfEven(1.2150, 2).toString();
		Assert.assertEquals(roundStr, "1.22");
		roundStr = NumberUtil.roundHalfEven(1.2250, 2).toString();
		Assert.assertEquals(roundStr, "1.22");
		roundStr = NumberUtil.roundHalfEven(1.2350, 2).toString();
		Assert.assertEquals(roundStr, "1.24");
		roundStr = NumberUtil.roundHalfEven(1.2450, 2).toString();
		Assert.assertEquals(roundStr, "1.24");
		roundStr = NumberUtil.roundHalfEven(1.2550, 2).toString();
		Assert.assertEquals(roundStr, "1.26");
		roundStr = NumberUtil.roundHalfEven(1.2650, 2).toString();
		Assert.assertEquals(roundStr, "1.26");
		roundStr = NumberUtil.roundHalfEven(1.2750, 2).toString();
		Assert.assertEquals(roundStr, "1.28");
		roundStr = NumberUtil.roundHalfEven(1.2850, 2).toString();
		Assert.assertEquals(roundStr, "1.28");
		roundStr = NumberUtil.roundHalfEven(1.2950, 2).toString();
		Assert.assertEquals(roundStr, "1.30");
	}

	@Test
	public void decimalFormatTest() {
		final long c = 299792458;// 光速

		final String format = NumberUtil.format(",###", c);
		Assert.assertEquals("299,792,458", format);
	}

	@Test(expected = IllegalArgumentException.class)
	public void decimalFormatNaNTest() {
		final Double a = 0D;
		final Double b = 0D;

		final Double c = a / b;
		Console.log(NumberUtil.format("#%", c));
	}

	@Test(expected = IllegalArgumentException.class)
	public void decimalFormatNaNTest2() {
		final Double a = 0D;
		final Double b = 0D;

		Console.log(NumberUtil.format("#%", a / b));
	}

	@Test
	public void decimalFormatDoubleTest() {
		final Double c = 467.8101;

		final String format = NumberUtil.format("0.00", c);
		Assert.assertEquals("467.81", format);
	}

	@Test
	public void decimalFormatMoneyTest() {
		final double c = 299792400.543534534;

		final String format = NumberUtil.formatMoney(c);
		Assert.assertEquals("299,792,400.54", format);

		final double value = 0.5;
		final String money = NumberUtil.formatMoney(value);
		Assert.assertEquals("0.50", money);
	}

	@Test
	public void equalsTest() {
		Assert.assertTrue(NumberUtil.equals(new BigDecimal("0.00"), BigDecimal.ZERO));
	}

	@Test
	public void toBigDecimalTest() {
		final double a = 3.14;

		BigDecimal bigDecimal = NumberUtil.toBigDecimal(a);
		Assert.assertEquals("3.14", bigDecimal.toString());

		bigDecimal = NumberUtil.toBigDecimal("1,234.55");
		Assert.assertEquals("1234.55", bigDecimal.toString());

		bigDecimal = NumberUtil.toBigDecimal("1,234.56D");
		Assert.assertEquals("1234.56", bigDecimal.toString());
	}

	@Test
	public void parseIntTest() {
		int number = NumberUtil.parseInt("0xFF");
		Assert.assertEquals(255, number);

		// 0开头
		number = NumberUtil.parseInt("010");
		Assert.assertEquals(10, number);

		number = NumberUtil.parseInt("10");
		Assert.assertEquals(10, number);

		number = NumberUtil.parseInt("   ");
		Assert.assertEquals(0, number);

		number = NumberUtil.parseInt("10F");
		Assert.assertEquals(10, number);

		number = NumberUtil.parseInt("22.4D");
		Assert.assertEquals(22, number);

		number = NumberUtil.parseInt("22.6D");
		Assert.assertEquals(22, number);

		number = NumberUtil.parseInt("0");
		Assert.assertEquals(0, number);

		number = NumberUtil.parseInt(".123");
		Assert.assertEquals(0, number);
	}

	@Test
	public void parseIntTest2() {
		// from 5.4.8 issue#I23ORQ@Gitee
		// 千位分隔符去掉
		final int v1 = NumberUtil.parseInt("1,482.00");
		Assert.assertEquals(1482, v1);
	}

	@Test(expected = NumberFormatException.class)
	public void parseIntTest3() {
		final int v1 = NumberUtil.parseInt("d");
		Assert.assertEquals(0, v1);
	}

	@Test(expected = NumberFormatException.class)
	public void parseIntTest4() {
		// issue#I5M55F
		// 科学计数法忽略支持，科学计数法一般用于表示非常小和非常大的数字，这类数字转换为int后精度丢失，没有意义。
		final String numberStr = "429900013E20220812163344551";
		NumberUtil.parseInt(numberStr);
	}

	@Test
	public void parseNumberTest() {
		// from 5.4.8 issue#I23ORQ@Gitee
		// 千位分隔符去掉
		final int v1 = NumberUtil.parseNumber("1,482.00").intValue();
		Assert.assertEquals(1482, v1);

		final Number v2 = NumberUtil.parseNumber("1,482.00D");
		Assert.assertEquals(1482L, v2.longValue());
	}

	@Test
	public void parseNumberTest2() {
		// issue#I5M55F
		final String numberStr = "429900013E20220812163344551";
		final Number number = NumberUtil.parseNumber(numberStr);
		Assert.assertNotNull(number);
		Assert.assertTrue(number instanceof BigDecimal);
	}

	@Test
	public void parseHexNumberTest() {
		// 千位分隔符去掉
		final int v1 = NumberUtil.parseNumber("0xff").intValue();
		Assert.assertEquals(255, v1);
	}

	@Test
	public void parseLongTest() {
		long number = NumberUtil.parseLong("0xFF");
		Assert.assertEquals(255, number);

		// 0开头
		number = NumberUtil.parseLong("010");
		Assert.assertEquals(10, number);

		number = NumberUtil.parseLong("10");
		Assert.assertEquals(10, number);

		number = NumberUtil.parseLong("   ");
		Assert.assertEquals(0, number);

		number = NumberUtil.parseLong("10F");
		Assert.assertEquals(10, number);

		number = NumberUtil.parseLong("22.4D");
		Assert.assertEquals(22, number);

		number = NumberUtil.parseLong("22.6D");
		Assert.assertEquals(22, number);

		number = NumberUtil.parseLong("0");
		Assert.assertEquals(0, number);

		number = NumberUtil.parseLong(".123");
		Assert.assertEquals(0, number);
	}

	@Test
	public void isPowerOfTwoTest() {
		Assert.assertFalse(NumberUtil.isPowerOfTwo(-1));
		Assert.assertTrue(NumberUtil.isPowerOfTwo(16));
		Assert.assertTrue(NumberUtil.isPowerOfTwo(65536));
		Assert.assertTrue(NumberUtil.isPowerOfTwo(1));
		Assert.assertFalse(NumberUtil.isPowerOfTwo(17));
	}

	@Test
	public void toStrTest() {
		Assert.assertEquals("1", NumberUtil.toStr(new BigDecimal("1.0000000000")));
		Assert.assertEquals("0", NumberUtil.toStr(NumberUtil.sub(new BigDecimal("9600.00000"), new BigDecimal("9600.00000"))));
		Assert.assertEquals("0", NumberUtil.toStr(NumberUtil.sub(new BigDecimal("9600.0000000000"), new BigDecimal("9600.000000"))));
		Assert.assertEquals("0", NumberUtil.toStr(new BigDecimal("9600.00000").subtract(new BigDecimal("9600.000000000"))));
	}

	@Test
	public void toPlainNumberTest() {
		final String num = "5344.34234e3";
		final String s = new BigDecimal(num).toPlainString();
		Assert.assertEquals("5344342.34", s);
	}

	@Test
	public void isOddOrEvenTest() {
		final int[] a = {0, 32, -32, 123, -123};
		Assert.assertFalse(NumberUtil.isOdd(a[0]));
		Assert.assertTrue(NumberUtil.isEven(a[0]));

		Assert.assertFalse(NumberUtil.isOdd(a[1]));
		Assert.assertTrue(NumberUtil.isEven(a[1]));

		Assert.assertFalse(NumberUtil.isOdd(a[2]));
		Assert.assertTrue(NumberUtil.isEven(a[2]));

		Assert.assertTrue(NumberUtil.isOdd(a[3]));
		Assert.assertFalse(NumberUtil.isEven(a[3]));

		Assert.assertTrue(NumberUtil.isOdd(a[4]));
		Assert.assertFalse(NumberUtil.isEven(a[4]));
	}

	@Test
	public void toBigIntegerTest() {
		final Number number = 1123123;
		final Number number2 = 1123123.123;
		Assert.assertNotNull(NumberUtil.toBigInteger(number));
		Assert.assertNotNull(NumberUtil.toBigInteger(number2));
	}

	@Test
	public void divIntegerTest() {
		final BigDecimal div = NumberUtil.div(100101300, 100);
		Assert.assertEquals(1001013, div.intValue());
	}

	@Test
	public void isDoubleTest() {
		Assert.assertFalse(NumberUtil.isDouble(null));
		Assert.assertFalse(NumberUtil.isDouble(""));
		Assert.assertFalse(NumberUtil.isDouble("  "));
		Assert.assertFalse(NumberUtil.isDouble("1"));
		Assert.assertFalse(NumberUtil.isDouble("-1"));
		Assert.assertFalse(NumberUtil.isDouble("+1"));
		Assert.assertFalse(NumberUtil.isDouble("0.1."));
		Assert.assertFalse(NumberUtil.isDouble("01"));
		Assert.assertFalse(NumberUtil.isDouble("NaN"));
		Assert.assertFalse(NumberUtil.isDouble("-NaN"));
		Assert.assertFalse(NumberUtil.isDouble("-Infinity"));

		Assert.assertTrue(NumberUtil.isDouble("0."));
		Assert.assertTrue(NumberUtil.isDouble("0.1"));
		Assert.assertTrue(NumberUtil.isDouble("-0.1"));
		Assert.assertTrue(NumberUtil.isDouble("+0.1"));
		Assert.assertTrue(NumberUtil.isDouble("0.110"));
		Assert.assertTrue(NumberUtil.isDouble("00.1"));
		Assert.assertTrue(NumberUtil.isDouble("01.1"));
	}

	@Test
	public void rangeTest() {
		final int[] range = NumberUtil.range(0, 10);
		Assert.assertEquals(0, range[0]);
		Assert.assertEquals(1, range[1]);
		Assert.assertEquals(2, range[2]);
		Assert.assertEquals(3, range[3]);
		Assert.assertEquals(4, range[4]);
		Assert.assertEquals(5, range[5]);
		Assert.assertEquals(6, range[6]);
		Assert.assertEquals(7, range[7]);
		Assert.assertEquals(8, range[8]);
		Assert.assertEquals(9, range[9]);
		Assert.assertEquals(10, range[10]);
	}

	@Test(expected = NegativeArraySizeException.class)
	public void rangeMinTest() {
		//noinspection ResultOfMethodCallIgnored
		NumberUtil.range(0, Integer.MIN_VALUE);
	}

	@Test
	public void isPrimeTest(){
		Assert.assertTrue(NumberUtil.isPrime(2));
		Assert.assertTrue(NumberUtil.isPrime(3));
		Assert.assertTrue(NumberUtil.isPrime(7));
		Assert.assertTrue(NumberUtil.isPrime(17));
		Assert.assertTrue(NumberUtil.isPrime(296731));
		Assert.assertTrue(NumberUtil.isPrime(99999989));

		Assert.assertFalse(NumberUtil.isPrime(4));
		Assert.assertFalse(NumberUtil.isPrime(296733));
		Assert.assertFalse(NumberUtil.isPrime(20_4123_2399));
	}
}
