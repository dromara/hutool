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
 *
 */
public class NumberUtilTest {

	@Test
	public void addTest() {
		final Float a = 3.15f;
		final Double b = 4.22;
		final double result = NumberUtil.add(a, b).doubleValue();
		Assert.assertEquals(7.37, result, 2);
	}

	@Test
	public void addTest2() {
		final double a = 3.15f;
		final double b = 4.22;
		final double result = NumberUtil.add(a, b).doubleValue();
		Assert.assertEquals(7.37, result, 2);
	}

	@Test
	public void addTest3() {
		final float a = 3.15f;
		final double b = 4.22;
		final double result = NumberUtil.add(a, b, a, b).doubleValue();
		Assert.assertEquals(14.74, result, 2);
	}

	@Test
	public void addTest4() {
		BigDecimal result = NumberUtil.add(new BigDecimal("133"), new BigDecimal("331"));
		Assert.assertEquals(new BigDecimal("464"), result);

		result = NumberUtil.add(new BigDecimal[]{new BigDecimal("133"), new BigDecimal("331")});
		Assert.assertEquals(new BigDecimal("464"), result);
	}

	@Test
	public void addBlankTest(){
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
	public void mulNullTest(){
		final BigDecimal mul = NumberUtil.mul(new BigDecimal("10"), null);
		Assert.assertEquals(BigDecimal.ZERO, mul);
	}

	@Test
	public void isIntegerTest() {
		Assert.assertTrue(NumberUtil.isInteger("-12"));
		Assert.assertTrue(NumberUtil.isInteger("256"));
		Assert.assertTrue(NumberUtil.isInteger("0256"));
		Assert.assertTrue(NumberUtil.isInteger("0"));
		Assert.assertFalse(NumberUtil.isInteger("23.4"));
		Assert.assertFalse(NumberUtil.isInteger(null));
		Assert.assertFalse(NumberUtil.isInteger(""));
		Assert.assertFalse(NumberUtil.isInteger(" "));
	}

	@Test
	public void isLongTest() {
		Assert.assertTrue(NumberUtil.isLong("-12"));
		Assert.assertTrue(NumberUtil.isLong("256"));
		Assert.assertTrue(NumberUtil.isLong("0256"));
		Assert.assertTrue(NumberUtil.isLong("0"));
		Assert.assertFalse(NumberUtil.isLong("23.4"));
		Assert.assertFalse(NumberUtil.isLong(null));
		Assert.assertFalse(NumberUtil.isLong(""));
		Assert.assertFalse(NumberUtil.isLong(" "));
	}

	@Test
	public void isNumberTest() {
		Assert.assertTrue(NumberUtil.isNumber("28.55"));
		Assert.assertTrue(NumberUtil.isNumber("0"));
		Assert.assertTrue(NumberUtil.isNumber("+100.10"));
		Assert.assertTrue(NumberUtil.isNumber("-22.022"));
		Assert.assertTrue(NumberUtil.isNumber("0X22"));
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
	public void decimalFormatNaNTest(){
		final Double a = 0D;
		final Double b = 0D;

		final Double c = a / b;
		Console.log(NumberUtil.format("#%", c));
	}

	@Test(expected = IllegalArgumentException.class)
	public void decimalFormatNaNTest2(){
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
	public void toStrTest(){
		Assert.assertEquals("1", NumberUtil.toStr(new BigDecimal("1.0000000000")));
		Assert.assertEquals("0", NumberUtil.toStr(NumberUtil.sub(new BigDecimal("9600.00000"), new BigDecimal("9600.00000"))));
		Assert.assertEquals("0", NumberUtil.toStr(NumberUtil.sub(new BigDecimal("9600.0000000000"), new BigDecimal("9600.000000"))));
		Assert.assertEquals("0", NumberUtil.toStr(new BigDecimal("9600.00000").subtract(new BigDecimal("9600.000000000"))));
	}

	@Test
	public void toPlainNumberTest(){
		final String num = "5344.34234e3";
		final String s = new BigDecimal(num).toPlainString();
		Assert.assertEquals("5344342.34", s);
	}

	@Test
	public void isOddOrEvenTest(){
		final int[] a = { 0, 32, -32, 123, -123 };
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
	public void toBigIntegerTest(){
		final Number number=1123123;
		final Number number2=1123123.123;
		Assert.assertNotNull(NumberUtil.toBigInteger(number));
		Assert.assertNotNull(NumberUtil.toBigInteger(number2));
	}

	@Test
	public void divIntegerTest(){
		final BigDecimal div = NumberUtil.div(100101300, 100);
		Assert.assertEquals(1001013, div.intValue());
	}

	@Test
	public void isDoubleTest(){
		Assert.assertFalse(NumberUtil.isDouble(null));
		Assert.assertFalse(NumberUtil.isDouble(""));
		Assert.assertFalse(NumberUtil.isDouble("  "));
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

	@Test(expected = NumberFormatException.class)
	public void parseIntTest3() {
		int v1 = NumberUtil.parseInt("d");
		Assert.assertEquals(0, v1);
	}
}
