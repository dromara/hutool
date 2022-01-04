package cn.hutool.core.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.id.NanoId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Set;

/**
 * {@link NumberUtil} 单元测试类
 *
 * @author Looly
 *
 */
public class NumberUtilTest {

	@Test
	public void addTest() {
		Float a = 3.15f;
		Double b = 4.22;
		double result = NumberUtil.add(a, b).doubleValue();
		Assertions.assertEquals(7.37, result, 2);
	}

	@Test
	public void addTest2() {
		double a = 3.15f;
		double b = 4.22;
		double result = NumberUtil.add(a, b);
		Assertions.assertEquals(7.37, result, 2);
	}

	@Test
	public void addTest3() {
		float a = 3.15f;
		double b = 4.22;
		double result = NumberUtil.add(a, b, a, b).doubleValue();
		Assertions.assertEquals(14.74, result, 2);
	}

	@Test
	public void addTest4() {
		BigDecimal result = NumberUtil.add(new BigDecimal("133"), new BigDecimal("331"));
		Assertions.assertEquals(new BigDecimal("464"), result);
	}

	@Test
	public void addBlankTest(){
		BigDecimal result = NumberUtil.add("123", " ");
		Assertions.assertEquals(new BigDecimal("123"), result);
	}

	@Test
	public void isIntegerTest() {
		Assertions.assertTrue(NumberUtil.isInteger("-12"));
		Assertions.assertTrue(NumberUtil.isInteger("256"));
		Assertions.assertTrue(NumberUtil.isInteger("0256"));
		Assertions.assertTrue(NumberUtil.isInteger("0"));
		Assertions.assertFalse(NumberUtil.isInteger("23.4"));
	}

	@Test
	public void isLongTest() {
		Assertions.assertTrue(NumberUtil.isLong("-12"));
		Assertions.assertTrue(NumberUtil.isLong("256"));
		Assertions.assertTrue(NumberUtil.isLong("0256"));
		Assertions.assertTrue(NumberUtil.isLong("0"));
		Assertions.assertFalse(NumberUtil.isLong("23.4"));
	}

	@Test
	public void isNumberTest() {
		Assertions.assertTrue(NumberUtil.isNumber("28.55"));
		Assertions.assertTrue(NumberUtil.isNumber("0"));
		Assertions.assertTrue(NumberUtil.isNumber("+100.10"));
		Assertions.assertTrue(NumberUtil.isNumber("-22.022"));
		Assertions.assertTrue(NumberUtil.isNumber("0X22"));
	}

	@Test
	public void divTest() {
		double result = NumberUtil.div(0, 1);
		Assertions.assertEquals(0.0, result, 0);
	}

	@Test
	public void divBigDecimalTest() {
		BigDecimal result = NumberUtil.div(BigDecimal.ZERO, BigDecimal.ONE);
		Assertions.assertEquals(BigDecimal.ZERO, result.stripTrailingZeros());
	}

	@Test
	public void roundTest() {

		// 四舍
		String round1 = NumberUtil.roundStr(2.674, 2);
		String round2 = NumberUtil.roundStr("2.674", 2);
		Assertions.assertEquals("2.67", round1);
		Assertions.assertEquals("2.67", round2);

		// 五入
		String round3 = NumberUtil.roundStr(2.675, 2);
		String round4 = NumberUtil.roundStr("2.675", 2);
		Assertions.assertEquals("2.68", round3);
		Assertions.assertEquals("2.68", round4);

		// 四舍六入五成双
		String round31 = NumberUtil.roundStr(4.245, 2, RoundingMode.HALF_EVEN);
		String round41 = NumberUtil.roundStr("4.2451", 2, RoundingMode.HALF_EVEN);
		Assertions.assertEquals("4.24", round31);
		Assertions.assertEquals("4.25", round41);

		// 补0
		String round5 = NumberUtil.roundStr(2.6005, 2);
		String round6 = NumberUtil.roundStr("2.6005", 2);
		Assertions.assertEquals("2.60", round5);
		Assertions.assertEquals("2.60", round6);

		// 补0
		String round7 = NumberUtil.roundStr(2.600, 2);
		String round8 = NumberUtil.roundStr("2.600", 2);
		Assertions.assertEquals("2.60", round7);
		Assertions.assertEquals("2.60", round8);
	}

