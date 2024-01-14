package org.dromara.hutool.core.math;

import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

public class Issue3423Test {
	@Test
	public void toBigDecimalOfNaNTest() {
		final BigDecimal naN = NumberUtil.toBigDecimal("NaN");
		Assertions.assertEquals(BigDecimal.ZERO, naN);
	}

	@Test
	@Disabled
	public void toBigDecimalOfNaNTest2() throws ParseException {
		final NumberFormat format = NumberFormat.getInstance();
		((DecimalFormat) format).setParseBigDecimal(true);
		final Number naN = format.parse("NaN");
		Console.log(naN.getClass());
	}
}
