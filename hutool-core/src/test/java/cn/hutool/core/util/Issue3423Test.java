package cn.hutool.core.util;

import cn.hutool.core.lang.Console;
import org.junit.Ignore;
import org.junit.Test;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

public class Issue3423Test {

	@Test(expected = IllegalArgumentException.class)
	public void toBigDecimalOfNaNTest() {
		NumberUtil.toBigDecimal("NaN");
	}

	@Test
	@Ignore
	public void toBigDecimalOfNaNTest2() throws ParseException {
		final NumberFormat format = NumberFormat.getInstance();
		((DecimalFormat) format).setParseBigDecimal(true);
		final Number naN = format.parse("NaN");
		Console.log(naN.getClass());
	}
}
