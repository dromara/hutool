package cn.hutool.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Assert;
import org.junit.Test;

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
		Assert.assertEquals(7.37, result, 2);
	}
	
	@Test
	public void addTest2() {
		double a = 3.15f;
		double b = 4.22;
		double result = NumberUtil.add(a, b);
		Assert.assertEquals(7.37, result, 2);
	}
	
	@Test
	public void addTest3() {
		float a = 3.15f;
		double b = 4.22;
		double result = NumberUtil.add(a, b, a, b).doubleValue();
		Assert.assertEquals(14.74, result, 2);
	}
	
	@Test
	public void addTest4() {
		BigDecimal result = NumberUtil.add(new BigDecimal("133"), new BigDecimal("331"));
		Assert.assertEquals(new BigDecimal("464"), result);
	}
	
	@Test
	public void isIntegerTest() {
		Assert.assertTrue(NumberUtil.isInteger("-12"));
		Assert.assertTrue(NumberUtil.isInteger("256"));
		Assert.assertTrue(NumberUtil.isInteger("0256"));
		Assert.assertTrue(NumberUtil.isInteger("0"));
		Assert.assertFalse(NumberUtil.isInteger("23.4"));
	}
	
	@Test
	public void isLongTest() {
		Assert.assertTrue(NumberUtil.isLong("-12"));
		Assert.assertTrue(NumberUtil.isLong("256"));
		Assert.assertTrue(NumberUtil.isLong("0256"));
		Assert.assertTrue(NumberUtil.isLong("0"));
		Assert.assertFalse(NumberUtil.isLong("23.4"));
	}
	
	@Test
	public void isNumberTest() {
		Assert.assertTrue(NumberUtil.isNumber("28.55"));
		Assert.assertTrue(NumberUtil.isNumber("0"));
	}

	@Test
	public void divTest() {
		double result = NumberUtil.div(0, 1);
		Assert.assertEquals(0.0, result, 0);
	}
	
	@Test
	public void roundTest() {

		// 四舍
		String round1 = NumberUtil.roundStr(2.674, 2);
		String round2 = NumberUtil.roundStr("2.674", 2);
		Assert.assertEquals("2.67", round1);
		Assert.assertEquals("2.67", round2);

		// 五入
		String round3 = NumberUtil.roundStr(2.675, 2);
		String round4 = NumberUtil.roundStr("2.675", 2);
		Assert.assertEquals("2.68", round3);
		Assert.assertEquals("2.68", round4);
		
		// 四舍六入五成双
		String round31 = NumberUtil.roundStr(4.245, 2, RoundingMode.HALF_EVEN);
		String round41 = NumberUtil.roundStr("4.2451", 2, RoundingMode.HALF_EVEN);
		Assert.assertEquals("4.24", round31);
		Assert.assertEquals("4.25", round41);

		// 补0
		String round5 = NumberUtil.roundStr(2.6005, 2);
		String round6 = NumberUtil.roundStr("2.6005", 2);
		Assert.assertEquals("2.60", round5);
		Assert.assertEquals("2.60", round6);
		
		// 补0
		String round7 = NumberUtil.roundStr(2.600, 2);
		String round8 = NumberUtil.roundStr("2.600", 2);
		Assert.assertEquals("2.60", round7);
		Assert.assertEquals("2.60", round8);
	}

	@Test
	public void roundStrTest() {
		String roundStr = NumberUtil.roundStr(2.647, 2);
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
		long c = 299792458;// 光速

		String format = NumberUtil.decimalFormat(",###", c);
		Assert.assertEquals("299,792,458", format);
	}
	
	@Test
	public void decimalFormatMoneyTest() {
		double c = 299792400.543534534;
		
		String format = NumberUtil.decimalFormatMoney(c);
		Assert.assertEquals("299,792,400.54", format);
	}

	@Test
	public void equalsTest() {
		Assert.assertTrue(NumberUtil.equals(new BigDecimal("0.00"), BigDecimal.ZERO));
	}
	
	@Test
	public void formatPercentTest() {
		String str = NumberUtil.formatPercent(0.33543545, 2);
		Assert.assertEquals("33.54%", str);
	}
	
	@Test
	public void toBigDecimalTest() {
		double a = 3.14;
				
		BigDecimal bigDecimal = NumberUtil.toBigDecimal(a);
		Assert.assertEquals("3.14", bigDecimal.toString());
	}
	
	@Test
	public void maxTest() {
		int max = NumberUtil.max(new int[]{5,4,3,6,1});
		Assert.assertEquals(6, max);
	}
	
	@Test
	public void minTest() {
		int min = NumberUtil.min(new int[]{5,4,3,6,1});
		Assert.assertEquals(1, min);
	}
	
	@Test
	public void parseIntTest() {
		int v1 = NumberUtil.parseInt("0xFF");
		Assert.assertEquals(255, v1);
		int v2 = NumberUtil.parseInt("010");
		Assert.assertEquals(10, v2);
		int v3 = NumberUtil.parseInt("10");
		Assert.assertEquals(10, v3);
		int v4 = NumberUtil.parseInt("   ");
		Assert.assertEquals(0, v4);
		int v5 = NumberUtil.parseInt("10F");
		Assert.assertEquals(10, v5);
		int v6 = NumberUtil.parseInt("22.4D");
		Assert.assertEquals(22, v6);
	}
	
	@Test
	public void parseLongTest() {
		long v1 = NumberUtil.parseLong("0xFF");
		Assert.assertEquals(255L, v1);
		long v2 = NumberUtil.parseLong("010");
		Assert.assertEquals(10L, v2);
		long v3 = NumberUtil.parseLong("10");
		Assert.assertEquals(10L, v3);
		long v4 = NumberUtil.parseLong("   ");
		Assert.assertEquals(0L, v4);
		long v5 = NumberUtil.parseLong("10F");
		Assert.assertEquals(10L, v5);
		long v6 = NumberUtil.parseLong("22.4D");
		Assert.assertEquals(22L, v6);
	}
}
