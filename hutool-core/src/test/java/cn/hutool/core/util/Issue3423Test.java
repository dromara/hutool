package cn.hutool.core.util;

import cn.hutool.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class Issue3423Test {

	@Test
	public void toBigDecimalOfNaNTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			NumberUtil.toBigDecimal("NaN");
		});
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
