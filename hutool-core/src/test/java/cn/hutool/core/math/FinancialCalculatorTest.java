package cn.hutool.core.math;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author yhkang
 * @since 2021/9/22
 */
public class FinancialCalculatorTest {

	@Test
	public void testRate() {
		double result1 = FinancialCalculator.rate(12, 43200, -480000);
		Assert.assertTrue(Math.abs(result1 - 0.01204345678141910) < FinancialCalculator.FINANCIAL_PRECISION);

		double result2 = FinancialCalculator.rate(24, 17018.59, -400000);
		Assert.assertTrue(Math.abs(result2 - 0.00167844219973171) < FinancialCalculator.FINANCIAL_PRECISION);
	}
}
