package cn.hutool.core.convert;

import cn.hutool.core.convert.impl.NumberConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumberConverterTest {

	@Test
	public void toDoubleTest(){
		final NumberConverter numberConverter = new NumberConverter(Double.class);
		final Number convert = numberConverter.convert("1,234.55", null);
		Assertions.assertEquals(1234.55D, convert);
	}

	@Test
	public void toIntegerTest(){
		final NumberConverter numberConverter = new NumberConverter(Integer.class);
		final Number convert = numberConverter.convert("1,234.55", null);
		Assertions.assertEquals(1234, convert);
	}
}
