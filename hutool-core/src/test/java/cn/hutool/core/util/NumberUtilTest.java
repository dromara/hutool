package cn.hutool.core.util;

import java.math.BigDecimal;

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
	public void isIntegerTest() {
		Assert.assertTrue(NumberUtil.isInteger("-12"));
		Assert.assertTrue(NumberUtil.isInteger("256"));
		Assert.assertTrue(NumberUtil.isInteger("0256"));
	}
	
	@Test
	public void isNumberTest() {
		Assert.assertTrue(NumberUtil.isNumber("28.55"));
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
	public void decimalFormatTest() {
		long c = 299792458;// 光速

		String format = NumberUtil.decimalFormat(",###", c);
		Assert.assertEquals("299,792,458", format);
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
}