	@Test
	public void roundStrTest() {
		String roundStr = NumberUtil.roundStr(2.647, 2);
		Assertions.assertEquals(roundStr, "2.65");
	}

	@Test
	public void roundHalfEvenTest() {
		String roundStr = NumberUtil.roundHalfEven(4.245, 2).toString();
		Assertions.assertEquals(roundStr, "4.24");
		roundStr = NumberUtil.roundHalfEven(4.2450, 2).toString();
		Assertions.assertEquals(roundStr, "4.24");
		roundStr = NumberUtil.roundHalfEven(4.2451, 2).toString();
		Assertions.assertEquals(roundStr, "4.25");
		roundStr = NumberUtil.roundHalfEven(4.2250, 2).toString();
		Assertions.assertEquals(roundStr, "4.22");

		roundStr = NumberUtil.roundHalfEven(1.2050, 2).toString();
		Assertions.assertEquals(roundStr, "1.20");
		roundStr = NumberUtil.roundHalfEven(1.2150, 2).toString();
		Assertions.assertEquals(roundStr, "1.22");
		roundStr = NumberUtil.roundHalfEven(1.2250, 2).toString();
		Assertions.assertEquals(roundStr, "1.22");
		roundStr = NumberUtil.roundHalfEven(1.2350, 2).toString();
		Assertions.assertEquals(roundStr, "1.24");
		roundStr = NumberUtil.roundHalfEven(1.2450, 2).toString();
		Assertions.assertEquals(roundStr, "1.24");
		roundStr = NumberUtil.roundHalfEven(1.2550, 2).toString();
		Assertions.assertEquals(roundStr, "1.26");
		roundStr = NumberUtil.roundHalfEven(1.2650, 2).toString();
		Assertions.assertEquals(roundStr, "1.26");
		roundStr = NumberUtil.roundHalfEven(1.2750, 2).toString();
		Assertions.assertEquals(roundStr, "1.28");
		roundStr = NumberUtil.roundHalfEven(1.2850, 2).toString();
		Assertions.assertEquals(roundStr, "1.28");
		roundStr = NumberUtil.roundHalfEven(1.2950, 2).toString();
		Assertions.assertEquals(roundStr, "1.30");
	}

	@Test
	public void decimalFormatTest() {
		long c = 299792458;// 光速

		String format = NumberUtil.decimalFormat(",###", c);
		Assertions.assertEquals("299,792,458", format);
	}

