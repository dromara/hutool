package cn.hutool.core.convert;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.NumberChineseFormater;

public class NumberChineseFormaterTest {

	@Rule public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void formatTest() {
		String f1 = NumberChineseFormater.format(10889.72356, false);
		Assert.assertEquals("一万零八百八十九点七二", f1);
		f1 = NumberChineseFormater.format(12653, false);
		Assert.assertEquals("一万二千六百五十三", f1);
		f1 = NumberChineseFormater.format(215.6387, false);
		Assert.assertEquals("二百一十五点六四", f1);
		f1 = NumberChineseFormater.format(1024, false);
		Assert.assertEquals("一千零二十四", f1);
		f1 = NumberChineseFormater.format(100350089, false);
		Assert.assertEquals("一亿三十五万零八十九", f1);
		f1 = NumberChineseFormater.format(1200, false);
		Assert.assertEquals("一千二百", f1);
		f1 = NumberChineseFormater.format(12, false);
		Assert.assertEquals("一十二", f1);
		f1 = NumberChineseFormater.format(0.05, false);
		Assert.assertEquals("零点零五", f1);
	}
	
	@Test
	public void formatTest2() {
		String f1 = NumberChineseFormater.format(-0.3, false, false);
		Assert.assertEquals("负零点三", f1);
	}
	
	@Test
	public void formatTranditionalTest() {
		String f1 = NumberChineseFormater.format(10889.72356, true);
		Assert.assertEquals("壹万零捌佰捌拾玖点柒贰", f1);
		f1 = NumberChineseFormater.format(12653, true);
		Assert.assertEquals("壹万贰仟陆佰伍拾叁", f1);
		f1 = NumberChineseFormater.format(215.6387, true);
		Assert.assertEquals("贰佰壹拾伍点陆肆", f1);
		f1 = NumberChineseFormater.format(1024, true);
		Assert.assertEquals("壹仟零贰拾肆", f1);
		f1 = NumberChineseFormater.format(100350089, true);
		Assert.assertEquals("壹亿叁拾伍万零捌拾玖", f1);
		f1 = NumberChineseFormater.format(1200, true);
		Assert.assertEquals("壹仟贰佰", f1);
		f1 = NumberChineseFormater.format(12, true);
		Assert.assertEquals("壹拾贰", f1);
		f1 = NumberChineseFormater.format(0.05, true);
		Assert.assertEquals("零点零伍", f1);
	}
	
	@Test
	public void digitToChineseTest() {
		String digitToChinese = Convert.digitToChinese(12412412412421.12);
		Assert.assertEquals("壹拾贰万肆仟壹佰贰拾肆亿壹仟贰佰肆拾壹万贰仟肆佰贰拾壹元壹角贰分", digitToChinese);
		
		String digitToChinese2 = Convert.digitToChinese(12412412412421D);
		Assert.assertEquals("壹拾贰万肆仟壹佰贰拾肆亿壹仟贰佰肆拾壹万贰仟肆佰贰拾壹元整", digitToChinese2);
		
		String digitToChinese3 = Convert.digitToChinese(2421.02);
		Assert.assertEquals("贰仟肆佰贰拾壹元零贰分", digitToChinese3);
	}

	@Test
	public void formatInputPositiveTrueOutputIllegalArgumentException() {

		// Arrange
		final double amount = 0x1.7p+46 /* 1.01155e+14 */;
		final boolean isUseTraditional = true;

		// Act
		thrown.expect(IllegalArgumentException.class);
		NumberChineseFormater.format(amount, isUseTraditional);

		// Method is not expected to return due to exception thrown
	}

	@Test
	public void formatInputNegativeInfinityTrueFalseOutputIllegalArgumentException() {

		// Arrange
		final double amount = Double.NEGATIVE_INFINITY;
		final boolean isUseTraditional = true;
		final boolean isMoneyMode = false;

		// Act
		thrown.expect(IllegalArgumentException.class);
		NumberChineseFormater.format(amount, isUseTraditional, isMoneyMode);

		// Method is not expected to return due to exception thrown
	}

	@Test
	public void formatInputNegativeInfinityFalseFalseOutputIllegalArgumentException() {

		// Arrange
		final double amount = Double.NEGATIVE_INFINITY;
		final boolean isUseTraditional = false;
		final boolean isMoneyMode = false;

		// Act
		thrown.expect(IllegalArgumentException.class);
		NumberChineseFormater.format(amount, isUseTraditional, isMoneyMode);

		// Method is not expected to return due to exception thrown
	}

	@Test
	public void formatInputPositiveFalseFalseOutputIllegalArgumentException() {

		// Arrange
		final double amount = 0x1.47afp+1021 /* 2.87634e+307 */;
		final boolean isUseTraditional = false;
		final boolean isMoneyMode = false;

		// Act
		thrown.expect(IllegalArgumentException.class);
		NumberChineseFormater.format(amount, isUseTraditional, isMoneyMode);

		// Method is not expected to return due to exception thrown
	}
}
