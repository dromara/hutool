package org.dromara.hutool.core.convert;

import org.dromara.hutool.core.convert.impl.NumberConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumberConverterTest {

	@Test
	public void toDoubleTest(){
		final NumberConverter numberConverter = new NumberConverter();
		final Number convert = numberConverter.convert(Double.class, "1,234.55", null);
		Assertions.assertEquals(1234.55D, convert);
	}

	@Test
	public void toIntegerTest(){
		final NumberConverter numberConverter = new NumberConverter();
		final Number convert = numberConverter.convert(Integer.class, "1,234.55", null);
		Assertions.assertEquals(1234, convert);
	}
}