	@Test
	public void decimalFormatNaNTest(){
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Double a = 0D;
			Double b = 0D;

			Double c = a / b;
			Console.log(NumberUtil.decimalFormat("#%", c));
		});
	}

	@Test
	public void decimalFormatNaNTest2(){
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Double a = 0D;
			Double b = 0D;

			Console.log(NumberUtil.decimalFormat("#%", a / b));
		});
	}

	@Test
	public void decimalFormatDoubleTest() {
		Double c = 467.8101;

		String format = NumberUtil.decimalFormat("0.00", c);
		Assertions.assertEquals("467.81", format);
	}

	@Test
	public void decimalFormatMoneyTest() {
		double c = 299792400.543534534;

		String format = NumberUtil.decimalFormatMoney(c);
		Assertions.assertEquals("299,792,400.54", format);

		double value = 0.5;
		String money = NumberUtil.decimalFormatMoney(value);
		Assertions.assertEquals("0.50", money);
	}

	@Test
	public void equalsTest() {
		Assertions.assertTrue(NumberUtil.equals(new BigDecimal("0.00"), BigDecimal.ZERO));
	}

	@Test
	public void toBigDecimalTest() {
		double a = 3.14;

		BigDecimal bigDecimal = NumberUtil.toBigDecimal(a);
		Assertions.assertEquals("3.14", bigDecimal.toString());

		bigDecimal = NumberUtil.toBigDecimal("1,234.55");
		Assertions.assertEquals("1234.55", bigDecimal.toString());

		bigDecimal = NumberUtil.toBigDecimal("1,234.56D");
		Assertions.assertEquals("1234.56", bigDecimal.toString());
	}

	@Test
	public void maxTest() {
		int max = NumberUtil.max(5,4,3,6,1);
		Assertions.assertEquals(6, max);
	}

	@Test
	public void minTest() {
		int min = NumberUtil.min(5,4,3,6,1);
		Assertions.assertEquals(1, min);
	}

	@Test
	public void parseIntTest() {
		int number = NumberUtil.parseInt("0xFF");
		Assertions.assertEquals(255, number);

		// 0开头
		number = NumberUtil.parseInt("010");
		Assertions.assertEquals(10, number);

		number = NumberUtil.parseInt("10");
		Assertions.assertEquals(10, number);

		number = NumberUtil.parseInt("   ");
		Assertions.assertEquals(0, number);

		number = NumberUtil.parseInt("10F");
		Assertions.assertEquals(10, number);

		number = NumberUtil.parseInt("22.4D");
		Assertions.assertEquals(22, number);

		number = NumberUtil.parseInt("22.6D");
		Assertions.assertEquals(22, number);

		number = NumberUtil.parseInt("0");
		Assertions.assertEquals(0, number);

		number = NumberUtil.parseInt(".123");
		Assertions.assertEquals(0, number);
	}

	@Test
	public void parseIntTest2() {
		// from 5.4.8 issue#I23ORQ@Gitee
		// 千位分隔符去掉
		int v1 = NumberUtil.parseInt("1,482.00");
		Assertions.assertEquals(1482, v1);
	}

	@Test
	public void parseNumberTest() {
		// from 5.4.8 issue#I23ORQ@Gitee
		// 千位分隔符去掉
		int v1 = NumberUtil.parseNumber("1,482.00").intValue();
		Assertions.assertEquals(1482, v1);

		Number v2 = NumberUtil.parseNumber("1,482.00D");
		Assertions.assertEquals(1482L, v2.longValue());
	}

	@Test
	public void parseLongTest() {
		long number = NumberUtil.parseLong("0xFF");
		Assertions.assertEquals(255, number);

		// 0开头
		number = NumberUtil.parseLong("010");
		Assertions.assertEquals(10, number);

		number = NumberUtil.parseLong("10");
		Assertions.assertEquals(10, number);

		number = NumberUtil.parseLong("   ");
		Assertions.assertEquals(0, number);

		number = NumberUtil.parseLong("10F");
		Assertions.assertEquals(10, number);

		number = NumberUtil.parseLong("22.4D");
		Assertions.assertEquals(22, number);

		number = NumberUtil.parseLong("22.6D");
		Assertions.assertEquals(22, number);

		number = NumberUtil.parseLong("0");
		Assertions.assertEquals(0, number);

		number = NumberUtil.parseLong(".123");
		Assertions.assertEquals(0, number);
	}

	@Test
	public void factorialTest(){
		long factorial = NumberUtil.factorial(0);
		Assertions.assertEquals(1, factorial);

		Assertions.assertEquals(1L, NumberUtil.factorial(1));
		Assertions.assertEquals(1307674368000L, NumberUtil.factorial(15));
		Assertions.assertEquals(2432902008176640000L, NumberUtil.factorial(20));

		factorial = NumberUtil.factorial(5, 0);
		Assertions.assertEquals(120, factorial);
		factorial = NumberUtil.factorial(5, 1);
		Assertions.assertEquals(120, factorial);

		Assertions.assertEquals(5, NumberUtil.factorial(5, 4));
		Assertions.assertEquals(2432902008176640000L, NumberUtil.factorial(20, 0));
	}

	@Test
	public void factorialTest2(){
		long factorial = NumberUtil.factorial(new BigInteger("0")).longValue();
		Assertions.assertEquals(1, factorial);

		Assertions.assertEquals(1L, NumberUtil.factorial(new BigInteger("1")).longValue());
		Assertions.assertEquals(1307674368000L, NumberUtil.factorial(new BigInteger("15")).longValue());
		Assertions.assertEquals(2432902008176640000L, NumberUtil.factorial(20));

		factorial = NumberUtil.factorial(new BigInteger("5"), new BigInteger("0")).longValue();
		Assertions.assertEquals(120, factorial);
		factorial = NumberUtil.factorial(new BigInteger("5"), BigInteger.ONE).longValue();
		Assertions.assertEquals(120, factorial);

		Assertions.assertEquals(5, NumberUtil.factorial(new BigInteger("5"), new BigInteger("4")).longValue());
		Assertions.assertEquals(2432902008176640000L, NumberUtil.factorial(new BigInteger("20"), BigInteger.ZERO).longValue());
	}

	@Test
	public void mulTest(){
		final BigDecimal mul = NumberUtil.mul(new BigDecimal("10"), null);
		Assertions.assertEquals(BigDecimal.ZERO, mul);
	}


	@Test
	public void isPowerOfTwoTest() {
		Assertions.assertFalse(NumberUtil.isPowerOfTwo(-1));
		Assertions.assertTrue(NumberUtil.isPowerOfTwo(16));
		Assertions.assertTrue(NumberUtil.isPowerOfTwo(65536));
		Assertions.assertTrue(NumberUtil.isPowerOfTwo(1));
		Assertions.assertFalse(NumberUtil.isPowerOfTwo(17));
	}

	@Test
	public void generateRandomNumberTest(){
		final int[] ints = NumberUtil.generateRandomNumber(10, 20, 5);
		Assertions.assertEquals(5, ints.length);
		final Set<?> set = Convert.convert(Set.class, ints);
		Assertions.assertEquals(5, set.size());
	}

	@Test
	public void toStrTest(){
		Assertions.assertEquals("1", NumberUtil.toStr(new BigDecimal("1.0000000000")));
		Assertions.assertEquals("0", NumberUtil.toStr(NumberUtil.sub(new BigDecimal("9600.00000"), new BigDecimal("9600.00000"))));
		Assertions.assertEquals("0", NumberUtil.toStr(NumberUtil.sub(new BigDecimal("9600.0000000000"), new BigDecimal("9600.000000"))));
		Assertions.assertEquals("0", NumberUtil.toStr(new BigDecimal("9600.00000").subtract(new BigDecimal("9600.000000000"))));
	}

	@Test
	public void generateRandomNumberTest2(){
		// 检查边界
		final int[] ints = NumberUtil.generateRandomNumber(1, 8, 7);
		Assertions.assertEquals(7, ints.length);
		final Set<?> set = Convert.convert(Set.class, ints);
		Assertions.assertEquals(7, set.size());
	}

	@Test
	public void toPlainNumberTest(){
		String num = "5344.34234e3";
		final String s = new BigDecimal(num).toPlainString();
		Assertions.assertEquals("5344342.34", s);
	}

	@Test
	public void generateBySetTest(){
		final Integer[] integers = NumberUtil.generateBySet(10, 100, 5);
		Assertions.assertEquals(5, integers.length);
	}

	@Test
	public void isOddOrEvenTest(){
		int[] a = { 0, 32, -32, 123, -123 };
		Assertions.assertFalse(NumberUtil.isOdd(a[0]));
		Assertions.assertTrue(NumberUtil.isEven(a[0]));

		Assertions.assertFalse(NumberUtil.isOdd(a[1]));
		Assertions.assertTrue(NumberUtil.isEven(a[1]));

		Assertions.assertFalse(NumberUtil.isOdd(a[2]));
		Assertions.assertTrue(NumberUtil.isEven(a[2]));

		Assertions.assertTrue(NumberUtil.isOdd(a[3]));
		Assertions.assertFalse(NumberUtil.isEven(a[3]));

		Assertions.assertTrue(NumberUtil.isOdd(a[4]));
		Assertions.assertFalse(NumberUtil.isEven(a[4]));
	}



}
